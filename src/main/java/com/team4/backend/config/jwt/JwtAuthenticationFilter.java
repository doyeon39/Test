package com.team4.backend.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.team4.backend.config.auth.PrincipalDetails;
import com.team4.backend.model.Token;
import com.team4.backend.model.User;
import com.team4.backend.model.dto.ResultDtoProperties;
import com.team4.backend.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도 - EMAIL && PASSWORD 검증 (JwtAuthenticationFilter.attemptAuthentication)");
        ObjectMapper om = new ObjectMapper();
        try {
            User user = om.readValue(request.getInputStream(), User.class);

            jwtService.checkLoginInfo(user);
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("JwtAuthenticationFilter.successfulAuthentication");

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        User user = principalDetails.getUser();

        Map<String, Object> accessJwtMap = jwtService.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        Map<String, Object> refreshJwtMap = jwtService.createRefreshToken(user.getEmail(), user.getRole());

        Token token = jwtService.createTokenDto(user.getEmail(),accessJwtMap,refreshJwtMap);
        jwtService.saveToken(token);

        response.addHeader(JwtProperties.HEADER_ACCESS, JwtProperties.TOKEN_PREFIX + token.getAccessToken());
        response.addHeader(JwtProperties.HEADER_REFRESH, JwtProperties.TOKEN_PREFIX + token.getRefreshToken());
        setSuccessResponse(response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        System.out.println("인증실패 : unsuccessfulAuthentication");
        Class<? extends AuthenticationException> exceptionType = failed.getClass();
        System.out.println(exceptionType);

        setFailResponse(response, failed.getMessage());
    }

    private void setSuccessResponse(HttpServletResponse response) throws IOException {
        response.setCharacterEncoding("UTF-8");

        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();

        responseJson.put(ResultDtoProperties.MESSAGE, "로그인 성공");
        responseJson.put(ResultDtoProperties.STATUS, true);
        response.getWriter().write(om.writeValueAsString(responseJson));
    }

    private void setFailResponse(HttpServletResponse response, String message) throws IOException {
        response.setCharacterEncoding("UTF-8");

        ObjectMapper om = new ObjectMapper();
        ObjectNode responseJson = om.createObjectNode();

        responseJson.put(ResultDtoProperties.MESSAGE, message);
        responseJson.put(ResultDtoProperties.STATUS, false);
        response.getWriter().write(om.writeValueAsString(responseJson));
    }
}
