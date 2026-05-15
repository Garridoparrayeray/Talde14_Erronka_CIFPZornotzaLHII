package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Artikulua;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;
import utils.XMLExportazioa;
import utils.XMLInportazioa;

/**
 * Inbentarioko artikuluen zerrenda, bilaketa-filtroak, edizio eta ezabaketa
 * kudeatzen dituen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class InbentarioController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(InbentarioController.class);

    @FXML
    private TableView<Artikulua> taula;
    @FXML
    private TableColumn<Artikulua, String> colKodea;
    @FXML
    private TableColumn<Artikulua, String> colIzena;
    @FXML
    private TableColumn<Artikulua, String> colDeskribapena;
    @FXML
    private TableColumn<Artikulua, String> colKategoria;
    @FXML
    private TableColumn<Artikulua, String> colKokalekua;
    @FXML
    private TableColumn<Artikulua, String> colSarrera;
    @FXML
    private TableColumn<Artikulua, String> colEgoera;
    @FXML
    private TextField txtBilaketa;
    @FXML
    private ComboBox<String> cbKategoria;
    @FXML
    private ComboBox<String> cbEgoera;

    // Botoi taldea — kanpoko goiburuan
    @FXML
    private Button btnEditatu;
    @FXML
    private Button btnEzabatu;
    @FXML
    private Button btnIrudia;

    private List<Artikulua> guztiak;
    private boolean betetzean = false;

    /**
     * Kontroladorea hasieratzen du: zutabeak konfiguratzen ditu, egoera-filtroa
     * betetzen du, botoi-egoera sinkronizatzen du eta artikuluak kargatzen ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluKodea()));
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzenburua()));
        colDeskribapena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeskribapena()));
        UIKudeatzailea.objetuarenWrapper(colDeskribapena);
        colKategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKategoriaIzena()));
        colKokalekua.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKokalekuaIzena()));
        colSarrera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSarreraDataFormatua()));
        colEgoera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEgoeraTestua()));

        cbEgoera.getItems().addAll("Egoera guztiak", "Biltegian", "Itzulita", "Iraungita");
        cbEgoera.getSelectionModel().selectFirst();

        // Botoiak aukeraketa-menpean
        desaktibatuBotoiak();
        taula.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean dago = sel != null;
            if (btnEditatu != null) {
                btnEditatu.setDisable(!dago);
            }
            if (btnEzabatu != null) {
                btnEzabatu.setDisable(!dago);
            }
            boolean duArgazkia = dago && sel.getArgazkiBidea() != null && !sel.getArgazkiBidea().isEmpty();
            if (btnIrudia != null) {
                btnIrudia.setDisable(!duArgazkia);
            }
        });

        kargatu();
    }

    // ─── Ekintza botoiak ────────────────────────────────────────────────────
    /**
     * Hautatutako artikuluaren argazkia eta deskribapena popup batean erakusten
     * du.
     */
    @FXML
    private void ikusiIrudia() {
        Artikulua sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null || sel.getArgazkiBidea() == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IrudiaPopup.fxml"));
            Parent root = loader.load();
            IrudiaPopupController ctrl = loader.getController();
            ctrl.kargatu(sel);

            Stage stage = new Stage();
            stage.initOwner(taula.getScene().getWindow());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(sel.getArtikuluKodea() + " — argazkia");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ikusiIrudia: popup errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da irudia erakutsi.", false);
        }
    }

    /**
     * Hautatutako artikulua editatzeko formularioa irekitzen du.
     */
    @FXML
    private void editatu() {
        Artikulua sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ArtikuluaEditatu.fxml"));
            Node nodoa = loader.load();
            ArtikuluaEditatuController ctrl = loader.getController();

            ctrl.kargatu(sel);
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "editatu: FXML kargatzean errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da editatzeko formularioa kargatu.", false);
        }
    }

    /**
     * Hautatutako artikulua berrespena eskatuta ezabatzen du.
     */
    @FXML
    private void ezabatu() {
        Artikulua sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) {
            return;
        }

        boolean ok = ArtikuluaDAO.ezabatu(sel.getArtikuluKodea());
        if (ok) {
            XMLExportazioa.exportatu();
            kargatu();
            UIKudeatzailea.erakutsiToast("Artikulua ezabatu da.", true);
        } else {
            UIKudeatzailea.erakutsiToast("Ezin da ezabatu: artikuluak emanaldia edo mugimendua dauka.", false);
        }
    }

    /**
     * Artikulu guztiak XML fitxategira exportatzen ditu partekatutako_datuak/
     * karpetan.
     */
    @FXML
    private void exportatuXML() {
        XMLExportazioa.exportatu();
        UIKudeatzailea.erakutsiToast("XML fitxategia eguneratu da.", true);
    }

    /**
     * XML fitxategi bat hautatzen du eta datu-basea egoerak eguneratuz
     * inportatzen du.
     */
    @FXML
    private void inportatuXML() {
        FileChooser fc = new FileChooser();
        fc.setTitle("XML fitxategia hautatu");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML fitxategiak", "*.xml"));
        java.io.File fitxategia = fc.showOpenDialog(taula.getScene().getWindow());
        if (fitxategia == null) {
            return;
        }

        XMLInportazioa.Emaitza emaitza = XMLInportazioa.inportatu(fitxategia);

        UIKudeatzailea.erakutsiToast("XML: " + emaitza.eguneratuak + " eguneratu, "
                + emaitza.saltaturak + " saltatuta.", emaitza.eguneratuak > 0 || emaitza.saltaturak >= 0);
        kargatu();
    }

    // ─── Bilaketa / kargaketa ────────────────────────────────────────────────
    /**
     * Kategoria ComboBox-a artikuluen kategoria esklusiboekin betetzen du.
     */
    private void beteteKategoriaCombo() {
        betetzean = true;
        try {
            cbKategoria.getItems().clear();
            cbKategoria.getItems().add("Kategoria guztiak");
            for (Artikulua a : guztiak) {
                String kat = a.getKategoriaIzena();
                if (!kat.equals("—") && !cbKategoria.getItems().contains(kat)) {
                    cbKategoria.getItems().add(kat);
                }
            }
            cbKategoria.getSelectionModel().selectFirst();
        } finally {
            betetzean = false;
        }
    }

    /**
     * Artikulu guztiak datu-basetik kargatzen ditu eta taula eguneratzen du.
     */
    private void kargatu() {
        guztiak = ArtikuluaDAO.getGuztiak();
        beteteKategoriaCombo();
        cbEgoera.getSelectionModel().selectFirst();
        erakutsiDatuak(guztiak);
        desaktibatuBotoiak();
    }

    /**
     * Emandako artikulu zerrenda taulan erakusten du.
     *
     * @param datuak Erakutsi beharreko artikuluen zerrenda
     */
    private void erakutsiDatuak(List<Artikulua> datuak) {
        taula.getItems().setAll(datuak);
    }

    /**
     * Editatu, ezabatu eta irudi botoiak desaktibatzen ditu artikulurik
     * hautatu ez denean.
     */
    private void desaktibatuBotoiak() {
        if (btnEditatu != null) {
            btnEditatu.setDisable(true);
        }
        if (btnEzabatu != null) {
            btnEzabatu.setDisable(true);
        }
        if (btnIrudia != null) {
            btnIrudia.setDisable(true);
        }
    }

    /**
     * Testu, kategoria eta egoera-iragazkien arabera artikuluen zerrenda
     * iragazten du eta emaitzak taulan erakusten ditu.
     */
    @FXML
    private void bilatu() {
        if (guztiak == null || betetzean) {
            return;
        }
        String testua = txtBilaketa.getText().trim().toLowerCase();
        String katSel = cbKategoria.getValue();
        String egSel = cbEgoera.getValue();

        List<Artikulua> iragaziak = new ArrayList<>();
        for (Artikulua a : guztiak) {
            boolean testuanAurkitu;
            if (testua.isEmpty()) {
                testuanAurkitu = true;
            } else if (a.getArtikuluKodea().toLowerCase().contains(testua)) {
                testuanAurkitu = true;
            } else if (a.getIzenburua().toLowerCase().contains(testua)) {
                testuanAurkitu = true;
            } else if (a.getDeskribapena().toLowerCase().contains(testua)) {
                testuanAurkitu = true;
            } else {
                testuanAurkitu = false;
            }

            boolean katPasa;
            if (katSel == null || katSel.equals("Kategoria guztiak")) {
                katPasa = true;
            } else if (a.getKategoriaIzena().equals(katSel)) {
                katPasa = true;
            } else {
                katPasa = false;
            }

            boolean egPasa;
            if (egSel == null || egSel.equals("Egoera guztiak")) {
                egPasa = true;
            } else if (a.getEgoeraTestua().equalsIgnoreCase(egSel)) {
                egPasa = true;
            } else {
                egPasa = false;
            }

            if (testuanAurkitu && katPasa && egPasa) {
                iragaziak.add(a);
            }
        }
        erakutsiDatuak(iragaziak);
    }

    /**
     * Bilaketa-iragazkiak garbitzen ditu eta artikulu guztiak berriro erakusten
     * ditu.
     */
    @FXML
    private void garbitu() {
        txtBilaketa.clear();
        cbKategoria.getSelectionModel().selectFirst();
        cbEgoera.getSelectionModel().selectFirst();
        erakutsiDatuak(guztiak);
    }
}
