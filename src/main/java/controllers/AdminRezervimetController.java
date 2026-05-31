package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import models.Perdoruesi;
import models.dto.RezervimeTableDto;
import services.AdminRezervimetService;
import services.RezervimetService;

public class AdminRezervimetController {

    @FXML private HBox navDashboard;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navAvionet;
    @FXML private HBox navHumbur;
    @FXML private HBox navStafi;
    @FXML private HBox navPasagjeret;
    @FXML private HBox navKompanite;

    @FXML private TextField txtSearchRezervimi;

    @FXML private TableView<RezervimeTableDto> tblRezervimet;
    @FXML private TableColumn<RezervimeTableDto, String> colRezervimiId;
    @FXML private TableColumn<RezervimeTableDto, String> colPasagjeri;
    @FXML private TableColumn<RezervimeTableDto, String> colFluturimiKodi;
    @FXML private TableColumn<RezervimeTableDto, String> colKlasa;
    @FXML private TableColumn<RezervimeTableDto, Double> colCmimi;
    @FXML private TableColumn<RezervimeTableDto, String> colStatusiPageses;

    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

    private final AdminRezervimetService rezervimetService = new AdminRezervimetService();
    private final ObservableList<RezervimeTableDto> listaRezervimeve = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupSidebarActions();
        initTableColumns();
        loadRezervimetNgaDB();
        loadAdminData();

        if (txtSearchRezervimi != null) {
            txtSearchRezervimi.textProperty().addListener((observable, oldValue, newValue) -> handleSearchRezervimi());
        }

        if (tblRezervimet != null) {
            tblRezervimet.setPlaceholder(new Label("Nuk u gjet asnjë rezervim."));
        }
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
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navStafi != null) navStafi.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_STAFI));
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
    }

    private void initTableColumns() {
        colRezervimiId.setCellValueFactory(new PropertyValueFactory<>("kodiRezervimit"));
        colPasagjeri.setCellValueFactory(new PropertyValueFactory<>("pasagjeri"));
        colFluturimiKodi.setCellValueFactory(new PropertyValueFactory<>("fluturimiKodi"));
        colKlasa.setCellValueFactory(new PropertyValueFactory<>("klasa"));

        colCmimi.setCellValueFactory(new PropertyValueFactory<>("cmimiTotal"));
        colCmimi.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    RezervimeTableDto rez = getTableView().getItems().get(getIndex());
                    String monedha = (rez != null && rez.getMonedha() != null && !rez.getMonedha().isBlank())
                            ? rez.getMonedha()
                            : "EUR";
                    setText(String.format("%s %.2f", monedha, item));
                }
            }
        });

        colStatusiPageses.setCellValueFactory(new PropertyValueFactory<>("statusiPageses"));
        colStatusiPageses.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }

                if (item.equalsIgnoreCase("e_kryer") || item.equalsIgnoreCase("confirmed")) {
                    setText("🟢 E Kryer");
                    setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                } else if (item.equalsIgnoreCase("e_pritshme") || item.equalsIgnoreCase("pending")) {
                    setText("🟡 E Pritshme");
                    setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;");
                } else if (item.equalsIgnoreCase("e_deshtuar") || item.equalsIgnoreCase("e_kthyer")) {
                    setText("🔴 Anuluar / Dështuar");
                    setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #0F172A;");
                }
            }
        });

        tblRezervimet.setItems(listaRezervimeve);
    }

    private void loadRezervimetNgaDB() {
        listaRezervimeve.setAll(rezervimetService.getAllRezervimet());
        System.out.println("Rezervime të marra: " + listaRezervimeve.size());
        applyCurrentSearch();
    }

    private void handleSearchRezervimi() {
        applyCurrentSearch();
    }

    private void applyCurrentSearch() {
        String query = txtSearchRezervimi.getText() == null ? "" : txtSearchRezervimi.getText().toLowerCase().trim();

        if (query.isEmpty()) {
            tblRezervimet.setItems(listaRezervimeve);
            return;
        }

        ObservableList<RezervimeTableDto> filteredList = FXCollections.observableArrayList();
        for (RezervimeTableDto r : listaRezervimeve) {
            boolean matches =
                    contains(r.getKodiRezervimit(), query) ||
                            contains(r.getPasagjeri(), query) ||
                            contains(r.getFluturimiKodi(), query) ||
                            contains(r.getKlasa(), query) ||
                            contains(r.getStatusi(), query) ||
                            contains(r.getStatusiPageses(), query) ||
                            String.valueOf(r.getIdRezervimit()).contains(query) ||
                            String.valueOf(r.getIdPasagjerit()).contains(query);

            if (matches) {
                filteredList.add(r);
            }
        }
        tblRezervimet.setItems(filteredList);
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    private boolean contains(String value, String query) {
        return value != null && value.toLowerCase().contains(query);
    }
}