package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import models.Rezervimet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import java.sql.Timestamp;

public class AdminRezervimetController {

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
    @FXML private TextField txtSearchRezervimi;

    // --- TABELA DHE KOLONAT ---
    @FXML private TableView<Rezervimet> tblRezervimet;
    @FXML private TableColumn<Rezervimet, String> colRezervimiId;  // Mapohet me kodiRezervimit
    @FXML private TableColumn<Rezervimet, Integer> colPasagjeri;   // ID e Pasagjerit (Konvertohet dinamikisht ose shfaqet si ID)
    @FXML private TableColumn<Rezervimet, Integer> colFluturimiKodi; // ID e Fluturimit
    @FXML private TableColumn<Rezervimet, String> colKlasa;
    @FXML private TableColumn<Rezervimet, Double> colCmimi;
    @FXML private TableColumn<Rezervimet, String> colStatusiPageses; // Mapohet me statusi (e_kryer, e_pritshme, etj.)

    // Lista programatike e rezervimeve
    private ObservableList<Rezervimet> listaRezervimeve = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Aktivizojmë klikimet në Sidebar
        setupSidebarActions();

        // Inicializojmë kolonat e tabelës
        initTableColumns();

        // Ngarkojmë të dhënat fillestare testuese
        loadInitialData();

        // Monitorojmë ndryshimet në fushën e kërkimit (Kërkim Dinamik)
        txtSearchRezervimi.textProperty().addListener((observable, oldValue, newValue) -> {
            handleSearchRezervimi();
        });
    }

    private void setupSidebarActions() {
        if (navDashboard != null) navDashboard.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_VIEW));
        if (navFluturimet != null) navFluturimet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_FLUTURIMET));
        if (navAvionet != null) navAvionet.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_AVIONET));
        if (navHumbur != null) navHumbur.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_ARTIKUJT_HUMBUR));
        if (navStafi != null) navStafi.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_STAFI));
        if (navPasagjeret != null) navPasagjeret.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_PASAGJERIT));
        if (navKompanite != null) navKompanite.setOnMouseClicked(e -> Router.navigateTo(ViewsEnum.ADMIN_KOMPANITE));
        // navRezervimet është faqja aktuale
    }

    private void initTableColumns() {
        colRezervimiId.setCellValueFactory(new PropertyValueFactory<>("kodiRezervimit"));
        colPasagjeri.setCellValueFactory(new PropertyValueFactory<>("idPasagjerit"));
        colFluturimiKodi.setCellValueFactory(new PropertyValueFactory<>("idFluturimit"));
        colKlasa.setCellValueFactory(new PropertyValueFactory<>("klasa"));

        // Formatimi i kolonës së çmimit për të shfaqur monedhën (p.sh. € 150.00)
        colCmimi.setCellValueFactory(new PropertyValueFactory<>("cmimiTotal"));
        colCmimi.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    // Merr monedhën nga rreshti aktual nëse dëshiron, ose vendos simbolin € direkt
                    Rezervimet rez = getTableView().getItems().get(getIndex());
                    String simboliMonedhes = (rez != null && rez.getMonedha() != null) ? rez.getMonedha() : "€";
                    setText(String.format("%s %.2f", simboliMonedhes, item));
                }
            }
        });

        // Formatimi dhe ngjyrosja e statusit të pagesës/rezervimit
        colStatusiPageses.setCellValueFactory(new PropertyValueFactory<>("statusi"));
        colStatusiPageses.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    // Përshtatja e tekstit sipas vlerave të DB suaj ('e_kryer', 'e_pritshme', 'e_deshtuar')
                    if (item.equalsIgnoreCase("e_kryer") || item.equalsIgnoreCase("Confirmed")) {
                        setText("🟢 E Kryer");
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;"); // E gjelbër
                    } else if (item.equalsIgnoreCase("e_pritshme") || item.equalsIgnoreCase("Pending")) {
                        setText("🟡 E Pritshme");
                        setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;"); // E portokalltë
                    } else if (item.equalsIgnoreCase("e_deshtuar") || item.equalsIgnoreCase("e_kthyer")) {
                        setText("🔴 Anuluar / Dështuar");
                        setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;"); // E kuqe
                    } else {
                        setText(item);
                        setStyle("-fx-text-fill: #0F172A;");
                    }
                }
            }
        });

        tblRezervimet.setItems(listaRezervimeve);
    }

    // --- KËRKIMI DINAMIK (SEARCH) ---
    private void handleSearchRezervimi() {
        String query = txtSearchRezervimi.getText() == null ? "" : txtSearchRezervimi.getText().toLowerCase().trim();

        if (query.isEmpty()) {
            tblRezervimet.setItems(listaRezervimeve);
            return;
        }

        ObservableList<Rezervimet> filteredList = FXCollections.observableArrayList();
        for (Rezervimet r : listaRezervimeve) {
            // Kërkon sipas Kodit të Rezervimit, Klasës ose ID-ve si string
            if (r.getKodiRezervimit().toLowerCase().contains(query) ||
                    r.getKlasa().toLowerCase().contains(query) ||
                    String.valueOf(r.getIdPasagjerit()).contains(query)) {
                filteredList.add(r);
            }
        }
        tblRezervimet.setItems(filteredList);
    }

    // --- LOGOUT ---
    @FXML
    private void handleLogout() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    // --- MBUSHJA ME TË DHËNA TESTUESE (MOCK DATA) ---
    private void loadInitialData() {
        long tani = System.currentTimeMillis();

        // Krijimi i të dhënave fillestare duke përdorur konstruktorin tënd real nga models.Rezervimet
        listaRezervimeve.add(new Rezervimet(
                1, "PRN-7X89A", 102, 1, "Business", 250.00, "€", "e_kryer",
                new Timestamp(tani), null, "Pa kërkesa shtesë"
        ));

        listaRezervimeve.add(new Rezervimet(
                2, "PRN-3L12B", 55, 1, "Economy", 89.99, "€", "e_pritshme",
                new Timestamp(tani), null, "Valixhe ekstra"
        ));

        listaRezervimeve.add(new Rezervimet(
                3, "PRN-9P45C", 88, 2, "Economy", 120.50, "€", "e_kryer",
                new Timestamp(tani), null, null
        ));

        listaRezervimeve.add(new Rezervimet(
                4, "PRN-5K22D", 14, 3, "Business", 310.00, "€", "e_deshtuar",
                new Timestamp(tani), new Timestamp(tani), "Transaksioni u refuzua nga banka"
        ));
    }
}