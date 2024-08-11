package wazoo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "chat")
public class Chat {
    @Id
    @Column(name = "chat_id")
    private String chatId;

    @ManyToOne
    @JoinColumn(name = "user_no", referencedColumnName = "user_no", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "partner_no", referencedColumnName = "user_no", nullable = false)
    private User partner;

    @PrePersist
    public void prePersist() {
        if (this.chatId == null) {
            this.chatId = UUID.randomUUID().toString();
        }
    }
}
