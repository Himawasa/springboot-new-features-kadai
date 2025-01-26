package com.example.samuraitravel.form;

// Jakarta Validationのアノテーションをインポート
import jakarta.validation.constraints.NotBlank; // 空文字列やnullを拒否するアノテーション
import jakarta.validation.constraints.NotNull; // nullを拒否するアノテーション

// Hibernateのバリデーションアノテーションをインポート
import org.hibernate.validator.constraints.Length; // 文字列の長さ制限を設定するアノテーション
import org.hibernate.validator.constraints.Range; // 数値の範囲を制限するアノテーション

// Lombokのアノテーションをインポート
import lombok.AllArgsConstructor; // 全フィールドを含むコンストラクタを自動生成
import lombok.Data; // ゲッター・セッター、toString、equals、hashCodeを自動生成

/**
 * レビュー編集のためのフォームクラス。
 * 入力値のバリデーションを含む。
 */
@Data
@AllArgsConstructor
public class ReviewEditForm {

    /**
     * レビューID (必須項目)
     * nullを許可しない。
     */
    @NotNull
    private Integer id;

    /**
     * 評価スコア (必須項目)
     * nullを許可しない。1～5の範囲内でなければならない。
     */
    @NotNull(message = "評価を選択してください。")
    @Range(min = 1, max = 5, message = "評価は1～5のいずれかを選択してください。")
    private Integer score;

    /**
     * コメント内容 (必須項目)
     * 空文字列を許可しない。300文字以内である必要がある。
     */
    @NotBlank(message = "コメントを入力してください。")
    @Length(max = 300, message = "コメントは300文字以内で入力してください。")
    private String content;
}
