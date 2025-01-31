package com.example.samuraitravel.service;

// 必要なインポート
import java.time.LocalDate; // 日付を表すクラス
import java.time.temporal.ChronoUnit; // 日付間の日数を計算するためのユーティリティ
import java.util.Map; // 支払い情報を格納するためのマップ

import org.springframework.stereotype.Service; // サービス層として定義するためのアノテーション
import org.springframework.transaction.annotation.Transactional; // トランザクション処理を保証するアノテーション

import com.example.samuraitravel.entity.House; // 民宿データを表すエンティティ
import com.example.samuraitravel.entity.Reservation; // 予約データを表すエンティティ
import com.example.samuraitravel.entity.User; // ユーザーデータを表すエンティティ
import com.example.samuraitravel.repository.HouseRepository; // 民宿データを操作するリポジトリ
import com.example.samuraitravel.repository.ReservationRepository; // 予約データを操作するリポジトリ
import com.example.samuraitravel.repository.UserRepository; // ユーザーデータを操作するリポジトリ

/**
 * 予約に関する処理を行うサービスクラス。
 */
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository; // 予約データを操作するリポジトリ
    private final HouseRepository houseRepository; // 民宿データを操作するリポジトリ
    private final UserRepository userRepository; // ユーザーデータを操作するリポジトリ

    /**
     * コンストラクタ
     * @param reservationRepository 予約リポジトリのインスタンス
     * @param houseRepository 民宿リポジトリのインスタンス
     * @param userRepository ユーザーリポジトリのインスタンス
     */
    public ReservationService(ReservationRepository reservationRepository, HouseRepository houseRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.houseRepository = houseRepository;
        this.userRepository = userRepository;
    }

    /**
     * 支払い情報を基に予約データを作成して保存するメソッド。
     * 
     * @param paymentIntentObject 支払い情報を格納したマップ
     */
    @Transactional // このメソッドがトランザクションとして実行されることを保証
    public void create(Map<String, String> paymentIntentObject) {
        Reservation reservation = new Reservation(); // 新しい予約エンティティを作成

        // 支払い情報から民宿IDとユーザーIDを取得
        Integer houseId = Integer.valueOf(paymentIntentObject.get("houseId"));
        Integer userId = Integer.valueOf(paymentIntentObject.get("userId"));

        // 民宿情報とユーザー情報をリポジトリから取得
        House house = houseRepository.getReferenceById(houseId);
        User user = userRepository.getReferenceById(userId);

        // 支払い情報からチェックイン日、チェックアウト日、宿泊人数、料金を取得
        LocalDate checkinDate = LocalDate.parse(paymentIntentObject.get("checkinDate"));
        LocalDate checkoutDate = LocalDate.parse(paymentIntentObject.get("checkoutDate"));
        Integer numberOfPeople = Integer.valueOf(paymentIntentObject.get("numberOfPeople"));
        Integer amount = Integer.valueOf(paymentIntentObject.get("amount"));

        // 取得した情報を予約エンティティに設定
        reservation.setHouse(house);
        reservation.setUser(user);
        reservation.setCheckinDate(checkinDate);
        reservation.setCheckoutDate(checkoutDate);
        reservation.setNumberOfPeople(numberOfPeople);
        reservation.setAmount(amount);

        // 予約データをデータベースに保存
        reservationRepository.save(reservation);
    }

    /**
     * 宿泊人数が民宿の定員を超えていないかを確認するメソッド。
     * 
     * @param numberOfPeople 宿泊人数
     * @param capacity 民宿の定員
     * @return 宿泊人数が定員以下の場合はtrue、そうでない場合はfalse
     */
    public boolean isWithinCapacity(Integer numberOfPeople, Integer capacity) {
        return numberOfPeople <= capacity;
    }

    /**
     * 宿泊料金を計算するメソッド。
     * 
     * @param checkinDate チェックイン日
     * @param checkoutDate チェックアウト日
     * @param price 1泊あたりの料金
     * @return 宿泊料金の合計
     */
    public Integer calculateAmount(LocalDate checkinDate, LocalDate checkoutDate, Integer price) {
        // チェックイン日からチェックアウト日までの日数を計算
        long numberOfNights = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

        // 宿泊料金を計算（1泊料金 × 宿泊日数）
        int amount = price * (int) numberOfNights;

        return amount;
    }
}
