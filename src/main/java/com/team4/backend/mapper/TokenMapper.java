package com.team4.backend.mapper;

import com.team4.backend.model.Token;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface TokenMapper {

    @Insert("INSERT INTO token (EMAIL,ACCESS_TOKEN,REFRESH_TOKEN, CREATE_DATE, ACCESS_TOKEN_EXPIRES_DATE, REFRESH_TOKEN_EXPIRES_DATE)" +
            "VALUES (#{email} ,#{accessToken} ,#{refreshToken} ,#{create_Date} ,#{accessTokenExpires_Date},#{refreshTokenExpires_Date})")
    void saveToken(Token token);

    @Select("SELECT * FROM token where EMAIL = #{email}")
    Optional<Token> findTokenByEmail(Token token);

    @Update("UPDATE token SET ACCESS_TOKEN = #{accessToken}, REFRESH_TOKEN = #{refreshToken},CREATE_DATE = #{create_Date},ACCESS_TOKEN_EXPIRES_DATE=#{accessTokenExpires_Date} ,REFRESH_TOKEN_EXPIRES_DATE = #{refreshTokenExpires_Date} WHERE EMAIL = #{email}")
    void updateToken(Token token);

    @Select("SELECT * FROM token where EMAIL = #{email} and REFRESH_TOKEN = #{refreshToken} and REFRESH_TOKEN_EXPIRES_DATE = #{refreshTokenExpires_Date}")
    Optional<Token> findTokenByALL(Token token);
}
