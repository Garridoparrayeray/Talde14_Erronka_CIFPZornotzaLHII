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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Kategoria;
import model.Kokalekua;

/**
 * Artikulu berri bat erregistratzeko formularioaren kontroladorea (pantaila osoa).
 * @author Yeray Garrido
 */
public class ErregistroaController implements Initializable {

    @FXML private TextField           txtIzena;
    @FXML private ComboBox<Kategoria> cbKategoria;
    @FXML private TextArea            txtDeskribapena;
    @FXML private TextField           txtAurkipenLekua;
    @FXML private DatePicker          dpSarreraData;
    @FXML private CheckBox            chkIragankorra;
    @FXML private ComboBox<Kokalekua> cbKokalekua;
    @FXML private Label               lblErrorea;

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
    private void erregistratu() {
        String izena = txtIzena.getText().trim();
        Kategoria kategoria = cbKategoria.getValue();
        String deskribapena = txtDeskribapena.getText().trim();
        String aurkipenLekua = txtAurkipenLekua.getText().trim();

        if (izena.isEmpty() || kategoria == null || deskribapena.isEmpty()) {
            erakutsiErrorea("(*) eremuak bete behar dira: Izena, Kategoria eta Deskribapena.");
            return;
        }

        // Aurkipen-lekua deskribapenarentzat
        String deskFinal = deskribapena;
        if (!aurkipenLekua.isEmpty()) {
            deskFinal = "Aurkitua: " + aurkipenLekua + "\n" + deskribapena;
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

        boolean ok = ArtikuluaDAO.gehitu(izena, deskFinal, iragankorra, idKat, idKok, sarreraData);
        if (ok) {
            garbitu();
            erakutsiErrorea("Artikulua ondo erregistratu da.");
        } else {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    /** Aldaketak gorde gabe formularioa garbitzen du. */
    @FXML
    private void utzi() {
        garbitu();
    }

    /** Formularioko eremu guztiak hasierako egoerara itzultzen ditu. */
    private void garbitu() {
        txtIzena.clear();
        cbKategoria.setValue(null);
        txtDeskribapena.clear();
        if (txtAurkipenLekua != null) {
            txtAurkipenLekua.clear();
        }
        dpSarreraData.setValue(LocalDate.now());
        if (chkIragankorra != null) {
            chkIragankorra.setSelected(false);
        }
        cbKokalekua.setValue(null);
        ezkutuErrorea();
    }

    private void erakutsiErrorea(String mezua) {
        if (lblErrorea != null) {
            lblErrorea.setText(mezua);
            lblErrorea.setVisible(true);
            lblErrorea.setManaged(true);
        }
    }

    private void ezkutuErrorea() {
        if (lblErrorea != null) {
            lblErrorea.setVisible(false);
            lblErrorea.setManaged(false);
        }
    }
}
