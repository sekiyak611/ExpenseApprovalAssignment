package com.example.expenseapproval.submission.check;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.expenseapproval.submission.SubmissionErrorCode;

class UsageDateCheckTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final UsageDateCheck check = new UsageDateCheck(30);

    @ParameterizedTest(name = "{0}（{1}）：合格={2}")
    @CsvSource({
            "基準日当日,   2026-10-05, true",
            "基準日の翌日, 2026-10-06, false",
            "30日前,       2026-09-05, true",
            "31日前,       2026-09-04, false",
    })
    void 基準日の30日前から基準日までを申請可能とする(String description, LocalDate usageDate, boolean expected) {
        assertEquals(expected, check.isSatisfiedBy(anApplication().usageDate(usageDate).build(), BASE_DATE));
    }

    @Test
    void 不合格時のエラーコードはINVALID_DATE() {
        assertEquals(SubmissionErrorCode.INVALID_DATE, check.errorCode());
    }
}
