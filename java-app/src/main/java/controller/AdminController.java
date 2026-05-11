package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
    private Button btnIraungitakoak;

    @FXML
    private Label lblLangileIzena;
    @FXML
    private Label lblLangileRola;
    @FXML
    private Label lblInitialak;

    private List<Button> navBotoiak;

    /**
     * Kontroladorea hasieratzen du: nabigazio-botoiak ezartzen ditu,
     * saio-datuak erakusten ditu eta administrazio-panela kargatzen du.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UIKudeatzailea.setEdukiGunea(adminContentArea);

        navBotoiak = new ArrayList<>();
        navBotoiak.add(btnAdminPanela);
        navBotoiak.add(btnLangileak);
        navBotoiak.add(btnKategoriak);
        navBotoiak.add(btnKokalekuak);
        navBotoiak.add(btnAuditoria);
        navBotoiak.add(btnIraungitakoak);

        Langilea l = Sesio.getLangilea();
        if (l != null) {
            lblLangileIzena.setText(l.getIzena() + " " + l.getAbizena());
            lblLangileRola.setText(l.getRola());

            StringBuilder ini = new StringBuilder();
            if (l.getIzena() != null && !l.getIzena().isEmpty()) {
                ini.append(l.getIzena().charAt(0));
            }
            if (l.getAbizena() != null && !l.getAbizena().isEmpty()) {
                ini.append(l.getAbizena().charAt(0));
            }
            lblInitialak.setText(ini.toString().toUpperCase());
        }

        setAktibo(btnAdminPanela);
        kargatuAdminPanela();
    }

    /**
     * Administrazio-panelaren FXML nodoa kargatzen du eta AdminController
     * erreferentzia kontroladoreari pasatzen dio.
     */
    private void kargatuAdminPanela() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminPanela.fxml"));
            Node nodoa = loader.load();
            AdminPanelaController ctrl = loader.getController();
            ctrl.setAdminController(this);
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "kargatuAdminPanela: FXML kargatzean errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da administrazio-panela kargatu.", false);
        }
    }

    /**
     * Admin panelaren bista kargatzen du.
     */
    @FXML
    public void loadAdminPanela() {
        setAktibo(btnAdminPanela);
        kargatuAdminPanela();
    }

    /**
     * Langileen kudeaketa bista kargatzen du.
     */
    @FXML
    public void loadLangileak() {
        setAktibo(btnLangileak);
        UIKudeatzailea.kargatuPanela("/view/Langileak.fxml");
    }

    /**
     * Kategorien kudeaketa bista kargatzen du.
     */
    @FXML
    public void loadKategoriak() {
        setAktibo(btnKategoriak);
        UIKudeatzailea.kargatuPanela("/view/Kategoriak.fxml");
    }

    /**
     * Kokalekuen kudeaketa bista kargatzen du.
     */
    @FXML
    public void loadKokalekuak() {
        setAktibo(btnKokalekuak);
        UIKudeatzailea.kargatuPanela("/view/Kokalekuak.fxml");
    }

    /**
     * Auditoriaren bista kargatzen du.
     */
    @FXML
    public void loadAuditoria() {
        setAktibo(btnAuditoria);
        UIKudeatzailea.kargatuPanela("/view/Auditoria.fxml");
    }

    /**
     * Iraungitako artikuluen kudeaketa bista kargatzen du.
     */
    @FXML
    public void loadIraungitakoak() {
        setAktibo(btnIraungitakoak);
        UIKudeatzailea.kargatuPanela("/view/Iraungitakoak.fxml");
    }

    /**
     * Langile berria gehitzeko bista kargatzen du eta nabigazioa eguneratzen
     * du.
     */
    public void loadLangileBerria() {
        setAktibo(btnLangileak);
        UIKudeatzailea.kargatuPanela("/view/LangileBerria.fxml");
    }

    /**
     * Kategoria berria gehitzeko bista kargatzen du eta nabigazioa eguneratzen
     * du.
     */
    public void loadKategoriaBerria() {
        setAktibo(btnKategoriak);
        UIKudeatzailea.kargatuPanela("/view/KategoriaBerria.fxml");
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
