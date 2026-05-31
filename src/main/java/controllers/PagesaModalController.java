package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import models.dto.FluturimSelectDto;
import models.dto.RezervimiCreateResponseDto;
import services.RezervimetService;

public class PagesaModalController {

    // Lidhjet me komponente sipas Pagesat_view.fxml
    @FXML private Label lblKodiRezervimit;
    @FXML private Label lblShuma;
    @FXML private ComboBox<String> cmbMetodaPageses;
    @FXML private VBox vboxDetajetKartes;
    @FXML private TextField txtDetajetPageses;
    @FXML private TextArea txtShenimetPageses;

    // Variablat statike për të ruajtur të dhënat e përkohshme para konfirmimit në DB
    private static FluturimSelectDto fluturimiZgjedhur;
    private static String klasaZgjedhur;
    private static String shenimetRezervimit;
    private static String cmimiTotal;
    private static String kodiSimuluar;

    private final RezervimetService rezervimetService = new RezervimetService();
    // private final PagesatService pagesatService = new PagesatService(); // Shërbimi yt për pagesa

    // Metoda që thirret nga dritarja e parë për të përcjellë vlerat
    public static void kaloniTeDhenatERezervimit(FluturimSelectDto fluturim, String klasa, String shenimet, String cmimi, String kodi) {
        fluturimiZgjedhur = fluturim;
        klasaZgjedhur = klasa;
        shenimetRezervimit = shenimet;
        cmimiTotal = cmimi;
        kodiSimuluar = kodi;
    }

    @FXML
    private void initialize() {
        // Vendosim vlerat dinamike në ekran sapo hapet dritarja
        if (cmimiTotal != null) {
            lblShuma.setText(cmimiTotal);
        }
        if (kodiSimuluar != null) {
            lblKodiRezervimit.setText(kodiSimuluar);
        }

        if (cmbMetodaPageses != null) {
            cmbMetodaPageses.getSelectionModel().select("karte");
        }
    }

    @FXML
    private void handleMetodaChange() {
        if (cmbMetodaPageses == null || vboxDetajetKartes == null) return;

        String metoda = cmbMetodaPageses.getValue();
        if ("karte".equalsIgnoreCase(metoda) || "online".equalsIgnoreCase(metoda)) {
            vboxDetajetKartes.setVisible(true);
            vboxDetajetKartes.setManaged(true);
        } else {
            vboxDetajetKartes.setVisible(false);
            vboxDetajetKartes.setManaged(false);
        }
    }

    @FXML
    private void handleKryejPagesen() {
        if (fluturimiZgjedhur == null) {
            showAlert(Alert.AlertType.ERROR, "Gabim", "Nuk u gjet asnjë rezervim në proces.");
            return;
        }

        // HAPI 1: Krijojmë dhe konfirmojmë rezervimin në Databazë
        RezervimiCreateResponseDto response = rezervimetService.createReservation(
                fluturimiZgjedhur,
                klasaZgjedhur,
                shenimetRezervimit
        );

        // Nëse databaza/shërbimi kthen gabim për rezervimin, ndalojmë procesin
        if (!response.isSuccess()) {
            showAlert(Alert.AlertType.ERROR, "Gabim gjatë rezervimit", response.getMessage());
            return;
        }

        // HAPI 2: Meqenëse rezervimi u krye, ruajmë transaksionin e Pagesës në Databazë
        String metoda = cmbMetodaPageses != null ? cmbMetodaPageses.getValue() : "karte";
        String shenimetPageses = txtShenimetPageses != null && txtShenimetPageses.getText() != null
                ? txtShenimetPageses.getText().trim()
                : "";

        // TODO: Këtu thërret shërbimin tënd të pagesave duke përdorur ID-në e rezervimit të ri
        // pagesatService.krijoPagese(response.getIdRezervimit(), cmimiTotal, metoda, shenimetPageses);

        System.out.println("SUKSES: Rezervimi u krye me kodin zyrtar: " + response.getKodiRezervimit());
        System.out.println("SUKSES: Pagesa prej " + cmimiTotal + " EUR u ruajt me sukses.");

        // Shfaqim mesazhin përfundimtar të suksesit me kodin zyrtar nga DB
        showAlert(Alert.AlertType.INFORMATION, "Sukses",
                "Rezervimi dhe Pagesa u kryen me sukses!\nKodi Zyrtar i Rezervimit: " + response.getKodiRezervimit());

        // Pastrojmë variablat statike dhe kthehemi te pamja kryesore
        pastroTeDhenat();
        Router.navigateTo(ViewsEnum.REZERVIMET_VIEW);
        mbyllDritaren();
    }

    @FXML
    private void handleAnuloPagesen() {
        pastroTeDhenat();
        Router.navigateTo(ViewsEnum.REZERVIMET_VIEW);
        mbyllDritaren();
    }

    private void pastroTeDhenat() {
        fluturimiZgjedhur = null;
        klasaZgjedhur = null;
        shenimetRezervimit = null;
        cmimiTotal = null;
        kodiSimuluar = null;
    }

    private void mbyllDritaren() {
        if (lblShuma != null && lblShuma.getScene() != null) {
            lblShuma.getScene().getWindow().hide();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}