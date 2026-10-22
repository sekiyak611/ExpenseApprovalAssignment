package com.example.expenseapproval.expense;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class InMemoryApplicationRepositoryTest {

    private final InMemoryApplicationRepository repository = new InMemoryApplicationRepository();

    @Test
    void 保存した申請を申請IDで取得できる() {
        RegisteredApplication registered = pending("A001", "U001");

        repository.save(registered);

        assertSame(registered, repository.findById("A001").orElseThrow());
        assertTrue(repository.findById("A999").isEmpty());
    }

    @Test
    void 同じ申請IDで保存すると置き換える() {
        RegisteredApplication registered = pending("A001", "U001");
        repository.save(registered);

        repository.save(registered.approve("U002"));

        assertEquals(ApplicationStatus.APPROVED, repository.findById("A001").orElseThrow().status());
        assertEquals(1, repository.findByApplicantId("U001").size());
    }

    @Test
    void 申請者の申請を登録順に取得できる() {
        repository.save(pending("A002", "U001"));
        repository.save(pending("A001", "U002"));
        repository.save(pending("A003", "U001"));

        List<String> applicationIds = repository.findByApplicantId("U001").stream()
                .map(RegisteredApplication::applicationId)
                .toList();

        assertEquals(List.of("A002", "A003"), applicationIds);
    }

    private static RegisteredApplication pending(String applicationId, String applicantId) {
        return RegisteredApplication.pending(
                anApplication().applicationId(applicationId).applicantId(applicantId).build());
    }
}
