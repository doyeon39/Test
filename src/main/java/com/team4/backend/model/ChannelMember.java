package com.team4.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelMember {
    private int channel_UID;
    private int member_UID;
    private String channel_member_authority;
    private Timestamp channel_member_joindate;
    private String channel_type;
}
