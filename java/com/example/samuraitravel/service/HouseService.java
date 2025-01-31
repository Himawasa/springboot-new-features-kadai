package com.example.samuraitravel.service;

// 必要なインポート
import java.io.IOException; // 入出力例外を処理するため
import java.nio.file.Files; // ファイル操作のユーティリティ
import java.nio.file.Path; // ファイルパスを表すクラス
import java.nio.file.Paths; // ファイルパスを操作するためのユーティリティ
import java.util.UUID; // 一意のファイル名を生成するため

import org.springframework.stereotype.Service; // このクラスをサービス層として定義するアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション処理を保証するアノテーション
import org.springframework.web.multipart.MultipartFile; // アップロードされたファイルを表すクラス

import com.example.samuraitravel.entity.House; // 民宿データを表すエンティティ
import com.example.samuraitravel.form.HouseEditForm; // 民宿編集フォーム
import com.example.samuraitravel.form.HouseRegisterForm; // 民宿登録フォーム
import com.example.samuraitravel.repository.HouseRepository; // 民宿データ操作を行うリポジトリ

/**
 * 民宿に関する処理を行うサービスクラス。
 */
@Service
public class HouseService {
    private final HouseRepository houseRepository; // 民宿データを操作するリポジトリ

    /**
     * コンストラクタ
     * @param houseRepository 民宿リポジトリのインスタンス
     */
    public HouseService(HouseRepository houseRepository) {
        this.houseRepository = houseRepository;
    }

    /**
     * 民宿を登録するメソッド。
     * 
     * @param houseRegisterForm 民宿登録フォームのデータ
     */
    @Transactional // このメソッドがトランザクションとして実行されることを保証
    public void create(HouseRegisterForm houseRegisterForm) {
        House house = new House(); // 民宿エンティティを生成
        MultipartFile imageFile = houseRegisterForm.getImageFile(); // アップロードされた画像ファイルを取得

        // 画像ファイルが存在する場合、そのファイルを保存
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename(); // 元のファイル名を取得
            String hashedImageName = generateNewFileName(imageName); // 一意のファイル名を生成
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName); // 保存先のパスを指定
            copyImageFile(imageFile, filePath); // 画像ファイルをコピー
            house.setImageName(hashedImageName); // 民宿エンティティにファイル名を設定
        }

        // 民宿エンティティにフォームデータを設定
        house.setName(houseRegisterForm.getName());
        house.setDescription(houseRegisterForm.getDescription());
        house.setPrice(houseRegisterForm.getPrice());
        house.setCapacity(houseRegisterForm.getCapacity());
        house.setPostalCode(houseRegisterForm.getPostalCode());
        house.setAddress(houseRegisterForm.getAddress());
        house.setPhoneNumber(houseRegisterForm.getPhoneNumber());

        houseRepository.save(house); // データベースに保存
    }

    /**
     * 民宿情報を更新するメソッド。
     * 
     * @param houseEditForm 民宿編集フォームのデータ
     */
    @Transactional // このメソッドがトランザクションとして実行されることを保証
    public void update(HouseEditForm houseEditForm) {
        House house = houseRepository.getReferenceById(houseEditForm.getId()); // 指定IDの民宿データを取得
        MultipartFile imageFile = houseEditForm.getImageFile(); // アップロードされた画像ファイルを取得

        // 画像ファイルが存在する場合、そのファイルを保存
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename(); // 元のファイル名を取得
            String hashedImageName = generateNewFileName(imageName); // 一意のファイル名を生成
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName); // 保存先のパスを指定
            copyImageFile(imageFile, filePath); // 画像ファイルをコピー
            house.setImageName(hashedImageName); // 民宿エンティティにファイル名を設定
        }

        // 民宿エンティティにフォームデータを設定
        house.setName(houseEditForm.getName());
        house.setDescription(houseEditForm.getDescription());
        house.setPrice(houseEditForm.getPrice());
        house.setCapacity(houseEditForm.getCapacity());
        house.setPostalCode(houseEditForm.getPostalCode());
        house.setAddress(houseEditForm.getAddress());
        house.setPhoneNumber(houseEditForm.getPhoneNumber());

        houseRepository.save(house); // データベースに保存
    }

    /**
     * ファイル名をUUIDを用いて生成するメソッド。
     * 
     * @param fileName 元のファイル名
     * @return 一意に生成されたファイル名
     */
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\."); // ファイル名と拡張子を分割
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString(); // ファイル名部分をUUIDに置き換える
        }
        return String.join(".", fileNames); // ファイル名を再構築して返す
    }

    /**
     * 画像ファイルを指定したパスにコピーするメソッド。
     * 
     * @param imageFile アップロードされた画像ファイル
     * @param filePath 保存先のパス
     */
    public void copyImageFile(MultipartFile imageFile, Path filePath) {
        try {
            Files.copy(imageFile.getInputStream(), filePath); // 画像ファイルを指定のパスにコピー
        } catch (IOException e) {
            e.printStackTrace(); // 例外発生時にスタックトレースを出力
        }
    }
}
