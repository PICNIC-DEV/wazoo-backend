package wazoo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wazoo.dto.*;
import wazoo.entity.Guide;
import wazoo.entity.Message;
import wazoo.entity.User;
import wazoo.repository.GuideRepository;
import wazoo.repository.UserRepository;
import wazoo.service.ChatRoomService;
import wazoo.service.MessageService;
import wazoo.service.OpenAIClientService;
import wazoo.service.TranslationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping(value = "/api/v1/chats")
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final OpenAIClientService openAIClientService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final TranslationService translationService;
    private final GuideRepository guideRepository;
    private final UserRepository userRepository;

    private String translatedText;
    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WhisperTranscriptionResponse> createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest) {
        WhisperTranscriptionResponse response = openAIClientService.createTranscription(transcriptionRequest);
        return ResponseEntity.ok(response);
    }

    // 채팅 메시지 수신 및 저장, 메시지를 데이터베이스에 저장한 후 브로드캐스트
    @MessageMapping("/message")
    public void sendMessage(MessageDTO message) {
        message.setGuideId(message.getGuideId()); // 메시지 처리 전 상대방 아이디 설정

        int userNo = message.getUserNo();
        User user = userRepository.findByUserNo(userNo);
        String userLang = user.getLanguage();

        int guideId = message.getGuideId();
        Guide guide  = guideRepository.findByGuideId(guideId);
        User guideUser = guide.getUser();
        String partnerLang = guideUser.getLanguage();

        // google translation api
        translatedText = translationService.translateText(message.getMessageContent(), partnerLang, userLang); // text, targetLanguage

        message.setTransMessageContent(translatedText);
        messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatId(), message);
    }

    // 3. 채팅방 id 별 대화목록 모두 조회
    @GetMapping("/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String chatId) {
        List<Message> list = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(list);
    }

    // 4. 특정 유저가 참여중인 채팅방 전체 조회
    @GetMapping("/users/{userNo}")
    public ResponseEntity<List<ChatListResponseDto>> getChatRoomsByUserNo(@PathVariable Integer userNo) {
        User user = userRepository.findByUserNo(userNo);
        List<ChatListResponseDto> list = chatRoomService.findChatRoomsByUserId(user);
        return ResponseEntity.ok(list);
    }

    // 5. 부적절한 대화 사용한 유저 신고
    @GetMapping("/report")
    public Map<String, Object> reportUser(@RequestParam String nickname) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Message> evidence = chatRoomService.getRecentDialogs(nickname);
            response.put("status", "success");
            response.put("evidence", evidence);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // 6. 채팅방 생성
    @PostMapping("/")
    public ResponseEntity<ChatDto> createChatRoom(@RequestBody ChatDto request) {
        try {
            ChatDto chatRoom = chatRoomService.createRoom(request.getUserNo(), request.getGuideId());
            return ResponseEntity.ok(chatRoom);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

}

