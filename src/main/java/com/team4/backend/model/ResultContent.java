package com.team4.backend.model;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultContent {
    private String username;
    private Long id;
    private int writer_id;
    private String title;
    private String content;
    private String uploadDate;
    private boolean visible;
    private byte[] contentIMG;
    private int isImgIn;
    private byte[] userIcon;
    private String sharingCode;
}
