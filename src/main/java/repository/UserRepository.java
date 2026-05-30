package repository;

import models.Perdoruesi;
import models.mappers.IMapper;
import models.mappers.UserMapper;
import services.DatabaseService;
import services.HashService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository extends BaseRepository<Perdoruesi> {

    private static final String TABLE = "perdoruesit";
    private static final String ID_COLUMN = "id_perdoruesit";

    private static final String[] INSERT_COLUMNS = {
            "emri", "mbiemri", "email", "password", "roli", "username"
    };

    private static final String[] UPDATE_COLUMNS = {
            "emri", "mbiemri", "email", "password", "roli", "username"
    };

    private final UserMapper mapper = new UserMapper();

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
    protected void setPstmCreate(PreparedStatement pstm, Perdoruesi obj) throws SQLException {
        pstm.setString(1, obj.getEmri());
        pstm.setString(2, obj.getMbiemri());
        pstm.setString(3, obj.getEmail());
        pstm.setString(4, obj.getPasswordHash() != null ? obj.getPasswordHash() : HashService.generateHash(""));
        pstm.setString(5, obj.getRoli());
        pstm.setString(6, obj.getUsername());
    }

    @Override
    protected void setPstmUpdate(PreparedStatement pstm, Perdoruesi obj) throws SQLException {
        pstm.setString(1, obj.getEmri());
        pstm.setString(2, obj.getMbiemri());
        pstm.setString(3, obj.getEmail());
        pstm.setString(4, obj.getPasswordHash());
        pstm.setString(5, obj.getRoli());
        pstm.setString(6, obj.getUsername());
        pstm.setInt(7, obj.getIdPerdoruesit());
    }

    @Override
    public boolean delete(Perdoruesi obj) {
        if (obj == null) {
            return false;
        }
        return delete(obj.getIdPerdoruesit());
    }

    public Perdoruesi findByUsername(String username) {
        String query = "SELECT * FROM " + TABLE + " WHERE username = ?";

        try (
                Connection conn = DatabaseService.getConnection();
                PreparedStatement pstm = conn.prepareStatement(query)
        ) {
            pstm.setString(1, username);

            try (ResultSet res = pstm.executeQuery()) {
                if (res.next()) {
                    return mapper.getFromResultSet(res);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by username: " + username, e);
        }

        return null;
    }
}