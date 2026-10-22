package com.example.expenseapproval.submission;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static com.example.expenseapproval.submission.SubmissionErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.expenseapproval.common.InvalidInputException;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.employee.EmployeeType;
import com.example.expenseapproval.employee.InMemoryEmployeeRepository;
import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ApplicationStatus;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.ExpenseCategory;
import com.example.expenseapproval.expense.InMemoryApplicationRepository;
import com.example.expenseapproval.expense.RegisteredApplication;
import com.example.expenseapproval.submission.check.AmountLimitCheck;
import com.example.expenseapproval.submission.check.ReceiptCheck;
import com.example.expenseapproval.submission.check.SubmissionChecks;

class SubmissionServiceTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();

    private final ApplicationRepository applicationRepository = new InMemoryApplicationRepository();

    private final SubmissionService service = new SubmissionService(applicationRepository,
            SubmissionChecks.standard(employeeRepository, applicationRepository));

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("U001", "山田 太郎", EmployeeType.GENERAL));
        employeeRepository.save(new Employee("U002", "佐藤 花子", EmployeeType.MANAGER));
    }

    @Test
    void 確認ケースA001_業務チェックに合格した申請を承認待ちで登録する() {
        ExpenseApplication application = anApplication()
                .applicationId("A001").applicantId("U001").usageDate(LocalDate.of(2026, 10, 1))
                .category(ExpenseCategory.TRANSPORT).amount(9_999).hasReceipt(false)
                .build();

        SubmissionResult result = service.submit(application, BASE_DATE);

        assertEquals(new SubmissionResult.Accepted("A001", ApplicationStatus.PENDING), result);
        RegisteredApplication registered = applicationRepository.findById("A001").orElseThrow();
        assertSame(application, registered.application());
        assertEquals(ApplicationStatus.PENDING, registered.status());
        assertEquals(Optional.empty(), registered.approverId());
    }

    @Test
    void 確認ケースA002_該当するエラーをすべてチェック順に返し登録しない() {
        ExpenseApplication application = anApplication()
                .applicationId("A002").applicantId("U001").usageDate(LocalDate.of(2026, 10, 6))
                .category(ExpenseCategory.LODGING).amount(30_001).hasReceipt(false)
                .build();

        SubmissionResult result = service.submit(application, BASE_DATE);

        assertEquals(new SubmissionResult.Rejected("A002", List.of(INVALID_DATE, AMOUNT_LIMIT, RECEIPT_REQUIRED)),
                result);
        assertTrue(applicationRepository.findById("A002").isEmpty());
    }

    @Test
    void 申請者不在と申請ID重複も含めてエラーをすべて返し登録済み申請を変更しない() {
        ExpenseApplication registered = anApplication().applicationId("A001").applicantId("U001").build();
        service.submit(registered, BASE_DATE);
        ExpenseApplication duplicated = anApplication()
                .applicationId("A001").applicantId("U999").usageDate(LocalDate.of(2026, 9, 4))
                .category(ExpenseCategory.SUPPLIES).amount(50_001).hasReceipt(false)
                .build();

        SubmissionResult result = service.submit(duplicated, BASE_DATE);

        assertEquals(new SubmissionResult.Rejected("A001",
                List.of(APPLICANT_NOT_FOUND, DUPLICATE_ID, INVALID_DATE, AMOUNT_LIMIT, RECEIPT_REQUIRED)), result);
        assertSame(registered, applicationRepository.findById("A001").orElseThrow().application());
    }

    @Test
    void 承認待ち申請が5件ある申請者の新規申請は受け付けない() {
        submitPendingApplications(5);

        SubmissionResult result = service.submit(anApplication().applicationId("A006").build(), BASE_DATE);

        assertEquals(new SubmissionResult.Rejected("A006", List.of(PENDING_LIMIT)), result);
        assertTrue(applicationRepository.findById("A006").isEmpty());
    }

    @Test
    void 承認待ち件数の上限はチェックの6番目に判定する() {
        submitPendingApplications(5);
        ExpenseApplication application = anApplication()
                .applicationId("A001").usageDate(LocalDate.of(2026, 10, 6))
                .category(ExpenseCategory.MEETING).amount(10_001).hasReceipt(false)
                .build();

        SubmissionResult result = service.submit(application, BASE_DATE);

        assertEquals(new SubmissionResult.Rejected("A001",
                List.of(DUPLICATE_ID, INVALID_DATE, AMOUNT_LIMIT, RECEIPT_REQUIRED, PENDING_LIMIT)), result);
    }

    @Test
    void 業務チェックは指定したリストの順に実施しエラーコードもその順で返す() {
        SubmissionService reordered = new SubmissionService(applicationRepository,
                List.of(new ReceiptCheck(), new AmountLimitCheck()));
        ExpenseApplication application = anApplication()
                .category(ExpenseCategory.LODGING).amount(30_001).hasReceipt(false)
                .build();

        SubmissionResult result = reordered.submit(application, BASE_DATE);

        assertEquals(new SubmissionResult.Rejected("A001", List.of(RECEIPT_REQUIRED, AMOUNT_LIMIT)), result);
    }

    @Test
    void 経費申請や基準日が未指定の場合は入力不備() {
        ExpenseApplication application = anApplication().build();

        assertThrows(InvalidInputException.class, () -> service.submit(null, BASE_DATE));
        assertThrows(InvalidInputException.class, () -> service.submit(application, null));
        assertTrue(applicationRepository.findById("A001").isEmpty());
    }

    /**
     * 申請者 U001 の承認待ち申請を、申請ID A001 から連番で登録する。
     */
    private void submitPendingApplications(int count) {
        for (int i = 1; i <= count; i++) {
            ExpenseApplication application = anApplication().applicationId("A00" + i).applicantId("U001").build();
            assertInstanceOf(SubmissionResult.Accepted.class, service.submit(application, BASE_DATE));
        }
    }
}
