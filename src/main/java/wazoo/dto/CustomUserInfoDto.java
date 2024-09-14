package wazoo.dto;

import lombok.*;
import wazoo.RoleType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomUserInfoDto extends UserDto {
    private String userId;
    private String password;
    private String name;
    private RoleType role;

}
