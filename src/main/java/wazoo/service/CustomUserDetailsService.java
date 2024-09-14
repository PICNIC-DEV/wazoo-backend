package wazoo.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wazoo.utils.CustomUserDetails;
import wazoo.dto.CustomUserInfoDto;
import wazoo.entity.User;
import wazoo.repository.UserRepository;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저가 없습니다."));


        CustomUserInfoDto dto = mapper.map(user, CustomUserInfoDto.class);

        return new CustomUserDetails(dto);
    }
}
