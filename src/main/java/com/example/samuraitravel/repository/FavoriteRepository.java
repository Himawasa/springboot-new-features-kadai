package com.example.samuraitravel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;

/**
 * "Favorite" エンティティのリポジトリインターフェイス。
 * データベースとのやり取りを管理します。
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    
    /**
     * 指定されたユーザーに紐づくお気に入りを、作成日時の降順で取得します。
     *
     * @param user ユーザーエンティティ
     * @param pageable ページング情報
     * @return Page<Favorite> ページングされたお気に入りリスト
     */
    Page<Favorite> findByUserOrderByCreatedAtDesc(User user, Pageable pageable);

    /**
     * 指定された民宿とユーザーに基づいて、お気に入りを1件取得します。
     *
     * @param house 民宿エンティティ
     * @param user ユーザーエンティティ
     * @return Favorite 該当するお気に入りエンティティ
     */
    Favorite findByHouseAndUser(House house, User user);
}
