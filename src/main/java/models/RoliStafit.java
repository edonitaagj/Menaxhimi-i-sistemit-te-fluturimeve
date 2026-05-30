package models;

public class RoliStafit {
    private int idRolit;
    private String emriRoli;
    private String pershkrimi;

    public RoliStafit(int idRolit) {
        this.idRolit = idRolit;
    }

    public RoliStafit(
            int idRolit,
            String emriRoli,
            String pershkrimi
    ) {
        this.idRolit = idRolit;
        this.emriRoli = emriRoli;
        this.pershkrimi = pershkrimi;
    }

    public int getIdRolit() {
        return idRolit;
    }

    public void setIdRolit(int idRolit) {
        this.idRolit = idRolit;
    }

    public String getEmriRoli() {
        return emriRoli;
    }

    public void setEmriRoli(String emriRoli) {
        this.emriRoli = emriRoli;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

}