package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import models.Perdoruesi;
import models.dto.FluturimiTabelaDTO;
import repository.AdminRepository;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AdminController {
    @FXML private Label totalFlightsLabel;
    @FXML private Label lostItemsLabel;

    // ===============================
    // ADMIN INFO (Sidebar bottom)
    // ===============================
    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

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
    // TABLE VIEW NDRYSHUAR NË DTO
    // ===============================
    @FXML private TableView<FluturimiTabelaDTO> adminDataTable;

    // Krijojmë një listë të vëzhgueshme (ObservableList) për tabelën
    private final ObservableList<FluturimiTabelaDTO> listaFluturimeve = FXCollections.observableArrayList();
    private final AdminRepository adminRepository = new AdminRepository();

    // ===============================
    // INIT
    // ===============================
    @FXML
    public void initialize() {
        loadAdminData();
        setupSidebarActions();

        // Konfigurojmë kolonat e tabelës që të përputhen me FXML
        initTableColumns();

        // Ngarkon automatikisht fluturimet sapo hapet kryefaqja
        loadDashboardData();

        // Dëgjuesi (Listener) për kërkim live gjatë shkrimit
        adminSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            handleAdminSearch();
        });
    }

    private void loadAdminData() {
        Perdoruesi admin = SessionManager.getCurrentUser();
        if (admin != null) {
            String fullName = admin.getEmri() + " " + admin.getMbiemri();
            userFullName.setText(fullName);
            userEmail.setText(admin.getEmail());
            avatarLabel.setText(admin.getEmri().substring(0, 1).toUpperCase());
        }
    }

    private void setupSidebarActions() {
        navDashboard.setOnMouseClicked(e -> loadDashboardData());
        navFluturimet.setOnMouseClicked(e -> loadFluturimetTable());
        navRezervimet.setOnMouseClicked(e -> loadRezervimetTable());
        navAvionet.setOnMouseClicked(e -> loadAvionetTable());
        navHumbur.setOnMouseClicked(e -> loadArtikujtHumburTable());
        navStafi.setOnMouseClicked(e -> loadStafiTable());
        navPasagjeret.setOnMouseClicked(e -> loadPasagjeretTable());
        navKompanite.setOnMouseClicked(e -> loadKompaniteTable());
    }

    private void initTableColumns() {
        ObservableList<TableColumn<FluturimiTabelaDTO, ?>> columns = adminDataTable.getColumns();

        if (columns.size() >= 7) {
            columns.get(0).setCellValueFactory(new PropertyValueFactory<>("idFluturimit"));
            columns.get(1).setCellValueFactory(new PropertyValueFactory<>("kodiFluturimit"));
            columns.get(2).setCellValueFactory(new PropertyValueFactory<>("emriKompanise"));
            columns.get(3).setCellValueFactory(new PropertyValueFactory<>("nisja"));       // Kolona e re: Nisja
            columns.get(4).setCellValueFactory(new PropertyValueFactory<>("mberritja"));   // Kolona e re: Mbërritja
            columns.get(5).setCellValueFactory(new PropertyValueFactory<>("dataOra"));
            columns.get(6).setCellValueFactory(new PropertyValueFactory<>("statusi"));

            // Kolona 7 (Veprimet) mbetet për butonat e modifikimit
        }

        adminDataTable.setItems(listaFluturimeve);
    }

    private void loadDashboardData() {
        System.out.println("Duke ngarkuar fluturimet dhe statistikat në Dashboard...");
        listaFluturimeve.clear();

        // 1. Mbush tabelën (Kodi që bëmë herën e kaluar)
        List<FluturimiTabelaDTO> fluturimetNgaDb = adminRepository.getFluturimetDashboard();
        listaFluturimeve.addAll(fluturimetNgaDb);

        // 2. Merr numrat live nga DB për kartat e statistikave
        int totalFlights = adminRepository.getTotalFlightsCount();
        int totalLostItems = adminRepository.getLostItemsCount();

        // 3. Vendos tekstin dinamikisht në UI
        totalFlightsLabel.setText(String.valueOf(totalFlights));
        lostItemsLabel.setText(String.valueOf(totalLostItems));
    }

    private void loadFluturimetTable() {
        // Nëse ke pamje të veçantë për fluturimet
        Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET);
    }

    private void loadRezervimetTable() { Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET); }
    private void loadAvionetTable() { Router.navigateTo(ViewsEnum.ADMIN_AVIONET); }
    private void loadArtikujtHumburTable() { Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR); }
    private void loadStafiTable() { Router.navigateTo(ViewsEnum.ADMIN_STAFI); }
    private void loadPasagjeretTable() { Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT); }
    private void loadKompaniteTable() { Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE); }

    @FXML
    private void handleAdminSearch() {
        String query = adminSearchField.getText() == null ? "" : adminSearchField.getText().toLowerCase().trim();

        if (query.isEmpty()) {
            adminDataTable.setItems(listaFluturimeve);
            return;
        }

        ObservableList<FluturimiTabelaDTO> filteredList = FXCollections.observableArrayList();
        for (FluturimiTabelaDTO f : listaFluturimeve) {
            if (f.getKodiFluturimit().toLowerCase().contains(query) ||
                    f.getNisja().toLowerCase().contains(query) ||
                    f.getMberritja().toLowerCase().contains(query) ||
                    f.getEmriKompanise().toLowerCase().contains(query)) {
                filteredList.add(f);
            }
        }
        adminDataTable.setItems(filteredList);
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}