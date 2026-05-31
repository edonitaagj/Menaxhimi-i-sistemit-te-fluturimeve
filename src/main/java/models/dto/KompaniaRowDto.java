package models.dto;

/**
 * DTO vetëm-shfaqje për tblKompanite (TableView).
 * Krijohet nga KompaniaService — controller nuk prek modelin direkt.
 */
public class KompaniaRowDto {

    private final int     idKompanise;
    private final String  emri;
    private final String  kodiIata;
    private final String  kodiIcao;
    private final String  emriIShkurter;
    private final String  faqjaWeb;
    private final String  statusi;   // "Aktive" | "Joaktive"

    public KompaniaRowDto(int idKompanise, String emri, String kodiIata,
                          String kodiIcao, String emriIShkurter,
                          String faqjaWeb, String statusi) {
        this.idKompanise = idKompanise;
        this.emri        = emri;
        this.kodiIata    = kodiIata;
        this.kodiIcao    = kodiIcao;
        this.emriIShkurter  = emriIShkurter;
        this.faqjaWeb    = faqjaWeb;
        this.statusi     = statusi;
    }

    public int    getIdKompanise() { return idKompanise; }
    public String getEmri()        { return emri; }
    public String getKodiIata()    { return kodiIata; }
    public String getKodiIcao()    { return kodiIcao; }
    public String getEmriIShkurter()  { return emriIShkurter; }
    public String getFaqjaWeb()    { return faqjaWeb; }
    public String getStatusi()     { return statusi; }
}