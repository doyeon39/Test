package com.team4.backend.repo;

import com.team4.backend.mapper.RedisToMariaDBMigrationMapper;
import com.team4.backend.model.ChatRoom;
import com.team4.backend.pubsub.RedisSubscriber;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class ChatRoomRepository {
    private final JdbcTemplate jdbcTemplate;
    // 채팅방(topic)에 발행되는 메시지를 처리할 Listner
    private final RedisMessageListenerContainer redisMessageListener;
    // 구독 처리 서비스
    private final RedisSubscriber redisSubscriber;
    // Redis
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;
    // MariaDB의 ChatRoom 정보를 가져오기 위한 Service
    private final RedisToMariaDBMigrationMapper redisToMariaDBMigrationMapper;


    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    public List<ChatRoom> findAllRoom() {
        // MariaDB에서 데이터를 먼저 가져옵니다.
        List<ChatRoom> chatRoomsFromDB = redisToMariaDBMigrationMapper.getAllChatRoomsFromDB();
        // Redis에서 데이터를 가져옵니다.
        List<ChatRoom> chatRoomsFromRedis = opsHashChatRoom.values(CHAT_ROOMS);

        // MariaDB에서 가져온 채팅룸의 ID를 추출합니다.
        Set<String> roomIdsFromDB = chatRoomsFromDB.stream()
                .map(ChatRoom::getRoomId)
                .collect(Collectors.toSet());

        // Redis에서 가져온 채팅룸 중, MariaDB에 없는 채팅룸만 선택합니다.
        List<ChatRoom> chatRoomsFromRedisOnly = chatRoomsFromRedis.stream()
                .filter(chatRoom -> !roomIdsFromDB.contains(chatRoom.getRoomId()))
                .collect(Collectors.toList());

        // 두 데이터를 합칩니다.
        List<ChatRoom> allChatRooms = new ArrayList<>(chatRoomsFromDB);
        allChatRooms.addAll(chatRoomsFromRedisOnly);

        return allChatRooms;
    }

    public ChatRoom findRoomById(String id) {
        return opsHashChatRoom.get(CHAT_ROOMS, id);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoom createChatRoom(String name, boolean room_type) {
        ChatRoom chatRoom = ChatRoom.create(name, room_type);
        opsHashChatRoom.put(CHAT_ROOMS, chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null)
            topic = new ChannelTopic(roomId);
        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    public List<ChatRoom> getAllChatRooms() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    public List<ChatRoom> findAllChatRooms() {
        String sql = "SELECT * FROM chat_room";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ChatRoom.class));
    }
}
