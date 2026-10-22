package com.example.expenseapproval.approval;

import com.example.expenseapproval.expense.ApplicationStatus;

/**
 * 承認の結果。
 */
public sealed interface ApprovalResult {

    String applicationId();

    /**
     * 承認成功。
     *
     * @param applicationId 申請ID
     * @param status 承認後の申請状態
     * @param approverId 承認者ID
     */
    record Approved(String applicationId, ApplicationStatus status, String approverId) implements ApprovalResult {
    }

    /**
     * 業務チェックのエラーによる承認失敗（登録内容は変更していない）。
     *
     * @param applicationId 申請ID
     * @param error 最初に検出したエラーコード
     */
    record Failed(String applicationId, ApprovalErrorCode error) implements ApprovalResult {
    }
}
