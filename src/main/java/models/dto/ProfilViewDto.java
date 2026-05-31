package models.dto;

/**
 * DTO vetëm-lexim — popullojë fushat e Profil_view.fxml.
 * Krijohet nga ProfilService.getProfilForView() dhe i jepet direkt controller-it.
 * Controller nuk prek kurrë modelin Perdoruesi direkt.
 */
public class ProfilViewDto {

    private final String emriPlote;   // "Arben Krasniqi"
    private final String username;    // "arben_k"
    private final String email;       // "arben@mail.com"
    private final String roli;        // "klient" | "staf" | "admin"

    public ProfilViewDto(String emriPlote, String username,
                         String email, String roli) {
        this.emriPlote = emriPlote;
        this.username  = username;
        this.email     = email;
        this.roli      = roli;
    }

    public String getEmriPlote() { return emriPlote; }
    public String getUsername()  { return username; }
    public String getEmail()     { return email; }
    public String getRoli()      { return roli; }
}