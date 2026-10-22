package com.example.expenseapproval.submission.check;

import static com.example.expenseapproval.fixture.ExpenseApplicationBuilder.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.example.expenseapproval.employee.Employee;
import com.example.expenseapproval.employee.EmployeeRepository;
import com.example.expenseapproval.employee.EmployeeType;
import com.example.expenseapproval.employee.InMemoryEmployeeRepository;
import com.example.expenseapproval.submission.SubmissionErrorCode;

class ApplicantExistenceCheckTest {

    private static final LocalDate BASE_DATE = LocalDate.of(2026, 10, 5);

    private final EmployeeRepository employeeRepository = new InMemoryEmployeeRepository();

    private final ApplicantExistenceCheck check = new ApplicantExistenceCheck(employeeRepository);

    @Test
    void 申請者が社員一覧に存在すれば合格() {
        employeeRepository.save(new Employee("U001", "山田 太郎", EmployeeType.GENERAL));

        assertTrue(check.isSatisfiedBy(anApplication().applicantId("U001").build(), BASE_DATE));
    }

    @Test
    void 申請者が社員一覧に存在しなければ不合格() {
        assertFalse(check.isSatisfiedBy(anApplication().applicantId("U999").build(), BASE_DATE));
    }

    @Test
    void 不合格時のエラーコードはAPPLICANT_NOT_FOUND() {
        assertEquals(SubmissionErrorCode.APPLICANT_NOT_FOUND, check.errorCode());
    }
}
