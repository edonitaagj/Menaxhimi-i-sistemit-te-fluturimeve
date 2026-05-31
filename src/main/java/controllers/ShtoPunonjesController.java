package controllers;

import models.dto.ShtoPunonjesDTO;
import models.mappers.StafiMapper;
import repository.StafiRepository;
import models.Stafi;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.time.LocalDate;

public class ShtoPunonjesController {

    @FXML private TextField txtEmri;
    @FXML private TextField txtMbiemri;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefoni;
    @FXML private ComboBox<String> cmbDepartamenti;
    @FXML private TextField txtRoli;
    @FXML private DatePicker dtPunesimit;
    @FXML private TextField txtPaga;

    @FXML
    public void initialize() {
        cmbDepartamenti.getItems().addAll(
                "Operacionet Fluturuese",
                "Shërbimet e Kabinës",
                "Shërbimet tokësore",
                "Menaxhimi i Trafikut Ajror",
                "Mirëmbajtja",
                "Operacionet e Aeroportit",
                "Siguria"
        );
        dtPunesimit.setValue(LocalDate.now());
    }

    @FXML
    private void handleRuajPunonjes() {
        if (txtEmri.getText().trim().isEmpty() || txtMbiemri.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() || cmbDepartamenti.getValue() == null ||
                txtRoli.getText().trim().isEmpty() || txtPaga.getText().trim().isEmpty()) {

            shfaqAlert(AlertType.WARNING, "Validimi", "Ju lutem plotësoni të gjitha fushat e shënuara me * !");
            return;
        }

        double pagaValue;
        try {
            pagaValue = Double.parseDouble(txtPaga.getText().trim());
        } catch (NumberFormatException e) {
            shfaqAlert(AlertType.ERROR, "Gabim Validimi", "Paga Neto duhet të jetë një numër valid decimal!");
            return;
        }

        ShtoPunonjesDTO punonjesDTO = new ShtoPunonjesDTO(
                txtEmri.getText().trim(),
                txtMbiemri.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefoni.getText().trim(),
                cmbDepartamenti.getValue(),
                txtRoli.getText().trim(),
                dtPunesimit.getValue(),
                pagaValue
        );

        // THIRRJA E MAPPER-IT SIPAS INTERFACES TË RI TË PROFESORIT
        StafiMapper mapper = new StafiMapper();
        Stafi stafiEntity = mapper.fromDto(punonjesDTO);

        // Thirrja e Repository për ruajtjen në Databazë
        boolean uRuajt = StafiRepository.shtoPunonjesTeRi(stafiEntity, punonjesDTO.getEmriRoli(), punonjesDTO.getDepartamenti());

        if (uRuajt) {
            shfaqAlert(AlertType.INFORMATION, "Sukses", "Punonjësi u regjistrua me sukses në sistem!\nNumri i Punonjësit: " + stafiEntity.getNumriPunonjesit());
            handleAnulo();
        } else {
            shfaqAlert(AlertType.ERROR, "Gabim", "Ndodhi një gabim në databazë gjatë ruajtjes së punonjësit.");
        }
    }

    @FXML
    private void handleAnulo() {
        Stage stage = (Stage) txtEmri.getScene().getWindow();
        stage.close();
    }

    private void shfaqAlert(AlertType tip, String titulli, String mesazhi) {
        Alert alert = new Alert(tip);
        alert.setTitle(titulli);
        alert.setHeaderText(null);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }
}