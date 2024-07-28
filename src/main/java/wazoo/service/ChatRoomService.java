package wazoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wazoo.dto.ChatRoomDTO;
import wazoo.entity.ChatRoom;
import wazoo.entity.Message;
import wazoo.entity.User;
import wazoo.filtering.AhoCorasick;
import wazoo.filtering.BadWordFiltering;
import wazoo.repository.ChatRoomRepository;
import wazoo.repository.MessageRepository;
import wazoo.repository.UserRepository;
import wazoo.utils.BadWordLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private AhoCorasick ahoCorasick;
    private BadWordFiltering badWordFiltering;


    private void initializeBadWords() throws IOException {
        ahoCorasick = new AhoCorasick();
        badWordFiltering = new BadWordFiltering();

        List<String> badWords = BadWordLoader.loadBadWords("badwords.txt");
        for (String word : badWords) {
            ahoCorasick.addKeyword(word);
        }
        ahoCorasick.buildFailureLinks();
        badWordFiltering.addBadWords(badWords);
    }

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

    //
    public List<Message> getRecentDialogs(String userName) throws Exception {

        initializeBadWords();

        User user = userRepository.findByName(userName);
        if (user == null) {
            throw new Exception("해당 이름의 유저를 찾을 수 없습니다.");
        }

        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<Message> recentDialogs = messageRepository.findByUserIdAndCreatedAtAfter(user.getUserId(), threeDaysAgo);
        List<Message> filteredDialogs = new ArrayList<>();

        for (Message dialog : recentDialogs) {
            Set<String> badWordsFound = ahoCorasick.search(dialog.getMessage());
            if (!badWordsFound.isEmpty() || badWordFiltering.checkBadWord(dialog.getMessage())) {
                filteredDialogs.add(dialog);
            }
        }

        return filteredDialogs;
    }

}
