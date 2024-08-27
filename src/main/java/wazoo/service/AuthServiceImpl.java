package wazoo.service;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wazoo.dto.CustomUserInfoDto;
import wazoo.dto.LoginRequestDto;
import wazoo.entity.User;
import wazoo.repository.UserRepository;
import wazoo.utils.JwtUtil;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public String login(LoginRequestDto dto) {
        String id = dto.getUserId();
        String password = dto.getUserPassword();
        Optional<User> user = userRepository.findByUserId(id);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        // 암호화된 password를 디코딩한 값과 입력한 패스워드 값이 다르면 null 반환
        if (!encoder.matches(password, user.get().getUserPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        CustomUserInfoDto info = modelMapper.map(user, CustomUserInfoDto.class);

        String accessToken = jwtUtil.createAccessToken(info);
        return accessToken;
    }
}
