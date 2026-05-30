package models;

import java.sql.Timestamp;

public class Rezervimet {
    private int idRezervimit;
    private String kodiRezervimit;
    private int idPasagjerit;
    private int idFluturimit;
    private String klasa;
    private Double cmimiTotal;
    private String monedha;
    private String statusi;
    private Timestamp dataRezervimit;
    private Timestamp dataAnulimit;
    private String shenimet;

    public Rezervimet(int idRezervimit) {
        this.idRezervimit = idRezervimit;
    }

    public Rezervimet(
            int idRezervimit,
            String kodiRezervimit,
            int idPasagjerit,
            int idFluturimit,
            String klasa,
            Double cmimiTotal,
            String monedha,
            String statusi,
            Timestamp dataRezervimit,
            Timestamp dataAnulimit,
            String shenimet
    ) {
        this.idRezervimit = idRezervimit;
        this.kodiRezervimit = kodiRezervimit;
        this.idPasagjerit = idPasagjerit;
        this.idFluturimit = idFluturimit;
        this.klasa = klasa;
        this.cmimiTotal = cmimiTotal;
        this.monedha = monedha;
        this.statusi = statusi;
        this.dataRezervimit = dataRezervimit;
        this.dataAnulimit = dataAnulimit;
        this.shenimet = shenimet;
    }

    public int getIdRezervimit() {
        return idRezervimit;
    }

    public void setIdRezervimit(int idRezervimit) {
        this.idRezervimit = idRezervimit;
    }

    public String getKodiRezervimit() {
        return kodiRezervimit;
    }

    public void setKodiRezervimit(String kodiRezervimit) {
        this.kodiRezervimit = kodiRezervimit;
    }

    public int getIdPasagjerit() {
        return idPasagjerit;
    }

    public void setIdPasagjerit(int idPasagjerit) {
        this.idPasagjerit = idPasagjerit;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public void setIdFluturimit(int idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public String getKlasa() {
        return klasa;
    }

    public void setKlasa(String klasa) {
        this.klasa = klasa;
    }

    public Double getCmimiTotal() {
        return cmimiTotal;
    }

    public void setCmimiTotal(Double cmimiTotal) {
        this.cmimiTotal = cmimiTotal;
    }

    public String getMonedha() {
        return monedha;
    }

    public void setMonedha(String monedha) {
        this.monedha = monedha;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

    public Timestamp getDataRezervimit() {
        return dataRezervimit;
    }

    public void setDataRezervimit(Timestamp dataRezervimit) {
        this.dataRezervimit = dataRezervimit;
    }

    public Timestamp getDataAnulimit() {
        return dataAnulimit;
    }

    public void setDataAnulimit(Timestamp dataAnulimit) {
        this.dataAnulimit = dataAnulimit;
    }

    public String getShenimet() {
        return shenimet;
    }

    public void setShenimet(String shenimet) {
        this.shenimet = shenimet;
    }

}