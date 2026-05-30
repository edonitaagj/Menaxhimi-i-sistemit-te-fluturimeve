package models;

public class KompaniteAjrore {
    private int idKompanise;
    private int idVendit;
    private String kodiIata;
    private String kodiIcao;
    private String emri;
    private String emriIShkurter;
    private String faqjaWeb;
    private String telefoni;
    private Boolean eshteAktive;

    public KompaniteAjrore(int idKompanise) {
        this.idKompanise = idKompanise;
    }

    public KompaniteAjrore(
            int idKompanise,
            int idVendit,
            String kodiIata,
            String kodiIcao,
            String emri,
            String emriIShkurter,
            String faqjaWeb,
            String telefoni,
            Boolean eshteAktive
    ) {
        this.idKompanise = idKompanise;
        this.idVendit = idVendit;
        this.kodiIata = kodiIata;
        this.kodiIcao = kodiIcao;
        this.emri = emri;
        this.emriIShkurter = emriIShkurter;
        this.faqjaWeb = faqjaWeb;
        this.telefoni = telefoni;
        this.eshteAktive = eshteAktive;
    }

    public int getIdKompanise() {
        return idKompanise;
    }

    public void setIdKompanise(int idKompanise) {
        this.idKompanise = idKompanise;
    }

    public int getIdVendit() {
        return idVendit;
    }

    public void setIdVendit(int idVendit) {
        this.idVendit = idVendit;
    }

    public String getKodiIata() {
        return kodiIata;
    }

    public void setKodiIata(String kodiIata) {
        this.kodiIata = kodiIata;
    }

    public String getKodiIcao() {
        return kodiIcao;
    }

    public void setKodiIcao(String kodiIcao) {
        this.kodiIcao = kodiIcao;
    }

    public String getEmri() {
        return emri;
    }

    public void setEmri(String emri) {
        this.emri = emri;
    }

    public String getEmriIShkurter() {
        return emriIShkurter;
    }

    public void setEmriIShkurter(String emriIShkurter) {
        this.emriIShkurter = emriIShkurter;
    }

    public String getFaqjaWeb() {
        return faqjaWeb;
    }

    public void setFaqjaWeb(String faqjaWeb) {
        this.faqjaWeb = faqjaWeb;
    }

    public String getTelefoni() {
        return telefoni;
    }

    public void setTelefoni(String telefoni) {
        this.telefoni = telefoni;
    }

    public Boolean getEshteAktive() {
        return eshteAktive;
    }

    public void setEshteAktive(Boolean eshteAktive) {
        this.eshteAktive = eshteAktive;
    }

}