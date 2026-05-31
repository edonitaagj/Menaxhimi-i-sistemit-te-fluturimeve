package repository;

import models.dto.AvionetRequestDto;
import models.dto.AvionetTableDto;
import models.dto.LookupDto;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AvionetRepository {

    public List<AvionetTableDto> getAll() {
        String sql = """
            SELECT a.id_avionit, a.id_kompanise, a.id_llojit, a.numri_regjistrit,
                   a.viti_prodhimit, a.statusi,
                   k.emri AS emri_kompanise,
                   l.prodhuesi, l.modeli
            FROM avionet a
            INNER JOIN kompanite_ajrore k ON k.id_kompanise = a.id_kompanise
            INNER JOIN llojet_avioneve l ON l.id_llojit = a.id_llojit
            ORDER BY a.id_avionit DESC
        """;

        List<AvionetTableDto> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new AvionetTableDto(
                        rs.getInt("id_avionit"),
                        rs.getInt("id_kompanise"),
                        rs.getInt("id_llojit"),
                        rs.getString("numri_regjistrit"),
                        rs.getString("prodhuesi"),
                        rs.getString("modeli"),
                        rs.getString("viti_prodhimit"),
                        rs.getString("statusi"),
                        rs.getString("emri_kompanise")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së avionëve: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<AvionetTableDto> searchByRegister(String query) {
        String sql = """
            SELECT a.id_avionit, a.id_kompanise, a.id_llojit, a.numri_regjistrit,
                   a.viti_prodhimit, a.statusi,
                   k.emri AS emri_kompanise,
                   l.prodhuesi, l.modeli
            FROM avionet a
            INNER JOIN kompanite_ajrore k ON k.id_kompanise = a.id_kompanise
            INNER JOIN llojet_avioneve l ON l.id_llojit = a.id_llojit
            WHERE LOWER(a.numri_regjistrit) LIKE LOWER(?)
               OR LOWER(l.prodhuesi) LIKE LOWER(?)
               OR LOWER(l.modeli) LIKE LOWER(?)
               OR LOWER(a.statusi) LIKE LOWER(?)
            ORDER BY a.id_avionit DESC
        """;

        List<AvionetTableDto> list = new ArrayList<>();
        String like = "%" + (query == null ? "" : query.trim()) + "%";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            ps.setString(4, like);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new AvionetTableDto(
                            rs.getInt("id_avionit"),
                            rs.getInt("id_kompanise"),
                            rs.getInt("id_llojit"),
                            rs.getString("numri_regjistrit"),
                            rs.getString("prodhuesi"),
                            rs.getString("modeli"),
                            rs.getString("viti_prodhimit"),
                            rs.getString("statusi"),
                            rs.getString("emri_kompanise")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë search të avionëve: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public boolean insert(AvionetRequestDto dto) {
        String sql = """
            INSERT INTO avionet (id_kompanise, id_llojit, numri_regjistrit, viti_prodhimit, statusi)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dto.getIdKompanise());
            ps.setInt(2, dto.getIdLlojit());
            ps.setString(3, dto.getNumriRegjistrit());
            if (dto.getVitiProdhimit() != null) {
                ps.setInt(4, dto.getVitiProdhimit());
            } else {
                ps.setNull(4, Types.INTEGER);
            }
            ps.setString(5, dto.getStatusi() != null ? dto.getStatusi() : "aktiv");

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë insert të avionit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteById(int idAvionit) {
        String sql = "DELETE FROM avionet WHERE id_avionit = ?";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAvionit);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Gabim gjatë delete të avionit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<LookupDto> getKompaniteLookup() {
        String sql = """
            SELECT id_kompanise, CONCAT(emri, ' (', kodi_iata, '/', kodi_icao, ')') AS label
            FROM kompanite_ajrore
            ORDER BY emri
        """;
        List<LookupDto> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new LookupDto(rs.getInt("id_kompanise"), rs.getString("label")));
            }
        } catch (SQLException e) {
            System.err.println("Gabim në lookup kompanitë: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<LookupDto> getLlojetLookup() {
        String sql = """
            SELECT id_llojit, CONCAT(prodhuesi, ' - ', modeli) AS label
            FROM llojet_avioneve
            ORDER BY prodhuesi, modeli
        """;
        List<LookupDto> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new LookupDto(rs.getInt("id_llojit"), rs.getString("label")));
            }
        } catch (SQLException e) {
            System.err.println("Gabim në lookup llojet: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}