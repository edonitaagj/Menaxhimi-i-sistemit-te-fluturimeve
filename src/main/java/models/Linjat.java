package models;

public class Linjat {

    private int     idLinjes;
    private int     idAeroportitNisjes;
    private int     idAeroportitMbrrritjes;
    private Integer distancaKm;
    private Integer kohaFluturimitMin;
    private boolean eshteAktive;

    // ── Joined fields ────────────────────────────────────────────────────
    private String kodiIataNisjes;       // aeroportet.kodi_iata (nisja)
    private String emriNisjes;           // aeroportet.emri_shqip (nisja)
    private String kodiIataMbrrritjes;   // aeroportet.kodi_iata (mbërrritja)
    private String emriMbrrritjes;       // aeroportet.emri_shqip (mbërrritja)

    public Linjat() {}

    public Linjat(int idLinjes, int idAeroportitNisjes, int idAeroportitMbrrritjes,
                 Integer distancaKm, Integer kohaFluturimitMin, boolean eshteAktive) {
        this.idLinjes               = idLinjes;
        this.idAeroportitNisjes     = idAeroportitNisjes;
        this.idAeroportitMbrrritjes = idAeroportitMbrrritjes;
        this.distancaKm             = distancaKm;
        this.kohaFluturimitMin      = kohaFluturimitMin;
        this.eshteAktive            = eshteAktive;
    }

    // ── Getters ───────────────────────────────────────────────────────────
    public int     getIdLinjes()                 { return idLinjes; }
    public int     getIdAeroportitNisjes()       { return idAeroportitNisjes; }
    public int     getIdAeroportitMbrrritjes()   { return idAeroportitMbrrritjes; }
    public Integer getDistancaKm()               { return distancaKm; }
    public Integer getKohaFluturimitMin()         { return kohaFluturimitMin; }
    public boolean isEshteAktive()               { return eshteAktive; }
    public String  getKodiIataNisjes()           { return kodiIataNisjes; }
    public String  getEmriNisjes()               { return emriNisjes; }
    public String  getKodiIataMbrrritjes()       { return kodiIataMbrrritjes; }
    public String  getEmriMbrrritjes()           { return emriMbrrritjes; }

    // ── Setters ───────────────────────────────────────────────────────────
    public void setIdAeroportitNisjes(int v)     { this.idAeroportitNisjes = v; }
    public void setIdAeroportitMbrrritjes(int v) { this.idAeroportitMbrrritjes = v; }
    public void setDistancaKm(Integer v)         { this.distancaKm = v; }
    public void setKohaFluturimitMin(Integer v)  { this.kohaFluturimitMin = v; }
    public void setEshteAktive(boolean v)        { this.eshteAktive = v; }
    public void setKodiIataNisjes(String v)      { this.kodiIataNisjes = v; }
    public void setEmriNisjes(String v)          { this.emriNisjes = v; }
    public void setKodiIataMbrrritjes(String v)  { this.kodiIataMbrrritjes = v; }
    public void setEmriMbrrritjes(String v)      { this.emriMbrrritjes = v; }

    /** Label i gatshëm për ComboBox/TableView: "PRN → VIE" */
    public String getLabel() {
        String nga  = kodiIataNisjes   != null ? kodiIataNisjes   : "?";
        String deri = kodiIataMbrrritjes != null ? kodiIataMbrrritjes : "?";
        return nga + " → " + deri;
    }
}