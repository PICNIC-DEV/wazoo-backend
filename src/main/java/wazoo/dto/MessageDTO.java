package wazoo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    public enum MessageType {
        JOIN, TALK

    }
    private int messageId;
    private MessageType messageType;
    private String chatId;
    private Integer userNo;
    private Integer partnerNo;
    private String messageContent;
    private String transMessageContent;
    private LocalDateTime createdAt;

}