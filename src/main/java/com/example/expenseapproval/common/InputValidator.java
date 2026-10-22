package com.example.expenseapproval.common;

/**
 * 入力不備の判定。
 *
 * <p>経費申請・承認依頼・社員の各入力で同じ基準（前後の空白を除いてから判定する等）を用いるため、
 * 判定と値の正規化をここに集約する。不備がある場合は {@link InvalidInputException} を送出する。</p>
 */
public final class InputValidator {

    private InputValidator() {
    }

    /**
     * 必須の文字列項目を検証し、前後の空白を取り除いた値を返す。
     *
     * @param value 入力値
     * @param itemName 項目名（エラーメッセージに使用）
     * @return 前後の空白を取り除いた値
     * @throws InvalidInputException null・空文字・空白のみの場合
     */
    public static String requireText(String value, String itemName) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(itemName + "が入力されていません");
        }
        return value.strip();
    }

    /**
     * 必須項目が null でないことを検証する。
     *
     * @param value 入力値
     * @param itemName 項目名（エラーメッセージに使用）
     * @return 入力値
     * @throws InvalidInputException null の場合
     */
    public static <T> T requireNonNull(T value, String itemName) {
        if (value == null) {
            throw new InvalidInputException(itemName + "が入力されていません");
        }
        return value;
    }

    /**
     * 1以上の値であることを検証する。
     *
     * @param value 入力値
     * @param itemName 項目名（エラーメッセージに使用）
     * @return 入力値
     * @throws InvalidInputException 0以下の場合
     */
    public static int requirePositive(int value, String itemName) {
        if (value <= 0) {
            throw new InvalidInputException(itemName + "は1以上を指定してください（入力値：" + value + "）");
        }
        return value;
    }
}
