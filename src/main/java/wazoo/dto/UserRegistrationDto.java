package wazoo.dto;

import lombok.*;

@Data
public class UserRegistrationDto {

    private String name;

    private String userId;

    private String userPassword;

    private String address;

    private String language;

    private String role;
}