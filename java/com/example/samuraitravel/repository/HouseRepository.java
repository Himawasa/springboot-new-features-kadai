package com.example.samuraitravel.repository;

// Java 標準ライブラリ
import java.util.List; // 複数のHouseオブジェクトを扱うためのリストクラス

// Spring Data JPAリポジトリとページングサポート
import org.springframework.data.domain.Page; // ページングされたデータを扱うためのクラス
import org.springframework.data.domain.Pageable; // ページングおよびソートの設定を提供
import org.springframework.data.jpa.repository.JpaRepository; // JPAを使用したCRUD操作を提供するインターフェース

// アプリケーション内のエンティティ
import com.example.samuraitravel.entity.House; // Houseエンティティの操作をサポート

/**
 * Houseエンティティを操作するためのリポジトリインターフェース。
 * Spring Data JPAにより、自動でCRUD機能が提供されます。
 * 
 * このインターフェースは JpaRepository を継承しているため、基本的なCRUD操作（Create, Read, Update, Delete）が
 * 自動で提供されるほか、カスタムクエリメソッドも定義できます。
 */
public interface HouseRepository extends JpaRepository<House, Integer> {

    /**
     * 名前が指定したキーワードに部分一致するHouseエンティティを検索します。
     *
     * @param keyword 検索キーワード
     * @param pageable ページング情報（ページサイズ、ソート順など）
     * @return ページングされた検索結果
     */
    public Page<House> findByNameLike(String keyword, Pageable pageable);

    /**
     * 名前または住所が指定したキーワードに部分一致するHouseエンティティを、
     * 作成日時の降順で検索します。
     *
     * @param nameKeyword 名前の検索キーワード
     * @param addressKeyword 住所の検索キーワード
     * @param pageable ページング情報
     * @return ページングされた検索結果
     */
    public Page<House> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);

    /**
     * 名前または住所が指定したキーワードに部分一致するHouseエンティティを、
     * 宿泊料金の昇順で検索します。
     *
     * @param nameKeyword 名前の検索キーワード
     * @param addressKeyword 住所の検索キーワード
     * @param pageable ページング情報
     * @return ページングされた検索結果
     */
    public Page<House> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);

    /**
     * 住所が指定したエリアに部分一致するHouseエンティティを、
     * 作成日時の降順で検索します。
     *
     * @param area エリアの検索キーワード
     * @param pageable ページング情報
     * @return ページングされた検索結果
     */
    public Page<House> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);

    /**
     * 住所が指定したエリアに部分一致するHouseエンティティを、
     * 宿泊料金の昇順で検索します。
     *
     * @param area エリアの検索キーワード
     * @param pageable ページング情報
     * @return ページングされた検索結果
     */
    public Page<House> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);

    /**
     * 宿泊料金が指定した金額以下のHouseエンティティを、
     * 作成日時の降順で検索します。
     *
     * @param price 宿泊料金の上限
     * @param pageable ページング情報
     * @return ページングされた検索結果
     */
    public Page<House> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);

    /**
     * 宿泊料金が指定した金額以下のHouseエンティティを、
     * 宿泊料金の昇順で検索します。
     *
     * @param price 宿泊料金の上限
     * @param pageable ページング情報
     * @return ページングされた検索結果
     */
    public Page<House> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable);

    /**
     * 全てのHouseエンティティを作成日時の降順で取得します。
     *
     * @param pageable ページング情報
     * @return ページングされた全てのHouseエンティティ
     */
    public Page<House> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * 全てのHouseエンティティを宿泊料金の昇順で取得します。
     *
     * @param pageable ページング情報
     * @return ページングされた全てのHouseエンティティ
     */
    public Page<House> findAllByOrderByPriceAsc(Pageable pageable);

    /**
     * 作成日時の降順でトップ10件のHouseエンティティを取得します。
     *
     * @return トップ10件のHouseエンティティをリストで返す
     */
    public List<House> findTop10ByOrderByCreatedAtDesc();
}
