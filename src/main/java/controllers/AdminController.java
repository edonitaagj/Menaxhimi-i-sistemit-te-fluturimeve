package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import models.Perdoruesi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AdminController {

    // ===============================
    // ADMIN INFO (Sidebar bottom)
    // ===============================
    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

    // ===============================
    // CLOCK & HEADER
    // ===============================
    // Shënim: Nëse dëshiron të shfaqësh orën edhe te paneli i adminit,
    // mund t'i shtosh këto labela në FXML te pjesa e Header-it.
    @FXML private Label clockLabel;
    @FXML private Label dateLabel;

    // ===============================
    // SEARCH & FILTERS
    // ===============================
    @FXML private TextField adminSearchField;

    // ===============================
    // SIDEBAR NAVIGATION ELEMENTS
    // ===============================
    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    // ===============================
    // TABLE VIEW (Menaxhimi i përgjithshëm)
    // ===============================
    @FXML private TableView<?> adminDataTable;

    // ===============================
    // INIT
    // ===============================
    @FXML
    public void initialize() {
        loadAdminData();
        setupSidebarActions();

        // Nëse ke vendosur clockLabel dhe dateLabel në FXML, hiqja komentet kësaj vije:
        // startClock();

        loadDashboardData(); // Ngarkon tabelën e parë sapo hapet faqja
    }

    private void loadAdminData() {
        Perdoruesi admin = SessionManager.getCurrentUser();

        if (admin != null) {
            String fullName = admin.getEmri() + " " + admin.getMbiemri();

            userFullName.setText(fullName);
            userEmail.setText(admin.getEmail());

            // Merr shkronjën e parë të emrit për rrethin e avatarit
            avatarLabel.setText(
                    admin.getEmri().substring(0, 1).toUpperCase()
            );
        }
    }
    // opsionale nese duhet clock
    private void startClock() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateClock())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        updateClock();
    }

    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();
        if (clockLabel != null) clockLabel.setText(now.format(DateTimeFormatter.ofPattern("HH:mm")));
        if (dateLabel != null) dateLabel.setText(now.format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")));
    }

    private void setupSidebarActions() {
        // Klikimet në sidebar ndryshojnë se çfarë të dhënash shfaqen në TableView
        navDashboard.setOnMouseClicked(e -> loadDashboardData());
        navFluturimet.setOnMouseClicked(e -> loadFluturimetTable());
        navRezervimet.setOnMouseClicked(e -> loadRezervimetTable());
        navAvionet.setOnMouseClicked(e -> loadAvionetTable());
        navHumbur.setOnMouseClicked(e -> loadArtikujtHumburTable());
        navStafi.setOnMouseClicked(e -> loadStafiTable());
        navPasagjeret.setOnMouseClicked(e -> loadPasagjeretTable());
        navKompanite.setOnMouseClicked(e -> loadKompaniteTable());
    }

    private void loadDashboardData() {
        System.out.println("Duke ngarkuar pamjen kryesore të Dashboard...");
        // TODO: Shfaq përmbledhjen e përgjithshme ose logun e modifikimeve
    }

    private void loadFluturimetTable() {
        System.out.println("SELECT * FROM fluturimet...");
        // TODO: Popullo 'adminDataTable' me kolonat dhe të dhënat e fluturimeve
    }

    private void loadRezervimetTable() {
        System.out.println("SELECT * FROM rezervimet...");
        // TODO: Popullo 'adminDataTable' me të dhënat e rezervimeve/pagesave
    }

    private void loadAvionetTable() {
        System.out.println("SELECT * FROM avionet...");
        // TODO: Popullo 'adminDataTable' me avionët dhe mirëmbajtjen
    }

    private void loadArtikujtHumburTable() {
        System.out.println("SELECT * FROM artikujt_humbur...");
        // TODO: Popullo 'adminDataTable' me sendet e raportuara të humbura
    }

    private void loadStafiTable() {
        System.out.println("SELECT * FROM stafi...");
        // TODO: Popullo 'adminDataTable' me menaxhimin e stafit
    }

    private void loadPasagjeretTable() {
        System.out.println("SELECT * FROM pasagjeret...");
        // TODO: Popullo 'adminDataTable' me listën e pasagjerëve
    }

    private void loadKompaniteTable() {
        System.out.println("SELECT * FROM kompanite_ajrore...");
        // TODO: Popullo 'adminDataTable' me kompanitë dhe linjat
    }

// shto, kerko
    @FXML
    private void handleAddRecord() {
        System.out.println("Klikuar butoni për të shtuar rresht të ri (p.sh. Fluturim, Staf, etj.)");
        // TODO: Hap një dritare të re (Modal Pop-up) për regjistrim të dhënash
    }

    @FXML
    private void handleAdminSearch() {
        String query = adminSearchField.getText();
        System.out.println("Duke kërkuar në databazë për: " + query);
        // TODO: Filtro tabelën aktive bazuar në tekstin e shkruar
    }
//logout
    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}