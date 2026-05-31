package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import models.Fluturimet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import java.sql.Date;
import java.time.LocalDate;

public class AdminFluturimetController {

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
    @FXML private TextField adminSearchField;

    // --- ELEMENTET E FORMËS SË FLUTURIMIT ---
    @FXML private TextField txtKodi;
    @FXML private ComboBox<String> cmbKompania;
    @FXML private TextField txtOrigjina;
    @FXML private TextField txtDestinacioni;
    @FXML private ComboBox<String> cmbStatusi;

    // --- TABELA DHE KOLONAT ---
    @FXML private TableView<Fluturimet> tblFluturimet;
    @FXML private TableColumn<Fluturimet, Integer> colId;
    @FXML private TableColumn<Fluturimet, String> colKodi;
    @FXML private TableColumn<Fluturimet, String> colKompania; // Do të shfaqet si emër string/ID
    @FXML private TableColumn<Fluturimet, String> colOrigjina;
    @FXML private TableColumn<Fluturimet, String> colDestinacioni;
    @FXML private TableColumn<Fluturimet, Integer> colGate;
    @FXML private TableColumn<Fluturimet, String> colStatusi;
    @FXML private TableColumn<Fluturimet, Void> colVeprimet; // Për butonin fshij/modifiko brenda rreshtit

    // Lista programatike e fluturimeve
    private ObservableList<Fluturimet> listaFluturimeve = FXCollections.observableArrayList();
    private Fluturimet fluturimiESelektuar = null;

    @FXML
    public void initialize() {
        // Aktivizojmë klikimet në Sidebar
        setupSidebarActions();

        // Inicializojmë kolonat e tabelës
        initTableColumns();

        // Ngarkojmë të dhënat fillestare në ComboBox-e dhe Tabelë
        loadInitialData();

        // Monitorojmë përzgjedhjen e rreshtave në tabelë për mbushjen e formës (për editim)
        tblFluturimet.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                mbushFormenFluturim(newSelection);
            }
        });
    }

    private void setupSidebarActions() {
        if (navDashboard != null) navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        if (navRezervimet != null) navRezervimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_REZERVIMET));
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navStafi != null) navStafi.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_STAFI));
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
        // navFluturimet është faqja aktuale
    }

    private void initTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idFluturimit"));
        colKodi.setCellValueFactory(new PropertyValueFactory<>("numriFluturimit"));
        colKompania.setCellValueFactory(new PropertyValueFactory<>("idKompanise")); // Mund të kovertohet në emër sipas nevojës
        colOrigjina.setCellValueFactory(new PropertyValueFactory<>("idLinjes"));     // Ose shto getOrigjina në model nëse lidhet me DB
        colDestinacioni.setCellValueFactory(new PropertyValueFactory<>("idLinjes"));
        colGate.setCellValueFactory(new PropertyValueFactory<>("idGejtitNisjes"));
        colStatusi.setCellValueFactory(new PropertyValueFactory<>("statusi"));

        // Formatimi i shtyllës së statusit me ngjyra sipas vlerës
        colStatusi.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.equalsIgnoreCase("Scheduled") || item.equalsIgnoreCase("Në Kohë")) {
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;"); // E gjelbër
                    } else if (item.equalsIgnoreCase("Delayed") || item.equalsIgnoreCase("Anuluar")) {
                        setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;"); // E kuqe
                    } else {
                        setStyle("-fx-text-fill: #3A94F2; -fx-font-weight: bold;"); // E kaltër
                    }
                }
            }
        });

        // Krijimi i butonit "Fshij" programatikisht për çdo rresht te kolona VEPRIMET
        setupVeprimetColumn();

        tblFluturimet.setItems(listaFluturimeve);
    }

    private void setupVeprimetColumn() {
        colVeprimet.setCellFactory(param -> new TableCell<>() {
            private final Button btnDelete = new Button("🗑 Fshij");
            {
                btnDelete.setStyle("-fx-background-color: rgba(239,68,68,0.1); -fx-text-fill: #EF4444; -fx-font-weight: 700; -fx-background-radius: 4; -fx-padding: 4 8; -fx-cursor: hand;");
                btnDelete.setOnAction(event -> {
                    Fluturimet f = getTableView().getItems().get(getIndex());
                    listaFluturimeve.remove(f);
                    shfaqAlert("Sukses", "Fluturimi u fshi me sukses!", Alert.AlertType.INFORMATION);
                    handleClearForm();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btnDelete);
                }
            }
        });
    }

    // --- CRUD OPERACIONET ---

    @FXML
    private void handleSaveFluturim() {
        // Validimi i fushave fillestare të detyrueshme
        if (txtKodi.getText().isEmpty() || cmbKompania.getValue() == null ||
                txtOrigjina.getText().isEmpty() || txtDestinacioni.getText().isEmpty() ||
                cmbStatusi.getValue() == null) {

            shfaqAlert("Gabim Validimi", "Ju lutem plotësoni të gjitha fushat e formës.", Alert.AlertType.WARNING);
            return;
        }

        if (fluturimiESelektuar == null) {
            // SHTIM I RI (INSERT)
            Fluturimet iRi = new Fluturimet(
                    listaFluturimeve.size() + 1,               // idFluturimit
                    null,                                      // idOrarit
                    cmbKompania.getSelectionModel().getSelectedIndex() + 1, // idKompanise (shembull)
                    100 + listaFluturimeve.size(),             // idLinjes
                    1,                                         // idAvionit
                    txtKodi.getText(),                         // numriFluturimit
                    Date.valueOf(LocalDate.now()),             // dataFluturimit
                    1,                                         // idGejtitNisjes
                    null, null, null, null, null,              // Timestamps
                    cmbStatusi.getValue(),                     // statusi
                    null,                                      // shkakuVoneses
                    180,                                       // kapacitetiTotal
                    180,                                       // vendetELira
                    49.99                                      // cmimiBaze
            );
            listaFluturimeve.add(iRi);
            shfaqAlert("Sukses", "Fluturimi u planifikua me sukses!", Alert.AlertType.INFORMATION);
        } else {
            // MODIFIKIM (UPDATE)
            fluturimiESelektuar.setNumriFluturimit(txtKodi.getText());
            fluturimiESelektuar.setStatusi(cmbStatusi.getValue());
            // Pasi që modelet e tjera (Linjat, Kompanitë) mund të mbajnë IDs, këtu përditësoni ID-të përkatëse

            tblFluturimet.refresh();
            shfaqAlert("Sukses", "Fluturimi u përditësua me sukses!", Alert.AlertType.INFORMATION);
        }
        handleClearForm();
    }

    @FXML
    private void handleClearForm() {
        txtKodi.clear();
        cmbKompania.setValue(null);
        txtOrigjina.clear();
        txtDestinacioni.clear();
        cmbStatusi.setValue(null);

        fluturimiESelektuar = null;
        tblFluturimet.getSelectionModel().clearSelection();
    }

    private void mbushFormenFluturim(Fluturimet f) {
        fluturimiESelektuar = f;
        txtKodi.setText(f.getNumriFluturimit());
        cmbStatusi.setValue(f.getStatusi());

        // Mbushja konvencionale e teksteve ndihmëse për ID-të
        txtOrigjina.setText("PRN");
        txtDestinacioni.setText("MUC");
        cmbKompania.getSelectionModel().select(0);
    }

    // --- KËRKIMI DINAMIK (SEARCH) ---
    @FXML
    private void handleAdminSearch(KeyEvent event) {
        String query = adminSearchField.getText().toLowerCase().trim();
        if (query.isEmpty()) {
            tblFluturimet.setItems(listaFluturimeve);
            return;
        }

        ObservableList<Fluturimet> filteredList = FXCollections.observableArrayList();
        for (Fluturimet f : listaFluturimeve) {
            if (f.getNumriFluturimit().toLowerCase().contains(query) ||
                    f.getStatusi().toLowerCase().contains(query)) {
                filteredList.add(f);
            }
        }
        tblFluturimet.setItems(filteredList);
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

    // --- MBUSHJA ME TË DHËNA TESTUESE (INITIAL MOCK DATA) ---
    private void loadInitialData() {
        // Popullimi i ComboBox-eve
        cmbKompania.setItems(FXCollections.observableArrayList("Wizz Air", "Eurowings", "Austrian Airlines", "Turkish Airlines"));
        cmbStatusi.setItems(FXCollections.observableArrayList("Scheduled", "Boarding", "Delayed", "Departed", "Anuluar"));

        // Shtimi i disa fluturimeve fillestare duke përdorur konstruktorin tënd real
        listaFluturimeve.add(new Fluturimet(1, 10, 1, 101, 1, "W64211", Date.valueOf(LocalDate.now()),
                3, null, null, null, null, null, "Scheduled", null, 180, 120, 59.99));

        listaFluturimeve.add(new Fluturimet(2, 11, 2, 102, 2, "EW4312", Date.valueOf(LocalDate.now()),
                1, null, null, null, null, null, "Delayed", "Moti i lig", 150, 45, 89.99));

        listaFluturimeve.add(new Fluturimet(3, 12, 3, 103, 3, "OS722", Date.valueOf(LocalDate.now()),
                5, null, null, null, null, null, "Boarding", null, 200, 10, 129.99));
    }
}