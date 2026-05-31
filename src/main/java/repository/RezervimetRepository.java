package repository;

import models.dto.FluturimSelectDto;
import models.dto.OperationResponseDto;
import models.dto.RezervimiCreateRequestDto;
import models.dto.RezervimiCreateResponseDto;
import models.dto.RezervimiTableDto;
import services.DatabaseService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RezervimetRepository {

    public List<FluturimSelectDto> findAvailableFlights() {
        List<FluturimSelectDto> lista = new ArrayList<>();

        String sql = """
            SELECT
                f.id_fluturimit,
                f.numri_fluturimit,
                CONCAT(ani.kodi_iata, ' → ', arr.kodi_iata) AS relacioni,
                DATE_FORMAT(f.data_fluturimit, '%Y-%m-%d') AS data_fluturimit,
                DATE_FORMAT(f.ora_nisjes_planifikuar, '%H:%i') AS ora_nisjes,
                DATE_FORMAT(f.ora_mbrrritjes_planifikuar, '%H:%i') AS ora_mbrritjes,
                f.vendet_e_lira,
                l.distanca_km,
                f.statusi
            FROM fluturimet f
            INNER JOIN linjat l ON l.id_linjes = f.id_linjes
            INNER JOIN aeroportet ani ON ani.id_aeroportit = l.id_aeroportit_nisjes
            INNER JOIN aeroportet arr ON arr.id_aeroportit = l.id_aeroportit_mbrrritjes
            WHERE f.vendet_e_lira > 0
              AND f.statusi IN ('i_planifikuar', 'boarding', 'i_vonuar')
              -- Kushti i mëposhtëm është komentuar që të shfaqen fluturimet e 2025-ës
              -- AND DATE(f.data_fluturimit) >= CURDATE() 
            ORDER BY f.data_fluturimit ASC, f.ora_nisjes_planifikuar ASC
        """;

        try (
                Connection conn = DatabaseService.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()
        ) {
            while (rs.next()) {
                lista.add(new FluturimSelectDto(
                        rs.getInt("id_fluturimit"),
                        rs.getString("numri_fluturimit"),
                        rs.getString("relacioni"),
                        rs.getString("data_fluturimit"),
                        rs.getString("ora_nisjes"),
                        rs.getString("ora_mbrritjes"),
                        rs.getInt("vendet_e_lira"),
                        rs.getObject("distanca_km", Integer.class),
                        rs.getString("statusi")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë marrjes së fluturimeve të disponueshme.", e);
        }

        return lista;
    }

    public Integer findPassengerIdByEmail(String email) {
        String sql = "SELECT id_pasagjerit FROM pasagjerit WHERE email = ? LIMIT 1";

        try (
                Connection conn = DatabaseService.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)
        ) {
            pstm.setString(1, email);

            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_pasagjerit");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë kërkimit të pasagjerit.", e);
        }

        return null;
    }

    public boolean existsReservationForPassengerOnFlight(int idPasagjerit, int idFluturimit) {
        String sql = """
            SELECT 1
            FROM rezervimet
            WHERE id_pasagjerit = ? AND id_fluturimit = ? AND statusi <> 'anuluar'
            LIMIT 1
        """;

        try (
                Connection conn = DatabaseService.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)
        ) {
            pstm.setInt(1, idPasagjerit);
            pstm.setInt(2, idFluturimit);

            try (ResultSet rs = pstm.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë kontrollit të rezervimit ekzistues.", e);
        }
    }

    public RezervimiCreateResponseDto createReservation(int idPasagjerit, RezervimiCreateRequestDto request) {
        String insertSql = """
            INSERT INTO rezervimet
            (kodi_rezervimit, id_pasagjerit, id_fluturimit, klasa, cmimi_total, monedha, statusi, shenimet)
            VALUES (?, ?, ?, ?, ?, ?, 'konfirmuar', ?)
        """;

        String updateSeatsSql = """
            UPDATE fluturimet
            SET vendet_e_lira = vendet_e_lira - 1
            WHERE id_fluturimit = ?
              AND vendet_e_lira > 0
        """;

        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement seatsPstm = conn.prepareStatement(updateSeatsSql);
                    PreparedStatement insertPstm = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)
            ) {
                seatsPstm.setInt(1, request.getIdFluturimit());
                int updatedRows = seatsPstm.executeUpdate();

                if (updatedRows == 0) {
                    conn.rollback();
                    return new RezervimiCreateResponseDto(false, "Nuk ka vende të lira në këtë fluturim.");
                }

                String kodiRezervimit = generateUniqueReservationCode(conn);

                insertPstm.setString(1, kodiRezervimit);
                insertPstm.setInt(2, idPasagjerit);
                insertPstm.setInt(3, request.getIdFluturimit());
                insertPstm.setString(4, request.getKlasa());
                insertPstm.setBigDecimal(5, request.getCmimiTotal());
                insertPstm.setString(6, request.getMonedha());
                insertPstm.setString(7, request.getShenimet());

                int inserted = insertPstm.executeUpdate();
                if (inserted == 0) {
                    conn.rollback();
                    return new RezervimiCreateResponseDto(false, "Rezervimi nuk u krijua.");
                }

                int idRezervimit = -1;
                try (ResultSet keys = insertPstm.getGeneratedKeys()) {
                    if (keys.next()) {
                        idRezervimit = keys.getInt(1);
                    }
                }

                conn.commit();
                return new RezervimiCreateResponseDto(true, "Rezervimi u krijua me sukses.", kodiRezervimit, idRezervimit);

            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë krijimit të rezervimit.", e);
        }
    }

    public List<RezervimiTableDto> findReservationsByPassenger(int idPasagjerit) {
        List<RezervimiTableDto> lista = new ArrayList<>();

        String sql = """
            SELECT
                r.id_rezervimit,
                r.kodi_rezervimit,
                f.numri_fluturimit,
                CONCAT(ani.kodi_iata, ' → ', arr.kodi_iata) AS relacioni,
                DATE_FORMAT(r.data_rezervimit, '%Y-%m-%d %H:%i') AS data_rezervimit,
                r.klasa,
                CONCAT(FORMAT(r.cmimi_total, 2), ' ', r.monedha) AS cmimi_total,
                r.statusi
            FROM rezervimet r
            INNER JOIN fluturimet f ON f.id_fluturimit = r.id_fluturimit
            INNER JOIN linjat l ON l.id_linjes = f.id_linjes
            INNER JOIN aeroportet ani ON ani.id_aeroportit = l.id_aeroportit_nisjes
            INNER JOIN aeroportet arr ON arr.id_aeroportit = l.id_aeroportit_mbrrritjes
            WHERE r.id_pasagjerit = ?
            ORDER BY r.data_rezervimit DESC
        """;

        try (
                Connection conn = DatabaseService.getConnection();
                PreparedStatement pstm = conn.prepareStatement(sql)
        ) {
            pstm.setInt(1, idPasagjerit);

            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    lista.add(new RezervimiTableDto(
                            rs.getInt("id_rezervimit"),
                            rs.getString("kodi_rezervimit"),
                            rs.getString("numri_fluturimit"),
                            rs.getString("relacioni"),
                            rs.getString("data_rezervimit"),
                            rs.getString("klasa"),
                            rs.getString("cmimi_total"),
                            rs.getString("statusi")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë marrjes së rezervimeve.", e);
        }

        return lista;
    }

    public OperationResponseDto cancelReservation(int idRezervimit, int idPasagjerit) {
        String selectSql = """
            SELECT id_fluturimit, statusi
            FROM rezervimet
            WHERE id_rezervimit = ? AND id_pasagjerit = ?
            LIMIT 1
        """;

        String updateRezervimSql = """
            UPDATE rezervimet
            SET statusi = 'anuluar', data_anulimit = NOW()
            WHERE id_rezervimit = ? AND id_pasagjerit = ? AND statusi <> 'anuluar'
        """;

        String updateSeatsSql = """
            UPDATE fluturimet
            SET vendet_e_lira = vendet_e_lira + 1
            WHERE id_fluturimit = ?
        """;

        try (Connection conn = DatabaseService.getConnection()) {
            conn.setAutoCommit(false);

            try (
                    PreparedStatement selectPstm = conn.prepareStatement(selectSql);
                    PreparedStatement updateRezPstm = conn.prepareStatement(updateRezervimSql)
            ) {
                selectPstm.setInt(1, idRezervimit);
                selectPstm.setInt(2, idPasagjerit);

                Integer idFluturimit = null;
                String statusi = null;

                try (ResultSet rs = selectPstm.executeQuery()) {
                    if (rs.next()) {
                        idFluturimit = rs.getInt("id_fluturimit");
                        statusi = rs.getString("statusi");
                    } else {
                        conn.rollback();
                        return new OperationResponseDto(false, "Rezervimi nuk u gjet.");
                    }
                }

                if ("anuluar".equalsIgnoreCase(statusi)) {
                    conn.rollback();
                    return new OperationResponseDto(false, "Rezervimi është tashmë i anuluar.");
                }

                updateRezPstm.setInt(1, idRezervimit);
                updateRezPstm.setInt(2, idPasagjerit);

                int updated = updateRezPstm.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    return new OperationResponseDto(false, "Rezervimi nuk mund të anulohet.");
                }

                try (PreparedStatement seatsPstm = conn.prepareStatement(updateSeatsSql)) {
                    seatsPstm.setInt(1, idFluturimit);
                    seatsPstm.executeUpdate();
                }

                conn.commit();
                return new OperationResponseDto(true, "Rezervimi u anulua me sukses.");
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gabim gjatë anulimit të rezervimit.", e);
        }
    }

    private String generateUniqueReservationCode(Connection conn) throws SQLException {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();

        for (int attempt = 0; attempt < 10; attempt++) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 8; i++) {
                sb.append(chars.charAt(random.nextInt(chars.length())));
            }
            String code = sb.toString();

            String checkSql = "SELECT 1 FROM rezervimet WHERE kodi_rezervimit = ? LIMIT 1";
            try (PreparedStatement checkPstm = conn.prepareStatement(checkSql)) {
                checkPstm.setString(1, code);
                try (ResultSet rs = checkPstm.executeQuery()) {
                    if (!rs.next()) {
                        return code;
                    }
                }
            }
        }

        throw new SQLException("Nuk u gjenerua kodi unik i rezervimit.");
    }
}