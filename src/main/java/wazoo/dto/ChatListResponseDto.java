package wazoo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import wazoo.entity.Chat;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class ChatListResponseDto {
    private String chatId;
    private Integer userNo;
    private Integer guideId;
    private String partnerName;
    private String lastMessage;
    private LocalDateTime lastMessageTime;

    public static ChatListResponseDto fromEntity(Chat chat, String lastMessage, LocalDateTime lastMessageTime, String partnerName) {
        return ChatListResponseDto.builder()
                .chatId(chat.getChatId())
                .userNo(chat.getUser().getUserNo())
                .guideId(chat.getGuide().getGuideId())
                .partnerName(partnerName)
                .lastMessage(lastMessage)
                .lastMessageTime(lastMessageTime)
                .build();
    }
}
