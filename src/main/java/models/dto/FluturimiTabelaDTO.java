package models.dto;

import java.sql.Timestamp;

public class FluturimiTabelaDTO {
    private int idFluturimit;
    private String kodiFluturimit;
    private String emriKompanise;
    private String nisja;       // Aeroporti i Nisjes
    private String mberritja;   // Aeroporti i Mbërritjes (Destinacioni)
    private Timestamp dataOra;
    private String statusi;

    public FluturimiTabelaDTO(int idFluturimit, String kodiFluturimit, String emriKompanise,
                              String nisja, String mberritja, Timestamp dataOra, String statusi) {
        this.idFluturimit = idFluturimit;
        this.kodiFluturimit = kodiFluturimit;
        this.emriKompanise = emriKompanise;
        this.nisja = nisja;
        this.mberritja = mberritja;
        this.dataOra = dataOra;
        this.statusi = statusi;
    }

    // Getters dhe Setters public për JavaFX Reflection
    public int getIdFluturimit() { return idFluturimit; }
    public void setIdFluturimit(int idFluturimit) { this.idFluturimit = idFluturimit; }

    public String getKodiFluturimit() { return kodiFluturimit; }
    public void setKodiFluturimit(String kodiFluturimit) { this.kodiFluturimit = kodiFluturimit; }

    public String getEmriKompanise() { return emriKompanise; }
    public void setEmriKompanise(String emriKompanise) { this.emriKompanise = emriKompanise; }

    public String getNisja() { return nisja; }
    public void setNisja(String nisja) { this.nisja = nisja; }

    public String getMberritja() { return mberritja; }
    public void setMberritja(String mberritja) { this.mberritja = mberritja; }

    public Timestamp getDataOra() { return dataOra; }
    public void setDataOra(Timestamp dataOra) { this.dataOra = dataOra; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }
}