package com.example.expenseapproval.submission;

import java.time.LocalDate;

import com.example.expenseapproval.expense.ExpenseApplication;

/**
 * 申請受付時の業務チェック1項目。
 *
 * <p>チェック項目を追加する場合は本インタフェースの実装クラスを作成し、チェックの一覧に登録する。
 * 受付の処理（{@link SubmissionService}）の変更は不要。</p>
 */
public interface SubmissionCheck {

    /**
     * チェックに合格しなかった場合のエラーコード。
     */
    SubmissionErrorCode errorCode();

    /**
     * 経費申請がチェックに合格するか。
     *
     * @param application 入力不備のない経費申請
     * @param baseDate 基準日
     */
    boolean isSatisfiedBy(ExpenseApplication application, LocalDate baseDate);
}
