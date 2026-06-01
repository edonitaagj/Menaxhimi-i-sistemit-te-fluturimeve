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
import javafx.scene.layout.VBox;
import models.dto.AdminFluturimiFormDto;
import models.dto.AdminFluturimiRowDto;
import models.dto.KompaniaItem;
import models.dto.OperacioniResponseDto;
import services.AdminFluturimiService;

import java.util.List;

public class AdminFluturimetController {

    // ── Sidebar nav ──────────────────────────────────────────────────────
    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    // ── Search bar ───────────────────────────────────────────────────────
    @FXML private TextField adminSearchField;

    // ── Forma ────────────────────────────────────────────────────────────
    @FXML private TextField               txtKodi;
    @FXML private ComboBox<KompaniaItem>  cmbKompania;
    @FXML private TextField               txtOrigjina;
    @FXML private TextField               txtDestinacioni;
    @FXML private ComboBox<String>        cmbStatusi;

    // ── TableView ────────────────────────────────────────────────────────
    @FXML private TableView<AdminFluturimiRowDto>                 tblFluturimet;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colId;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colKodi;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colKompania;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colOrigjina;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colDestinacioni;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colGate;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colStatusi;
    @FXML private TableColumn<AdminFluturimiRowDto, String>       colVeprimet;

    // ── Service ──────────────────────────────────────────────────────────
    private final AdminFluturimiService service = new AdminFluturimiService();

    // ── State ─────────────────────────────────────────────────────────────
    private FilteredList<AdminFluturimiRowDto> filteredList;

    // ═════════════════════════════════════════════════════════════════════
    @FXML
    private void initialize() {
        setupColumns();
        loadComboBoxes();
        loadFluturimet();
        setupSidebarActions();
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
    //  Setup kolonat
    // ════════════════════════════════════════════════════════════════════
    private void setupColumns() {
        colId.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getIdFluturimit())));
        colKodi.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getKodi()));
        colKompania.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getKompania()));
        colOrigjina.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getOrigjina()));
        colDestinacioni.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getDestinacioni()));
        colGate.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getGate()));
        colStatusi.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getStatusi()));

        // Ngjyrë statusi
        colStatusi.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) { setText(null); setStyle(""); return; }
                setText(formatStatusi(item));
                setStyle("-fx-text-fill: " + colorStatusi(item) +
                        "; -fx-font-weight: 800; -fx-font-size: 11;");
            }
        });

        // Kolona VEPRIMET — buton ndrysho statusin + fshij
        colVeprimet.setCellFactory(col -> new TableCell<>() {
            private final Button btnNdrysho = new Button("✏ Statusi");
            private final Button btnFshij   = new Button("🗑");
            private final HBox   box        = new HBox(6, btnNdrysho, btnFshij);

            {
                btnNdrysho.setStyle(
                        "-fx-background-color: #3A94F2; -fx-text-fill: white; " +
                                "-fx-font-size: 10; -fx-font-weight: 800; " +
                                "-fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 4 8;");
                btnFshij.setStyle(
                        "-fx-background-color: rgba(239,68,68,0.1); -fx-text-fill: #EF4444; " +
                                "-fx-font-size: 10; -fx-font-weight: 800; " +
                                "-fx-background-radius: 5; -fx-cursor: hand; -fx-padding: 4 8;");

                btnNdrysho.setOnAction(e -> {
                    AdminFluturimiRowDto row = getTableView().getItems().get(getIndex());
                    handleNdryshiStatusinDialog(row);
                });

                btnFshij.setOnAction(e -> {
                    AdminFluturimiRowDto row = getTableView().getItems().get(getIndex());
                    handleFshij(row.getIdFluturimit());
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : box);
                setText(null);
            }
        });
    }

    // ════════════════════════════════════════════════════════════════════
    //  Load të dhënat
    // ════════════════════════════════════════════════════════════════════
    private void loadFluturimet() {
        try {
            List<AdminFluturimiRowDto> rows = service.getAll();
            filteredList = new FilteredList<>(
                    FXCollections.observableArrayList(rows), p -> true);
            tblFluturimet.setItems(filteredList);
            if (rows.isEmpty())
                tblFluturimet.setPlaceholder(new Label("Nuk ka fluturime të regjistruara."));
        } catch (Exception e) {
            tblFluturimet.setPlaceholder(
                    new Label("Gabim duke ngarkuar: " + e.getMessage()));
        }
    }

    private void loadComboBoxes() {
        // cmbKompania
        try {
            List<KompaniaItem> komp = service.getKompaniteItems();
            cmbKompania.setItems(FXCollections.observableArrayList(komp));
        } catch (Exception ignored) {}

        // cmbStatusi
        cmbStatusi.setItems(FXCollections.observableArrayList(service.getStatusetItems()));
        cmbStatusi.setValue("i_planifikuar");
    }

    // ════════════════════════════════════════════════════════════════════
    //  Handlers
    // ════════════════════════════════════════════════════════════════════
    @FXML
    private void handleSaveFluturim() {
        KompaniaItem kompania = cmbKompania.getValue();
        if (kompania == null) { showError("Zgjidhni kompaninë ajrore."); return; }

        AdminFluturimiFormDto dto = new AdminFluturimiFormDto(
                txtKodi.getText(),
                kompania.getId(),
                txtOrigjina.getText(),
                txtDestinacioni.getText(),
                cmbStatusi.getValue()
        );

        OperacioniResponseDto result = service.create(dto);
        showResult(result);
        if (result.isSuccess()) {
            handleClearForm();
            loadFluturimet();
        }
    }

    @FXML
    private void handleClearForm() {
        txtKodi.clear();
        txtOrigjina.clear();
        txtDestinacioni.clear();
        cmbKompania.setValue(null);
        cmbStatusi.setValue("i_planifikuar");
    }

    @FXML
    private void handleAdminSearch() {
        String term = adminSearchField.getText();
        if (filteredList == null) return;
        if (term == null || term.isBlank()) {
            filteredList.setPredicate(p -> true);
            return;
        }
        String low = term.toLowerCase();
        filteredList.setPredicate(r ->
                r.getKodi().toLowerCase().contains(low)        ||
                        r.getKompania().toLowerCase().contains(low)    ||
                        r.getOrigjina().toLowerCase().contains(low)    ||
                        r.getDestinacioni().toLowerCase().contains(low)
        );
    }

    // ── Ndrysho statusin me dialog ────────────────────────────────────────
    private void handleNdryshiStatusinDialog(AdminFluturimiRowDto row) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>(
                row.getStatusi(), service.getStatusetItems());
        dialog.setTitle("Ndrysho Statusin");
        dialog.setHeaderText("Fluturimi: " + row.getKodi());
        dialog.setContentText("Zgjidhni statusin e ri:");

        dialog.showAndWait().ifPresent(statusiRi -> {
            OperacioniResponseDto result = service.updateStatusi(
                    row.getIdFluturimit(), statusiRi);
            showResult(result);
            if (result.isSuccess()) loadFluturimet();
        });
    }

    // ── Fshij ─────────────────────────────────────────────────────────────
    private void handleFshij(int idFluturimit) {
        if (!confirm("Konfirmo fshirjen",
                "A jeni i sigurt? Ky veprim fshihet rezervimet e lidhura.")) return;

        OperacioniResponseDto result = service.delete(idFluturimit);
        showResult(result);
        if (result.isSuccess()) loadFluturimet();
    }


    @FXML private void handleNavDashboard()  { Router.navigateTo(ViewsEnum.ADMIN_VIEW); }
    @FXML private void handleNavRezervimet() { Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET); }
    @FXML private void handleNavAvionet()    { Router.navigateTo(ViewsEnum.ADMIN_AVIONET); }
    @FXML private void handleNavHumbur()     { Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR); }
    @FXML private void handleNavStafi()      { Router.navigateTo(ViewsEnum.ADMIN_STAFI); }
    @FXML private void handleNavPasagjeret() { Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT); }
    @FXML private void handleNavKompanite()  { Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE); }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }


    // ════════════════════════════════════════════════════════════════════
    //  UI helpers
    // ════════════════════════════════════════════════════════════════════
    private String formatStatusi(String s) {
        return switch (s.toLowerCase()) {
            case "i_planifikuar" -> "I PLANIFIKUAR";
            case "boarding"      -> "BOARDING";
            case "ngritur"       -> "NGRITUR";
            case "ne_fluturim"   -> "NË FLUTURIM";
            case "zbritur"       -> "ZBRITUR";
            case "mberriti"      -> "MBËRRITI";
            case "anuluar"       -> "ANULUAR";
            case "i_vonuar"      -> "I VONUAR";
            case "devijuar"      -> "DEVIJUAR";
            default              -> s.toUpperCase();
        };
    }

    private String colorStatusi(String s) {
        return switch (s.toLowerCase()) {
            case "boarding"              -> "#3A94F2";
            case "ne_fluturim","ngritur" -> "#10B981";
            case "i_vonuar","devijuar"   -> "#F59E0B";
            case "anuluar"               -> "#EF4444";
            case "mberriti","zbritur"    -> "#64748B";
            default                      -> "#0F172A";
        };
    }

    private void showResult(OperacioniResponseDto r) {
        Alert.AlertType type = r.isSuccess()
                ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
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
                .filter(b -> b == ButtonType.OK).isPresent();
    }
}