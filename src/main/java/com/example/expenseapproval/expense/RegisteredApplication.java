package com.example.expenseapproval.expense;

import java.util.Objects;
import java.util.Optional;

/**
 * 登録済み申請（経費申請の内容と、申請状態・承認者）。
 *
 * <p>不変オブジェクトとし、承認は新しいインスタンスを返す。
 * 保存し直すまで登録済みデータは変わらないため、承認に失敗した場合に状態や承認者だけが書き換わることはない。</p>
 */
public final class RegisteredApplication {

    private final ExpenseApplication application;

    private final ApplicationStatus status;

    /** 承認者ID。承認前は null */
    private final String approverId;

    private RegisteredApplication(ExpenseApplication application, ApplicationStatus status, String approverId) {
        this.application = Objects.requireNonNull(application);
        this.status = Objects.requireNonNull(status);
        this.approverId = approverId;
    }

    /**
     * 承認待ちの登録済み申請を生成する。
     */
    public static RegisteredApplication pending(ExpenseApplication application) {
        return new RegisteredApplication(application, ApplicationStatus.PENDING, null);
    }

    /**
     * 承認済みにした登録済み申請を返す。このインスタンス自体は変更しない。
     *
     * @param approverId 承認者ID
     * @throws IllegalStateException 承認待ちではない場合
     */
    public RegisteredApplication approve(String approverId) {
        if (!isPending()) {
            throw new IllegalStateException("承認待ちではない申請は承認できません：" + applicationId());
        }
        return new RegisteredApplication(application, ApplicationStatus.APPROVED, Objects.requireNonNull(approverId));
    }

    public boolean isPending() {
        return status == ApplicationStatus.PENDING;
    }

    /**
     * 指定の社員の申請か。
     */
    public boolean isSubmittedBy(String employeeId) {
        return applicantId().equals(employeeId);
    }

    public String applicationId() {
        return application.applicationId();
    }

    public String applicantId() {
        return application.applicantId();
    }

    public ExpenseApplication application() {
        return application;
    }

    public ApplicationStatus status() {
        return status;
    }

    /**
     * 承認者ID。承認前は empty。
     */
    public Optional<String> approverId() {
        return Optional.ofNullable(approverId);
    }
}
