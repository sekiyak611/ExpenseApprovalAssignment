package com.example.expenseapproval.submission.check;

import java.util.List;

import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.submission.SubmissionCheck;

/**
 * 現行の申請受付チェックの定義。
 *
 * <p>どのチェックを、どの順序・どの値で実施するかを本クラスに集約する。
 * 経費区分ごとのルール（上限金額・領収書の要否）は {@link com.example.expenseapproval.expense.ExpenseCategory} に定義している。</p>
 */
public final class SubmissionChecks {

    /** 利用日として申請可能な、基準日からさかのぼる日数 */
    private static final int ACCEPTABLE_PAST_DAYS = 30;

    /** 申請者1人あたりの承認待ち申請の上限件数 */
    private static final int PENDING_LIMIT = 5;

    private SubmissionChecks() {
    }

    /**
     * 現行の申請受付チェックを実施順に返す。
     */
    public static List<SubmissionCheck> standard(EmployeeRepository employeeRepository,
            ApplicationRepository applicationRepository) {
        return List.of(
                new ApplicantExistenceCheck(employeeRepository),
                new UniqueApplicationIdCheck(applicationRepository),
                new UsageDateCheck(ACCEPTABLE_PAST_DAYS),
                new AmountLimitCheck(),
                new ReceiptCheck(),
                new PendingLimitCheck(applicationRepository, PENDING_LIMIT));
    }
}
