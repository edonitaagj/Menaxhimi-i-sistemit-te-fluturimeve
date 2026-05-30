package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import models.Perdoruesi;
import repository.UserRepository;
import services.HashService;

public class SignupController {

    @FXML private TextField emriField;
    @FXML private TextField mbiemriField;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmPassField;

    @FXML private HBox errBox;
    @FXML private Label errLabel;
    @FXML private Region errSpacer;

    private final UserRepository userRepo = new UserRepository();

    @FXML
    private void initialize() {
        hideError();
    }

    @FXML
    private void handleSignUp() {
        String emri = safeText(emriField);
        String mbiemri = safeText(mbiemriField);
        String username = safeText(usernameField);
        String email = safeText(emailField);
        String password = passField.getText() != null ? passField.getText().trim() : "";
        String confirmPassword = confirmPassField.getText() != null ? confirmPassField.getText().trim() : "";

        if (emri.isEmpty() || mbiemri.isEmpty() || username.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Ju lutemi plotësoni të gjitha fushat.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Email-i nuk është i vlefshëm.");
            return;
        }

        if (username.length() < 3) {
            showError("Username duhet të ketë të paktën 3 karaktere.");
            return;
        }

        if (password.length() < 8) {
            showError("Fjalëkalimi duhet të ketë të paktën 8 karaktere.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Fjalëkalimet nuk përputhen.");
            return;
        }

        try {
            String hashedPassword = HashService.generateHash(password);

            Perdoruesi user = new Perdoruesi(
                    0,
                    null,
                    emri,
                    mbiemri,
                    email,
                    hashedPassword,
                    "klient",
                    true,
                    0,
                    null,
                    username
            );

            userRepo.create(user);

            hideError();
            clearForm();
            Router.navigateTo(ViewsEnum.LOGIN_VIEW);

        } catch (RuntimeException ex) {
            String message = ex.getMessage();

            if (message == null || message.isBlank()) {
                message = "Ndodhi një gabim gjatë regjistrimit.";
            } else if (message.toLowerCase().contains("duplicate") || message.toLowerCase().contains("unique")) {
                message = "Username ose email ekziston tashmë.";
            }

            showError(message);
        }
    }

    @FXML
    private void handleBackToLogin() {
        Router.navigateTo(ViewsEnum.LOGIN_VIEW);
    }

    private String safeText(TextField field) {
        return field.getText() == null ? "" : field.getText().trim();
    }

    private void showError(String message) {
        errLabel.setText(message);
        errBox.setVisible(true);
        errBox.setManaged(true);
        errSpacer.setVisible(true);
        errSpacer.setManaged(true);
    }

    private void hideError() {
        errBox.setVisible(false);
        errBox.setManaged(false);
        errSpacer.setVisible(false);
        errSpacer.setManaged(false);
    }

    private void clearForm() {
        emriField.clear();
        mbiemriField.clear();
        usernameField.clear();
        emailField.clear();
        passField.clear();
        confirmPassField.clear();
    }
}