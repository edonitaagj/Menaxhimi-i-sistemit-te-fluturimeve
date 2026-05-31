package models.dto;

public class BiletaTableDto {
    private int idBiletes;
    private String numriBiletes;
    private String vendiUljes;
    private String klasaUljes;
    private double cmimi;
    private double taksa;
    private boolean checkedIn;
    private String statusiBiletes;

    public BiletaTableDto() {}

    public BiletaTableDto(int idBiletes, String numriBiletes, String vendiUljes,
                          String klasaUljes, double cmimi, double taksa,
                          boolean checkedIn, String statusiBiletes) {
        this.idBiletes = idBiletes;
        this.numriBiletes = numriBiletes;
        this.vendiUljes = vendiUljes;
        this.klasaUljes = klasaUljes;
        this.cmimi = cmimi;
        this.taksa = taksa;
        this.checkedIn = checkedIn;
        this.statusiBiletes = statusiBiletes;
    }

    public int getIdBiletes() { return idBiletes; }
    public void setIdBiletes(int idBiletes) { this.idBiletes = idBiletes; }

    public String getNumriBiletes() { return numriBiletes; }
    public void setNumriBiletes(String numriBiletes) { this.numriBiletes = numriBiletes; }

    public String getVendiUljes() { return vendiUljes; }
    public void setVendiUljes(String vendiUljes) { this.vendiUljes = vendiUljes; }

    public String getKlasaUljes() { return klasaUljes; }
    public void setKlasaUljes(String klasaUljes) { this.klasaUljes = klasaUljes; }

    public double getCmimi() { return cmimi; }
    public void setCmimi(double cmimi) { this.cmimi = cmimi; }

    public double getTaksa() { return taksa; }
    public void setTaksa(double taksa) { this.taksa = taksa; }

    public boolean isCheckedIn() { return checkedIn; }
    public void setCheckedIn(boolean checkedIn) { this.checkedIn = checkedIn; }

    public String getStatusiBiletes() { return statusiBiletes; }
    public void setStatusiBiletes(String statusiBiletes) { this.statusiBiletes = statusiBiletes; }

    public String getCheckedInStatusText() {
        return checkedIn ? "Po" : "Jo";
    }
}