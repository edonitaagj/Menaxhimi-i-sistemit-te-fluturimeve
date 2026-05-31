package repository;

import models.dto.HomeFluturimiTableDto;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class HomeRepository {

    public List<HomeFluturimiTableDto> getFluturimetBoard() {
        return searchFluturimet("", "", null);
    }

    public List<HomeFluturimiTableDto> searchFluturimet(String nga, String deri, LocalDate data) {
        List<HomeFluturimiTableDto> fluturimet = new ArrayList<>();
        List<Object> params = new ArrayList<>();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT f.id_fluturimit, ");
        sql.append("f.numri_fluturimit, ");
        sql.append("CONCAT(an.kodi_iata, ' -> ', am.kodi_iata) AS destinacioni, ");
        sql.append("DATE_FORMAT(COALESCE(f.ora_nisjes_aktuale, f.ora_nisjes_planifikuar), '%H:%i') AS ora, ");
        sql.append("COALESCE(g.kodi_gejtit, '-') AS gejti, ");
        sql.append("f.statusi, ");
        sql.append("DATE_FORMAT(f.data_fluturimit, '%Y-%m-%d') AS data_fluturimit ");
        sql.append("FROM fluturimet f ");
        sql.append("JOIN linjat l ON f.id_linjes = l.id_linjes ");
        sql.append("JOIN aeroportet an ON l.id_aeroportit_nisjes = an.id_aeroportit ");
        sql.append("JOIN aeroportet am ON l.id_aeroportit_mbrrritjes = am.id_aeroportit ");
        sql.append("LEFT JOIN gejtat g ON f.id_gejtit_nisjes = g.id_gejtit ");
        sql.append("WHERE 1 = 1 ");

        if (nga != null && !nga.trim().isEmpty()) {
            sql.append("AND (LOWER(an.kodi_iata) LIKE ? OR LOWER(an.emri_shqip) LIKE ?) ");
            String value = "%" + nga.trim().toLowerCase() + "%";
            params.add(value);
            params.add(value);
        }

        if (deri != null && !deri.trim().isEmpty()) {
            sql.append("AND (LOWER(am.kodi_iata) LIKE ? OR LOWER(am.emri_shqip) LIKE ?) ");
            String value = "%" + deri.trim().toLowerCase() + "%";
            params.add(value);
            params.add(value);
        }

        if (data != null) {
            sql.append("AND f.data_fluturimit = ? ");
            params.add(Date.valueOf(data));
        }

        sql.append("ORDER BY f.data_fluturimit ASC, COALESCE(f.ora_nisjes_aktuale, f.ora_nisjes_planifikuar) ASC ");
        sql.append("LIMIT 50");

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    fluturimet.add(new HomeFluturimiTableDto(
                            rs.getInt("id_fluturimit"),
                            rs.getString("numri_fluturimit"),
                            rs.getString("destinacioni"),
                            rs.getString("ora"),
                            rs.getString("gejti"),
                            formatStatus(rs.getString("statusi")),
                            rs.getString("data_fluturimit")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fluturimet;
    }

    public int getTotalFlightsCount() {
        return getCount("SELECT COUNT(*) FROM fluturimet");
    }

    public int getReservationsCount() {
        return getCount("SELECT COUNT(*) FROM rezervimet");
    }

    public int getNotificationsCount() {
        return getCount("SELECT COUNT(*) FROM njoftimet");
    }

    public HomeFluturimiTableDto getNextFlight() {
        List<HomeFluturimiTableDto> futureFlights = new ArrayList<>();

        String sql = "SELECT f.id_fluturimit, f.numri_fluturimit, " +
                "CONCAT(an.kodi_iata, ' -> ', am.kodi_iata) AS destinacioni, " +
                "DATE_FORMAT(COALESCE(f.ora_nisjes_aktuale, f.ora_nisjes_planifikuar), '%H:%i') AS ora, " +
                "COALESCE(g.kodi_gejtit, '-') AS gejti, f.statusi, " +
                "DATE_FORMAT(f.data_fluturimit, '%Y-%m-%d') AS data_fluturimit " +
                "FROM fluturimet f " +
                "JOIN linjat l ON f.id_linjes = l.id_linjes " +
                "JOIN aeroportet an ON l.id_aeroportit_nisjes = an.id_aeroportit " +
                "JOIN aeroportet am ON l.id_aeroportit_mbrrritjes = am.id_aeroportit " +
                "LEFT JOIN gejtat g ON f.id_gejtit_nisjes = g.id_gejtit " +
                "WHERE f.data_fluturimit >= CURDATE() " +
                "ORDER BY f.data_fluturimit ASC, COALESCE(f.ora_nisjes_aktuale, f.ora_nisjes_planifikuar) ASC " +
                "LIMIT 1";

        futureFlights.addAll(readFlights(sql));
        if (!futureFlights.isEmpty()) {
            return futureFlights.get(0);
        }

        String fallbackSql = "SELECT f.id_fluturimit, f.numri_fluturimit, " +
                "CONCAT(an.kodi_iata, ' -> ', am.kodi_iata) AS destinacioni, " +
                "DATE_FORMAT(COALESCE(f.ora_nisjes_aktuale, f.ora_nisjes_planifikuar), '%H:%i') AS ora, " +
                "COALESCE(g.kodi_gejtit, '-') AS gejti, f.statusi, " +
                "DATE_FORMAT(f.data_fluturimit, '%Y-%m-%d') AS data_fluturimit " +
                "FROM fluturimet f " +
                "JOIN linjat l ON f.id_linjes = l.id_linjes " +
                "JOIN aeroportet an ON l.id_aeroportit_nisjes = an.id_aeroportit " +
                "JOIN aeroportet am ON l.id_aeroportit_mbrrritjes = am.id_aeroportit " +
                "LEFT JOIN gejtat g ON f.id_gejtit_nisjes = g.id_gejtit " +
                "ORDER BY f.data_fluturimit ASC, COALESCE(f.ora_nisjes_aktuale, f.ora_nisjes_planifikuar) ASC " +
                "LIMIT 1";

        List<HomeFluturimiTableDto> fallbackFlights = readFlights(fallbackSql);
        return fallbackFlights.isEmpty() ? null : fallbackFlights.get(0);
    }

    private List<HomeFluturimiTableDto> readFlights(String sql) {
        List<HomeFluturimiTableDto> fluturimet = new ArrayList<>();

        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                fluturimet.add(new HomeFluturimiTableDto(
                        rs.getInt("id_fluturimit"),
                        rs.getString("numri_fluturimit"),
                        rs.getString("destinacioni"),
                        rs.getString("ora"),
                        rs.getString("gejti"),
                        formatStatus(rs.getString("statusi")),
                        rs.getString("data_fluturimit")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return fluturimet;
    }

    private int getCount(String sql) {
        try (Connection conn = DatabaseService.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String formatStatus(String status) {
        if (status == null || status.isBlank()) {
            return "-";
        }
        return status.replace("_", " ").toUpperCase();
    }
}
