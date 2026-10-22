package com.example.expenseapproval.submission.check;

import java.time.LocalDate;

import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.submission.SubmissionCheck;
import com.example.expenseapproval.submission.SubmissionErrorCode;

/**
 * 必要な領収書があるか。領収書の要否は {@link com.example.expenseapproval.expense.ExpenseCategory} に定義する。
 */
public final class ReceiptCheck implements SubmissionCheck {

    @Override
    public SubmissionErrorCode errorCode() {
        return SubmissionErrorCode.RECEIPT_REQUIRED;
    }

    @Override
    public boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate) {
        return application.hasReceipt() || !application.category().requiresReceipt(application.amount());
    }
}
