package com.example.samuraitravel.security;

// 必要なパッケージをインポート
import java.util.ArrayList; // 権限を保持するためのリストクラス
import java.util.Collection; // コレクションデータ型を使用するため

import org.springframework.security.core.GrantedAuthority; // ユーザーの権限（ロール）を表すインターフェース
import org.springframework.security.core.authority.SimpleGrantedAuthority; // 権限を具体化するクラス
import org.springframework.security.core.userdetails.UserDetails; // Spring Securityで認証対象ユーザーの詳細情報を保持するインターフェース
import org.springframework.security.core.userdetails.UserDetailsService; // 認証の際にユーザー情報をロードするためのインターフェース
import org.springframework.security.core.userdetails.UsernameNotFoundException; // ユーザーが見つからなかった場合に投げられる例外
import org.springframework.stereotype.Service; // Spring Serviceクラスとしてのアノテーション

import com.example.samuraitravel.entity.User; // ユーザー情報を表すエンティティ
import com.example.samuraitravel.repository.UserRepository; // ユーザー情報を取得するためのリポジトリ

/**
 * 認証用のユーザー情報を提供するSpring Securityのサービス実装クラス
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository; // ユーザー情報を取得するためのリポジトリ
    
    /**
     * コンストラクタ
     * @param userRepository ユーザー情報を取得するリポジトリ
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    /**
     * ユーザー名（メールアドレス）を使用して認証情報を取得します
     * 
     * @param email ログイン時に使用するメールアドレス
     * @return UserDetails 認証用のユーザー情報
     * @throws UsernameNotFoundException ユーザーが見つからない場合
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            // メールアドレスでユーザーを検索
            User user = userRepository.findByEmail(email);

            // ユーザーのロール名を取得し、権限を設定
            String userRoleName = user.getRole().getName();
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(userRoleName));

            // 認証用のUserDetailsを返却
            return new UserDetailsImpl(user, authorities);
        } catch (Exception e) {
            throw new UsernameNotFoundException("ユーザーが見つかりませんでした。");
        }
    }
}
