package com.team4.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    private int id;
    private String email;
    private String accessToken;
    private String refreshToken;
    private Date create_Date;
    private Date accessTokenExpires_Date;
    private Date refreshTokenExpires_Date;

    @Builder
    public Token(String email, String accessToken, String refreshToken, Date create_Date, Date accessTokenExpires_Date,Date refreshTokenExpires_Date) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.create_Date = create_Date;
        this.accessTokenExpires_Date = accessTokenExpires_Date;
        this.refreshTokenExpires_Date = refreshTokenExpires_Date;
    }
}
