package com.example.expenseapproval.approval;

import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.expense.RegisteredApplication;

/**
 * 承認時の業務ルール1項目。申請・承認者の存在を確認した後に判定する。
 *
 * <p>ルールを追加する場合は本インタフェースの実装クラスを作成し、ルールの一覧に登録する。
 * 承認の処理（{@link ApprovalService}）の変更は不要。</p>
 */
public interface ApprovalRule {

    /**
     * ルールを満たさない場合のエラーコード。
     */
    ApprovalErrorCode errorCode();

    /**
     * 承認がルールを満たすか。
     *
     * @param application 承認対象の登録済み申請
     * @param approver 承認者
     */
    boolean isSatisfiedBy(RegisteredApplication application, Employee approver);
}
