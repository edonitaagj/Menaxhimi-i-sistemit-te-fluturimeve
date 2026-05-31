package controllers;

import app.Router;
import app.ViewsEnum;
import models.dto.StafiTableDTO;
import models.dto.StafiStatsDTO;
import repository.StafiRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

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

    // --- KARTAT DINAMIKE NALT ---
    @FXML private Label lblTotaliStafit;
    @FXML private Label lblStafiAktiv;
    @FXML private Label lblDepartamente;

    // --- FILTRAT DHE KËRKIMI ---
    @FXML private TextField txtSearchStaf;
    @FXML private ComboBox<String> cmbDepartamentiFilter;

    // --- TABELA DHE KOLONAT ---
    @FXML private TableView<StafiTableDTO> tblStafi;
    @FXML private TableColumn<StafiTableDTO, Integer> colStafiId;
    @FXML private TableColumn<StafiTableDTO, String> colEmri;
    @FXML private TableColumn<StafiTableDTO, String> colMbiemri;
    @FXML private TableColumn<StafiTableDTO, String> colRoli;
    @FXML private TableColumn<StafiTableDTO, String> colDepartamenti;
    @FXML private TableColumn<StafiTableDTO, String> colEmail;
    @FXML private TableColumn<StafiTableDTO, Boolean> colStatusiStafit;

    private ObservableList<StafiTableDTO> listaStafit = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSidebarActions();
        initTableColumns();
        loadStaffFromDatabase(); // Kjo metodë tani ngarkon edhe kartat nalt!

        txtSearchStaf.textProperty().addListener((observable, oldValue, newValue) -> handleSearchAndFilter());
        cmbDepartamentiFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> handleSearchAndFilter());
    }

    private void setupSidebarActions() {
        if (navDashboard != null) navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        if (navFluturimet != null) navFluturimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET));
        if (navRezervimet != null) navRezervimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET));
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
    }

    private void initTableColumns() {
        colStafiId.setCellValueFactory(new PropertyValueFactory<>("idStafit"));
        colEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colMbiemri.setCellValueFactory(new PropertyValueFactory<>("mbiemri"));
        colRoli.setCellValueFactory(new PropertyValueFactory<>("emriRoli"));
        colDepartamenti.setCellValueFactory(new PropertyValueFactory<>("departamenti"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("emailPunes"));

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
    }

    private void loadStaffFromDatabase() {
        // 1. Mbushja e ComboBox
        cmbDepartamentiFilter.setItems(FXCollections.observableArrayList(
                "Të Gjitha", "Operacionet Fluturuese", "Shërbimet e Kabinës", "Shërbimet tokësore",
                "Menaxhimi i Trafikut Ajror", "Mirëmbajtja", "Operacionet e Aeroportit", "Siguria"
        ));
        cmbDepartamentiFilter.getSelectionModel().selectFirst();

        // 2. RIFRESKIMI I KARTAVE STATISTIKE (DINAMIKE)
        StafiStatsDTO stats = StafiRepository.getStaffStatistics();
        if (lblTotaliStafit != null) lblTotaliStafit.setText(stats.getTotaliStafit() + " Punonjës");
        if (lblStafiAktiv != null) lblStafiAktiv.setText(stats.getStafiAktiv() + " në Ndërtesë");
        if (lblDepartamente != null) lblDepartamente.setText(stats.getNumriDepartamenteve() + " Sektorë Aktivë");

        // 3. Mbushja e Tabelës
        listaStafit = StafiRepository.getAllStaffForTable();
        tblStafi.setItems(listaStafit);
    }

    @FXML
    private void handleAddNewStaffModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ShtoPunonjes.fxml"));
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.setTitle("Regjistro Punonjës të Ri");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);

            modalStage.showAndWait();

            // Kur dritarja mbyllet (pas shtimit të punonjësit), rifreskohen edhe tabela edhe kartat!
            loadStaffFromDatabase();

        } catch (IOException e) {
            System.err.println("Gabim gjatë ngarkimit të pamjes ShtoPunonjes.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleSearchAndFilter() {
        String searchQuery = txtSearchStaf.getText() == null ? "" : txtSearchStaf.getText().toLowerCase().trim();
        String selectedDept = cmbDepartamentiFilter.getValue();

        ObservableList<StafiTableDTO> filteredList = FXCollections.observableArrayList();

        for (StafiTableDTO punonjes : listaStafit) {
            boolean matchesSearch = searchQuery.isEmpty() ||
                    punonjes.getEmri().toLowerCase().contains(searchQuery) ||
                    punonjes.getMbiemri().toLowerCase().contains(searchQuery) ||
                    punonjes.getEmriRoli().toLowerCase().contains(searchQuery);

            boolean matchesDepartment = selectedDept == null ||
                    selectedDept.equals("Të Gjitha") ||
                    punonjes.getDepartamenti().equalsIgnoreCase(selectedDept);

            if (matchesSearch && matchesDepartment) {
                filteredList.add(punonjes);
            }
        }
        tblStafi.setItems(filteredList);
    }

    @FXML
    private void handleLogout() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}