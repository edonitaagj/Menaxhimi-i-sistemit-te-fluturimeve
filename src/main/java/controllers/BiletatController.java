package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dto.BiletaTableDto;
import services.BiletatService;

import java.util.List;

public class BiletatController {

    @FXML private TextField txtNumriBiletes;
    @FXML private TextField txtVendiUljes;
    @FXML private CheckBox chkCheckedIn;
    @FXML private Button btnKryejCheckIn;

    @FXML private TableView<BiletaTableDto> tblBiletat;
    @FXML private TableColumn<BiletaTableDto, Integer> colIdBiletes;
    @FXML private TableColumn<BiletaTableDto, String> colNumriBiletes;
    @FXML private TableColumn<BiletaTableDto, String> colVendiUljes;
    @FXML private TableColumn<BiletaTableDto, String> colKlasaUljes;
    @FXML private TableColumn<BiletaTableDto, Double> colCmimi;
    @FXML private TableColumn<BiletaTableDto, Double> colTaksa;
    @FXML private TableColumn<BiletaTableDto, String> colCheckedIn;
    @FXML private TableColumn<BiletaTableDto, String> colStatusiBiletes;

    private final BiletatService biletatService = new BiletatService();
    private BiletaTableDto biletaESelektuar;

    @FXML
    private void initialize() {
        colIdBiletes.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("idBiletes"));
        colNumriBiletes.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numriBiletes"));
        colVendiUljes.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("vendiUljes"));
        colKlasaUljes.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("klasaUljes"));
        colCmimi.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("cmimi"));
        colTaksa.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("taksa"));

        colCheckedIn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() == null ? "" : cell.getValue().getCheckedInStatusText())
        );
        colStatusiBiletes.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue() == null ? "" : cell.getValue().getStatusiBiletes())
        );

        txtNumriBiletes.setEditable(false);
        tblBiletat.setPlaceholder(new Label("Nuk u gjetën bileta për përdoruesin aktual."));

        loadBiletatData();

        tblBiletat.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            biletaESelektuar = newSelection;

            if (newSelection != null) {
                txtNumriBiletes.setText(newSelection.getNumriBiletes());
                txtVendiUljes.setText(newSelection.getVendiUljes());
                chkCheckedIn.setSelected(newSelection.isCheckedIn());
            } else {
                pastroFormen();
            }
        });

        btnKryejCheckIn.setOnAction(e -> handleKryejCheckIn());
    }

    private void loadBiletatData() {
        List<BiletaTableDto> biletat = biletatService.getMyaTickets();
        tblBiletat.setItems(FXCollections.observableArrayList(biletat));

        if (biletat.isEmpty()) {
            tblBiletat.setPlaceholder(new Label("Nuk u gjetën bileta për përdoruesin aktual."));
        }
    }

    private void handleKryejCheckIn() {
        if (biletaESelektuar == null) {
            showAlert(Alert.AlertType.WARNING, "Kujdes", "Ju lutem selektoni një biletë nga tabela.");
            return;
        }

        String ulësja = txtVendiUljes.getText();
        boolean checkedStatus = chkCheckedIn.isSelected();

        if (ulësja == null || ulësja.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Fushë e zbrazët", "Ju lutem specifikoni ulësen, p.sh. 14A.");
            return;
        }

        boolean sukses = biletatService.kryejCheckIn(
                biletaESelektuar.getIdBiletes(),
                ulësja,
                checkedStatus
        );

        if (sukses) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Statusi i biletës u përditësua me sukses.");
            loadBiletatData();
            pastroFormen();
        } else {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Përditësimi dështoi. Kontrolloni të dhënat.");
        }
    }

    private void pastroFormen() {
        tblBiletat.getSelectionModel().clearSelection();
        txtNumriBiletes.clear();
        txtVendiUljes.clear();
        chkCheckedIn.setSelected(false);
        biletaESelektuar = null;
    }

    @FXML private void handleNavHome() { Router.navigateTo(ViewsEnum.HOME_VIEW); }
    @FXML private void handleNavRezervimet() { Router.navigateTo(ViewsEnum.REZERVIMET_VIEW); }
    @FXML private void handleNavNjoftimet() { Router.navigateTo(ViewsEnum.NJOFTIMET_VIEW); }
    @FXML private void handleNavProfili() { Router.navigateTo(ViewsEnum.PROFIL_VIEW); }



    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}