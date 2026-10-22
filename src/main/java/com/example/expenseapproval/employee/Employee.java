package com.example.expenseapproval.employee;

import com.example.expenseapproval.common.InputValidator;

/**
 * 社員。
 *
 * @param id 社員ID
 * @param name 社員名
 * @param type 区分
 */
public record Employee(String id, String name, EmployeeType type) {

    /**
     * @throws com.example.expenseapproval.common.InvalidInputException 入力不備がある場合
     */
    public Employee {
        id = InputValidator.requireText(id, "社員ID");
        name = InputValidator.requireText(name, "社員名");
        type = InputValidator.requireNonNull(type, "社員区分");
    }

    /**
     * 経費申請を承認する権限があるか。
     */
    public boolean canApprove() {
        return type.canApprove();
    }
}
