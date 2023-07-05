package com.team4.backend.mapper;

import com.team4.backend.model.Channel;
import com.team4.backend.model.ChannelMember;
import com.team4.backend.model.dto.MyChannelsDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ChannelMapper {

    @Select("select m.CHANNEL_UID, m.MEMBER_UID, c.CHANNEL_TITLE, c.CHANNEL_ICON_URL, c.CHANNEL_TYPE,c.CHANNEL_INVITE_CODE " +
            "from channelmember m " +
            "left join channel c on m.CHANNEL_UID = c.CHANNEL_UID " +
            "where m.MEMBER_UID = #{memberUID} order by CHANNEL_MEMBER_JOINDATE desc")
    List<MyChannelsDTO> findChannelsByMemberUID(@Param("memberUID") int memberUID);


    @Insert("Insert into channel (CHANNEL_TITLE, CHANNEL_ICON_URL, CHANNEL_DESCRIPTION, CHANNEL_OWNER," +
            "CHANNEL_INVITE_CODE) VALUES (#{channel_title},#{channel_icon_url}, #{channel_description}" +
            ", #{channel_owner},#{channel_inviteCode})")
    void saveChannel(Channel channel);

    //수정해야함
    @Select("select CHANNEL_UID from channel where CHANNEL_OWNER = #{memberUID} order by CHANNEL_UID desc limit 1;")
    int findChannelUIDByMemberUID(int memberUID);

    @Insert("Insert into channelmember (CHANNEL_UID, MEMBER_UID, CHANNEL_MEMBER_AUTHORITY)" +
            "VALUES (#{channel_UID},#{memberUID},#{authority})")
    void saveChannelMember(@Param("channel_UID") int channel_UID,@Param("memberUID") int memberUID, @Param("authority") String authority);

    @Select("select m.CHANNEL_UID, m.MEMBER_UID, c.CHANNEL_TITLE, c.CHANNEL_ICON_URL, c.CHANNEL_TYPE,c.CHANNEL_INVITE_CODE " +
            " from channelmember m left join channel c on m.CHANNEL_UID = c.CHANNEL_UID " +
            " where m.MEMBER_UID = #{memberUID} order by CHANNEL_MEMBER_JOINDATE desc limit 1")
    MyChannelsDTO findLastChannelByMemberUID(int memberUID);

    @Select("SELECT * FROM channel WHERE CHANNEL_INVITE_CODE = #{inviteCode}")
    Optional<Channel> findChannelByInviteCode(String inviteCode);

    @Select("SELECT * FROM channelmember WHERE MEMBER_UID = #{memberUID} and CHANNEL_UID = #{channelUID}")
    Optional<ChannelMember> findChannelMemberByMemberUID(int memberUID,int channelUID);

    @Delete("delete FROM channelmember WHERE CHANNEL_UID = #{channelUID} and MEMBER_UID = #{memberUID}")
    void deleteChannelMember(int channelUID, int memberUID);

    @Select("SELECT * FROM channelmember WHERE CHANNEL_UID = #{channelUID}")
    Optional<ChannelMember> findChannelMemberByChannelUID(int channelUID);

    @Delete("delete from channel WHERE CHANNEL_UID = #{channelUID}")
    void deleteChannel(int channelUID);
}
//select m.CHANNEL_UID, m.MEMBER_UID, c.CHANNEL_TITLE, c.CHANNEL_ICON_URL
//from channelmember m
//left join channel c on m.CHANNEL_UID = c.CHANNEL_UID
//where m.MEMBER_UID = 3