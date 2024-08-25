package wazoo.repository;

import wazoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserNo(Integer userNo);
    List<User> findAll();
//    Optional<User> findByName(String name);
    User findByName(String name);
    Optional<User> findByUserId(String id);
    User findByUserIdAndUserPassword(String userLoginId, String userLoginPassword);
}
