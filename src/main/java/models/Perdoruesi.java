package models;

import java.sql.Timestamp;

public class Perdoruesi {
    private int idPerdoruesit;
    private Integer idStafit; // mund te jete null
    private String emri;
    private String mbiemri;
    private String email;
    private String passwordHash;
    private String roli;
    private boolean eshteAktiv;
    private int tentativaLogin;
    private Timestamp lastLogin;
    private String username;

    // Constructor minimal (p.sh. per login)
    public Perdoruesi(int idPerdoruesit, String email, String passwordHash) {
        this.idPerdoruesit = idPerdoruesit;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    // Constructor i plote
    public Perdoruesi(
            int idPerdoruesit,
            Integer idStafit,
            String emri,
            String mbiemri,
            String email,
            String passwordHash,
            String roli,
            boolean eshteAktiv,
            int tentativaLogin,
            Timestamp lastLogin,
            String username
    ) {
        this.idPerdoruesit = idPerdoruesit;
        this.idStafit = idStafit;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roli = roli;
        this.eshteAktiv = eshteAktiv;
        this.tentativaLogin = tentativaLogin;
        this.lastLogin = lastLogin;
        this.username = username;
    }

    // Getters & Setters

    public int getIdPerdoruesit() {
        return idPerdoruesit;
    }

    public Integer getIdStafit() {
        return idStafit;
    }

    public void setIdStafit(Integer idStafit) {
        this.idStafit = idStafit;
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

    public String getFullName() {
        return emri + " " + mbiemri;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRoli() {
        return roli;
    }

    public void setRoli(String roli) {
        this.roli = roli;
    }

    public boolean isEshteAktiv() {
        return eshteAktiv;
    }

    public void setEshteAktiv(boolean eshteAktiv) {
        this.eshteAktiv = eshteAktiv;
    }

    public int getTentativaLogin() {
        return tentativaLogin;
    }

    public void setTentativaLogin(int tentativaLogin) {
        this.tentativaLogin = tentativaLogin;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}