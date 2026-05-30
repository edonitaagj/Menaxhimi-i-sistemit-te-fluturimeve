package models;

import java.sql.Timestamp;

public class MirembajtjaAvioneve {
    private int idMirembajtjes;
    private int idAvionit;
    private String llojiSherbimit;
    private Timestamp dataFillimit;
    private Timestamp dataMbarimit;
    private String pershkrimiPunes;
    private Double kostoja;
    private int idStafitPergjegjes;
    private String statusi;

    public MirembajtjaAvioneve(int idMirembajtjes) {
        this.idMirembajtjes = idMirembajtjes;
    }

    public MirembajtjaAvioneve(
            int idMirembajtjes,
            int idAvionit,
            String llojiSherbimit,
            Timestamp dataFillimit,
            Timestamp dataMbarimit,
            String pershkrimiPunes,
            Double kostoja,
            int idStafitPergjegjes,
            String statusi
    ) {
        this.idMirembajtjes = idMirembajtjes;
        this.idAvionit = idAvionit;
        this.llojiSherbimit = llojiSherbimit;
        this.dataFillimit = dataFillimit;
        this.dataMbarimit = dataMbarimit;
        this.pershkrimiPunes = pershkrimiPunes;
        this.kostoja = kostoja;
        this.idStafitPergjegjes = idStafitPergjegjes;
        this.statusi = statusi;
    }

    public int getIdMirembajtjes() {
        return idMirembajtjes;
    }

    public void setIdMirembajtjes(int idMirembajtjes) {
        this.idMirembajtjes = idMirembajtjes;
    }

    public int getIdAvionit() {
        return idAvionit;
    }

    public void setIdAvionit(int idAvionit) {
        this.idAvionit = idAvionit;
    }

    public String getLlojiSherbimit() {
        return llojiSherbimit;
    }

    public void setLlojiSherbimit(String llojiSherbimit) {
        this.llojiSherbimit = llojiSherbimit;
    }

    public Timestamp getDataFillimit() {
        return dataFillimit;
    }

    public void setDataFillimit(Timestamp dataFillimit) {
        this.dataFillimit = dataFillimit;
    }

    public Timestamp getDataMbarimit() {
        return dataMbarimit;
    }

    public void setDataMbarimit(Timestamp dataMbarimit) {
        this.dataMbarimit = dataMbarimit;
    }

    public String getPershkrimiPunes() {
        return pershkrimiPunes;
    }

    public void setPershkrimiPunes(String pershkrimiPunes) {
        this.pershkrimiPunes = pershkrimiPunes;
    }

    public Double getKostoja() {
        return kostoja;
    }

    public void setKostoja(Double kostoja) {
        this.kostoja = kostoja;
    }

    public int getIdStafitPergjegjes() {
        return idStafitPergjegjes;
    }

    public void setIdStafitPergjegjes(int idStafitPergjegjes) {
        this.idStafitPergjegjes = idStafitPergjegjes;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

}