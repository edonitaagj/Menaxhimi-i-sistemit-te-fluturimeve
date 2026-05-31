package models.dto;

public class EditoPunonjesDTO {
    private int idStafit;
    private String emri;
    private String mbiemri;
    private String email;
    private String telefoni;
    private String emriRoli;
    private String departamenti;
    private double paga;
    private boolean eshteAktiv;

    public EditoPunonjesDTO(int idStafit, String emri, String mbiemri, String email,
                            String telefoni, String emriRoli, String departamenti, double paga, boolean eshteAktiv) {
        this.idStafit = idStafit;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.email = email;
        this.telefoni = telefoni;
        this.emriRoli = emriRoli;
        this.departamenti = departamenti;
        this.paga = paga;
        this.eshteAktiv = eshteAktiv;
    }

    // Getters
    public int getIdStafit() { return idStafit; }
    public String getEmri() { return emri; }
    public String getMbiemri() { return mbiemri; }
    public String getEmail() { return email; }
    public String getTelefoni() { return telefoni; }
    public String getEmriRoli() { return emriRoli; }
    public String getDepartamenti() { return departamenti; }
    public double getPaga() { return paga; }
    public boolean isEshteAktiv() { return eshteAktiv; }
}