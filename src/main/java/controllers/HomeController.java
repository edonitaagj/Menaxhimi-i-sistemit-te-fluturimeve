package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import models.Perdoruesi;

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
    // TABLE
    // ===============================
    @FXML private TableView<?> departureBoardTable;

    // ===============================
    // INIT
    // ===============================
    @FXML
    public void initialize() {
        loadUserData();
        startClock();
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
    // ACCESSIBILITY
    // ===============================
    private void setupAccessibility() {
        searchNga.setFocusTraversable(true);
        searchDeri.setFocusTraversable(true);
        searchData.setFocusTraversable(true);
        departureBoardTable.setFocusTraversable(true);

        searchNga.setAccessibleText("Fusha për vendin e nisjes");
        searchDeri.setAccessibleText("Fusha për destinacionin");
        searchData.setAccessibleText("Zgjedhja e datës së udhëtimit");
        departureBoardTable.setAccessibleText("Tabela me nisjet dhe udhëtimet");

        greetingLabel.setAccessibleText("Përshëndetje për përdoruesin aktual");
        userFullName.setAccessibleText("Emri dhe mbiemri i përdoruesit");
        userEmail.setAccessibleText("Email adresa e përdoruesit");
        avatarLabel.setAccessibleText("Iniciali i përdoruesit");
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

        System.out.println("Search: " + nga + " -> " + deri + " | " + data);

        // TODO: connect with DB
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