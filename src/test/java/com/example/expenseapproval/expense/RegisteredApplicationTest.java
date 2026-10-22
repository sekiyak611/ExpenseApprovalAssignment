package com.example.expenseapproval.expense;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class RegisteredApplicationTest {

    private final ExpenseApplication application = anApplication().applicationId("A001").applicantId("U001").build();

    @Test
    void 登録時は承認待ちで承認者は未設定() {
        RegisteredApplication registered = RegisteredApplication.pending(application);

        assertEquals(ApplicationStatus.PENDING, registered.status());
        assertTrue(registered.isPending());
        assertEquals(Optional.empty(), registered.approverId());
    }

    @Test
    void 承認すると承認済みになり承認者が設定される() {
        RegisteredApplication approved = RegisteredApplication.pending(application).approve("U002");

        assertEquals(ApplicationStatus.APPROVED, approved.status());
        assertFalse(approved.isPending());
        assertEquals(Optional.of("U002"), approved.approverId());
        assertSame(application, approved.application());
    }

    @Test
    void 承認しても元の登録済み申請は変更しない() {
        RegisteredApplication pending = RegisteredApplication.pending(application);

        pending.approve("U002");

        assertEquals(ApplicationStatus.PENDING, pending.status());
        assertEquals(Optional.empty(), pending.approverId());
    }

    @Test
    void 承認待ちではない申請は承認できない() {
        RegisteredApplication approved = RegisteredApplication.pending(application).approve("U002");

        assertThrows(IllegalStateException.class, () -> approved.approve("U003"));
    }

    @Test
    void 指定の社員の申請か判定できる() {
        RegisteredApplication registered = RegisteredApplication.pending(application);

        assertTrue(registered.isSubmittedBy("U001"));
        assertFalse(registered.isSubmittedBy("U002"));
    }
}
