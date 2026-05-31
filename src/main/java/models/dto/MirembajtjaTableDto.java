package models.dto;

public class MirembajtjaTableDto {
    private int idMirembajtjes;
    private int idAvionit;
    private String llojiSherbimit;
    private String dataFillimit;
    private String dataMbarimit;
    private String pershkrimiPunes;
    private double kostoja;
    private int idStafitPergjegjes;
    private String statusi;
    private String emriStafit;

    public MirembajtjaTableDto() {}

    public MirembajtjaTableDto(int idMirembajtjes, int idAvionit, String llojiSherbimit,
                               String dataFillimit, String dataMbarimit, String pershkrimiPunes,
                               double kostoja, int idStafitPergjegjes, String statusi, String emriStafit) {
        this.idMirembajtjes = idMirembajtjes;
        this.idAvionit = idAvionit;
        this.llojiSherbimit = llojiSherbimit;
        this.dataFillimit = dataFillimit;
        this.dataMbarimit = dataMbarimit;
        this.pershkrimiPunes = pershkrimiPunes;
        this.kostoja = kostoja;
        this.idStafitPergjegjes = idStafitPergjegjes;
        this.statusi = statusi;
        this.emriStafit = emriStafit;
    }

    public int getIdMirembajtjes() { return idMirembajtjes; }
    public void setIdMirembajtjes(int idMirembajtjes) { this.idMirembajtjes = idMirembajtjes; }

    public int getIdAvionit() { return idAvionit; }
    public void setIdAvionit(int idAvionit) { this.idAvionit = idAvionit; }

    public String getLlojiSherbimit() { return llojiSherbimit; }
    public void setLlojiSherbimit(String llojiSherbimit) { this.llojiSherbimit = llojiSherbimit; }

    public String getDataFillimit() { return dataFillimit; }
    public void setDataFillimit(String dataFillimit) { this.dataFillimit = dataFillimit; }

    public String getDataMbarimit() { return dataMbarimit; }
    public void setDataMbarimit(String dataMbarimit) { this.dataMbarimit = dataMbarimit; }

    public String getPershkrimiPunes() { return pershkrimiPunes; }
    public void setPershkrimiPunes(String pershkrimiPunes) { this.pershkrimiPunes = pershkrimiPunes; }

    public double getKostoja() { return kostoja; }
    public void setKostoja(double kostoja) { this.kostoja = kostoja; }

    public int getIdStafitPergjegjes() { return idStafitPergjegjes; }
    public void setIdStafitPergjegjes(int idStafitPergjegjes) { this.idStafitPergjegjes = idStafitPergjegjes; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }

    public String getEmriStafit() { return emriStafit; }
    public void setEmriStafit(String emriStafit) { this.emriStafit = emriStafit; }
}