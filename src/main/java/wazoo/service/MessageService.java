package wazoo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wazoo.dto.MessageDTO;
import wazoo.entity.Message;
import wazoo.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {
    private final MessageRepository messageRepository;
    // 채팅 메시지 DB 저장 (소켓 메시지 전송 시)
    public void saveMessage(MessageDTO messageDTO) {

        Message messageDocument = Message.builder()
                .chatId(messageDTO.getChatId())
                .userNo(messageDTO.getUserNo())
                .partnerNo(messageDTO.getPartnerNo())
                .messageContent(messageDTO.getMessageContent())
                .transMessageContent(messageDTO.getTransMessageContent())
                .createdAt(messageDTO.getCreatedAt())
                .build();
        messageRepository.save(messageDocument); // MongoDB 저장

    }

    // 3. 채팅방 ID 별 대화목록 모두 조회
    public List<Message> getMessagesByChatId(String chatId) {
        return messageRepository.findByChatId(chatId);
    }

}
