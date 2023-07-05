package com.team4.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultMember {
    private int id;
    private String email;
    private String username; // email
    private String role;
    private String join_date;
    private byte[] user_icon_url;
    private String user_description;
}
