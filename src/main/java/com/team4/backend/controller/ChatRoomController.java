package com.team4.backend.controller;

import com.team4.backend.mapper.RedisToMariaDBMigrationMapper;
import com.team4.backend.model.ChatMessage;
import com.team4.backend.model.ChatRoom;
import com.team4.backend.repo.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController // @Controller 대신 @RestController를 사용하여, JSON 형태로 응답할 수 있게 만듭니다.
@RequestMapping("/api/v1/chat")
public class ChatRoomController {

    private final RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper;
    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/room")
    public String rooms(Model model) {
        System.out.println("get rooms");
        return "api/v1/chat/room";
    }

    @GetMapping("/api/v1/chat/rooms")
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }



    @GetMapping("/room/enter/{roomId}")
    @ResponseBody
    public List<ChatMessage> roomDetail(Model model, @PathVariable String roomId) {
        // 문자열 대신 필요한 데이터를 반환하도록 수정합니다.
        System.out.print("GetMapping /room/enter/roomId");
        List<ChatMessage> chatMessages = redisToMariaDBMigrationMapper.getChatMessagesFromDB(roomId);
        model.addAttribute("roomId", roomId);
        model.addAttribute("chatMessages", chatMessages);
        return chatMessages; // chatMessages 반환
    }

    @GetMapping("/room/{roomId}")
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}
