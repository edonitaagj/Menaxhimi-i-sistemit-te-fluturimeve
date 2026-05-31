package models.dto;

/**
 * DTO për ndryshimin e fjalëkalimit.
 * Dërgohet nga ProfilController → ProfilService.updateFjalekalim().
 */
public class FjalekalimiUpdateDto implements IRequestDto {

    private final String fjalekalimiAktual;  // plain-text — verifikohet me BCrypt
    private final String fjalekalimiRi;      // plain-text — minimum 8 karaktere

    public FjalekalimiUpdateDto(String fjalekalimiAktual, String fjalekalimiRi) {
        this.fjalekalimiAktual = fjalekalimiAktual != null ? fjalekalimiAktual : "";
        this.fjalekalimiRi     = fjalekalimiRi     != null ? fjalekalimiRi     : "";
    }

    public String getFjalekalimiAktual() { return fjalekalimiAktual; }
    public String getFjalekalimiRi()     { return fjalekalimiRi; }
}