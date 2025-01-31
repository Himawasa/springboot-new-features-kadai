package com.example.samuraitravel.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal; // ログインユーザー情報を取得するためのアノテーション
import org.springframework.stereotype.Controller; // Spring MVCのコントローラとして指定
import org.springframework.ui.Model; // ビューにデータを渡すためのオブジェクト
import org.springframework.validation.BindingResult; // バリデーション結果を格納するオブジェクト
import org.springframework.validation.FieldError; // フィールドエラーを表現するクラス
import org.springframework.validation.annotation.Validated; // 入力データのバリデーションを有効にするアノテーション
import org.springframework.web.bind.annotation.GetMapping; // HTTP GETリクエストを処理するためのアノテーション
import org.springframework.web.bind.annotation.ModelAttribute; // フォームデータをオブジェクトにバインドするアノテーション
import org.springframework.web.bind.annotation.PostMapping; // HTTP POSTリクエストを処理するためのアノテーション
import org.springframework.web.bind.annotation.RequestMapping; // コントローラの共通URLを指定
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にデータを渡すためのオブジェクト

import com.example.samuraitravel.entity.User; // ユーザーエンティティ
import com.example.samuraitravel.form.UserEditForm; // ユーザー編集フォームクラス
import com.example.samuraitravel.repository.UserRepository; // ユーザー情報を操作するリポジトリ
import com.example.samuraitravel.security.UserDetailsImpl; // ログインユーザーの詳細情報を扱うクラス
import com.example.samuraitravel.service.UserService; // ユーザー関連のビジネスロジックを扱うサービス

/**
 * ユーザー情報を管理するコントローラ
 * ユーザーの詳細表示、編集、更新などの機能を提供します。
 */
@Controller
@RequestMapping("/user") // "/user" 以下のURLをこのコントローラで処理
public class UserController {
    private final UserRepository userRepository; // ユーザー情報をデータベースとやり取りするリポジトリ
    private final UserService userService; // ユーザー関連のビジネスロジックを扱うサービス
    
    /**
     * コンストラクタで依存オブジェクトを注入
     */
    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository; 
        this.userService = userService; 
    }    
    
    /**
     * ユーザー詳細ページの表示
     * 
     * @param userDetailsImpl ログインユーザー情報
     * @param model ビューに渡すデータを格納
     * @return ユーザー詳細ページ
     */
    @GetMapping
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {         
        // ログインユーザー情報をデータベースから取得
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        
        // モデルにユーザー情報を追加
        model.addAttribute("user", user);
        
        return "user/index"; // ビュー: "user/index.html"
    }
    
    /**
     * ユーザー編集ページの表示
     * 
     * @param userDetailsImpl ログインユーザー情報
     * @param model ビューに渡すデータを格納
     * @return ユーザー編集ページ
     */
    @GetMapping("/edit")
    public String edit(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, Model model) {        
        // ログインユーザー情報をデータベースから取得
        User user = userRepository.getReferenceById(userDetailsImpl.getUser().getId());  
        
        // ユーザー情報をフォームにバインド
        UserEditForm userEditForm = new UserEditForm(
            user.getId(), user.getName(), user.getFurigana(), 
            user.getPostalCode(), user.getAddress(), user.getPhoneNumber(), 
            user.getEmail()
        );
        
        // モデルにフォームデータを追加
        model.addAttribute("userEditForm", userEditForm);
        
        return "user/edit"; // ビュー: "user/edit.html"
    }
    
    /**
     * ユーザー情報の更新処理
     * 
     * @param userEditForm ユーザー編集フォームデータ
     * @param bindingResult バリデーション結果
     * @param redirectAttributes リダイレクト時にメッセージを渡すオブジェクト
     * @return 更新後のリダイレクト先
     */
    @PostMapping("/update")
    public String update(@ModelAttribute @Validated UserEditForm userEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        // メールアドレスが変更されており、かつ登録済みであればエラーを追加
        if (userService.isEmailChanged(userEditForm) && userService.isEmailRegistered(userEditForm.getEmail())) {
            FieldError fieldError = new FieldError(bindingResult.getObjectName(), "email", "すでに登録済みのメールアドレスです。");
            bindingResult.addError(fieldError);                       
        }
        
        // バリデーションエラーがある場合、編集ページを再表示
        if (bindingResult.hasErrors()) {
            return "user/edit"; // ビュー: "user/edit.html"
        }
        
        // ユーザー情報を更新
        userService.update(userEditForm);
        
        // 成功メッセージをリダイレクト時に設定
        redirectAttributes.addFlashAttribute("successMessage", "会員情報を編集しました。");
        
        return "redirect:/user"; // ユーザー詳細ページにリダイレクト
    }    
}
