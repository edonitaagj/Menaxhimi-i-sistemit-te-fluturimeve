package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Perdoruesi;
import models.dto.FluturimiTabelaDTO;
import repository.AdminRepository;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AdminController {

    // ===============================
    // DASHBOARD KPI
    // ===============================
    @FXML private Label totalFlightsLabel;
    @FXML private Label lostItemsLabel;
    @FXML private Label totalReservationsLabel;
    @FXML private Label totalTicketsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private Label avgOccupancyLabel;
    @FXML private Label topDestinationLabel;

    // ===============================
    // BI & AI
    // ===============================
    @FXML private BarChart<String, Number> reservationsChart;
    @FXML private PieChart destinationsChart;
    @FXML private LineChart<String, Number> revenueChart;
    @FXML private BarChart<String, Number> flightStatusChart;
    @FXML private TextArea aiInsightText;

    // ===============================
    // ADMIN INFO
    // ===============================
    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

    // ===============================
    // SEARCH & TABLE
    // ===============================
    @FXML private TextField adminSearchField;
    @FXML private TableView<FluturimiTabelaDTO> adminDataTable;

    // ===============================
    // SIDEBAR
    // ===============================
    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    private final ObservableList<FluturimiTabelaDTO> listaFluturimeve = FXCollections.observableArrayList();
    private final AdminRepository adminRepository = new AdminRepository();

    @FXML
    public void initialize() {
        loadAdminData();
        setupAccessibility();
        setupSidebarActions();
        initTableColumns();
        loadDashboardData();

        adminSearchField.textProperty().addListener((observable, oldValue, newValue) -> handleAdminSearch());
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

    private void setupAccessibility() {
        adminSearchField.setAccessibleText("Kerko fluturime sipas kodit, kompanise, nisjes ose destinacionit");
        adminDataTable.setAccessibleText("Tabela administrative me fluturime");
        reservationsChart.setAccessibleText("Grafiku BI i rezervimeve sipas muajve");
        destinationsChart.setAccessibleText("Grafiku BI i destinacioneve me te kerkuara");
        revenueChart.setAccessibleText("Grafiku BI i te hyrave sipas muajve");
        flightStatusChart.setAccessibleText("Grafiku BI i statusit te fluturimeve");
        aiInsightText.setAccessibleText("Paneli AI me analiza dhe rekomandime automatike");
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
            columns.get(3).setCellValueFactory(new PropertyValueFactory<>("nisja"));
            columns.get(4).setCellValueFactory(new PropertyValueFactory<>("mberritja"));
            columns.get(5).setCellValueFactory(new PropertyValueFactory<>("dataOra"));
            columns.get(6).setCellValueFactory(new PropertyValueFactory<>("statusi"));
        }

        adminDataTable.setItems(listaFluturimeve);
    }

    private void loadDashboardData() {
        listaFluturimeve.clear();

        List<FluturimiTabelaDTO> fluturimetNgaDb = adminRepository.getFluturimetDashboard();
        listaFluturimeve.addAll(fluturimetNgaDb);
        adminDataTable.setItems(listaFluturimeve);

        int totalFlights = adminRepository.getTotalFlightsCount();
        int totalLostItems = adminRepository.getLostItemsCount();

        totalFlightsLabel.setText(String.valueOf(totalFlights));
        lostItemsLabel.setText(String.valueOf(totalLostItems));

        try (Connection conn = DatabaseService.getConnection()) {
            int totalReservations = getInt(conn, "SELECT COUNT(*) FROM rezervimet");
            int totalTickets = getInt(conn, "SELECT COUNT(*) FROM biletat");
            double totalRevenue = getDouble(conn, "SELECT COALESCE(SUM(shuma), 0) FROM pagesat WHERE statusi = 'e_kryer'");
            double avgOccupancy = getDouble(conn, "SELECT COALESCE(AVG((kapaciteti_total - vendet_e_lira) / kapaciteti_total * 100), 0) FROM fluturimet");
            String topDestination = loadTopDestination(conn);

            totalReservationsLabel.setText(String.valueOf(totalReservations));
            totalTicketsLabel.setText(String.valueOf(totalTickets));
            totalRevenueLabel.setText(String.format("%.2f EUR", totalRevenue));
            avgOccupancyLabel.setText(String.format("%.1f%%", avgOccupancy));
            topDestinationLabel.setText(topDestination);

            loadReservationsChart(conn);
            loadDestinationsChart(conn);
            loadRevenueChart(conn);
            loadFlightStatusChart(conn);
            buildAiInsights(totalReservations, totalRevenue, avgOccupancy, topDestination);
        } catch (SQLException | RuntimeException e) {
            aiInsightText.setText("Nuk u arrit te ngarkohen statistikat BI/AI nga databaza.\n" + e.getMessage());
        }
    }

    private int getInt(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private double getDouble(Connection conn, String sql) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    private String loadTopDestination(Connection conn) throws SQLException {
        String sql = "SELECT a.kodi_iata, COUNT(*) AS total " +
                "FROM rezervimet r " +
                "JOIN fluturimet f ON r.id_fluturimit = f.id_fluturimit " +
                "JOIN linjat l ON f.id_linjes = l.id_linjes " +
                "JOIN aeroportet a ON l.id_aeroportit_mbrrritjes = a.id_aeroportit " +
                "GROUP BY a.kodi_iata " +
                "ORDER BY total DESC " +
                "LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("kodi_iata") + " (" + rs.getInt("total") + ")";
            }
        }
        return "N/A";
    }

    private void loadReservationsChart(Connection conn) throws SQLException {
        reservationsChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rezervime");

        String sql = "SELECT DATE_FORMAT(data_rezervimit, '%Y-%m') AS muaji, COUNT(*) AS total " +
                "FROM rezervimet GROUP BY muaji ORDER BY muaji";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("muaji"), rs.getInt("total")));
            }
        }

        reservationsChart.getData().add(series);
    }

    private void loadDestinationsChart(Connection conn) throws SQLException {
        destinationsChart.setData(FXCollections.observableArrayList());

        String sql = "SELECT a.kodi_iata, COUNT(*) AS total " +
                "FROM rezervimet r " +
                "JOIN fluturimet f ON r.id_fluturimit = f.id_fluturimit " +
                "JOIN linjat l ON f.id_linjes = l.id_linjes " +
                "JOIN aeroportet a ON l.id_aeroportit_mbrrritjes = a.id_aeroportit " +
                "GROUP BY a.kodi_iata " +
                "ORDER BY total DESC " +
                "LIMIT 6";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                destinationsChart.getData().add(new PieChart.Data(rs.getString("kodi_iata"), rs.getInt("total")));
            }
        }
    }

    private void loadRevenueChart(Connection conn) throws SQLException {
        revenueChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Te hyra");

        String sql = "SELECT DATE_FORMAT(data_pageses, '%Y-%m') AS muaji, COALESCE(SUM(shuma), 0) AS total " +
                "FROM pagesat WHERE statusi = 'e_kryer' GROUP BY muaji ORDER BY muaji";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("muaji"), rs.getDouble("total")));
            }
        }

        revenueChart.getData().add(series);
    }

    private void loadFlightStatusChart(Connection conn) throws SQLException {
        flightStatusChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Fluturime");

        String sql = "SELECT statusi, COUNT(*) AS total FROM fluturimet GROUP BY statusi ORDER BY total DESC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("statusi"), rs.getInt("total")));
            }
        }

        flightStatusChart.getData().add(series);
    }

    private void buildAiInsights(int totalReservations, double totalRevenue, double avgOccupancy, String topDestination) {
        StringBuilder insight = new StringBuilder();
        insight.append("AI Insight / BI Analysis\n\n");
        insight.append("- Destinacioni me i kerkuar eshte ").append(topDestination).append(".\n");

        if (avgOccupancy >= 75) {
            insight.append("- Mbushja mesatare eshte e larte. Rekomandohet shtimi i kapaciteteve ose fluturimeve shtese.\n");
        } else if (avgOccupancy >= 45) {
            insight.append("- Mbushja mesatare eshte stabile. Rekomandohet monitorim i destinacioneve kryesore.\n");
        } else {
            insight.append("- Mbushja mesatare eshte e ulet. Rekomandohet oferte promocionale per destinacionet me pak te kerkuara.\n");
        }

        if (totalReservations > 0 && totalRevenue > 0) {
            insight.append("- Vlera mesatare per rezervim eshte rreth ")
                    .append(String.format("%.2f EUR", totalRevenue / totalReservations))
                    .append(".\n");
        }

        insight.append("- Dashboard-i perdor te dhena BI nga rezervimet, pagesat, fluturimet dhe destinacionet.");
        aiInsightText.setText(insight.toString());
    }

    private void loadFluturimetTable() {
        Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET);
    }

    private void loadRezervimetTable() {
        Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET);
    }

    private void loadAvionetTable() {
        Router.navigateTo(ViewsEnum.ADMIN_AVIONET);
    }

    private void loadArtikujtHumburTable() {
        Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR);
    }

    private void loadStafiTable() {
        Router.navigateTo(ViewsEnum.ADMIN_STAFI);
    }

    private void loadPasagjeretTable() {
        Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT);
    }

    private void loadKompaniteTable() {
        Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE);
    }

    @FXML
    private void handleAdminSearch() {
        String query = adminSearchField.getText() == null ? "" : adminSearchField.getText().toLowerCase().trim();

        if (query.isEmpty()) {
            adminDataTable.setItems(listaFluturimeve);
            return;
        }

        ObservableList<FluturimiTabelaDTO> filteredList = FXCollections.observableArrayList();
        for (FluturimiTabelaDTO f : listaFluturimeve) {
            if (contains(f.getKodiFluturimit(), query) ||
                    contains(f.getNisja(), query) ||
                    contains(f.getMberritja(), query) ||
                    contains(f.getEmriKompanise(), query)) {
                filteredList.add(f);
            }
        }
        adminDataTable.setItems(filteredList);
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}
