package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import dao.AurkitzaileaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Artikulua;
import model.Aurkitzailea;
import model.EgoeraArtikulua;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Iraungitako artikuluak kudeatzen dituen kontroladorea. Bi urteko epea igaro
 * ondoren, aurkitzaileari edo erakundeari eskaintzeko Emanaldia formularioa
 * irekitzen du panel berean.
 *
 * @author Yeray Garrido
 */
public class IraungitakoakController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(IraungitakoakController.class);

    @FXML
    private TableView<Lerroa> taula;
    @FXML
    private TableColumn<Lerroa, String> colKodea;
    @FXML
    private TableColumn<Lerroa, String> colIzena;
    @FXML
    private TableColumn<Lerroa, String> colIraungita;
    @FXML
    private TableColumn<Lerroa, String> colAurkIzena;
    @FXML
    private TableColumn<Lerroa, String> colAurkTel;
    @FXML
    private TableColumn<Lerroa, String> colAurkEmail;
    @FXML
    private TableColumn<Lerroa, String> colEskainita;
    @FXML
    private Button btnEskaini;
    @FXML
    private Button btnErakundea;

    /**
     * Taula-lerro bat irudikatzen duen barneko klasea, artikulua eta aurkitzailea
     * bikotea gordetzen dituena.
     */
    public static class Lerroa {

        final Artikulua artikulua;
        final Aurkitzailea aurkitzailea;

        /**
         * Lerroa sortzen du artikulua eta aurkitzailearekin.
         *
         * @param a  Iraungitako artikulua
         * @param au Artikuluaren aurkitzailea (null bada, eremu hutsak erakusten dira)
         */
        Lerroa(Artikulua a, Aurkitzailea au) {
            this.artikulua = a;
            this.aurkitzailea = au;
        }

        /**
         * Aurkitzailearen izen osoa itzultzen du, edo "—" bada null.
         *
         * @return Aurkitzailearen izena
         */
        String getAurkIzena() { // Ternary is fine here for conciseness
            if (aurkitzailea != null) {
                return aurkitzailea.getIzenOsoa();
            } else {
                return "—";
            }
        }

        /**
         * Aurkitzailearen telefonoa itzultzen du, edo "—" bada null.
         *
         * @return Aurkitzailearen telefonoa
         */
        String getAurkTel() { // Ternary is fine here for conciseness
            if (aurkitzailea != null && aurkitzailea.getTelefonoa() != null) {
                return aurkitzailea.getTelefonoa();
            } else {
                return "—";
            }
        }

        /**
         * Aurkitzailearen helbide elektronikoa itzultzen du, edo "—" bada null.
         *
         * @return Aurkitzailearen emaila
         */
        String getAurkEmail() { // Ternary is fine here for conciseness
            if (aurkitzailea != null && aurkitzailea.getEmaila() != null) {
                return aurkitzailea.getEmaila();
            } else {
                return "—";
            }
        }
    }

    /**
     * Kontroladorea hasieratzen du: zutabeak konfiguratzen ditu, botoi-egoera
     * sinkronizatzen du eta iraungitako artikuluak kargatzen ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().artikulua.getArtikuluKodea()));
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().artikulua.getIzenburua()));
        colIraungita
                .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().artikulua.getSarreraDataFormatua()));
        colAurkIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAurkIzena()));
        colAurkTel.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAurkTel()));
        colAurkEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAurkEmail()));
        colEskainita.setCellValueFactory(c -> new SimpleStringProperty(""));
        UIKudeatzailea.objetuarenWrapper(colIzena);
        UIKudeatzailea.objetuarenWrapper(colAurkIzena);
        UIKudeatzailea.objetuarenWrapper(colAurkEmail);

        desaktibatuBotoiak();
        taula.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean dago = sel != null;
            btnEskaini.setDisable(!dago);
            btnErakundea.setDisable(!dago);
        });

        kargatu();
    }

    /**
     * Iraungitako artikuluak datu-basetik kargatzen ditu, aurkitzaileekin
     * lotuta, eta taula betetzen du.
     */
    private void kargatu() {
        ArtikuluaDAO.iraungituakEguneratu();
        List<Lerroa> lerroak = new ArrayList<>();
        for (Artikulua a : ArtikuluaDAO.getGuztiak()) {
            if (a.getEgoera() == EgoeraArtikulua.IRAUNGITA) {
                Aurkitzailea au = AurkitzaileaDAO.getByArtikulua(a.getArtikuluKodea());
                lerroak.add(new Lerroa(a, au));
            }
        }
        taula.getItems().setAll(lerroak);
        desaktibatuBotoiak();
    }

    /**
     * Emanaldia formularioa irekitzen du aurkitzailearen datuak beteta
     * (Pertsona modua).
     */
    @FXML
    private void eskaintzaEgin() {
        Lerroa sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Emanaldia.fxml"));
            Parent root = loader.load();
            EmanaldiaController ctrl = loader.getController();

            String aurkIzena;
            if (sel.aurkitzailea != null) {
                aurkIzena = sel.aurkitzailea.getIzena();
            } else {
                aurkIzena = "";
            }
            String aurkAbizena;
            if (sel.aurkitzailea != null) {
                aurkAbizena = sel.aurkitzailea.getAbizena();
            } else {
                aurkAbizena = "";
            }
            String aurkTel;
            if (sel.aurkitzailea != null) {
                aurkTel = sel.aurkitzailea.getTelefonoa();
            } else {
                aurkTel = "";
            }
            String aurkEmail;
            if (sel.aurkitzailea != null) {
                aurkEmail = sel.aurkitzailea.getEmaila();
            } else {
                aurkEmail = "";
            }

            ctrl.setIraungitakoa(sel.artikulua, false, aurkIzena, aurkAbizena, aurkTel, aurkEmail);
            UIKudeatzailea.kargatuPanela(root);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "eskaintzaEgin: FXML errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da emanaldia formularioa kargatu.", false);
        }
    }

    /**
     * Emanaldia formularioa irekitzen du erakundea moduan (Erakundea modua).
     */
    @FXML
    private void erakundeaEskaini() {
        Lerroa sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Emanaldia.fxml"));
            Parent root = loader.load();
            EmanaldiaController ctrl = loader.getController();

            ctrl.setIraungitakoa(sel.artikulua, true, null, null, null, null);
            UIKudeatzailea.kargatuPanela(root);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "erakundeaEskaini: FXML errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da emanaldia formularioa kargatu.", false);
        }
    }

    /**
     * Eskaintza botoiak desaktibatzen ditu artikulurik hautatu ez denean.
     */
    private void desaktibatuBotoiak() {
        btnEskaini.setDisable(true);
        btnErakundea.setDisable(true);
    }
}
