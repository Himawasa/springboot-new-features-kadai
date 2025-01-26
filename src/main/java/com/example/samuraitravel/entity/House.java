package com.example.samuraitravel.entity;

import java.sql.Timestamp; // データベースのタイムスタンプ型に対応

import jakarta.persistence.Column; // データベースのカラムを指定するアノテーション
import jakarta.persistence.Entity; // このクラスをエンティティとして指定するアノテーション
import jakarta.persistence.GeneratedValue; // 主キーの値を自動生成するためのアノテーション
import jakarta.persistence.GenerationType; // 主キーの生成戦略を指定する列挙型
import jakarta.persistence.Id; // 主キーを指定するアノテーション
import jakarta.persistence.Table; // テーブル名を指定するアノテーション

import lombok.Data; // Lombokの@Dataアノテーションで、ゲッター・セッター、toString、equals、hashCodeを自動生成

/**
 * Houseエンティティクラス
 * "houses" テーブルと対応するクラスです。
 * 民宿情報を表現するデータモデル。
 */
@Entity // このクラスがデータベースのエンティティであることを指定
@Table(name = "houses") // このエンティティが "houses" テーブルと対応していることを指定
@Data // Lombokを使用して、ゲッター、セッター、toString、equals、hashCodeを自動生成
public class House {
    
    /**
     * 主キー（ID）
     * データベースの "id" 列と対応します。
     * 自動生成されるユニークな識別子。
     */
    @Id // 主キーを指定
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    // 主キーの値をデータベースのAUTO_INCREMENT機能で自動生成
    @Column(name = "id") // データベースの列名を "id" にマッピング
    private Integer id;

    /**
     * 民宿名
     * データベースの "name" 列と対応します。
     */
    @Column(name = "name") // データベースの列名を "name" にマッピング
    private String name;

    /**
     * 民宿の画像ファイル名
     * データベースの "image_name" 列と対応します。
     * 画像ファイルのパスまたは名前を保存します。
     */
    @Column(name = "image_name") // データベースの列名を "image_name" にマッピング
    private String imageName;

    /**
     * 民宿の説明
     * データベースの "description" 列と対応します。
     * 民宿の特徴や詳細情報を記述します。
     */
    @Column(name = "description") // データベースの列名を "description" にマッピング
    private String description;

    /**
     * 宿泊料金
     * データベースの "price" 列と対応します。
     * 1泊あたりの料金（整数値）を表します。
     */
    @Column(name = "price") // データベースの列名を "price" にマッピング
    private Integer price;

    /**
     * 宿泊可能人数
     * データベースの "capacity" 列と対応します。
     * 民宿に収容可能な最大人数を表します。
     */
    @Column(name = "capacity") // データベースの列名を "capacity" にマッピング
    private Integer capacity;

    /**
     * 郵便番号
     * データベースの "postal_code" 列と対応します。
     * 民宿の所在地の郵便番号を保存します。
     */
    @Column(name = "postal_code") // データベースの列名を "postal_code" にマッピング
    private String postalCode;

    /**
     * 住所
     * データベースの "address" 列と対応します。
     * 民宿の所在地を保存します。
     */
    @Column(name = "address") // データベースの列名を "address" にマッピング
    private String address;

    /**
     * 電話番号
     * データベースの "phone_number" 列と対応します。
     * 民宿の連絡先電話番号を保存します。
     */
    @Column(name = "phone_number") // データベースの列名を "phone_number" にマッピング
    private String phoneNumber;

    /**
     * 作成日時
     * データベースの "created_at" 列と対応します。
     * レコードが作成された日時を表します。
     * データベースが自動的に値を挿入します。
     */
    @Column(name = "created_at", insertable = false, updatable = false) 
    // 挿入時のみ設定可能（更新不可）
    private Timestamp createdAt;

    /**
     * 更新日時
     * データベースの "updated_at" 列と対応します。
     * レコードが最後に更新された日時を表します。
     * データベースが自動的に値を更新します。
     */
    @Column(name = "updated_at", insertable = false, updatable = false) 
    // 更新時に自動設定（挿入時には設定不可）
    private Timestamp updatedAt;
}
