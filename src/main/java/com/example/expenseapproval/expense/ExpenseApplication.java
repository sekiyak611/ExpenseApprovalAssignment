package com.example.expenseapproval.expense;

import java.time.LocalDate;

import com.example.expenseapproval.common.InputValidator;

/**
 * 経費申請（申請時の入力内容）。
 *
 * <p>生成時に入力不備を検証し、文字列は前後の空白を取り除いて保持する。
 * そのため、生成済みのインスタンスに対する業務チェックは入力不備がない前提で判定できる。</p>
 *
 * @param applicationId 申請ID
 * @param applicantId 申請者ID
 * @param usageDate 利用日
 * @param category 経費区分
 * @param amount 金額（円）
 * @param hasReceipt 領収書の有無
 * @param purpose 利用目的
 */
public record ExpenseApplication(
        String applicationId,
        String applicantId,
        LocalDate usageDate,
        ExpenseCategory category,
        int amount,
        boolean hasReceipt,
        String purpose) {

    /**
     * @throws com.example.expenseapproval.common.InvalidInputException 入力不備がある場合
     */
    public ExpenseApplication {
        applicationId = InputValidator.requireText(applicationId, "申請ID");
        applicantId = InputValidator.requireText(applicantId, "申請者ID");
        usageDate = InputValidator.requireNonNull(usageDate, "利用日");
        category = InputValidator.requireNonNull(category, "経費区分");
        amount = InputValidator.requirePositive(amount, "金額");
        purpose = InputValidator.requireText(purpose, "利用目的");
    }
}
