package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Langilea;
import utils.LogKudeatzailea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Aplikazioaren lehio nagusia kudeatzen duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class MainController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(MainController.class);

    @FXML
    private StackPane contentArea;

    @FXML
    private Button btnPanela;
    @FXML
    private Button btnInbentarioa;
    @FXML
    private Button btnErregistroa;
    @FXML
    private Button btnErreklamazioak;
    @FXML
    private Button btnEmanaldia;
    @FXML
    private Button btnGalduDabenak;
    @FXML
    private Button btnBabesKopia;

    @FXML
    private Label lblLangileIzena;
    @FXML
    private Label lblLangileRola;
    @FXML
    private Label lblInitialak;
    @FXML
    private VBox boxAdminSwitch;

    private List<Button> navBotoiak;

    /**
     * Kontroladorea hasieratzen du: nabigazio-botoiak ezartzen ditu, saio-datuak
     * erakusten ditu, ikusle-rolaren murrizketa aplikatzen du eta panela kargatzen du.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UIKudeatzailea.setEdukiGunea(contentArea);

        navBotoiak = new ArrayList<>();
        navBotoiak.add(btnPanela);
        navBotoiak.add(btnInbentarioa);
        navBotoiak.add(btnErregistroa);
        navBotoiak.add(btnErreklamazioak);
        navBotoiak.add(btnEmanaldia);
        navBotoiak.add(btnGalduDabenak);
        navBotoiak.add(btnBabesKopia);

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

        if (boxAdminSwitch != null) {
            boxAdminSwitch.setVisible(Sesio.isAdmin());
            boxAdminSwitch.setManaged(Sesio.isAdmin());
        }

        // Ikusle rolak baimen mugatuak ditu
        if (l != null && "Ikuslea".equalsIgnoreCase(l.getRola())) {
            btnEmanaldia.setVisible(false);
            btnEmanaldia.setManaged(false);
            btnErreklamazioak.setVisible(false);
            btnErreklamazioak.setManaged(false);
            btnGalduDabenak.setVisible(false);
            btnGalduDabenak.setManaged(false);
            btnBabesKopia.setVisible(false);
            btnBabesKopia.setManaged(false);
        }

        setAktibo(btnPanela);
        UIKudeatzailea.kargatuPanela("/view/Panela.fxml");
    }

    /**
     * Panela bista kargatzen du.
     */
    @FXML
    public void loadPanela() {
        setAktibo(btnPanela);
        UIKudeatzailea.kargatuPanela("/view/Panela.fxml");
    }

    /**
     * Inbentarioa bista kargatzen du.
     */
    @FXML
    public void loadInbentarioa() {
        setAktibo(btnInbentarioa);
        UIKudeatzailea.kargatuPanela("/view/Inbentario.fxml");
    }

    /**
     * Erregistroa bista kargatzen du.
     */
    @FXML
    public void loadErregistroa() {
        setAktibo(btnErregistroa);
        UIKudeatzailea.kargatuPanela("/view/Erregistroa.fxml");
    }

    /**
     * Erreklamazioak bista kargatzen du.
     */
    @FXML
    public void loadErreklamazioak() {
        setAktibo(btnErreklamazioak);
        UIKudeatzailea.kargatuPanela("/view/Erreklamazioak.fxml");
    }

    /**
     * Emanaldia bista kargatzen du.
     */
    @FXML
    public void loadEmanaldia() {
        setAktibo(btnEmanaldia);
        UIKudeatzailea.kargatuPanela("/view/Emanaldia.fxml");
    }

    /**
     * Galdu Dabenak bista kargatzen du.
     */
    @FXML
    public void loadGalduDabenak() {
        setAktibo(btnGalduDabenak);
        UIKudeatzailea.kargatuPanela("/view/GalduDabenak.fxml");
    }

    /**
     * Datu-basearen babes-kopia egiten du.
     */
    @FXML
    public void eginBabesKopia() {
        try {
            dao.BackupDAO.eginBabesKopia();
            UIKudeatzailea.erakutsiToast("Babes-kopia ondo egin da.", true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "eginBabesKopia: errorea", e);
            UIKudeatzailea.erakutsiToast("Ezin izan da babes-kopia egin: " + e.getMessage(), false);
        }
    }

    /**
     * Administratzailearen panelera nabigatzen du baimena badu.
     */
    @FXML
    public void irAAdmin() {
        if (Sesio.isAdmin()) {
            UIKudeatzailea.aldatuLeihoa(contentArea, "/view/AdminLayout.fxml", true);
        }
    }

    /**
     * Saioa ixten du eta login pantailara itzultzen da.
     */
    @FXML
    public void itxiSaioa() {
        Sesio.itxi();
        UIKudeatzailea.aldatuLeihoa(contentArea, "/view/login.fxml", true);
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
