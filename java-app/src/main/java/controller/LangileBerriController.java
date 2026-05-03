package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.LangileaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Langile berri bat erregistratzeko elkarrizketa-koadroaren kontroladorea.
 * @author Yeray Garrido
 */
public class LangileBerriController implements Initializable {

    @FXML private TextField             txtIzena;
    @FXML private TextField             txtAbizena;
    @FXML private TextField             txtErabiltzailea;
    @FXML private PasswordField         pfPasahitza;
    @FXML private PasswordField         pfPasahitzaBerretsi;
    @FXML private ComboBox<String>      cbRola;
    @FXML private Label                 lblErrorea;
    @FXML private Button                btnUtzi;

    private Runnable onGordeCb;
    private List<String[]> rolak;

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
        rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
        if (!cbRola.getItems().isEmpty()) {
            cbRola.getSelectionModel().selectFirst();
        }
    }

    /** Formularioko datuak egiaztatzen ditu eta langile berria datu-basean gordetzen du. */
    @FXML
    private void gorde() {
        String izena           = txtIzena.getText().trim();
        String abizena         = txtAbizena.getText().trim();
        String erabiltzailea   = txtErabiltzailea.getText().trim();
        String pasahitza       = pfPasahitza.getText();
        String pasahitzaBerresti = pfPasahitzaBerretsi.getText();
        int    rolIdx          = cbRola.getSelectionModel().getSelectedIndex();

        if (izena.isEmpty() || abizena.isEmpty() || erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            erakutsiErrorea("(*) Eremu guztiak bete behar dira.");
            return;
        }
        if (!pasahitza.equals(pasahitzaBerresti)) {
            erakutsiErrorea("Pasahitzak ez datoz bat.");
            return;
        }
        if (rolIdx < 0) {
            erakutsiErrorea("Hautatu rol bat.");
            return;
        }

        int idRola = Integer.parseInt(rolak.get(rolIdx)[0]);
        boolean ok = LangileaDAO.gehitu(izena, abizena, erabiltzailea, pasahitza, idRola);
        if (ok) {
            if (onGordeCb != null) onGordeCb.run();
            itxi();
        } else {
            erakutsiErrorea("Errorea gordetzean. Erabiltzaile izena dagoeneko existitu daiteke.");
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
