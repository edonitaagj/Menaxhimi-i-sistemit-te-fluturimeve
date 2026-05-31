package services;

import models.dto.VendiItem;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VendiService {

    public List<VendiItem> getAllAsItems() {
        String sql = "SELECT id_vendi, emri_shqip FROM vendet ORDER BY emri_shqip";
        List<VendiItem> list = new ArrayList<>();
        try (Connection c = DatabaseService.getConnection();
             PreparedStatement p = c.prepareStatement(sql);
             ResultSet rs = p.executeQuery()) {
            while (rs.next())
                list.add(new VendiItem(rs.getInt("id_vendi"), rs.getString("emri_shqip")));
        } catch (SQLException e) {
            throw new RuntimeException("VendiService.getAllAsItems failed", e);
        }
        return list;
    }
}