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

    /**
     * Kontroladorea hasieratzen du eta errore-etiketa ezkutatzen du.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UIKudeatzailea.ezkutuFormularioErrorea(lblErrorea);
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta kokalekua datu-basean gordetzen
     * du.
     */
    @FXML
    private void gorde() {
        String armairua = txtArmairua.getText().trim().toUpperCase();
        String apala = txtApala.getText().trim();
        boolean bhaDa = chkBhaDa.isSelected();

        if (armairua.isEmpty() || apala.isEmpty()) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "(*) Armairua eta apala bete behar dira.");
            return;
        }

        boolean ok = KokalekuaDAO.gehitu(armairua, apala, bhaDa);
        if (ok) {
            itxi();
        } else {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    /**
     * Aldaketak gorde gabe zerrendara itzultzen du.
     */
    @FXML
    private void utzi() {
        itxi();
    }

    /**
     * Kokalekuen zerrendara itzultzen da formularioa itxiz.
     */
    private void itxi() {
        UIKudeatzailea.kargatuPanela("/view/Kokalekuak.fxml");
    }
}
