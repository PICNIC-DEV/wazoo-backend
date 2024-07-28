package wazoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wazoo.dto.MessageDTO;
import wazoo.entity.Message;
import wazoo.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    // 채팅 메시지 DB 저장 (소켓 메시지 전송 시)
    public void saveMessage(MessageDTO messageDTO) {

        LocalDateTime createdAt = LocalDateTime.now(); // 현재 시간
        System.out.println("메시지생숑시간" + createdAt);

        Message messageDocument = Message.builder()
                .chatId(messageDTO.getChatId())
                .userId(messageDTO.getUserId())
                .partnerId(messageDTO.getPartnerId())
                .message(messageDTO.getMessage())
                .transMessage(messageDTO.getTransMessage())
                .createdAt(createdAt)
                .build();
        messageRepository.save(messageDocument); // MongoDB 저장

    }

    // 3. 채팅방 ID 별 대화목록 모두 조회
    public List<Message> getMessagesByChatId(String chatId) {
        return messageRepository.findByChatId(chatId);
    }

}
