package services;

import models.dto.KompaniaItem;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service i lehtë — kthe kompanitë si items për ComboBox.
 * Nuk ka logjikë biznesi — vetëm query.
 */
public class KompaniaItemService {

    public List<KompaniaItem> getAllAsItems() {
        String sql = """
            SELECT id_kompanise, kodi_iata, emri_i_shkurter
            FROM kompanite_ajrore
            WHERE eshte_aktive = TRUE
            ORDER BY emri_i_shkurter
            """;
        List<KompaniaItem> list = new ArrayList<>();
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next()) {
                list.add(new KompaniaItem(
                        rs.getInt("id_kompanise"),
                        rs.getString("kodi_iata"),
                        nvl(rs.getString("emri_i_shkurter"), rs.getString("kodi_iata"))
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("KompaniaItemService.getAllAsItems failed", e);
        }
        return list;
    }

    private String nvl(String v, String fb) {
        return (v != null && !v.isBlank()) ? v : fb;
    }
}