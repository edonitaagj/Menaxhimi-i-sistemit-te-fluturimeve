package models.dto;

public class RezervimeTableDto {
    private int idRezervimit;
    private String kodiRezervimit;
    private int idPasagjerit;
    private int idFluturimit;
    private String pasagjeri;
    private String fluturimiKodi;
    private String klasa;
    private double cmimiTotal;
    private String monedha;
    private String statusi;
    private String statusiPageses;
    private String dataRezervimit;
    private String dataAnulimit;
    private String shenimet;

    public RezervimeTableDto() {}

    public RezervimeTableDto(int idRezervimit, String kodiRezervimit, int idPasagjerit, int idFluturimit,
                             String pasagjeri, String fluturimiKodi, String klasa, double cmimiTotal,
                             String monedha, String statusi, String statusiPageses,
                             String dataRezervimit, String dataAnulimit, String shenimet) {
        this.idRezervimit = idRezervimit;
        this.kodiRezervimit = kodiRezervimit;
        this.idPasagjerit = idPasagjerit;
        this.idFluturimit = idFluturimit;
        this.pasagjeri = pasagjeri;
        this.fluturimiKodi = fluturimiKodi;
        this.klasa = klasa;
        this.cmimiTotal = cmimiTotal;
        this.monedha = monedha;
        this.statusi = statusi;
        this.statusiPageses = statusiPageses;
        this.dataRezervimit = dataRezervimit;
        this.dataAnulimit = dataAnulimit;
        this.shenimet = shenimet;
    }

    public int getIdRezervimit() { return idRezervimit; }
    public void setIdRezervimit(int idRezervimit) { this.idRezervimit = idRezervimit; }

    public String getKodiRezervimit() { return kodiRezervimit; }
    public void setKodiRezervimit(String kodiRezervimit) { this.kodiRezervimit = kodiRezervimit; }

    public int getIdPasagjerit() { return idPasagjerit; }
    public void setIdPasagjerit(int idPasagjerit) { this.idPasagjerit = idPasagjerit; }

    public int getIdFluturimit() { return idFluturimit; }
    public void setIdFluturimit(int idFluturimit) { this.idFluturimit = idFluturimit; }

    public String getPasagjeri() { return pasagjeri; }
    public void setPasagjeri(String pasagjeri) { this.pasagjeri = pasagjeri; }

    public String getFluturimiKodi() { return fluturimiKodi; }
    public void setFluturimiKodi(String fluturimiKodi) { this.fluturimiKodi = fluturimiKodi; }

    public String getKlasa() { return klasa; }
    public void setKlasa(String klasa) { this.klasa = klasa; }

    public double getCmimiTotal() { return cmimiTotal; }
    public void setCmimiTotal(double cmimiTotal) { this.cmimiTotal = cmimiTotal; }

    public String getMonedha() { return monedha; }
    public void setMonedha(String monedha) { this.monedha = monedha; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }

    public String getStatusiPageses() { return statusiPageses; }
    public void setStatusiPageses(String statusiPageses) { this.statusiPageses = statusiPageses; }

    public String getDataRezervimit() { return dataRezervimit; }
    public void setDataRezervimit(String dataRezervimit) { this.dataRezervimit = dataRezervimit; }

    public String getDataAnulimit() { return dataAnulimit; }
    public void setDataAnulimit(String dataAnulimit) { this.dataAnulimit = dataAnulimit; }

    public String getShenimet() { return shenimet; }
    public void setShenimet(String shenimet) { this.shenimet = shenimet; }
}