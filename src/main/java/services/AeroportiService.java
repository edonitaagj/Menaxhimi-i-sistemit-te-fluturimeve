package services;

import models.dto.AeroportiItem;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AeroportiService {

    public List<AeroportiItem> getAllAsItems() {
        String sql = "SELECT id_aeroportit, kodi_iata, emri_shqip FROM aeroportet ORDER BY kodi_iata";
        List<AeroportiItem> list = new ArrayList<>();
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next())
                list.add(new AeroportiItem(
                        rs.getInt("id_aeroportit"),
                        rs.getString("kodi_iata"),
                        rs.getString("emri_shqip")));
        } catch (SQLException e) {
            throw new RuntimeException("AeroportiService.getAllAsItems failed", e);
        }
        return list;
    }
}