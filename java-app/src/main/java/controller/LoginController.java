package controller;

import dao.LangileaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Administratzailea;
import model.Langilea;
import utils.Sesio;

public class LoginController {

    @FXML private TextField     txtErabiltzailea;
    @FXML private PasswordField txtPasahitza;
    @FXML private Label         lblErrorea;

    @FXML
    private void sartu() {
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String pasahitza     = txtPasahitza.getText();

        if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            lblErrorea.setText("Bete eremu guztiak.");
            return;
        }

        Langilea langilea = LangileaDAO.login(erabiltzailea, pasahitza);

        if (langilea == null) {
            lblErrorea.setText("Erabiltzailea edo pasahitza okerra.");
            return;
        }

        boolean adminDa = langilea instanceof Administratzailea;
        Sesio.hasiera(langilea, adminDa);

        String fxml;
        if (adminDa) {
            fxml = "/view/AdminLayout.fxml";
        } else {
            fxml = "/view/MainLayout.fxml";
        }
        kargatu(fxml);
    }

    private void kargatu(String fxmlBidea) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlBidea));
            Stage stage = (Stage) txtErabiltzailea.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
        } catch (Exception e) {
            lblErrorea.setText("Pantaila kargatzeko errorea.");
            System.err.println("LoginController kargatu errorea: " + e.getMessage());
        }
    }
}
