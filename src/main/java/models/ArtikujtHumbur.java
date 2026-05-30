package models;

import java.sql.Date;

public class ArtikujtHumbur {
    private int idArtikullit;
    private int idAeroportit;
    private String pershkrimi;
    private String kategoria;
    private Date dataGjetjes;
    private String vendiGjetjes;
    private String statusi;
    private Integer idPasagjeritPronar;
    private int idStafitRaportues;
    private String fotoPath;

    public ArtikujtHumbur(int idArtikullit) {
        this.idArtikullit = idArtikullit;
    }

    public ArtikujtHumbur(
            int idArtikullit,
            int idAeroportit,
            String pershkrimi,
            String kategoria,
            Date dataGjetjes,
            String vendiGjetjes,
            String statusi,
            Integer idPasagjeritPronar,
            int idStafitRaportues,
            String fotoPath
    ) {
        this.idArtikullit = idArtikullit;
        this.idAeroportit = idAeroportit;
        this.pershkrimi = pershkrimi;
        this.kategoria = kategoria;
        this.dataGjetjes = dataGjetjes;
        this.vendiGjetjes = vendiGjetjes;
        this.statusi = statusi;
        this.idPasagjeritPronar = idPasagjeritPronar;
        this.idStafitRaportues = idStafitRaportues;
        this.fotoPath = fotoPath;
    }

    public int getIdArtikullit() {
        return idArtikullit;
    }

    public void setIdArtikullit(int idArtikullit) {
        this.idArtikullit = idArtikullit;
    }

    public int getIdAeroportit() {
        return idAeroportit;
    }

    public void setIdAeroportit(int idAeroportit) {
        this.idAeroportit = idAeroportit;
    }

    public String getPershkrimi() {
        return pershkrimi;
    }

    public void setPershkrimi(String pershkrimi) {
        this.pershkrimi = pershkrimi;
    }

    public String getKategoria() {
        return kategoria;
    }

    public void setKategoria(String kategoria) {
        this.kategoria = kategoria;
    }

    public Date getDataGjetjes() {
        return dataGjetjes;
    }

    public void setDataGjetjes(Date dataGjetjes) {
        this.dataGjetjes = dataGjetjes;
    }

    public String getVendiGjetjes() {
        return vendiGjetjes;
    }

    public void setVendiGjetjes(String vendiGjetjes) {
        this.vendiGjetjes = vendiGjetjes;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

    public Integer getIdPasagjeritPronar() {
        return idPasagjeritPronar;
    }

    public void setIdPasagjeritPronar(Integer idPasagjeritPronar) {
        this.idPasagjeritPronar = idPasagjeritPronar;
    }

    public int getIdStafitRaportues() {
        return idStafitRaportues;
    }

    public void setIdStafitRaportues(int idStafitRaportues) {
        this.idStafitRaportues = idStafitRaportues;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

}