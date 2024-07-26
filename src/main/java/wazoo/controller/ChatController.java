package wazoo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import wazoo.dto.ChatRoomDTO;
import wazoo.dto.MessageDTO;
import wazoo.dto.TranscriptionRequest;
import wazoo.dto.WhisperTranscriptionResponse;
import wazoo.entity.Message;
import wazoo.entity.User;
import wazoo.repository.MessageRepository;
import wazoo.repository.UserRepository;
import wazoo.service.ChatRoomService;
import wazoo.service.MessageService;
import wazoo.service.OpenAIClientService;
import wazoo.service.TranslationService;

import java.util.List;

@RequiredArgsConstructor
@Controller
@Slf4j
@RequestMapping(value = "/chat")
public class ChatController {
    private final SimpMessageSendingOperations messagingTemplate;
    private final OpenAIClientService openAIClientService;
    private final ChatRoomService chatRoomService;
    private final MessageService messageService;
    private final TranslationService translationService;
    private final UserRepository userRepository;

    private String translatedText;

    @PostMapping(value = "/transcription", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> createTranscription(@ModelAttribute TranscriptionRequest transcriptionRequest) {
        WhisperTranscriptionResponse response = openAIClientService.createTranscription(transcriptionRequest);
        return ResponseEntity.ok(response.getText());
    }

    // 채팅 메시지 수신 및 저장, 메시지를 데이터베이스에 저장한 후 브로드캐스트
    @MessageMapping("/message")
    public void sendMessage(MessageDTO message) {
        // 메시지 처리 전 상대방 아이디 설정
        message.setPartnerId(message.getPartnerId());

        int user1 = message.getUserId();
        User user1Info = userRepository.findByUserId(user1);
        String userLang = user1Info.getNativeLanguage();

        int user2 = message.getPartnerId();
        User user2Info = userRepository.findByUserId(user2);
        String partnerLang = user2Info.getNativeLanguage();

        System.out.println("사람 검사" + message.getUserId() + (message.getPartnerId()));
        System.out.println("언어검사" + partnerLang + userLang);

        // google translation api
        translatedText = translationService.translateText(message.getMessage(), partnerLang, userLang); // text, targetLanguage

        message.setTransMessage(translatedText);
        messageService.saveMessage(message);
        messagingTemplate.convertAndSend("/sub/chat/room/" + message.getChatId(), message);
    }

    // 1. 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDTO>> getAllRooms() {
        List<ChatRoomDTO> rooms = chatRoomService.findAll();
        return ResponseEntity.ok(rooms);
    }

    // 2. 채팅방 ID 로 특정 채팅방 정보 조회
    @GetMapping("/rooms/{chatId}")
    public ResponseEntity<ChatRoomDTO> getRoomById(@PathVariable String chatId) {
        ChatRoomDTO room = chatRoomService.findChatRoomById(chatId);
        return ResponseEntity.ok(room);
    }

    // 3. 채팅방 id 별 대화목록 모두 조회
    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String chatId) {
        List<Message> list = messageService.getMessagesByChatId(chatId);
        return ResponseEntity.ok(list);
    }

    // 4. 특정 유저가 참여중인 채팅방 전체 조회
    @GetMapping("/rooms/user/{userId}")
    public ResponseEntity<List<ChatRoomDTO>> getChatRoomsByUserId(@PathVariable int userId) {
        User user = userRepository.findByUserId(userId);
        List<ChatRoomDTO> list = chatRoomService.findChatRoomsByUserId(user);
        return ResponseEntity.ok(list);
    }

}

