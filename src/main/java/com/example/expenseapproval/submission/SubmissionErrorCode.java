package com.example.expenseapproval.submission;

/**
 * 申請受付時の業務チェックのエラーコード。
 */
public enum SubmissionErrorCode {

    /** 申請者IDが社員一覧に存在しない */
    APPLICANT_NOT_FOUND,

    /** 申請IDが登録済み申請と重複する */
    DUPLICATE_ID,

    /** 利用日が申請可能な期間外 */
    INVALID_DATE,

    /** 経費区分ごとの1申請あたりの上限金額を超える */
    AMOUNT_LIMIT,

    /** 必要な領収書がない */
    RECEIPT_REQUIRED,

    /** 申請者の承認待ち申請が上限件数に達している（追加仕様） */
    PENDING_LIMIT
}
