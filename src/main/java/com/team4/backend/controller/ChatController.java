package com.team4.backend.controller;

import com.team4.backend.mapper.RedisToMariaDBMigrationMapper;
import com.team4.backend.model.ChatMessage;
import com.team4.backend.pubsub.RedisPublisher;
import com.team4.backend.repo.ChatMessageRepository;
import com.team4.backend.repo.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper;


    /**
     * websocket "/pub/chat/message"로 들어오는 메시징을 처리한다.
     */
    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            chatRoomRepository.enterChatRoom(message.getRoomId());
        }
        chatMessageRepository.save(message);
        // Websocket에 발행된 메시지를 redis로 발행한다(publish)
        redisPublisher.publish(chatRoomRepository.getTopic(message.getRoomId()), message);
    }

    @ResponseBody
    @GetMapping("/enter/{roomId}")
    public List<ChatMessage> getChatMessages(@PathVariable String roomId) {
        System.out.print("Start GetChatMessages");
        return redisToMariaDBMigrationMapper.getChatMessagesFromDB(roomId);
    }
}
