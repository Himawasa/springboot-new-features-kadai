package com.example.samuraitravel.repository;

import java.util.List; // List型を使用するためのインポート

import org.springframework.data.domain.Page; // ページング機能をサポートするためのインポート
import org.springframework.data.domain.Pageable; // ページング条件を指定するためのインポート
import org.springframework.data.jpa.repository.JpaRepository; // JpaRepositoryを継承するためのインポート

import com.example.samuraitravel.entity.House; // Houseエンティティを使用するためのインポート
import com.example.samuraitravel.entity.Review; // Reviewエンティティを使用するためのインポート
import com.example.samuraitravel.entity.User; // Userエンティティを使用するためのインポート

/**
 * Reviewエンティティに対するデータ操作を行うリポジトリインターフェース。
 * JpaRepositoryを継承することで基本的なCRUD操作が利用可能。
 */
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    /**
     * 指定されたHouseに関連する最新の6件のレビューを取得するメソッド。
     * @param house 対象のHouseエンティティ
     * @return レビューのリスト (最大6件)
     */
    List<Review> findTop6ByHouseOrderByCreatedAtDesc(House house);

    /**
     * 指定されたHouseとUserに関連するレビューを1件取得するメソッド。
     * @param house 対象のHouseエンティティ
     * @param user 対象のUserエンティティ
     * @return 該当するレビュー (なければnull)
     */
    Review findByHouseAndUser(House house, User user);

    /**
     * 指定されたHouseに関連するレビューの件数を取得するメソッド。
     * @param house 対象のHouseエンティティ
     * @return レビューの件数
     */
    long countByHouse(House house);

    /**
     * 指定されたHouseに関連するレビューを作成日時の降順でページングして取得するメソッド。
     * @param house 対象のHouseエンティティ
     * @param pageable ページング条件
     * @return レビューのページング結果
     */
    Page<Review> findByHouseOrderByCreatedAtDesc(House house, Pageable pageable);
}
