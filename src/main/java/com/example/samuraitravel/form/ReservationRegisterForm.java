package com.example.samuraitravel.form;

import lombok.AllArgsConstructor; // すべてのフィールドを引数に持つコンストラクタを自動生成
import lombok.Data; // Lombokによるゲッター、セッター、toString、equals、hashCodeの自動生成

/**
 * ReservationRegisterFormクラス
 * 
 * このクラスは、予約情報を管理するためのデータモデルとして使用されます。
 * 特に、予約の登録処理においてフォームデータを一時的に保持します。
 */
@Data // Lombokの@Dataアノテーションを使用して、ゲッター、セッター、toString、equals、hashCodeを自動生成
@AllArgsConstructor // 全てのフィールドを引数に持つコンストラクタを自動生成
public class ReservationRegisterForm {    

    /**
     * 宿泊施設ID
     * - 予約する宿泊施設のIDを保持します。
     */
    private Integer houseId;
        
    /**
     * ユーザーID
     * - 予約を行ったユーザーのIDを保持します。
     */
    private Integer userId;    
        
    /**
     * チェックイン日
     * - 宿泊を開始する日付を保持します。
     * - 文字列形式（例: "2025-01-18"）。
     */
    private String checkinDate;    
        
    /**
     * チェックアウト日
     * - 宿泊を終了する日付を保持します。
     * - 文字列形式（例: "2025-01-20"）。
     */
    private String checkoutDate;    
    
    /**
     * 宿泊人数
     * - 予約に含まれる宿泊人数を保持します。
     */
    private Integer numberOfPeople;
    
    /**
     * 合計料金
     * - 宿泊料金の合計額を保持します。
     */
    private Integer amount;    
}
