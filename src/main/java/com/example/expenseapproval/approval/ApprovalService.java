package com.example.expenseapproval.approval;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.example.expenseapproval.common.InputValidator;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.RegisteredApplication;

/**
 * 登録済み申請を承認する。
 *
 * <p>申請・承認者の存在を確認した後に承認ルールを順に判定し、最初に見つかったエラー1件を返す。
 * 存在確認は以降のルール判定の前提（判定対象の特定）となるため本クラスで行い、
 * 存在確認後の業務ルールは {@link ApprovalRule} として追加・変更できるようにしている。</p>
 */
public final class ApprovalService {

    private final EmployeeRepository employeeRepository;

    private final ApplicationRepository applicationRepository;

    private final List<ApprovalRule> rules;

    /**
     * @param employeeRepository 社員一覧
     * @param applicationRepository 登録済み申請の保存先
     * @param rules 承認ルール。リストの順に判定する
     */
    public ApprovalService(EmployeeRepository employeeRepository, ApplicationRepository applicationRepository,
            List<ApprovalRule> rules) {
        this.employeeRepository = Objects.requireNonNull(employeeRepository);
        this.applicationRepository = Objects.requireNonNull(applicationRepository);
        this.rules = List.copyOf(rules);
    }

    /**
     * 申請を承認する。エラーがあれば登録内容（状態・承認者）を変更しない。
     *
     * @throws com.example.expenseapproval.common.InvalidInputException 承認依頼が未指定の場合
     */
    public ApprovalResult approve(ApprovalRequest request) {
        InputValidator.requireNonNull(request, "承認依頼");

        Optional<RegisteredApplication> application = applicationRepository.findById(request.applicationId());
        if (application.isEmpty()) {
            return new ApprovalResult.Failed(request.applicationId(), ApprovalErrorCode.APPLICATION_NOT_FOUND);
        }
        Optional<Employee> approver = employeeRepository.findById(request.approverId());
        if (approver.isEmpty()) {
            return new ApprovalResult.Failed(request.applicationId(), ApprovalErrorCode.APPROVER_NOT_FOUND);
        }
        return approve(application.get(), approver.get());
    }

    private ApprovalResult approve(RegisteredApplication application, Employee approver) {
        Optional<ApprovalErrorCode> violation = rules.stream()
                .filter(rule -> !rule.isSatisfiedBy(application, approver))
                .map(ApprovalRule::errorCode)
                .findFirst();
        if (violation.isPresent()) {
            return new ApprovalResult.Failed(application.applicationId(), violation.get());
        }

        RegisteredApplication approved = application.approve(approver.id());
        applicationRepository.save(approved);
        return new ApprovalResult.Approved(approved.applicationId(), approved.status(), approver.id());
    }
}
