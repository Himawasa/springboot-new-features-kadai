package com.example.samuraitravel.controller;

import java.util.List; // リスト型のインポート

import org.springframework.stereotype.Controller; // Spring MVCのコントローラであることを示すアノテーション
import org.springframework.ui.Model; // ビューにデータを渡すためのオブジェクト
import org.springframework.web.bind.annotation.GetMapping; // GETリクエストを処理するためのアノテーション

import com.example.samuraitravel.entity.House; // 民宿エンティティ
import com.example.samuraitravel.repository.HouseRepository; // 民宿データベース操作用のリポジトリ

/**
 * ホーム画面を管理するコントローラ
 * ユーザーが最初にアクセスするホームページを表示するための処理を担当。
 */
@Controller
public class HomeController {
    private final HouseRepository houseRepository; // 民宿データ操作を行うリポジトリ
    
    /**
     * コンストラクタ
     * @param houseRepository 民宿データを操作するリポジトリ
     */
    public HomeController(HouseRepository houseRepository) {
        this.houseRepository = houseRepository; // houseRepositoryをDIで注入
    }    
    
    /**
     * ホーム画面を表示する
     * @param model ビューにデータを渡すためのオブジェクト
     * @return ホーム画面のテンプレート名
     */
    @GetMapping("/")    
    public String index(Model model) {
        // 最新の10件の民宿データを取得する
        List<House> newHouses = houseRepository.findTop10ByOrderByCreatedAtDesc();
        // ビューに "newHouses" という名前でデータを渡す
        model.addAttribute("newHouses", newHouses);        
        
        // ホーム画面のテンプレート "index" を返す
        return "index";
    }   
}
