package models.dto;

public class AvionetRequestDto implements IRequestDto {
    private Integer idAvionit;
    private Integer idKompanise;
    private Integer idLlojit;
    private String numriRegjistrit;
    private Integer vitiProdhimit;
    private String statusi;

    public Integer getIdAvionit() { return idAvionit; }
    public void setIdAvionit(Integer idAvionit) { this.idAvionit = idAvionit; }

    public Integer getIdKompanise() { return idKompanise; }
    public void setIdKompanise(Integer idKompanise) { this.idKompanise = idKompanise; }

    public Integer getIdLlojit() { return idLlojit; }
    public void setIdLlojit(Integer idLlojit) { this.idLlojit = idLlojit; }

    public String getNumriRegjistrit() { return numriRegjistrit; }
    public void setNumriRegjistrit(String numriRegjistrit) { this.numriRegjistrit = numriRegjistrit; }

    public Integer getVitiProdhimit() { return vitiProdhimit; }
    public void setVitiProdhimit(Integer vitiProdhimit) { this.vitiProdhimit = vitiProdhimit; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }
}