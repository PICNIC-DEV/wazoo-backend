package wazoo.service;

import wazoo.dto.LoginRequestDto;

public interface AuthService {
    public String login(LoginRequestDto dto);
}


