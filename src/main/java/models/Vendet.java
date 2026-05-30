package models;

public class Vendet {
    private int idVendi;
    private String kodiIso;
    private String emriShqip;
    private String kodiTelefon;

    public Vendet(int idVendi) {
        this.idVendi = idVendi;
    }

    public Vendet(
            int idVendi,
            String kodiIso,
            String emriShqip,
            String kodiTelefon
    ) {
        this.idVendi = idVendi;
        this.kodiIso = kodiIso;
        this.emriShqip = emriShqip;
        this.kodiTelefon = kodiTelefon;
    }

    public int getIdVendi() {
        return idVendi;
    }

    public void setIdVendi(int idVendi) {
        this.idVendi = idVendi;
    }

    public String getKodiIso() {
        return kodiIso;
    }

    public void setKodiIso(String kodiIso) {
        this.kodiIso = kodiIso;
    }

    public String getEmriShqip() {
        return emriShqip;
    }

    public void setEmriShqip(String emriShqip) {
        this.emriShqip = emriShqip;
    }

    public String getKodiTelefon() {
        return kodiTelefon;
    }

    public void setKodiTelefon(String kodiTelefon) {
        this.kodiTelefon = kodiTelefon;
    }

}