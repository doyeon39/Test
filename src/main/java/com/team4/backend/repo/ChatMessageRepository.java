package com.team4.backend.repo;

import com.team4.backend.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ChatMessageRepository {

    private static final String CHAT_MESSAGES = "CHAT_MESSAGES";

    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatMessage> opsHashChatMessage;

    @PostConstruct
    private void init() {
        opsHashChatMessage = redisTemplate.opsForHash();
    }

    public void save(ChatMessage message) {
        opsHashChatMessage.put(CHAT_MESSAGES, generateKey(message), message);
    }

    // ChatMessage를 구분하기 위한 고유 키 생성
    private String generateKey(ChatMessage message) {
        // 고유 키 생성 로직을 구현해야 함
        // 예시로 roomId와 sender를 조합하여 고유 키 생성
        return message.getRoomId() + "_" + message.getSender() + "_" + message.getSendDate();
    }

    public List<ChatMessage> getAllChatMessage() {
        return opsHashChatMessage.values(CHAT_MESSAGES);
    }

}

