package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Fluturimet {

    private int idFluturimit;
    private Integer idOrarit;
    private int idKompanise;
    private int idLinjes;
    private int idAvionit;
    private String numriFluturimit;
    private Date dataFluturimit;
    private Integer idGejtitNisjes;
    private Timestamp oraNisjesPlanifikuar;
    private Timestamp oraNisjesAktuale;
    private Integer idGejtitMbrrritjes;
    private Timestamp oraMbrrritjesPlanifikuar;
    private Timestamp oraMbrrritjesAktuale;
    private String statusi;
    private String shkakuVoneses;
    private int kapacitetiTotal;
    private int vendetELira;
    private double cmimiBaze;

    // JOIN fields
    private String emriKompanise;
    private String kodiIataNisjes;
    private String kodiIataDestinacioni;
    private String kodiGejtiNisjes;

    public Fluturimet() {
    }

    public Fluturimet(int idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public Fluturimet(
            int idFluturimit,
            Integer idOrarit,
            int idKompanise,
            int idLinjes,
            int idAvionit,
            String numriFluturimit,
            Date dataFluturimit,
            Integer idGejtitNisjes,
            Timestamp oraNisjesPlanifikuar,
            Timestamp oraNisjesAktuale,
            Integer idGejtitMbrrritjes,
            Timestamp oraMbrrritjesPlanifikuar,
            Timestamp oraMbrrritjesAktuale,
            String statusi,
            String shkakuVoneses,
            int kapacitetiTotal,
            int vendetELira,
            double cmimiBaze
    ) {
        this.idFluturimit = idFluturimit;
        this.idOrarit = idOrarit;
        this.idKompanise = idKompanise;
        this.idLinjes = idLinjes;
        this.idAvionit = idAvionit;
        this.numriFluturimit = numriFluturimit;
        this.dataFluturimit = dataFluturimit;
        this.idGejtitNisjes = idGejtitNisjes;
        this.oraNisjesPlanifikuar = oraNisjesPlanifikuar;
        this.oraNisjesAktuale = oraNisjesAktuale;
        this.idGejtitMbrrritjes = idGejtitMbrrritjes;
        this.oraMbrrritjesPlanifikuar = oraMbrrritjesPlanifikuar;
        this.oraMbrrritjesAktuale = oraMbrrritjesAktuale;
        this.statusi = statusi;
        this.shkakuVoneses = shkakuVoneses;
        this.kapacitetiTotal = kapacitetiTotal;
        this.vendetELira = vendetELira;
        this.cmimiBaze = cmimiBaze;
    }

    public Fluturimet(
            int idFluturimit,
            Integer idOrarit,
            int idKompanise,
            int idLinjes,
            int idAvionit,
            String numriFluturimit,
            LocalDate dataFluturimit,
            Integer idGejtitNisjes,
            LocalDateTime oraNisjesPlanifikuar,
            LocalDateTime oraNisjesAktuale,
            Integer idGejtitMbrrritjes,
            LocalDateTime oraMbrrritjesPlanifikuar,
            LocalDateTime oraMbrrritjesAktuale,
            String statusi,
            String shkakuVoneses,
            int kapacitetiTotal,
            int vendetELira
    ) {
        this.idFluturimit = idFluturimit;
        this.idOrarit = idOrarit;
        this.idKompanise = idKompanise;
        this.idLinjes = idLinjes;
        this.idAvionit = idAvionit;
        this.numriFluturimit = numriFluturimit;

        this.dataFluturimit =
                dataFluturimit != null ? Date.valueOf(dataFluturimit) : null;

        this.idGejtitNisjes = idGejtitNisjes;

        this.oraNisjesPlanifikuar =
                oraNisjesPlanifikuar != null
                        ? Timestamp.valueOf(oraNisjesPlanifikuar)
                        : null;

        this.oraNisjesAktuale =
                oraNisjesAktuale != null
                        ? Timestamp.valueOf(oraNisjesAktuale)
                        : null;

        this.idGejtitMbrrritjes = idGejtitMbrrritjes;

        this.oraMbrrritjesPlanifikuar =
                oraMbrrritjesPlanifikuar != null
                        ? Timestamp.valueOf(oraMbrrritjesPlanifikuar)
                        : null;

        this.oraMbrrritjesAktuale =
                oraMbrrritjesAktuale != null
                        ? Timestamp.valueOf(oraMbrrritjesAktuale)
                        : null;

        this.statusi = statusi;
        this.shkakuVoneses = shkakuVoneses;
        this.kapacitetiTotal = kapacitetiTotal;
        this.vendetELira = vendetELira;
    }

    public int getIdFluturimit() {
        return idFluturimit;
    }

    public void setIdFluturimit(int idFluturimit) {
        this.idFluturimit = idFluturimit;
    }

    public Integer getIdOrarit() {
        return idOrarit;
    }

    public void setIdOrarit(Integer idOrarit) {
        this.idOrarit = idOrarit;
    }

    public int getIdKompanise() {
        return idKompanise;
    }

    public void setIdKompanise(int idKompanise) {
        this.idKompanise = idKompanise;
    }

    public int getIdLinjes() {
        return idLinjes;
    }

    public void setIdLinjes(int idLinjes) {
        this.idLinjes = idLinjes;
    }

    public int getIdAvionit() {
        return idAvionit;
    }

    public void setIdAvionit(int idAvionit) {
        this.idAvionit = idAvionit;
    }

    public String getNumriFluturimit() {
        return numriFluturimit;
    }

    public void setNumriFluturimit(String numriFluturimit) {
        this.numriFluturimit = numriFluturimit;
    }

    public Date getDataFluturimit() {
        return dataFluturimit;
    }

    public void setDataFluturimit(Date dataFluturimit) {
        this.dataFluturimit = dataFluturimit;
    }

    public Integer getIdGejtitNisjes() {
        return idGejtitNisjes;
    }

    public void setIdGejtitNisjes(Integer idGejtitNisjes) {
        this.idGejtitNisjes = idGejtitNisjes;
    }

    public Timestamp getOraNisjesPlanifikuar() {
        return oraNisjesPlanifikuar;
    }

    public void setOraNisjesPlanifikuar(Timestamp oraNisjesPlanifikuar) {
        this.oraNisjesPlanifikuar = oraNisjesPlanifikuar;
    }

    public Timestamp getOraNisjesAktuale() {
        return oraNisjesAktuale;
    }

    public void setOraNisjesAktuale(Timestamp oraNisjesAktuale) {
        this.oraNisjesAktuale = oraNisjesAktuale;
    }

    public Integer getIdGejtitMbrrritjes() {
        return idGejtitMbrrritjes;
    }

    public void setIdGejtitMbrrritjes(Integer idGejtitMbrrritjes) {
        this.idGejtitMbrrritjes = idGejtitMbrrritjes;
    }

    public Timestamp getOraMbrrritjesPlanifikuar() {
        return oraMbrrritjesPlanifikuar;
    }

    public void setOraMbrrritjesPlanifikuar(Timestamp oraMbrrritjesPlanifikuar) {
        this.oraMbrrritjesPlanifikuar = oraMbrrritjesPlanifikuar;
    }

    public Timestamp getOraMbrrritjesAktuale() {
        return oraMbrrritjesAktuale;
    }

    public void setOraMbrrritjesAktuale(Timestamp oraMbrrritjesAktuale) {
        this.oraMbrrritjesAktuale = oraMbrrritjesAktuale;
    }

    public String getStatusi() {
        return statusi;
    }

    public void setStatusi(String statusi) {
        this.statusi = statusi;
    }

    public String getShkakuVoneses() {
        return shkakuVoneses;
    }

    public void setShkakuVoneses(String shkakuVoneses) {
        this.shkakuVoneses = shkakuVoneses;
    }

    public int getKapacitetiTotal() {
        return kapacitetiTotal;
    }

    public void setKapacitetiTotal(int kapacitetiTotal) {
        this.kapacitetiTotal = kapacitetiTotal;
    }

    public int getVendetELira() {
        return vendetELira;
    }

    public void setVendetELira(int vendetELira) {
        this.vendetELira = vendetELira;
    }

    public double getCmimiBaze() {
        return cmimiBaze;
    }

    public void setCmimiBaze(double cmimiBaze) {
        this.cmimiBaze = cmimiBaze;
    }

    public String getEmriKompanise() {
        return emriKompanise;
    }

    public void setEmriKompanise(String emriKompanise) {
        this.emriKompanise = emriKompanise;
    }

    public String getKodiIataNisjes() {
        return kodiIataNisjes;
    }

    public void setKodiIataNisjes(String kodiIataNisjes) {
        this.kodiIataNisjes = kodiIataNisjes;
    }

    public String getKodiIataDestinacioni() {
        return kodiIataDestinacioni;
    }

    public void setKodiIataDestinacioni(String kodiIataDestinacioni) {
        this.kodiIataDestinacioni = kodiIataDestinacioni;
    }

    public String getKodiGejtiNisjes() {
        return kodiGejtiNisjes;
    }

    public void setKodiGejtiNisjes(String kodiGejtiNisjes) {
        this.kodiGejtiNisjes = kodiGejtiNisjes;
    }
}