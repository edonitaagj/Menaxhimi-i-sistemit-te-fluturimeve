package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import models.KompaniteAjrore;
import models.Linjat;
import models.Perdoruesi;
import models.dto.*;
import services.KompaniaService;
import services.LinjaService;
import services.VendiService;
import services.AeroportiService;

import java.util.List;
import java.util.Map;

public class AdminKompaniteController {

    // ── Sidebar ──────────────────────────────────────────────────────────
    @FXML private HBox  navDashboard;
    @FXML private HBox  navFluturimet;
    @FXML private HBox  navRezervimet;
    @FXML private HBox  navAvionet;
    @FXML private HBox  navHumbur;
    @FXML private HBox  navStafi;
    @FXML private HBox  navPasagjeret;
    @FXML private HBox  navKompanite;
    @FXML private Label avatarLabel;
    @FXML private Label userFullName;
    @FXML private Label userEmail;

    // ── Forma e Kompanisë ─────────────────────────────────────────────────
    @FXML private TextField txtKompaniaEmri;
    @FXML private TextField txtKodiIata;
    @FXML private TextField txtKodiIcao;
    @FXML private TextField txtEmriShkurter;
    @FXML private ComboBox<VendiItem>  cmbKompaniaShteti;
    @FXML private TextField txtFaqjaWeb;
    @FXML private TextField txtKompaniaTelefoni;
    @FXML private CheckBox  chkKompaniaAktive;
    @FXML private Button    btnSaveKompania;
    @FXML private Button    btnDeleteKompania;

    // ── Tabela e Kompanive ────────────────────────────────────────────────
    @FXML private TableView<KompaniaRowDto>                 tblKompanite;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompId;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompEmri;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompIata;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompIcao;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompShteti;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompWeb;
    @FXML private TableColumn<KompaniaRowDto, String>       colKompStatusi;
    @FXML private TextField txtSearchKompania;

    // ── Forma e Linjës ────────────────────────────────────────────────────
    @FXML private ComboBox<AeroportiItem> cmbAeroportiNisjes;
    @FXML private ComboBox<AeroportiItem> cmbAeroportiMberritjes;
    @FXML private TextField txtDistanca;
    @FXML private TextField txtKohaFluturimit;
    @FXML private CheckBox  chkLinjaAktive;
    @FXML private Button    btnSaveLinja;
    @FXML private Button    btnDeleteLinja;

    // ── Tabela e Linjave ──────────────────────────────────────────────────
    @FXML private TableView<LinjaRowDto>                 tblLinjat;
    @FXML private TableColumn<LinjaRowDto, String>       colLinjaId;
    @FXML private TableColumn<LinjaRowDto, String>       colLinjaNisja;
    @FXML private TableColumn<LinjaRowDto, String>       colLinjaMberritja;
    @FXML private TableColumn<LinjaRowDto, String>       colLinjaDistanca;
    @FXML private TableColumn<LinjaRowDto, String>       colLinjaKoha;
    @FXML private TableColumn<LinjaRowDto, String>       colLinjaStatusi;
    @FXML private TextField txtSearchLinja;

    // ── Services ─────────────────────────────────────────────────────────
    private final KompaniaService  kompaniaService  = new KompaniaService();
    private final LinjaService     linjaService     = new LinjaService();
    private final VendiService     vendiService     = new VendiService();
    private final AeroportiService aeroportiService = new AeroportiService();

    // ── State ─────────────────────────────────────────────────────────────
    private int selectedKompaniaId = -1;   // -1 = mode krijo
    private int selectedLinjaId    = -1;

    private FilteredList<KompaniaRowDto> filteredKompanite;
    private FilteredList<LinjaRowDto>    filteredLinjat;

    // ═════════════════════════════════════════════════════════════════════
    @FXML
    private void initialize() {
        loadUserInfo();
        setupKompaniaTable();
        setupLinjaTable();
        loadKompanite();
        loadLinjat();
        loadComboBoxes();
        setupSidebarActions();
    }

    // ════════════════════════════════════════════════════════════════════
    //  User info
    // ════════════════════════════════════════════════════════════════════
    private void loadUserInfo() {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null) return;
        String emri = user.getEmri() != null ? user.getEmri() : user.getUsername();
        avatarLabel.setText(emri.substring(0, 1).toUpperCase());
        userFullName.setText(user.getFullName());
        userEmail.setText(nvl(user.getEmail(), ""));
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

    // ════════════════════════════════════════════════════════════════════
    //  Setup kolonat — Kompani
    // ════════════════════════════════════════════════════════════════════
    private void setupKompaniaTable() {
        colKompId.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdKompanise())));
        colKompEmri.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getEmri()));
        colKompIata.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getKodiIata()));
        colKompIcao.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getKodiIcao()));
        colKompShteti.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getEmriIShkurter()));
        colKompWeb.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getFaqjaWeb()));
        colKompStatusi.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getStatusi()));

        // Ngjyrë statusi
        colKompStatusi.setCellFactory(col -> statusCell());

        // Klikim në rresht → ngarko në formë
        tblKompanite.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, row) -> { if (row != null) onKompaniaSelected(row); });
    }

    // ════════════════════════════════════════════════════════════════════
    //  Setup kolonat — Linja
    // ════════════════════════════════════════════════════════════════════
    private void setupLinjaTable() {
        colLinjaId.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdLinjes())));
        colLinjaNisja.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getAeroportiNisjes()));
        colLinjaMberritja.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getAeroportiMbrrritjes()));
        colLinjaDistanca.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getDistanca()));
        colLinjaKoha.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getKoha()));
        colLinjaStatusi.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getStatusi()));

        colLinjaStatusi.setCellFactory(col -> statusCell());

        tblLinjat.getSelectionModel().selectedItemProperty().addListener(
                (obs, old, row) -> { if (row != null) onLinjaSelected(row); });
    }

    // ════════════════════════════════════════════════════════════════════
    //  Load të dhënat
    // ════════════════════════════════════════════════════════════════════
    private void loadKompanite() {
        try {
            List<KompaniaRowDto> rows = kompaniaService.getAll();
            filteredKompanite = new FilteredList<>(
                    FXCollections.observableArrayList(rows), p -> true);
            tblKompanite.setItems(filteredKompanite);
        } catch (Exception e) {
            showError("Gabim duke ngarkuar kompanitë: " + e.getMessage());
        }
    }

    private void loadLinjat() {
        try {
            List<LinjaRowDto> rows = linjaService.getAll();
            filteredLinjat = new FilteredList<>(
                    FXCollections.observableArrayList(rows), p -> true);
            tblLinjat.setItems(filteredLinjat);
        } catch (Exception e) {
            showError("Gabim duke ngarkuar linjat: " + e.getMessage());
        }
    }

    private void loadComboBoxes() {
        // Vendet → cmbKompaniaShteti
        try {
            List<VendiItem> vendet = vendiService.getAllAsItems();
            cmbKompaniaShteti.setItems(FXCollections.observableArrayList(vendet));
        } catch (Exception ignored) {}

        // Aeroportet → cmbAeroportiNisjes + cmbAeroportiMberritjes
        try {
            List<AeroportiItem> aeroportet = aeroportiService.getAllAsItems();
            cmbAeroportiNisjes.setItems(FXCollections.observableArrayList(aeroportet));
            cmbAeroportiMberritjes.setItems(FXCollections.observableArrayList(aeroportet));
        } catch (Exception ignored) {}
    }

    // ════════════════════════════════════════════════════════════════════
    //  Klikim në tabelë → ngarko formën
    // ════════════════════════════════════════════════════════════════════
    private void onKompaniaSelected(KompaniaRowDto row) {
        selectedKompaniaId = row.getIdKompanise();
        KompaniteAjrore k = kompaniaService.getById(selectedKompaniaId);
        if (k == null) return;

        txtKompaniaEmri.setText(nvl(k.getEmri(), ""));
        txtKodiIata.setText(nvl(k.getKodiIata(), ""));
        txtKodiIcao.setText(nvl(k.getKodiIcao(), ""));
        txtEmriShkurter.setText(nvl(k.getEmriIShkurter(), ""));
        txtFaqjaWeb.setText(nvl(k.getFaqjaWeb(), ""));
        txtKompaniaTelefoni.setText(nvl(k.getTelefoni(), ""));
        chkKompaniaAktive.setSelected(k.getEshteAktive());

        // Zgjidhni shtetin në ComboBox
        cmbKompaniaShteti.getItems().stream()
                .filter(v -> v.getId() == k.getIdVendit())
                .findFirst()
                .ifPresent(cmbKompaniaShteti::setValue);

        btnDeleteKompania.setDisable(false);
        btnSaveKompania.setText("💾 Përditëso");
    }

    private void onLinjaSelected(LinjaRowDto row) {
        selectedLinjaId = row.getIdLinjes();
        Linjat l = linjaService.getById(selectedLinjaId);
        if (l == null) return;

        cmbAeroportiNisjes.getItems().stream()
                .filter(a -> a.getId() == l.getIdAeroportitNisjes())
                .findFirst().ifPresent(cmbAeroportiNisjes::setValue);

        cmbAeroportiMberritjes.getItems().stream()
                .filter(a -> a.getId() == l.getIdAeroportitMbrrritjes())
                .findFirst().ifPresent(cmbAeroportiMberritjes::setValue);

        txtDistanca.setText(l.getDistancaKm() != null ? String.valueOf(l.getDistancaKm()) : "");
        txtKohaFluturimit.setText(l.getKohaFluturimitMin() != null ? String.valueOf(l.getKohaFluturimitMin()) : "");
        chkLinjaAktive.setSelected(l.isEshteAktive());

        btnDeleteLinja.setDisable(false);
        btnSaveLinja.setText("💾 Përditëso");
    }

    // ════════════════════════════════════════════════════════════════════
    //  Handlers — Kompani
    // ════════════════════════════════════════════════════════════════════
    @FXML
    private void handleSaveKompania() {
        KompaniaFormDto dto = buildKompaniaDto();
        if (dto == null) return;

        OperacioniResponseDto result = selectedKompaniaId == -1
                ? kompaniaService.create(dto)
                : kompaniaService.update(selectedKompaniaId, dto);

        showResult(result);
        if (result.isSuccess()) {
            handleClearKompania();
            loadKompanite();
        }
    }

    @FXML
    private void handleDeleteKompania() {
        if (selectedKompaniaId == -1) return;
        if (!confirm("Konfirmo fshirjen", "A jeni i sigurt që doni të fshini këtë kompani?")) return;

        OperacioniResponseDto result = kompaniaService.delete(selectedKompaniaId);
        showResult(result);
        if (result.isSuccess()) { handleClearKompania(); loadKompanite(); }
    }

    @FXML
    private void handleClearKompania() {
        selectedKompaniaId = -1;
        txtKompaniaEmri.clear(); txtKodiIata.clear(); txtKodiIcao.clear();
        txtEmriShkurter.clear(); txtFaqjaWeb.clear(); txtKompaniaTelefoni.clear();
        cmbKompaniaShteti.setValue(null);
        chkKompaniaAktive.setSelected(true);
        btnDeleteKompania.setDisable(true);
        btnSaveKompania.setText("💾 Ruaj");
        tblKompanite.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearchKompania() {
        String term = txtSearchKompania.getText();
        if (filteredKompanite == null) return;
        if (term == null || term.isBlank()) {
            filteredKompanite.setPredicate(p -> true);
            return;
        }
        String low = term.toLowerCase();
        filteredKompanite.setPredicate(r ->
                r.getEmri().toLowerCase().contains(low) ||
                        r.getKodiIata().toLowerCase().contains(low) ||
                        r.getKodiIcao().toLowerCase().contains(low));
    }

    // ════════════════════════════════════════════════════════════════════
    //  Handlers — Linja
    // ════════════════════════════════════════════════════════════════════
    @FXML
    private void handleSaveLinja() {
        LinjaFormDto dto = buildLinjaDto();
        if (dto == null) return;

        OperacioniResponseDto result = selectedLinjaId == -1
                ? linjaService.create(dto)
                : linjaService.update(selectedLinjaId, dto);

        showResult(result);
        if (result.isSuccess()) { handleClearLinja(); loadLinjat(); }
    }

    @FXML
    private void handleDeleteLinja() {
        if (selectedLinjaId == -1) return;
        if (!confirm("Konfirmo fshirjen", "A jeni i sigurt që doni të fshini këtë linjë?")) return;

        OperacioniResponseDto result = linjaService.delete(selectedLinjaId);
        showResult(result);
        if (result.isSuccess()) { handleClearLinja(); loadLinjat(); }
    }

    @FXML
    private void handleClearLinja() {
        selectedLinjaId = -1;
        cmbAeroportiNisjes.setValue(null);
        cmbAeroportiMberritjes.setValue(null);
        txtDistanca.clear(); txtKohaFluturimit.clear();
        chkLinjaAktive.setSelected(true);
        btnDeleteLinja.setDisable(true);
        btnSaveLinja.setText("💾 Ruaj");
        tblLinjat.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleSearchLinja() {
        String term = txtSearchLinja.getText();
        if (filteredLinjat == null) return;
        if (term == null || term.isBlank()) {
            filteredLinjat.setPredicate(p -> true);
            return;
        }
        String low = term.toLowerCase();
        filteredLinjat.setPredicate(r ->
                r.getAeroportiNisjes().toLowerCase().contains(low) ||
                        r.getAeroportiMbrrritjes().toLowerCase().contains(low));
    }


    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    // ════════════════════════════════════════════════════════════════════
    //  Build DTOs nga forma
    // ════════════════════════════════════════════════════════════════════
    private KompaniaFormDto buildKompaniaDto() {
        VendiItem vendi = cmbKompaniaShteti.getValue();
        if (vendi == null) {
            showError("Zgjidhni shtetin e origjinës.");
            return null;
        }
        return new KompaniaFormDto(
                txtKompaniaEmri.getText(),
                txtKodiIata.getText(),
                txtKodiIcao.getText(),
                txtEmriShkurter.getText(),
                vendi.getId(),
                txtFaqjaWeb.getText(),
                txtKompaniaTelefoni.getText(),
                chkKompaniaAktive.isSelected()
        );
    }

    private LinjaFormDto buildLinjaDto() {
        AeroportiItem nisja     = cmbAeroportiNisjes.getValue();
        AeroportiItem mbrrritja = cmbAeroportiMberritjes.getValue();
        if (nisja == null || mbrrritja == null) {
            showError("Zgjidhni të dy aeroportet.");
            return null;
        }
        Integer dist = parseInteger(txtDistanca.getText());
        Integer koha = parseInteger(txtKohaFluturimit.getText());
        return new LinjaFormDto(
                nisja.getId(), mbrrritja.getId(), dist, koha,
                chkLinjaAktive.isSelected()
        );
    }

    // ════════════════════════════════════════════════════════════════════
    //  UI helpers
    // ════════════════════════════════════════════════════════════════════
    private <T> TableCell<T, String> statusCell() {
        return new TableCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(item);
                setStyle("-fx-text-fill: " + (item.equals("Aktive") ? "#10B981" : "#EF4444") +
                        "; -fx-font-weight: 800; -fx-font-size: 11;");
            }
        };
    }

    private void showResult(OperacioniResponseDto r) {
        Alert.AlertType type = r.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        Alert alert = new Alert(type);
        alert.setTitle(r.isSuccess() ? "Sukses" : "Gabim");
        alert.setHeaderText(null);
        alert.setContentText(r.getMessage());
        alert.showAndWait();
    }

    private void showError(String msg) {
        showResult(OperacioniResponseDto.error(msg));
    }

    private boolean confirm(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        return alert.showAndWait()
                .filter(b -> b == ButtonType.OK)
                .isPresent();
    }

    private Integer parseInteger(String s) {
        if (s == null || s.isBlank()) return null;
        try { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return null; }
    }

    private String nvl(String v, String fb) {
        return (v != null && !v.isBlank()) ? v : fb;
    }
}