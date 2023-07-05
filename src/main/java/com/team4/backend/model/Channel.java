package com.team4.backend.model;

import com.team4.backend.util.CodeGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    private int channel_UID;
    private String channel_title;
    private String channel_icon_url;
    private String channel_description;
    private int channel_owner;
    private String channel_inviteCode;
    private boolean channel_is_Open;
    private Timestamp channel_createDate;
    private String channel_type;

    @Builder
    public Channel(String serverName, String fileURL, int nickName){
        this.channel_title = serverName;
        this.channel_icon_url = fileURL;
        this.channel_description = channel_title +"님 환영합니다.";
        this.channel_owner = nickName;
        this.channel_inviteCode = CodeGenerator.createCode();
    }
}
