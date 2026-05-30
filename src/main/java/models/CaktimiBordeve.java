package models;

import java.sql.Timestamp;

public class CaktimiBordeve {
    private int idCaktimit;
    private int idFluturimit;
    private int idBordit;
    private Timestamp hapurNga;
    private Timestamp mbyllurNe;

    public CaktimiBordeve(int idCaktimit) {
        this.idCaktimit = idCaktimit;
    }

    public CaktimiBordeve(
            int idCaktimit,
            int idFluturimit,
            int idBordit,
            Timestamp hapurNga,
            Timestamp mbyllurNe
    ) {
        this.idCaktimit = idCaktimit;
        this.idFluturimit = idFluturimit;
        this.idBordit = idBordit;
        this.hapurNga = hapurNga;
        this.mbyllurNe = mbyllurNe;
    }

    public int getIdCaktimit() {
        return idCaktimit;
    }

    public void setIdCaktimit(int idCaktimit) {
        this.idCaktimit = idCaktimit;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public void setIdFluturimit(int idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public int getIdBordit() {
        return idBordit;
    }

    public void setIdBordit(int idBordit) {
        this.idBordit = idBordit;
    }

    public Timestamp getHapurNga() {
        return hapurNga;
    }

    public void setHapurNga(Timestamp hapurNga) {
        this.hapurNga = hapurNga;
    }

    public Timestamp getMbyllurNe() {
        return mbyllurNe;
    }

    public void setMbyllurNe(Timestamp mbyllurNe) {
        this.mbyllurNe = mbyllurNe;
    }

}