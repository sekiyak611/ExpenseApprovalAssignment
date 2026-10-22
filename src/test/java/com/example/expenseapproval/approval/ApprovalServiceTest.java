package com.example.expenseapproval.approval;

import static com.example.expenseapproval.approval.ApprovalErrorCode.*;
import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.expenseapproval.approval.rule.ApprovalRules;
import com.example.expenseapproval.common.InvalidInputException;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.employee.EmployeeType;
import com.example.expenseapproval.employee.InMemoryEmployeeRepository;
import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ApplicationStatus;
import com.example.expenseapproval.expense.InMemoryApplicationRepository;
import com.example.expenseapproval.expense.RegisteredApplication;

class ApprovalServiceTest {

    private final EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();

    private final ApplicationRepository applicationRepository = new InMemoryApplicationRepository();

    private final ApprovalService service = new ApprovalService(employeeRepository, applicationRepository,
            ApprovalRules.standard());

    @BeforeEach
    void setUp() {
        employeeRepository.save(new Employee("U001", "山田 太郎", EmployeeType.GENERAL));
        employeeRepository.save(new Employee("U002", "佐藤 花子", EmployeeType.MANAGER));
        employeeRepository.save(new Employee("U003", "鈴木 一郎", EmployeeType.MANAGER));
        registerPending("A001", "U001");
        registerPending("A002", "U002");
    }

    @Test
    void 確認ケース_管理職が承認すると承認済みになり承認者が設定される() {
        ApprovalResult result = service.approve(new ApprovalRequest("A001", "U002"));

        assertEquals(new ApprovalResult.Approved("A001", ApplicationStatus.APPROVED, "U002"), result);
        RegisteredApplication registered = find("A001");
        assertEquals(ApplicationStatus.APPROVED, registered.status());
        assertEquals(Optional.of("U002"), registered.approverId());
    }

    @Test
    void 確認ケース_承認済みの申請を再度承認するとINVALID_STATUSで登録内容は変更しない() {
        service.approve(new ApprovalRequest("A001", "U002"));

        ApprovalResult result = service.approve(new ApprovalRequest("A001", "U003"));

        assertEquals(new ApprovalResult.Failed("A001", INVALID_STATUS), result);
        RegisteredApplication registered = find("A001");
        assertEquals(ApplicationStatus.APPROVED, registered.status());
        assertEquals(Optional.of("U002"), registered.approverId());
    }

    @Test
    void 申請が存在しない場合はAPPLICATION_NOT_FOUND() {
        ApprovalResult result = service.approve(new ApprovalRequest("A999", "U002"));

        assertEquals(new ApprovalResult.Failed("A999", APPLICATION_NOT_FOUND), result);
        assertTrue(applicationRepository.findById("A999").isEmpty());
    }

    @Test
    void 承認者が存在しない場合はAPPROVER_NOT_FOUNDで登録内容は変更しない() {
        ApprovalResult result = service.approve(new ApprovalRequest("A001", "U999"));

        assertEquals(new ApprovalResult.Failed("A001", APPROVER_NOT_FOUND), result);
        assertPendingWithoutApprover("A001");
    }

    @Test
    void 承認者が管理職でない場合はNOT_AUTHORIZEDで登録内容は変更しない() {
        ApprovalResult result = service.approve(new ApprovalRequest("A002", "U001"));

        assertEquals(new ApprovalResult.Failed("A002", NOT_AUTHORIZED), result);
        assertPendingWithoutApprover("A002");
    }

    @Test
    void 申請者本人が承認する場合はSELF_APPROVALで登録内容は変更しない() {
        ApprovalResult result = service.approve(new ApprovalRequest("A002", "U002"));

        assertEquals(new ApprovalResult.Failed("A002", SELF_APPROVAL), result);
        assertPendingWithoutApprover("A002");
    }

    @Test
    void 申請と承認者がともに存在しない場合は申請の不在のみ返す() {
        assertEquals(new ApprovalResult.Failed("A999", APPLICATION_NOT_FOUND),
                service.approve(new ApprovalRequest("A999", "U999")));
    }

    @Test
    void 一般社員が自分の申請を承認する場合は権限なしのみ返す() {
        assertEquals(new ApprovalResult.Failed("A001", NOT_AUTHORIZED),
                service.approve(new ApprovalRequest("A001", "U001")));
    }

    @Test
    void 承認済みの自分の申請を承認する場合は自己承認のみ返す() {
        service.approve(new ApprovalRequest("A002", "U003"));

        assertEquals(new ApprovalResult.Failed("A002", SELF_APPROVAL),
                service.approve(new ApprovalRequest("A002", "U002")));
    }

    @Test
    void IDの前後の空白を取り除いて承認する() {
        ApprovalResult result = service.approve(new ApprovalRequest(" A001 ", "　U002　"));

        assertEquals(new ApprovalResult.Approved("A001", ApplicationStatus.APPROVED, "U002"), result);
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = "  ")
    void IDが未入力の場合は入力不備(String blankId) {
        assertThrows(InvalidInputException.class, () -> new ApprovalRequest(blankId, "U002"));
        assertThrows(InvalidInputException.class, () -> new ApprovalRequest("A001", blankId));
    }

    @Test
    void 承認依頼が未指定の場合は入力不備() {
        assertThrows(InvalidInputException.class, () -> service.approve(null));
    }

    private void registerPending(String applicationId, String applicantId) {
        applicationRepository.save(RegisteredApplication.pending(
                anApplication().applicationId(applicationId).applicantId(applicantId).build()));
    }

    private RegisteredApplication find(String applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow();
    }

    private void assertPendingWithoutApprover(String applicationId) {
        RegisteredApplication registered = find(applicationId);
        assertEquals(ApplicationStatus.PENDING, registered.status());
        assertEquals(Optional.empty(), registered.approverId());
    }
}
