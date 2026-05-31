package models.dto;

/**
 * DTO për formën e kompanisë (create + update).
 * Dërgohet nga AdminKompaniteController → KompaniaService.
 */
public class KompaniaFormDto implements IRequestDto {

    private final String  emri;
    private final String  kodiIata;
    private final String  kodiIcao;
    private final String  emriIShkurter;
    private final int     idVendit;
    private final String  faqjaWeb;
    private final String  telefoni;
    private final boolean eshteAktive;

    public KompaniaFormDto(String emri, String kodiIata, String kodiIcao,
                           String emriIShkurter, int idVendit, String faqjaWeb,
                           String telefoni, boolean eshteAktive) {
        this.emri          = emri          != null ? emri.trim()          : "";
        this.kodiIata      = kodiIata      != null ? kodiIata.trim().toUpperCase() : "";
        this.kodiIcao      = kodiIcao      != null ? kodiIcao.trim().toUpperCase() : "";
        this.emriIShkurter = emriIShkurter != null ? emriIShkurter.trim() : "";
        this.idVendit      = idVendit;
        this.faqjaWeb      = faqjaWeb      != null ? faqjaWeb.trim()      : "";
        this.telefoni      = telefoni      != null ? telefoni.trim()      : "";
        this.eshteAktive   = eshteAktive;
    }

    public String  getEmri()          { return emri; }
    public String  getKodiIata()      { return kodiIata; }
    public String  getKodiIcao()      { return kodiIcao; }
    public String  getEmriIShkurter() { return emriIShkurter; }
    public int     getIdVendit()      { return idVendit; }
    public String  getFaqjaWeb()      { return faqjaWeb; }
    public String  getTelefoni()      { return telefoni; }
    public boolean isEshteAktive()    { return eshteAktive; }
}