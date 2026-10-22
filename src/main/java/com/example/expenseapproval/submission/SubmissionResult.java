package com.example.expenseapproval.submission;

import java.util.List;

import com.example.expenseapproval.expense.ApplicationStatus;

/**
 * 申請受付の結果。
 */
public sealed interface SubmissionResult {

    String applicationId();

    /**
     * 受付成功（登録済み）。
     *
     * @param applicationId 申請ID
     * @param status 登録時の申請状態
     */
    record Accepted(String applicationId, ApplicationStatus status) implements SubmissionResult {
    }

    /**
     * 業務チェックのエラーによる受付不可（未登録）。
     *
     * @param applicationId 申請ID
     * @param errors 検出したエラーコード（チェックの実施順）
     */
    record Rejected(String applicationId, List<SubmissionErrorCode> errors) implements SubmissionResult {

        public Rejected {
            if (errors.isEmpty()) {
                throw new IllegalArgumentException("受付不可の結果にはエラーコードが1件以上必要です");
            }
            errors = List.copyOf(errors);
        }
    }
}
