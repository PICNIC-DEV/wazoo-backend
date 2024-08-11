    package wazoo.dto;

    import lombok.Data;

    @Data
    public class LoginRequestDto {
        private String userId;
        private String userPassword;
    }