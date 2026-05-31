package models.dto;

public class StafiTableDTO {
    private int idStafit;
    private String emri;
    private String mbiemri;
    private String emriRoli;
    private String departamenti;
    private String emailPunes;
    private boolean eshteAktiv;

    public StafiTableDTO(int idStafit, String emri, String mbiemri, String emriRoli, String departamenti, String emailPunes, boolean eshteAktiv) {
        this.idStafit = idStafit;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.emriRoli = emriRoli;
        this.departamenti = departamenti;
        this.emailPunes = emailPunes;
        this.eshteAktiv = eshteAktiv;
    }

    // Getters për JavaFX TableView (PropertyValueFactory lidhet me këto emra saktësisht)
    public int getIdStafit() { return idStafit; }
    public String getEmri() { return emri; }
    public String getMbiemri() { return mbiemri; }
    public String getEmriRoli() { return emriRoli; }
    public String getDepartamenti() { return departamenti; }
    public String getEmailPunes() { return emailPunes; }
    public boolean isEshteAktiv() { return eshteAktiv; }
}