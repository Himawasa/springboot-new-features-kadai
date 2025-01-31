package com.example.samuraitravel.form;

import jakarta.validation.constraints.Min; // 値の最小制限をチェックするアノテーション
import jakarta.validation.constraints.NotBlank; // 空文字やnullを許容しないアノテーション
import jakarta.validation.constraints.NotNull; // nullを許容しないアノテーション

import org.springframework.web.multipart.MultipartFile; // ファイルアップロード用クラスをインポート

import lombok.AllArgsConstructor; // 全フィールドを引数に持つコンストラクタを自動生成
import lombok.Data; // ゲッター、セッター、toString、equals、hashCodeを自動生成

/**
 * HouseEditFormクラス
 * 
 * このクラスは、民宿情報の編集フォームに使用されます。
 * ユーザーが入力したデータを受け取り、バリデーションを行います。
 */
@Data // Lombokの@Dataアノテーションを使用して、ゲッター・セッター等を自動生成
@AllArgsConstructor // Lombokの@AllArgsConstructorアノテーションを使用して、全フィールドを引数に持つコンストラクタを自動生成
public class HouseEditForm {

    /**
     * 民宿のID
     * 民宿情報を一意に識別するためのフィールド。
     */
    @NotNull // nullを許容しないバリデーション
    private Integer id;    
    
    /**
     * 民宿名
     * 必須項目で、空白文字列やnullは許容されません。
     */
    @NotBlank(message = "民宿名を入力してください。") // 空白やnullを許容しないバリデーション
    private String name;
    
    /**
     * 画像ファイル
     * ユーザーがアップロードした画像を受け取るためのフィールド。
     */
    private MultipartFile imageFile;
    
    /**
     * 民宿の説明
     * 必須項目で、空白文字列やnullは許容されません。
     */
    @NotBlank(message = "説明を入力してください。") // 空白やnullを許容しないバリデーション
    private String description;   
    
    /**
     * 宿泊料金
     * 必須項目で、1円以上である必要があります。
     */
    @NotNull(message = "宿泊料金を入力してください。") // nullを許容しないバリデーション
    @Min(value = 1, message = "宿泊料金は1円以上に設定してください。") // 最小値を1に設定
    private Integer price; 
    
    /**
     * 民宿の定員
     * 必須項目で、1人以上である必要があります。
     */
    @NotNull(message = "定員を入力してください。") // nullを許容しないバリデーション
    @Min(value = 1, message = "定員は1人以上に設定してください。") // 最小値を1に設定
    private Integer capacity;       
    
    /**
     * 郵便番号
     * 必須項目で、空白文字列やnullは許容されません。
     */
    @NotBlank(message = "郵便番号を入力してください。") // 空白やnullを許容しないバリデーション
    private String postalCode;
    
    /**
     * 住所
     * 必須項目で、空白文字列やnullは許容されません。
     */
    @NotBlank(message = "住所を入力してください。") // 空白やnullを許容しないバリデーション
    private String address;
    
    /**
     * 電話番号
     * 必須項目で、空白文字列やnullは許容されません。
     */
    @NotBlank(message = "電話番号を入力してください。") // 空白やnullを許容しないバリデーション
    private String phoneNumber;
}
