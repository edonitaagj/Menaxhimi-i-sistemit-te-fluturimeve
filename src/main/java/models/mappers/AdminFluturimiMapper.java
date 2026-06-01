package models.mappers;

import models.Fluturimet;
import models.dto.AdminFluturimiFormDto;
import models.dto.IRequestDto;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Mapper për AdminFluturimet_view.
 *
 * getFromResultSet() — map i plotë nga query me JOIN.
 * fromDto()          — krijon Fluturimi të ri nga forma admin.
 * fromDto(obj, dto)  — aplikon ndryshimet mbi objektin ekzistues (UPDATE statusi/gate).
 *
 * SHËNIM: FluturimiMapper ekzistues map-on vetëm fluturime publike.
 * Ky mapper e zgjeron për nevojat e panelit admin (ka logje shtesë
 * dhe fillon fluturimin me defaults të sigurta).
 */
public class AdminFluturimiMapper implements IMapper<Fluturimet> {

    @Override
    public Fluturimet getFromResultSet(ResultSet rs) {
        try {
            Fluturimet f = new Fluturimet(
                    rs.getInt("id_fluturimit"),
                    rs.getObject("id_orarit") != null ? rs.getInt("id_orarit") : null,
                    rs.getInt("id_kompanise"),
                    rs.getInt("id_linjes"),
                    rs.getInt("id_avionit"),
                    rs.getString("numri_fluturimit"),
                    rs.getDate("data_fluturimit").toLocalDate(),
                    rs.getObject("id_gejtit_nisjes") != null ? rs.getInt("id_gejtit_nisjes") : null,
                    rs.getTimestamp("ora_nisjes_planifikuar").toLocalDateTime(),
                    rs.getTimestamp("ora_nisjes_aktuale") != null
                            ? rs.getTimestamp("ora_nisjes_aktuale").toLocalDateTime() : null,
                    rs.getObject("id_gejtit_mbrrritjes") != null ? rs.getInt("id_gejtit_mbrrritjes") : null,
                    rs.getTimestamp("ora_mbrrritjes_planifikuar").toLocalDateTime(),
                    rs.getTimestamp("ora_mbrrritjes_aktuale") != null
                            ? rs.getTimestamp("ora_mbrrritjes_aktuale").toLocalDateTime() : null,
                    rs.getString("statusi"),
                    rs.getString("shkaku_voneses"),
                    rs.getInt("kapaciteti_total"),
                    rs.getInt("vendet_e_lira")
            );

            // Joined fields — silent skip nëse mungojnë
            safeSet(() -> f.setKodiIataNisjes(rs.getString("kodi_iata_nisjes")));
            safeSet(() -> f.setKodiIataDestinacioni(rs.getString("kodi_iata_dest")));
            safeSet(() -> f.setEmriKompanise(rs.getString("emri_kompanise")));
            safeSet(() -> f.setKodiGejtiNisjes(rs.getString("kodi_gejti_nisjes")));

            return f;
        } catch (SQLException e) {
            throw new RuntimeException("AdminFluturimiMapper.getFromResultSet: gabim", e);
        }
    }

    /**
     * Krijon Fluturimi të ri nga forma admin.
     * Fushat që mungojnë nga forma (id_avionit, kapaciteti, ora) vendosen
     * si defaults të sigurta — do të mund të ndryshohen me UPDATE më vonë.
     *
     * KUJDES: id_linjes dhe id_avionit DUHET të jenë gjetur nga service
     * para se të thirret ky fromDto. Nëse nuk gjenden, service kthen error
     * pa arritur kurrë këtë metodë.
     */
    @Override
    public Fluturimet fromDto(IRequestDto dto) {
        if (!(dto instanceof AdminFluturimiFormDto f))
            throw new IllegalArgumentException("AdminFluturimiMapper: tip i papritur");

        // Defaults për fushat që nuk janë në formë
        LocalDate sot      = LocalDate.now();
        LocalDateTime nisja    = LocalDateTime.of(sot, LocalTime.of(12, 0));
        LocalDateTime mbrrritja = nisja.plusHours(2);

        Fluturimet fl = new Fluturimet();
        fl.setNumriFluturimit(f.getKodi());
        fl.setIdKompanise(f.getIdKompanise());
        fl.setStatusi(f.getStatusi());
        fl.setDataFluturimit(Date.valueOf(sot));
        fl.setOraNisjesPlanifikuar(Timestamp.valueOf(nisja));
        fl.setOraMbrrritjesPlanifikuar(Timestamp.valueOf(mbrrritja));
        fl.setKapacitetiTotal(0);   // do të plotësohet kur të zgjidhet avioni
        fl.setVendetELira(0);
        return fl;
    }

    /**
     * Aplikon ndryshimet mbi fluturimin ekzistues — UPDATE statusi + gate.
     */
    @Override
    public Fluturimet fromDto(Fluturimet obj, IRequestDto dto) {
        if (!(dto instanceof AdminFluturimiFormDto f))
            throw new IllegalArgumentException("AdminFluturimiMapper: tip i papritur");

        obj.setNumriFluturimit(f.getKodi());
        obj.setIdKompanise(f.getIdKompanise());
        obj.setStatusi(f.getStatusi());
        return obj;
    }

    // ── Helper ────────────────────────────────────────────────────────────
    @FunctionalInterface
    private interface SqlRunnable { void run() throws SQLException; }

    private void safeSet(SqlRunnable r) {
        try { r.run(); } catch (SQLException ignored) {}
    }
}