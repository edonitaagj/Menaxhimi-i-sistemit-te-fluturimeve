package models;

public class BordetCheckin {
    private int idBordit;
    private int idAeroportit;
    private int idTerminalit;
    private String numriBordit;
    private String lloji;
    private Boolean eshteAktive;

    public BordetCheckin(int idBordit) {
        this.idBordit = idBordit;
    }

    public BordetCheckin(
            int idBordit,
            int idAeroportit,
            int idTerminalit,
            String numriBordit,
            String lloji,
            Boolean eshteAktive
    ) {
        this.idBordit = idBordit;
        this.idAeroportit = idAeroportit;
        this.idTerminalit = idTerminalit;
        this.numriBordit = numriBordit;
        this.lloji = lloji;
        this.eshteAktive = eshteAktive;
    }

    public int getIdBordit() {
        return idBordit;
    }

    public void setIdBordit(int idBordit) {
        this.idBordit = idBordit;
    }

    public int getIdAeroportit() {
        return idAeroportit;
    }

    public void setIdAeroportit(int idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public int getIdTerminalit() {
        return idTerminalit;
    }

    public void setIdTerminalit(int idTerminalit) {
        this.idTerminalit = idTerminalit;
    }

    public String getNumriBordit() {
        return numriBordit;
    }

    public void setNumriBordit(String numriBordit) {
        this.numriBordit = numriBordit;
    }

    public String getLloji() {
        return lloji;
    }

    public void setLloji(String lloji) {
        this.lloji = lloji;
    }

    public Boolean getEshteAktive() {
        return eshteAktive;
    }

    public void setEshteAktive(Boolean eshteAktive) {
        this.eshteAktive = eshteAktive;
    }

}