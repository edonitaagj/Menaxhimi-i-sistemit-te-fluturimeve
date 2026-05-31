package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.Avionet;
import models.LlojetAvioneve;
import models.MirembajtjaAvioneve;

import java.sql.Timestamp;

public class AdminAvionetController {

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
    @FXML private TextField txtSearchAvioni;

    // ===============================
    // TABLE VIEW: AVIONET
    // ===============================
    @FXML private TableView<Avionet> tblAvionet;
    @FXML private TableColumn<Avionet, Integer> colAvioniId;
    @FXML private TableColumn<Avionet, String> colNumriRegjistrit;
    @FXML private TableColumn<Avionet, String> colProdhuesi;
    @FXML private TableColumn<Avionet, String> colModeli;
    @FXML private TableColumn<Avionet, String> colVitiProdhimit;
    @FXML private TableColumn<Avionet, String> colStatusiAvionit;

    // ===============================
    // TABLE VIEW: MIREMBAJTJA
    // ===============================
    @FXML private TableView<MirembajtjaAvioneve> tblMirembajtja;
    @FXML private TableColumn<MirembajtjaAvioneve, String> colLlojiSherbimit;
    @FXML private TableColumn<MirembajtjaAvioneve, String> colDataFillimit;
    @FXML private TableColumn<MirembajtjaAvioneve, String> colDataMbarimit;
    @FXML private TableColumn<MirembajtjaAvioneve, String> colPershkrimiPunes;
    @FXML private TableColumn<MirembajtjaAvioneve, Double> colKostoja;
    @FXML private TableColumn<MirembajtjaAvioneve, String> colStatusiMirembajtjes;

    private ObservableList<Avionet> listaAvioneve = FXCollections.observableArrayList();
    private ObservableList<MirembajtjaAvioneve> listaMirembajtjes = FXCollections.observableArrayList();

    // ===============================
    // INITIALIZE
    // ===============================
    @FXML
    public void initialize() {
        setupSidebarActions();
        setupTableColumns();

        // Ngarko të dhënat fillestare
        loadAvionetNgaDB();

        // MASTER-DETAIL LISTENER: Kur përzgjidhet një avion, ngarko historikun e mirëmbajtjes
        tblAvionet.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                loadMirembajtjaPerAvionin(newSelection.getIdAvionit());
            } else {
                tblMirembajtja.getItems().clear();
            }
        });

        // Kërkimi dinamik (onKeyReleased)
        if (txtSearchAvioni != null) {
            txtSearchAvioni.setOnKeyReleased(e -> handleSearchAvioni());
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
        // 1. Mapimi i kolonave për tabelën e Avionëve
        if (colAvioniId != null) colAvioniId.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getIdAvionit()).asObject());
        if (colNumriRegjistrit != null) colNumriRegjistrit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNumriRegjistrit()));
        if (colVitiProdhimit != null) colVitiProdhimit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVitiProdhimit()));
        if (colStatusiAvionit != null) colStatusiAvionit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusi()));

        // Prodhuesi dhe Modeli lidhen me idLlojit. Kur të bësh query në DB me JOIN, këto do të plotësohen automatikisht.
        if (colProdhuesi != null) {
            colProdhuesi.setCellValueFactory(data -> new SimpleStringProperty("Lloji ID: " + data.getValue().getIdLlojit()));
        }
        if (colModeli != null) {
            colModeli.setCellValueFactory(data -> new SimpleStringProperty("Modeli ID: " + data.getValue().getIdLlojit()));
        }

        // 2. Mapimi i kolonave për tabelën e Mirëmbajtjes
        if (colLlojiSherbimit != null) colLlojiSherbimit.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLlojiSherbimit()));
        if (colPershkrimiPunes != null) colPershkrimiPunes.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPershkrimiPunes()));
        if (colStatusiMirembajtjes != null) colStatusiMirembajtjes.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusi()));
        if (colKostoja != null) colKostoja.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getKostoja()).asObject());

        if (colDataFillimit != null) {
            colDataFillimit.setCellValueFactory(data -> {
                Timestamp t = data.getValue().getDataFillimit();
                return new SimpleStringProperty(t != null ? t.toString().substring(0, 16) : "-");
            });
        }
        if (colDataMbarimit != null) {
            colDataMbarimit.setCellValueFactory(data -> {
                Timestamp t = data.getValue().getDataMbarimit();
                return new SimpleStringProperty(t != null ? t.toString().substring(0, 16) : "-");
            });
        }
    }

    // ===============================
    // DATA LOADING METHODS
    // ===============================

    private void loadAvionetNgaDB() {
        System.out.println("Duke marrë listën e avionëve nga DB...");
        // TODO: Mbushe listën nga repository yt
        // listaAvioneve.clear();
        // listaAvioneve.addAll(avionetRepo.getAllAvionet());
        // tblAvionet.setItems(listaAvioneve);
    }

    private void loadMirembajtjaPerAvionin(int idAvionit) {
        System.out.println("Duke marrë historikun e mirëmbajtjes për avionin me ID: " + idAvionit);
        // TODO: Filtro të dhënat nga tabela mirembajtja_avioneve WHERE id_avionit = idAvionit
        // listaMirembajtjes.clear();
        // listaMirembajtjes.addAll(mirembajtjaRepo.getMirembajtjaByAvionId(idAvionit));
        // tblMirembajtja.setItems(listaMirembajtjes);
    }

    // ===============================
    // BUTTON & ACTION HANDLERS
    // ===============================

    @FXML
    private void handleShtoAvion(ActionEvent event) {
        System.out.println("U klikua shto avion i ri. Hap modalin ose formën e re...");
        // TODO: Router.openModal(ViewsEnum.SHTO_AVION_POPUP); ose hapja e një dritareje dialogu
    }

    @FXML
    private void handleShtoMirembajtje(ActionEvent event) {
        Avionet avioniSelektuar = tblAvionet.getSelectionModel().getSelectedItem();
        if (avioniSelektuar == null) {
            System.out.println("Ju lutem selektoni një avion nga tabela për të regjistruar një shërbim teknik!");
            return;
        }
        System.out.println("U klikua regjistrimi i shërbimit teknik për avionin: " + avioniSelektuar.getNumriRegjistrit());
        // TODO: Hap dialogun për shtimin e urdhër-punës së re
    }

    private void handleSearchAvioni() {
        String query = txtSearchAvioni.getText() != null ? txtSearchAvioni.getText().trim() : "";
        System.out.println("Duke kërkuar flotën ajrore për: " + query);
        // TODO: Filtro listën e avionëve
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}