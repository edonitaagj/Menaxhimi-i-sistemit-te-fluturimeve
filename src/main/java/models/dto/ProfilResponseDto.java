package models.dto;

/**
 * DTO rezultati — kthehet nga çdo metodë e ProfilService.
 * Controller e lexon dhe shfaq Alert sipas success/message.
 * Nuk ka nevojë për try/catch në controller.
 */
public class ProfilResponseDto {

    private final boolean success;
    private final String  message;

    public ProfilResponseDto(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // ── Factory methods — lexohen si fjali të qarta ───────────────────
    public static ProfilResponseDto ok(String message) {
        return new ProfilResponseDto(true, message);
    }

    public static ProfilResponseDto error(String message) {
        return new ProfilResponseDto(false, message);
    }

    public boolean isSuccess() { return success; }
    public String  getMessage() { return message; }
}