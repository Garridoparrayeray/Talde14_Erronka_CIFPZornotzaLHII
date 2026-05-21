package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.LangileaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import model.Langilea;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Langileen zerrenda eta bilaketa kudeatzen duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class LangileakController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(LangileakController.class);

    @FXML
    private TableView<Langilea> taula;
    @FXML
    private TableColumn<Langilea, String> colLangilea;
    @FXML
    private TableColumn<Langilea, String> colErabiltzailea;
    @FXML
    private TableColumn<Langilea, String> colRola;
    @FXML
    private TableColumn<Langilea, String> colEgoera;
    @FXML
    private TextField txtBilaketa;
    @FXML
    private ComboBox<String> cbRola;

    private List<Langilea> guztiak;

    /**
     * Kontroladorea hasieratzen du. Zutabeak konfiguratzen ditu, rolak
     * kargatzen ditu eta langileen zerrenda bistaratzen du.
     *
     * @param url Hasierako URLa
     * @param rb Baliabideen sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colLangilea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzenOsoa()));
        colErabiltzailea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getErabiltzailea()));
        colRola.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRola()));
        colEgoera.setCellValueFactory(c -> new SimpleStringProperty("Aktibo"));

        cbRola.getItems().add("Rol guztiak");
        List<String[]> rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
        cbRola.getSelectionModel().selectFirst();

        kargatu();
    }

    /**
     * Langileen zerrenda datu-basetik kargatzen du eta taula eguneratzen du.
     */
    private void kargatu() {
        guztiak = LangileaDAO.getGuztiak();
        taula.getItems().setAll(guztiak);
    }

    /**
     * Testu eta rol-iragazkiarekin langileen zerrenda iragazten du.
     */
    @FXML
    private void bilatu() {
        if (guztiak == null) {
            return;
        }
        String testua = txtBilaketa.getText().trim().toLowerCase();
        String rolSel = cbRola.getValue();

        if (rolSel == null) {
            rolSel = "Rol guztiak";
        }

        List<Langilea> iragaziak = new ArrayList<>();
        for (Langilea l : guztiak) {
            boolean testPasa;
            if (testua.isEmpty()) {
                testPasa = true;
            } else if (l.getIzenOsoa().toLowerCase().contains(testua)) {
                testPasa = true;
            } else {
                testPasa = l.getErabiltzailea().toLowerCase().contains(testua);
            }

            boolean rolPasa;
            if (rolSel.equals("Rol guztiak")) {
                rolPasa = true;
            } else {
                rolPasa = l.getRola().equals(rolSel);
            }

            if (testPasa && rolPasa) {
                iragaziak.add(l);
            }
        }
        taula.getItems().setAll(iragaziak);
    }

    /**
     * Langile berri bat gehitzeko formularioa contentArea-n kargatzen du.
     */
    @FXML
    public void langileaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LangileBerria.fxml"));
            Node nodoa = loader.load();
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "langileaBerria: errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da formularioa kargatu.", false);
        }
    }

    /**
     * Taulan aukeratutako langilea datu-basetik ezabatzen du. Ezabatu aurretik,
     * erabiltzaileari baieztapena eskatzen dio erroreak ekiditeko.
     */
    @FXML
    public void langileaEzabatu() {
        Langilea sel = taula.getSelectionModel().getSelectedItem();

        if (sel == null) {
            UIKudeatzailea.erakutsiToast("Aukeratu langile bat taulan ezabatzeko.", false);
            return;
        }

        boolean ondo = LangileaDAO.ezabatu(sel.getLangileId());
        if (ondo) {
            kargatu();
            UIKudeatzailea.erakutsiToast("Langilea ezabatu da: " + sel.getErabiltzailea(), true);
        } else {
            UIKudeatzailea.erakutsiToast("Ezin izan da langilea ezabatu. Agian beste datu batzuekin lotuta dago.", false);
        }
    }

    /**
     * Taulan aukeratutako langilea editatzeko leihoa irekitzen du.
     */
    @FXML
    public void langileaEditatu() {
        Langilea sel = taula.getSelectionModel().getSelectedItem();

        if (sel == null) {
            UIKudeatzailea.erakutsiToast("Aukeratu langile bat taulan editatzeko.", false);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LangileaEditu.fxml"));
            Node nodoa = loader.load();
            LangileaEdituController ctrl = loader.getController();
            ctrl.setLangilea(sel);
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            UIKudeatzailea.erakutsiToast("Ezin izan da editatzeko leihoa kargatu: " + e.getMessage(), false);
            LOG.log(Level.SEVERE, "langileaEditatu: FXML kargatzean errorea", e);
        }
    }
}
