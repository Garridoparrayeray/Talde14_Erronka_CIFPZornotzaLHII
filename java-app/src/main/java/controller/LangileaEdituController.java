package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.LangileaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Langilea;
import utils.LogKudeatzailea;

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
    private Runnable onUpdateCallback;
    private List<String[]> rolak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
    }

    /**
     * Editatu beharreko langilea ezartzen du eta formularioko eremuak betetzen ditu.
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
     * Eguneraketa egin ondoren deitu beharreko callback-a ezartzen du.
     *
     * @param callback Langilea gordetzean exekutatuko den funtzioa
     */
    public void setOnUpdateCallback(Runnable callback) {
        this.onUpdateCallback = callback;
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta langilearen aldaketak datu-basean gordetzen ditu.
     *
     * @param event Botoiaren ekintza-gertaera
     */
    @FXML
    public void eguneratu(ActionEvent event) {
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String rolaDeskribapena = cbRola.getValue();
        String pasahitzaBerria = txtPasahitza != null ? txtPasahitza.getText() : "";

        if (izena.isEmpty() || abizena.isEmpty() || erabiltzailea.isEmpty() || rolaDeskribapena == null) {
            erakutsiErrorea("Eremu guztiak bete behar dira.");
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
            if (onUpdateCallback != null) {
                onUpdateCallback.run();
            }
            itxiLeihoa();
        } else {
            erakutsiErrorea("Errorea gertatu da datu-basean eguneratzean.");
        }
    }

    /**
     * Aldaketak gorde gabe leihoa ixten du.
     *
     * @param event Botoiaren ekintza-gertaera
     */
    @FXML
    public void utzi(ActionEvent event) {
        itxiLeihoa();
    }

    /**
     * Errore-mezu bat erakusten du formularioaren azpian.
     *
     * @param mezua Erakutsi beharreko testua
     */
    private void erakutsiErrorea(String mezua) {
        lblErrorea.setText(mezua);
        lblErrorea.setVisible(true);
        lblErrorea.setManaged(true);
    }

    /**
     * Leiho hau ixten du.
     */
    private void itxiLeihoa() {
        Stage stage = (Stage) btnUtzi.getScene().getWindow();
        stage.close();
    }
}
