package com.example.samuraitravel.controller;

// 必要なクラスをインポート
import org.springframework.data.domain.Page; // ページネーション用
import org.springframework.data.domain.Pageable; // ページ情報を扱うクラス
import org.springframework.data.domain.Sort.Direction; // ソート順を指定するためのクラス
import org.springframework.data.web.PageableDefault; // ページネーションのデフォルト設定
import org.springframework.stereotype.Controller; // コントローラクラスとして定義するアノテーション
import org.springframework.ui.Model; // ビューにデータを渡すためのクラス
import org.springframework.validation.BindingResult; // バリデーション結果を扱うクラス
import org.springframework.validation.annotation.Validated; // バリデーションを適用するアノテーション
// 各種リクエストマッピング用アノテーションをインポート
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // リダイレクト時にデータを渡すためのクラス

import com.example.samuraitravel.entity.House; // 民宿のエンティティ
import com.example.samuraitravel.form.HouseEditForm; // 民宿編集用のフォーム
import com.example.samuraitravel.form.HouseRegisterForm; // 民宿登録用のフォーム
import com.example.samuraitravel.repository.HouseRepository; // 民宿リポジトリ（データ操作用）
import com.example.samuraitravel.service.HouseService; // 民宿のビジネスロジックを扱うサービスクラス

/**
 * AdminHouseControllerクラス
 * 管理者が民宿情報を管理するためのコントローラークラス。
 * 登録、編集、削除、閲覧などの操作を提供する。
 */
@Controller
@RequestMapping("/admin/houses") // "/admin/houses" のリクエストを処理
public class AdminHouseController {
    private final HouseRepository houseRepository; // 民宿情報を操作するためのリポジトリ
    private final HouseService houseService; // 民宿に関するビジネスロジックを実行するサービス
    
    /**
     * コンストラクタ
     * リポジトリとサービスをDI（依存性注入）で受け取る
     */
    public AdminHouseController(HouseRepository houseRepository, HouseService houseService) {
        this.houseRepository = houseRepository;
        this.houseService = houseService;
    }
    
    /**
     * 民宿の一覧ページを表示する
     * @param model ビューに渡すデータを格納
     * @param pageable ページネーションの設定
     * @param keyword 検索キーワード（オプション）
     * @return 民宿一覧ページ
     */
    @GetMapping
    public String index(Model model, 
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable, 
                        @RequestParam(name = "keyword", required = false) String keyword) {
        Page<House> housePage; // ページネーションされた民宿リストを格納
        
        // キーワードで検索
        if (keyword != null && !keyword.isEmpty()) {
            housePage = houseRepository.findByNameLike("%" + keyword + "%", pageable);
        } else {
            housePage = houseRepository.findAll(pageable); // 全件取得
        }
        
        model.addAttribute("housePage", housePage); // ページネーション情報をビューに渡す
        model.addAttribute("keyword", keyword); // 検索キーワードをビューに渡す
        
        return "admin/houses/index"; // 一覧ページのテンプレート名
    }
    
    /**
     * 民宿の詳細ページを表示する
     * @param id 民宿のID
     * @param model ビューに渡すデータ
     * @return 詳細ページ
     */
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id); // 指定IDの民宿を取得
        model.addAttribute("house", house); // ビューに民宿データを渡す
        return "admin/houses/show"; // 詳細ページのテンプレート名
    }
    
    /**
     * 民宿登録ページを表示する
     * @param model ビューに渡すデータ
     * @return 登録ページ
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("houseRegisterForm", new HouseRegisterForm()); // 空のフォームをビューに渡す
        return "admin/houses/register"; // 登録ページのテンプレート名
    }
    
    /**
     * 民宿を登録する
     * @param houseRegisterForm 登録用フォームの入力データ
     * @param bindingResult バリデーション結果
     * @param redirectAttributes リダイレクト時のメッセージ設定
     * @return 一覧ページへのリダイレクト
     */
    @PostMapping("/create")
    public String create(@ModelAttribute @Validated HouseRegisterForm houseRegisterForm, 
                         BindingResult bindingResult, 
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) { // バリデーションエラーがある場合
            return "admin/houses/register"; // 登録ページに戻る
        }
        
        houseService.create(houseRegisterForm); // 登録処理を実行
        redirectAttributes.addFlashAttribute("successMessage", "民宿を登録しました。"); // 成功メッセージを設定
        
        return "redirect:/admin/houses"; // 一覧ページにリダイレクト
    }
    
    /**
     * 民宿編集ページを表示する
     * @param id 民宿のID
     * @param model ビューに渡すデータ
     * @return 編集ページ
     */
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id); // 民宿情報を取得
        String imageName = house.getImageName(); // 画像名を取得
        // 民宿情報をフォーム用オブジェクトに変換
        HouseEditForm houseEditForm = new HouseEditForm(house.getId(), house.getName(), null, house.getDescription(), 
                                                        house.getPrice(), house.getCapacity(), house.getPostalCode(), 
                                                        house.getAddress(), house.getPhoneNumber());
        
        model.addAttribute("imageName", imageName); // 画像名をビューに渡す
        model.addAttribute("houseEditForm", houseEditForm); // フォームデータをビューに渡す
        
        return "admin/houses/edit"; // 編集ページのテンプレート名
    }
    
    /**
     * 民宿情報を更新する
     * @param houseEditForm 編集用フォームの入力データ
     * @param bindingResult バリデーション結果
     * @param redirectAttributes リダイレクト時のメッセージ設定
     * @return 一覧ページへのリダイレクト
     */
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute @Validated HouseEditForm houseEditForm, 
                         BindingResult bindingResult, 
                         RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) { // バリデーションエラーがある場合
            return "admin/houses/edit"; // 編集ページに戻る
        }
        
        houseService.update(houseEditForm); // 更新処理を実行
        redirectAttributes.addFlashAttribute("successMessage", "民宿情報を編集しました。"); // 成功メッセージを設定
        
        return "redirect:/admin/houses"; // 一覧ページにリダイレクト
    }
    
    /**
     * 民宿を削除する
     * @param id 民宿のID
     * @param redirectAttributes リダイレクト時のメッセージ設定
     * @return 一覧ページへのリダイレクト
     */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {
        houseRepository.deleteById(id); // 指定IDの民宿を削除
        redirectAttributes.addFlashAttribute("successMessage", "民宿を削除しました。"); // 成功メッセージを設定
        return "redirect:/admin/houses"; // 一覧ページにリダイレクト
    }
}
