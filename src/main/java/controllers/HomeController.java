package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import models.Perdoruesi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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