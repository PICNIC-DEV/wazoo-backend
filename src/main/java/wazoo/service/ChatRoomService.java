package wazoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wazoo.dto.ChatRoomDTO;
import wazoo.entity.ChatRoom;
import wazoo.entity.Message;
import wazoo.entity.User;
import wazoo.repository.ChatRoomRepository;
import wazoo.repository.MessageRepository;
import wazoo.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    // 1. 전체 채팅방 목록 반환
    public List<ChatRoomDTO> findAll() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomDTO::fromEntity)
                .collect(Collectors.toList());
    }
    // 2. 특정 채팅방 반환
    public ChatRoomDTO findChatRoomById(String chatId) {
        Optional<ChatRoom> chatRoom = chatRoomRepository.findByChatRoomId(chatId);
        return chatRoom.map(ChatRoomDTO::fromEntity).orElse(null);
    }

    // 4. 특정 유저가 참여하고 있는 전체 채팅방 정보 조회
    public List<ChatRoomDTO> findChatRoomsByUserId(User userId) {
        List<ChatRoom> chatRooms = chatRoomRepository.findByUserId(userId.getUserId());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return chatRooms.stream().map(chatRoom -> {
            User partner = (chatRoom.getChatRoomFounder() == userId) ? chatRoom.getChatRoomAudience() : chatRoom.getChatRoomFounder();
            String partnerName = partner.getName();

            List<Message> chats = messageRepository.findByChatIdOrderByCreatedAtDesc(chatRoom.getChatRoomId());
            String lastMessage = "";
            String lastMessageTime = "";

            if (!chats.isEmpty()) {
                Message lastChat = chats.get(0); // 가장 최신 메시지
                lastMessage = lastChat.getMessage();
                LocalDateTime lastMessageTimeValue = lastChat.getCreatedAt();
                if (lastMessageTimeValue != null) {
                    lastMessageTime = lastMessageTimeValue.format(formatter); // 날짜 형식 지정
                }
            }

            return ChatRoomDTO.fromEntity(
                    chatRoom,
                    lastMessage,
                    lastMessageTime,
                    partnerName
            );
        }).collect(Collectors.toList());
    }

}
