package app;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

public class Router {

    private static Stage stage;

    private Router() {}

    public static void setStage(Stage stage) {
        Router.stage = stage;
    }

    public static Stage getStage() {
        return stage;
    }

    private static Parent loadView(String viewPath) {
        try {
            FXMLLoader loader = new FXMLLoader(Router.class.getResource(viewPath));
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load view: " + viewPath, e);
        }
    }

    /**
     * Ndryshon krejt skenën (p.sh. login, signup).
     */
    public static void navigateTo(ViewsEnum view) {
        if (stage == null) {
            throw new IllegalStateException("Stage is not initialized. Call Router.setStage(stage) first.");
        }

        Parent root = loadView(view.getValue());
        stage.setScene(new Scene(root));
        stage.setTitle("Aeroporti Adem Jashari");
        stage.show();
    }

    /**
     * Zëvendëson përmbajtjen e një Pane (p.sh. BorderPane qendror)
     * me një pamje tjetër, pa ndryshuar skenën kryesore.
     */
    public static void navigateContentTo(ViewsEnum view, Pane container) {
        Parent content = loadView(view.getValue());
        container.getChildren().setAll(content);
    }
}