package repository;

import models.Fluturimet;
import models.mappers.AdminFluturimiMapper;
import models.mappers.IMapper;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminFluturimiRepository extends BaseRepository<Fluturimet> {

    private static final String TABLE  = "fluturimet";
    private static final String ID_COL = "id_fluturimit";

    private static final String[] INSERT_COLS = {
            "id_kompanise", "id_linjes", "id_avionit", "numri_fluturimit",
            "data_fluturimit", "ora_nisjes_planifikuar", "ora_mbrrritjes_planifikuar",
            "statusi", "kapaciteti_total", "vendet_e_lira"
    };

    private static final String[] UPDATE_COLS = {
            "statusi", "ora_nisjes_aktuale", "ora_mbrrritjes_aktuale",
            "shkaku_voneses", "vendet_e_lira", "id_gejtit_nisjes"
    };

    private final AdminFluturimiMapper mapper = new AdminFluturimiMapper();

    @Override protected String   tableName()     { return TABLE; }
    @Override protected String   idColumnName()  { return ID_COL; }
    @Override protected String[] insertColumns() { return INSERT_COLS; }
    @Override protected String[] updateColumns() { return UPDATE_COLS; }
    @Override protected IMapper<Fluturimet> getMapper() { return mapper; }

    @Override
    protected void setPstmCreate(PreparedStatement p, Fluturimet f) throws SQLException {
        p.setInt      (1,  f.getIdKompanise());
        p.setInt      (2,  f.getIdLinjes());
        p.setInt      (3,  f.getIdAvionit());
        p.setString   (4,  f.getNumriFluturimit());
        p.setDate     (5,  Date.valueOf(f.getDataFluturimit().toLocalDate()));
        p.setTimestamp(6,  Timestamp.valueOf(f.getOraNisjesPlanifikuar().toLocalDateTime()));
        p.setTimestamp(7,  Timestamp.valueOf(f.getOraMbrrritjesPlanifikuar().toLocalDateTime()));
        p.setString   (8,  f.getStatusi());
        p.setInt      (9,  f.getKapacitetiTotal());
        p.setInt      (10, f.getVendetELira());
    }

    @Override
    protected void setPstmUpdate(PreparedStatement p, Fluturimet f) throws SQLException {
        p.setString   (1, f.getStatusi());
        p.setTimestamp(2, f.getOraNisjesAktuale()    != null ? Timestamp.valueOf(f.getOraNisjesAktuale().toLocalDateTime())    : null);
        p.setTimestamp(3, f.getOraMbrrritjesAktuale() != null ? Timestamp.valueOf(f.getOraMbrrritjesAktuale().toLocalDateTime()) : null);
        p.setString   (4, f.getShkakuVoneses());
        p.setInt      (5, f.getVendetELira());
        if (f.getIdGejtitNisjes() != null) p.setInt(6, f.getIdGejtitNisjes());
        else                               p.setNull(6, Types.INTEGER);
        p.setInt      (7, f.getIdFluturimit()); // WHERE
    }

    @Override
    public boolean delete(Fluturimet f) {
        return f != null && delete(f.getIdFluturimit());
    }

    // ════════════════════════════════════════════════════════════════════
    //  Queries specifike për admin panel
    // ════════════════════════════════════════════════════════════════════

    /**
     * Të gjitha fluturimet me JOIN të plotë:
     * kompania, aeroporti i nisjes/mbërrritjes, gejti.
     */
    public List<Fluturimet> getAll() {
        String sql = """
            SELECT f.*,
                   k.emri_i_shkurter             AS emri_kompanise,
                   a_n.kodi_iata                 AS kodi_iata_nisjes,
                   a_d.kodi_iata                 AS kodi_iata_dest,
                   g.kodi_gejtit                 AS kodi_gejti_nisjes
            FROM fluturimet f
            JOIN kompanite_ajrore k  ON f.id_kompanise        = k.id_kompanise
            JOIN linjat l            ON f.id_linjes            = l.id_linjes
            JOIN aeroportet a_n      ON l.id_aeroportit_nisjes     = a_n.id_aeroportit
            JOIN aeroportet a_d      ON l.id_aeroportit_mbrrritjes = a_d.id_aeroportit
            LEFT JOIN gejtat g       ON f.id_gejtit_nisjes     = g.id_gejtit
            ORDER BY f.ora_nisjes_planifikuar DESC
            """;
        return execList(sql, p -> {});
    }

    /**
     * Kërkim live — filtron sipas kodit, kompanisë, origjinës ose destinacionit.
     */
    public List<Fluturimet> search(String term) {
        String sql = """
            SELECT f.*,
                   k.emri_i_shkurter             AS emri_kompanise,
                   a_n.kodi_iata                 AS kodi_iata_nisjes,
                   a_d.kodi_iata                 AS kodi_iata_dest,
                   g.kodi_gejtit                 AS kodi_gejti_nisjes
            FROM fluturimet f
            JOIN kompanite_ajrore k  ON f.id_kompanise        = k.id_kompanise
            JOIN linjat l            ON f.id_linjes            = l.id_linjes
            JOIN aeroportet a_n      ON l.id_aeroportit_nisjes     = a_n.id_aeroportit
            JOIN aeroportet a_d      ON l.id_aeroportit_mbrrritjes = a_d.id_aeroportit
            LEFT JOIN gejtat g       ON f.id_gejtit_nisjes     = g.id_gejtit
            WHERE f.numri_fluturimit LIKE ?
               OR k.emri_i_shkurter  LIKE ?
               OR a_n.kodi_iata      LIKE ?
               OR a_d.kodi_iata      LIKE ?
            ORDER BY f.ora_nisjes_planifikuar DESC
            """;
        String like = "%" + term + "%";
        return execList(sql, p -> {
            p.setString(1, like); p.setString(2, like);
            p.setString(3, like); p.setString(4, like);
        });
    }

    /**
     * Kthe id_linjes nga kodi IATA i nisjes dhe destinacionit.
     * Nevojitet para INSERT kur forma jep IATA, jo id.
     */
    public Integer findLinjaId(String iataFrom, String iataDest) {
        String sql = """
            SELECT l.id_linjes
            FROM linjat l
            JOIN aeroportet a_n ON l.id_aeroportit_nisjes     = a_n.id_aeroportit
            JOIN aeroportet a_d ON l.id_aeroportit_mbrrritjes = a_d.id_aeroportit
            WHERE a_n.kodi_iata = ? AND a_d.kodi_iata = ?
            LIMIT 1
            """;
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, iataFrom);
            p.setString(2, iataDest);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() ? rs.getInt("id_linjes") : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("findLinjaId failed", e);
        }
    }

    /**
     * Kthe avionin e parë aktiv të kompanisë — default kur nuk zgjidhet avioni.
     */
    public Integer findDefaultAvioni(int idKompanise) {
        String sql = """
            SELECT id_avionit FROM avionet
            WHERE id_kompanise = ? AND statusi = 'aktiv'
            LIMIT 1
            """;
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idKompanise);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() ? rs.getInt("id_avionit") : null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("findDefaultAvioni failed", e);
        }
    }

    /**
     * UPDATE vetëm statusin — nga butoni i veprimeve në tabelë.
     */
    public boolean updateStatusi(int idFluturimit, String statusiRi) {
        String sql = "UPDATE " + TABLE + " SET statusi = ? WHERE " + ID_COL + " = ?";
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, statusiRi);
            p.setInt   (2, idFluturimit);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("updateStatusi failed", e);
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────
    @FunctionalInterface
    private interface PstmSetter { void set(PreparedStatement p) throws SQLException; }

    private List<Fluturimet> execList(String sql, PstmSetter setter) {
        List<Fluturimet> list = new ArrayList<>();
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            setter.set(p);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) list.add(mapper.getFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("AdminFluturimiRepository query failed", e);
        }
        return list;
    }
}