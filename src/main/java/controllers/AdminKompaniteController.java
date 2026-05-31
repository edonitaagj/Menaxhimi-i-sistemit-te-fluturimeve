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
import models.KompaniteAjrore;
import models.Linjat;

// Shënim: Këto modele artificiale shërbejnë për të mbushur ComboBox-et tuaja vizualisht
class Shteti {
    int id; String emri;
    Shteti(int id, String emri) { this.id = id; this.emri = emri; }
    @Override public String toString() { return emri; }
}

class Aeroporti {
    int id; String kodi; String qyteti;
    Aeroporti(int id, String kodi, String qyteti) { this.id = id; this.kodi = kodi; this.qyteti = qyteti; }
    @Override public String toString() { return kodi + " - " + qyteti; }
}

public class AdminKompaniteController {
    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    // --- ELEMENTET E FORMËS: KOMPANITË ---
    @FXML private TextField txtKompaniaEmri;
    @FXML private TextField txtKodiIata;
    @FXML private TextField txtKodiIcao;
    @FXML private TextField txtEmriShkurter;
    @FXML private TextField txtFaqjaWeb;
    @FXML private TextField txtKompaniaTelefoni;
    @FXML private ComboBox<Shteti> cmbKompaniaShteti;
    @FXML private CheckBox chkKompaniaAktive;
    @FXML private Button btnSaveKompania;
    @FXML private Button btnDeleteKompania;
    @FXML private TextField txtSearchKompania;

    // --- TABELA: KOMPANITË ---
    @FXML private TableView<KompaniteAjrore> tblKompanite;
    @FXML private TableColumn<KompaniteAjrore, Integer> colKompId;
    @FXML private TableColumn<KompaniteAjrore, String> colKompEmri;
    @FXML private TableColumn<KompaniteAjrore, String> colKompIata;
    @FXML private TableColumn<KompaniteAjrore, String> colKompIcao;
    @FXML private TableColumn<KompaniteAjrore, Integer> colKompShteti;
    @FXML private TableColumn<KompaniteAjrore, String> colKompWeb;
    @FXML private TableColumn<KompaniteAjrore, Boolean> colKompStatusi;

    // --- ELEMENTET E FORMËS: LINJAT ---
    @FXML private ComboBox<Aeroporti> cmbAeroportiNisjes;
    @FXML private ComboBox<Aeroporti> cmbAeroportiMberritjes;
    @FXML private TextField txtDistanca;
    @FXML private TextField txtKohaFluturimit;
    @FXML private CheckBox chkLinjaAktive;
    @FXML private Button btnSaveLinja;
    @FXML private Button btnDeleteLinja;
    @FXML private TextField txtSearchLinja;

    // --- TABELA: LINJAT ---
    @FXML private TableView<Linjat> tblLinjat;
    @FXML private TableColumn<Linjat, Integer> colLinjaId;
    @FXML private TableColumn<Linjat, Integer> colLinjaNisja;
    @FXML private TableColumn<Linjat, Integer> colLinjaMberritja;
    @FXML private TableColumn<Linjat, Integer> colLinjaDistanca;
    @FXML private TableColumn<Linjat, Integer> colLinjaKoha;
    @FXML private TableColumn<Linjat, Boolean> colLinjaStatusi;

    // --- LISTAT PROGRAMATIKE (OBSERVABLE LISTS) ---
    private ObservableList<KompaniteAjrore> listaKompanive = FXCollections.observableArrayList();
    private ObservableList<Linjat> listaLinjave = FXCollections.observableArrayList();

    private KompaniteAjrore kompaniaESelektuar = null;
    private Linjat linjaESelektuar = null;

    @FXML
    public void initialize() {
        // !!! FIX-I KRYESOR: Thirrja e metodës së sidebar-it që të aktivizohet navigimi !!!
        setupSidebarActions();

        initKompaniteTable();
        initLinjatTable();
        loadMockData();

        // Monitorimi i klikimeve në Tabelën e Kompanive
        tblKompanite.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mbushFormenKompania(newSelection);
            }
        });

        // Monitorimi i klikimeve në Tabelën e Linjave
        tblLinjat.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mbushFormenLinja(newSelection);
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
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
    }

    // --- KONFIGURIMI I TABELËS SË KOMPANIVE ---
    private void initKompaniteTable() {
        colKompId.setCellValueFactory(new PropertyValueFactory<>("idKompanise"));
        colKompEmri.setCellValueFactory(new PropertyValueFactory<>("emri"));
        colKompIata.setCellValueFactory(new PropertyValueFactory<>("kodiIata"));
        colKompIcao.setCellValueFactory(new PropertyValueFactory<>("kodiIcao"));
        colKompShteti.setCellValueFactory(new PropertyValueFactory<>("idVendit"));
        colKompWeb.setCellValueFactory(new PropertyValueFactory<>("faqjaWeb"));

        colKompStatusi.setCellValueFactory(new PropertyValueFactory<>("eshteAktive"));
        colKompStatusi.setCellFactory(column -> new TableCell<>() {
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

        tblKompanite.setItems(listaKompanive);
    }

    // --- KONFIGURIMI I TABELËS SË LINJAVE ---
    private void initLinjatTable() {
        colLinjaId.setCellValueFactory(new PropertyValueFactory<>("idLinjes"));
        colLinjaNisja.setCellValueFactory(new PropertyValueFactory<>("idAeroportitNisjes"));
        colLinjaMberritja.setCellValueFactory(new PropertyValueFactory<>("idAeroportitMbrrritjes"));
        colLinjaDistanca.setCellValueFactory(new PropertyValueFactory<>("distancaKm"));
        colLinjaKoha.setCellValueFactory(new PropertyValueFactory<>("kohaFluturimitMin"));

        colLinjaStatusi.setCellValueFactory(new PropertyValueFactory<>("eshteAktive"));
        colLinjaStatusi.setCellFactory(column -> new TableCell<>() {
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

        tblLinjat.setItems(listaLinjave);
    }

    // --- VEPRIMET PËR KOMPANITË ---
    @FXML
    private void handleSaveKompania() {
        if (txtKompaniaEmri.getText().isEmpty() || txtKodiIata.getText().isEmpty() || cmbKompaniaShteti.getValue() == null) {
            shfaqAlert("Gabim Validimi", "Ju lutem plotësoni fushat e detyrueshme (*).", Alert.AlertType.WARNING);
            return;
        }

        if (kompaniaESelektuar == null) {
            KompaniteAjrore eRe = new KompaniteAjrore(
                    listaKompanive.size() + 1,
                    cmbKompaniaShteti.getValue().id,
                    txtKodiIata.getText(),
                    txtKodiIcao.getText(),
                    txtKompaniaEmri.getText(),
                    txtEmriShkurter.getText(),
                    txtFaqjaWeb.getText(),
                    txtKompaniaTelefoni.getText(),
                    chkKompaniaAktive.isSelected()
            );
            listaKompanive.add(eRe);
        } else {
            kompaniaESelektuar.setEmri(txtKompaniaEmri.getText());
            kompaniaESelektuar.setKodiIata(txtKodiIata.getText());
            kompaniaESelektuar.setKodiIcao(txtKodiIcao.getText());
            kompaniaESelektuar.setEmriIShkurter(txtEmriShkurter.getText());
            kompaniaESelektuar.setIdVendit(cmbKompaniaShteti.getValue().id);
            kompaniaESelektuar.setFaqjaWeb(txtFaqjaWeb.getText());
            kompaniaESelektuar.setTelefoni(txtKompaniaTelefoni.getText());
            kompaniaESelektuar.setEshteAktive(chkKompaniaAktive.isSelected());
            tblKompanite.refresh();
        }
        handleClearKompania();
    }

    @FXML
    private void handleDeleteKompania() {
        if (kompaniaESelektuar != null) {
            listaKompanive.remove(kompaniaESelektuar);
            handleClearKompania();
        }
    }

    @FXML
    private void handleClearKompania() {
        txtKompaniaEmri.clear();
        txtKodiIata.clear();
        txtKodiIcao.clear();
        txtEmriShkurter.clear();
        txtFaqjaWeb.clear();
        txtKompaniaTelefoni.clear();
        cmbKompaniaShteti.setValue(null);
        chkKompaniaAktive.setSelected(true);
        btnDeleteKompania.setDisable(true);
        kompaniaESelektuar = null;
        tblKompanite.getSelectionModel().clearSelection();
    }

    private void mbushFormenKompania(KompaniteAjrore komp) {
        kompaniaESelektuar = komp;
        txtKompaniaEmri.setText(komp.getEmri());
        txtKodiIata.setText(komp.getKodiIata());
        txtKodiIcao.setText(komp.getKodiIcao());
        txtEmriShkurter.setText(komp.getEmriIShkurter());
        txtFaqjaWeb.setText(komp.getFaqjaWeb());
        txtKompaniaTelefoni.setText(komp.getTelefoni());
        chkKompaniaAktive.setSelected(komp.getEshteAktive());

        for (Shteti s : cmbKompaniaShteti.getItems()) {
            if (s.id == komp.getIdVendit()) {
                cmbKompaniaShteti.setValue(s);
                break;
            }
        }
        btnDeleteKompania.setDisable(false);
    }

    @FXML
    private void handleSearchKompania() {
        String query = txtSearchKompania.getText().toLowerCase();
        if (query.isEmpty()) {
            tblKompanite.setItems(listaKompanive);
        } else {
            ObservableList<KompaniteAjrore> filtered = FXCollections.observableArrayList();
            for (KompaniteAjrore k : listaKompanive) {
                if (k.getEmri().toLowerCase().contains(query) || k.getKodiIata().toLowerCase().contains(query)) {
                    filtered.add(k);
                }
            }
            tblKompanite.setItems(filtered);
        }
    }

    // --- VEPRIMET PËR LINJAT ---
    @FXML
    private void handleSaveLinja() {
        if (cmbAeroportiNisjes.getValue() == null || cmbAeroportiMberritjes.getValue() == null) {
            shfaqAlert("Gabim Validimi", "Zgjidhni aeroportin e nisjes dhe atë të mbërritjes.", Alert.AlertType.WARNING);
            return;
        }

        int distanca = txtDistanca.getText().isEmpty() ? 0 : Integer.parseInt(txtDistanca.getText());
        int koha = txtKohaFluturimit.getText().isEmpty() ? 0 : Integer.parseInt(txtKohaFluturimit.getText());

        if (linjaESelektuar == null) {
            Linjat eRe = new Linjat(
                    listaLinjave.size() + 1,
                    cmbAeroportiNisjes.getValue().id,
                    cmbAeroportiMberritjes.getValue().id,
                    distanca,
                    koha,
                    chkLinjaAktive.isSelected()
            );
            listaLinjave.add(eRe);
        } else {
            linjaESelektuar.setIdAeroportitNisjes(cmbAeroportiNisjes.getValue().id);
            linjaESelektuar.setIdAeroportitMbrrritjes(cmbAeroportiMberritjes.getValue().id);
            linjaESelektuar.setDistancaKm(distanca);
            linjaESelektuar.setKohaFluturimitMin(koha);
            linjaESelektuar.setEshteAktive(chkLinjaAktive.isSelected());
            tblLinjat.refresh();
        }
        handleClearLinja();
    }

    @FXML
    private void handleDeleteLinja() {
        if (linjaESelektuar != null) {
            listaLinjave.remove(linjaESelektuar);
            handleClearLinja();
        }
    }

    @FXML
    private void handleClearLinja() {
        cmbAeroportiNisjes.setValue(null);
        cmbAeroportiMberritjes.setValue(null);
        txtDistanca.clear();
        txtKohaFluturimit.clear();
        chkLinjaAktive.setSelected(true);
        btnDeleteLinja.setDisable(true);
        linjaESelektuar = null;
        tblLinjat.getSelectionModel().clearSelection();
    }

    private void mbushFormenLinja(Linjat linja) {
        linjaESelektuar = linja;
        txtDistanca.setText(String.valueOf(linja.getDistancaKm()));
        txtKohaFluturimit.setText(String.valueOf(linja.getKohaFluturimitMin()));
        chkLinjaAktive.setSelected(linja.getEshteAktive());

        for (Aeroporti a : cmbAeroportiNisjes.getItems()) {
            if (a.id == linja.getIdAeroportitNisjes()) cmbAeroportiNisjes.setValue(a);
        }
        for (Aeroporti a : cmbAeroportiMberritjes.getItems()) {
            if (a.id == linja.getIdAeroportitMbrrritjes()) cmbAeroportiMberritjes.setValue(a);
        }
        btnDeleteLinja.setDisable(false);
    }

    @FXML
    private void handleSearchLinja() {
        String query = txtSearchLinja.getText().toLowerCase();
        if (query.isEmpty()) {
            tblLinjat.setItems(listaLinjave);
        } else {
            ObservableList<Linjat> filtered = FXCollections.observableArrayList();
            for (Linjat l : listaLinjave) {
                if (String.valueOf(l.getIdLinjes()).contains(query)) {
                    filtered.add(l);
                }
            }
            tblLinjat.setItems(filtered);
        }
    }

    @FXML
    private void handleLogout() {
        // Pastrojmë sesionin ekzistues dhe kthehemi te ekrani i Login-it
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    private void shfaqAlert(String titulli, String mesazhi, Alert.AlertType tip) {
        Alert alert = new Alert(tip);
        alert.setTitle(titulli);
        alert.setHeaderText(null);
        alert.setContentText(mesazhi);
        alert.showAndWait();
    }

    private void loadMockData() {
        cmbKompaniaShteti.setItems(FXCollections.observableArrayList(
                new Shteti(1, "Kosovë"), new Shteti(2, "Shqipëri"), new Shteti(3, "Austri"), new Shteti(4, "Gjermani")
        ));

        ObservableList<Aeroporti> aeroportet = FXCollections.observableArrayList(
                new Aeroporti(1, "PRN", "Prishtinë"), new Aeroporti(2, "TIA", "Tiranë"),
                new Aeroporti(3, "VIE", "Vjenë"), new Aeroporti(4, "FRA", "Frankfurt")
        );
        cmbAeroportiNisjes.setItems(aeroportet);
        cmbAeroportiMberritjes.setItems(aeroportet);

        listaKompanive.add(new KompaniteAjrore(1, 3, "OS", "AUA", "Austrian Airlines", "Austrian", "https://www.austrian.com", null, true));
        listaKompanive.add(new KompaniteAjrore(2, 4, "LH", "DLH", "Lufthansa", "Lufthansa", "https://www.lufthansa.com", null, true));

        listaLinjave.add(new Linjat(1, 1, 3, 800, 90, true));
        listaLinjave.add(new Linjat(2, 3, 1, 800, 90, true));
    }
}