package com.example.expenseapproval.approval;

/**
 * 承認時の業務チェックのエラーコード。
 */
public enum ApprovalErrorCode {

    /** 申請が存在しない */
    APPLICATION_NOT_FOUND,

    /** 承認者が存在しない */
    APPROVER_NOT_FOUND,

    /** 承認者に承認権限がない */
    NOT_AUTHORIZED,

    /** 申請者と承認者が同一 */
    SELF_APPROVAL,

    /** 申請状態が承認待ちではない */
    INVALID_STATUS
}
