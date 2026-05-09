package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.LangileaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.Langilea;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Langile bat editatzeko elkarrizketa-leihoaren kontroladorea.
 *
 * @author Eder Martin
 */
public class LangileaEdituController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(LangileaEdituController.class);

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtAbizena;
    @FXML
    private TextField txtErabiltzailea;
    @FXML
    private ComboBox<String> cbRola;
    @FXML
    private PasswordField txtPasahitza;
    @FXML
    private Label lblErrorea;
    @FXML
    private Button btnUtzi;

    private Langilea langilea;
    private List<String[]> rolak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
    }

    /**
     * Editatu beharreko langilea ezartzen du eta formularioko eremuak betetzen
     * ditu.
     *
     * @param l Editatu beharreko langilea
     */
    public void setLangilea(Langilea l) {
        this.langilea = l;
        txtIzena.setText(l.getIzena());
        txtAbizena.setText(l.getAbizena());
        txtErabiltzailea.setText(l.getErabiltzailea());
        cbRola.getSelectionModel().select(l.getRola());
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta langilearen aldaketak
     * datu-basean gordetzen ditu.
     */
    @FXML
    private void eguneratu() {
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String rolaDeskribapena = cbRola.getValue();
        String pasahitzaBerria = txtPasahitza != null ? txtPasahitza.getText() : "";

        if (izena.isEmpty() || abizena.isEmpty() || erabiltzailea.isEmpty() || rolaDeskribapena == null) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Eremu guztiak bete behar dira.");
            return;
        }

        int idRola = -1;
        for (String[] r : rolak) {
            if (r[1].equals(rolaDeskribapena)) {
                idRola = Integer.parseInt(r[0]);
                break;
            }
        }

        boolean ondo = LangileaDAO.eguneratu(langilea.getLangileId(), izena, abizena, erabiltzailea, idRola, pasahitzaBerria);

        if (ondo) {
            itxi();
        } else {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Errorea gertatu da datu-basean eguneratzean.");
        }
    }

    /** Aldaketak gorde gabe leihoa ixten du. */
    @FXML
    private void utzi() {
        itxi();
    }

    /**
     * Langileen zerrendara itzultzen da.
     */
    private void itxi() {
        UIKudeatzailea.kargatuPanela("/view/Langileak.fxml");
    }
}
