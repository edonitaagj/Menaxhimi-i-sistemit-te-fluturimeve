package models.dto;

public class AvionetTableDto {
    private int idAvionit;
    private int idKompanise;
    private int idLlojit;
    private String numriRegjistrit;
    private String prodhuesi;
    private String modeli;
    private String vitiProdhimit;
    private String statusi;
    private String emriKompanise;

    public AvionetTableDto() {}

    public AvionetTableDto(int idAvionit, int idKompanise, int idLlojit, String numriRegjistrit,
                           String prodhuesi, String modeli, String vitiProdhimit,
                           String statusi, String emriKompanise) {
        this.idAvionit = idAvionit;
        this.idKompanise = idKompanise;
        this.idLlojit = idLlojit;
        this.numriRegjistrit = numriRegjistrit;
        this.prodhuesi = prodhuesi;
        this.modeli = modeli;
        this.vitiProdhimit = vitiProdhimit;
        this.statusi = statusi;
        this.emriKompanise = emriKompanise;
    }

    public int getIdAvionit() { return idAvionit; }
    public void setIdAvionit(int idAvionit) { this.idAvionit = idAvionit; }

    public int getIdKompanise() { return idKompanise; }
    public void setIdKompanise(int idKompanise) { this.idKompanise = idKompanise; }

    public int getIdLlojit() { return idLlojit; }
    public void setIdLlojit(int idLlojit) { this.idLlojit = idLlojit; }

    public String getNumriRegjistrit() { return numriRegjistrit; }
    public void setNumriRegjistrit(String numriRegjistrit) { this.numriRegjistrit = numriRegjistrit; }

    public String getProdhuesi() { return prodhuesi; }
    public void setProdhuesi(String prodhuesi) { this.prodhuesi = prodhuesi; }

    public String getModeli() { return modeli; }
    public void setModeli(String modeli) { this.modeli = modeli; }

    public String getVitiProdhimit() { return vitiProdhimit; }
    public void setVitiProdhimit(String vitiProdhimit) { this.vitiProdhimit = vitiProdhimit; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }

    public String getEmriKompanise() { return emriKompanise; }
    public void setEmriKompanise(String emriKompanise) { this.emriKompanise = emriKompanise; }

    @Override
    public String toString() {
        return numriRegjistrit + " - " + prodhuesi + " " + modeli;
    }
}