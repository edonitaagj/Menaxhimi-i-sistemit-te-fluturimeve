package controllers;

import app.Router;
import app.SessionManager;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import models.Perdoruesi;
import models.dto.LoginResponseDto;
import repository.UserRepository;
import services.AuthService;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passField;
    @FXML private Label errLabel;
    @FXML private HBox errBox;
    @FXML private Region errSpacer;

    private final AuthService authService = new AuthService();
    private final UserRepository userRepo = new UserRepository();

    @FXML
    private void initialize() {
        hideError();
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText() != null ? usernameField.getText().trim() : "";
        String password = passField.getText() != null ? passField.getText() : "";

        if (username.isEmpty() || password.isEmpty()) {
            showError("Ju lutemi plotesoni te gjitha fushat.");
            return;
        }

        LoginResponseDto response = authService.login(username, password);

        if (!response.isLogin()) {
            showError(response.getMessage());
            return;
        }

        Perdoruesi user = userRepo.findByUsername(username);
        if (user == null) {
            showError("Login u pranua, por perdoruesi nuk u gjet ne databaze.");
            return;
        }

        SessionManager.login(user);

        String role = user.getRoli() != null ? user.getRoli().trim().toLowerCase() : "";

        switch (role) {
            case "admin" -> Router.navigateTo(ViewsEnum.ADMIN_VIEW);
            case "klient" -> Router.navigateTo(ViewsEnum.HOME_VIEW);
            default -> Router.navigateTo(ViewsEnum.HOME_VIEW);
        }
    }

    @FXML
    private void handleForgotPassword() {
        Router.navigateTo(ViewsEnum.FORGOT_PASSWORD_VIEW);
    }

    @FXML
    private void handleSignUp() {
        Router.navigateTo(ViewsEnum.SIGNUP_VIEW);
    }

    private void showError(String message) {
        errLabel.setText(message);
        errBox.setVisible(true);
        errBox.setManaged(true);
        errSpacer.setVisible(true);
        errSpacer.setManaged(true);
    }

    private void hideError() {
        if (errBox != null) {
            errBox.setVisible(false);
            errBox.setManaged(false);
        }
        if (errSpacer != null) {
            errSpacer.setVisible(false);
            errSpacer.setManaged(false);
        }
    }
}
