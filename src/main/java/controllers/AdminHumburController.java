package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.ArtikujtHumbur;
import models.Perdoruesi;

import java.sql.Date;
import java.time.LocalDate;

public class AdminHumburController {

    // ===============================
    // SIDEBAR NAVIGATION ELEMENTS
    // ===============================
    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    // ===============================
    // SEARCH FIELD
    // ===============================
    @FXML private TextField txtSearchHumbur;

    // ===============================
    // REGISTRATION FORM FIELDS
    // ===============================
    @FXML private TextField txtArtikulli;
    @FXML private TextField txtVendi;
    @FXML private DatePicker dtDataGjetjes;
    @FXML private TextArea txtPershkrimi;
    @FXML private ComboBox<String> cmbStatusiHumbur;

    // ===============================
    // TABLE VIEW & COLUMNS
    // ===============================
    @FXML private TableView<ArtikujtHumbur> tblArtikujt;
    @FXML private TableColumn<ArtikujtHumbur, Integer> colHumburId;
    @FXML private TableColumn<ArtikujtHumbur, String> colArtikulli; // Do të shfaqë kategorinë/emrin
    @FXML private TableColumn<ArtikujtHumbur, String> colVendi;
    @FXML private TableColumn<ArtikujtHumbur, String> colData;
    @FXML private TableColumn<ArtikujtHumbur, String> colStatusiHumbur;

    private ObservableList<ArtikujtHumbur> listaArtikujve = FXCollections.observableArrayList();

    // ===============================
    // INITIALIZE
    // ===============================
    @FXML
    public void initialize() {
        setupSidebarActions();
        setupTableColumns();
        populloComboBoxat();
        loadArtikujtNgaDB();

        // Kërkim në kohë reale gjatë shkrimit (onKeyReleased)
        if (txtSearchHumbur != null) {
            txtSearchHumbur.setOnKeyReleased(e -> handleSearchHumbur());
        }
    }

    private void setupSidebarActions() {
        navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        navFluturimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET));
        navRezervimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET));
        navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        navStafi.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_STAFI));
        navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
        navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
    }

    private void setupTableColumns() {
        // Lidhja direkte me Getters e modelit tënd ArtikujtHumbur
        if (colHumburId != null) {
            colHumburId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdArtikullit()).asObject());
        }
        if (colArtikulli != null) {
            // Përdorim fushën 'kategoria' si Emër/Lloj i artikullit
            colArtikulli.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getKategoria()));
        }
        if (colVendi != null) {
            colVendi.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVendiGjetjes()));
        }
        if (colData != null) {
            colData.setCellValueFactory(data -> {
                Date dataGjetjes = data.getValue().getDataGjetjes();
                return new SimpleStringProperty(dataGjetjes != null ? dataGjetjes.toString() : "-");
            });
        }
        if (colStatusiHumbur != null) {
            colStatusiHumbur.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusi()));
        }
    }

    private void populloComboBoxat() {
        if (cmbStatusiHumbur != null) {
            cmbStatusiHumbur.setItems(FXCollections.observableArrayList("I Gjetur (Në Depo)", "I Kthyer te Pronari", "Nën Hetim"));
        }
    }

    private void loadArtikujtNgaDB() {
        System.out.println("Duke marrë artikujt nga tabela artikujt_humbur...");

        // Pasi ta kesh gati Repository-n përkatës:
        // listaArtikujve.clear();
        // listaArtikujve.addAll(humburRepo.getAllArtikujt());
        // tblArtikujt.setItems(listaArtikujve);
    }

    // ===============================
    // BUTTON & SEARCH ACTIONS
    // ===============================

    @FXML
    private void handleSaveArtikull(ActionEvent event) {
        String artikulliEmri = txtArtikulli.getText();
        String vendi = txtVendi.getText();
        LocalDate localDate = dtDataGjetjes.getValue();
        String pershkrimiDetaje = txtPershkrimi.getText();
        String statusi = cmbStatusiHumbur.getValue();

        if (artikulliEmri == null || artikulliEmri.isEmpty() || vendi == null || vendi.isEmpty() || localDate == null) {
            System.out.println("Ju lutem plotësoni fushat obligative (Emri, Vendi dhe Data)!");
            return;
        }

        // Konvertimi i LocalDate të JavaFX në java.sql.Date për Databazë
        Date sqlDate = Date.valueOf(localDate);

        System.out.println("Duke regjistruar artikullin e ri në Lost & Found: " + artikulliEmri);

        // Ndërtimi i objektit sipas konstruktorit të plotë:
        // ArtikujtHumbur i ri = new ArtikujtHumbur(0, 1, pershkrimiDetaje, artikulliEmri, sqlDate, vendi, statusi, null, SessionManager.getCurrentUser().getId(), null);
        // humburRepo.insert(i ri);

        handleClearForm();
        loadArtikujtNgaDB();
    }

    private void handleClearForm() {
        if (txtArtikulli != null) txtArtikulli.clear();
        if (txtVendi != null) txtVendi.clear();
        if (dtDataGjetjes != null) dtDataGjetjes.setValue(null);
        if (txtPershkrimi != null) txtPershkrimi.clear();
        if (cmbStatusiHumbur != null) cmbStatusiHumbur.getSelectionModel().clearSelection();
    }

    private void handleSearchHumbur() {
        String query = txtSearchHumbur.getText() != null ? txtSearchHumbur.getText().trim() : "";
        System.out.println("Duke kërkuar në Lost & Found për: " + query);
        // TODO: Filtro listën lokalisht ose përmes SQL LIKE
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}