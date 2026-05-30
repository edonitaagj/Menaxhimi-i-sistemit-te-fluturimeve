package models;

import java.sql.Date;

public class Oraret {
    private int idOrarit;
    private int idKompanise;
    private int idLinjes;
    private String numriFluturimit;
    private String ditaJaves;
    private String oraNisjes;
    private String oraMbrrritjes;
    private Integer kalimiNate;
    private Integer idAvionitDefault;
    private Date vlefshmeNga;
    private Date vlefshmeDeri;
    private Boolean eshteAktive;

    public Oraret(int idOrarit) {
        this.idOrarit = idOrarit;
    }

    public Oraret(
            int idOrarit,
            int idKompanise,
            int idLinjes,
            String numriFluturimit,
            String ditaJaves,
            String oraNisjes,
            String oraMbrrritjes,
            Integer kalimiNate,
            Integer idAvionitDefault,
            Date vlefshmeNga,
            Date vlefshmeDeri,
            Boolean eshteAktive
    ) {
        this.idOrarit = idOrarit;
        this.idKompanise = idKompanise;
        this.idLinjes = idLinjes;
        this.numriFluturimit = numriFluturimit;
        this.ditaJaves = ditaJaves;
        this.oraNisjes = oraNisjes;
        this.oraMbrrritjes = oraMbrrritjes;
        this.kalimiNate = kalimiNate;
        this.idAvionitDefault = idAvionitDefault;
        this.vlefshmeNga = vlefshmeNga;
        this.vlefshmeDeri = vlefshmeDeri;
        this.eshteAktive = eshteAktive;
    }

    public int getIdOrarit() {
        return idOrarit;
    }

    public void setIdOrarit(int idOrarit) {
        this.idOrarit = idOrarit;
    }

    public int getIdKompanise() {
        return idKompanise;
    }

    public void setIdKompanise(int idKompanise) {
        this.idKompanise = idKompanise;
    }

    public int getIdLinjes() {
        return idLinjes;
    }

    public void setIdLinjes(int idLinjes) {
        this.idLinjes = idLinjes;
    }

    public String getNumriFluturimit() {
        return numriFluturimit;
    }

    public void setNumriFluturimit(String numriFluturimit) {
        this.numriFluturimit = numriFluturimit;
    }

    public String getDitaJaves() {
        return ditaJaves;
    }

    public void setDitaJaves(String ditaJaves) {
        this.ditaJaves = ditaJaves;
    }

    public String getOraNisjes() {
        return oraNisjes;
    }

    public void setOraNisjes(String oraNisjes) {
        this.oraNisjes = oraNisjes;
    }

    public String getOraMbrrritjes() {
        return oraMbrrritjes;
    }

    public void setOraMbrrritjes(String oraMbrrritjes) {
        this.oraMbrrritjes = oraMbrrritjes;
    }

    public Integer getKalimiNate() {
        return kalimiNate;
    }

    public void setKalimiNate(Integer kalimiNate) {
        this.kalimiNate = kalimiNate;
    }

    public Integer getIdAvionitDefault() {
        return idAvionitDefault;
    }

    public void setIdAvionitDefault(Integer idAvionitDefault) {
        this.idAvionitDefault = idAvionitDefault;
    }

    public Date getVlefshmeNga() {
        return vlefshmeNga;
    }

    public void setVlefshmeNga(Date vlefshmeNga) {
        this.vlefshmeNga = vlefshmeNga;
    }

    public Date getVlefshmeDeri() {
        return vlefshmeDeri;
    }

    public void setVlefshmeDeri(Date vlefshmeDeri) {
        this.vlefshmeDeri = vlefshmeDeri;
    }

    public Boolean getEshteAktive() {
        return eshteAktive;
    }

    public void setEshteAktive(Boolean eshteAktive) {
        this.eshteAktive = eshteAktive;
    }

}