package models.dto;

import java.time.LocalDate;

public class ShtoPunonjesDTO implements IRequestDto {
    private String emri;
    private String mbiemri;
    private String email;
    private String telefoni;
    private String departamenti;
    private String emriRoli;
    private LocalDate dataPunesimit;
    private double paga; // Mbajtur sipas fushës së formës suaj

    public ShtoPunonjesDTO(String emri, String mbiemri, String email, String telefoni,
                           String departamenti, String emriRoli, LocalDate dataPunesimit, double paga) {
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.email = email;
        this.telefoni = telefoni;
        this.departamenti = departamenti;
        this.emriRoli = emriRoli;
        this.dataPunesimit = dataPunesimit;
        this.paga = paga;
    }

    // Getters
    public String getEmri() { return emri; }
    public String getMbiemri() { return mbiemri; }
    public String getEmail() { return email; }
    public String getTelefoni() { return telefoni; }
    public String getDepartamenti() { return departamenti; }
    public String getEmriRoli() { return emriRoli; }
    public LocalDate getDataPunesimit() { return dataPunesimit; }
}