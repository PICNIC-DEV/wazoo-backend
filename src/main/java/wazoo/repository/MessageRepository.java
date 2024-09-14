package wazoo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import wazoo.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByChatId(String chatId); // 3. 특정 채팅방 대화 목록 불러오기

    // 유저와 파트너가 참여하는 채팅방이 존재하는지 확인하
    @Query("{ $or: [ { 'userNo': ?0, 'partnerNo': ?1 }, { 'userNo': ?1, 'partnerNo': ?0 } ] }")
    List<Message> findChatByUsers(int userId1, int userId2);

    // 4. 특정 채팅방의 마지막 메시지 찾기
    List<Message> findByChatIdOrderByCreatedAtDesc(String chatId);

    // 3일 동안의 메시지 내역 불러오기
    List<Message> findByUserNoAndCreatedAtAfter(int userNo, LocalDateTime timestamp);
}