package com.example.expenseapproval.expense;

/**
 * 経費区分。
 *
 * <p>区分ごとに異なる業務ルール（1申請あたりの上限金額、領収書の要否）を区分自身に定義する。
 * 区分の追加は定数を1つ追加するだけでよく、ルールの定義漏れはコンストラクタ引数によりコンパイルエラーとなる。
 * 区分を判定する if / switch を他のクラスに書かないことで、ルールが各所に重複しないようにしている。</p>
 */
public enum ExpenseCategory {

    /** 交通費：上限20,000円、10,000円以上の場合のみ領収書必須 */
    TRANSPORT(20_000, ReceiptRequirement.fromAmount(10_000)),

    /** 宿泊費：上限30,000円、領収書必須 */
    LODGING(30_000, ReceiptRequirement.always()),

    /** 備品費：上限50,000円、領収書必須 */
    SUPPLIES(50_000, ReceiptRequirement.always()),

    /** 会議費（追加仕様）：上限10,000円、領収書必須 */
    MEETING(10_000, ReceiptRequirement.always());

    /** 1申請あたりの上限金額（円）。上限と同額は申請可能 */
    private final int amountLimit;

    private final ReceiptRequirement receiptRequirement;

    ExpenseCategory(int amountLimit, ReceiptRequirement receiptRequirement) {
        this.amountLimit = amountLimit;
        this.receiptRequirement = receiptRequirement;
    }

    /**
     * 金額が1申請あたりの上限を超えるか。
     *
     * @param amount 申請金額（円）
     */
    public boolean exceedsAmountLimit(int amount) {
        return amount > amountLimit;
    }

    /**
     * 領収書が必要か。
     *
     * @param amount 申請金額（円）
     */
    public boolean requiresReceipt(int amount) {
        return receiptRequirement.isRequiredFor(amount);
    }
}
