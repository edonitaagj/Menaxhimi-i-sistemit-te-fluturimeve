package models.dto;

/**
 * DTO për rreshtin e tblLinjat (TableView).
 */
public class LinjaRowDto {

    private final int    idLinjes;
    private final String aeroportiNisjes;     // "PRN – Aeroporti Adem Jashari"
    private final String aeroportiMbrrritjes; // "VIE – Aeroporti i Vjenës"
    private final String distanca;            // "800 km"
    private final String koha;               // "90 min"
    private final String statusi;            // "Aktive" | "Joaktive"

    public LinjaRowDto(int idLinjes, String aeroportiNisjes,
                       String aeroportiMbrrritjes, String distanca,
                       String koha, String statusi) {
        this.idLinjes            = idLinjes;
        this.aeroportiNisjes     = aeroportiNisjes;
        this.aeroportiMbrrritjes = aeroportiMbrrritjes;
        this.distanca            = distanca;
        this.koha                = koha;
        this.statusi             = statusi;
    }

    public int    getIdLinjes()              { return idLinjes; }
    public String getAeroportiNisjes()       { return aeroportiNisjes; }
    public String getAeroportiMbrrritjes()   { return aeroportiMbrrritjes; }
    public String getDistanca()              { return distanca; }
    public String getKoha()                  { return koha; }
    public String getStatusi()               { return statusi; }
}