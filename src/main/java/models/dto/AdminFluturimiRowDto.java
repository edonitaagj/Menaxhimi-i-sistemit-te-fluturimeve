package models.dto;

/**
 * DTO për rreshtin e tblFluturimet në AdminFluturimet_view.fxml.
 * Kolonat: ID, KODI, KOMPANIA, ORIGJINA, DESTINACIONI, GATE, STATUSI, VEPRIMET.
 */
public class AdminFluturimiRowDto {

    private final int    idFluturimit;
    private final String kodi;           // numri_fluturimit   "W64211"
    private final String kompania;       // emri_i_shkurter    "Wizz Air"
    private final String origjina;       // kodi_iata nisjes   "PRN"
    private final String destinacioni;   // kodi_iata mbërrrit "MUC"
    private final String gate;           // kodi_gejtit        "B12" ose "—"
    private final String statusi;        // enum raw

    public AdminFluturimiRowDto(int idFluturimit, String kodi, String kompania,
                                String origjina, String destinacioni,
                                String gate, String statusi) {
        this.idFluturimit = idFluturimit;
        this.kodi         = kodi;
        this.kompania     = kompania;
        this.origjina     = origjina;
        this.destinacioni = destinacioni;
        this.gate         = gate;
        this.statusi      = statusi;
    }

    public int    getIdFluturimit() { return idFluturimit; }
    public String getKodi()         { return kodi; }
    public String getKompania()     { return kompania; }
    public String getOrigjina()     { return origjina; }
    public String getDestinacioni() { return destinacioni; }
    public String getGate()         { return gate; }
    public String getStatusi()      { return statusi; }
}