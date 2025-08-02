package com.example.project_hardware.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // 스프링 시큐리티 관련 메소드
        http
                .csrf(AbstractHttpConfigurer::disable)
                //CSRF(사이트 간 요청 위조) 공격 방어 일단 비활성화 - RESTFUL API 까지 고려
                //cors - 특정 서버로만 데이터를 넘길 수 있도록 설정 - 이 부분은 생략
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                //세션 설정
                .authorizeHttpRequests(authz->authz.requestMatchers("/", "/loginPage", "/logout", "/registerPage", "/bbs/board", "/bbs/view/**", "/img/**")
                        .permitAll()
                        .requestMatchers(HttpMethod.POST, "/login", "/register").permitAll()
                        .requestMatchers("/resources/**", "/WEB-INF/**").permitAll()
                        .requestMatchers("/api/v1/board", "/api/v1/boardView/**", "/api/v1/delete/**", "/api/v1e/board", "/ap1/v1/rollback").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/count/**").permitAll()
                        .requestMatchers("/bbs/insert", "/bbs/update/**").hasAnyAuthority("ADMIN", "MANAGER", "MEMBER")
                        .requestMatchers(HttpMethod.POST, "/api/v1/write", "/api/v1/uploadimg").hasAnyAuthority("ADMIN", "MANAGER", "MEMBER")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/modify/**").hasAnyAuthority("ADMIN", "MANAGER", "MEMBER")
                        //.requestMatchers(HttpMethod.DELETE, ).hasAnyAuthority("ADMIN", "MANAGER", "MEMBER")
                        .anyRequest().authenticated() //로그인을 해야지만 접근이 가능하게끔 만듬. 그렇기 때문에 로그인 페이지로 자동 이동
                        // RESTFUL API의 스프링 시큐리티 권한설정을 좀더 강화(2025-07-24)

                )

                .formLogin(
                        login->login.loginPage("/loginPage") //url을 작성해서 로그인페이지로 이동할때
                                .loginProcessingUrl("/login")
                                .failureUrl("/loginPage?error=true")
                                .usernameParameter("id")
                                .passwordParameter("password")
                                .successHandler(authenticationSuccessHandler()) //로그인 성공 후 어느 페이지로 이동하는지를 안내하며 동시에 세션과 연관
                                .permitAll()
                )

                .logout(logout->logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))// logout URL을 통해서 로그아웃이 진행됨
                        .logoutSuccessUrl("/")//로그아웃 성공 후 이 URL로 리다이렉팅
                        .invalidateHttpSession(true)//세션무효화 => 세션공간안에 있던 데이터 사라짐
                        .deleteCookies("JSESSIONID")//쿠키삭제
                        .permitAll()
                );
        
        return http.build();
        //최종 http에 적용시킬때 사용하는 메소드

    }

    //successHandler를 따로 작성해 주어야만 한다
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new SimpleUrlAuthenticationSuccessHandler() {

            //이런건 기능암기보다는 개발할때 따라 쓰는 식으로 개발
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                Authentication authentication) throws IOException, ServletException {
                //로그인이 성공했을 때 우리가 특별기능을 넣고 싶을 때(세션과 권한 기능)
                HttpSession session = request.getSession(); // 세션 기능 가져온 것
                boolean isManager = authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority ->
                                grantedAuthority.getAuthority().equals("ADMIN") ||
                                grantedAuthority.getAuthority().equals("MANAGER"));
                if (isManager) {
                    session.setAttribute("MANAGER", true);
                }
                //관리자 전용 기능 사용위해 다음의 로직을 추가
                boolean isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(grantedAuthority ->
                                grantedAuthority.getAuthority().equals("ADMIN"));
                if (isAdmin) {
                    session.setAttribute("ADMIN", true);
                }
                session.setAttribute("id", authentication.getName());
                // 로그인할때 form에서 전달받은 사용자 이름을 다시 세션에 집어넣는다.
                //세션에다가 로그인한 아이디를 저장한다.
                session.setAttribute("isAuthenticated", true);
                //세션에다가 로그인 여부를 저장
                // request.getContextPath() -> localhost:8080
                response.sendRedirect(request.getContextPath() + "/");
                super.onAuthenticationSuccess(request, response, authentication);
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //DB에 패스워드를 암호화해서 저장
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public CorsConfigurationSource CorsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080","http://localhost:8080"));
//        //localhost:8080서버에서는 프론트에서 백엔드단 혹은 백엔드단에서 프론트단으로 데이터를 주고받을수 있게 만든것
//        //일반적으로는 프론트단 localhost:3000 백엔드단 localhost:8080
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
