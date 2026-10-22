/**
 * 経費申請・承認システム。
 *
 * <h2>パッケージ構成と責務</h2>
 * <ul>
 * <li>{@code common}：入力不備の判定（{@code InputValidator}）と、その例外。申請・承認・社員の入力で同じ基準を用いるため共通化</li>
 * <li>{@code employee}：社員・社員区分（承認権限の有無）・社員一覧の保持</li>
 * <li>{@code expense}：経費申請・経費区分（区分ごとの上限金額と領収書の要否）・登録済み申請（状態と承認者）・登録済み申請の保持</li>
 * <li>{@code submission}：申請受付の流れ（{@code SubmissionService}）と業務チェックのインタフェース。
 * {@code check} に個々のチェックと現行のチェック定義（{@code SubmissionChecks}）を置く</li>
 * <li>{@code approval}：承認の流れ（{@code ApprovalService}）と承認ルールのインタフェース。
 * {@code rule} に個々のルールと現行のルール定義（{@code ApprovalRules}）を置く</li>
 * <li>{@code console}：結果のコンソール出力</li>
 * </ul>
 *
 * <h2>設計上の判断</h2>
 * <ul>
 * <li>経費区分ごとのルールは {@code ExpenseCategory} にのみ定義し、区分による分岐を他のクラスに書かない。
 * 対象外の区分を正常扱いしないよう、ルールは列挙子のコンストラクタ引数で必ず定義させる。</li>
 * <li>業務チェックは「判定条件」と「不合格時のエラーコード」を1クラスにまとめ、実施順はチェック定義のリスト順で決める。
 * 受付・承認の各サービスはチェックを順に評価するだけで、チェックの中身を知らない。</li>
 * <li>受付（全件を返す）と承認（最初の1件を返す、存在確認が後続の前提になる）は評価方法・判定対象が異なるため、
 * チェックのインタフェースは共通化せずそれぞれに定義している。</li>
 * <li>入力不備は入力値（{@code ExpenseApplication} / {@code ApprovalRequest}）の生成時に例外で拒否するため、業務チェックは不備のない値を前提にできる。</li>
 * <li>登録済み申請は不変とし、すべてのチェックに合格した場合のみ保存し直す。失敗時に登録済みデータが変わらないことを構造で保証する。</li>
 * </ul>
 *
 * <h2>追加仕様で変更する箇所</h2>
 * <ul>
 * <li>会議費（MEETING）の追加：{@code ExpenseCategory} に定数を1つ追加する（実装済み）。</li>
 * <li>承認待ち件数の上限（PENDING_LIMIT）の追加：{@code SubmissionErrorCode} にコードを追加し、
 * {@code PendingLimitCheck} を作成して {@code SubmissionChecks} の6番目に登録する（実装済み）。受付の流れは変更しない。</li>
 * <li>承認ルールの追加：{@code ApprovalRule} の実装クラスを作成し、{@code ApprovalRules} に登録する。</li>
 * <li>社員区分の追加：{@code EmployeeType} に承認権限の有無とともに定数を追加する。</li>
 * </ul>
 *
 * <h2>仕様の解釈</h2>
 * <ul>
 * <li>利用日は「基準日の30日前 ～ 基準日」（両端を含む）を申請可能とする。</li>
 * <li>前後の空白には全角スペース等も含める（{@link java.lang.String#strip()}）。</li>
 * <li>承認待ち件数の上限は、登録済みの承認待ち申請の件数（今回の申請を含まない）が5件以上の場合に受け付けない。</li>
 * </ul>
 */
package com.example.expenseapproval;
