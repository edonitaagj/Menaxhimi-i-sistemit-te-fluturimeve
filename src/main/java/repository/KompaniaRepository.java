package repository;

import models.KompaniteAjrore;
import models.mappers.IMapper;
import models.mappers.KompaniaMapper;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KompaniaRepository extends BaseRepository<KompaniteAjrore> {

    private static final String TABLE  = "kompanite_ajrore";
    private static final String ID_COL = "id_kompanise";

    private static final String[] INSERT_COLS = {
            "id_vendit", "kodi_iata", "kodi_icao", "emri",
            "emri_i_shkurter", "faqja_web", "telefoni", "eshte_aktive"
    };

    private static final String[] UPDATE_COLS = {
            "id_vendit", "kodi_iata", "kodi_icao", "emri",
            "emri_i_shkurter", "faqja_web", "telefoni", "eshte_aktive"
    };

    private final KompaniaMapper mapper = new KompaniaMapper();

    @Override protected String   tableName()     { return TABLE; }
    @Override protected String   idColumnName()  { return ID_COL; }
    @Override protected String[] insertColumns() { return INSERT_COLS; }
    @Override protected String[] updateColumns() { return UPDATE_COLS; }
    @Override protected IMapper<KompaniteAjrore> getMapper() { return mapper; }

    @Override
    protected void setPstmCreate(PreparedStatement p, KompaniteAjrore k) throws SQLException {
        p.setInt    (1, k.getIdVendit());
        p.setString (2, k.getKodiIata());
        p.setString (3, k.getKodiIcao());
        p.setString (4, k.getEmri());
        p.setString (5, k.getEmriIShkurter());
        p.setString (6, k.getFaqjaWeb());
        p.setString (7, k.getTelefoni());
        p.setBoolean(8, k.getEshteAktive());
    }

    @Override
    protected void setPstmUpdate(PreparedStatement p, KompaniteAjrore k) throws SQLException {
        p.setInt    (1, k.getIdVendit());
        p.setString (2, k.getKodiIata());
        p.setString (3, k.getKodiIcao());
        p.setString (4, k.getEmri());
        p.setString (5, k.getEmriIShkurter());
        p.setString (6, k.getFaqjaWeb());
        p.setString (7, k.getTelefoni());
        p.setBoolean(8, k.getEshteAktive());
        p.setInt    (9, k.getIdKompanise()); // WHERE
    }

    @Override
    public boolean delete(KompaniteAjrore k) {
        return k != null && delete(k.getIdKompanise());
    }

    // ════════════════════════════════════════════════════════════════════
    //  Queries specifike
    // ════════════════════════════════════════════════════════════════════

    /** Të gjitha kompanitë me JOIN për emrin e shtetit. */
    public List<KompaniteAjrore> getAll() {
        String sql = """
            SELECT k.*, v.emri_shqip AS emri_shteti
            FROM kompanite_ajrore k
            JOIN vendet v ON k.id_vendit = v.id_vendi
            ORDER BY k.emri ASC
            """;
        return execList(sql, p -> {});
    }

    /** Kërkim live sipas emrit ose kodit IATA. */
    public List<KompaniteAjrore> search(String term) {
        String sql = """
            SELECT k.*, v.emri_shqip AS emri_shteti
            FROM kompanite_ajrore k
            JOIN vendet v ON k.id_vendit = v.id_vendi
            WHERE k.emri LIKE ? OR k.kodi_iata LIKE ? OR k.kodi_icao LIKE ?
            ORDER BY k.emri ASC
            """;
        String like = "%" + term + "%";
        return execList(sql, p -> {
            p.setString(1, like);
            p.setString(2, like);
            p.setString(3, like);
        });
    }

    /** Kontroll duplikat IATA — nëse ekziston ID tjetër me të njëjtin kod. */
    public boolean existsIata(String kodiIata, int excludeId) {
        String sql = "SELECT COUNT(*) FROM " + TABLE +
                " WHERE kodi_iata = ? AND id_kompanise != ?";
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            p.setString(1, kodiIata);
            p.setInt   (2, excludeId);
            try (ResultSet rs = p.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("existsIata failed", e);
        }
    }

    // ── Helper ────────────────────────────────────────────────────────────
    @FunctionalInterface
    private interface PstmSetter { void set(PreparedStatement p) throws SQLException; }

    private List<KompaniteAjrore> execList(String sql, PstmSetter setter) {
        List<KompaniteAjrore> list = new ArrayList<>();
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            setter.set(p);
            try (ResultSet rs = p.executeQuery()) {
                while (rs.next()) list.add(mapper.getFromResultSet(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("KompaniaRepository query failed", e);
        }
        return list;
    }
}