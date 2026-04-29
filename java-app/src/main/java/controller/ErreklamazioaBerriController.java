package controller;

import dao.ErreklamazioaDAO;
import dao.KategoriaDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Kategoria;
import utils.Sesio;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ErreklamazioaBerriController implements Initializable {

    @FXML private TextField txtIzena;
    @FXML private TextField txtAbizena;
    @FXML private TextField txtNan;
    @FXML private TextField txtTelefonoa;
    @FXML private TextField txtEmaila;
    @FXML private ComboBox<Kategoria> cbKategoria;
    @FXML private TextArea txtDeskribapena;
    @FXML private Label lblErrorea;

    private Runnable onGordeCb;

    public void setOnGorde(Runnable cb) {
        this.onGordeCb = cb;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        cbKategoria.setItems(FXCollections.observableArrayList(kategoriak));
    }

    @FXML
    private void gorde() {
        String nan          = txtNan.getText().trim();
        String izena        = txtIzena.getText().trim();
        String abizena      = txtAbizena.getText().trim();
        String telefonoa    = txtTelefonoa.getText().trim();
        String emaila       = txtEmaila.getText().trim();
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
            if (onGordeCb != null) {
                onGordeCb.run();
            }
            itxi();
        } else {
            mostrarErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

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
        Stage stage = (Stage) btnUtzi.getScene().getWindow();
        stage.close();
    }

    @FXML private javafx.scene.control.Button btnUtzi;
}
