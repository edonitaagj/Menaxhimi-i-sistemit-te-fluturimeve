package models;

import java.sql.Date;

public class Stafi {
    private int idStafit;
    private int idRolit;
    private Integer idKompanise;
    private Integer idAeroportit;
    private String emri;
    private String mbiemri;
    private String numriPunonjesit;
    private String emailPunes;
    private String telefoni;
    private Date datelindja;
    private Date dataFillimit;
    private Date dataMbarimit;
    private Boolean eshteAktiv;

    public Stafi(int idStafit) {
        this.idStafit = idStafit;
    }

    public Stafi(
            int idStafit,
            int idRolit,
            Integer idKompanise,
            Integer idAeroportit,
            String emri,
            String mbiemri,
            String numriPunonjesit,
            String emailPunes,
            String telefoni,
            Date datelindja,
            Date dataFillimit,
            Date dataMbarimit,
            Boolean eshteAktiv
    ) {
        this.idStafit = idStafit;
        this.idRolit = idRolit;
        this.idKompanise = idKompanise;
        this.idAeroportit = idAeroportit;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.numriPunonjesit = numriPunonjesit;
        this.emailPunes = emailPunes;
        this.telefoni = telefoni;
        this.datelindja = datelindja;
        this.dataFillimit = dataFillimit;
        this.dataMbarimit = dataMbarimit;
        this.eshteAktiv = eshteAktiv;
    }

    public int getIdStafit() {
        return idStafit;
    }

    public void setIdStafit(int idStafit) {
        this.idStafit = idStafit;
    }

    public int getIdRolit() {
        return idRolit;
    }

    public void setIdRolit(int idRolit) {
        this.idRolit = idRolit;
    }

    public Integer getIdKompanise() {
        return idKompanise;
    }

    public void setIdKompanise(Integer idKompanise) {
        this.idKompanise = idKompanise;
    }

    public Integer getIdAeroportit() {
        return idAeroportit;
    }

    public void setIdAeroportit(Integer idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getMbiemri() {
        return mbiemri;
    }

    public void setMbiemri(String mbiemri) {
        this.mbiemri = mbiemri;
    }

    public String getNumriPunonjesit() {
        return numriPunonjesit;
    }

    public void setNumriPunonjesit(String numriPunonjesit) {
        this.numriPunonjesit = numriPunonjesit;
    }

    public String getEmailPunes() {
        return emailPunes;
    }

    public void setEmailPunes(String emailPunes) {
        this.emailPunes = emailPunes;
    }

    public String getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(String telefoni) {
        this.telefoni = telefoni;
    }

    public Date getDatelindja() {
        return datelindja;
    }

    public void setDatelindja(Date datelindja) {
        this.datelindja = datelindja;
    }

    public Date getDataFillimit() {
        return dataFillimit;
    }

    public void setDataFillimit(Date dataFillimit) {
        this.dataFillimit = dataFillimit;
    }

    public Date getDataMbarimit() {
        return dataMbarimit;
    }

    public void setDataMbarimit(Date dataMbarimit) {
        this.dataMbarimit = dataMbarimit;
    }

    public Boolean getEshteAktiv() {
        return eshteAktiv;
    }

    public void setEshteAktiv(Boolean eshteAktiv) {
        this.eshteAktiv = eshteAktiv;
    }

}