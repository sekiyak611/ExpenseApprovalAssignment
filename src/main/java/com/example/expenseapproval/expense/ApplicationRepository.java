package com.example.expenseapproval.expense;

import java.util.List;
import java.util.Optional;

/**
 * 登録済み申請の保持・検索。
 */
public interface ApplicationRepository {

    Optional<RegisteredApplication> findById(String applicationId);

    /**
     * 指定の申請者の登録済み申請を登録順に返す。
     */
    List<RegisteredApplication> findByApplicantId(String applicantId);

    /**
     * 登録済み申請を保存する。同じ申請IDの申請が登録済みの場合は置き換える（承認による状態の更新）。
     */
    void save(RegisteredApplication application);
}
