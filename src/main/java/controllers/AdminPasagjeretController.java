package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import java.time.LocalDate;

// --- MODEL ARTIFICIAL PËR PASAGJERIN (Zëvendësoje me modelin tënd real nga models.Pasagjeret) ---
class Pasagjeri {
    private int id;
    private String numriPasaportes;
    private String emri;
    private String mbiemri;
    private String shtetesia;
    private String gjinia;
    private LocalDate datelindja;
    private String email;
    private String telefoni;
    private String adresa;
    private LocalDate pasaportaSkadimi;

    public Pasagjeri(int id, String numriPasaportes, String emri, String mbiemri, String shtetesia,
                     String gjinia, LocalDate datelindja, String email, String telefoni, String adresa, LocalDate pasaportaSkadimi) {
        this.id = id;
        this.numriPasaportes = numriPasaportes;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.shtetesia = shtetesia;
        this.gjinia = gjinia;
        this.datelindja = datelindja;
        this.email = email;
        this.telefoni = telefoni;
        this.adresa = adresa;
        this.pasaportaSkadimi = pasaportaSkadimi;
    }

    // Getters dhe Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNumriPasaportes() { return numriPasaportes; }
    public void setNumriPasaportes(String numriPasaportes) { this.numriPasaportes = numriPasaportes; }
    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }
    public String getMbiemri() { return mbiemri; }
    public void setMbiemri(String mbiemri) { this.mbiemri = mbiemri; }
    public String getShtetesia() { return shtetesia; }
    public void setShtetesia(String shtetesia) { this.shtetesia = shtetesia; }
    public String getGjinia() { return gjinia; }
    public void setGjinia(String gjinia) { this.gjinia = gjinia; }
    public LocalDate getDatelindja() { return datelindja; }
    public void setDatelindja(LocalDate datelindja) { this.datelindja = datelindja; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefoni() { return telefoni; }
    public void setTelefoni(String telefoni) { this.telefoni = telefoni; }
    public String getAdresa() { return adresa; }
    public void setAdresa(String adresa) { this.adresa = adresa; }
    public LocalDate getPasaportaSkadimi() { return pasaportaSkadimi; }
    public void setPasaportaSkadimi(LocalDate pasaportaSkadimi) { this.pasaportaSkadimi = pasaportaSkadimi; }
}

public class AdminPasagjeretController {

    // --- SIDEBAR NAVIGIMI ---
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

    // --- ELEMENTET E FORMËS SË PASAGJERIT ---
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

    // --- TABELA DHE KOLONAT ---
    @FXML private TableView<Pasagjeri> tblPasagjeret;
    @FXML private TableColumn<Pasagjeri, Integer> colId;
    @FXML private TableColumn<Pasagjeri, String> colPasaporta;
    @FXML private TableColumn<Pasagjeri, String> colEmri;
    @FXML private TableColumn<Pasagjeri, String> colMbiemri;
    @FXML private TableColumn<Pasagjeri, String> colShtetesia;
    @FXML private TableColumn<Pasagjeri, String> colGjinia;
    @FXML private TableColumn<Pasagjeri, String> colEmail;
    @FXML private TableColumn<Pasagjeri, String> colTelefoni;

    // --- PAGINATION LABEL ---
    @FXML private Label lblPagination;

    // Listat programatike
    private ObservableList<Pasagjeri> listaPasagjereve = FXCollections.observableArrayList();
    private Pasagjeri pasagjeriESelektuar = null;

    @FXML
    public void initialize() {
        // Aktivizojmë klikimet në Sidebar
        setupSidebarActions();

        // Inicializojmë kolonat e tabelës
        initTableColumns();

        // Ngarkojmë të dhënat fillestare në ComboBox-e dhe Tabelë
        loadInitialData();

        // Monitorojmë klikimet e rreshtave në tabelë për mbushjen e formës
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
        // navPasagjeret është faqja aktuale, s'ka nevojë për veprim ose mund të rifreskohet
    }

    private void initTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPasaporta.setCellValueFactory(new PropertyValueFactory<>("numriPasaportes"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colMbiemri.setCellValueFactory(new PropertyValueFactory<>("mbiemri"));
        colShtetesia.setCellValueFactory(new PropertyValueFactory<>("shtetesia"));
        colGjinia.setCellValueFactory(new PropertyValueFactory<>("gjinia"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colTelefoni.setCellValueFactory(new PropertyValueFactory<>("telefoni"));

        tblPasagjeret.setItems(listaPasagjereve);
    }

    // --- CRUD OPERACIONET ---

    @FXML
    private void handleSavePasagjer() {
        // Validimi i fushave të detyrueshme (*)
        if (txtNumriPasaportes.getText().isEmpty() || txtEmri.getText().isEmpty() ||
                txtMbiemri.getText().isEmpty() || cmbShtetesia.getValue() == null ||
                cmbGjinia.getValue() == null || dpDatelindja.getValue() == null) {

            shfaqAlert("Gabim Validimi", "Ju lutem plotësoni të gjitha fushat e detyrueshme (*).", Alert.AlertType.WARNING);
            return;
        }

        if (pasagjeriESelektuar == null) {
            // SHTIM I RI (INSERT)
            Pasagjeri iRi = new Pasagjeri(
                    listaPasagjereve.size() + 1,
                    txtNumriPasaportes.getText(),
                    txtEmri.getText(),
                    txtMbiemri.getText(),
                    cmbShtetesia.getValue(),
                    cmbGjinia.getValue(),
                    dpDatelindja.getValue(),
                    txtEmail.getText(),
                    txtTelefoni.getText(),
                    txtAdresa.getText(),
                    dpPasaportaSkadimi.getValue()
            );
            listaPasagjereve.add(iRi);
            shfaqAlert("Sukses", "Pasagjeri u regjistrua me sukses!", Alert.AlertType.INFORMATION);
        } else {
            // MODIFIKIM (UPDATE)
            pasagjeriESelektuar.setNumriPasaportes(txtNumriPasaportes.getText());
            pasagjeriESelektuar.setEmri(txtEmri.getText());
            pasagjeriESelektuar.setMbiemri(txtMbiemri.getText());
            pasagjeriESelektuar.setShtetesia(cmbShtetesia.getValue());
            pasagjeriESelektuar.setGjinia(cmbGjinia.getValue());
            pasagjeriESelektuar.setDatelindja(dpDatelindja.getValue());
            pasagjeriESelektuar.setEmail(txtEmail.getText());
            pasagjeriESelektuar.setTelefoni(txtTelefoni.getText());
            pasagjeriESelektuar.setAdresa(txtAdresa.getText());
            pasagjeriESelektuar.setPasaportaSkadimi(dpPasaportaSkadimi.getValue());

            tblPasagjeret.refresh();
            shfaqAlert("Sukses", "Të dhënat e pasagjerit u përditësuan!", Alert.AlertType.INFORMATION);
        }
        handleClearForm();
    }

    @FXML
    private void handleDeletePasagjer() {
        if (pasagjeriESelektuar != null) {
            listaPasagjereve.remove(pasagjeriESelektuar);
            shfaqAlert("Sukses", "Pasagjeri u fshi nga regjistri.", Alert.AlertType.INFORMATION);
            handleClearForm();
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

    private void mbushFormenPasagjer(Pasagjeri p) {
        pasagjeriESelektuar = p;
        txtNumriPasaportes.setText(p.getNumriPasaportes());
        txtEmri.setText(p.getEmri());
        txtMbiemri.setText(p.getMbiemri());
        cmbShtetesia.setValue(p.getShtetesia());
        cmbGjinia.setValue(p.getGjinia());
        dpDatelindja.setValue(p.getDatelindja());
        txtEmail.setText(p.getEmail());
        txtTelefoni.setText(p.getTelefoni());
        txtAdresa.setText(p.getAdresa());
        dpPasaportaSkadimi.setValue(p.getPasaportaSkadimi());

        btnDeletePasagjer.setDisable(false);
    }

    // --- KËRKIMI DINAMIK (SEARCH) ---
    @FXML
    private void handleSearchPasagjeri(KeyEvent event) {
        String query = txtSearchPasagjeri.getText().toLowerCase();
        if (query.isEmpty()) {
            tblPasagjeret.setItems(listaPasagjereve);
            return;
        }

        ObservableList<Pasagjeri> filteredList = FXCollections.observableArrayList();
        for (Pasagjeri p : listaPasagjereve) {
            if (p.getEmri().toLowerCase().contains(query) ||
                    p.getMbiemri().toLowerCase().contains(query) ||
                    p.getNumriPasaportes().toLowerCase().contains(query)) {
                filteredList.add(p);
            }
        }
        tblPasagjeret.setItems(filteredList);
    }

    // --- PAGINIMI (PAGINATION ACCIONS) ---
    @FXML
    private void handlePreviousPage() {
        System.out.println("Klikuar: Faqja e mëparshme");
        // Logjika e paginimit SQL (OFFSET) nëse e aplikoni më vonë
    }

    @FXML
    private void handleNextPage() {
        System.out.println("Klikuar: Faqja tjetër");
        // Logjika e paginimit SQL
    }

    // --- LOGOUT ---
    @FXML
    private void handleLogout() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    private void shfaqAlert(String titulli, String mesazhi, Alert.AlertType lloji) {
        Alert alert = new Alert(lloji);
        alert.setTitle(titulli);
        alert.setHeaderText(null);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }

    // --- MBUSHJA ME TË DHËNA TESTUESE ---
    private void loadInitialData() {
        // Populllimi i ComboBox-eve
        cmbShtetesia.setItems(FXCollections.observableArrayList("Kosovë", "Shqipëri", "Gjermani", "Zvicër", "SHBA"));
        cmbGjinia.setItems(FXCollections.observableArrayList("M", "F", "Tjetër"));

        // Pasagjerë shembuj
        listaPasagjereve.add(new Pasagjeri(1, "XK1020304", "Ardian", "Krasniqi", "Kosovë", "M",
                LocalDate.of(1995, 5, 12), "ardian@email.com", "+38344100200", "Prishtinë", LocalDate.of(2030, 5, 12)));

        listaPasagjereve.add(new Pasagjeri(2, "XK9080706", "Blerta", "Gashi", "Kosovë", "F",
                LocalDate.of(1998, 8, 24), "blerta@email.com", "+38349555666", "Tiranë", LocalDate.of(2029, 2, 18)));

        lblPagination.setText("Faqja 1 nga 1");
    }
}