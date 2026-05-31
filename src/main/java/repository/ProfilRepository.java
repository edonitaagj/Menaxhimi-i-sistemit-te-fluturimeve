package repository;

import models.Perdoruesi;
import models.mappers.IMapper;
import models.mappers.UserMapper;
import services.DatabaseService;

import java.sql.*;

/**
 * Repository vetëm për operacionet e Profil_view:
 *   - getById()          → lexo profilin (trashëguar nga BaseRepository)
 *   - updateEmriMbiemri() → UPDATE minimal, pa prekur password/roli
 *   - updatePassword()    → UPDATE minimal, vetëm kolona password
 *
 * CREATE / DELETE nuk nevojiten këtu — i bën UserRepository.
 * Nuk importohet UserRepository fare.
 */
public class ProfilRepository extends BaseRepository<Perdoruesi> {

    private static final String TABLE  = "perdoruesit";
    private static final String ID_COL = "id_perdoruesit";

    // UPDATE i profilit — vetëm emri + mbiemri
    private static final String[] UPDATE_COLS = { "emri", "mbiemri" };

    // INSERT nuk përdoret — placeholder i detyrueshëm nga BaseRepository
    private static final String[] INSERT_COLS = {};

    private final UserMapper mapper = new UserMapper();

    // ── BaseRepository abstracts ─────────────────────────────────────────
    @Override protected String    tableName()     { return TABLE; }
    @Override protected String    idColumnName()  { return ID_COL; }
    @Override protected String[]  insertColumns() { return INSERT_COLS; }
    @Override protected String[]  updateColumns() { return UPDATE_COLS; }
    @Override protected IMapper<Perdoruesi> getMapper() { return mapper; }

    /** Nuk përdoret nga Profil — hedh exception nëse thirret gabimisht. */
    @Override
    protected void setPstmCreate(PreparedStatement p, Perdoruesi obj) {
        throw new UnsupportedOperationException(
                "ProfilRepository nuk suporton create(). Përdor UserRepository.");
    }

    /** Përdoret nga BaseRepository.update() — emri + mbiemri + WHERE id. */
    @Override
    protected void setPstmUpdate(PreparedStatement p, Perdoruesi obj) throws SQLException {
        p.setString(1, obj.getEmri());
        p.setString(2, obj.getMbiemri());
        p.setInt   (3, obj.getIdPerdoruesit()); // WHERE
    }

    @Override
    public boolean delete(Perdoruesi obj) {
        throw new UnsupportedOperationException(
                "ProfilRepository nuk suporton delete(). Përdor UserRepository.");
    }

    // ════════════════════════════════════════════════════════════════════
    //  Queries specifike për Profil
    // ════════════════════════════════════════════════════════════════════

    /**
     * UPDATE vetëm emri + mbiemri.
     * Nuk prek password, roli, email, username — asnjë fushë tjetër.
     */
    public boolean updateEmriMbiemri(int idPerdoruesit, String emri, String mbiemri) {
        String sql = "UPDATE " + TABLE +
                " SET emri = ?, mbiemri = ?" +
                " WHERE " + ID_COL + " = ?";
        return execUpdate(sql, p -> {
            p.setString(1, emri);
            p.setString(2, mbiemri);
            p.setInt   (3, idPerdoruesit);
        });
    }

    public boolean updateProfil(int idPerdoruesit,
                                String emri,
                                String mbiemri,
                                String email) {

        String sql =
                "UPDATE " + TABLE +
                        " SET emri = ?, mbiemri = ?, email = ?" +
                        " WHERE " + ID_COL + " = ?";

        return execUpdate(sql, p -> {
            p.setString(1, emri);
            p.setString(2, mbiemri);
            p.setString(3, email);
            p.setInt(4, idPerdoruesit);
        });
    }

    /**
     * UPDATE vetëm password.
     * Merr hash-in e gatshëm (BCrypt) — nuk bën hash vetë.
     */
    public boolean updatePassword(int idPerdoruesit, String passwordHash) {
        String sql = "UPDATE " + TABLE +
                " SET password = ?" +
                " WHERE " + ID_COL + " = ?";
        return execUpdate(sql, p -> {
            p.setString(1, passwordHash);
            p.setInt   (2, idPerdoruesit);
        });
    }

    // ════════════════════════════════════════════════════════════════════
    //  Helper privat
    // ════════════════════════════════════════════════════════════════════
    @FunctionalInterface
    private interface PstmSetter { void set(PreparedStatement p) throws SQLException; }

    private boolean execUpdate(String sql, PstmSetter setter) {
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql)) {
            setter.set(p);
            return p.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("ProfilRepository.execUpdate failed: " + sql, e);
        }
    }
}