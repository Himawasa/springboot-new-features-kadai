package com.example.samuraitravel.form;

import java.time.LocalDate; // 日付データを扱うためのクラスをインポート

import jakarta.validation.constraints.Min; // 値の最小制限をチェックするアノテーション
import jakarta.validation.constraints.NotBlank; // 空文字やnullを許容しないアノテーション
import jakarta.validation.constraints.NotNull; // nullを許容しないアノテーション

import lombok.Data; // Lombokによるゲッター、セッター、toString、equals、hashCodeの自動生成

/**
 * ReservationInputFormクラス
 * 
 * このクラスは、予約フォームの入力データを保持および検証するために使用されます。
 */
@Data // Lombokの@Dataアノテーションを使用して、ゲッター、セッター、toString、equals、hashCodeを自動生成
public class ReservationInputForm {

    /**
     * チェックイン日とチェックアウト日
     * フォーマット: "yyyy-MM-dd から yyyy-MM-dd"
     * - フォームでは1つの入力項目で指定される。
     * - 空白やnullは許容されない。
     */
    @NotBlank(message = "チェックイン日とチェックアウト日を選択してください。")
    private String fromCheckinDateToCheckoutDate;    
    
    /**
     * 宿泊人数
     * - 必須項目で、1以上である必要があります。
     */
    @NotNull(message = "宿泊人数を入力してください。") // nullを許容しないバリデーション
    @Min(value = 1, message = "宿泊人数は1人以上に設定してください。") // 最小値を1に設定
    private Integer numberOfPeople; 
    
    /**
     * チェックイン日を取得する
     * - フォームで入力された `fromCheckinDateToCheckoutDate` を分解し、チェックイン日を取得します。
     * - フォーマット: "yyyy-MM-dd から yyyy-MM-dd"
     * 
     * @return チェックイン日を `LocalDate` 型で返す
     */
    public LocalDate getCheckinDate() {
        // " から " で文字列を分割し、最初の部分がチェックイン日
        String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
        return LocalDate.parse(checkinDateAndCheckoutDate[0]);
    }

    /**
     * チェックアウト日を取得する
     * - フォームで入力された `fromCheckinDateToCheckoutDate` を分解し、チェックアウト日を取得します。
     * - フォーマット: "yyyy-MM-dd から yyyy-MM-dd"
     * 
     * @return チェックアウト日を `LocalDate` 型で返す
     */
    public LocalDate getCheckoutDate() {
        // " から " で文字列を分割し、2番目の部分がチェックアウト日
        String[] checkinDateAndCheckoutDate = getFromCheckinDateToCheckoutDate().split(" から ");
        return LocalDate.parse(checkinDateAndCheckoutDate[1]);
    }    
}
