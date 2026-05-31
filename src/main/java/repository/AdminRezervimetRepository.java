package repository;

import models.dto.RezervimeTableDto;
import models.mappers.RezervimeMapper;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminRezervimetRepository {

    private final RezervimeMapper mapper = new RezervimeMapper();

    public List<RezervimeTableDto> getAll() {
        String sql = """
            SELECT
                r.id_rezervimit,
                r.kodi_rezervimit,
                r.id_pasagjerit,
                r.id_fluturimit,
                r.klasa,
                r.cmimi_total,
                r.monedha,
                r.statusi,
                r.data_rezervimit,
                r.data_anulimit,
                r.shenimet,
                CONCAT(COALESCE(p.emri, ''), ' ', COALESCE(p.mbiemri, '')) AS pasagjeri,
                COALESCE(f.numri_fluturimit, '-') AS fluturimi_kodi,
                COALESCE(pg.statusi, 'e_pritshme') AS statusi_pageses
            FROM rezervimet r
            LEFT JOIN pasagjerit p ON p.id_pasagjerit = r.id_pasagjerit
            LEFT JOIN fluturimet f ON f.id_fluturimit = r.id_fluturimit
            LEFT JOIN pagesat pg ON pg.id_rezervimit = r.id_rezervimit
            ORDER BY r.id_rezervimit DESC
        """;

        List<RezervimeTableDto> list = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapper.getFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë marrjes së rezervimeve: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public List<RezervimeTableDto> search(String q) {
        String sql = """
            SELECT
                r.id_rezervimit,
                r.kodi_rezervimit,
                r.id_pasagjerit,
                r.id_fluturimit,
                r.klasa,
                r.cmimi_total,
                r.monedha,
                r.statusi,
                r.data_rezervimit,
                r.data_anulimit,
                r.shenimet,
                CONCAT(COALESCE(p.emri, ''), ' ', COALESCE(p.mbiemri, '')) AS pasagjeri,
                COALESCE(f.numri_fluturimit, '-') AS fluturimi_kodi,
                COALESCE(pg.statusi, 'e_pritshme') AS statusi_pageses
            FROM rezervimet r
            LEFT JOIN pasagjerit p ON p.id_pasagjerit = r.id_pasagjerit
            LEFT JOIN fluturimet f ON f.id_fluturimit = r.id_fluturimit
            LEFT JOIN pagesat pg ON pg.id_rezervimit = r.id_rezervimit
            WHERE LOWER(COALESCE(r.kodi_rezervimit, '')) LIKE LOWER(?)
               OR LOWER(CONCAT(COALESCE(p.emri, ''), ' ', COALESCE(p.mbiemri, ''))) LIKE LOWER(?)
               OR LOWER(COALESCE(f.numri_fluturimit, '')) LIKE LOWER(?)
               OR LOWER(COALESCE(r.klasa, '')) LIKE LOWER(?)
               OR LOWER(COALESCE(r.statusi, '')) LIKE LOWER(?)
               OR LOWER(COALESCE(pg.statusi, '')) LIKE LOWER(?)
               OR CAST(r.id_rezervimit AS CHAR) LIKE ?
            ORDER BY r.id_rezervimit DESC
        """;

        List<RezervimeTableDto> list = new ArrayList<>();
        String like = "%" + (q == null ? "" : q.trim()) + "%";

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 1; i <= 7; i++) {
                ps.setString(i, like);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapper.getFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Gabim gjatë search të rezervimeve: " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    public int countTodayReservations() {
        String sql = "SELECT COUNT(*) AS total FROM rezervimet WHERE DATE(data_rezervimit) = CURDATE()";
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt("total");
        } catch (SQLException e) {
            System.err.println("Gabim gjatë count të rezervimeve të sotme: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public double sumTodayRevenue() {
        String sql = """
            SELECT COALESCE(SUM(p.shuma), 0) AS total
            FROM pagesat p
            INNER JOIN rezervimet r ON r.id_rezervimit = p.id_rezervimit
            WHERE DATE(p.data_pageses) = CURDATE()
              AND p.statusi = 'e_kryer'
        """;
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble("total");
        } catch (SQLException e) {
            System.err.println("Gabim gjatë sum të arkëtimeve të sotme: " + e.getMessage());
            e.printStackTrace();
        }
        return 0.0;
    }
}