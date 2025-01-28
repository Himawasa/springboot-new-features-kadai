package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.samuraitravel.entity.Favorite;
import com.example.samuraitravel.entity.House;
import com.example.samuraitravel.entity.User;
import com.example.samuraitravel.repository.FavoriteRepository;
import com.example.samuraitravel.repository.HouseRepository;
import com.example.samuraitravel.security.UserDetailsImpl;
import com.example.samuraitravel.service.FavoriteService;

/**
 * お気に入り機能を管理するコントローラークラス
 */
@Controller
public class FavoriteController {

    private final FavoriteRepository favoriteRepository;
    private final HouseRepository houseRepository;
    private final FavoriteService favoriteService;

    /**
     * コンストラクタで必要な依存を注入
     * 
     * @param favoriteRepository Favoriteリポジトリ
     * @param houseRepository    Houseリポジトリ
     * @param favoriteService    Favoriteサービス
     */
    public FavoriteController(FavoriteRepository favoriteRepository, HouseRepository houseRepository,
                               FavoriteService favoriteService) {
        this.favoriteRepository = favoriteRepository;
        this.houseRepository = houseRepository;
        this.favoriteService = favoriteService;
    }

    /**
     * お気に入り一覧を表示
     *
     * @param userDetailsImpl ログイン中のユーザー情報
     * @param pageable        ページング情報
     * @param model           モデルオブジェクト
     * @return お気に入り一覧ページのテンプレートパス
     */
    @GetMapping("/favorites")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                        @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable, Model model) {
        // ログイン中のユーザーを取得
        User user = userDetailsImpl.getUser();
        // ユーザーのお気に入り一覧を取得
        Page<Favorite> favoritePage = favoriteRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        // モデルにお気に入り一覧を追加
        model.addAttribute("favoritePage", favoritePage);
        // お気に入り一覧ページを返却
        return "favorites/index";
    }

    /**
     * お気に入りを作成
     *
     * @param houseId          対象の民宿ID
     * @param userDetailsImpl  ログイン中のユーザー情報
     * @param redirectAttributes リダイレクト時のフラッシュメッセージ
     * @param model            モデルオブジェクト
     * @return 民宿詳細ページへのリダイレクトパス
     */
    @PostMapping("/houses/{houseId}/favorites/create")
    public String create(@PathVariable(name = "houseId") Integer houseId,
                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                         RedirectAttributes redirectAttributes, Model model) {
        // 民宿情報を取得
        House house = houseRepository.getReferenceById(houseId);
        // ログイン中のユーザーを取得
        User user = userDetailsImpl.getUser();
        // お気に入りを作成
        favoriteService.create(house, user);
        // 成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りに追加しました。");
        // 民宿詳細ページへリダイレクト
        return "redirect:/houses/" + houseId;
    }

    /**
     * お気に入りを削除
     *
     * @param houseId          対象の民宿ID
     * @param favoriteId       対象のお気に入りID
     * @param redirectAttributes リダイレクト時のフラッシュメッセージ
     * @return 民宿詳細ページへのリダイレクトパス
     */
    @PostMapping("/houses/{houseId}/favorites/{favoriteId}/delete")
    public String delete(@PathVariable(name = "houseId") Integer houseId,
                         @PathVariable(name = "favoriteId") Integer favoriteId,
                         RedirectAttributes redirectAttributes) {
        // お気に入りを削除
        favoriteRepository.deleteById(favoriteId);
        // 成功メッセージを設定
        redirectAttributes.addFlashAttribute("successMessage", "お気に入りを解除しました。");
        // 民宿詳細ページへリダイレクト
        return "redirect:/houses/" + houseId;
    }
}
