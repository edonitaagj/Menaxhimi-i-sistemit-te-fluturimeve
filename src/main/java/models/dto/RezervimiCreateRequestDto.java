package models.dto;

import java.math.BigDecimal;

public class RezervimiCreateRequestDto {
    private int idFluturimit;
    private String klasa;
    private BigDecimal cmimiTotal;
    private String monedha;
    private String shenimet;

    public RezervimiCreateRequestDto(int idFluturimit, String klasa, BigDecimal cmimiTotal, String monedha, String shenimet) {
        this.idFluturimit = idFluturimit;
        this.klasa = klasa;
        this.cmimiTotal = cmimiTotal;
        this.monedha = monedha;
        this.shenimet = shenimet;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public String getKlasa() {
        return klasa;
    }

    public BigDecimal getCmimiTotal() {
        return cmimiTotal;
    }

    public String getMonedha() {
        return monedha;
    }

    public String getShenimet() {
        return shenimet;
    }
}