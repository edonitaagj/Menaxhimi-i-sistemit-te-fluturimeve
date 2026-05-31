package repository;

import models.dto.BiletaTableDto;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BiletatRepository {

    public List<BiletaTableDto> getBiletatByPerdoruesiId(int perdoruesiId) {
        String email = getEmailByPerdoruesiId(perdoruesiId);
        if (email == null || email.isBlank()) {
            return new ArrayList<>();
        }
        return getBiletatByEmail(email);
    }

    private String getEmailByPerdoruesiId(int perdoruesiId) {
        String query = "SELECT email FROM perdoruesit WHERE id_perdoruesit = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, perdoruesiId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("email");
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së email-it të përdoruesit: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public List<BiletaTableDto> getBiletatByEmail(String email) {
        List<BiletaTableDto> lista = new ArrayList<>();

        String query = """
            SELECT b.id_biletes, b.numri_biletes, b.vendi_uljes, b.klasa_uljes,
                   b.cmimi, b.taksa, b.checked_in, b.statusi
            FROM biletat b
            INNER JOIN pasagjerit p ON p.id_pasagjerit = b.id_pasagjerit
            WHERE LOWER(TRIM(p.email)) = LOWER(TRIM(?))
            ORDER BY b.id_biletes DESC
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, email == null ? "" : email.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(new BiletaTableDto(
                            rs.getInt("id_biletes"),
                            rs.getString("numri_biletes"),
                            rs.getString("vendi_uljes"),
                            rs.getString("klasa_uljes"),
                            rs.getDouble("cmimi"),
                            rs.getDouble("taksa"),
                            rs.getBoolean("checked_in"),
                            rs.getString("statusi")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së biletave: " + e.getMessage());
            e.printStackTrace();
        }

        return lista;
    }

    public boolean updateCheckIn(int idBiletes, String vendiUljes, boolean isCheckedIn) {
        String statusiIRi = isCheckedIn ? "i_perdorur" : "i_vlefshem";

        String query = """
            UPDATE biletat
            SET vendi_uljes = ?,
                checked_in = ?,
                ora_checkin = CASE WHEN ? = 1 THEN NOW() ELSE NULL END,
                statusi = ?
            WHERE id_biletes = ?
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, vendiUljes);
            stmt.setBoolean(2, isCheckedIn);
            stmt.setBoolean(3, isCheckedIn);
            stmt.setString(4, statusiIRi);
            stmt.setInt(5, idBiletes);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë përditësimit të biletës: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}