package com.example.samuraitravel.controller;

import java.time.LocalDate;

import jakarta.servlet.http.HttpServletRequest; // HTTPリクエスト情報

import org.springframework.data.domain.Page; // ページネーション用
import org.springframework.data.domain.Pageable; // ページネーションの設定
import org.springframework.data.domain.Sort.Direction; // 並び順の指定
import org.springframework.data.web.PageableDefault; // ページネーションのデフォルト設定
import org.springframework.security.core.annotation.AuthenticationPrincipal; // 認証情報の取得
import org.springframework.stereotype.Controller; // コントローラの指定
import org.springframework.ui.Model; // ビューにデータを渡すためのオブジェクト
import org.springframework.validation.BindingResult; // バリデーションの結果を格納
import org.springframework.validation.FieldError; // フィールドのエラーメッセージ
import org.springframework.validation.annotation.Validated; // バリデーションの有効化
import org.springframework.web.bind.annotation.GetMapping; // GETリクエスト用のマッピング
import org.springframework.web.bind.annotation.ModelAttribute; // モデル属性を取得
import org.springframework.web.bind.annotation.PathVariable; // URLのパス変数を取得
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にメッセージを渡す

import com.example.samuraitravel.entity.House; // 民宿エンティティ
import com.example.samuraitravel.entity.Reservation; // 予約エンティティ
import com.example.samuraitravel.entity.User; // ユーザーエンティティ
import com.example.samuraitravel.form.ReservationInputForm; // 予約入力フォーム
import com.example.samuraitravel.form.ReservationRegisterForm; // 予約登録フォーム
import com.example.samuraitravel.repository.HouseRepository; // 民宿リポジトリ
import com.example.samuraitravel.repository.ReservationRepository; // 予約リポジトリ
import com.example.samuraitravel.security.UserDetailsImpl; // 認証されたユーザー情報
import com.example.samuraitravel.service.ReservationService; // 予約サービス
import com.example.samuraitravel.service.StripeService; // Stripe決済サービス

/**
 * 予約関連のリクエストを処理するコントローラ
 */
@Controller
public class ReservationController {
    private final ReservationRepository reservationRepository; // 予約データ操作用リポジトリ
    private final HouseRepository houseRepository; // 民宿データ操作用リポジトリ
    private final ReservationService reservationService; // 予約処理サービス
    private final StripeService stripeService; // Stripe決済サービス

    /**
     * コンストラクタで依存性を注入
     */
    public ReservationController(ReservationRepository reservationRepository, HouseRepository houseRepository, ReservationService reservationService, StripeService stripeService) {
        this.reservationRepository = reservationRepository;
        this.houseRepository = houseRepository;
        this.reservationService = reservationService;
        this.stripeService = stripeService;
    }

    /**
     * ユーザーの予約一覧を表示
     */
    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) {
        User user = userDetailsImpl.getUser(); // 認証済みユーザーを取得
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable); // ユーザーの予約を取得
        
        model.addAttribute("reservationPage", reservationPage); // ページ情報をビューに渡す
        
        return "reservations/index"; // 予約一覧ページを表示
    }
    
    /**
     * 予約入力画面の処理
     */
    @GetMapping("/houses/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) {
        House house = houseRepository.getReferenceById(id); // 民宿データを取得
        Integer numberOfPeople = reservationInputForm.getNumberOfPeople(); // 宿泊人数
        Integer capacity = house.getCapacity(); // 民宿の定員

        // 宿泊人数が定員を超えているか確認
        if (numberOfPeople != null) {
            if (!reservationService.isWithinCapacity(numberOfPeople, capacity)) {
                FieldError fieldError = new FieldError(bindingResult.getObjectName(), "numberOfPeople", "宿泊人数が定員を超えています。");
                bindingResult.addError(fieldError);
            }
        }

        // エラーがある場合、入力画面に戻す
        if (bindingResult.hasErrors()) {
            model.addAttribute("house", house); // 民宿情報を渡す
            model.addAttribute("errorMessage", "予約内容に不備があります。");
            return "houses/show"; // 入力画面に戻る
        }

        // 問題がなければ確認画面にリダイレクト
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);
        return "redirect:/houses/{id}/reservations/confirm";
    }

    /**
     * 予約確認画面の処理
     */
    @GetMapping("/houses/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                          HttpServletRequest httpServletRequest,
                          Model model) {
        House house = houseRepository.getReferenceById(id); // 民宿データを取得
        User user = userDetailsImpl.getUser(); // 認証済みユーザーを取得

        // チェックイン日とチェックアウト日を取得
        LocalDate checkinDate = reservationInputForm.getCheckinDate();
        LocalDate checkoutDate = reservationInputForm.getCheckoutDate();

        // 宿泊料金を計算
        Integer price = house.getPrice();
        Integer amount = reservationService.calculateAmount(checkinDate, checkoutDate, price);

        // 予約登録フォームを作成
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(house.getId(), user.getId(), checkinDate.toString(), checkoutDate.toString(), reservationInputForm.getNumberOfPeople(), amount);

        // Stripeのセッションを作成
        String sessionId = stripeService.createStripeSession(house.getName(), reservationRegisterForm, httpServletRequest);

        // ビューにデータを渡す
        model.addAttribute("house", house);
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);
        model.addAttribute("sessionId", sessionId);

        return "reservations/confirm"; // 予約確認画面を表示
    }
}
