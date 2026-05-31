package models.dto;

import java.time.LocalDateTime;

public class MirembajtjaRequestDto implements IRequestDto {
    private Integer idMirembajtjes;
    private Integer idAvionit;
    private String llojiSherbimit;
    private LocalDateTime dataFillimit;
    private LocalDateTime dataMbarimit;
    private String pershkrimiPunes;
    private Double kostoja;
    private Integer idStafitPergjegjes;
    private String statusi;

    public Integer getIdMirembajtjes() { return idMirembajtjes; }
    public void setIdMirembajtjes(Integer idMirembajtjes) { this.idMirembajtjes = idMirembajtjes; }

    public Integer getIdAvionit() { return idAvionit; }
    public void setIdAvionit(Integer idAvionit) { this.idAvionit = idAvionit; }

    public String getLlojiSherbimit() { return llojiSherbimit; }
    public void setLlojiSherbimit(String llojiSherbimit) { this.llojiSherbimit = llojiSherbimit; }

    public LocalDateTime getDataFillimit() { return dataFillimit; }
    public void setDataFillimit(LocalDateTime dataFillimit) { this.dataFillimit = dataFillimit; }

    public LocalDateTime getDataMbarimit() { return dataMbarimit; }
    public void setDataMbarimit(LocalDateTime dataMbarimit) { this.dataMbarimit = dataMbarimit; }

    public String getPershkrimiPunes() { return pershkrimiPunes; }
    public void setPershkrimiPunes(String pershkrimiPunes) { this.pershkrimiPunes = pershkrimiPunes; }

    public Double getKostoja() { return kostoja; }
    public void setKostoja(Double kostoja) { this.kostoja = kostoja; }

    public Integer getIdStafitPergjegjes() { return idStafitPergjegjes; }
    public void setIdStafitPergjegjes(Integer idStafitPergjegjes) { this.idStafitPergjegjes = idStafitPergjegjes; }

    public String getStatusi() { return statusi; }
    public void setStatusi(String statusi) { this.statusi = statusi; }
}