package wazoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import wazoo.entity.Chat;
import wazoo.entity.User;


@Slf4j
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatDto {
    private String chatId;
    private Integer userNo;
    private Integer partnerNo;

    public static ChatDto fromEntity(Chat chat) {
        return ChatDto.builder()
                .chatId(chat.getChatId())
                .userNo(chat.getUser().getUserNo())
                .partnerNo(chat.getPartner().getUserNo())
                .build();
    }

}
