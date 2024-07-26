package wazoo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "chatroom")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {
    @Id
    @Column(name = "chat_room_id")
    private String chatRoomId;

    @ManyToOne
    @JoinColumn(name = "chat_room_founder", referencedColumnName = "user_id")
    private User chatRoomFounder;

    @ManyToOne
    @JoinColumn(name = "chat_room_audience", referencedColumnName = "user_id")
    private User chatRoomAudience;

    @PrePersist
    public void prePersist() {
        if (this.chatRoomId == null) {
            this.chatRoomId = UUID.randomUUID().toString();
        }
    }
}
