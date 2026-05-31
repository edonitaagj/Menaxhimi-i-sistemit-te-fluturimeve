package models.dto;

public class HomeFluturimiTableDto {
    private final int idFluturimit;
    private final String fluturimi;
    private final String destinacioni;
    private final String ora;
    private final String gejti;
    private final String statusi;
    private final String data;

    public HomeFluturimiTableDto(
            int idFluturimit,
            String fluturimi,
            String destinacioni,
            String ora,
            String gejti,
            String statusi,
            String data
    ) {
        this.idFluturimit = idFluturimit;
        this.fluturimi = fluturimi;
        this.destinacioni = destinacioni;
        this.ora = ora;
        this.gejti = gejti;
        this.statusi = statusi;
        this.data = data;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public String getFluturimi() {
        return fluturimi;
    }

    public String getDestinacioni() {
        return destinacioni;
    }

    public String getOra() {
        return ora;
    }

    public String getGejti() {
        return gejti;
    }

    public String getStatusi() {
        return statusi;
    }

    public String getData() {
        return data;
    }
}
