package wazoo.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Date;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
@Builder
public class Message {
    @Id
    private String id; // MongoDB의 문서 id
    private String chatId;
    private String messageContent;
    private String transMessageContent;
    private Integer userNo;
    private Integer partnerNo;
    private LocalDateTime createdAt;
    private MessageType messageType;

    public enum MessageType {
        JOIN, TALK;
    }

}