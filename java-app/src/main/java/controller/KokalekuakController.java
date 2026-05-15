package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.KokalekuaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import model.Kokalekua;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Kokalekuen zerrenda eta kokaleku berria gehitzeko kontroladorea.
 *
 * @author Yeray Garrido eta Eder Martin
 */
public class KokalekuakController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(KokalekuakController.class);

    @FXML
    private TableView<Kokalekua> taula;
    @FXML
    private TableColumn<Kokalekua, String> colId;
    @FXML
    private TableColumn<Kokalekua, String> colArmairua;
    @FXML
    private TableColumn<Kokalekua, String> colApala;
    @FXML
    private TableColumn<Kokalekua, String> colArtikuluak;
    @FXML
    private TableColumn<Kokalekua, String> colMota;

    /**
     * Kontroladorea hasieratzen du: taula-zutabeak konfiguratzen ditu eta
     * kokalekuak kargatzen ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getKokalekuId())));
        colArmairua.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArmairua()));
        colApala.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApala()));
        colArtikuluak.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluKopuruaStr()));
        colMota.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMota()));
        kargatu();
    }

    /**
     * Kokaleku guztiak datu-basetik kargatzen ditu eta taula eguneratzen du.
     */
    private void kargatu() {
        List<Kokalekua> datuak = KokalekuaDAO.getGuztiak();
        taula.getItems().setAll(datuak);
    }

    /**
     * Kokaleku berri bat gehitzeko formularioa contentArea-n kargatzen du.
     */
    @FXML
    public void kokalekuaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KokalekuaBerria.fxml"));
            Node nodoa = loader.load();
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "kokalekuaBerria: errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da formularioa kargatu.", false);
        }
    }

    /**
     * Taulan aukeratutako kokalekua datu-basetik ezabatzen du.
     */
    @FXML
    public void kokalekuaEzabatu() {
        Kokalekua sel = taula.getSelectionModel().getSelectedItem();

        if (sel == null) {
            UIKudeatzailea.erakutsiToast("Aukeratu kokaleku bat taulan ezabatzeko.", false);
            return;
        }

        boolean ondo = KokalekuaDAO.ezabatu(sel.getKokalekuId());
        if (ondo) {
            kargatu();
            UIKudeatzailea.erakutsiToast("Kokalekua ezabatu da.", true);
        } else {
            UIKudeatzailea.erakutsiToast("Ezin izan da kokalekua ezabatu. Agian objektuekin lotuta dago.", false);
        }
    }

    /**
     * Taulan aukeratutako kokalekua editatzeko elkarrizketa-koadroa erakusten
     * du.
     */
    @FXML
    public void kokalekuaEditatu() {
        Kokalekua sel = taula.getSelectionModel().getSelectedItem();

        if (sel == null) {
            UIKudeatzailea.erakutsiToast("Aukeratu kokaleku bat taulan editatzeko.", false);
            return;
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 0));

        TextField txtArmairua = new TextField(sel.getArmairua());
        txtArmairua.setPromptText("Armairua");

        TextField txtApala = new TextField(sel.getApala());
        txtApala.setPromptText("Apala");

        CheckBox chkBha = new CheckBox("BHA (Bolumen Handiko Armairua)");
        chkBha.setSelected(sel.isBhaDa());

        grid.add(new Label("Armairua:"), 0, 0);
        grid.add(txtArmairua, 1, 0);
        grid.add(new Label("Apala:"), 0, 1);
        grid.add(txtApala, 1, 1);
        grid.add(chkBha, 1, 2);

        UIKudeatzailea.erakutsiFormOverlay("Kokalekua editatu", grid, () -> {
            boolean ondo = KokalekuaDAO.eguneratu(
                    sel.getKokalekuId(),
                    txtArmairua.getText().trim(),
                    txtApala.getText().trim(),
                    chkBha.isSelected());
            if (ondo) {
                kargatu();
                UIKudeatzailea.erakutsiToast("Kokalekua ondo eguneratu da.", true);
            } else {
                UIKudeatzailea.erakutsiToast("Ezin izan da kokalekua eguneratu.", false);
            }
        });
    }
}
