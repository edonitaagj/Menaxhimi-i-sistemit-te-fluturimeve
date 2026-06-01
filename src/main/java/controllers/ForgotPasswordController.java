package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import models.dto.ForgotPasswordRequestDto;
import models.dto.ForgotPasswordResponseDto;
import services.AuthService;

public class ForgotPasswordController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML private HBox messageBox;
    @FXML private Label messageIcon;
    @FXML private Label messageLabel;
    @FXML private Region messageSpacer;

    private final AuthService authService = new AuthService();

    @FXML
    private void initialize() {
        hideMessage();
    }

    @FXML
    private void handleResetPassword() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto(
                text(usernameField),
                text(emailField),
                password(newPasswordField),
                password(confirmPasswordField)
        );

        ForgotPasswordResponseDto response = authService.resetPassword(request);

        if (response.isSuccess()) {
            showSuccess(response.getMessage());
            clearPasswordFields();
        } else {
            showError(response.getMessage());
        }
    }

    @FXML
    private void handleBackToLogin() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    private String text(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private String password(PasswordField field) {
        return field.getText() == null ? "" : field.getText();
    }

    private void clearPasswordFields() {
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showSuccess(String message) {
        messageIcon.setText("OK");
        messageLabel.setText(message);
        messageBox.setStyle("-fx-background-color: #ECFDF5; -fx-border-color: #86EFAC; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 12 16 12 16;");
        messageLabel.setStyle("-fx-text-fill: #047857; -fx-font-size: 13; -fx-font-weight: 700;");
        messageIcon.setStyle("-fx-text-fill: #047857; -fx-font-size: 12; -fx-font-weight: 900;");
        showMessage();
    }

    private void showError(String message) {
        messageIcon.setText("!");
        messageLabel.setText(message);
        messageBox.setStyle("-fx-background-color: #FEF2F2; -fx-border-color: #FCA5A5; -fx-border-width: 1; -fx-border-radius: 10; -fx-background-radius: 10; -fx-padding: 12 16 12 16;");
        messageLabel.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 13; -fx-font-weight: 700;");
        messageIcon.setStyle("-fx-text-fill: #DC2626; -fx-font-size: 14; -fx-font-weight: 900;");
        showMessage();
    }

    private void showMessage() {
        messageBox.setVisible(true);
        messageBox.setManaged(true);
        messageSpacer.setVisible(true);
        messageSpacer.setManaged(true);
    }

    private void hideMessage() {
        if (messageBox != null) {
            messageBox.setVisible(false);
            messageBox.setManaged(false);
        }
        if (messageSpacer != null) {
            messageSpacer.setVisible(false);
            messageSpacer.setManaged(false);
        }
    }
}
