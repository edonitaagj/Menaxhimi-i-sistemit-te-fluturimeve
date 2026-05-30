package app;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) {
        Router.setStage(stage);
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    public static void main(String[] args) {
        launch(args);
    }
}