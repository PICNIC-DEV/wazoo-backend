package wazoo.service;

import wazoo.dto.LoginRequestDto;
import wazoo.dto.UserRegistrationDto;
import wazoo.entity.User;
import wazoo.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final JsonNode travel_type;

    public UserService() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        travel_type = mapper.readTree(new ClassPathResource("types.json").getInputStream());
    }

    public User saveTravelTypeUser(int user_id, boolean[] answers) {
        String type1 = determineType(new int[]{0, 1, 2}, answers, "유동적", "계획적");
        String type2 = determineType(new int[]{2, 3, 4}, answers, "플렉스", "가성비");
        String type3 = answers[5] ? "현지인 체험" : "유명 관광지";
        String type4 = answers[6] ? "혼자" : "다같이";

        String type = travel_type.path(type1)
                .path(type2)
                .path(type3)
                .path(type4)
                .asText();

        Optional<User> userOptional = userRepository.findById(user_id);
        if(userOptional.isPresent()){
            User user = userOptional.get();
            user.setTravelType(type);
            return userRepository.save(user);
        }else{
            throw new RuntimeException("User not found with id : " + user_id);
        }
    }

    private String determineType(int[] questionIndices, boolean[] answers, String option1, String option2) {
        int countOption1 = 0;
        int countOption2 = 0;

        for (int index : questionIndices) {
            if (answers[index]) {
                countOption1++;
            } else {
                countOption2++;
            }
        }

        return countOption1 >= 2 ? option1 : option2;
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        User existingUser = userRepository.findByName(registrationDto.getName()).orElse(null);
        if (existingUser != null) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();

        user.setName(registrationDto.getName());
        user.setUserLoginId(registrationDto.getLogin_id());
        user.setUserLoginPassword(registrationDto.getLogin_password());
        user.setAddress(registrationDto.getAddress());
        user.setNativeLanguage(registrationDto.getNativeLanguage());

        return userRepository.save(user);
    }

    public User login(LoginRequestDto loginRequestDto) {

        User user = userRepository.findByUserLoginIdAndUserLoginPassword(loginRequestDto.getLogin_id(), loginRequestDto.getLogin_password());
        if (user == null) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }

}