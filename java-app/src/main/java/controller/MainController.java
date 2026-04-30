package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Langilea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Aplikazioaren lehio nagusia kudeatzen duen kontroladorea.
 * @author Yeray Garrido
 */
public class MainController implements Initializable {

    @FXML private StackPane contentArea;

    @FXML private Button btnPanela;
    @FXML private Button btnInbentarioa;
    @FXML private Button btnErregistroa;
    @FXML private Button btnErreklamazioak;
    @FXML private Button btnEmanaldia;
    @FXML private Button btnGalduDabenak;

    @FXML private Label  lblLangileIzena;
    @FXML private Label  lblLangileRola;
    @FXML private Label  lblInitialak;
    @FXML private VBox   boxAdminSwitch;

    private ArrayList<Button> navBotoiak;

    /**
     * Kontroladorea hasieratzen du. Langilearen datuak kargatzen ditu eta hasierako panela ezartzen du.
     * @param url Hasierako URLa
     * @param rb Baliabideen sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navBotoiak = new ArrayList<Button>();
        navBotoiak.add(btnPanela);
        navBotoiak.add(btnInbentarioa);
        navBotoiak.add(btnErregistroa);
        navBotoiak.add(btnErreklamazioak);
        navBotoiak.add(btnEmanaldia);
        navBotoiak.add(btnGalduDabenak);

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

        // Admin switch botoia ezkutu langile arruntentzat
        if (boxAdminSwitch != null) {
            boxAdminSwitch.setVisible(Sesio.isAdmin());
            boxAdminSwitch.setManaged(Sesio.isAdmin());
        }

        setAktibo(btnPanela);
        UIKudeatzailea.kargatuPanela(contentArea, "/view/Panela.fxml");
    }

    /**
     * Panela bista kargatzen du.
     */
    @FXML public void loadPanela()         { setAktibo(btnPanela);         UIKudeatzailea.kargatuPanela(contentArea, "/view/Panela.fxml"); }
    /**
     * Inbentarioa bista kargatzen du.
     */
    @FXML public void loadInbentarioa()    { setAktibo(btnInbentarioa);    UIKudeatzailea.kargatuPanela(contentArea, "/view/Inbentario.fxml"); }
    /**
     * Erregistroa bista kargatzen du.
     */
    @FXML public void loadErregistroa()    { setAktibo(btnErregistroa);    UIKudeatzailea.kargatuPanela(contentArea, "/view/Erregistroa.fxml"); }
    /**
     * Erreklamazioak bista kargatzen du.
     */
    @FXML public void loadErreklamazioak() { setAktibo(btnErreklamazioak); UIKudeatzailea.kargatuPanela(contentArea, "/view/Erreklamazioak.fxml"); }
    /**
     * Emanaldia bista kargatzen du.
     */
    @FXML public void loadEmanaldia()      { setAktibo(btnEmanaldia);      UIKudeatzailea.kargatuPanela(contentArea, "/view/Emanaldia.fxml"); }
    /**
     * Galdu Dabenak bista kargatzen du.
     */
    @FXML public void loadGalduDabenak()   { setAktibo(btnGalduDabenak);   UIKudeatzailea.kargatuPanela(contentArea, "/view/GalduDabenak.fxml"); }

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

    // ── Laguntzaileak ────────────────────────────────────────────────────────

    /**
     * Nabigazio menuko botoi bat aktibo gisa markatzen du estiloz.
     * @param aktibo Aktibatu beharreko botoia
     */
    private void setAktibo(Button aktibo) {
        for (Button b : navBotoiak) {  // botoi guztiak berrezarri
            b.getStyleClass().removeAll("nav-item-active", "nav-item");
            b.getStyleClass().add("nav-item");
        }
        aktibo.getStyleClass().removeAll("nav-item");
        aktibo.getStyleClass().add("nav-item-active");
    }
}
