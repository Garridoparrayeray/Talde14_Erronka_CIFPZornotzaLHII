package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.KategoriaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Kategoria berri bat gehitzeko elkarrizketa-koadroaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class KategoriaBerriController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(KategoriaBerriController.class);

    @FXML
    private TextField txtIzena;
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
     * Formularioko datuak egiaztatzen ditu eta kategoria datu-basean gordetzen
     * du.
     */
    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        if (izena.isEmpty()) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "(*) Kategoriaren izena bete behar da.");
            return;
        }

        boolean ok = KategoriaDAO.gehitu(izena);
        if (ok) {
            itxi();
        } else {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea,
                    "Errorea gordetzean. Baliteke izen hori dagoeneko existitzea.");
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
     * Kategorien zerrendara itzultzen da formularioa itxiz.
     */
    private void itxi() {
        UIKudeatzailea.kargatuPanela("/view/Kategoriak.fxml");
    }
}
