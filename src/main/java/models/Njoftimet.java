package models;

import java.sql.Timestamp;

public class Njoftimet {
    private int idNjoftimit;
    private Integer idFluturimit;
    private int idAeroportit;
    private String lloji;
    private String titulliSq;
    private String mesazhiSq;
    private Timestamp aktivNga;
    private Timestamp aktivDeri;

    public Njoftimet(int idNjoftimit) {
        this.idNjoftimit = idNjoftimit;
    }

    public Njoftimet(
            int idNjoftimit,
            Integer idFluturimit,
            int idAeroportit,
            String lloji,
            String titulliSq,
            String mesazhiSq,
            Timestamp aktivNga,
            Timestamp aktivDeri
    ) {
        this.idNjoftimit = idNjoftimit;
        this.idFluturimit = idFluturimit;
        this.idAeroportit = idAeroportit;
        this.lloji = lloji;
        this.titulliSq = titulliSq;
        this.mesazhiSq = mesazhiSq;
        this.aktivNga = aktivNga;
        this.aktivDeri = aktivDeri;
    }

    public int getIdNjoftimit() {
        return idNjoftimit;
    }

    public void setIdNjoftimit(int idNjoftimit) {
        this.idNjoftimit = idNjoftimit;
    }

    public Integer getIdFluturimit() {
        return idFluturimit;
    }

    public void setIdFluturimit(Integer idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public int getIdAeroportit() {
        return idAeroportit;
    }

    public void setIdAeroportit(int idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public String getLloji() {
        return lloji;
    }

    public void setLloji(String lloji) {
        this.lloji = lloji;
    }

    public String getTitulliSq() {
        return titulliSq;
    }

    public void setTitulliSq(String titulliSq) {
        this.titulliSq = titulliSq;
    }

    public String getMesazhiSq() {
        return mesazhiSq;
    }

    public void setMesazhiSq(String mesazhiSq) {
        this.mesazhiSq = mesazhiSq;
    }

    public Timestamp getAktivNga() {
        return aktivNga;
    }

    public void setAktivNga(Timestamp aktivNga) {
        this.aktivNga = aktivNga;
    }

    public Timestamp getAktivDeri() {
        return aktivDeri;
    }

    public void setAktivDeri(Timestamp aktivDeri) {
        this.aktivDeri = aktivDeri;
    }

}