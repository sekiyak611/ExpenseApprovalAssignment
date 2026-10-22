package com.example.expenseapproval;

import java.time.LocalDate;

import com.example.expenseapproval.approval.ApprovalRequest;
import com.example.expenseapproval.approval.ApprovalService;
import com.example.expenseapproval.approval.rule.ApprovalRules;
import com.example.expenseapproval.common.InvalidInputException;
import com.example.expenseapproval.console.ResultPrinter;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.employee.EmployeeType;
import com.example.expenseapproval.employee.InMemoryEmployeeRepository;
import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.ExpenseCategory;
import com.example.expenseapproval.expense.InMemoryApplicationRepository;
import com.example.expenseapproval.submission.SubmissionService;
import com.example.expenseapproval.submission.check.SubmissionChecks;

/**
 * 動作確認用のエントリポイント。課題の確認ケースをサンプルデータで実行する。
 */
public final class Main {

    /** 確認ケースの基準日 */
    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private Main() {
    }

    public static void main(String[] args) {
        EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();
        employeeRepository.save(new Employee("U001", "山田 太郎", EmployeeType.GENERAL));
        employeeRepository.save(new Employee("U002", "佐藤 花子", EmployeeType.MANAGER));

        ApplicationRepository applicationRepository = new InMemoryApplicationRepository();
        SubmissionService submissionService = new SubmissionService(applicationRepository,
                SubmissionChecks.standard(employeeRepository, applicationRepository));
        ApprovalService approvalService = new ApprovalService(employeeRepository, applicationRepository,
                ApprovalRules.standard());
        ResultPrinter printer = new ResultPrinter(System.out);

        // 確認ケース：受付成功（PENDING）となり、U002 が承認すると APPROVED
        ExpenseApplication transport = new ExpenseApplication(
                "A001", "U001", LocalDate.of(2026, 10, 1), ExpenseCategory.TRANSPORT, 9_999, false, "客先訪問");
        printer.printSubmissionResult(submissionService.submit(transport, BASE_DATE));
        printer.printApprovalResult(approvalService.approve(new ApprovalRequest("A001", "U002")));

        // 確認ケース：該当するエラーをすべて返し、登録しない
        ExpenseApplication lodging = new ExpenseApplication(
                "A002", "U001", LocalDate.of(2026, 10, 6), ExpenseCategory.LODGING, 30_001, false, "出張");
        printer.printSubmissionResult(submissionService.submit(lodging, BASE_DATE));

        // 確認ケース：承認済みの申請を再度承認すると INVALID_STATUS
        printer.printApprovalResult(approvalService.approve(new ApprovalRequest("A001", "U002")));

        // 承認権限なし・自己承認（会議費は追加仕様の経費区分）
        ExpenseApplication meeting = new ExpenseApplication(
                "A003", "U002", LocalDate.of(2026, 10, 5), ExpenseCategory.MEETING, 8_000, true, "取引先との打合せ");
        printer.printSubmissionResult(submissionService.submit(meeting, BASE_DATE));
        printer.printApprovalResult(approvalService.approve(new ApprovalRequest("A003", "U001")));
        printer.printApprovalResult(approvalService.approve(new ApprovalRequest("A003", "U002")));

        // 入力不備は例外で拒否する
        try {
            ExpenseApplication blankPurpose = new ExpenseApplication(
                    "A004", "U001", LocalDate.of(2026, 10, 5), ExpenseCategory.SUPPLIES, 3_000, true, "   ");
            printer.printSubmissionResult(submissionService.submit(blankPurpose, BASE_DATE));
        } catch (InvalidInputException e) {
            printer.printInputError(e);
        }
    }
}
