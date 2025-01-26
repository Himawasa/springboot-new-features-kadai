package com.example.samuraitravel.controller;

import org.springframework.data.domain.Page; // ページネーション用
import org.springframework.data.domain.Pageable; // ページ情報
import org.springframework.data.domain.Sort.Direction; // 並び順の方向（ASC/DESC）
import org.springframework.data.web.PageableDefault; // ページネーションのデフォルト設定
import org.springframework.stereotype.Controller; // コントローラのアノテーション
import org.springframework.ui.Model; // ビューにデータを渡すためのオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // GETリクエスト用のマッピング
import org.springframework.web.bind.annotation.PathVariable; // パス変数を取得するためのアノテーション
import org.springframework.web.bind.annotation.RequestMapping; // コントローラのベースURLを設定
import org.springframework.web.bind.annotation.RequestParam; // クエリパラメータを取得するためのアノテーション

import com.example.samuraitravel.entity.House; // 民宿エンティティ
import com.example.samuraitravel.form.ReservationInputForm; // 予約入力フォームクラス
import com.example.samuraitravel.repository.HouseRepository; // 民宿リポジトリ

/**
 * 民宿の一覧表示・詳細表示を管理するコントローラクラス。
 */
@Controller
@RequestMapping("/houses") // このコントローラのURLパスのベースを "/houses" に設定
public class HouseController {
    private final HouseRepository houseRepository; // 民宿データ操作用リポジトリ
    
    /**
     * コンストラクタ
     * @param houseRepository 民宿データを操作するためのリポジトリ
     */
    public HouseController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;            
    }     
  
    /**
     * 民宿の一覧ページを表示するメソッド。
     * @param keyword 民宿名や住所の検索キーワード（任意）
     * @param area エリアの検索キーワード（任意）
     * @param price 予算の上限（任意）
     * @param order ソート順（任意、価格昇順/作成日時降順）
     * @param pageable ページネーション情報
     * @param model ビューに渡すデータを格納するオブジェクト
     * @return houses/index テンプレート名
     */
    @GetMapping
    public String index(@RequestParam(name = "keyword", required = false) String keyword,
                        @RequestParam(name = "area", required = false) String area,
                        @RequestParam(name = "price", required = false) Integer price,  
                        @RequestParam(name = "order", required = false) String order,
                        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.ASC) Pageable pageable,
                        Model model) 
    {
        Page<House> housePage; // ページングされた民宿データを格納
        
        // 検索条件に基づいてデータを取得
        if (keyword != null && !keyword.isEmpty()) {            
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByPriceAsc("%" + keyword + "%", "%" + keyword + "%", pageable);
            } else {
                housePage = houseRepository.findByNameLikeOrAddressLikeOrderByCreatedAtDesc("%" + keyword + "%", "%" + keyword + "%", pageable);
            }            
        } else if (area != null && !area.isEmpty()) {            
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByAddressLikeOrderByPriceAsc("%" + area + "%", pageable);
            } else {
                housePage = houseRepository.findByAddressLikeOrderByCreatedAtDesc("%" + area + "%", pageable);
            }            
        } else if (price != null) {            
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findByPriceLessThanEqualOrderByPriceAsc(price, pageable);
            } else {
                housePage = houseRepository.findByPriceLessThanEqualOrderByCreatedAtDesc(price, pageable);
            }            
        } else {            
            if (order != null && order.equals("priceAsc")) {
                housePage = houseRepository.findAllByOrderByPriceAsc(pageable);
            } else {
                housePage = houseRepository.findAllByOrderByCreatedAtDesc(pageable);   
            }            
        }                
        
        // ビューにデータを渡す
        model.addAttribute("housePage", housePage); // ページングされた民宿データ
        model.addAttribute("keyword", keyword); // 検索キーワード
        model.addAttribute("area", area); // エリア情報
        model.addAttribute("price", price); // 価格情報
        model.addAttribute("order", order); // ソート順
        
        return "houses/index"; // ビュー名を返す
    }
    
    /**
     * 民宿の詳細ページを表示するメソッド。
     * @param id 民宿ID
     * @param model ビューに渡すデータを格納するオブジェクト
     * @return houses/show テンプレート名
     */
    @GetMapping("/{id}")
    public String show(@PathVariable(name = "id") Integer id, Model model) {
        House house = houseRepository.getReferenceById(id); // 民宿データを取得
        
        model.addAttribute("house", house); // 民宿情報をビューに渡す
        model.addAttribute("reservationInputForm", new ReservationInputForm()); // 予約入力フォームをビューに渡す
        
        return "houses/show"; // ビュー名を返す
    }    
}
