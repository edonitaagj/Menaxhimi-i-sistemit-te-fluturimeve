package models;

public class Qytetet {
    private int idQytetit;
    private int idVendit;
    private String emriShqip;
    private String zonaKohore;

    public Qytetet(int idQytetit) {
        this.idQytetit = idQytetit;
    }

    public Qytetet(
            int idQytetit,
            int idVendit,
            String emriShqip,
            String zonaKohore
    ) {
        this.idQytetit = idQytetit;
        this.idVendit = idVendit;
        this.emriShqip = emriShqip;
        this.zonaKohore = zonaKohore;
    }

    public int getIdQytetit() {
        return idQytetit;
    }

    public void setIdQytetit(int idQytetit) {
        this.idQytetit = idQytetit;
    }

    public int getIdVendit() {
        return idVendit;
    }

    public void setIdVendit(int idVendit) {
        this.idVendit = idVendit;
    }

    public String getEmriShqip() {
        return emriShqip;
    }

    public void setEmriShqip(String emriShqip) {
        this.emriShqip = emriShqip;
    }

    public String getZonaKohore() {
        return zonaKohore;
    }

    public void setZonaKohore(String zonaKohore) {
        this.zonaKohore = zonaKohore;
    }

}