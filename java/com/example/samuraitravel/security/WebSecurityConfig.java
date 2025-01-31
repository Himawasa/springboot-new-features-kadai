package com.example.samuraitravel.security;

// Spring Security設定に必要なパッケージをインポート
import org.springframework.context.annotation.Bean; // Beanを定義するためのアノテーション
import org.springframework.context.annotation.Configuration; // コンフィギュレーションクラスのアノテーション
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // メソッドレベルのセキュリティ設定を有効化
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // HTTPセキュリティ設定を行うためのクラス
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Webセキュリティ設定を有効化
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // パスワードを暗号化するためのエンコーダー
import org.springframework.security.crypto.password.PasswordEncoder; // パスワードエンコーダーのインターフェース
import org.springframework.security.web.SecurityFilterChain; // セキュリティフィルターチェーンの構築クラス

/**
 * Spring Securityの設定クラス
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    /**
     * HTTPセキュリティ設定を構築します
     * 
     * @param http HTTPセキュリティ設定のオブジェクト
     * @return SecurityFilterChain セキュリティフィルターチェーン
     * @throws Exception 設定に失敗した場合の例外
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // すべてのユーザーにアクセスを許可するURL
                .requestMatchers(
                    "/css/**", 
                    "/images/**", 
                    "/js/**", 
                    "/storage/**", 
                    "/", 
                    "/signup/**", 
                    "/houses", 
                    "/houses/{id}", 
                    "/houses/{id}/reviews",  // 追加: レビュー一覧ページへのアクセスを許可
                    "/stripe/webhook"
                ).permitAll()
                // 管理者にのみアクセスを許可するURL
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // それ以外のリクエストには認証が必要
                .anyRequest().authenticated()
            )
            // ログイン設定
            .formLogin((form) -> form
                .loginPage("/login")              // ログインページのURL
                .loginProcessingUrl("/login")     // ログイン処理のURL
                .defaultSuccessUrl("/?loggedIn")  // ログイン成功時のリダイレクト先
                .failureUrl("/login?error")       // ログイン失敗時のリダイレクト先
                .permitAll()
            )
            // ログアウト設定
            .logout((logout) -> logout
                .logoutSuccessUrl("/?loggedOut")  // ログアウト成功時のリダイレクト先
                .permitAll()
            )
            // 特定のリクエストに対してCSRF保護を無効化
            .csrf().ignoringRequestMatchers("/stripe/webhook");
        
        return http.build();
    }

    /**
     * パスワードをハッシュ化するためのエンコーダーを定義
     * 
     * @return PasswordEncoder パスワードエンコーダー
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
