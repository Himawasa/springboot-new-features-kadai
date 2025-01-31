package com.example.samuraitravel.repository;

// Spring Data JPAリポジトリとページングサポート
import org.springframework.data.domain.Page; // ページングされたデータを扱うためのクラス
import org.springframework.data.domain.Pageable; // ページングおよびソートの設定を提供
import org.springframework.data.jpa.repository.JpaRepository; // JPAを使用したCRUD操作を提供するインターフェース

// アプリケーション内のエンティティ
import com.example.samuraitravel.entity.User; // Userエンティティの操作をサポート

/**
 * Userエンティティを操作するためのリポジトリインターフェース。
 * Spring Data JPAにより、自動でCRUD機能が提供されます。
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * メールアドレスでユーザーを検索します。
     *
     * @param email メールアドレス
     * @return 検索結果のUser
     */
    public User findByEmail(String email);

    /**
     * 名前またはフリガナが部分一致するユーザーを検索します。
     *
     * @param nameKeyword 名前の検索キーワード
     * @param furiganaKeyword フリガナの検索キーワード
     * @param pageable ページング情報
     * @return ユーザーのページング結果
     */
    public Page<User> findByNameLikeOrFuriganaLike(String nameKeyword, String furiganaKeyword, Pageable pageable);
}
