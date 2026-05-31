package models.dto;

public class RezervimiTableDto {
    private final int idRezervimit;
    private final String kodiRezervimit;
    private final String numriFluturimit;
    private final String relacioni;
    private final String dataRezervimit;
    private final String klasa;
    private final String cmimiTotal;
    private final String statusi;

    public RezervimiTableDto(
            int idRezervimit,
            String kodiRezervimit,
            String numriFluturimit,
            String relacioni,
            String dataRezervimit,
            String klasa,
            String cmimiTotal,
            String statusi
    ) {
        this.idRezervimit = idRezervimit;
        this.kodiRezervimit = kodiRezervimit;
        this.numriFluturimit = numriFluturimit;
        this.relacioni = relacioni;
        this.dataRezervimit = dataRezervimit;
        this.klasa = klasa;
        this.cmimiTotal = cmimiTotal;
        this.statusi = statusi;
    }

    public int getIdRezervimit() {
        return idRezervimit;
    }

    public String getKodiRezervimit() {
        return kodiRezervimit;
    }

    public String getNumriFluturimit() {
        return numriFluturimit;
    }

    public String getRelacioni() {
        return relacioni;
    }

    public String getDataRezervimit() {
        return dataRezervimit;
    }

    public String getKlasa() {
        return klasa;
    }

    public String getCmimiTotal() {
        return cmimiTotal;
    }

    public String getStatusi() {
        return statusi;
    }
}