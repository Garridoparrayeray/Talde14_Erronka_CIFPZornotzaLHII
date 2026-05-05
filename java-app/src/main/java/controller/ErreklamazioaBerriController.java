package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.ErreklamazioaDAO;
import dao.KategoriaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import model.Kategoria;
import utils.LogKudeatzailea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Erreklamazino berri bat sortzeko formularioaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class ErreklamazioaBerriController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(ErreklamazioaBerriController.class);

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtAbizena;
    @FXML
    private TextField txtNan;
    @FXML
    private TextField txtTelefonoa;
    @FXML
    private TextField txtEmaila;
    @FXML
    private ComboBox<Kategoria> cbKategoria;
    @FXML
    private TextArea txtDeskribapena;
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
        ArrayList<Kategoria> kategoriak = new ArrayList<Kategoria>(KategoriaDAO.getGuztiak());
        cbKategoria.getItems().setAll(kategoriak);
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta erreklamazioa datu-basean
     * gordetzen du.
     */
    @FXML
    private void gorde() {
        String nan = txtNan.getText().trim();
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();
        String telefonoa = txtTelefonoa.getText().trim();
        String emaila = txtEmaila.getText().trim();
        String deskribapena = txtDeskribapena.getText().trim();
        Kategoria kategoria = cbKategoria.getValue();

        if (nan.isEmpty() || izena.isEmpty() || abizena.isEmpty() || deskribapena.isEmpty() || kategoria == null) {
            mostrarErrorea("(*) eremuak bete behar dira.");
            return;
        }

        int idLangile = 0;
        if (Sesio.getLangilea() != null) {
            idLangile = Sesio.getLangilea().getLangileId();
        }

        boolean ok = ErreklamazioaDAO.gorde(nan, izena, abizena, telefonoa, emaila,
                kategoria.getKategoriaId(), deskribapena, idLangile);
        if (ok) {
            itxi();
        } else {
            mostrarErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    /**
     * Aldaketak gorde gabe zerrendara itzultzen du.
     */
    @FXML
    private void utzi() {
        itxi();
    }

    private void mostrarErrorea(String mezua) {
        lblErrorea.setText(mezua);
        lblErrorea.setVisible(true);
        lblErrorea.setManaged(true);
    }

    private void itxi() {
        UIKudeatzailea.kargatuPanela(contentArea, "/view/Erreklamazioak.fxml");
    }
}
