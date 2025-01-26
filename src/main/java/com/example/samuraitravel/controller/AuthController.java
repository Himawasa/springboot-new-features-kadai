package com.example.samuraitravel.controller;

import jakarta.servlet.http.HttpServletRequest; // HTTPリクエストを操作するためのクラス

// 必要なクラスをインポート
import org.springframework.stereotype.Controller; // Spring MVCのコントローラであることを示すアノテーション
import org.springframework.ui.Model; // ビューにデータを渡すためのオブジェクト
import org.springframework.validation.BindingResult; // フォームのバリデーション結果を格納するオブジェクト
import org.springframework.validation.FieldError; // バリデーションエラーを表すオブジェクト
import org.springframework.validation.annotation.Validated; // 入力フォームのバリデーションを有効にするアノテーション
// 各種リクエストマッピング用のアノテーションをインポート
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にメッセージなどを渡すためのオブジェクト

import com.example.samuraitravel.entity.User; // ユーザーエンティティ
import com.example.samuraitravel.entity.VerificationToken; // 認証トークンエンティティ
import com.example.samuraitravel.event.SignupEventPublisher; // 会員登録イベントのパブリッシャー
import com.example.samuraitravel.form.SignupForm; // 会員登録用の入力フォーム
import com.example.samuraitravel.service.UserService; // ユーザー関連のサービス
import com.example.samuraitravel.service.VerificationTokenService; // 認証トークン関連のサービス

/**
 * AuthControllerクラス
 * ユーザーの認証および会員登録を管理するコントローラー。
 */
@Controller
public class AuthController {    
    private final UserService userService; // ユーザー操作を提供するサービス
    private final SignupEventPublisher signupEventPublisher; // 会員登録イベントのパブリッシャー
    private final VerificationTokenService verificationTokenService; // 認証トークンサービス
    
    /**
     * コンストラクタ
     * DIを使用して依存オブジェクトを注入する。
     */
    public AuthController(UserService userService, SignupEventPublisher signupEventPublisher, VerificationTokenService verificationTokenService) { 
        this.userService = userService; 
        this.signupEventPublisher = signupEventPublisher;
        this.verificationTokenService = verificationTokenService;
    }    
    
    /**
     * ログイン画面を表示する
     * @return ログイン画面のテンプレート名
     */
    @GetMapping("/login")
    public String login() {        
        return "auth/login"; // "auth/login" テンプレートを返す
    }
    
    /**
     * 会員登録画面を表示する
     * @param model ビューにデータを渡すためのオブジェクト
     * @return 会員登録画面のテンプレート名
     */
    @GetMapping("/signup")
    public String signup(Model model) {        
        model.addAttribute("signupForm", new SignupForm()); // 空のSignupFormオブジェクトをビューに渡す
        return "auth/signup"; // "auth/signup" テンプレートを返す
    }    
    
    /**
     * 会員登録処理を行う
     * @param signupForm 入力された会員登録フォーム
     * @param bindingResult フォームのバリデーション結果
     * @param redirectAttributes リダイレクト時にメッセージを渡すオブジェクト
     * @param httpServletRequest HTTPリクエスト情報
     * @return リダイレクト先のURL
     */
    @PostMapping("/signup")
    public String signup(@ModelAttribute @Validated SignupForm signupForm, BindingResult bindingResult, RedirectAttributes redirectAttributes, HttpServletRequest httpServletRequest) {
        // メールアドレスが既に登録済みかチェック
        if (userService.isEmailRegistered(signupForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }    
        
        // パスワードと確認用パスワードが一致するかチェック
        if (!userService.isSamePassword(signupForm.getPassword(), signupForm.getPasswordConfirmation())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "password", "パスワードが一致しません。");
            bindingResult.addError(fieldError);
        }        
        
        // 入力エラーがある場合は再度会員登録画面を表示
        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }
        
        // ユーザーを作成
        User createdUser = userService.create(signupForm);
        // 現在のリクエストURLを取得
        String requestUrl = new String(httpServletRequest.getRequestURL());
        // 会員登録イベントを発行（メール認証リンクを送信）
        signupEventPublisher.publishSignupEvent(createdUser, requestUrl);
        // 成功メッセージをリダイレクト先に渡す
        redirectAttributes.addFlashAttribute("successMessage", "ご入力いただいたメールアドレスに認証メールを送信しました。メールに記載されているリンクをクリックし、会員登録を完了してください。");        
        
        return "redirect:/"; // ホームページへリダイレクト
    }   
    
    /**
     * 認証トークンの検証処理を行う
     * @param token 認証トークン
     * @param model ビューにデータを渡すためのオブジェクト
     * @return 認証結果画面のテンプレート名
     */
    @GetMapping("/signup/verify")
    public String verify(@RequestParam(name = "token") String token, Model model) {
        // トークンをデータベースから取得
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);
        
        if (verificationToken != null) {
            // トークンが有効ならユーザーを有効化
            User user = verificationToken.getUser();  
            userService.enableUser(user); // ユーザーを有効化
            String successMessage = "会員登録が完了しました。";
            model.addAttribute("successMessage", successMessage);            
        } else {
            // トークンが無効ならエラーメッセージを表示
            String errorMessage = "トークンが無効です。";
            model.addAttribute("errorMessage", errorMessage);
        }
        
        return "auth/verify"; // 認証結果画面のテンプレートを返す
    }    
}
