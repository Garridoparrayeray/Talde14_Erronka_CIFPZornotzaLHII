package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import dao.ArtikuluaDAO;
import dao.KategoriaDAO;
import dao.KokalekuaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Kategoria;
import model.Kokalekua;

/**
 * Artikulu berri bat gehitzeko elkarrizketa-koadroaren kontroladorea.
 * @author Yeray Garrido
 */
public class ArtikuluaBerriController implements Initializable {

    @FXML private TextField           txtIzena;
    @FXML private ComboBox<Kategoria> cbKategoria;
    @FXML private TextArea            txtDeskribapena;
    @FXML private ComboBox<Kokalekua> cbKokalekua;
    @FXML private DatePicker          dpSarreraData;
    @FXML private CheckBox            chkIragankorra;
    @FXML private Label               lblErrorea;
    @FXML private Button              btnUtzi;

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
        ArrayList<Kategoria> kategoriak = new ArrayList<Kategoria>(KategoriaDAO.getGuztiak());
        cbKategoria.getItems().setAll(kategoriak);

        ArrayList<Kokalekua> kokalekuak = new ArrayList<Kokalekua>(KokalekuaDAO.getZerrenda());
        cbKokalekua.getItems().setAll(kokalekuak);

        dpSarreraData.setValue(LocalDate.now());
        ezkutuErrorea();
    }

    /** Formularioko datuak egiaztatzen ditu eta artikulua datu-basean gordetzen du. */
    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        Kategoria kategoria = cbKategoria.getValue();
        String deskribapena = txtDeskribapena.getText().trim();

        if (izena.isEmpty() || kategoria == null || deskribapena.isEmpty()) {
            erakutsiErrorea("(*) eremuak bete behar dira: Izena, Kategoria eta Deskribapena.");
            return;
        }

        java.sql.Date sarreraData;
        if (dpSarreraData.getValue() != null) {
            sarreraData = java.sql.Date.valueOf(dpSarreraData.getValue());
        } else {
            sarreraData = java.sql.Date.valueOf(LocalDate.now());
        }

        int idKat = kategoria.getKategoriaId();
        int idKok = 0;
        Kokalekua kokalekua = cbKokalekua.getValue();
        if (kokalekua != null) {
            idKok = kokalekua.getKokalekuId();
        }

        boolean iragankorra = false;
        if (chkIragankorra != null) {
            iragankorra = chkIragankorra.isSelected();
        }

        boolean ok = ArtikuluaDAO.gehitu(izena, deskribapena, iragankorra, idKat, idKok, sarreraData);
        if (ok) {
            if (onGordeCb != null) {
                onGordeCb.run();
            }
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
