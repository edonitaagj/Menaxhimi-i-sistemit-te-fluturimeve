package repository;

import models.Perdoruesi;
import models.mappers.UserMapper;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordResetRepository {

    private final UserMapper userMapper = new UserMapper();

    public Perdoruesi findByUsernameAndEmail(String username, String email) {
        String sql = "SELECT * FROM perdoruesit WHERE TRIM(username) = ? AND LOWER(TRIM(email)) = LOWER(?)";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());
            stmt.setString(2, email.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return userMapper.getFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user for password reset.", e);
        }

        return null;
    }

    public boolean updatePassword(int userId, String passwordHash) {
        String sql = "UPDATE perdoruesit SET password = ?, tentativa_login = 0 WHERE id_perdoruesit = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, passwordHash);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update user password.", e);
        }
    }
}
