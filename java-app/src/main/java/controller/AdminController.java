package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.Langilea;
import utils.Sesio;
import utils.UIKudeatzailea;

public class AdminController implements Initializable {

    @FXML private StackPane adminContentArea;

    @FXML private Button btnAdminPanela;
    @FXML private Button btnLangileak;
    @FXML private Button btnKategoriak;
    @FXML private Button btnKokalekuak;
    @FXML private Button btnAuditoria;

    @FXML private Label lblLangileIzena;
    @FXML private Label lblLangileRola;
    @FXML private Label lblInitialak;

    private List<Button> navBotoiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navBotoiak = List.of(btnAdminPanela, btnLangileak, btnKategoriak, btnKokalekuak, btnAuditoria);

        Langilea l = Sesio.getLangilea();
        if (l != null) {
            lblLangileIzena.setText(l.getIzena() + " " + l.getAbizena());
            lblLangileRola.setText("Administratzailea");
            
            String ini = "";
            if (l.getIzena() != null && !l.getIzena().isEmpty()) {
                ini = ini + l.getIzena().charAt(0);
            }
            if (l.getAbizena() != null && !l.getAbizena().isEmpty()) {
                ini = ini + l.getAbizena().charAt(0);
            }
            
            lblInitialak.setText(ini.toUpperCase());
        }

        setAktibo(btnAdminPanela);
        UIKudeatzailea.kargatuPanela(adminContentArea, "/view/AdminPanela.fxml");
    }

    @FXML public void loadAdminPanela() { setAktibo(btnAdminPanela); UIKudeatzailea.kargatuPanela(adminContentArea, "/view/AdminPanela.fxml"); }
    @FXML public void loadLangileak()   { setAktibo(btnLangileak);   UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Langileak.fxml"); }
    @FXML public void loadKategoriak()  { setAktibo(btnKategoriak);  UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Kategoriak.fxml"); }
    @FXML public void loadKokalekuak()  { setAktibo(btnKokalekuak);  UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Kokalekuak.fxml"); }
    @FXML public void loadAuditoria()   { setAktibo(btnAuditoria);   UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Auditoria.fxml"); }

    @FXML
    public void volverAUser() {
        UIKudeatzailea.aldatuLeihoa(adminContentArea, "/view/MainLayout.fxml", true);
    }

    @FXML
    public void itxiSaioa() {
        Sesio.itxi();
        UIKudeatzailea.aldatuLeihoa(adminContentArea, "/view/login.fxml", true);
    }

    // ---- helpers ----

    private void setAktibo(Button aktibo) {
        for (Button b : navBotoiak) {
            b.getStyleClass().removeAll("nav-item-active", "nav-item");
            b.getStyleClass().add("nav-item");
        }
        aktibo.getStyleClass().removeAll("nav-item");
        aktibo.getStyleClass().add("nav-item-active");
    }
}
