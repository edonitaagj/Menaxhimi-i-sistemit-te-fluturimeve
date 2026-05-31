package repository;

import models.dto.NjoftimiTableDto;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NjoftimetRepository {

    public List<NjoftimiTableDto> findAllActiveNotifications() {
        List<NjoftimiTableDto> lista = new ArrayList<>();

        String sql = """
            SELECT
                id_njoftimit,
                lloji,
                CASE
                    WHEN mesazhi_sq IS NOT NULL AND mesazhi_sq <> '' THEN mesazhi_sq
                    ELSE COALESCE(titulli_sq, '')
                END AS mesazhi,
                DATE_FORMAT(aktiv_nga, '%Y-%m-%d %H:%i') AS data_njoftimit,
                aktiv_nga,
                aktiv_deri
            FROM njoftimet
            ORDER BY aktiv_nga DESC
        """;

        try (
                Connection conn = DatabaseService.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()
        ) {
            while (rs.next()) {
                String lloji = formatType(rs.getString("lloji"));
                String mesazhi = rs.getString("mesazhi");
                String data = rs.getString("data_njoftimit");

                lista.add(new NjoftimiTableDto(
                        rs.getInt("id_njoftimit"),
                        lloji,
                        mesazhi != null ? mesazhi : "",
                        data != null ? data : ""
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë marrjes së njoftimeve.", e);
        }

        return lista;
    }

    private String formatType(String value) {
        if (value == null) return "";
        return switch (value.toLowerCase()) {
            case "info" -> "INFO";
            case "vonese" -> "VONË";
            case "anulim" -> "ANULIM";
            case "boarding" -> "BOARDING";
            case "emergjence" -> "EMERGJENCË";
            case "tjeter" -> "TJETËR";
            default -> value.toUpperCase();
        };
    }
}