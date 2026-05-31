package controllers;

import models.dto.StafiTableDTO;
import models.dto.EditoPunonjesDTO;
import models.mappers.StafiMapper;
import repository.StafiRepository;
import models.Stafi;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class EditoPunonjesController {

    @FXML private TextField txtEmri;
    @FXML private TextField txtMbiemri;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefoni;
    @FXML private ComboBox<String> cmbDepartamenti;
    @FXML private TextField txtRoli;
    @FXML private TextField txtPaga;
    @FXML private CheckBox chkAktiv;

    private int idStafitAktual;

    @FXML
    public void initialize() {
        cmbDepartamenti.getItems().addAll(
                "Operacionet Fluturuese", "Shërbimet e Kabinës", "Shërbimet tokësore",
                "Menaxhimi i Trafikut Ajror", "Mirëmbajtja", "Operacionet e Aeroportit", "Siguria"
        );
    }

    // Metodë speciale që thërritet nga tabela kryesore për të pasuar të dhënat e rreshtit
    public void setPunonjesiTeDhenat(StafiTableDTO punonjes) {
        this.idStafitAktual = punonjes.getIdStafit();
        txtEmri.setText(punonjes.getEmri());
        txtMbiemri.setText(punonjes.getMbiemri());
        txtEmail.setText(punonjes.getEmailPunes());
        txtRoli.setText(punonjes.getEmriRoli());
        cmbDepartamenti.setValue(punonjes.getDepartamenti());
        chkAktiv.setSelected(punonjes.isEshteAktiv());

        // Pasi DTO i tabelës nuk e ka pasur pagën dhe telefonin, mund t'i lëmë default ose t'i lexojmë (p.sh. po vendosim një vlerë fillestare ose thërrasim DB nëse është e nevojshme). Po e lëmë 0.00 ose tekst të lirë fillimisht.
        txtPaga.setText("850.00");
        txtTelefoni.setText("");
    }

    @FXML
    private void handleRuajNdryshimet() {
        if (txtEmri.getText().trim().isEmpty() || txtMbiemri.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() || cmbDepartamenti.getValue() == null ||
                txtRoli.getText().trim().isEmpty() || txtPaga.getText().trim().isEmpty()) {
            shfaqAlert(Alert.AlertType.WARNING, "Validimi", "Plotësoni të gjitha fushat me *!");
            return;
        }

        double pagaValue = Double.parseDouble(txtPaga.getText().trim());

        EditoPunonjesDTO editDTO = new EditoPunonjesDTO(
                idStafitAktual,
                txtEmri.getText().trim(),
                txtMbiemri.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefoni.getText().trim(),
                txtRoli.getText().trim(),
                cmbDepartamenti.getValue(),
                pagaValue,
                chkAktiv.isSelected()
        );

        Stafi entitetiNdryshuar = StafiMapper.toEntityFromEdit(editDTO, 0);

        boolean uPerditesua = StafiRepository.perditesoPunonjes(entitetiNdryshuar, editDTO.getEmriRoli(), editDTO.getDepartamenti());

        if (uPerditesua) {
            shfaqAlert(Alert.AlertType.INFORMATION, "Sukses", "Të dhënat e punonjësit u përditësuan me sukses!");
            handleAnulo();
        } else {
            shfaqAlert(Alert.AlertType.ERROR, "Gabim", "Ndodhi një gabim gjatë ruajtjes në databazë.");
        }
    }

    @FXML
    private void handleAnulo() {
        ((Stage) txtEmri.getScene().getWindow()).close();
    }

    private void shfaqAlert(Alert.AlertType tip, String titulli, String mesazhi) {
        Alert alert = new Alert(tip);
        alert.setTitle(titulli);
        alert.setHeaderText(null);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }
}