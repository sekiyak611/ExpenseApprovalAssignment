package com.example.expenseapproval.submission;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import com.example.expenseapproval.common.InputValidator;
import com.example.expenseapproval.expense.ApplicationRepository;
import com.example.expenseapproval.expense.ExpenseApplication;
import com.example.expenseapproval.expense.RegisteredApplication;

/**
 * 経費申請を受け付ける。
 *
 * <p>業務チェックを順にすべて実施し、エラーがなければ承認待ちで登録する、という受付の流れのみを担う。
 * 個々のチェック内容は {@link SubmissionCheck} に、データの保持は {@link ApplicationRepository} に委ねる。</p>
 */
public final class SubmissionService {

    private final ApplicationRepository applicationRepository;

    private final List<SubmissionCheck> checks;

    /**
     * @param applicationRepository 登録済み申請の保存先
     * @param checks 業務チェック。リストの順に実施し、エラーコードもこの順で返す
     */
    public SubmissionService(ApplicationRepository applicationRepository, List<SubmissionCheck> checks) {
        this.applicationRepository = Objects.requireNonNull(applicationRepository);
        this.checks = List.copyOf(checks);
    }

    /**
     * 経費申請を受け付ける。業務チェックのエラーが1件でもあれば登録しない。
     *
     * @param application 経費申請
     * @param baseDate 基準日
     * @throws com.example.expenseapproval.common.InvalidInputException 経費申請または基準日が未指定の場合
     */
    public SubmissionResult submit(ExpenseApplication application, LocalDate baseDate) {
        InputValidator.requireNonNull(application, "経費申請");
        InputValidator.requireNonNull(baseDate, "基準日");

        List<SubmissionErrorCode> errors = checks.stream()
                .filter(check -> !check.isSatisfiedBy(application, baseDate))
                .map(SubmissionCheck::errorCode)
                .toList();
        if (!errors.isEmpty()) {
            return new SubmissionResult.Rejected(application.applicationId(), errors);
        }

        RegisteredApplication registered = RegisteredApplication.pending(application);
        applicationRepository.save(registered);
        return new SubmissionResult.Accepted(registered.applicationId(), registered.status());
    }
}
