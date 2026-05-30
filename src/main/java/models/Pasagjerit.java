package models;

import java.sql.Date;

public class Pasagjerit {
    private int idPasagjerit;
    private String numriPasaportes;
    private int idVenditShtetesise;
    private String emri;
    private String mbiemri;
    private Date datelindja;
    private String gjinia;
    private String email;
    private String telefoni;
    private String adresa;
    private Date pasaportaSkadimi;

    public Pasagjerit(int idPasagjerit) {
        this.idPasagjerit = idPasagjerit;
    }

    public Pasagjerit(
            int idPasagjerit,
            String numriPasaportes,
            int idVenditShtetesise,
            String emri,
            String mbiemri,
            Date datelindja,
            String gjinia,
            String email,
            String telefoni,
            String adresa,
            Date pasaportaSkadimi
    ) {
        this.idPasagjerit = idPasagjerit;
        this.numriPasaportes = numriPasaportes;
        this.idVenditShtetesise = idVenditShtetesise;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.datelindja = datelindja;
        this.gjinia = gjinia;
        this.email = email;
        this.telefoni = telefoni;
        this.adresa = adresa;
        this.pasaportaSkadimi = pasaportaSkadimi;
    }

    public int getIdPasagjerit() {
        return idPasagjerit;
    }

    public void setIdPasagjerit(int idPasagjerit) {
        this.idPasagjerit = idPasagjerit;
    }

    public String getNumriPasaportes() {
        return numriPasaportes;
    }

    public void setNumriPasaportes(String numriPasaportes) {
        this.numriPasaportes = numriPasaportes;
    }

    public int getIdVenditShtetesise() {
        return idVenditShtetesise;
    }

    public void setIdVenditShtetesise(int idVenditShtetesise) {
        this.idVenditShtetesise = idVenditShtetesise;
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

    public Date getDatelindja() {
        return datelindja;
    }

    public void setDatelindja(Date datelindja) {
        this.datelindja = datelindja;
    }

    public String getGjinia() {
        return gjinia;
    }

    public void setGjinia(String gjinia) {
        this.gjinia = gjinia;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(String telefoni) {
        this.telefoni = telefoni;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Date getPasaportaSkadimi() {
        return pasaportaSkadimi;
    }

    public void setPasaportaSkadimi(Date pasaportaSkadimi) {
        this.pasaportaSkadimi = pasaportaSkadimi;
    }

}