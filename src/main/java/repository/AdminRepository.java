package repository;

import models.dto.FluturimiTabelaDTO;
import models.mappers.FluturimiMapper;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRepository {

    public List<FluturimiTabelaDTO> getFluturimetDashboard() {
        List<FluturimiTabelaDTO> lista = new ArrayList<>();

        // SQL me dy JOIN-e mbi të njëjtën tabelë aeroportesh duke përdorur alias-et 'an' dhe 'am'
        String sql = "SELECT f.id_fluturimit, f.numri_fluturimit, f.ora_nisjes_planifikuar, f.statusi, " +
                "       k.emri AS kompania_emri, " +
                "       an.emri_shqip AS aeroporti_nisjes, " +
                "       am.emri_shqip AS aeroporti_mberritjes " +
                "FROM fluturimet f " +
                "JOIN kompanite_ajrore k ON f.id_kompanise = k.id_kompanise " +
                "JOIN linjat l ON f.id_linjes = l.id_linjes " +
                "JOIN aeroportet an ON l.id_aeroportit_nisjes = an.id_aeroportit " +
                "JOIN aeroportet am ON l.id_aeroportit_mbrrritjes = am.id_aeroportit";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(FluturimiMapper.toDTO(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public int getTotalFlightsCount() {
        String sql = "SELECT COUNT(*) AS total FROM fluturimet";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    public int getLostItemsCount() {
        String sql = "SELECT COUNT(*) AS total FROM artikujt_e_humbur";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }
}