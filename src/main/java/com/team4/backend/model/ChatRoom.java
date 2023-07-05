package com.team4.backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String roomId;
    private String name;
    private boolean roomType; // 변경: 필드 이름을 roomType으로 수정

    public static ChatRoom create(String name, boolean roomType) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        chatRoom.roomType = roomType; // 변경: 필드 이름을 roomType으로 수정
        return chatRoom;
    }
}

