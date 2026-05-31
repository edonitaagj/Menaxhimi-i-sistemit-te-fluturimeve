package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.Perdoruesi;
import repository.UserRepository;
import services.HashService;


public class ProfilController {

    // ── Sidebar nav ──────────────────────────────────────────────────────
    @FXML private HBox navHome;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navBileta;
    @FXML private HBox navNjoftimet;
    @FXML private HBox navProfili;

    // ── Profili fields ───────────────────────────────────────────────────
    @FXML private TextField     txtEmriPlote;
    @FXML private TextField     txtEmail;
    @FXML private TextField     txtPasaporta;

    // ── Siguria fields ───────────────────────────────────────────────────
    @FXML private PasswordField txtFjalekalimiAktual;
    @FXML private PasswordField txtFjalekalimiRi;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    private void initialize() {
        loadProfili();
    }

    // ════════════════════════════════════════════════════════════════════
    //  Load
    // ════════════════════════════════════════════════════════════════════
    private void loadProfili() {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null) return;
        txtEmriPlote.setText(user.getFullName());
        txtEmail.setText(user.getEmail() != null ? user.getEmail() : "");
        txtPasaporta.setPromptText("P.sh. P0000000");
    }

    // ════════════════════════════════════════════════════════════════════
    //  Ruaj profilin
    // ════════════════════════════════════════════════════════════════════
    @FXML
    private void handleRuajProfili() {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null) return;

        String emriPlote = txtEmriPlote.getText().trim();
        if (emriPlote.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Gabim Validimi", "Emri i plotë nuk mund të jetë bosh.");
            return;
        }

        String[] pjeset  = emriPlote.split(" ", 2);
        String emriRi    = pjeset[0].trim();
        String mbiemriRi = pjeset.length > 1 ? pjeset[1].trim() : user.getMbiemri();

        try {
            user.setEmri(emriRi);
            user.setMbiemri(mbiemriRi);
            userRepo.update(user);
            SessionManager.login(userRepo.getById(user.getIdPerdoruesit()));
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Profili u përditësua me sukses.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Ndodhi një gabim: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Ndrysho fjalëkalimin
    // ════════════════════════════════════════════════════════════════════
    @FXML
    private void handleNdryshoFjalekalimin() {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null) return;

        String aktual = txtFjalekalimiAktual.getText();
        String ri     = txtFjalekalimiRi.getText();

        if (aktual.isEmpty() || ri.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Gabim Validimi", "Plotësoni të dy fushat.");
            return;
        }
        if (ri.length() < 8) {
            showAlert(Alert.AlertType.WARNING, "Gabim Validimi", "Fjalëkalimi i ri duhet të ketë të paktën 8 karaktere.");
            return;
        }
        if (!HashService.validatePassword(aktual, user.getPasswordHash())) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Fjalëkalimi aktual është i gabuar.");
            txtFjalekalimiAktual.clear();
            return;
        }

        try {
            user.setPasswordHash(HashService.generateHash(ri));
            userRepo.update(user);
            SessionManager.login(userRepo.getById(user.getIdPerdoruesit()));
            txtFjalekalimiAktual.clear();
            txtFjalekalimiRi.clear();
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Fjalëkalimi u ndryshua me sukses.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Ndodhi një gabim: " + e.getMessage());
        }
    }

    // ════════════════════════════════════════════════════════════════════
    //  Sidebar navigation — të gjitha metodat që i kërkon FXML-i
    // ════════════════════════════════════════════════════════════════════
    @FXML private void handleNavHome()       { Router.navigateTo(ViewsEnum.HOME_VIEW); }
    @FXML private void handleNavRezervimet() { Router.navigateTo(ViewsEnum.REZERVIMET_VIEW); }
    @FXML private void handleNavBileta()     { Router.navigateTo(ViewsEnum.BILETAT_VIEW); }
    @FXML private void handleNavNjoftimet()  { Router.navigateTo(ViewsEnum.NJOFTIMET_VIEW); }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    // ════════════════════════════════════════════════════════════════════
    //  Helper
    // ════════════════════════════════════════════════════════════════════
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}