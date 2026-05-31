package models.dto;

public class FluturimSelectDto {
    private final int idFluturimit;
    private final String numriFluturimit;
    private final String relacioni;
    private final String dataFluturimit;
    private final String oraNisjes;
    private final String oraMbrritjes;
    private final int vendetELira;
    private final Integer distancaKm;
    private final String statusi;

    public FluturimSelectDto(
            int idFluturimit,
            String numriFluturimit,
            String relacioni,
            String dataFluturimit,
            String oraNisjes,
            String oraMbrritjes,
            int vendetELira,
            Integer distancaKm,
            String statusi
    ) {
        this.idFluturimit = idFluturimit;
        this.numriFluturimit = numriFluturimit;
        this.relacioni = relacioni;
        this.dataFluturimit = dataFluturimit;
        this.oraNisjes = oraNisjes;
        this.oraMbrritjes = oraMbrritjes;
        this.vendetELira = vendetELira;
        this.distancaKm = distancaKm;
        this.statusi = statusi;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public String getNumriFluturimit() {
        return numriFluturimit;
    }

    public String getRelacioni() {
        return relacioni;
    }

    public String getDataFluturimit() {
        return dataFluturimit;
    }

    public String getOraNisjes() {
        return oraNisjes;
    }

    public String getOraMbrritjes() {
        return oraMbrritjes;
    }

    public int getVendetELira() {
        return vendetELira;
    }

    public Integer getDistancaKm() {
        return distancaKm;
    }

    public String getStatusi() {
        return statusi;
    }

    @Override
    public String toString() {
        return numriFluturimit + " | " + relacioni + " | " + dataFluturimit + " " + oraNisjes + " | vende: " + vendetELira;
    }
}