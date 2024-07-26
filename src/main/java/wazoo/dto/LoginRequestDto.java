    package wazoo.dto;

    import lombok.Data;

    @Data
    public class LoginRequestDto {
        private String login_id;

        private String login_password;
    }