package wazoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import wazoo.dto.ChatDto;
import wazoo.dto.ChatListResponseDto;
import wazoo.entity.Chat;
import wazoo.entity.Guide;
import wazoo.entity.Message;
import wazoo.entity.User;
import wazoo.filtering.AhoCorasick;
import wazoo.filtering.BadWordFiltering;
import wazoo.repository.ChatRoomRepository;
import wazoo.repository.GuideRepository;
import wazoo.repository.MessageRepository;
import wazoo.repository.UserRepository;
import wazoo.utils.BadWordLoader;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final GuideRepository guideRepository;
    private final MessageService messageService;

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

    // 3. 채팅방 ID 별 대화목록 모두 조회
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String chatId) {
        List<Message> list = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(list);
    }

    // 4. 특정 유저가 참여하고 있는 전체 채팅방 정보 조회
    public List<ChatListResponseDto> findChatRoomsByUserId(User user) {
        // 사용자의 가이드아이디 조회
        Guide guide = guideRepository.findByUser(user);
        List<Chat> chatRooms;

        if (guide == null) {
            chatRooms = chatRoomRepository.findChatRoomsByUserNoOrGuideId(user.getUserNo(), null);
        } else {
            chatRooms = chatRoomRepository.findChatRoomsByUserNoOrGuideId(user.getUserNo(), guide.getGuideId());
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return chatRooms.stream().map(chat -> { // 2번이야.
            // 채팅 상대방 이름
            User partner = (chat.getUser().getUserNo().equals(user.getUserNo())) ? chat.getGuide().getUser() : chat.getUser();
            String partnerName = partner.getName();

            List<Message> chats = messageRepository.findByChatIdOrderByCreatedAtDesc(chat.getChatId());
            String lastMessage = "";

            LocalDateTime lastMessageTime = null;
            if (!chats.isEmpty()) {
                Message lastChat = chats.get(0); // 가장 최신 메시지
                lastMessage = lastChat.getMessageContent();
                lastMessageTime = lastChat.getCreatedAt();
            }

            return ChatListResponseDto.fromEntity(
                    chat,
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
        List<Message> recentDialogs = messageRepository.findByUserNoAndCreatedAtAfter(user.getUserNo(), threeDaysAgo);
        List<Message> filteredDialogs = new ArrayList<>();

        for (Message dialog : recentDialogs) {
            Set<String> badWordsFound = ahoCorasick.search(dialog.getMessageContent());
            if (!badWordsFound.isEmpty() || badWordFiltering.checkBadWord(dialog.getMessageContent())) {
                filteredDialogs.add(dialog);
            }
        }

        return filteredDialogs;
    }

    // 6. 채팅방 생성
    public ChatDto createRoom(Integer userNo, Integer guideId) {
        User user = userRepository.findByUserNo(userNo);
        Guide guide = guideRepository.findByGuideId(guideId);
//        User partner = userRepository.findByUserNo(partnerNo);

        // 6-1. 기존 채팅방 존재 유무 확인
        boolean chatRoomExists = chatRoomRepository.findByUserNoAndPartnerNo(user.getUserNo(), guide.getGuideId()).size() > 0;

        if (chatRoomExists) {
            // 기존 채팅방이 존재하면 null 반환
            return null;
        }

        Chat chatRoom = Chat.builder()
                .chatId(UUID.randomUUID().toString())
                .user(user)
                .guide(guide)
                .build();
        Chat savedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatDto.fromEntity(savedChatRoom);

    }
}
