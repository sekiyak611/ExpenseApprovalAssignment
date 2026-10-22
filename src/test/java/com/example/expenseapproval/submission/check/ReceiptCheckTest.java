package com.example.expenseapproval.submission.check;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.ExpenseCategory;
import com.example.expenseapproval.submission.SubmissionErrorCode;

class ReceiptCheckTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final ReceiptCheck check = new ReceiptCheck();

    @Test
    void 交通費は9999円なら領収書なしで合格() {
        assertTrue(check.isSatisfiedBy(application(ExpenseCategory.TRANSPORT, 9_999, false), BASE_DATE));
    }

    @Test
    void 交通費は10000円以上なら領収書なしで不合格() {
        assertFalse(check.isSatisfiedBy(application(ExpenseCategory.TRANSPORT, 10_000, false), BASE_DATE));
    }

    @Test
    void 交通費は10000円以上でも領収書があれば合格() {
        assertTrue(check.isSatisfiedBy(application(ExpenseCategory.TRANSPORT, 10_000, true), BASE_DATE));
    }

    @ParameterizedTest
    @EnumSource(value = ExpenseCategory.class, names = { "LODGING", "SUPPLIES", "MEETING" })
    void 宿泊費_備品費_会議費は金額にかかわらず領収書なしで不合格(ExpenseCategory category) {
        assertFalse(check.isSatisfiedBy(application(category, 1, false), BASE_DATE));
    }

    @ParameterizedTest
    @EnumSource(ExpenseCategory.class)
    void 領収書があればどの経費区分でも合格(ExpenseCategory category) {
        assertTrue(check.isSatisfiedBy(application(category, 10_000, true), BASE_DATE));
    }

    @Test
    void 不合格時のエラーコードはRECEIPT_REQUIRED() {
        assertEquals(SubmissionErrorCode.RECEIPT_REQUIRED, check.errorCode());
    }

    private static ExpenseApplication application(ExpenseCategory category, int amount, boolean hasReceipt) {
        return anApplication().category(category).amount(amount).hasReceipt(hasReceipt).build();
    }
}
