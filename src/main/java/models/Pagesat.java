package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Pagesat {
    private int idPageses;
    private int idRezervimit;
    private BigDecimal shuma;
    private String monedha;
    private String metoda;      // 'karte','cash','transfer','online','voucher'
    private String statusi;     // 'e_pritshme','e_kryer','e_deshtuar','e_kthyer'
    private String referencaTransaksionit;
    private LocalDateTime dataPageses;
    private String shenimet;

    public Pagesat(int idPageses) {
        this.idPageses = idPageses;
    }

    public Pagesat(
            int idPageses,
            int idRezervimit,
            BigDecimal shuma,
            String monedha,
            String metoda,
            String statusi,
            String referencaTransaksionit,
            LocalDateTime dataPageses,
            String shenimet
    ) {
        this.idPageses = idPageses;
        this.idRezervimit = idRezervimit;
        this.shuma = shuma;
        this.monedha = monedha;
        this.metoda = metoda;
        this.statusi = statusi;
        this.referencaTransaksionit = referencaTransaksionit;
        this.dataPageses = dataPageses;
        this.shenimet = shenimet;
    }

    public int getIdPageses() {
        return idPageses;
    }

    public void setIdPageses(int idPageses) {
        this.idPageses = idPageses;
    }

    public int getIdRezervimit() {
        return idRezervimit;
    }

    public void setIdRezervimit(int idRezervimit) {
        this.idRezervimit = idRezervimit;
    }

    public BigDecimal getShuma() {
        return shuma;
    }

    public void setShuma(BigDecimal shuma) {
        this.shuma = shuma;
    }

    public String getMonedha() {
        return monedha;
    }

    public void setMonedha(String monedha) {
        this.monedha = monedha;
    }

    public String getMetoda() {
        return metoda;
    }

    public void setMetoda(String metoda) {
        this.metoda = metoda;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

    public String getReferencaTransaksionit() {
        return referencaTransaksionit;
    }

    public void setReferencaTransaksionit(String referencaTransaksionit) {
        this.referencaTransaksionit = referencaTransaksionit;
    }

    public LocalDateTime getDataPageses() {
        return dataPageses;
    }

    public void setDataPageses(LocalDateTime dataPageses) {
        this.dataPageses = dataPageses;
    }

    public String getShenimet() {
        return shenimet;
    }

    public void setShenimet(String shenimet) {
        this.shenimet = shenimet;
    }
}