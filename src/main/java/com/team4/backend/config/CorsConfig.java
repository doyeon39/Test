package com.team4.backend.config;

import com.team4.backend.config.jwt.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;


@Configuration
@EnableWebMvc
public class CorsConfig implements WebMvcConfigurer {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.addAllowedHeader("*"); // 모든 header에 응답을 허용하겠다.
        config.addAllowedMethod("*"); // 모든 post,get,put,delete,patch 요청을 허용하겠다.
        config.setAllowedOriginPatterns(Arrays.asList("http://*.meatteam.online", "http://localhost:3000"));
        config.addExposedHeader("Access-Control-Allow-Origin");
        config.addExposedHeader(JwtProperties.HEADER_ACCESS);
        config.addExposedHeader(JwtProperties.HEADER_REFRESH);
        source.registerCorsConfiguration("/api/**",config);
        source.registerCorsConfiguration("/login",config);
        source.registerCorsConfiguration("/**",config);
        source.registerCorsConfiguration("/ws/**",config);
        return new CorsFilter(source);
    }

}
