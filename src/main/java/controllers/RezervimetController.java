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
import models.dto.OperationResponseDto;
import models.dto.RezervimiTableDto;
import services.RezervimetService;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class RezervimetController {

    @FXML private TableView<RezervimiTableDto> tblRezervimet;
    @FXML private TableColumn<RezervimiTableDto, String> colKodiRezervimit;
    @FXML private TableColumn<RezervimiTableDto, String> colFluturimi;
    @FXML private TableColumn<RezervimiTableDto, String> colRelacioni;
    @FXML private TableColumn<RezervimiTableDto, String> colData;
    @FXML private TableColumn<RezervimiTableDto, String> colKlasa;
    @FXML private TableColumn<RezervimiTableDto, String> colCmimi;
    @FXML private TableColumn<RezervimiTableDto, String> colStatusi;
    @FXML private TableColumn<RezervimiTableDto, Void> colVeprimet;

    @FXML private HBox navHome;
    @FXML private HBox navFluturimet;
    @FXML private HBox navRezervimet;
    @FXML private HBox navBileta;
    @FXML private HBox navNjoftimet;
    @FXML private HBox navProfili;

    private final RezervimetService rezervimetService = new RezervimetService();
    private final ObservableList<RezervimiTableDto> rezervimetData = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        setupColumns();
        loadRezervimet();
    }

    private void setupColumns() {
        // Zëvendësimi i PropertyValueFactory me Lambda expressions
        colKodiRezervimit.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKodiRezervimit()));
        colFluturimi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumriFluturimit()));
        colRelacioni.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRelacioni()));
        colData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataRezervimit()));
        colKlasa.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKlasa()));
        colCmimi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCmimiTotal()));
        colStatusi.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusi()));

        // Pjesa ekzistuese e butonit "Anulo" (lihet ashtu siç ishte)
        colVeprimet.setCellFactory(param -> new TableCell<>() {
            private final Button cancelBtn = new Button("Anulo");
            private final HBox box = new HBox(cancelBtn);

            {
                box.setSpacing(8);
                cancelBtn.setStyle("""
                    -fx-background-color: rgba(239,68,68,0.10);
                    -fx-text-fill: #DC2626;
                    -fx-font-size: 11;
                    -fx-font-weight: 800;
                    -fx-background-radius: 7;
                    -fx-border-color: rgba(239,68,68,0.20);
                    -fx-border-radius: 7;
                    -fx-cursor: hand;
                    -fx-padding: 6 12 6 12;
                    """);

                cancelBtn.setOnAction(e -> {
                    RezervimiTableDto row = getTableView().getItems().get(getIndex());
                    handleCancelReservation(row);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                RezervimiTableDto row = getTableView().getItems().get(getIndex());
                boolean canCancel = row != null
                        && row.getStatusi() != null
                        && !row.getStatusi().equalsIgnoreCase("Anuluar")
                        && !row.getStatusi().equalsIgnoreCase("Mberriti")
                        && !row.getStatusi().equalsIgnoreCase("Bordinguar");

                cancelBtn.setDisable(!canCancel);
                cancelBtn.setOpacity(canCancel ? 1.0 : 0.45);

                setGraphic(box);
            }
        });
    }
    private void loadRezervimet() {
        List<RezervimiTableDto> lista = rezervimetService.getMyRezervimet();
        rezervimetData.setAll(lista);
        tblRezervimet.setItems(rezervimetData);
    }

    private void handleCancelReservation(RezervimiTableDto row) {
        if (row == null) {
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Konfirmo anulimin");
        confirm.setHeaderText("A dëshironi ta anuloni këtë rezervim?");
        confirm.setContentText("Kodi rezervimit: " + row.getKodiRezervimit());

        ButtonType result = confirm.showAndWait().orElse(ButtonType.CANCEL);
        if (result != ButtonType.OK) {
            return;
        }

        OperationResponseDto response = rezervimetService.cancelMyReservation(row.getIdRezervimit());

        Alert alert = new Alert(response.isSuccess() ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(response.isSuccess() ? "Sukses" : "Gabim");
        alert.setHeaderText(null);
        alert.setContentText(response.getMessage());
        alert.showAndWait();

        if (response.isSuccess()) {
            loadRezervimet();
        }
    }

    @FXML private void handleNavHome() { Router.navigateTo(ViewsEnum.HOME_VIEW); }
    @FXML private void handleNavBileta() { Router.navigateTo(ViewsEnum.BILETAT_VIEW); }
    @FXML private void handleNavNjoftimet() { Router.navigateTo(ViewsEnum.NJOFTIMET_VIEW); }
    @FXML private void handleNavProfili() { Router.navigateTo(ViewsEnum.PROFIL_VIEW); }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    @FXML
    private void handleHapModalRezervim() {
        Router.navigateTo(ViewsEnum.SHTO_REZERVIM);
    }
}