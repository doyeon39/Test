package com.team4.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.team4.backend.config.auth.PrincipalDetails;
import com.team4.backend.config.jwt.JwtProperties;
import com.team4.backend.mapper.MemberMapper;
import com.team4.backend.mapper.TokenMapper;
import com.team4.backend.model.Token;
import com.team4.backend.model.User;
import com.team4.backend.config.jwt.JwtError;
import com.team4.backend.model.dto.ResultDTO;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class JwtService {

    private final MemberMapper memberMapper;
    private final TokenMapper tokenMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public JwtService(MemberMapper memberMapper, TokenMapper tokenMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.memberMapper = memberMapper;
        this.tokenMapper = tokenMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void checkLoginInfo(User user) {
        String userPassword = memberMapper.findUsername(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException(JwtError.JWT_MEMBER_NOT_FOUND_USERNAME)).getPassword();
        if (!bCryptPasswordEncoder.matches(user.getPassword(), userPassword)) {
            throw new BadCredentialsException(JwtError.JWT_MEMBER_PASSWORD_WRONG);
        }
    }

    /**
     * @return (" accessJwt ", jwt) <br> ("Expires_Date", accessTokenExpires_Date)
     */
    public Map<String, Object> createAccessToken(int id, String userEmail, String userRole) {
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HMAC512");
        headerMap.put("type", "jwt");

        Map<String, Object> payload = new HashMap<>();
        payload.put("id", id);
        payload.put("email", userEmail);
        payload.put("USER_ROLE", userRole);

        Date accessTokenExpires_Date = new Date(System.currentTimeMillis() + JwtProperties.ACCESS_TOKEN_EXPIRATION_TIME);
        String jwt = JWT.create()
                .withSubject("Login")
                .withHeader(headerMap)
                .withPayload(payload)
                .withExpiresAt(accessTokenExpires_Date)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));

        Map<String, Object> list = new HashMap<>();
        list.put(JwtProperties.HEADER_ACCESS, jwt);
        list.put(JwtProperties.EXPIRED_DATE, accessTokenExpires_Date);
        return list;
    }

    /**
     * @return (" refreshJwt ", jwt) <br> ("Expires_Date", refreshTokenExpires_Date)
     */
    public Map<String, Object> createRefreshToken(String userEmail, String userRole) {
        Date refreshTokenExpires_Date = new Date(System.currentTimeMillis() + JwtProperties.REFRESH_TOKEN_EXPIRATION_TIME);
        String jwt = JWT.create()
                .withClaim("email", userEmail)
                .withClaim("USER_ROLE", userRole)
                .withExpiresAt(refreshTokenExpires_Date)
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        Map<String, Object> list = new HashMap<>();
        list.put(JwtProperties.HEADER_REFRESH, jwt);
        list.put(JwtProperties.EXPIRED_DATE, refreshTokenExpires_Date);
        return list;
    }

    public Token createTokenDto(String email, Map<String, Object> accessJwtMap, Map<String, Object> refreshJwtMap) {
        String accessJwt = (String) accessJwtMap.get(JwtProperties.HEADER_ACCESS);
        String refreshJwt = (String) refreshJwtMap.get(JwtProperties.HEADER_REFRESH);

        Date create_Date = new Date(System.currentTimeMillis());
        Date accessJwtExpires = (Date) accessJwtMap.get(JwtProperties.EXPIRED_DATE);
        Date refreshJwtExpires = (Date) refreshJwtMap.get(JwtProperties.EXPIRED_DATE);

        return Token.builder()
                .email(email)
                .accessToken(accessJwt)
                .refreshToken(refreshJwt)
                .create_Date(create_Date)
                .accessTokenExpires_Date(accessJwtExpires)
                .refreshTokenExpires_Date(refreshJwtExpires)
                .build();
    }

    public void saveToken(Token token) {
        tokenMapper.saveToken(token);
    }

    public String findTokenType(HttpServletRequest request) {
        Iterator<String> iterator = request.getHeaderNames().asIterator();
        while (iterator.hasNext()) {
            String index = iterator.next();
            if (index.contains("jwt")) {
                return index.replace("jwt", "Jwt");
            }
        }
        throw new BadCredentialsException("토큰을 찾을 수 없습니다.");
    }

    public String getTokenFromHeader(HttpServletRequest request, String tokenType) {
        String token = checkTokenPrefix(request.getHeader(tokenType)) ? request.getHeader(tokenType) : null;
        if (token == null)
            throw new BadCredentialsException("Bear 토큰이 아닙니다.");
        return token;
    }

    private boolean checkTokenPrefix(String token) {
        return token.startsWith(JwtProperties.TOKEN_PREFIX);
    }

    public DecodedJWT decodedJWT(String jwtToken) {
        return JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
                .build()
                .verify(jwtToken);
    }

    public <T> T getClaim(DecodedJWT token, String key, Class<T> valueType) {
        Map<String, Claim> claims = token.getClaims();
        Claim claim = claims.get(key);
        if (claim != null) {
            if (valueType == String.class)
                return valueType.cast(claim.asString());
            else if (valueType == Integer.class)
                return valueType.cast(claim.asInt());
            else if (valueType == Date.class)
                return valueType.cast(claim.asDate());
        }
        System.out.println("getClaim: 매개변수를 다시 확인해주세요");
        return null; //Exception 핸들링해도될듯?
    }

    public User findUserByEmail(String email) {
        return memberMapper.findUserByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email + "::유저를 찾을 수 없습니다."));
    }
    public void updateToken(Token token){
        tokenMapper.updateToken(token);
    }
    public ResultDTO getResult(boolean status, String message){
        return ResultDTO.builder()
                .status(status)
                .message(message)
                .build();
    }

}
