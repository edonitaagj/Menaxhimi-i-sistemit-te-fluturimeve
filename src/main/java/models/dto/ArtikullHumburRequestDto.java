package models.dto;

public class ArtikullHumburRequestDto implements IRequestDto {

    private String artikulli;    // titulli / objekti i humbur
    private String vendi;
    private String dataGjetjes;  // "yyyy-MM-dd"
    private String pershkrimi;   // detaje shtesë
    private String statusi;      // i_raportuar / i_gjetur / i_kthyer / i_asgjësuar

    public String getArtikulli() {
        return artikulli;
    }

    public void setArtikulli(String artikulli) {
        this.artikulli = artikulli;
    }

    public String getVendi() {
        return vendi;
    }

    public void setVendi(String vendi) {
        this.vendi = vendi;
    }

    public String getDataGjetjes() {
        return dataGjetjes;
    }

    public void setDataGjetjes(String dataGjetjes) {
        this.dataGjetjes = dataGjetjes;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }
}