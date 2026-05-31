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

    public void setPunonjesiTeDhenat(StafiTableDTO punonjes) {
        this.idStafitAktual = punonjes.getIdStafit();
        txtEmri.setText(punonjes.getEmri());
        txtMbiemri.setText(punonjes.getMbiemri());
        txtEmail.setText(punonjes.getEmailPunes());
        txtRoli.setText(punonjes.getEmriRoli());
        cmbDepartamenti.setValue(punonjes.getDepartamenti());
        chkAktiv.setSelected(punonjes.isEshteAktiv());

        // Thërrasim repository për të marrë objektin e plotë nga DB (përfshirë telefonin dhe pagën)
        StafiRepository repo = new StafiRepository();
        Stafi stafiPlote = repo.getById(this.idStafitAktual);

        if (stafiPlote != null) {
            // Nëse klasa jote Stafi e ka fushën e pagës dhe telefonit, i lexon nga stafiPlote:
            // txtPaga.setText(String.valueOf(stafiPlote.getPaga()));
            txtPaga.setText("850.00");

            // ZGJIDHJA E GABIMIT: Lexohet nga stafiPlote dhe jo nga punonjes!
            txtTelefoni.setText(stafiPlote.getTelefoni() != null ? stafiPlote.getTelefoni() : "");
        } else {
            txtPaga.setText("850.00");
            txtTelefoni.setText("");
        }
    }

    @FXML
    private void handleRuajNdryshimet() {
        // Validimi i fushave (mund ta heqësh txtPaga nga validimi nëse nuk të duhet domosdo)
        if (txtEmri.getText().trim().isEmpty() || txtMbiemri.getText().trim().isEmpty() ||
                txtEmail.getText().trim().isEmpty() || cmbDepartamenti.getValue() == null ||
                txtRoli.getText().trim().isEmpty()) {
            shfaqAlert(Alert.AlertType.WARNING, "Validimi", "Plotësoni të gjitha fushat me *!");
            return;
        }

        double pagaValue = 0.0;
        try {
            pagaValue = Double.parseDouble(txtPaga.getText().trim());
        } catch (NumberFormatException e) {
            // Nëse dëshiron mund ta lejosh edhe gabim, ose thjesht e lëmë 0.0 pasi nuk ruhet në DB
        }

        EditoPunonjesDTO editDTO = new EditoPunonjesDTO(
                idStafitAktual,
                txtEmri.getText().trim(),
                txtMbiemri.getText().trim(),
                txtEmail.getText().trim(),
                txtTelefoni.getText().trim(),
                txtRoli.getText().trim(),
                cmbDepartamenti.getValue(),
                pagaValue, // Ky mbetet vetëm brenda DTO-së për UI
                chkAktiv.isSelected()
        );

        StafiMapper mapper = new StafiMapper();
        Stafi stafiEkzistues = new Stafi(idStafitAktual);
        Stafi entitetiNdryshuar = mapper.fromDto(stafiEkzistues, editDTO);

        // KËTU NUK IA SHTOJMË PAGËN ENTITETIT - REPO DO TË RUN VETËM 7 KOLONAT E DB

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