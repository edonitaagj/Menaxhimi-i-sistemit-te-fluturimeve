package controllers;

import app.I18n;
import app.Router;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import models.dto.ForgotPasswordRequestDto;
import models.dto.ForgotPasswordResponseDto;
import services.ForgotPasswordService;

import java.util.Locale;

public class ForgotPasswordController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private HBox errBox;
    @FXML private Label errLabel;
    @FXML private Region errSpacer;
    @FXML private HBox successBox;
    @FXML private Label successLabel;
    @FXML private Region successSpacer;
    @FXML private Button languageButton;

    private final ForgotPasswordService forgotPasswordService = new ForgotPasswordService();

    @FXML
    private void initialize() {
        hideError();
        hideSuccess();
        updateLanguageButton();
    }

    @FXML
    private void handleResetPassword() {
        ForgotPasswordRequestDto request = new ForgotPasswordRequestDto(
                text(usernameField),
                text(emailField),
                password(newPasswordField),
                password(confirmPasswordField)
        );

        ForgotPasswordResponseDto response = forgotPasswordService.resetPassword(request);

        if (response.isSuccess()) {
            showSuccess(response.getMessage());
            hideError();
            clearPasswordFields();
            return;
        }

        showError(response.getMessage());
        hideSuccess();
    }

    @FXML
    private void handleBackToLogin() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    @FXML
    private void handleLanguageSwitch() {
        Locale nextLocale = I18n.getLocale().getLanguage().equals("sq")
                ? Locale.ENGLISH
                : Locale.forLanguageTag("sq");
        I18n.setLocale(nextLocale);
        Router.navigateTo(ViewsEnum.FORGOT_PASSWORD_VIEW);
    }

    private void updateLanguageButton() {
        if (languageButton != null) {
            languageButton.setText(I18n.getResourceBundle().getString("language.switch"));
        }
    }

    private String text(TextField field) {
        return field.getText() != null ? field.getText().trim() : "";
    }

    private String password(PasswordField field) {
        return field.getText() != null ? field.getText() : "";
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

    private void showSuccess(String message) {
        successLabel.setText(message);
        successBox.setVisible(true);
        successBox.setManaged(true);
        successSpacer.setVisible(true);
        successSpacer.setManaged(true);
    }

    private void hideSuccess() {
        if (successBox != null) {
            successBox.setVisible(false);
            successBox.setManaged(false);
        }
        if (successSpacer != null) {
            successSpacer.setVisible(false);
            successSpacer.setManaged(false);
        }
    }

    private void clearPasswordFields() {
        newPasswordField.clear();
        confirmPasswordField.clear();
    }
}
