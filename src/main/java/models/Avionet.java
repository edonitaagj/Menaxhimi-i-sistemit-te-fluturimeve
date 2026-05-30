package models;

public class Avionet {
    private int idAvionit;
    private int idKompanise;
    private int idLlojit;
    private String numriRegjistrit;
    private String vitiProdhimit;
    private String statusi;

    public Avionet(int idAvionit) {
        this.idAvionit = idAvionit;
    }

    public Avionet(
            int idAvionit,
            int idKompanise,
            int idLlojit,
            String numriRegjistrit,
            String vitiProdhimit,
            String statusi
    ) {
        this.idAvionit = idAvionit;
        this.idKompanise = idKompanise;
        this.idLlojit = idLlojit;
        this.numriRegjistrit = numriRegjistrit;
        this.vitiProdhimit = vitiProdhimit;
        this.statusi = statusi;
    }

    public int getIdAvionit() {
        return idAvionit;
    }

    public void setIdAvionit(int idAvionit) {
        this.idAvionit = idAvionit;
    }

    public int getIdKompanise() {
        return idKompanise;
    }

    public void setIdKompanise(int idKompanise) {
        this.idKompanise = idKompanise;
    }

    public int getIdLlojit() {
        return idLlojit;
    }

    public void setIdLlojit(int idLlojit) {
        this.idLlojit = idLlojit;
    }

    public String getNumriRegjistrit() {
        return numriRegjistrit;
    }

    public void setNumriRegjistrit(String numriRegjistrit) {
        this.numriRegjistrit = numriRegjistrit;
    }

    public String getVitiProdhimit() {
        return vitiProdhimit;
    }

    public void setVitiProdhimit(String vitiProdhimit) {
        this.vitiProdhimit = vitiProdhimit;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

}