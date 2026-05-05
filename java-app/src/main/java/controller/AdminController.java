package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import model.Langilea;
import utils.LogKudeatzailea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Administrazio-atalaren nabigazio-menua eta edukia kudeatzen duen
 * kontroladorea.
 *
 * @author Yeray Garrido
 */
public class AdminController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(AdminController.class);

    @FXML
    private StackPane adminContentArea;

    @FXML
    private Button btnAdminPanela;
    @FXML
    private Button btnLangileak;
    @FXML
    private Button btnKategoriak;
    @FXML
    private Button btnKokalekuak;
    @FXML
    private Button btnAuditoria;

    @FXML
    private Label lblLangileIzena;
    @FXML
    private Label lblLangileRola;
    @FXML
    private Label lblInitialak;

    private ArrayList<Button> navBotoiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navBotoiak = new ArrayList<>();
        navBotoiak.add(btnAdminPanela);
        navBotoiak.add(btnLangileak);
        navBotoiak.add(btnKategoriak);
        navBotoiak.add(btnKokalekuak);
        navBotoiak.add(btnAuditoria);

        Langilea l = Sesio.getLangilea();
        if (l != null) {
            lblLangileIzena.setText(l.getIzena() + " " + l.getAbizena());
            lblLangileRola.setText(l.getRola());

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

    /**
     * Admin panelaren bista kargatzen du.
     */
    @FXML
    public void loadAdminPanela() {
        setAktibo(btnAdminPanela);
        UIKudeatzailea.kargatuPanela(adminContentArea, "/view/AdminPanela.fxml");
    }

    @FXML
    public void loadLangileak() {
        setAktibo(btnLangileak);
        UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Langileak.fxml");
    }

    @FXML
    public void loadKategoriak() {
        setAktibo(btnKategoriak);
        UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Kategoriak.fxml");
    }

    @FXML
    public void loadKokalekuak() {
        setAktibo(btnKokalekuak);
        UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Kokalekuak.fxml");
    }

    @FXML
    public void loadAuditoria() {
        setAktibo(btnAuditoria);
        UIKudeatzailea.kargatuPanela(adminContentArea, "/view/Auditoria.fxml");
    }

    /**
     * Langile-ikuspegira itzultzen da admin modutik.
     */
    @FXML
    public void ErabiltzaileaItzuli() {
        UIKudeatzailea.aldatuLeihoa(adminContentArea, "/view/MainLayout.fxml", true);
    }

    /**
     * Saioa ixten du eta login pantailara itzultzen da.
     */
    @FXML
    public void itxiSaioa() {
        Sesio.itxi();
        UIKudeatzailea.aldatuLeihoa(adminContentArea, "/view/login.fxml", true);
    }

    /**
     * Nabigazio menuko botoi bat aktibo gisa markatzen du estiloz.
     *
     * @param aktibo Aktibatu beharreko botoia
     */
    private void setAktibo(Button aktibo) {
        for (Button b : navBotoiak) {
            b.getStyleClass().removeAll("nav-item-active", "nav-item");
            b.getStyleClass().add("nav-item");
        }
        aktibo.getStyleClass().removeAll("nav-item");
        aktibo.getStyleClass().add("nav-item-active");
    }
}
