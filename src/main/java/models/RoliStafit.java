package models;

public class RoliStafit {
    private int idRolit;
    private String emriRoli;
    private String departamenti;

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
        this.departamenti = departamenti;
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

    public String getDepartamenti() {
        return departamenti;
    }

    public void setDepartamenti(String departamenti) {
        this.departamenti = departamenti;
    }

}