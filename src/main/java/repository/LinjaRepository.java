package repository;

import models.Linjat;
import models.mappers.IMapper;
import models.mappers.LinjaMapper;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LinjaRepository extends BaseRepository<Linjat> {

    private static final String TABLE  = "linjat";
    private static final String ID_COL = "id_linjes";

    private static final String[] INSERT_COLS = {
            "id_aeroportit_nisjes", "id_aeroportit_mbrrritjes",
            "distanca_km", "koha_fluturimit_min", "eshte_aktive"
    };

    private static final String[] UPDATE_COLS = {
            "id_aeroportit_nisjes", "id_aeroportit_mbrrritjes",
            "distanca_km", "koha_fluturimit_min", "eshte_aktive"
    };

    private final LinjaMapper mapper = new LinjaMapper();

    @Override protected String   tableName()     { return TABLE; }
    @Override protected String   idColumnName()  { return ID_COL; }
    @Override protected String[] insertColumns() { return INSERT_COLS; }
    @Override protected String[] updateColumns() { return UPDATE_COLS; }
    @Override protected IMapper<Linjat> getMapper() { return mapper; }

    @Override
    protected void setPstmCreate(PreparedStatement p, Linjat l) throws SQLException {
        p.setInt    (1, l.getIdAeroportitNisjes());
        p.setInt    (2, l.getIdAeroportitMbrrritjes());
        setNullableInt(p, 3, l.getDistancaKm());
        setNullableInt(p, 4, l.getKohaFluturimitMin());
        p.setBoolean(5, l.isEshteAktive());
    }

    @Override
    protected void setPstmUpdate(PreparedStatement p, Linjat l) throws SQLException {
        p.setInt    (1, l.getIdAeroportitNisjes());
        p.setInt    (2, l.getIdAeroportitMbrrritjes());
        setNullableInt(p, 3, l.getDistancaKm());
        setNullableInt(p, 4, l.getKohaFluturimitMin());
        p.setBoolean(5, l.isEshteAktive());
        p.setInt    (6, l.getIdLinjes()); // WHERE
    }

    @Override
    public boolean delete(Linjat l) {
        return l != null && delete(l.getIdLinjes());
    }

    // ════════════════════════════════════════════════════════════════════
    //  Queries specifike
    // ════════════════════════════════════════════════════════════════════

    /** Të gjitha linjat me JOIN për emrat e aeroporteve. */
    public List<Linjat> getAll() {
        String sql = """
            SELECT l.*,
                   a_n.kodi_iata   AS kodi_iata_nisjes,
                   a_n.emri_shqip  AS emri_nisjes,
                   a_m.kodi_iata   AS kodi_iata_mbrrritjes,
                   a_m.emri_shqip  AS emri_mbrrritjes
            FROM linjat l
            JOIN aeroportet a_n ON l.id_aeroportit_nisjes      = a_n.id_aeroportit
            JOIN aeroportet a_m ON l.id_aeroportit_mbrrritjes  = a_m.id_aeroportit
            ORDER BY a_n.kodi_iata, a_m.kodi_iata
            """;
        return execList(sql, p -> {});
    }

    /** Kërkim sipas kodit IATA të nisjes ose mbërrritjes. */
    public List<Linjat> search(String term) {
        String sql = """
            SELECT l.*,
                   a_n.kodi_iata   AS kodi_iata_nisjes,
                   a_n.emri_shqip  AS emri_nisjes,
                   a_m.kodi_iata   AS kodi_iata_mbrrritjes,
                   a_m.emri_shqip  AS emri_mbrrritjes
            FROM linjat l
            JOIN aeroportet a_n ON l.id_aeroportit_nisjes      = a_n.id_aeroportit
            JOIN aeroportet a_m ON l.id_aeroportit_mbrrritjes  = a_m.id_aeroportit
            WHERE a_n.kodi_iata LIKE ? OR a_m.kodi_iata LIKE ?
               OR a_n.emri_shqip LIKE ? OR a_m.emri_shqip LIKE ?
            ORDER BY a_n.kodi_iata, a_m.kodi_iata
            """;
        String like = "%" + term + "%";
        return execList(sql, p -> {
            p.setString(1, like); p.setString(2, like);
            p.setString(3, like); p.setString(4, like);
        });
    }

    /** Kontroll duplikat — linja e njëjtë nisje+mbërrritje. */
    public boolean existsRoute(int idNisjes, int idMbrrritjes, int excludeId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE +
                " WHERE id_aeroportit_nisjes = ? AND id_aeroportit_mbrrritjes = ?" +
                " AND id_linjes != ?";
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setInt(1, idNisjes);
            p.setInt(2, idMbrrritjes);
            p.setInt(3, excludeId);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("existsRoute failed", e);
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────
    private void setNullableInt(PreparedStatement p, int idx, Integer val) throws SQLException {
        if (val != null) p.setInt(idx, val);
        else             p.setNull(idx, Types.SMALLINT);
    }

    @FunctionalInterface
    private interface PstmSetter { void set(PreparedStatement p) throws SQLException; }

    private List<Linjat> execList(String sql, PstmSetter setter) {
        List<Linjat> list = new ArrayList<>();
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            setter.set(p);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) list.add(mapper.getFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("LinjaRepository query failed", e);
        }
        return list;
    }
}