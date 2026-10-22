package com.example.expenseapproval.expense;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * 登録済み申請をメモリ上で保持する {@link ApplicationRepository}。
 */
public final class InMemoryApplicationRepository implements ApplicationRepository {

    private final Map<String, RegisteredApplication> applications = new LinkedHashMap<>();

    @Override
    public Optional<RegisteredApplication> findById(String applicationId) {
        return Optional.ofNullable(applications.get(applicationId));
    }

    @Override
    public List<RegisteredApplication> findByApplicantId(String applicantId) {
        return applications.values().stream()
                .filter(application -> application.isSubmittedBy(applicantId))
                .toList();
    }

    @Override
    public void save(RegisteredApplication application) {
        Objects.requireNonNull(application);
        applications.put(application.applicationId(), application);
    }
}
