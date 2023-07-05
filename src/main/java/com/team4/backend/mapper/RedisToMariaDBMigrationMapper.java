package com.team4.backend.mapper;

import com.team4.backend.model.ChatMessage;
import com.team4.backend.model.ChatRoom;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface RedisToMariaDBMigrationMapper {

    @Select("SELECT COUNT(*) FROM chat_room WHERE room_id = #{roomId}")
    int countChatRoom(@Param("roomId") String roomId);

    @Insert("INSERT INTO chat_room (room_id, name, room_type) VALUES (#{roomId}, #{name}, #{roomType})")
    void insertChatRoom(@Param("roomId") String roomId, @Param("name") String name, @Param("roomType") boolean roomType);

    @Select("SELECT COUNT(*) FROM chat_messages WHERE room_id = #{roomId} AND sender = #{sender} AND message = #{message} AND send_date = #{sendDate}")
    int countChatMessage(@Param("roomId") String roomId, @Param("sender") String sender, @Param("message") String message, @Param("sendDate") Date sendDate);

    @Insert("INSERT IGNORE INTO chat_messages (room_id, sender, message, send_date) VALUES (#{roomId}, #{sender}, #{message}, #{sendDate})")
    void insertChatMessage(@Param("roomId") String roomId, @Param("sender") String sender, @Param("message") String message, @Param("sendDate") Date sendDate);

    @Select("SELECT * FROM chat_room")
    @Results({
            @Result(property = "roomId", column = "room_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "roomType", column = "room_type")
    })
    List<ChatRoom> getAllChatRoomsFromDB();

    @Select("SELECT * FROM chat_messages WHERE room_id = #{roomId} ORDER BY send_date ASC")
    @Results({
            @Result(property = "sendDate", column = "send_date")
    })
    List<ChatMessage> getChatMessagesFromDB(@Param("roomId") String roomId);

}