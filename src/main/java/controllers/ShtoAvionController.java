package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dto.AvionetRequestDto;
import models.dto.LookupDto;
import services.AvionetService;

public class ShtoAvionController {

    @FXML private ComboBox<LookupDto> cmbKompania;
    @FXML private ComboBox<LookupDto> cmbLloji;
    @FXML private TextField txtNumriRegjistrit;
    @FXML private TextField txtVitiProdhimit;
    @FXML private ComboBox<String> cmbStatusi;
    @FXML private Button btnRuaj;
    @FXML private Button btnAnulo;

    private final AvionetService avionetService = new AvionetService();

    @FXML
    public void initialize() {
        cmbKompania.setItems(FXCollections.observableArrayList(avionetService.getKompanite()));
        cmbLloji.setItems(FXCollections.observableArrayList(avionetService.getLlojet()));
        cmbStatusi.setItems(FXCollections.observableArrayList("aktiv", "mirembajtje", "jashte_sherbimit"));

        if (!cmbStatusi.getItems().isEmpty()) {
            cmbStatusi.getSelectionModel().selectFirst();
        }

        btnRuaj.setOnAction(e -> handleRuaj());
        btnAnulo.setOnAction(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
    }

    private void handleRuaj() {
        LookupDto kompania = cmbKompania.getValue();
        LookupDto lloji = cmbLloji.getValue();
        String numri = txtNumriRegjistrit.getText();
        String vitiText = txtVitiProdhimit.getText();
        String statusi = cmbStatusi.getValue();

        if (kompania == null || lloji == null || numri == null || numri.trim().isEmpty()) {
            showAlert("Plotëso fushat kryesore", "Duhet të zgjedhësh kompaninë, llojin dhe numrin e regjistrit.");
            return;
        }

        Integer viti = null;
        if (vitiText != null && !vitiText.trim().isEmpty()) {
            try {
                viti = Integer.parseInt(vitiText.trim());
            } catch (NumberFormatException ex) {
                showAlert("Viti i prodhimit", "Viti duhet të jetë numër, p.sh. 2019.");
                return;
            }
        }

        AvionetRequestDto dto = new AvionetRequestDto();
        dto.setIdKompanise(kompania.getId());
        dto.setIdLlojit(lloji.getId());
        dto.setNumriRegjistrit(numri.trim().toUpperCase());
        dto.setVitiProdhimit(viti);
        dto.setStatusi(statusi);

        boolean sukses = avionetService.saveAvion(dto);
        if (sukses) {
            showAlert("Sukses", "Avioni u regjistrua me sukses.");
            Router.navigateTo(ViewsEnum.ADMIN_AVIONET);
        } else {
            showAlert("Gabim", "Regjistrimi dështoi. Kontrollo numrin e regjistrit ose lidhjet me DB.");
        }
    }

    private void showAlert(String title, String message) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}