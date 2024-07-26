package wazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wazoo.entity.ChatRoom;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByChatRoomId(String chatRoomId);

    // 4. 특정 유저가 있는 채팅방 조회
//    List<ChatRoom> findByChatRoomFounderOrChatRoomAudience(User chatRoomFounder, User chatRoomAudience);

    // 특정 유저가 채팅방에 참여 중인지 확인
    @Query("SELECT c FROM ChatRoom c WHERE c.chatRoomFounder = :userId OR c.chatRoomAudience = :userId")
    List<ChatRoom> findByUserId(@Param("userId") int userId);
}