package models.dto;

/**
 * DTO për përditësimin e të dhënave bazë të profilit.
 */
public class ProfilUpdateDto implements IRequestDto {

    private final String emriPlote;
    private final String email;

    public ProfilUpdateDto(String emriPlote, String email) {
        this.emriPlote = emriPlote != null ? emriPlote.trim() : "";
        this.email     = email != null ? email.trim() : "";
    }

    public String getEmriPlote() {
        return emriPlote;
    }

    public String getEmail() {
        return email;
    }
}