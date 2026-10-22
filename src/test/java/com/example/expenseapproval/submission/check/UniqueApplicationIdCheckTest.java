package com.example.expenseapproval.submission.check;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.InMemoryApplicationRepository;
import com.example.expenseapproval.expense.RegisteredApplication;
import com.example.expenseapproval.submission.SubmissionErrorCode;

class UniqueApplicationIdCheckTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final ApplicationRepository applicationRepository = new InMemoryApplicationRepository();

    private final UniqueApplicationIdCheck check = new UniqueApplicationIdCheck(applicationRepository);

    @Test
    void 申請IDが未登録なら合格() {
        assertTrue(check.isSatisfiedBy(anApplication().applicationId("A001").build(), BASE_DATE));
    }

    @Test
    void 申請IDが登録済みなら不合格() {
        applicationRepository.save(RegisteredApplication.pending(anApplication().applicationId("A001").build()));

        assertFalse(check.isSatisfiedBy(anApplication().applicationId("A001").build(), BASE_DATE));
    }

    @Test
    void 不合格時のエラーコードはDUPLICATE_ID() {
        assertEquals(SubmissionErrorCode.DUPLICATE_ID, check.errorCode());
    }
}
