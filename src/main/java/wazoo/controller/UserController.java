package wazoo.controller;

import org.springframework.http.ResponseEntity;
import wazoo.dto.LoginRequestDto;
import wazoo.dto.UserRegistrationDto;
import wazoo.entity.User;
import wazoo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/traveltype/{user_id}")
    public User analyzeUser(@PathVariable int user_id, @RequestBody boolean[] answers) {
        return userService.saveTravelTypeUser(user_id, answers);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("name") String name,
            @RequestParam("login_id") String login_id,
            @RequestParam("login_password") String login_password,
            @RequestParam("address") String address,
            @RequestParam("nativeLanguage") String nativeLanguage) {
        try {

            UserRegistrationDto registrationDto = new UserRegistrationDto();
            registrationDto.setName(name);
            registrationDto.setLogin_id(login_id);
            registrationDto.setLogin_password(login_password);
            registrationDto.setAddress(address);
            registrationDto.setNativeLanguage(nativeLanguage);

            userService.registerUser(registrationDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam("login_id") String login_id,
            @RequestParam("login_password") String login_password) {
        try {

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setLogin_id(login_id);
            loginRequestDto.setLogin_password(login_password);

            User userDto = userService.login(loginRequestDto);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

}
