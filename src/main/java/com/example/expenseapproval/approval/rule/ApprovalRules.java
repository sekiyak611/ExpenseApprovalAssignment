package com.example.expenseapproval.approval.rule;

import java.util.List;

import com.example.expenseapproval.approval.ApprovalRule;

/**
 * 現行の承認ルールの定義。どのルールをどの順序で判定するかを本クラスに集約する。
 */
public final class ApprovalRules {

    private ApprovalRules() {
    }

    /**
     * 現行の承認ルールを判定順に返す。
     */
    public static List<ApprovalRule> standard() {
        return List.of(
                new ApproverAuthorityRule(),
                new NoSelfApprovalRule(),
                new PendingStatusRule());
    }
}
