package models.dto;

public class SignupResponseDto implements IRequestDto{

    private boolean success;
    private String message;

    public SignupResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public SignupResponseDto(boolean login) {
        this.success = success;
        this.message = "";
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}