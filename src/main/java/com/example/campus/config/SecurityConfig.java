package com.example.campus.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // 公开页面无需登录
                .requestMatchers("/", "/activities", "/activities/**",
                        "/news", "/news/**", "/consultations", "/consultations/**",
                        "/login", "/css/**", "/js/**", "/error").permitAll()
                // 管理端权限细分（具体路径在前，通配路径在后）
                .requestMatchers("/admin/news/**").hasRole("ADMIN")
                .requestMatchers("/admin/consult/**").hasAnyRole("ADMIN", "EVENT_ADMIN")
                .requestMatchers("/admin/union/**").hasAnyRole("ADMIN", "UNION_ADMIN")
                .requestMatchers("/admin/activity/stat/**").hasAnyRole("ADMIN", "EVENT_ADMIN", "CLUB_LEADER", "TEACHER")
                .requestMatchers("/admin/activity/**").hasAnyRole("ADMIN", "EVENT_ADMIN", "CLUB_LEADER", "UNION_ADMIN")
                .requestMatchers("/admin/**").hasAnyRole("ADMIN", "EVENT_ADMIN", "CLUB_LEADER", "UNION_ADMIN")
                // 其余用户功能需登录
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/", true)
                .failureUrl("/login?error"))
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/").permitAll())
            .exceptionHandling(ex -> ex
                .accessDeniedHandler(jsonAccessDeniedHandler()))
            .userDetailsService(userDetailsService);
        return http.build();
    }

    /** 403 权限拒绝返回 JSON 错误信息。 */
    @Bean
    public AccessDeniedHandler jsonAccessDeniedHandler() {
        return (request, response, ex) -> {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(403);
            response.getWriter().write("{\"code\":403,\"message\":\"权限不足，无法访问该资源\"}");
        };
    }
}
