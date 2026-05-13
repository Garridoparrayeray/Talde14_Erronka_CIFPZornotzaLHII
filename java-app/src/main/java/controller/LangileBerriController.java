package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.LangileaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Langile berri bat erregistratzeko elkarrizketa-koadroaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class LangileBerriController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(LangileBerriController.class);

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtAbizena;
    @FXML
    private TextField txtErabiltzailea;
    @FXML
    private PasswordField pfPasahitza;
    @FXML
    private PasswordField pfPasahitzaBerretsi;
    @FXML
    private ComboBox<String> cbRola;
    @FXML
    private Label lblErrorea;

    private List<String[]> rolak;

    /**
     * Kontroladorea hasieratzen du: errore-etiketa ezkutatzen du eta rol zerrenda
     * ComboBox-ean betetzen du.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UIKudeatzailea.ezkutuFormularioErrorea(lblErrorea);
        rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
        if (!cbRola.getItems().isEmpty()) {
            cbRola.getSelectionModel().selectFirst();
        }
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta langile berria datu-basean
     * gordetzen du.
     */
    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String pasahitza = pfPasahitza.getText();
        String pasahitzaBerresti = pfPasahitzaBerretsi.getText();
        int rolIdx = cbRola.getSelectionModel().getSelectedIndex();

        if (izena.isEmpty() || abizena.isEmpty() || erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "(*) Eremu guztiak bete behar dira.");
            return;
        }
        if (!pasahitza.equals(pasahitzaBerresti)) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Pasahitzak ez datoz bat.");
            return;
        }
        if (rolIdx < 0) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Hautatu rol bat.");
            return;
        }

        int idRola = Integer.parseInt(rolak.get(rolIdx)[0]);
        boolean ok = LangileaDAO.gehitu(izena, abizena, erabiltzailea, pasahitza, idRola);
        if (ok) {
            itxi();
        } else {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Errorea gordetzean. Erabiltzaile izena dagoeneko existitu daiteke.");
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
     * Langileen zerrendara itzultzen da formularioa itxiz.
     */
    private void itxi() {
        UIKudeatzailea.kargatuPanela("/view/Langileak.fxml");
    }
}
