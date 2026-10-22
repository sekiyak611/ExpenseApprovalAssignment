package com.example.expenseapproval.common;

/**
 * 入力不備（必須項目の未入力、範囲外の値など）を表す例外。
 *
 * <p>業務チェックのエラーはエラーコードとして結果で返し、入力不備は本例外で呼出し元へ通知する。</p>
 */
public class InvalidInputException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public InvalidInputException(String message) {
        super(message);
    }
}
