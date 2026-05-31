package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.ArtikujtHumbur;
import models.dto.ArtikullHumburRequestDto;
import services.ArtikujtHumburService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminHumburController implements Initializable {

    @FXML private TextField txtArtikulli;
    @FXML private TextField txtVendi;
    @FXML private TextField txtSearchHumbur;
    @FXML private DatePicker dtDataGjetjes;
    @FXML private TextArea txtPershkrimi;
    @FXML private ComboBox<String> cmbStatusiHumbur;

    @FXML private TableView<ArtikujtHumbur> tblArtikujt;
    @FXML private TableColumn<ArtikujtHumbur, Integer> colHumburId;
    @FXML private TableColumn<ArtikujtHumbur, String> colArtikulli;
    @FXML private TableColumn<ArtikujtHumbur, String> colVendi;
    @FXML private TableColumn<ArtikujtHumbur, String> colData;
    @FXML private TableColumn<ArtikujtHumbur, String> colStatusiHumbur;

    private final ArtikujtHumburService service = new ArtikujtHumburService();
    private final ObservableList<ArtikujtHumbur> lista = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupTable();
        setupComboBox();
        setupSearch();
        loadData();

        if (dtDataGjetjes != null) {
            dtDataGjetjes.setValue(LocalDate.now());
        }
    }

    private void setupTable() {
        colHumburId.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cellData.getValue() != null ? cellData.getValue().getIdArtikullit() : 0
                ).asObject()
        );

        colArtikulli.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue() != null ? cellData.getValue().getPershkrimi() : ""
                )
        );

        colVendi.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue() != null ? cellData.getValue().getVendiGjetjes() : ""
                )
        );

        colData.setCellValueFactory(cellData -> {
            ArtikujtHumbur item = cellData.getValue();
            if (item != null && item.getDataGjetjes() != null) {
                return new javafx.beans.property.SimpleStringProperty(item.getDataGjetjes().toString());
            }
            return new javafx.beans.property.SimpleStringProperty("");
        });

        colStatusiHumbur.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue() != null ? cellData.getValue().getStatusi() : ""
                )
        );

        colHumburId.setStyle("-fx-alignment: CENTER;");
        colStatusiHumbur.setStyle("-fx-alignment: CENTER;");

        tblArtikujt.setItems(lista);
    }

    private void setupComboBox() {
        if (cmbStatusiHumbur == null) return;

        cmbStatusiHumbur.getItems().setAll(
                "i_raportuar",
                "i_gjetur",
                "i_kthyer",
                "i_asgjësuar"
        );
        cmbStatusiHumbur.setValue("i_raportuar");
    }

    private void setupSearch() {
        if (txtSearchHumbur == null) return;

        txtSearchHumbur.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isBlank()) {
                loadData();
            } else {
                try {
                    lista.setAll(service.search(newVal));
                } catch (Exception e) {
                    showAlert("Gabim", e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        });
    }

    private void loadData() {
        try {
            lista.setAll(service.getAll());
        } catch (Exception e) {
            showAlert("Gabim", "Nuk u ngarkuan të dhënat: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleSaveArtikull() {
        if (txtArtikulli.getText() == null || txtArtikulli.getText().trim().isEmpty()
                || txtVendi.getText() == null || txtVendi.getText().trim().isEmpty()
                || dtDataGjetjes.getValue() == null
                || txtPershkrimi.getText() == null || txtPershkrimi.getText().trim().isEmpty()) {

            showAlert("Gabim", "Ju lutemi plotësoni fushat e detyrueshme!", Alert.AlertType.ERROR);
            return;
        }

        try {
            ArtikullHumburRequestDto dto = new ArtikullHumburRequestDto();
            dto.setArtikulli(txtArtikulli.getText().trim());
            dto.setVendi(txtVendi.getText().trim());
            dto.setDataGjetjes(dtDataGjetjes.getValue().toString());
            dto.setPershkrimi(txtPershkrimi.getText().trim());
            dto.setStatusi(cmbStatusiHumbur.getValue());

            ArtikujtHumbur saved = service.save(dto);

            showAlert(
                    "Sukses",
                    "Artikulli u regjistrua me sukses! ID: " + saved.getIdArtikullit(),
                    Alert.AlertType.INFORMATION
            );

            clearForm();
            loadData();

        } catch (Exception e) {
            showAlert("Gabim", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void clearForm() {
        txtArtikulli.clear();
        txtVendi.clear();
        txtPershkrimi.clear();
        dtDataGjetjes.setValue(LocalDate.now());
        if (cmbStatusiHumbur != null) {
            cmbStatusiHumbur.setValue("i_raportuar");
        }
    }

    private void showAlert(String title, String msg, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}