package models;

public class Linjat {
    private int idLinjes;
    private int idAeroportitNisjes;
    private int idAeroportitMbrrritjes;
    private Integer distancaKm;
    private Integer kohaFluturimitMin;
    private Boolean eshteAktive;

    public Linjat(int idLinjes) {
        this.idLinjes = idLinjes;
    }

    public Linjat(
            int idLinjes,
            int idAeroportitNisjes,
            int idAeroportitMbrrritjes,
            Integer distancaKm,
            Integer kohaFluturimitMin,
            Boolean eshteAktive
    ) {
        this.idLinjes = idLinjes;
        this.idAeroportitNisjes = idAeroportitNisjes;
        this.idAeroportitMbrrritjes = idAeroportitMbrrritjes;
        this.distancaKm = distancaKm;
        this.kohaFluturimitMin = kohaFluturimitMin;
        this.eshteAktive = eshteAktive;
    }

    public int getIdLinjes() {
        return idLinjes;
    }

    public void setIdLinjes(int idLinjes) {
        this.idLinjes = idLinjes;
    }

    public int getIdAeroportitNisjes() {
        return idAeroportitNisjes;
    }

    public void setIdAeroportitNisjes(int idAeroportitNisjes) {
        this.idAeroportitNisjes = idAeroportitNisjes;
    }

    public int getIdAeroportitMbrrritjes() {
        return idAeroportitMbrrritjes;
    }

    public void setIdAeroportitMbrrritjes(int idAeroportitMbrrritjes) {
        this.idAeroportitMbrrritjes = idAeroportitMbrrritjes;
    }

    public Integer getDistancaKm() {
        return distancaKm;
    }

    public void setDistancaKm(Integer distancaKm) {
        this.distancaKm = distancaKm;
    }

    public Integer getKohaFluturimitMin() {
        return kohaFluturimitMin;
    }

    public void setKohaFluturimitMin(Integer kohaFluturimitMin) {
        this.kohaFluturimitMin = kohaFluturimitMin;
    }

    public Boolean getEshteAktive() {
        return eshteAktive;
    }

    public void setEshteAktive(Boolean eshteAktive) {
        this.eshteAktive = eshteAktive;
    }

}