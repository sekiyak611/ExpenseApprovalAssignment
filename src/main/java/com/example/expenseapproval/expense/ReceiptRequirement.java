package com.example.expenseapproval.expense;

/**
 * 領収書の要否の判定基準。
 */
@FunctionalInterface
public interface ReceiptRequirement {

    /**
     * @param amount 申請金額（円）
     * @return 領収書が必要な場合は true
     */
    boolean isRequiredFor(int amount);

    /**
     * 金額にかかわらず領収書を必須とする。
     */
    static ReceiptRequirement always() {
        return amount -> true;
    }

    /**
     * 指定の金額以上の場合のみ領収書を必須とする。
     *
     * @param threshold 領収書が必要となる金額（円）。この金額ちょうども必須
     */
    static ReceiptRequirement fromAmount(int threshold) {
        return amount -> amount >= threshold;
    }
}
