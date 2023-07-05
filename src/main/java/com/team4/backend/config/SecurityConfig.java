package com.team4.backend.config;

import com.team4.backend.config.auth.CustomAuthenticationEntryPoint;
import com.team4.backend.config.jwt.JwtAuthenticationFilter;
import com.team4.backend.config.jwt.JwtAuthorizationFilter;
import com.team4.backend.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CorsFilter corsFilter;
    private final JwtService jwtService;

    public SecurityConfig(CorsFilter corsFilter, JwtService jwtService) {
        this.corsFilter = corsFilter;
        this.jwtService = jwtService;
    }

    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsFilter) //@CrossOrigin(인증X), 시큐리티 필터에 등록 인증(O)
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, jwtService))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, jwtService));
        }
    }
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().mvcMatchers(
                "/api/v1/join/**",
                "/ws/**"
        );
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .apply(new MyCustomDsl())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint()).and()
                .authorizeRequests()
                .antMatchers("/api/v1/**").authenticated()
//                .antMatchers(HttpMethod.OPTIONS,"**").permitAll()
                .anyRequest().permitAll()
                .and().build();
    }


}
