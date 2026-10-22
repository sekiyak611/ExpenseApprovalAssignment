package com.example.expenseapproval.approval.rule;

import com.example.expenseapproval.approval.ApprovalErrorCode;
import com.example.expenseapproval.approval.ApprovalRule;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.expense.RegisteredApplication;

/**
 * 申請状態が承認待ちか。
 */
public final class PendingStatusRule implements ApprovalRule {

    @Override
    public ApprovalErrorCode errorCode() {
        return ApprovalErrorCode.INVALID_STATUS;
    }

    @Override
    public boolean isSatisfiedBy(RegisteredApplication application, Employee approver) {
        return application.isPending();
    }
}
