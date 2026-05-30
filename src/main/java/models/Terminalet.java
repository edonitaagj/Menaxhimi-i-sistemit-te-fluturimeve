package models;

public class Terminalet {
    private int idTerminalit;
    private int idAeroportit;
    private String kodiTerminalit;
    private String emri;
    private Integer kapacitetiMax;

    public Terminalet(int idTerminalit) {
        this.idTerminalit = idTerminalit;
    }

    public Terminalet(
            int idTerminalit,
            int idAeroportit,
            String kodiTerminalit,
            String emri,
            Integer kapacitetiMax
    ) {
        this.idTerminalit = idTerminalit;
        this.idAeroportit = idAeroportit;
        this.kodiTerminalit = kodiTerminalit;
        this.emri = emri;
        this.kapacitetiMax = kapacitetiMax;
    }

    public int getIdTerminalit() {
        return idTerminalit;
    }

    public void setIdTerminalit(int idTerminalit) {
        this.idTerminalit = idTerminalit;
    }

    public int getIdAeroportit() {
        return idAeroportit;
    }

    public void setIdAeroportit(int idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public String getKodiTerminalit() {
        return kodiTerminalit;
    }

    public void setKodiTerminalit(String kodiTerminalit) {
        this.kodiTerminalit = kodiTerminalit;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public Integer getKapacitetiMax() {
        return kapacitetiMax;
    }

    public void setKapacitetiMax(Integer kapacitetiMax) {
        this.kapacitetiMax = kapacitetiMax;
    }

}