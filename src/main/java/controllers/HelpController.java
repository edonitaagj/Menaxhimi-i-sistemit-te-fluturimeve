package controllers;

import app.Router;
import app.ViewsEnum;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class HelpController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextArea helpMessageArea;
    @FXML private Label statusLabel;

    // Kthehu në HomeView
    @FXML
    private void handleBack() {
        Router.navigateTo(ViewsEnum.HOME_VIEW);
    }

    // Dërgo kërkesë për ndihmë (simulim / integrim real)
    @FXML
    private void handleSendHelpRequest() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String message = helpMessageArea.getText().trim();

        if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
            statusLabel.setText("⚠ Ju lutemi plotësoni të gjitha fushat (Emri, Email, Mesazhi).");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("⚠ Email-i duket i pavlefshëm. Provoni përsëri.");
            return;
        }

        // Këtu mund të dërgohet në server / API / database
        // Për këtë shembull, do të simulohet në console
        System.out.println("===== KËRKESË E RE PËR NDIHMË =====");
        System.out.println("Emri: " + name);
        System.out.println("Email: " + email);
        System.out.println("Mesazhi: " + message);
        System.out.println("===================================");

        // Në një sistem real këtu do të thërrisni një metodë për të dërguar email ose për të ruajtur në DB

        statusLabel.setText("✅ Kërkesa u dërgua me sukses! Do të përgjigjemi brenda 24 orëve.");

        // Pastro fusha pas dërgimit (opsionale)
        nameField.clear();
        emailField.clear();
        helpMessageArea.clear();
    }
}