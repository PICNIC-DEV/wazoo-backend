package wazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wazoo.entity.Chat;
import wazoo.entity.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface ChatRoomRepository extends JpaRepository<Chat, String> {

    // 특정 유저가 채팅방에 참여 중인지 확인
    @Query("SELECT c FROM Chat c WHERE c.user.userNo = :userNo OR c.guide.guideId = :guideId")
    List<Chat> findChatRoomsByUserNoOrGuideId(@Param("userNo") Integer userNo, @Param("guideId") Integer guideId);


    // 6-1. 기존 채팅방 존재유무 확인
    @Query("SELECT c FROM Chat c WHERE c.user.userNo = :userNo AND c.guide.guideId = :guideId")
    List<Chat> findByUserNoAndPartnerNo(@Param("userNo") Integer userNo, @Param("guideId") Integer guideId);


}