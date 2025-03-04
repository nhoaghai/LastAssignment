package com.example.demo_clothes_shop_23.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomFilter customFilter;

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder);
        authProvider.setUserDetailsService(customUserDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    //cấu hình path
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable());

        //TODO: cấu hình path dựa trn role
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers(
                "/api/admin/**",
                "/admin/**")
                .hasRole("ADMIN");
            auth.requestMatchers(
                "/cart/**",
                "/favorite/**",
                "/checkout/**",
                "/payment-success/**",
                "/user-info",
                "/checkout",
                "/cod-Return",
                "/order-history",
                "/payment_return")
                .authenticated();
            auth.requestMatchers(
                "/api/carts/**",
                "/api/reviews/**",
                "/api/comments/**",
                "/api/favorites/**",
                "/api/addresses",
                "/api/auth/updateInfo",
                "/api/auth/update-password/**",
                "/api/coupons/**",
                "/api/orders/**",
                "/api/orderDetails/**",
                "api/payments/")
                .authenticated();
            auth.anyRequest().permitAll();
        });

        //cấu hình login
        http.formLogin(formLogin->{
            formLogin.loginPage("/login"); //trang login do mình thiết kế
            formLogin.defaultSuccessUrl("/",true); //login thành công chuyenr hg về trang chủ
           formLogin.permitAll();//login ai cx truy cập được
        });


        //cấu hình logout
        http.logout(logout->{
            logout.logoutSuccessUrl("/"); // nếu logout thành công thì chuyển về trng chủ
            logout.deleteCookies("JSESSIONID"); // xóa cookie
            logout.invalidateHttpSession(true);//hủy session
            logout.clearAuthentication(true);//xóa thông tin xác thực
            logout.permitAll();//ai cx truy cập được
        });

        //Cấu hình xác thực
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }




}
