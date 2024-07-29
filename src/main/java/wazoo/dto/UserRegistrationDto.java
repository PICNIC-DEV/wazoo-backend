package wazoo.dto;

import lombok.*;

@Data
public class UserRegistrationDto {

    private String name;

    private String login_id;

    private String login_password;

    private String address;

    private String nativeLanguage;
}