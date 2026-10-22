package com.example.expenseapproval.approval.rule;

import com.example.expenseapproval.approval.ApprovalErrorCode;
import com.example.expenseapproval.approval.ApprovalRule;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.expense.RegisteredApplication;

/**
 * 申請者本人による承認ではないか。
 */
public final class NoSelfApprovalRule implements ApprovalRule {

    @Override
    public ApprovalErrorCode errorCode() {
        return ApprovalErrorCode.SELF_APPROVAL;
    }

    @Override
    public boolean isSatisfiedBy(RegisteredApplication application, Employee approver) {
        return !application.isSubmittedBy(approver.id());
    }
}
