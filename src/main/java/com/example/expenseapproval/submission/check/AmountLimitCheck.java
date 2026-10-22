package com.example.expenseapproval.submission.check;

import java.time.LocalDate;

import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.submission.SubmissionCheck;
import com.example.expenseapproval.submission.SubmissionErrorCode;

/**
 * 金額が経費区分ごとの1申請あたりの上限以内か。上限金額は {@link com.example.expenseapproval.expense.ExpenseCategory} に定義する。
 */
public final class AmountLimitCheck implements SubmissionCheck {

    @Override
    public SubmissionErrorCode errorCode() {
        return SubmissionErrorCode.AMOUNT_LIMIT;
    }

    @Override
    public boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate) {
        return !application.category().exceedsAmountLimit(application.amount());
    }
}
