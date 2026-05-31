package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import models.Pasagjeri;
import models.dto.PasagjeriTableDTO;
import models.mappers.PasagjeriMapper;
import repository.PasagjeriRepository;

import java.time.LocalDate;
import java.util.List;

public class AdminPasagjeretController {

    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    @FXML private Label avatarLabel;
    @FXML private Label userFullName;
    @FXML private Label userEmail;

    @FXML private TextField txtNumriPasaportes;
    @FXML private TextField txtEmri;
    @FXML private TextField txtMbiemri;
    @FXML private ComboBox<String> cmbShtetesia;
    @FXML private ComboBox<String> cmbGjinia;
    @FXML private DatePicker dpDatelindja;
    @FXML private TextField txtEmail;
    @FXML private TextField txtTelefoni;
    @FXML private TextField txtAdresa;
    @FXML private DatePicker dpPasaportaSkadimi;

    @FXML private Button btnDeletePasagjer;
    @FXML private TextField txtSearchPasagjeri;

    @FXML private TableView<PasagjeriTableDTO> tblPasagjeret;
    @FXML private TableColumn<PasagjeriTableDTO, Integer> colId;
    @FXML private TableColumn<PasagjeriTableDTO, String> colPasaporta;
    @FXML private TableColumn<PasagjeriTableDTO, String> colEmri;
    @FXML private TableColumn<PasagjeriTableDTO, String> colMbiemri;
    @FXML private TableColumn<PasagjeriTableDTO, String> colShtetesia;
    @FXML private TableColumn<PasagjeriTableDTO, String> colGjinia;
    @FXML private TableColumn<PasagjeriTableDTO, String> colEmail;
    @FXML private TableColumn<PasagjeriTableDTO, String> colTelefoni;

    @FXML private Label lblPagination;

    private ObservableList<PasagjeriTableDTO> masterDataList = FXCollections.observableArrayList();
    private PasagjeriTableDTO pasagjeriESelektuar = null;

    @FXML
    public void initialize() {
        setupSidebarActions();
        initTableColumns();
        loadInitialData();

        tblPasagjeret.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mbushFormenPasagjer(newSelection);
            }
        });
    }

    private void setupSidebarActions() {
        if (navDashboard != null) navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        if (navFluturimet != null) navFluturimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET));
        if (navRezervimet != null) navRezervimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET));
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navStafi != null) navStafi.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_STAFI));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
    }

    private void initTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idPasagjerit"));
        colPasaporta.setCellValueFactory(new PropertyValueFactory<>("numriPasaportes"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colMbiemri.setCellValueFactory(new PropertyValueFactory<>("mbiemri"));
        colShtetesia.setCellValueFactory(new PropertyValueFactory<>("shtetesia"));
        colGjinia.setCellValueFactory(new PropertyValueFactory<>("gjinia"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefoni.setCellValueFactory(new PropertyValueFactory<>("telefoni"));

        tblPasagjeret.setItems(masterDataList);
    }

    private void loadInitialData() {
        cmbShtetesia.setItems(FXCollections.observableArrayList("Kosovë", "Shqipëri", "Gjermani", "Zvicër", "SHBA"));
        cmbGjinia.setItems(FXCollections.observableArrayList("M", "F", "Tjeter"));

        masterDataList.clear();
        masterDataList.addAll(PasagjeriRepository.getAllPasagjeretTable());
        lblPagination.setText("Faqja 1 nga 1");
    }

    @FXML
    private void handleSavePasagjer() {
        if (txtNumriPasaportes.getText().isEmpty() || txtEmri.getText().isEmpty() ||
                txtMbiemri.getText().isEmpty() || cmbShtetesia.getValue() == null ||
                cmbGjinia.getValue() == null || dpDatelindja.getValue() == null) {

            shfaqAlert("Gabim Validimi", "Ju lutem plotësoni të gjitha fushat e detyrueshme (*).", Alert.AlertType.WARNING);
            return;
        }

        int idAktuale = (pasagjeriESelektuar != null) ? pasagjeriESelektuar.getIdPasagjerit() : 0;

        PasagjeriTableDTO dto = new PasagjeriTableDTO(
                idAktuale,
                txtNumriPasaportes.getText().trim(),
                txtEmri.getText().trim(),
                txtMbiemri.getText().trim(),
                cmbShtetesia.getValue(),
                cmbGjinia.getValue(),
                dpDatelindja.getValue(),
                txtEmail.getText().trim(),
                txtTelefoni.getText().trim(),
                txtAdresa.getText().trim(),
                dpPasaportaSkadimi.getValue()
        );

        PasagjeriMapper mapper = new PasagjeriMapper();
        boolean suksese;

        if (pasagjeriESelektuar == null) {
            Pasagjeri iRi = mapper.fromDto(null, dto);
            suksese = PasagjeriRepository.shtoPasagjer(iRi, dto.getShtetesia());
        } else {
            Pasagjeri ekzistues = new Pasagjeri(idAktuale);
            Pasagjeri iNdryshuar = mapper.fromDto(ekzistues, dto);
            suksese = PasagjeriRepository.perditesoPasagjer(iNdryshuar, dto.getShtetesia());
        }

        if (suksese) {
            shfaqAlert("Sukses", "Të dhënat u ruajtën me sukses në databazë!", Alert.AlertType.INFORMATION);
            loadInitialData();
            handleClearForm();
        } else {
            shfaqAlert("Gabim", "Ruajtja në databazë dështoi. Kontrolloni log-et.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeletePasagjer() {
        if (pasagjeriESelektuar != null) {
            boolean uFshi = PasagjeriRepository.fshijPasagjer(pasagjeriESelektuar.getIdPasagjerit());
            if (uFshi) {
                shfaqAlert("Sukses", "Pasagjeri u fshi me sukses nga sistemi.", Alert.AlertType.INFORMATION);
                loadInitialData();
                handleClearForm();
            } else {
                shfaqAlert("Gabim", "Fshirja dështoi.", Alert.AlertType.ERROR);
            }
        }
    }

    @FXML
    private void handleClearForm() {
        txtNumriPasaportes.clear();
        txtEmri.clear();
        txtMbiemri.clear();
        cmbShtetesia.setValue(null);
        cmbGjinia.setValue(null);
        dpDatelindja.setValue(null);
        txtEmail.clear();
        txtTelefoni.clear();
        txtAdresa.clear();
        dpPasaportaSkadimi.setValue(null);

        btnDeletePasagjer.setDisable(true);
        pasagjeriESelektuar = null;
        tblPasagjeret.getSelectionModel().clearSelection();
    }

    private void mbushFormenPasagjer(PasagjeriTableDTO p) {
        pasagjeriESelektuar = p;
        txtNumriPasaportes.setText(p.getNumriPasaportes());
        txtEmri.setText(p.getEmri());
        txtMbiemri.setText(p.getMbiemri());
        cmbShtetesia.setValue(p.getShtetesia());
        cmbGjinia.setValue(p.getGjinia());
        dpDatelindja.setValue(p.getDatelindja());
        txtEmail.setText(p.getEmail() != null ? p.getEmail() : "");
        txtTelefoni.setText(p.getTelefoni() != null ? p.getTelefoni() : "");
        txtAdresa.setText(p.getAdresa() != null ? p.getAdresa() : "");
        dpPasaportaSkadimi.setValue(p.getPasaportaSkadimi());

        btnDeletePasagjer.setDisable(false);
    }

    @FXML
    private void handleSearchPasagjeri(KeyEvent event) {
        String query = txtSearchPasagjeri.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            tblPasagjeret.setItems(masterDataList);
            return;
        }

        ObservableList<PasagjeriTableDTO> filteredList = FXCollections.observableArrayList();
        for (PasagjeriTableDTO p : masterDataList) {
            if (p.getEmri().toLowerCase().contains(query) ||
                    p.getMbiemri().toLowerCase().contains(query) ||
                    p.getNumriPasaportes().toLowerCase().contains(query) ||
                    p.getShtetesia().toLowerCase().contains(query)) {
                filteredList.add(p);
            }
        }
        tblPasagjeret.setItems(filteredList);
    }

    @FXML private void handlePreviousPage() {}
    @FXML private void handleNextPage() {}
    @FXML private void handleLogout() { Router.navigateTo(ViewsEnum.LOGIN_VIEW); }

    private void shfaqAlert(String titulli, String mesazhi, Alert.AlertType lloji) {
        Alert alert = new Alert(lloji);
        alert.setTitle(titulli);
        alert.setHeaderText(null);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }
}