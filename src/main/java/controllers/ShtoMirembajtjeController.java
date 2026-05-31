package controllers;

import app.AdminSelectionState;
import app.Router;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.dto.AvionetTableDto;
import models.dto.LookupDto;
import models.dto.MirembajtjaRequestDto;
import models.dto.StaffLookupDto;
import services.AvionetService;
import services.MirembajtjaService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class ShtoMirembajtjeController {

    @FXML private ComboBox<AvionetTableDto> cmbAvioni;
    @FXML private ComboBox<LookupDto> cmbLlojiSherbimit;
    @FXML private DatePicker dpDataFillimit;
    @FXML private TextField txtOraFillimit;
    @FXML private DatePicker dpDataMbarimit;
    @FXML private TextField txtOraMbarimit;
    @FXML private TextArea txtPershkrimiPunes;
    @FXML private TextField txtKostoja;
    @FXML private ComboBox<StaffLookupDto> cmbStafi;
    @FXML private ComboBox<LookupDto> cmbStatusi;
    @FXML private Button btnRuaj;
    @FXML private Button btnAnulo;

    private final AvionetService avionetService = new AvionetService();
    private final MirembajtjaService mirembajtjaService = new MirembajtjaService();

    @FXML
    public void initialize() {
        cmbAvioni.setItems(FXCollections.observableArrayList(avionetService.getAllAvionet()));
        cmbLlojiSherbimit.setItems(FXCollections.observableArrayList(mirembajtjaService.getLlojetSherbimit()));
        cmbStafi.setItems(FXCollections.observableArrayList(mirembajtjaService.getStafi()));
        cmbStatusi.setItems(FXCollections.observableArrayList(mirembajtjaService.getStatusetMirembajtjes()));

        if (!cmbStatusi.getItems().isEmpty()) {
            cmbStatusi.getSelectionModel().selectFirst();
        }

        Integer selectedAvionId = AdminSelectionState.getSelectedAvionId();
        if (selectedAvionId != null) {
            cmbAvioni.getItems().stream()
                    .filter(a -> a.getIdAvionit() == selectedAvionId)
                    .findFirst()
                    .ifPresent(a -> cmbAvioni.getSelectionModel().select(a));
        }

        btnRuaj.setOnAction(e -> handleRuaj());
        btnAnulo.setOnAction(e -> {
            AdminSelectionState.clear();
            Router.navigateTo(ViewsEnum.ADMIN_AVIONET);
        });
    }

    private void handleRuaj() {
        AvionetTableDto avion = cmbAvioni.getValue();
        LookupDto lloji = cmbLlojiSherbimit.getValue();
        StaffLookupDto stafi = cmbStafi.getValue();
        LookupDto statusi = cmbStatusi.getValue();

        if (avion == null || lloji == null || stafi == null || dpDataFillimit.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Fushat e obligueshme", "Zgjidh avionin, llojin e shërbimit, stafin dhe datën e fillimit.");
            return;
        }

        LocalDateTime start = composeDateTime(dpDataFillimit.getValue(), txtOraFillimit.getText(), true);
        if (start == null) {
            return;
        }

        LocalDateTime end = null;
        if (dpDataMbarimit.getValue() != null || (txtOraMbarimit.getText() != null && !txtOraMbarimit.getText().trim().isEmpty())) {
            end = composeDateTime(dpDataMbarimit.getValue(), txtOraMbarimit.getText(), false);
            if (end == null) {
                return;
            }
        }

        Double kostoja = null;
        String kostoText = txtKostoja.getText();
        if (kostoText != null && !kostoText.trim().isEmpty()) {
            try {
                kostoja = Double.parseDouble(kostoText.trim().replace(",", "."));
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Kostoja", "Kostoja duhet të jetë numër, p.sh. 120.50");
                return;
            }
        }

        MirembajtjaRequestDto dto = new MirembajtjaRequestDto();
        dto.setIdAvionit(avion.getIdAvionit());
        dto.setLlojiSherbimit(lloji.getLabel());
        dto.setDataFillimit(start);
        dto.setDataMbarimit(end);
        dto.setPershkrimiPunes(txtPershkrimiPunes.getText());
        dto.setKostoja(kostoja);
        dto.setIdStafitPergjegjes(stafi.getId());
        dto.setStatusi(statusi != null ? statusi.getLabel() : "në_proces");

        boolean sukses = mirembajtjaService.saveMirembajtje(dto);
        if (sukses) {
            showAlert(Alert.AlertType.INFORMATION, "Sukses", "Shërbimi teknik u regjistrua me sukses.");
            AdminSelectionState.clear();
            Router.navigateTo(ViewsEnum.ADMIN_AVIONET);
        } else {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Regjistrimi i mirëmbajtjes dështoi.");
        }
    }

    private LocalDateTime composeDateTime(LocalDate date, String timeText, boolean required) {
        if (date == null) {
            if (required) {
                showAlert(Alert.AlertType.WARNING, "Data", "Zgjidh datën e fillimit.");
                return null;
            }
            return null;
        }

        LocalTime time = LocalTime.of(0, 0);
        if (timeText != null && !timeText.trim().isEmpty()) {
            try {
                String[] parts = timeText.trim().split(":");
                int h = Integer.parseInt(parts[0]);
                int m = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
                time = LocalTime.of(h, m);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.WARNING, "Koha", "Shkruaje kohën si HH:mm, p.sh. 09:30");
                return null;
            }
        }
        return LocalDateTime.of(date, time);
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert a = new Alert(type);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(message);
        a.showAndWait();
    }
}