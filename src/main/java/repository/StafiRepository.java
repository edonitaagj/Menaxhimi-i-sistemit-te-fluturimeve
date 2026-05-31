package repository;

import models.dto.StafiTableDTO;
import services.DatabaseService;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StafiRepository {
    public static models.dto.StafiStatsDTO getStaffStatistics() {
        int totali = 0;
        int aktiv = 0;
        int departamente = 0;

        // Queries për të numëruar vlerat reale
        String queryTotali = "SELECT COUNT(*) FROM stafi";
        String queryAktiv = "SELECT COUNT(*) FROM stafi WHERE eshte_aktiv = 1";
        String queryDept = "SELECT COUNT(DISTINCT departamenti) FROM roli_stafit";

        try (java.sql.Connection conn = services.DatabaseService.getConnection()) {

            try (java.sql.PreparedStatement stmt = conn.prepareStatement(queryTotali);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) totali = rs.getInt(1);
            }

            try (java.sql.PreparedStatement stmt = conn.prepareStatement(queryAktiv);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) aktiv = rs.getInt(1);
            }

            try (java.sql.PreparedStatement stmt = conn.prepareStatement(queryDept);
                 java.sql.ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) departamente = rs.getInt(1);
            }

        } catch (java.sql.SQLException e) {
            System.err.println("Gabim gjatë leximit të statistikave: " + e.getMessage());
        }

        return new models.dto.StafiStatsDTO(totali, aktiv, departamente);
    }

    public static ObservableList<StafiTableDTO> getAllStaffForTable() {
        ObservableList<StafiTableDTO> lista = FXCollections.observableArrayList();

        // Query që bën bashkimin e stafit me rolin për të marrë emrin e rolit dhe departamentin
        String query = "SELECT s.id_stafit, s.emri, s.mbiemri, r.emri_roli, r.departamenti, s.email_punes, s.eshte_aktiv " +
                "FROM stafi s " +
                "INNER JOIN roli_stafit r ON s.id_rolit = r.id_rolit";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                StafiTableDTO dto = new StafiTableDTO(
                        rs.getInt("id_stafit"),
                        rs.getString("emri"),
                        rs.getString("mbiemri"),
                        rs.getString("emri_roli"),
                        rs.getString("departamenti"),
                        rs.getString("email_punes"),
                        rs.getBoolean("eshte_aktiv")
                );
                lista.add(dto);
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë leximit të stafit nga databaza: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

}