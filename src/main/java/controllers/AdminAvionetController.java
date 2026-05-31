package controllers;

import app.AdminSelectionState;
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
import models.Perdoruesi;
import models.dto.AvionetTableDto;
import models.dto.MirembajtjaTableDto;
import services.AvionetService;
import services.MirembajtjaService;

public class AdminAvionetController {

    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    @FXML private TextField txtSearchAvioni;

    @FXML private TableView<AvionetTableDto> tblAvionet;
    @FXML private TableColumn<AvionetTableDto, Integer> colAvioniId;
    @FXML private TableColumn<AvionetTableDto, String> colNumriRegjistrit;
    @FXML private TableColumn<AvionetTableDto, String> colProdhuesi;
    @FXML private TableColumn<AvionetTableDto, String> colModeli;
    @FXML private TableColumn<AvionetTableDto, String> colVitiProdhimit;
    @FXML private TableColumn<AvionetTableDto, String> colStatusiAvionit;

    @FXML private TableView<MirembajtjaTableDto> tblMirembajtja;
    @FXML private TableColumn<MirembajtjaTableDto, String> colLlojiSherbimit;
    @FXML private TableColumn<MirembajtjaTableDto, String> colDataFillimit;
    @FXML private TableColumn<MirembajtjaTableDto, String> colDataMbarimit;
    @FXML private TableColumn<MirembajtjaTableDto, String> colPershkrimiPunes;
    @FXML private TableColumn<MirembajtjaTableDto, Double> colKostoja;
    @FXML private TableColumn<MirembajtjaTableDto, String> colStatusiMirembajtjes;

    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

    private final AvionetService avionetService = new AvionetService();
    private final MirembajtjaService mirembajtjaService = new MirembajtjaService();

    private final ObservableList<AvionetTableDto> avionetMaster = FXCollections.observableArrayList();
    private final ObservableList<MirembajtjaTableDto> mirembajtjaMaster = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSidebarActions();
        setupTableColumns();
        loadAvionetNgaDB();
        loadAdminData();

        tblAvionet.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                AdminSelectionState.setSelectedAvionId(newVal.getIdAvionit());
                AdminSelectionState.setSelectedAvionRegjistri(newVal.getNumriRegjistrit());
                loadMirembajtjaPerAvionin(newVal.getIdAvionit());
            } else {
                tblMirembajtja.getItems().clear();
            }
        });

        if (txtSearchAvioni != null) {
            txtSearchAvioni.textProperty().addListener((obs, oldText, newText) -> filterAvionet(newText));
        }

        tblAvionet.setPlaceholder(new Label("Nuk u gjetën avionë."));
        tblMirembajtja.setPlaceholder(new Label("Zgjidh një avion për të parë mirëmbajtjen."));
    }

    private void loadAdminData() {
        Perdoruesi admin = SessionManager.getCurrentUser();
        if (admin != null) {
            String fullName = admin.getEmri() + " " + admin.getMbiemri();
            userFullName.setText(fullName);
            userEmail.setText(admin.getEmail());
            avatarLabel.setText(admin.getEmri().substring(0, 1).toUpperCase());
        }
    }

    private void setupSidebarActions() {
        if (navDashboard != null) navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        if (navFluturimet != null) navFluturimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET));
        if (navRezervimet != null) navRezervimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET));
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navStafi != null) navStafi.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_STAFI));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
    }

    private void setupTableColumns() {
        colAvioniId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdAvionit()).asObject());
        colNumriRegjistrit.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getNumriRegjistrit())));
        colProdhuesi.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getProdhuesi())));
        colModeli.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getModeli())));
        colVitiProdhimit.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getVitiProdhimit())));
        colStatusiAvionit.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getStatusi())));

        colLlojiSherbimit.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getLlojiSherbimit())));
        colDataFillimit.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getDataFillimit())));
        colDataMbarimit.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getDataMbarimit())));
        colPershkrimiPunes.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getPershkrimiPunes())));
        colKostoja.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getKostoja()));
        colStatusiMirembajtjes.setCellValueFactory(data -> new SimpleStringProperty(nvl(data.getValue().getStatusi())));
    }

    private void loadAvionetNgaDB() {
        avionetMaster.setAll(avionetService.getAllAvionet());
        tblAvionet.setItems(avionetMaster);
    }

    private void filterAvionet(String query) {
        if (query == null || query.trim().isEmpty()) {
            tblAvionet.setItems(avionetMaster);
            return;
        }

        String q = query.trim().toLowerCase();
        ObservableList<AvionetTableDto> filtered = FXCollections.observableArrayList();

        for (AvionetTableDto a : avionetMaster) {
            boolean matches =
                    contains(a.getNumriRegjistrit(), q) ||
                            contains(a.getProdhuesi(), q) ||
                            contains(a.getModeli(), q) ||
                            contains(a.getStatusi(), q) ||
                            contains(a.getEmriKompanise(), q);

            if (matches) {
                filtered.add(a);
            }
        }

        tblAvionet.setItems(filtered);
    }

    private void loadMirembajtjaPerAvionin(int idAvionit) {
        mirembajtjaMaster.setAll(mirembajtjaService.getByAvionId(idAvionit));
        tblMirembajtja.setItems(mirembajtjaMaster);
    }

    @FXML
    private void handleShtoAvion(ActionEvent event) {
        Router.navigateTo(ViewsEnum.ADMIN_SHTO_AVION);
    }

    @FXML
    private void handleShtoMirembajtje(ActionEvent event) {
        if (tblAvionet.getSelectionModel().getSelectedItem() == null) {
            showAlert(Alert.AlertType.WARNING, "Kujdes", "Selekto një avion para regjistrimit të shërbimit teknik.");
            return;
        }
        Router.navigateTo(ViewsEnum.ADMIN_SHTO_MIREMBAJTJE);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    private boolean contains(String value, String q) {
        return value != null && value.toLowerCase().contains(q);
    }

    private String nvl(String s) {
        return s == null ? "" : s;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}