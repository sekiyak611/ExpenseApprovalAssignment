package com.example.expenseapproval.fixture;

import java.time.LocalDate;

import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.ExpenseCategory;

/**
 * テスト用の経費申請を組み立てる。
 *
 * <p>既定値は、基準日 2026-10-05・申請者 U001 の場合に入力不備も業務エラーもない申請。
 * テストでは確認したい項目だけを変更する。</p>
 */
public final class ExpenseApplicationBuilder {

    private String applicationId = "A001";

    private String applicantId = "U001";

    private LocalDate usageDate = LocalDate.of(2026, 10, 1);

    private ExpenseCategory category = ExpenseCategory.TRANSPORT;

    private int amount = 1_000;

    private boolean hasReceipt = true;

    private String purpose = "客先訪問";

    private ExpenseApplicationBuilder() {
    }

    public static ExpenseApplicationBuilder anApplication() {
        return new ExpenseApplicationBuilder();
    }

    public ExpenseApplicationBuilder applicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public ExpenseApplicationBuilder applicantId(String applicantId) {
        this.applicantId = applicantId;
        return this;
    }

    public ExpenseApplicationBuilder usageDate(LocalDate usageDate) {
        this.usageDate = usageDate;
        return this;
    }

    public ExpenseApplicationBuilder category(ExpenseCategory category) {
        this.category = category;
        return this;
    }

    public ExpenseApplicationBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ExpenseApplicationBuilder hasReceipt(boolean hasReceipt) {
        this.hasReceipt = hasReceipt;
        return this;
    }

    public ExpenseApplicationBuilder purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }

    public ExpenseApplication build() {
        return new ExpenseApplication(applicationId, applicantId, usageDate, category, amount, hasReceipt, purpose);
    }
}
