package models.dto;

/**
 * DTO për formën "SHTO FLUTURIM TË RI":
 *   - txtKodi         → numri_fluturimit
 *   - cmbKompania     → id_kompanise
 *   - txtOrigjina     → kodi IATA nisjes (gjendet linja)
 *   - txtDestinacioni → kodi IATA mbërrritjes (gjendet linja)
 *   - cmbStatusi      → statusi fillestar
 *
 * Fushat e tjera (id_avionit, kapaciteti, ora_nisjes, etj.)
 * janë të detyrueshme në DB por nuk janë në formë → vendosen
 * si defaults nga service para INSERT.
 */
public class AdminFluturimiFormDto implements IRequestDto {

    private final String kodi;
    private final int    idKompanise;
    private final String iataOrigjina;
    private final String iataDestinacioni;
    private final String statusi;

    public AdminFluturimiFormDto(String kodi, int idKompanise,
                                 String iataOrigjina, String iataDestinacioni,
                                 String statusi) {
        this.kodi              = kodi             != null ? kodi.trim().toUpperCase()             : "";
        this.idKompanise       = idKompanise;
        this.iataOrigjina      = iataOrigjina      != null ? iataOrigjina.trim().toUpperCase()      : "";
        this.iataDestinacioni  = iataDestinacioni  != null ? iataDestinacioni.trim().toUpperCase()  : "";
        this.statusi           = statusi           != null ? statusi                               : "i_planifikuar";
    }

    public String getKodi()             { return kodi; }
    public int    getIdKompanise()      { return idKompanise; }
    public String getIataOrigjina()     { return iataOrigjina; }
    public String getIataDestinacioni() { return iataDestinacioni; }
    public String getStatusi()          { return statusi; }

}