package wazoo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wazoo.dto.LoginRequestDto;
import wazoo.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<String> getUserProfile(
            @Valid @RequestBody LoginRequestDto requset
            ) {
        String token = this.authService.login(requset);
        return ResponseEntity.status(HttpStatus.OK).body(token);
    }
}
