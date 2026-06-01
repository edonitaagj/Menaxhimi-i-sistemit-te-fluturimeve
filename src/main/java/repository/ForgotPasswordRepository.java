package repository;

import models.Perdoruesi;
import models.mappers.ForgotPasswordMapper;
import models.mappers.IMapper;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ForgotPasswordRepository extends BaseRepository<Perdoruesi> {

    private static final String TABLE = "perdoruesit";
    private static final String ID_COLUMN = "id_perdoruesit";
    private static final String[] INSERT_COLUMNS = {};
    private static final String[] UPDATE_COLUMNS = {};

    private final ForgotPasswordMapper mapper = new ForgotPasswordMapper();

    @Override
    protected String tableName() {
        return TABLE;
    }

    @Override
    protected String idColumnName() {
        return ID_COLUMN;
    }

    @Override
    protected String[] insertColumns() {
        return INSERT_COLUMNS;
    }

    @Override
    protected String[] updateColumns() {
        return UPDATE_COLUMNS;
    }

    @Override
    protected IMapper<Perdoruesi> getMapper() {
        return mapper;
    }

    @Override
    protected void setPstmCreate(PreparedStatement pstm, Perdoruesi obj) {
        throw new UnsupportedOperationException("ForgotPasswordRepository does not support create().");
    }

    @Override
    protected void setPstmUpdate(PreparedStatement pstm, Perdoruesi obj) {
        throw new UnsupportedOperationException("ForgotPasswordRepository does not support update().");
    }

    @Override
    public boolean delete(Perdoruesi obj) {
        throw new UnsupportedOperationException("ForgotPasswordRepository does not support delete().");
    }

    public Perdoruesi findByUsernameAndEmail(String username, String email) {
        String sql = "SELECT * FROM " + TABLE + " WHERE username = ? AND email = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, username);
            pstm.setString(2, email);

            try (ResultSet res = pstm.executeQuery()) {
                if (res.next()) {
                    return mapper.getFromResultSet(res);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user for password reset.", e);
        }

        return null;
    }

    public boolean updatePassword(int idPerdoruesit, String passwordHash) {
        String sql = "UPDATE " + TABLE + " SET password = ? WHERE " + ID_COLUMN + " = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement pstm = conn.prepareStatement(sql)) {

            pstm.setString(1, passwordHash);
            pstm.setInt(2, idPerdoruesit);
            return pstm.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update forgotten password.", e);
        }
    }
}
