package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

// --- MODEL ARTIFICIAL PËR STAFIN (Zëvendësoje me modelin tënd real nga models.StafPunonjes) ---
class StafPunonjes {
    private int id;
    private String emri;
    private String mbiemri;
    private String roli;
    private String departamenti;
    private String email;
    private boolean eshteAktiv;

    public StafPunonjes(int id, String emri, String mbiemri, String roli, String departamenti, String email, boolean eshteAktiv) {
        this.id = id;
        this.emri = emri;
        this.mbiemri = mbiemri;
        this.roli = roli;
        this.departamenti = departamenti;
        this.email = email;
        this.eshteAktiv = eshteAktiv;
    }

    // Getters dhe Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getEmri() { return emri; }
    public void setEmri(String emri) { this.emri = emri; }
    public String getMbiemri() { return mbiemri; }
    public void setMbiemri(String mbiemri) { this.mbiemri = mbiemri; }
    public String getRoli() { return roli; }
    public void setRoli(String roli) { this.roli = roli; }
    public String getDepartamenti() { return departamenti; }
    public void setDepartamenti(String departamenti) { this.departamenti = departamenti; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public boolean isEshteAktiv() { return eshteAktiv; }
    public void setEshteAktiv(boolean eshteAktiv) { this.eshteAktiv = eshteAktiv; }
}

public class AdminStafiController {

    // --- SIDEBAR NAVIGIMI ---
    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    // --- FILTRAT DHE KËRKIMI ---
    @FXML private TextField txtSearchStaf;
    @FXML private ComboBox<String> cmbDepartamentiFilter;

    // --- TABELA DHE KOLONAT ---
    @FXML private TableView<StafPunonjes> tblStafi;
    @FXML private TableColumn<StafPunonjes, Integer> colStafiId;
    @FXML private TableColumn<StafPunonjes, String> colEmri;
    @FXML private TableColumn<StafPunonjes, String> colMbiemri;
    @FXML private TableColumn<StafPunonjes, String> colRoli;
    @FXML private TableColumn<StafPunonjes, String> colDepartamenti;
    @FXML private TableColumn<StafPunonjes, String> colEmail;
    @FXML private TableColumn<StafPunonjes, Boolean> colStatusiStafit;

    // Listat programatike (Observable Lists)
    private ObservableList<StafPunonjes> listaStafit = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Aktivizojmë navigimin në Sidebar
        setupSidebarActions();

        // Inicializojmë kolonat e tabelës
        initTableColumns();

        // Ngarkojmë të dhënat fillestare
        loadInitialData();

        // Monitorojmë ndryshimet në fushën e kërkimit (Kërkim Dinamik)
        txtSearchStaf.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchAndFilter();
        });

        // Monitorojmë përzgjedhjen e departamentit në ComboBox për filtrim
        cmbDepartamentiFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchAndFilter();
        });
    }

    private void setupSidebarActions() {
        if (navDashboard != null) navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        if (navFluturimet != null) navFluturimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET));
        if (navRezervimet != null) navRezervimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET));
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
        // navStafi është faqja aktuale
    }

    private void initTableColumns() {
        colStafiId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colMbiemri.setCellValueFactory(new PropertyValueFactory<>("mbiemri"));
        colRoli.setCellValueFactory(new PropertyValueFactory<>("roli"));
        colDepartamenti.setCellValueFactory(new PropertyValueFactory<>("departamenti"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Konvertimi i statusit Boolean në tregues vizual (🟢 Aktiv / 🔴 Jo-Aktiv)
        colStatusiStafit.setCellValueFactory(new PropertyValueFactory<>("eshteAktiv"));
        colStatusiStafit.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "🟢 Aktiv" : "🔴 Jo-Aktiv");
                }
            }
        });

        tblStafi.setItems(listaStafit);
    }

    // --- HAPJA E MODALIT PËR SHTIMIN E NJË PUNONJËSI TË RI ---
    @FXML
    private void handleAddNewStaffModal() {
        System.out.println("Hapja e dritares modale (Pop-up) për regjistrimin e një punonjësi të ri...");
        // Këtu mund të përdorësh Router ose të hapësh një Stage të ri (Pop-up):
        // Router.navigateTo(ViewsEnum.ADMIN_SHTO_STAF);
    }

    // --- KËRKIMI DHE FILTRIMI KOMBINOI ---
    private void handleSearchAndFilter() {
        String searchQuery = txtSearchStaf.getText() == null ? "" : txtSearchStaf.getText().toLowerCase().trim();
        String selectedDept = cmbDepartamentiFilter.getValue();

        ObservableList<StafPunonjes> filteredList = FXCollections.observableArrayList();

        for (StafPunonjes punonjes : listaStafit) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    punonjes.getEmri().toLowerCase().contains(searchQuery) ||
                    punonjes.getMbiemri().toLowerCase().contains(searchQuery) ||
                    punonjes.getRoli().toLowerCase().contains(searchQuery);

            boolean matchesDepartment = selectedDept == null ||
                    selectedDept.equals("Të Gjitha") ||
                    punonjes.getDepartamenti().equals(selectedDept);

            if (matchesSearch && matchesDepartment) {
                filteredList.add(punonjes);
            }
        }

        tblStafi.setItems(filteredList);
    }

    // --- LOGOUT ---
    @FXML
    private void handleLogout() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    // --- POPULLIMI ME TË DHËNA TESTUESE (MOCK DATA) ---
    private void loadInitialData() {
        // Mbushja e ComboBox të filtrave të departamentit
        cmbDepartamentiFilter.setItems(FXCollections.observableArrayList(
                "Të Gjitha", "Operacionet e Fluturimit", "Logjistika & Avionët", "Siguria", "Burimet Njerëzore", "Shërbimi i Pasagjerëve"
        ));

        // Shtimi i punonjësve shembuj në listë
        listaStafit.add(new StafPunonjes(1, "Ilir", "Hoxha", "Pilot Kapiten", "Operacionet e Fluturimit", "ilir.hoxha@prn-airport.com", true));
        listaStafit.add(new StafPunonjes(2, "Fidan", "Berisha", "Inxhinier Avionësh", "Logjistika & Avionët", "fidan.berisha@prn-airport.com", true));
        listaStafit.add(new StafPunonjes(3, "Anisa", "Gashi", "Menaxhere e Burimeve Njerëzore", "Burimet Njerëzore", "anisa.gashi@prn-airport.com", true));
        listaStafit.add(new StafPunonjes(4, "Valon", "Rama", "Oficer i Sigurisë", "Siguria", "valon.rama@prn-airport.com", false));
        listaStafit.add(new StafPunonjes(5, "Elena", "Krasniqi", "Koordinatore e Fluturimeve", "Operacionet e Fluturimit", "elena.krasniqi@prn-airport.com", true));
    }
}