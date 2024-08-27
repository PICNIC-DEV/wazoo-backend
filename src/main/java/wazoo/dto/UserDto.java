package wazoo.dto;

import lombok.Data;
import wazoo.RoleType;

@Data
public class UserDto {

    private String name;
    private String userId;
    private String password;
    private String address;
    private RoleType role;
    private String nativeLanguage;
}
