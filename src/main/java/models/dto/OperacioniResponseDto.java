package models.dto;

/**
 * DTO rezultati — kthehet nga çdo operacion i KompaniaService / LinjaService.
 * Controller e lexon dhe shfaq Alert pa asnjë try/catch.
 */
public class OperacioniResponseDto {

    private final boolean success;
    private final String  message;

    private OperacioniResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static OperacioniResponseDto ok(String message) {
        return new OperacioniResponseDto(true, message);
    }

    public static OperacioniResponseDto error(String message) {
        return new OperacioniResponseDto(false, message);
    }

    public boolean isSuccess() { return success; }
    public String  getMessage() { return message; }
}