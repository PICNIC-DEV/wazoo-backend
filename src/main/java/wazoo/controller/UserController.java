package wazoo.controller;

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
        System.out.println("?");
        return userService.saveTravelTypeUser(user_id, answers);
    }
}
