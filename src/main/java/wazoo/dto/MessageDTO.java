package wazoo.dto;

import lombok.*;

import java.sql.Timestamp;

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
    private int userId;
    private int partnerId;
    private String message;
    private String transMessage;
    private Timestamp createdAt;

}