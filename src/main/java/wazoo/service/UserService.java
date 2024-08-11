package wazoo.service;

import wazoo.dto.LoginRequestDto;
import wazoo.dto.UserRegistrationDto;
import wazoo.entity.User;
import wazoo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService() throws IOException {
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        User existingUser = userRepository.findByName(registrationDto.getName());
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setName(registrationDto.getName());
        user.setUserId(registrationDto.getUserId());
        user.setUserPassword(registrationDto.getUserPassword());
        user.setAddress(registrationDto.getAddress());
        user.setLanguage(registrationDto.getLanguage());

        return userRepository.save(user);
    }

    public User login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByUserIdAndUserPassword(loginRequestDto.getUserId(), loginRequestDto.getUserPassword());
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }

}