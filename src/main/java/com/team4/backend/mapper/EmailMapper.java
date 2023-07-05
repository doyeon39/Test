package com.team4.backend.mapper;

import com.team4.backend.model.dto.EmailAuthenticationDTO;
import org.apache.ibatis.annotations.*;

import java.util.Optional;

@Mapper
public interface EmailMapper {

    @Insert("insert into emailauthentication values(#{email},#{auth} )")
    void save(EmailAuthenticationDTO auth);

    @Select("select * from emailauthentication where EMAIL = #{email}")
    Optional<EmailAuthenticationDTO> findEmail(String email);

    @Select("select * from emailauthentication where EMAIL = #{email} and AUTH=#{auth}")
    Optional<EmailAuthenticationDTO> findAuth(EmailAuthenticationDTO auth);

    @Select("select * from emailauthentication where EMAIL = #{email} and AUTH=#{auth}")
    Optional<EmailAuthenticationDTO> findAuth2(@Param("email") String email, @Param("auth") String auth);

    @Update("Update emailauthentication set AUTH = #{auth} where EMAIL = #{email}")
    void update(EmailAuthenticationDTO auth);
}
