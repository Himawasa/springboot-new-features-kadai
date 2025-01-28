package com.example.samuraitravel.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;

/**
 * お気に入り関連のビジネスロジックを管理するサービスクラス。
 */
@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    /**
     * コンストラクタでFavoriteRepositoryを注入。
     * 
     * @param favoriteRepository Favoriteリポジトリのインスタンス
     */
    public FavoriteService(FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    /**
     * お気に入りを作成するメソッド。
     * 
     * @param house 対象の民宿
     * @param user 対象のユーザー
     */
    @Transactional
    public void create(House house, User user) {
        // Favoriteクラスのインスタンスを生成
        Favorite favorite = new Favorite();

        // 民宿とユーザーを設定
        favorite.setHouse(house);
        favorite.setUser(user);

        // お気に入りを保存
        favoriteRepository.save(favorite);
    }

    /**
     * 指定された民宿とユーザーが既にお気に入りに登録されているかを判定する。
     * 
     * @param house 対象の民宿
     * @param user 対象のユーザー
     * @return お気に入り登録されている場合はtrue、そうでない場合はfalse
     */
    public boolean isFavorite(House house, User user) {
        // お気に入りが存在するかを判定
        if (favoriteRepository.findByHouseAndUser(house, user) != null) {
            return true; // お気に入りが存在する場合はtrueを返却
        }
        return false; // お気に入りが存在しない場合はfalseを返却
    }
}
