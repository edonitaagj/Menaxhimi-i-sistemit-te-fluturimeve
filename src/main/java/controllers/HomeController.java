package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import models.Perdoruesi;
import models.dto.HomeFluturimiTableDto;
import repository.HomeRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class HomeController {

    // ===============================
    // USER INFO
    // ===============================
    @FXML private Label greetingLabel;
    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

    // ===============================
    // CLOCK
    // ===============================
    @FXML private Label clockLabel;
    @FXML private Label dateLabel;

    // ===============================
    // SEARCH
    // ===============================
    @FXML private TextField searchNga;
    @FXML private TextField searchDeri;
    @FXML private DatePicker searchData;

    // ===============================
    // STATS
    // ===============================
    @FXML private Label statFluturime;
    @FXML private Label statRezervimet;
    @FXML private Label statFluturimiArdhshem;
    @FXML private Label statFluturimiData;
    @FXML private Label statNjoftimet;
    @FXML private Label badgeNjoftime;

    // ===============================
    // TABLE
    // ===============================
    @FXML private TableView<HomeFluturimiTableDto> departureBoardTable;
    @FXML private TableColumn<HomeFluturimiTableDto, String> colFluturimi;
    @FXML private TableColumn<HomeFluturimiTableDto, String> colDestinacioni;
    @FXML private TableColumn<HomeFluturimiTableDto, String> colOra;
    @FXML private TableColumn<HomeFluturimiTableDto, String> colGejti;
    @FXML private TableColumn<HomeFluturimiTableDto, String> colStatusi;

    private final HomeRepository homeRepository = new HomeRepository();
    private final ObservableList<HomeFluturimiTableDto> fluturimet = FXCollections.observableArrayList();

    // ===============================
    // INIT
    // ===============================
    @FXML
    public void initialize() {
        loadUserData();
        startClock();
        initDepartureTable();
        loadDashboardFromDatabase();
        setupAccessibility();
        setupKeyboardShortcuts();
    }

    // ===============================
    // USER DATA
    // ===============================
    private void loadUserData() {
        Perdoruesi user = SessionManager.getCurrentUser();

        if (user != null) {
            String fullName = user.getEmri() + " " + user.getMbiemri();

            greetingLabel.setText(fullName);
            userFullName.setText(fullName);
            userEmail.setText(user.getEmail());

            avatarLabel.setText(
                    user.getEmri().substring(0, 1).toUpperCase()
            );
        }
    }

    // ===============================
    // CLOCK
    // ===============================
    private void startClock() {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateClock())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        updateClock();
    }

    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();

        clockLabel.setText(
                now.format(DateTimeFormatter.ofPattern("HH:mm"))
        );

        dateLabel.setText(
                now.format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy"))
        );
    }

    // ===============================
    // DATABASE DATA
    // ===============================
    private void initDepartureTable() {
        colFluturimi.setCellValueFactory(new PropertyValueFactory<>("fluturimi"));
        colDestinacioni.setCellValueFactory(new PropertyValueFactory<>("destinacioni"));
        colOra.setCellValueFactory(new PropertyValueFactory<>("ora"));
        colGejti.setCellValueFactory(new PropertyValueFactory<>("gejti"));
        colStatusi.setCellValueFactory(new PropertyValueFactory<>("statusi"));

        departureBoardTable.setItems(fluturimet);
        departureBoardTable.setPlaceholder(new Label("Nuk ka fluturime per kriteret e kerkuara."));
    }

    private void loadDashboardFromDatabase() {
        fluturimet.setAll(homeRepository.getFluturimetBoard());

        statFluturime.setText(String.valueOf(homeRepository.getTotalFlightsCount()));
        statRezervimet.setText(String.valueOf(homeRepository.getReservationsCount()));

        int notificationsCount = homeRepository.getNotificationsCount();
        statNjoftimet.setText(String.valueOf(notificationsCount));
        badgeNjoftime.setText(String.valueOf(notificationsCount));

        HomeFluturimiTableDto nextFlight = homeRepository.getNextFlight();
        if (nextFlight != null) {
            statFluturimiArdhshem.setText(nextFlight.getFluturimi());
            statFluturimiData.setText(nextFlight.getDestinacioni() + " - " + nextFlight.getData());
        } else {
            statFluturimiArdhshem.setText("-");
            statFluturimiData.setText("Nuk ka fluturime");
        }
    }

    // ===============================
    // ACCESSIBILITY
    // ===============================
    private void setupAccessibility() {
        searchNga.setFocusTraversable(true);
        searchDeri.setFocusTraversable(true);
        searchData.setFocusTraversable(true);
        departureBoardTable.setFocusTraversable(true);

        searchNga.setAccessibleText("Fusha per vendin e nisjes");
        searchDeri.setAccessibleText("Fusha per destinacionin");
        searchData.setAccessibleText("Zgjedhja e dates se udhetimit");
        departureBoardTable.setAccessibleText("Tabela me nisjet dhe udhetimet");

        greetingLabel.setAccessibleText("Pershendetje per perdoruesin aktual");
        userFullName.setAccessibleText("Emri dhe mbiemri i perdoruesit");
        userEmail.setAccessibleText("Email adresa e perdoruesit");
        avatarLabel.setAccessibleText("Iniciali i perdoruesit");
        clockLabel.setAccessibleText("Ora aktuale");
        dateLabel.setAccessibleText("Data aktuale");

        configureTabOrder(Arrays.asList(
                searchNga,
                searchDeri,
                searchData,
                departureBoardTable
        ));

        searchNga.setOnAction(e -> handleSearch());
        searchDeri.setOnAction(e -> handleSearch());
        searchData.setOnAction(e -> handleSearch());
    }

    private void configureTabOrder(List<Node> nodes) {
        for (Node node : nodes) {
            node.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.TAB) {
                    event.consume();

                    int currentIndex = nodes.indexOf(node);
                    int nextIndex;

                    if (event.isShiftDown()) {
                        nextIndex = currentIndex == 0 ? nodes.size() - 1 : currentIndex - 1;
                    } else {
                        nextIndex = currentIndex == nodes.size() - 1 ? 0 : currentIndex + 1;
                    }

                    nodes.get(nextIndex).requestFocus();
                }
            });
        }
    }

    // ===============================
    // KEYBOARD SHORTCUTS
    // ===============================
    private void setupKeyboardShortcuts() {
        Platform.runLater(() -> {
            Scene scene = searchNga.getScene();

            if (scene == null) {
                return;
            }

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.F, KeyCombination.SHORTCUT_DOWN),
                    () -> searchNga.requestFocus()
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN),
                    this::handleSearch
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                    this::handleNavRezervimet
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN),
                    this::handleNavBileta
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN),
                    this::handleNavNjoftimet
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN),
                    this::handleNavProfili
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.H, KeyCombination.SHORTCUT_DOWN),
                    this::handleNavHelp
            );

            scene.getAccelerators().put(
                    new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN),
                    this::handleLogout
            );
        });
    }

    // ===============================
    // SEARCH ACTION
    // ===============================
    @FXML
    private void handleSearch() {
        String nga = searchNga.getText();
        String deri = searchDeri.getText();
        var data = searchData.getValue();

        fluturimet.setAll(homeRepository.searchFluturimet(nga, deri, data));
    }

    // ===============================
    // NAVIGATION
    // ===============================
    @FXML
    private void handleNavRezervimet() {
        Router.navigateTo(ViewsEnum.REZERVIMET_VIEW);
    }

    @FXML
    private void handleNavBileta() {
        Router.navigateTo(ViewsEnum.BILETAT_VIEW);
    }

    @FXML
    private void handleNavNjoftimet() {
        Router.navigateTo(ViewsEnum.NJOFTIMET_VIEW);
    }

    @FXML
    private void handleNavProfili() {
        Router.navigateTo(ViewsEnum.PROFIL_VIEW);
    }

    @FXML
    private void handleNavHelp() {
        Router.navigateTo(ViewsEnum.HELP_VIEW);
    }

    // ===============================
    // LOGOUT
    // ===============================
    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}
