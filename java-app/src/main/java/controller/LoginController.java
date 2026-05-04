package controller;

import dao.LangileaDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Langilea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Login formularioa kudeatzen duen kontroladorea. Erabiltzaile-izena eta
 * pasahitza egiaztatzen ditu eta dagokion bista kargatzen du.
 *
 * @author Yeray Garrido
 */
public class LoginController {

    @FXML
    private TextField txtErabiltzailea;
    @FXML
    private PasswordField txtPasahitza;
    @FXML
    private Label lblErrorea;

    /**
     * Saioa hasteko saiakera egiten du. Arrakasta izanez gero dagokion bista
     * irekitzen du.
     */
    @FXML
    private void sartu() {
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String pasahitza = txtPasahitza.getText();

        if (erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            lblErrorea.setText("Bete eremu guztiak.");
            return;
        }

        Langilea langilea = LangileaDAO.login(erabiltzailea, pasahitza);

        if (langilea == null) {
            lblErrorea.setText("Erabiltzailea edo pasahitza okerra.");
            return;
        }

        boolean adminDa = langilea.isAdmin();
        Sesio.hasiera(langilea, adminDa);

        String fxml;
        if (adminDa) {
            fxml = "/view/AdminLayout.fxml";
        } else {
            fxml = "/view/MainLayout.fxml";
        }
        UIKudeatzailea.aldatuLeihoa(txtErabiltzailea, fxml, true);
    }
}
