package com.example.expenseapproval.submission.check;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.InMemoryApplicationRepository;
import com.example.expenseapproval.expense.RegisteredApplication;
import com.example.expenseapproval.submission.SubmissionErrorCode;

class PendingLimitCheckTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final ApplicationRepository applicationRepository = new InMemoryApplicationRepository();

    private final PendingLimitCheck check = new PendingLimitCheck(applicationRepository, 5);

    private final ExpenseApplication newApplication = anApplication().applicationId("NEW").applicantId("U001").build();

    @Test
    void 承認待ちが4件なら合格() {
        registerPending("U001", 4);

        assertTrue(check.isSatisfiedBy(newApplication, BASE_DATE));
    }

    @Test
    void 承認待ちが5件なら不合格() {
        registerPending("U001", 5);

        assertFalse(check.isSatisfiedBy(newApplication, BASE_DATE));
    }

    @Test
    void 承認済みの申請は件数に含めない() {
        registerPending("U001", 5);
        RegisteredApplication first = applicationRepository.findById("U001-1").orElseThrow();
        applicationRepository.save(first.approve("U002"));

        assertTrue(check.isSatisfiedBy(newApplication, BASE_DATE));
    }

    @Test
    void 他の申請者の承認待ち申請は件数に含めない() {
        registerPending("U002", 5);

        assertTrue(check.isSatisfiedBy(newApplication, BASE_DATE));
    }

    @Test
    void 不合格時のエラーコードはPENDING_LIMIT() {
        assertEquals(SubmissionErrorCode.PENDING_LIMIT, check.errorCode());
    }

    /**
     * 申請ID「申請者ID-連番」で承認待ちの申請を登録する。
     */
    private void registerPending(String applicantId, int count) {
        for (int i = 1; i <= count; i++) {
            ExpenseApplication application = anApplication()
                    .applicationId(applicantId + "-" + i)
                    .applicantId(applicantId)
                    .build();
            applicationRepository.save(RegisteredApplication.pending(application));
        }
    }
}
