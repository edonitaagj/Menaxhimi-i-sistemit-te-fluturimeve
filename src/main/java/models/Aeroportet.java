package models;

public class Aeroportet {
    private int idAeroportit;
    private int idQytetit;
    private String kodiIata;
    private String kodiIcao;
    private String emriShqip;
    private Double latituda;
    private Double longituda;
    private Integer lartesiaM;
    private Integer numriTerminaleve;
    private Boolean eshteNderkombetare;

    public Aeroportet(int idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public Aeroportet(
            int idAeroportit,
            int idQytetit,
            String kodiIata,
            String kodiIcao,
            String emriShqip,
            Double latituda,
            Double longituda,
            Integer lartesiaM,
            Integer numriTerminaleve,
            Boolean eshteNderkombetare
    ) {
        this.idAeroportit = idAeroportit;
        this.idQytetit = idQytetit;
        this.kodiIata = kodiIata;
        this.kodiIcao = kodiIcao;
        this.emriShqip = emriShqip;
        this.latituda = latituda;
        this.longituda = longituda;
        this.lartesiaM = lartesiaM;
        this.numriTerminaleve = numriTerminaleve;
        this.eshteNderkombetare = eshteNderkombetare;
    }

    public int getIdAeroportit() {
        return idAeroportit;
    }

    public void setIdAeroportit(int idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public int getIdQytetit() {
        return idQytetit;
    }

    public void setIdQytetit(int idQytetit) {
        this.idQytetit = idQytetit;
    }

    public String getKodiIata() {
        return kodiIata;
    }

    public void setKodiIata(String kodiIata) {
        this.kodiIata = kodiIata;
    }

    public String getKodiIcao() {
        return kodiIcao;
    }

    public void setKodiIcao(String kodiIcao) {
        this.kodiIcao = kodiIcao;
    }

    public String getEmriShqip() {
        return emriShqip;
    }

    public void setEmriShqip(String emriShqip) {
        this.emriShqip = emriShqip;
    }

    public Double getLatituda() {
        return latituda;
    }

    public void setLatituda(Double latituda) {
        this.latituda = latituda;
    }

    public Double getLongituda() {
        return longituda;
    }

    public void setLongituda(Double longituda) {
        this.longituda = longituda;
    }

    public Integer getLartesiaM() {
        return lartesiaM;
    }

    public void setLartesiaM(Integer lartesiaM) {
        this.lartesiaM = lartesiaM;
    }

    public Integer getNumriTerminaleve() {
        return numriTerminaleve;
    }

    public void setNumriTerminaleve(Integer numriTerminaleve) {
        this.numriTerminaleve = numriTerminaleve;
    }

    public Boolean getEshteNderkombetare() {
        return eshteNderkombetare;
    }

    public void setEshteNderkombetare(Boolean eshteNderkombetare) {
        this.eshteNderkombetare = eshteNderkombetare;
    }

}