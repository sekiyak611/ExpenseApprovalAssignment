package com.example.expenseapproval.approval.rule;

import com.example.expenseapproval.approval.ApprovalErrorCode;
import com.example.expenseapproval.approval.ApprovalRule;
import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.expense.RegisteredApplication;

/**
 * 承認者に承認権限があるか。権限の有無は {@link com.example.expenseapproval.employee.EmployeeType} に定義する。
 */
public final class ApproverAuthorityRule implements ApprovalRule {

    @Override
    public ApprovalErrorCode errorCode() {
        return ApprovalErrorCode.NOT_AUTHORIZED;
    }

    @Override
    public boolean isSatisfiedBy(RegisteredApplication application, Employee approver) {
        return approver.canApprove();
    }
}
