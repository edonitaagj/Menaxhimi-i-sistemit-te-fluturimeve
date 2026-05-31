package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class ShtoPunonjesController {

    @FXML private TextField txtEmri;
    @FXML private TextField txtMbiemri;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefoni;
    @FXML private ComboBox<String> cmbDepartamenti;
    @FXML private TextField txtRoli;
    @FXML private DatePicker dtPunesimit;
    @FXML private TextField txtPaga;

    @FXML
    public void initialize() {
        // Mbushja e ComboBox-it me departamentet ekzakte nga kërkesa juaj
        cmbDepartamenti.getItems().addAll(
                "Operacionet Fluturuese",
                "Shërbimet e Kabinës",
                "Shërbimet tokësore",
                "Menaxhimi i Trafikut Ajror",
                "Mirëmbajtja",
                "Operacionet e Aeroportit",
                "Siguria"
        );

        // Vendos datën e sotme si vlerë fillestare te data e punësimit
        dtPunesimit.setValue(LocalDate.now());
    }

    @FXML
    private void handleRuajPunonjes() {
        // Validimi i fushave të detyrueshme (*)
        if (txtEmri.getText().trim().isEmpty() || txtMbiemri.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() || cmbDepartamenti.getValue() == null ||
                txtRoli.getText().trim().isEmpty() || txtPaga.getText().trim().isEmpty()) {

            shfaqAlert(AlertType.WARNING, "Validimi", "Ju lutem plotësoni të gjitha fushat e shënuara me * !");
            return;
        }

        String emri = txtEmri.getText().trim();
        String mbiemri = txtMbiemri.getText().trim();
        String email = txtEmail.getText().trim();
        String telefoni = txtTelefoni.getText().trim();
        String departamenti = cmbDepartamenti.getValue();
        String emriRoli = txtRoli.getText().trim();
        LocalDate dataPunesimit = dtPunesimit.getValue();

        // Gjenerimi automatik i një numri punonjësi unik (p.sh. EMP-shifër)
        String numriPunonjesit = "EMP-" + (int)(Math.random() * 9000 + 1000);

        Connection conn = null;
        try {
            conn = DatabaseService.getConnection();
            conn.setAutoCommit(false); // Fillojmë një transaksion për siguri

            // Hapi A: Kontrollojmë nëse roli ekziston në 'roli_stafit', nëse jo e krijojmë
            int idRolit = gjejOseKrijoRolin(conn, emriRoli, departamenti);

            // Hapi B: Fusim punonjësin e ri në tabelën 'stafi'
            String sqlStafi = "INSERT INTO stafi (id_rolit, emri, mbiemri, numri_punonjesit, email_punes, telefoni, data_fillimit, eshte_aktiv) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, 1)";

            try (PreparedStatement stmtStafi = conn.prepareStatement(sqlStafi)) {
                stmtStafi.setInt(1, idRolit);
                stmtStafi.setString(2, emri);
                stmtStafi.setString(3, mbiemri);
                stmtStafi.setString(4, numriPunonjesit);
                stmtStafi.setString(5, email);
                stmtStafi.setString(6, telefoni.isEmpty() ? null : telefoni);
                stmtStafi.setDate(7, java.sql.Date.valueOf(dataPunesimit));

                stmtStafi.executeUpdate();
            }

            conn.commit(); // Ruajmë ndryshimet me sukses në DB
            shfaqAlert(AlertType.INFORMATION, "Sukses", "Punonjësi u regjistrua me sukses! Numri i punonjësit: " + numriPunonjesit);

            // Mbyllim formën (dritaren pop-up) automatikisht pas ruajtjes
            handleAnulo();

        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            shfaqAlert(AlertType.ERROR, "Gabim në Databazë", "Ndodhi një gabim gjatë ruajtjes: " + e.getMessage());
        }
    }

    // Ndihmës funksion që gjen id-në e rolit ose shton një të ri nëse nuk ekziston për atë departament
    private int gjejOseKrijoRolin(Connection conn, String emriRoli, String departamenti) throws SQLException {
        String sqlKerko = "SELECT id_rolit FROM roli_stafit WHERE LOWER(emri_roli) = LOWER(?) AND LOWER(departamenti) = LOWER(?)";
        try (PreparedStatement stmt = conn.prepareStatement(sqlKerko)) {
            stmt.setString(1, emriRoli);
            stmt.setString(2, departamenti);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_rolit");
                }
            }
        }

        // Nëse nuk ekziston, e krijojmë rolin e ri automatikisht te roli_stafit
        String sqlShtoRol = "INSERT INTO roli_stafit (emri_roli, departamenti, pershkrimi) VALUES (?, ?, ?)";
        try (PreparedStatement stmtShto = conn.prepareStatement(sqlShtoRol, Statement.RETURN_GENERATED_KEYS)) {
            stmtShto.setString(1, emriRoli);
            stmtShto.setString(2, departamenti);
            stmtShto.setString(3, "Pozitë e shtuar përmes regjistrimit të stafit.");
            stmtShto.executeUpdate();

            try (ResultSet generatedKeys = stmtShto.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        throw new SQLException("Dështoi krijimi i rolit të ri.");
    }

    @FXML
    private void handleAnulo() {
        // Mbyll dritaren aktuale pop-up
        Stage stage = (Stage) txtEmri.getScene().getWindow();
        stage.close();
    }

    private void shfaqAlert(AlertType tip, String titulli, String mesazhi) {
        Alert alert = new Alert(tip);
        alert.setTitle(titulli);
        alert.setHeaderText(null);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }
}