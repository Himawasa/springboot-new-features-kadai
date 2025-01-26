package com.example.samuraitravel.security;

// 必要なインポート文
import java.util.Collection; // ユーザーの権限情報を保持するコレクション

import org.springframework.security.core.GrantedAuthority; // 権限（ロール）を表すインターフェース
import org.springframework.security.core.userdetails.UserDetails; // Spring Security のユーザー詳細情報を管理するインターフェース

import com.example.samuraitravel.entity.User; // アプリケーションのユーザー情報を表すエンティティ

/**
 * `UserDetails` インターフェースの実装クラス。
 * Spring Security による認証処理で必要なユーザー情報を提供する。
 */
public class UserDetailsImpl implements UserDetails {
    private final User user; // ユーザーエンティティ
    private final Collection<GrantedAuthority> authorities; // ユーザーの権限情報

    /**
     * コンストラクタ
     *
     * @param user ユーザーエンティティ
     * @param authorities ユーザーの権限情報
     */
    public UserDetailsImpl(User user, Collection<GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    /**
     * ユーザーエンティティを取得する。
     *
     * @return ユーザーエンティティ
     */
    public User getUser() {
        return user;
    }

    /**
     * ユーザーのハッシュ化されたパスワードを返す。
     *
     * @return ユーザーのパスワード
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * ユーザーの認証に使用される名前（メールアドレス）を返す。
     *
     * @return ユーザーのメールアドレス
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * ユーザーに関連付けられた権限（ロール）を返す。
     *
     * @return 権限のコレクション
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * アカウントが有効期限切れでない場合に `true` を返す。
     *
     * @return 常に `true` （今回は有効期限を使用していない）
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * ユーザーアカウントがロックされていない場合に `true` を返す。
     *
     * @return 常に `true` （今回はロック機能を使用していない）
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * ユーザーのパスワードが期限切れでない場合に `true` を返す。
     *
     * @return 常に `true` （今回はパスワードの有効期限を使用していない）
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * ユーザーアカウントが有効である場合に `true` を返す。
     *
     * @return ユーザーの `enabled` フィールドの値
     */
    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }
}
