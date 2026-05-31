package models.dto;

import java.time.LocalDate;

public class PasagjeriTableDTO implements IRequestDto {
    private int idPasagjerit;
    private String numriPasaportes;
    private String emri;
    private String mbiemri;
    private String shtetesia; // Tekst për UI (p.sh. "Kosovë")
    private String gjinia;
    private LocalDate datelindja;
    private String email;
    private String telefoni;
    private String adresa;
    private LocalDate pasaportaSkadimi;

    public PasagjeriTableDTO(int idPasagjerit, String numriPasaportes, String emri, String mbiemri,
                             String shtetesia, String gjinia, LocalDate datelindja, String email,
                             String telefoni, String adresa, LocalDate pasaportaSkadimi) {
        this.idPasagjerit = idPasagjerit;
        this.numriPasaportes = numriPasaportes;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.shtetesia = shtetesia;
        this.gjinia = gjinia;
        this.datelindja = datelindja;
        this.email = email;
        this.telefoni = telefoni;
        this.adresa = adresa;
        this.pasaportaSkadimi = pasaportaSkadimi;
    }

    // Getters
    public int getIdPasagjerit() { return idPasagjerit; }
    public String getNumriPasaportes() { return numriPasaportes; }
    public String getEmri() { return emri; }
    public String getMbiemri() { return mbiemri; }
    public String getShtetesia() { return shtetesia; }
    public String getGjinia() { return gjinia; }
    public LocalDate getDatelindja() { return datelindja; }
    public String getEmail() { return email; }
    public String getTelefoni() { return telefoni; }
    public String getAdresa() { return adresa; }
    public LocalDate getPasaportaSkadimi() { return pasaportaSkadimi; }
}