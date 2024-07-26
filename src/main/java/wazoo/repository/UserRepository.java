package wazoo.repository;

import wazoo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(Integer userId);
    List<User> findAll();
    Optional<User> findByName(String name);
    User findByUserLoginIdAndUserLoginPassword(String userLoginId, String userLoginPassword);
}
