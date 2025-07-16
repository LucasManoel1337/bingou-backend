package project.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
    private String message;
    private String userId;

    public LoginResponseDto(String message, String userId) {
        this.message = message;
        this.userId = userId;
    }
}