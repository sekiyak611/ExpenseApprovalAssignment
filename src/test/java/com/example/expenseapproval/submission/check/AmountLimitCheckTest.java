package com.example.expenseapproval.submission.check;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.ExpenseCategory;
import com.example.expenseapproval.submission.SubmissionErrorCode;

class AmountLimitCheckTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final AmountLimitCheck check = new AmountLimitCheck();

    @ParameterizedTest(name = "{0}：上限{1}円")
    @CsvSource({
            "TRANSPORT, 20000",
            "LODGING,   30000",
            "SUPPLIES,  50000",
            "MEETING,   10000",
    })
    void 上限と同額は合格し1円超過は不合格(ExpenseCategory category, int amountLimit) {
        assertAll(
                () -> assertTrue(check.isSatisfiedBy(application(category, amountLimit), BASE_DATE)),
                () -> assertFalse(check.isSatisfiedBy(application(category, amountLimit + 1), BASE_DATE)));
    }

    @Test
    void 不合格時のエラーコードはAMOUNT_LIMIT() {
        assertEquals(SubmissionErrorCode.AMOUNT_LIMIT, check.errorCode());
    }

    private static ExpenseApplication application(ExpenseCategory category, int amount) {
        return anApplication().category(category).amount(amount).build();
    }
}
