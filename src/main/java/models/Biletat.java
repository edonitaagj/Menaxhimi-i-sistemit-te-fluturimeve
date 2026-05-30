package models;

import java.sql.Timestamp;

public class Biletat {
    private int idBiletes;
    private int idRezervimit;
    private int idPasagjerit;
    private String numriBiletes;
    private String vendiUljes;
    private String klasaUljes;
    private Double cmimi;
    private Double taksa;
    private Boolean checkedIn;
    private Timestamp oraCheckin;
    private Integer bagazheKg;
    private String statusi;

    public Biletat(int idBiletes) {
        this.idBiletes = idBiletes;
    }

    public Biletat(
            int idBiletes,
            int idRezervimit,
            int idPasagjerit,
            String numriBiletes,
            String vendiUljes,
            String klasaUljes,
            Double cmimi,
            Double taksa,
            Boolean checkedIn,
            Timestamp oraCheckin,
            Integer bagazheKg,
            String statusi
    ) {
        this.idBiletes = idBiletes;
        this.idRezervimit = idRezervimit;
        this.idPasagjerit = idPasagjerit;
        this.numriBiletes = numriBiletes;
        this.vendiUljes = vendiUljes;
        this.klasaUljes = klasaUljes;
        this.cmimi = cmimi;
        this.taksa = taksa;
        this.checkedIn = checkedIn;
        this.oraCheckin = oraCheckin;
        this.bagazheKg = bagazheKg;
        this.statusi = statusi;
    }

    public int getIdBiletes() {
        return idBiletes;
    }

    public void setIdBiletes(int idBiletes) {
        this.idBiletes = idBiletes;
    }

    public int getIdRezervimit() {
        return idRezervimit;
    }

    public void setIdRezervimit(int idRezervimit) {
        this.idRezervimit = idRezervimit;
    }

    public int getIdPasagjerit() {
        return idPasagjerit;
    }

    public void setIdPasagjerit(int idPasagjerit) {
        this.idPasagjerit = idPasagjerit;
    }

    public String getNumriBiletes() {
        return numriBiletes;
    }

    public void setNumriBiletes(String numriBiletes) {
        this.numriBiletes = numriBiletes;
    }

    public String getVendiUljes() {
        return vendiUljes;
    }

    public void setVendiUljes(String vendiUljes) {
        this.vendiUljes = vendiUljes;
    }

    public String getKlasaUljes() {
        return klasaUljes;
    }

    public void setKlasaUljes(String klasaUljes) {
        this.klasaUljes = klasaUljes;
    }

    public Double getCmimi() {
        return cmimi;
    }

    public void setCmimi(Double cmimi) {
        this.cmimi = cmimi;
    }

    public Double getTaksa() {
        return taksa;
    }

    public void setTaksa(Double taksa) {
        this.taksa = taksa;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }

    public Timestamp getOraCheckin() {
        return oraCheckin;
    }

    public void setOraCheckin(Timestamp oraCheckin) {
        this.oraCheckin = oraCheckin;
    }

    public Integer getBagazheKg() {
        return bagazheKg;
    }

    public void setBagazheKg(Integer bagazheKg) {
        this.bagazheKg = bagazheKg;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

}