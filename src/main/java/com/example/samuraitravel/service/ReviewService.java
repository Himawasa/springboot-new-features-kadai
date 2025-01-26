package com.example.samuraitravel.service;

// 必要なインポート
import org.springframework.stereotype.Service; // サービスクラスとしてSpringに登録するためのアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション管理

import com.example.samuraitravel.entity.House; // 民宿エンティティ
import com.example.samuraitravel.entity.Review; // レビューエンティティ
import com.example.samuraitravel.entity.User; // ユーザーエンティティ
import com.example.samuraitravel.form.ReviewEditForm; // レビュー編集用フォーム
import com.example.samuraitravel.form.ReviewRegisterForm; // レビュー登録用フォーム
import com.example.samuraitravel.repository.ReviewRepository; // レビューデータベース操作用リポジトリ

/**
 * レビューに関するビジネスロジックを提供するサービスクラス
 */
@Service
public class ReviewService {
    private final ReviewRepository reviewRepository; // レビューデータベース操作用リポジトリ

    /**
     * コンストラクタ
     *
     * @param reviewRepository ReviewRepository のインスタンス
     */
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /**
     * 新しいレビューを作成して保存する
     *
     * @param house 民宿情報
     * @param user ユーザー情報
     * @param reviewRegisterForm レビュー登録用フォーム
     */
    @Transactional
    public void create(House house, User user, ReviewRegisterForm reviewRegisterForm) {
        // Reviewクラスのインスタンスを生成
        Review review = new Review();

        // reviewインスタンスにフォームから取得したデータをセット
        review.setHouse(house); // 対象の民宿を設定
        review.setUser(user); // レビューを書いたユーザーを設定
        review.setScore(reviewRegisterForm.getScore()); // レビューのスコアを設定
        review.setContent(reviewRegisterForm.getContent()); // レビューのコメントを設定

        // データベースに保存
        reviewRepository.save(review);
    }

    /**
     * 既存のレビューを更新する
     *
     * @param reviewEditForm レビュー編集用フォーム
     */
    @Transactional
    public void update(ReviewEditForm reviewEditForm) {
        // データベースから編集対象のレビューを取得
        Review review = reviewRepository.getReferenceById(reviewEditForm.getId());

        // reviewインスタンスにフォームのデータを上書き設定
        review.setScore(reviewEditForm.getScore()); // 新しいスコアを設定
        review.setContent(reviewEditForm.getContent()); // 新しいコメントを設定

        // データベースに保存
        reviewRepository.save(review);
    }

    /**
     * ユーザーがすでに特定の民宿にレビューを投稿しているか確認する
     *
     * @param house 民宿情報
     * @param user ユーザー情報
     * @return レビュー済みなら true、それ以外は false
     */
    public boolean hasUserAlreadyReviewed(House house, User user) {
        // データベースでユーザーがすでにレビューを投稿しているかを確認
        if (reviewRepository.findByHouseAndUser(house, user) != null) {
            return true; // レビュー済み
        }
        return false; // 未レビュー
    }
}
