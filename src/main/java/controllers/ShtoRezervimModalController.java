package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dto.FluturimSelectDto;
import services.RezervimetService;

import java.util.List;
import java.util.Random;

public class ShtoRezervimModalController {

    @FXML private ComboBox<FluturimSelectDto> cmbFluturimi;
    @FXML private ComboBox<String> cmbKlasa;
    @FXML private TextField txtCmimiTotal;
    @FXML private TextField txtMonedha;
    @FXML private TextArea txtShenimet;

    private final RezervimetService rezervimetService = new RezervimetService();

    @FXML
    private void initialize() {
        loadFlights();

        cmbKlasa.setItems(FXCollections.observableArrayList("ekonomike", "biznes", "first"));
        cmbKlasa.getSelectionModel().select("ekonomike");

        txtMonedha.setText("EUR");
        txtCmimiTotal.setText("0.00");

        cmbFluturimi.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
        cmbKlasa.valueProperty().addListener((obs, oldVal, newVal) -> updatePrice());
    }

    private void loadFlights() {
        List<FluturimSelectDto> flights = rezervimetService.getAvailableFlights();
        cmbFluturimi.setItems(FXCollections.observableArrayList(flights));

        if (!flights.isEmpty()) {
            cmbFluturimi.getSelectionModel().selectFirst();
        }

        updatePrice();
    }

    private void updatePrice() {
        FluturimSelectDto fluturimi = cmbFluturimi.getValue();
        String klasa = cmbKlasa.getValue();

        if (fluturimi == null || klasa == null) {
            txtCmimiTotal.setText("0.00");
            return;
        }

        double base = (fluturimi.getDistancaKm() == null || fluturimi.getDistancaKm() <= 0)
                ? 35.0
                : Math.max(35.0, fluturimi.getDistancaKm() * 0.08);

        double multiplier = switch (klasa.toLowerCase()) {
            case "biznes" -> 1.60;
            case "first" -> 2.30;
            default -> 1.00;
        };

        double total = Math.round(base * multiplier * 100.0) / 100.0;
        txtCmimiTotal.setText(String.format("%.2f", total));
    }

    @FXML
    private void handleKalojTePagesa() {
        FluturimSelectDto fluturimi = cmbFluturimi.getValue();
        String klasa = cmbKlasa.getValue();
        String shenimet = txtShenimet.getText() != null ? txtShenimet.getText().trim() : "";

        if (fluturimi == null) {
            showAlert(Alert.AlertType.WARNING, "Zgjidhni një fluturim.");
            return;
        }

        if (klasa == null || klasa.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Zgjidhni klasën e fluturimit.");
            return;
        }

        // NDRYSHIMI KRYESOR: Nuk e thërrasim DB-në këtu!
        // Gjenerojmë një kod të përkohshëm sa për ta treguar në dizajnin FXML të pagesës
        String kodiPekohshem = "PND-" + (10000 + new Random().nextInt(90000));
        String cmimi = txtCmimiTotal.getText();

        // 1. Kalojmë të dhënat e përzgjedhura në mënyrë statike te faqja e pagesës
        PagesaModalController.kaloniTeDhenatERezervimit(fluturimi, klasa, shenimet, cmimi, kodiPekohshem);

        // 2. Navigojmë te pamja e pagesave
        Router.navigateTo(ViewsEnum.PAGESAT_VIEW);

        // 3. Fshehim dritaren modal të hapur
        if (txtCmimiTotal != null && txtCmimiTotal.getScene() != null) {
            txtCmimiTotal.getScene().getWindow().hide();
        }
    }

    @FXML
    private void handleAnulo() {
        if (txtCmimiTotal != null && txtCmimiTotal.getScene() != null) {
            txtCmimiTotal.getScene().getWindow().hide();
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(type == Alert.AlertType.ERROR ? "Gabim" : "Njoftim");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}