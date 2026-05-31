package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import services.DatabaseService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashboardController {

    @FXML private Label totalFluturimeLabel;
    @FXML private Label totalRezervimeLabel;
    @FXML private Label totalBiletaLabel;
    @FXML private Label totalTeHyraLabel;
    @FXML private Label topDestinacionLabel;
    @FXML private Label avgMbushjaLabel;

    @FXML private BarChart<String, Number> rezervimeChart;
    @FXML private PieChart destinacionetChart;
    @FXML private LineChart<String, Number> teHyratChart;
    @FXML private BarChart<String, Number> statusiChart;

    @FXML private TextArea aiInsightText;

    @FXML
    public void initialize() {
        setupAccessibility();
        loadDashboardData();
    }

    private void setupAccessibility() {
        rezervimeChart.setAccessibleText("Grafiku i rezervimeve sipas muajve");
        destinacionetChart.setAccessibleText("Grafiku i destinacioneve me te kerkuara");
        teHyratChart.setAccessibleText("Grafiku i te hyrave sipas muajve");
        statusiChart.setAccessibleText("Grafiku i statusit te fluturimeve");
        aiInsightText.setAccessibleText("Analize dhe rekomandime automatike nga sistemi AI");
    }

    private void loadDashboardData() {
        try (Connection conn = DatabaseService.getConnection()) {
            int totalFluturime = getInt(conn, "SELECT COUNT(*) FROM fluturimet");
            int totalRezervime = getInt(conn, "SELECT COUNT(*) FROM rezervimet");
            int totalBileta = getInt(conn, "SELECT COUNT(*) FROM biletat");
            double totalTeHyra = getDouble(conn, "SELECT COALESCE(SUM(shuma), 0) FROM pagesat WHERE statusi = 'e_kryer'");
            double avgMbushja = getDouble(conn,
                    "SELECT COALESCE(AVG((kapaciteti_total - vendet_e_lira) / kapaciteti_total * 100), 0) FROM fluturimet");

            totalFluturimeLabel.setText(String.valueOf(totalFluturime));
            totalRezervimeLabel.setText(String.valueOf(totalRezervime));
            totalBiletaLabel.setText(String.valueOf(totalBileta));
            totalTeHyraLabel.setText(String.format("%.2f EUR", totalTeHyra));
            avgMbushjaLabel.setText(String.format("%.1f%%", avgMbushja));

            String topDestinacion = loadTopDestination(conn);
            topDestinacionLabel.setText(topDestinacion);

            loadReservationsChart(conn);
            loadDestinationsChart(conn);
            loadRevenueChart(conn);
            loadStatusChart(conn);
            buildAiInsights(totalRezervime, totalTeHyra, avgMbushja, topDestinacion);
        } catch (SQLException | RuntimeException e) {
            aiInsightText.setText("Nuk u arrit te ngarkohen statistikat nga databaza.\n" + e.getMessage());
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
        String sql = """
                SELECT a.kodi_iata, COUNT(*) AS total
                FROM rezervimet r
                JOIN fluturimet f ON r.id_fluturimit = f.id_fluturimit
                JOIN linjat l ON f.id_linjes = l.id_linjes
                JOIN aeroportet a ON l.id_aeroportit_mbrrritjes = a.id_aeroportit
                GROUP BY a.kodi_iata
                ORDER BY total DESC
                LIMIT 1
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("kodi_iata") + " (" + rs.getInt("total") + ")";
            }
        }
        return "N/A";
    }

    private void loadReservationsChart(Connection conn) throws SQLException {
        rezervimeChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Rezervime");

        String sql = """
                SELECT DATE_FORMAT(data_rezervimit, '%Y-%m') AS muaji, COUNT(*) AS total
                FROM rezervimet
                GROUP BY muaji
                ORDER BY muaji
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("muaji"), rs.getInt("total")));
            }
        }

        rezervimeChart.getData().add(series);
    }

    private void loadDestinationsChart(Connection conn) throws SQLException {
        destinacionetChart.setData(FXCollections.observableArrayList());

        String sql = """
                SELECT a.kodi_iata, COUNT(*) AS total
                FROM rezervimet r
                JOIN fluturimet f ON r.id_fluturimit = f.id_fluturimit
                JOIN linjat l ON f.id_linjes = l.id_linjes
                JOIN aeroportet a ON l.id_aeroportit_mbrrritjes = a.id_aeroportit
                GROUP BY a.kodi_iata
                ORDER BY total DESC
                LIMIT 6
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                destinacionetChart.getData().add(
                        new PieChart.Data(rs.getString("kodi_iata"), rs.getInt("total"))
                );
            }
        }
    }

    private void loadRevenueChart(Connection conn) throws SQLException {
        teHyratChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Te hyra");

        String sql = """
                SELECT DATE_FORMAT(data_pageses, '%Y-%m') AS muaji, COALESCE(SUM(shuma), 0) AS total
                FROM pagesat
                WHERE statusi = 'e_kryer'
                GROUP BY muaji
                ORDER BY muaji
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("muaji"), rs.getDouble("total")));
            }
        }

        teHyratChart.getData().add(series);
    }

    private void loadStatusChart(Connection conn) throws SQLException {
        statusiChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Fluturime");

        String sql = """
                SELECT statusi, COUNT(*) AS total
                FROM fluturimet
                GROUP BY statusi
                ORDER BY total DESC
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                series.getData().add(new XYChart.Data<>(rs.getString("statusi"), rs.getInt("total")));
            }
        }

        statusiChart.getData().add(series);
    }

    private void buildAiInsights(int totalRezervime, double totalTeHyra, double avgMbushja, String topDestinacion) {
        StringBuilder insight = new StringBuilder();

        insight.append("AI Insight / BI Analysis\n\n");
        insight.append("- Destinacioni me i kerkuar eshte ").append(topDestinacion).append(".\n");

        if (avgMbushja >= 75) {
            insight.append("- Mbushja mesatare e fluturimeve eshte e larte. Rekomandohet shtimi i kapaciteteve ose fluturimeve shtese.\n");
        } else if (avgMbushja >= 45) {
            insight.append("- Mbushja mesatare eshte stabile. Rekomandohet monitorim i destinacioneve kryesore.\n");
        } else {
            insight.append("- Mbushja mesatare eshte e ulet. Rekomandohet oferte promocionale per destinacionet me pak te kerkuara.\n");
        }

        if (totalRezervime > 0 && totalTeHyra > 0) {
            insight.append("- Vlera mesatare per rezervim eshte rreth ")
                    .append(String.format("%.2f EUR", totalTeHyra / totalRezervime))
                    .append(".\n");
        }

        insight.append("- Dashboard-i perdor te dhena BI nga rezervimet, pagesat, fluturimet dhe destinacionet.");
        aiInsightText.setText(insight.toString());
    }

    @FXML
    private void handleRefresh() {
        loadDashboardData();
    }

    @FXML
    private void handleNavDashboard() {
        Router.navigateTo(ViewsEnum.ADMIN_VIEW);
    }

    @FXML
    private void handleNavFluturimet() {
        Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET);
    }

    @FXML
    private void handleNavRezervimet() {
        Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET);
    }

    @FXML
    private void handleNavAvionet() {
        Router.navigateTo(ViewsEnum.ADMIN_AVIONET);
    }

    @FXML
    private void handleNavHumbur() {
        Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR);
    }

    @FXML
    private void handleNavStafi() {
        Router.navigateTo(ViewsEnum.ADMIN_STAFI);
    }

    @FXML
    private void handleNavPasagjeret() {
        Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT);
    }
    @FXML
    private void handleNavKompanite() {
        Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE);
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}
