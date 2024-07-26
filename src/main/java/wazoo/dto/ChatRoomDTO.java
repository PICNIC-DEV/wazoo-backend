package wazoo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import wazoo.entity.ChatRoom;
import wazoo.entity.User;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ChatRoomDTO {
    private String chatId;
    private User chatFounder;
    private User chatAudience;
    private String audienceName;
    private String lastMessage;
    private String lastMessageTime;


    public static ChatRoomDTO fromEntity(ChatRoom chatRoom) {
        return ChatRoomDTO.builder()
                .chatId(chatRoom.getChatRoomId())
                .chatFounder(chatRoom.getChatRoomFounder())
                .chatAudience(chatRoom.getChatRoomAudience())
                .build();
    }

    public static ChatRoomDTO fromEntity(ChatRoom chatRoom, String lastMessage, String lastMessageTime, String userName2) {
        return ChatRoomDTO.builder()
                .chatId(chatRoom.getChatRoomId())
                .chatFounder(chatRoom.getChatRoomFounder())
                .chatAudience(chatRoom.getChatRoomAudience())
                .audienceName(userName2)
                .lastMessage(lastMessage)
                .lastMessageTime(lastMessageTime)
                .build();
    }

}
