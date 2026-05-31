package repository;

import models.Stafi;
import models.dto.StafiTableDTO;
import models.dto.StafiStatsDTO;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StafiRepository {

    /**
     * Merr statistikat e stafit në mënyrë dinamike për kartat e sipërme të pamjes.
     */
    public static StafiStatsDTO getStaffStatistics() {
        int totali = 0;
        int aktiv = 0;
        int departamente = 0;

        String queryTotali = "SELECT COUNT(*) FROM stafi";
        String queryAktiv = "SELECT COUNT(*) FROM stafi WHERE eshte_aktiv = 1";
        String queryDept = "SELECT COUNT(DISTINCT departamenti) FROM roli_stafit";

        try (Connection conn = DatabaseService.getConnection()) {

            try (PreparedStatement stmt = conn.prepareStatement(queryTotali);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) totali = rs.getInt(1);
            }

            try (PreparedStatement stmt = conn.prepareStatement(queryAktiv);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) aktiv = rs.getInt(1);
            }

            try (PreparedStatement stmt = conn.prepareStatement(queryDept);
                 ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) departamente = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println("Gabim gjatë leximit të statistikave të stafit: " + e.getMessage());
        }

        return new StafiStatsDTO(totali, aktiv, departamente);
    }

    /**
     * Ngarkon të gjithë punonjësit nga databaza duke i lidhur me rolet e tyre
     * dhe i kthen si një ObservableList e përshtatshme për TableView.
     */
    public static ObservableList<StafiTableDTO> getAllStaffForTable() {
        ObservableList<StafiTableDTO> lista = FXCollections.observableArrayList();

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
            System.err.println("Gabim gjatë leximit të stafit për tabelë: " + e.getMessage());
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Regjistron një punonjës të ri në mënyrë transaksionale.
     * Kontrollon nëse roli ekziston në atë departament, nëse jo e krijon,
     * dhe në fund inserton punonjësin në tabelën 'stafi'.
     */
    public static boolean shtoPunonjesTeRi(Stafi punonjes, String emriRoli, String departamenti) {
        String sqlKerkoRoli = "SELECT id_rolit FROM roli_stafit WHERE LOWER(emri_roli) = LOWER(?) AND LOWER(departamenti) = LOWER(?)";
        String sqlShtoRoli = "INSERT INTO roli_stafit (emri_roli, departamenti) VALUES (?, ?)";
        String sqlInsertStafi = "INSERT INTO stafi (id_rolit, id_kompanise, id_aeroportit, emri, mbiemri, numri_punonjesit, email_punes, telefoni, data_fillimit, eshte_aktiv) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DatabaseService.getConnection();
            conn.setAutoCommit(false); // Fillimi i Transaksionit (ACID)

            int idRolit = -1;

            // 1. Kontrollojmë nëse ekziston kombinimi i këtij roli me këtë departament
            try (PreparedStatement stmtKerko = conn.prepareStatement(sqlKerkoRoli)) {
                stmtKerko.setString(1, emriRoli);
                stmtKerko.setString(2, departamenti);
                try (ResultSet rs = stmtKerko.executeQuery()) {
                    if (rs.next()) {
                        idRolit = rs.getInt("id_rolit");
                    }
                }
            }

            // 2. Nëse roli nuk ekziston për këtë sektor, e krijojmë të ririn automatikisht
            if (idRolit == -1) {
                try (PreparedStatement stmtShto = conn.prepareStatement(sqlShtoRoli, Statement.RETURN_GENERATED_KEYS)) {
                    stmtShto.setString(1, emriRoli);
                    stmtShto.setString(2, departamenti);
                    stmtShto.executeUpdate();

                    try (ResultSet gk = stmtShto.getGeneratedKeys()) {
                        if (gk.next()) {
                            idRolit = gk.getInt(1);
                        }
                    }
                }
            }

            // 3. Ruajmë të dhënat e punonjësit në tabelën 'stafi'
            try (PreparedStatement stmtStafi = conn.prepareStatement(sqlInsertStafi)) {
                stmtStafi.setInt(1, idRolit);

                // Trajtimi i fushave Nullable (për të shmangur NullPointerException në SQL)
                if (punonjes.getIdKompanise() == null) {
                    stmtStafi.setNull(2, Types.INTEGER);
                } else {
                    stmtStafi.setInt(2, punonjes.getIdKompanise());
                }

                if (punonjes.getIdAeroportit() == null) {
                    stmtStafi.setNull(3, Types.INTEGER);
                } else {
                    stmtStafi.setInt(3, punonjes.getIdAeroportit());
                }

                stmtStafi.setString(4, punonjes.getEmri());
                stmtStafi.setString(5, punonjes.getMbiemri());
                stmtStafi.setString(6, punonjes.getNumriPunonjesit());
                stmtStafi.setString(7, punonjes.getEmailPunes());
                stmtStafi.setString(8, punonjes.getTelefoni());
                stmtStafi.setDate(9, punonjes.getDataFillimit());
                stmtStafi.setInt(10, punonjes.getEshteAktiv() ? 1 : 0);

                stmtStafi.executeUpdate();
            }

            conn.commit(); // Ruajtja përfundimtare në databazë nëse çdo gjë shkoi me sukses
            return true;

        } catch (SQLException e) {
            // Kthimi prapa (Rollback) i të gjitha hapave nëse dështon qoftë edhe njëri veprim
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.err.println("Gabim gjatë ekzekutimit të transaksionit për shtimin e punonjësit: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static boolean perditesoPunonjes(Stafi punonjes, String emriRoli, String departamenti) {
        String sqlKerkoRoli = "SELECT id_rolit FROM roli_stafit WHERE LOWER(emri_roli) = LOWER(?) AND LOWER(departamenti) = LOWER(?)";
        String sqlShtoRoli = "INSERT INTO roli_stafit (emri_roli, departamenti) VALUES (?, ?)";
        String sqlUpdateStafi = "UPDATE stafi SET id_rolit = ?, emri = ?, mbiemri = ?, email_punes = ?, telefoni = ?, eshte_aktiv = ?, data_mbarimit = ? WHERE id_stafit = ?";

        Connection conn = null;
        try {
            conn = DatabaseService.getConnection();
            conn.setAutoCommit(false);

            int idRolit = -1;
            // 1. Kontrollojmë rolin e ri/ekzistues
            try (PreparedStatement stmtKerko = conn.prepareStatement(sqlKerkoRoli)) {
                stmtKerko.setString(1, emriRoli);
                stmtKerko.setString(2, departamenti);
                try (ResultSet rs = stmtKerko.executeQuery()) {
                    if (rs.next()) idRolit = rs.getInt("id_rolit");
                }
            }

            if (idRolit == -1) {
                try (PreparedStatement stmtShto = conn.prepareStatement(sqlShtoRoli, Statement.RETURN_GENERATED_KEYS)) {
                    stmtShto.setString(1, emriRoli);
                    stmtShto.setString(2, departamenti);
                    stmtShto.executeUpdate();
                    try (ResultSet gk = stmtShto.getGeneratedKeys()) {
                        if (gk.next()) idRolit = gk.getInt(1);
                    }
                }
            }

            // 2. Përditësojmë tabelën stafi
            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateStafi)) {
                stmtUpdate.setInt(1, idRolit);
                stmtUpdate.setString(2, punonjes.getEmri());
                stmtUpdate.setString(3, punonjes.getMbiemri());
                stmtUpdate.setString(4, punonjes.getEmailPunes());
                stmtUpdate.setString(5, punonjes.getTelefoni());
                stmtUpdate.setInt(6, punonjes.getEshteAktiv() ? 1 : 0);
                stmtUpdate.setDate(7, punonjes.getDataMbarimit()); // Do të jetë null nëse është aktiv
                stmtUpdate.setInt(8, punonjes.getIdStafit());

                stmtUpdate.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {} }
            e.printStackTrace();
            return false;
        }
    }
}