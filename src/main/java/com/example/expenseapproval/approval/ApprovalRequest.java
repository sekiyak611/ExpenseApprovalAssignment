package com.example.expenseapproval.approval;

import com.example.expenseapproval.common.InputValidator;

/**
 * 承認依頼（承認時の入力内容）。生成時に入力不備を検証し、前後の空白を取り除いて保持する。
 *
 * @param applicationId 承認対象の申請ID
 * @param approverId 承認者ID
 */
public record ApprovalRequest(String applicationId, String approverId) {

    /**
     * @throws com.example.expenseapproval.common.InvalidInputException 入力不備がある場合
     */
    public ApprovalRequest {
        applicationId = InputValidator.requireText(applicationId, "申請ID");
        approverId = InputValidator.requireText(approverId, "承認者ID");
    }
}
