package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.HBox;
import models.Perdoruesi;
import models.dto.NjoftimiTableDto;
import services.NjoftimetService;

import java.util.List;

public class NjoftimetController {

    @FXML private TableView<NjoftimiTableDto> tblNjoftimet;
    @FXML private TableColumn<NjoftimiTableDto, String> colTip;
    @FXML private TableColumn<NjoftimiTableDto, String> colMesazhi;
    @FXML private TableColumn<NjoftimiTableDto, String> colDataNjoftimit;

    @FXML private HBox navHome;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navBileta;
    @FXML private HBox navNjoftimet;
    @FXML private HBox navProfili;

    @FXML private Label userFullName;
    @FXML private Label userEmail;
    @FXML private Label avatarLabel;

    private final NjoftimetService njoftimetService = new NjoftimetService();
    private final ObservableList<NjoftimiTableDto> njoftimetData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupColumns();
        loadUserInfo();
        loadNjoftimet();
        loadUserData();

        if (navNjoftimet != null) {
            navNjoftimet.setStyle(
                    "-fx-background-color: rgba(58,148,242,0.15);" +
                            "-fx-border-color: rgba(58,148,242,0.4);" +
                            "-fx-border-width: 0 0 0 3;" +
                            "-fx-background-radius: 0 8 8 0;" +
                            "-fx-padding: 11 16 11 13;" +
                            "-fx-cursor: hand;"
            );
        }
    }

    private void loadUserData() {
        Perdoruesi user = SessionManager.getCurrentUser();

        if (user != null) {
            String fullName = user.getEmri() + " " + user.getMbiemri();

            userFullName.setText(fullName);
            userEmail.setText(user.getEmail());

            avatarLabel.setText(
                    user.getEmri().substring(0, 1).toUpperCase()
            );
        }
    }

    private void setupColumns() {
        // Përdorimi i Lambdas parandalon problemet me module-info dhe reflection
        colTip.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTipi()));

        colMesazhi.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getMesazhi()));

        colDataNjoftimit.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDataNjoftimit()));
    }

    private void loadUserInfo() {
        Perdoruesi user = SessionManager.getCurrentUser();
        if (user == null) return;

        // Nëse do, mund t’i lidhësh këto me fx:id në FXML.
        // Tani po e lë thjesht funksionale për tabelën.
    }

    private void loadNjoftimet() {
        List<NjoftimiTableDto> lista = njoftimetService.getAllNotifications();

        // Printo në konsolë numrin e rreshtave të gjetur për siguri
        System.out.println("Njoftime të gjetura në DB: " + lista.size());

        njoftimetData.setAll(lista);
        tblNjoftimet.setItems(njoftimetData);
    }

    @FXML
    private void handleMarkAllRead() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacion");
        alert.setHeaderText(null);
        alert.setContentText(
                "Në databazën aktuale nuk ekziston kolonë për 'të lexuara'. " +
                        "Nëse shton një fushë si `eshte_lexuar`, mund ta bëjmë edhe këtë funksional."
        );
        alert.showAndWait();
    }

    @FXML
    private void handleNavHome() {
        Router.navigateTo(ViewsEnum.HOME_VIEW);
    }

    @FXML
    private void handleNavRezervimet() {
        Router.navigateTo(ViewsEnum.REZERVIMET_VIEW);
    }

    @FXML
    private void handleNavBileta() {
        Router.navigateTo(ViewsEnum.BILETAT_VIEW);
    }

    @FXML
    private void handleNavProfili() {
        Router.navigateTo(ViewsEnum.PROFIL_VIEW);
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }
}