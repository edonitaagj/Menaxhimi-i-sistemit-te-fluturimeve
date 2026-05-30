package models;

public class EkuipazhiFluturimit {
    private int idFluturimit;
    private int idStafit;
    private String roliNeFluturim;
    private Boolean konfirmuar;

    public EkuipazhiFluturimit(int idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public EkuipazhiFluturimit(
            int idFluturimit,
            int idStafit,
            String roliNeFluturim,
            Boolean konfirmuar
    ) {
        this.idFluturimit = idFluturimit;
        this.idStafit = idStafit;
        this.roliNeFluturim = roliNeFluturim;
        this.konfirmuar = konfirmuar;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public void setIdFluturimit(int idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public int getIdStafit() {
        return idStafit;
    }

    public void setIdStafit(int idStafit) {
        this.idStafit = idStafit;
    }

    public String getRoliNeFluturim() {
        return roliNeFluturim;
    }

    public void setRoliNeFluturim(String roliNeFluturim) {
        this.roliNeFluturim = roliNeFluturim;
    }

    public Boolean getKonfirmuar() {
        return konfirmuar;
    }

    public void setKonfirmuar(Boolean konfirmuar) {
        this.konfirmuar = konfirmuar;
    }

}