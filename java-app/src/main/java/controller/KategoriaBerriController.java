package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.KategoriaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Kategoria berri bat gehitzeko elkarrizketa-koadroaren kontroladorea.
 * @author Yeray Garrido
 */
public class KategoriaBerriController implements Initializable {

    @FXML private TextField txtIzena;
    @FXML private Label     lblErrorea;
    @FXML private Button    btnUtzi;

    private Runnable onGordeCb;

    /**
     * Gordetzean exekutatu beharreko callback ezartzen du.
     * @param cb Gordetzean dei beharreko Runnable
     */
    public void setOnGorde(Runnable cb) {
        this.onGordeCb = cb;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ezkutuErrorea();
    }

    /** Formularioko datuak egiaztatzen ditu eta kategoria datu-basean gordetzen du. */
    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        if (izena.isEmpty()) {
            erakutsiErrorea("(*) Kategoriaren izena bete behar da.");
            return;
        }

        boolean ok = KategoriaDAO.gehitu(izena);
        if (ok) {
            if (onGordeCb != null) onGordeCb.run();
            itxi();
        } else {
            erakutsiErrorea("Errorea gordetzean. Baliteke izen hori dagoeneko existitzea.");
        }
    }

    /** Aldaketak gorde gabe leihoa ixten du. */
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
        Stage stage = (Stage) btnUtzi.getScene().getWindow();
        stage.close();
    }
}
