package models.dto;

public class OperationResponseDto {
    private boolean success;
    private String message;

    public OperationResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public OperationResponseDto(boolean success) {
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