package models;

public class Gejtat {
    private int idGejtit;
    private int idTerminalit;
    private String kodiGejtit;
    private String lloji;
    private Boolean eshteAktive;

    public Gejtat(int idGejtit) {
        this.idGejtit = idGejtit;
    }

    public Gejtat(
            int idGejtit,
            int idTerminalit,
            String kodiGejtit,
            String lloji,
            Boolean eshteAktive
    ) {
        this.idGejtit = idGejtit;
        this.idTerminalit = idTerminalit;
        this.kodiGejtit = kodiGejtit;
        this.lloji = lloji;
        this.eshteAktive = eshteAktive;
    }

    public int getIdGejtit() {
        return idGejtit;
    }

    public void setIdGejtit(int idGejtit) {
        this.idGejtit = idGejtit;
    }

    public int getIdTerminalit() {
        return idTerminalit;
    }

    public void setIdTerminalit(int idTerminalit) {
        this.idTerminalit = idTerminalit;
    }

    public String getKodiGejtit() {
        return kodiGejtit;
    }

    public void setKodiGejtit(String kodiGejtit) {
        this.kodiGejtit = kodiGejtit;
    }

    public String getLloji() {
        return lloji;
    }

    public void setLloji(String lloji) {
        this.lloji = lloji;
    }

    public Boolean getEshteAktive() {
        return eshteAktive;
    }

    public void setEshteAktive(Boolean eshteAktive) {
        this.eshteAktive = eshteAktive;
    }

}