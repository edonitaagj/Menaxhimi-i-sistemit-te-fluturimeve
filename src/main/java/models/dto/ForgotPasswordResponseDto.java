package models.dto;

public class ForgotPasswordResponseDto {

    private final boolean success;
    private final String message;

    public ForgotPasswordResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static ForgotPasswordResponseDto ok(String message) {
        return new ForgotPasswordResponseDto(true, message);
    }

    public static ForgotPasswordResponseDto error(String message) {
        return new ForgotPasswordResponseDto(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
