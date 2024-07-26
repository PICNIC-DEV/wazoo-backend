package wazoo.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @Id
    private String id; // MongoDB의 문서 id
    private String chatId;
    private String message;
    private String transMessage;
    private int userId;
    private int partnerId;
    private LocalDateTime createdAt;
    private MessageType messageType;

    public enum MessageType {
        JOIN, TALK;
    }

}