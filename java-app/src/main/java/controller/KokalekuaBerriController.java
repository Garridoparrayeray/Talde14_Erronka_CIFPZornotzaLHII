package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.KokalekuaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Kokaleku berri bat gehitzeko elkarrizketa-koadroaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class KokalekuaBerriController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(KokalekuaBerriController.class);

    @FXML
    private TextField txtArmairua;
    @FXML
    private TextField txtApala;
    @FXML
    private CheckBox chkBhaDa;
    @FXML
    private Label lblErrorea;

    private StackPane contentArea;

    /**
     * Itzultzean erabili beharreko StackPane ezartzen du.
     *
     * @param contentArea Formularioa kargatuta dagoen gunea
     */
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ezkutuErrorea();
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta kokalekua datu-basean gordetzen
     * du.
     */
    @FXML
    private void gorde() {
        String armairua = txtArmairua.getText().trim().toUpperCase();
        String apala = txtApala.getText().trim();
        boolean bhaDa = chkBhaDa != null && chkBhaDa.isSelected();

        if (armairua.isEmpty() || apala.isEmpty()) {
            erakutsiErrorea("(*) Armairua eta apala bete behar dira.");
            return;
        }

        boolean ok = KokalekuaDAO.gehitu(armairua, apala, bhaDa);
        if (ok) {
            itxi();
        } else {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    /**
     * Aldaketak gorde gabe zerrendara itzultzen du.
     */
    @FXML
    private void utzi() {
        itxi();
    }

    private void erakutsiErrorea(String mezua) {
        lblErrorea.setText(mezua);
        lblErrorea.setVisible(true);
        lblErrorea.setManaged(true);
    }

    private void ezkutuErrorea() {
        lblErrorea.setVisible(false);
        lblErrorea.setManaged(false);
    }

    private void itxi() {
        UIKudeatzailea.kargatuPanela(contentArea, "/view/Kokalekuak.fxml");
    }
}
