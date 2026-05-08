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
import javafx.scene.layout.StackPane;
import model.Artikulua;
import model.Aurkitzailea;
import model.EgoeraArtikulua;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Iraungitako artikuluak kudeatzen dituen kontroladorea.
 * Bi urteko epea igaro ondoren, aurkitzaileari edo erakundeari eskaintzeko
 * Emanaldia formularioa irekitzen du panel berean.
 */
public class IraungitakoakController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(IraungitakoakController.class);

    @FXML private TableView<Lerroa> taula;
    @FXML private TableColumn<Lerroa, String> colKodea;
    @FXML private TableColumn<Lerroa, String> colIzena;
    @FXML private TableColumn<Lerroa, String> colIraungita;
    @FXML private TableColumn<Lerroa, String> colAurkIzena;
    @FXML private TableColumn<Lerroa, String> colAurkTel;
    @FXML private TableColumn<Lerroa, String> colAurkEmail;
    @FXML private TableColumn<Lerroa, String> colEskainita;
    @FXML private Button btnEskaini;
    @FXML private Button btnErakundea;

    public static class Lerroa {
        final Artikulua artikulua;
        final Aurkitzailea aurkitzailea;

        Lerroa(Artikulua a, Aurkitzailea au) {
            this.artikulua   = a;
            this.aurkitzailea = au;
        }

        String getAurkIzena()  { return aurkitzailea != null ? aurkitzailea.getIzenOsoa() : "—"; }
        String getAurkTel()    { return aurkitzailea != null && aurkitzailea.getTelefonoa() != null ? aurkitzailea.getTelefonoa() : "—"; }
        String getAurkEmail()  { return aurkitzailea != null && aurkitzailea.getEmaila()    != null ? aurkitzailea.getEmaila()    : "—"; }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodea.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue().artikulua.getArtikuluKodea()));
        colIzena.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue().artikulua.getIzenburua()));
        colIraungita.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().artikulua.getSarreraDataFormatua()));
        colAurkIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAurkIzena()));
        colAurkTel.setCellValueFactory(c  -> new SimpleStringProperty(c.getValue().getAurkTel()));
        colAurkEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getAurkEmail()));
        colEskainita.setCellValueFactory(c -> new SimpleStringProperty(""));

        desaktibatiBotoiak();
        taula.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean dago = sel != null;
            btnEskaini.setDisable(!dago);
            btnErakundea.setDisable(!dago);
        });

        kargatu();
    }

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
        desaktibatiBotoiak();
    }

    /**
     * Emanaldia formularioa irekitzen du aurkitzailearen datuak beteta (Pertsona modua).
     */
    @FXML
    private void eskaintzaEgin() {
        Lerroa sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        StackPane contentArea = getAdminContentArea();
        if (contentArea == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Emanaldia.fxml"));
            Parent root = loader.load();
            EmanaldiaController ctrl = loader.getController();

            String aurkIzena   = sel.aurkitzailea != null ? sel.aurkitzailea.getIzena()     : "";
            String aurkAbizena = sel.aurkitzailea != null ? sel.aurkitzailea.getAbizena()    : "";
            String aurkTel     = sel.aurkitzailea != null ? sel.aurkitzailea.getTelefonoa()  : "";
            String aurkEmail   = sel.aurkitzailea != null ? sel.aurkitzailea.getEmaila()     : "";

            ctrl.setIraungitakoa(sel.artikulua, false, aurkIzena, aurkAbizena, aurkTel, aurkEmail, contentArea);
            UIKudeatzailea.kargatuPanela(contentArea, root);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "eskaintzaEgin: FXML errorea", e);
        }
    }

    /**
     * Emanaldia formularioa irekitzen du erakundea moduan (Erakundea modua).
     */
    @FXML
    private void erakundeaEskaini() {
        Lerroa sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        StackPane contentArea = getAdminContentArea();
        if (contentArea == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Emanaldia.fxml"));
            Parent root = loader.load();
            EmanaldiaController ctrl = loader.getController();

            ctrl.setIraungitakoa(sel.artikulua, true, null, null, null, null, contentArea);
            UIKudeatzailea.kargatuPanela(contentArea, root);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "erakundeaEskaini: FXML errorea", e);
        }
    }

    private StackPane getAdminContentArea() {
        return (StackPane) taula.getScene().getRoot().lookup("#adminContentArea");
    }

    private void desaktibatiBotoiak() {
        btnEskaini.setDisable(true);
        btnErakundea.setDisable(true);
    }
}
