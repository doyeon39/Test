package com.team4.backend.service;

import com.team4.backend.mapper.RedisToMariaDBMigrationMapper;
import com.team4.backend.model.ChatMessage;
import com.team4.backend.model.ChatRoom;
import com.team4.backend.repo.ChatMessageRepository;
import com.team4.backend.repo.ChatRoomRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RedisToMariaDBMigrationService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper;

    public RedisToMariaDBMigrationService(ChatRoomRepository chatRoomRepository, ChatMessageRepository chatMessageRepository, RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.redisToMariaDBMigrationMapper = redisToMariaDBMigrationMapper;
    }

    @Scheduled(fixedDelay = 10000)
    private void migrateData() {
        // Redis 데이터 읽기
        List<ChatRoom> chatRooms = chatRoomRepository.getAllChatRooms();
        List<ChatMessage> chatMessages = chatMessageRepository.getAllChatMessage();
        // 데이터 변환
        System.out.println("Redis insert chat rooms: " + chatRooms.size());
        // MariaDB에 데이터 저장
        for (ChatRoom chatRoom : chatRooms) {
            String roomId = chatRoom.getRoomId();
            String name = chatRoom.getName();
            boolean roomType = chatRoom.isRoomType();

            boolean isDuplicate = checkDuplicateChatRoom(roomId);
            if (isDuplicate) {
                continue; // 중복된 값은 삽입을 건너뜁니다.
            }

            redisToMariaDBMigrationMapper.insertChatRoom(roomId, name, roomType);
        }

        for (ChatMessage chatMessage : chatMessages) {
            String roomId = chatMessage.getRoomId();
            String sender = chatMessage.getSender();
            String message = chatMessage.getMessage();
            Date sendDate = chatMessage.getSendDate();

            boolean isDuplicate = checkDuplicateChatMessage(roomId, sender, message, sendDate);
            if (isDuplicate) {
                continue; // 중복된 값은 삽입을 건너뜁니다.
            }

            redisToMariaDBMigrationMapper.insertChatMessage(roomId, sender, message, sendDate);
        }
    }

    private boolean checkDuplicateChatRoom(String roomId) {
        // 중복 여부를 확인하는 쿼리
        int count = redisToMariaDBMigrationMapper.countChatRoom(roomId);

        return count > 0; // count가 0보다 크면 중복이 있는 경우
    }

    private boolean checkDuplicateChatMessage(String roomId, String sender, String message, Date sendDate) {
        // date가 null인 경우에는 바로 중복으로 처리
        if(sendDate == null) {
            return true;
        }
        // 중복 여부를 확인하는 쿼리
        int count = redisToMariaDBMigrationMapper.countChatMessage(roomId, sender, message, sendDate);

        return count > 0; // count가 0보다 크면 중복이 있는 경우
    }
}
