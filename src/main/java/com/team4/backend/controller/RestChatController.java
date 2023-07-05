package com.team4.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.team4.backend.mapper.RedisToMariaDBMigrationMapper;
import com.team4.backend.model.ChatRoom;
import com.team4.backend.repo.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping(ControllerProperties.API_VERSION+"/chat")
public class RestChatController {

    private final RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/rooms")
    public ResponseEntity<?> room() {
        List<ChatRoom> list = chatRoomRepository.findAllRoom();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
    @PostMapping("/room")
    public ResponseEntity<?> createRoom(@RequestBody Map<String,?> params) {
        String name = (String)params.get("name");
        boolean room_type = (boolean)params.get("room_type");
        ChatRoom chatRoom = chatRoomRepository.createChatRoom(name, room_type);
        return new ResponseEntity<>(chatRoom,HttpStatus.CREATED);
    }
}
