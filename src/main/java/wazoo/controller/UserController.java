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
    public String analyzeUser(@PathVariable int user_id, @RequestBody boolean[] answers) {
        return userService.saveTravelTypeUser(user_id, answers);
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(
            @RequestParam("name") String name,
            @RequestParam("loginId") String loginId,
            @RequestParam("loginPassword") String loginPassword,
            @RequestParam("address") String address,
            @RequestParam("language") String language) {
        try {

            System.out.println(">>????12222");

            UserRegistrationDto registrationDto = new UserRegistrationDto();
            registrationDto.setName(name);
            registrationDto.setUserId(loginId);
            registrationDto.setUserPassword(loginPassword);
            registrationDto.setAddress(address);
            registrationDto.setLanguage(language);

            userService.registerUser(registrationDto);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(
            @RequestParam("userId") String userId,
            @RequestParam("userPassword") String userPassword) {
        try {

            LoginRequestDto loginRequestDto = new LoginRequestDto();
            loginRequestDto.setUserId(userId);
            loginRequestDto.setUserPassword(userPassword);

            User userDto = userService.login(loginRequestDto);
            return ResponseEntity.ok(userDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

}
