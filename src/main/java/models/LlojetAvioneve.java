package models;

public class LlojetAvioneve {
    private int idLlojit;
    private String prodhuesi;
    private String modeli;
    private String kodiIata;
    private int kapacitetiEkonomik;
    private Integer kapacitetiBiznes;
    private Integer kapacitetiFirst;
    private Integer rrezjaKm;
    private Integer shpejtesiaKmh;

    public LlojetAvioneve(int idLlojit) {
        this.idLlojit = idLlojit;
    }

    public LlojetAvioneve(
            int idLlojit,
            String prodhuesi,
            String modeli,
            String kodiIata,
            int kapacitetiEkonomik,
            Integer kapacitetiBiznes,
            Integer kapacitetiFirst,
            Integer rrezjaKm,
            Integer shpejtesiaKmh
    ) {
        this.idLlojit = idLlojit;
        this.prodhuesi = prodhuesi;
        this.modeli = modeli;
        this.kodiIata = kodiIata;
        this.kapacitetiEkonomik = kapacitetiEkonomik;
        this.kapacitetiBiznes = kapacitetiBiznes;
        this.kapacitetiFirst = kapacitetiFirst;
        this.rrezjaKm = rrezjaKm;
        this.shpejtesiaKmh = shpejtesiaKmh;
    }

    public int getIdLlojit() {
        return idLlojit;
    }

    public void setIdLlojit(int idLlojit) {
        this.idLlojit = idLlojit;
    }

    public String getProdhuesi() {
        return prodhuesi;
    }

    public void setProdhuesi(String prodhuesi) {
        this.prodhuesi = prodhuesi;
    }

    public String getModeli() {
        return modeli;
    }

    public void setModeli(String modeli) {
        this.modeli = modeli;
    }

    public String getKodiIata() {
        return kodiIata;
    }

    public void setKodiIata(String kodiIata) {
        this.kodiIata = kodiIata;
    }

    public int getKapacitetiEkonomik() {
        return kapacitetiEkonomik;
    }

    public void setKapacitetiEkonomik(int kapacitetiEkonomik) {
        this.kapacitetiEkonomik = kapacitetiEkonomik;
    }

    public Integer getKapacitetiBiznes() {
        return kapacitetiBiznes;
    }

    public void setKapacitetiBiznes(Integer kapacitetiBiznes) {
        this.kapacitetiBiznes = kapacitetiBiznes;
    }

    public Integer getKapacitetiFirst() {
        return kapacitetiFirst;
    }

    public void setKapacitetiFirst(Integer kapacitetiFirst) {
        this.kapacitetiFirst = kapacitetiFirst;
    }

    public Integer getRrezjaKm() {
        return rrezjaKm;
    }

    public void setRrezjaKm(Integer rrezjaKm) {
        this.rrezjaKm = rrezjaKm;
    }

    public Integer getShpejtesiaKmh() {
        return shpejtesiaKmh;
    }

    public void setShpejtesiaKmh(Integer shpejtesiaKmh) {
        this.shpejtesiaKmh = shpejtesiaKmh;
    }

}