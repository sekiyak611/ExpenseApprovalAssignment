package com.example.expenseapproval.employee;

/**
 * 社員区分。
 *
 * <p>区分ごとの権限を区分自身に持たせ、区分を追加しても権限の判定箇所が散らばらないようにする。</p>
 */
public enum EmployeeType {

    /** 一般社員 */
    GENERAL(false),

    /** 管理職 */
    MANAGER(true);

    private final boolean approvalAuthority;

    EmployeeType(boolean approvalAuthority) {
        this.approvalAuthority = approvalAuthority;
    }

    /**
     * 経費申請を承認する権限があるか。
     */
    public boolean canApprove() {
        return approvalAuthority;
    }
}
