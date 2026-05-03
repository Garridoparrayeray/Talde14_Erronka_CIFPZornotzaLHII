package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.KokalekuaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Kokaleku berri bat gehitzeko elkarrizketa-koadroaren kontroladorea.
 * @author Yeray Garrido
 */
public class KokalekuaBerriController implements Initializable {

    @FXML private TextField txtArmairua;
    @FXML private TextField txtApala;
    @FXML private CheckBox  chkBhaDa;
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

    /** Formularioko datuak egiaztatzen ditu eta kokalekua datu-basean gordetzen du. */
    @FXML
    private void gorde() {
        String armairua = txtArmairua.getText().trim().toUpperCase();
        String apala    = txtApala.getText().trim();
        boolean bhaDa   = chkBhaDa != null && chkBhaDa.isSelected();

        if (armairua.isEmpty() || apala.isEmpty()) {
            erakutsiErrorea("(*) Armairua eta apala bete behar dira.");
            return;
        }

        boolean ok = KokalekuaDAO.gehitu(armairua, apala, bhaDa);
        if (ok) {
            if (onGordeCb != null) onGordeCb.run();
            itxi();
        } else {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
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
