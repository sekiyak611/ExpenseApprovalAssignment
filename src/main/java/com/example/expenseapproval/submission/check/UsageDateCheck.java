package com.example.expenseapproval.submission.check;

import java.time.LocalDate;

import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.submission.SubmissionCheck;
import com.example.expenseapproval.submission.SubmissionErrorCode;

/**
 * 利用日が申請可能な期間内か。
 *
 * <p>基準日から指定日数さかのぼった日から基準日までを申請可能とする（両端を含む）。</p>
 */
public final class UsageDateCheck implements SubmissionCheck {

    /** 申請可能な、基準日からさかのぼる日数 */
    private final int acceptablePastDays;

    public UsageDateCheck(int acceptablePastDays) {
        if (acceptablePastDays < 0) {
            throw new IllegalArgumentException("日数は0以上を指定してください：" + acceptablePastDays);
        }
        this.acceptablePastDays = acceptablePastDays;
    }

    @Override
    public SubmissionErrorCode errorCode() {
        return SubmissionErrorCode.INVALID_DATE;
    }

    @Override
    public boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate) {
        LocalDate usageDate = application.usageDate();
        LocalDate earliestDate = baseDate.minusDays(acceptablePastDays);
        return !usageDate.isBefore(earliestDate) && !usageDate.isAfter(baseDate);
    }
}
