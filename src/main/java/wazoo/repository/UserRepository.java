package wazoo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wazoo.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(Integer userId);
    List<User> findAll();
}
