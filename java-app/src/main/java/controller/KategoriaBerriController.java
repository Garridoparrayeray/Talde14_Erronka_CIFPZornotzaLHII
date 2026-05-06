package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.KategoriaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import utils.UIKudeatzailea;

/**
 * Kategoria berri bat gehitzeko elkarrizketa-koadroaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class KategoriaBerriController implements Initializable {

    @FXML
    private TextField txtIzena;
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
     * Formularioko datuak egiaztatzen ditu eta kategoria datu-basean gordetzen
     * du.
     */
    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        if (izena.isEmpty()) {
            erakutsiErrorea("(*) Kategoriaren izena bete behar da.");
            return;
        }

        boolean ok = KategoriaDAO.gehitu(izena);
        if (ok) {
            itxi();
        } else {
            erakutsiErrorea("Errorea gordetzean. Baliteke izen hori dagoeneko existitzea.");
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
        UIKudeatzailea.kargatuPanela(contentArea, "/view/Kategoriak.fxml");
    }
}
