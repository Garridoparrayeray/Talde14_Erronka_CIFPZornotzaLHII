package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Artikulua;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;
import utils.XMLExportazioa;

/**
 * Inbentarioko artikuluen zerrenda, bilaketa-filtroak, edizio eta
 * ezabaketa kudeatzen dituen kontroladorea.
 */
public class InbentarioController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(InbentarioController.class);

    @FXML private TableView<Artikulua> taula;
    @FXML private TableColumn<Artikulua, String> colKodea;
    @FXML private TableColumn<Artikulua, String> colIzena;
    @FXML private TableColumn<Artikulua, String> colDeskribapena;
    @FXML private TableColumn<Artikulua, String> colKategoria;
    @FXML private TableColumn<Artikulua, String> colKokalekua;
    @FXML private TableColumn<Artikulua, String> colSarrera;
    @FXML private TableColumn<Artikulua, String> colEgoera;
    @FXML private TextField txtBilaketa;
    @FXML private ComboBox<String> cbKategoria;
    @FXML private ComboBox<String> cbEgoera;

    // Botoi taldea — kanpoko goiburuan
    @FXML private Button btnEditatu;
    @FXML private Button btnEzabatu;
    @FXML private Button btnIrudia;

    private List<Artikulua> guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluKodea()));
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzenburua()));
        colDeskribapena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeskribapenaSegurua()));
        UIKudeatzailea.ehundatuZelulak(colDeskribapena);
        colKategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKategoriaIzena()));
        colKokalekua.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKokalekuaIzena()));
        colSarrera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getSarreraDataFormatua()));
        colEgoera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEgoeraTestua()));

        cbEgoera.getItems().addAll("Egoera guztiak", "Biltegian", "Itzulita", "Iraungita");
        cbEgoera.getSelectionModel().selectFirst();

        // Botoiak aukeraketa-menpean
        desaktibatiBotoiak();
        taula.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            boolean dago = sel != null;
            if (btnEditatu != null) btnEditatu.setDisable(!dago);
            if (btnEzabatu != null) btnEzabatu.setDisable(!dago);
            boolean duArgazkia = dago && sel.getArgazkiBidea() != null && !sel.getArgazkiBidea().isEmpty();
            if (btnIrudia != null) btnIrudia.setDisable(!duArgazkia);
        });

        kargatu();
    }

    // ─── Ekintza botoiak ────────────────────────────────────────────────────

    /**
     * Hautatutako artikuluaren argazkia eta deskribapena popup batean erakusten du.
     */
    @FXML
    private void ikusiIrudia() {
        Artikulua sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null || sel.getArgazkiBidea() == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IrudiaPopup.fxml"));
            Parent root = loader.load();
            IrudiaPopupController ctrl = loader.getController();
            ctrl.kargatu(sel);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle(sel.getArtikuluKodea() + " — argazkia");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ikusiIrudia: popup errorea", e);
        }
    }

    /**
     * Hautatutako artikulua editatzeko formularioa irekitzen du.
     */
    @FXML
    private void editatu() {
        Artikulua sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ArtikuluaEditu.fxml"));
            Parent root = loader.load();
            ArtikuluaEdituController ctrl = loader.getController();
            ctrl.kargatu(sel, this::kargatu);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Artikulua editatu — " + sel.getArtikuluKodea());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "editatu: FXML kargatzean errorea", e);
        }
    }

    /**
     * Hautatutako artikulua berrespena eskatuta ezabatzen du.
     */
    @FXML
    private void ezabatu() {
        Artikulua sel = taula.getSelectionModel().getSelectedItem();
        if (sel == null) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Ezabatu");
        confirm.setHeaderText("Artikulua ezabatu: " + sel.getArtikuluKodea());
        confirm.setContentText("Ziur zaude? Eragiketa hau ezin da desegin.");
        Optional<ButtonType> resp = confirm.showAndWait();

        if (resp.isPresent() && resp.get() == ButtonType.OK) {
            boolean ok = ArtikuluaDAO.ezabatu(sel.getArtikuluKodea());
            if (ok) {
                XMLExportazioa.exportatu();
                kargatu();
            } else {
                Alert err = new Alert(Alert.AlertType.ERROR);
                err.setHeaderText("Ezin da ezabatu");
                err.setContentText("Artikuluak emanaldia edo mugimendua dauka. Ezin da ezabatu.");
                err.showAndWait();
            }
        }
    }

    /**
     * Artikulu guztiak XML fitxategira exportatzen ditu partekatutako_datuak/ karpetan.
     */
    @FXML
    private void exportatuXML() {
        XMLExportazioa.exportatu();
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setTitle("XML Exportazioa");
        info.setHeaderText(null);
        info.setContentText("XML fitxategia eguneratu da:\n" + utils.AppConfig.getXmlBidea());
        info.showAndWait();
    }

    // ─── Bilaketa / kargaketa ────────────────────────────────────────────────

    private void beteteKategoriaCombo() {
        cbKategoria.getItems().clear();
        cbKategoria.getItems().add("Kategoria guztiak");
        for (Artikulua a : guztiak) {
            String kat = a.getKategoriaIzena();
            if (!kat.equals("—") && !cbKategoria.getItems().contains(kat)) {
                cbKategoria.getItems().add(kat);
            }
        }
        cbKategoria.getSelectionModel().selectFirst();
    }

    private void kargatu() {
        guztiak = ArtikuluaDAO.getGuztiak();
        erakutsiDatuak(guztiak);
        beteteKategoriaCombo();
        desaktibatiBotoiak();
    }

    private void erakutsiDatuak(List<Artikulua> datuak) {
        taula.getItems().setAll(datuak);
    }

    private void desaktibatiBotoiak() {
        if (btnEditatu != null) btnEditatu.setDisable(true);
        if (btnEzabatu != null) btnEzabatu.setDisable(true);
        if (btnIrudia != null)  btnIrudia.setDisable(true);
    }

    @FXML
    private void bilatu() {
        if (guztiak == null) return;
        String testua = txtBilaketa.getText().trim().toLowerCase();
        String katSel = cbKategoria.getValue();
        String egSel = cbEgoera.getValue();

        List<Artikulua> iragaziak = new ArrayList<>();
        for (Artikulua a : guztiak) {
            boolean testPasa = testua.isEmpty()
                    || a.getArtikuluKodea().toLowerCase().contains(testua)
                    || a.getIzenburua().toLowerCase().contains(testua)
                    || a.getDeskribapenaSegurua().toLowerCase().contains(testua);
            boolean katPasa = katSel == null || katSel.equals("Kategoria guztiak") || a.getKategoriaIzena().equals(katSel);
            boolean egPasa = egSel == null || egSel.equals("Egoera guztiak") || a.getEgoeraTestua().equalsIgnoreCase(egSel);
            if (testPasa && katPasa && egPasa) {
                iragaziak.add(a);
            }
        }
        erakutsiDatuak(iragaziak);
    }

    @FXML
    private void garbitu() {
        txtBilaketa.clear();
        cbKategoria.getSelectionModel().selectFirst();
        cbEgoera.getSelectionModel().selectFirst();
        erakutsiDatuak(guztiak);
    }
}
