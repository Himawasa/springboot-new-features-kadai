package com.example.samuraitravel.controller;

// 必要なインポート
import org.springframework.data.domain.Page; // ページネーション用
import org.springframework.data.domain.Pageable; // ページネーション用
import org.springframework.data.web.PageableDefault; // デフォルトのページ設定
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 現在認証されたユーザー情報を取得
import org.springframework.stereotype.Controller; // コントローラークラスを示すアノテーション
import org.springframework.ui.Model; // テンプレートにデータを渡すためのクラス
import org.springframework.validation.BindingResult; // バリデーション結果を格納するクラス
import org.springframework.validation.annotation.Validated; // バリデーション用アノテーション
// 各種リクエストマッピング用
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にデータを渡すクラス

import com.example.samuraitravel.entity.House; // 民宿エンティティ
import com.example.samuraitravel.entity.Review; // レビューエンティティ
import com.example.samuraitravel.entity.User; // ユーザーエンティティ
import com.example.samuraitravel.form.ReviewEditForm; // レビュー編集用フォーム
import com.example.samuraitravel.form.ReviewRegisterForm; // レビュー登録用フォーム
import com.example.samuraitravel.repository.HouseRepository; // 民宿リポジトリ
import com.example.samuraitravel.repository.ReviewRepository; // レビューデータベース操作用リポジトリ
import com.example.samuraitravel.security.UserDetailsImpl; // 認証済みユーザー情報
import com.example.samuraitravel.service.ReviewService; // レビュー操作サービス

/**
 * レビュー関連のリクエストを処理するコントローラー
 */
@Controller
@RequestMapping("/houses/{houseId}/reviews")
public class ReviewController {
    private final ReviewRepository reviewRepository; // レビューデータベース操作用リポジトリ
    private final HouseRepository houseRepository; // 民宿データベース操作用リポジトリ
    private final ReviewService reviewService; // レビュー操作用サービス

    /**
     * コンストラクタ
     * 
     * @param reviewRepository ReviewRepositoryのインスタンス
     * @param houseRepository HouseRepositoryのインスタンス
     * @param reviewService ReviewServiceのインスタンス
     */
    public ReviewController(ReviewRepository reviewRepository, HouseRepository houseRepository,
                            ReviewService reviewService) {
        this.reviewRepository = reviewRepository;
        this.houseRepository = houseRepository;
        this.reviewService = reviewService;
    }

    /**
     * 指定された民宿のレビュー一覧を表示
     */
    @GetMapping
    public String index(@PathVariable(name = "houseId") Integer houseId,
                        @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
        // 民宿データを取得
        House house = houseRepository.getReferenceById(houseId);

        // 指定された民宿のレビューを取得
        Page<Review> reviewPage = reviewRepository.findByHouseOrderByCreatedAtDesc(house, pageable);

        // テンプレートに渡すデータを設定
        model.addAttribute("house", house);
        model.addAttribute("reviewPage", reviewPage);

        return "reviews/index"; // レビュー一覧ページを返却
    }

    /**
     * 新しいレビューを登録するフォームを表示
     */
    @GetMapping("/register")
    public String register(@PathVariable(name = "houseId") Integer houseId, Model model) {
        // 民宿データを取得
        House house = houseRepository.getReferenceById(houseId);

        // テンプレートに渡すデータを設定
        model.addAttribute("house", house);
        model.addAttribute("reviewRegisterForm", new ReviewRegisterForm());

        return "reviews/register"; // レビュー登録ページを返却
    }

    /**
     * 新しいレビューを作成
     */
    @PostMapping("/create")
    public String create(@PathVariable(name = "houseId") Integer houseId,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         @ModelAttribute @Validated ReviewRegisterForm reviewRegisterForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        // 民宿データを取得
        House house = houseRepository.getReferenceById(houseId);

        // 現在ログインしているユーザー情報を取得
        User user = userDetailsImpl.getUser();

        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            model.addAttribute("house", house);
            return "reviews/register"; // エラーがある場合、登録ページを再表示
        }

        // レビューを作成
        reviewService.create(house, user, reviewRegisterForm);

        // 成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "レビューを投稿しました。");

        return "redirect:/houses/{houseId}"; // 民宿詳細ページにリダイレクト
    }

    /**
     * レビューを編集するフォームを表示
     */
    @GetMapping("/{reviewId}/edit")
    public String edit(@PathVariable(name = "houseId") Integer houseId,
                       @PathVariable(name = "reviewId") Integer reviewId, Model model) {
        // 民宿データを取得
        House house = houseRepository.getReferenceById(houseId);

        // 編集対象のレビューを取得
        Review review = reviewRepository.getReferenceById(reviewId);

        // フォームデータを設定
        ReviewEditForm reviewEditForm = new ReviewEditForm(review.getId(), review.getScore(), review.getContent());

        // テンプレートに渡すデータを設定
        model.addAttribute("house", house);
        model.addAttribute("review", review);
        model.addAttribute("reviewEditForm", reviewEditForm);

        return "reviews/edit"; // レビュー編集ページを返却
    }

    /**
     * レビューを更新
     */
    @PostMapping("/{reviewId}/update")
    public String update(@PathVariable(name = "houseId") Integer houseId,
                         @PathVariable(name = "reviewId") Integer reviewId,
                         @ModelAttribute @Validated ReviewEditForm reviewEditForm,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes,
                         Model model) {
        // 民宿データを取得
        House house = houseRepository.getReferenceById(houseId);

        // 編集対象のレビューを取得
        Review review = reviewRepository.getReferenceById(reviewId);

        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {
            model.addAttribute("house", house);
            model.addAttribute("review", review);
            return "reviews/edit"; // エラーがある場合、編集ページを再表示
        }

        // レビューを更新
        reviewService.update(reviewEditForm);

        // 成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "レビューを編集しました。");

        return "redirect:/houses/{houseId}"; // 民宿詳細ページにリダイレクト
    }

    /**
     * レビューを削除
     */
    @PostMapping("/{reviewId}/delete")
    public String delete(@PathVariable(name = "reviewId") Integer reviewId, RedirectAttributes redirectAttributes) {
        // レビューを削除
        reviewRepository.deleteById(reviewId);

        // 成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "レビューを削除しました。");

        return "redirect:/houses/{houseId}"; // 民宿詳細ページにリダイレクト
    }
}
