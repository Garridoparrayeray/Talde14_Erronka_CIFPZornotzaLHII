package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dao.ArtikuluaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Artikulua;

/**
 * Inbentarioko artikuluen zerrenda eta bilaketa-filtroak kudeatzen dituen kontroladorea.
 * @author Yeray Garrido
 */
public class InbentarioController implements Initializable {

    @FXML private TableView<Artikulua>           taula;
    @FXML private TableColumn<Artikulua, String> colKodea;
    @FXML private TableColumn<Artikulua, String> colIzena;
    @FXML private TableColumn<Artikulua, String> colDeskribapena;
    @FXML private TableColumn<Artikulua, String> colKategoria;
    @FXML private TableColumn<Artikulua, String> colKokalekua;
    @FXML private TableColumn<Artikulua, String> colSarrera;
    @FXML private TableColumn<Artikulua, String> colEgoera;
    @FXML private TextField    txtBilaketa;
    @FXML private ComboBox<String> cbKategoria;
    @FXML private ComboBox<String> cbEgoera;

    private List<Artikulua> guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodea.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue().getArtikuluKodea()));
        colIzena.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue().getIzenburua()));
        colDeskribapena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeskribapenaSegurua()));
        colKategoria.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue().getKategoriaIzena()));
        colKokalekua.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue().getKokalekuaIzena()));
        colSarrera.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue().getSarreraDataFormatua()));
        colEgoera.setCellValueFactory(c       -> new SimpleStringProperty(c.getValue().getEgoeraTestua()));

        cbEgoera.getItems().add("Egoera guztiak");
        cbEgoera.getItems().add("Biltegian");
        cbEgoera.getItems().add("Itzulita");
        cbEgoera.getItems().add("Iraungita");
        cbEgoera.getSelectionModel().selectFirst();

        kargatu();
    }

    /** Kategoria-combo betetzen du inbentarioan dauden kategoriekin. */
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
    }

    private void erakutsiDatuak(List<Artikulua> datuak) {
        taula.getItems().setAll(datuak);
    }

    /** Testua, kategoria eta egoeraren araberako bilaketa-filtroa aplikatzen du. */
    @FXML
    private void bilatu() {
        if (guztiak == null) {
            return;
        }
        String testua = txtBilaketa.getText().trim().toLowerCase();
        String katSel = cbKategoria.getValue();
        String egSel = cbEgoera.getValue();

        if (katSel == null) katSel = "Kategoria guztiak";
        if (egSel == null)  egSel  = "Egoera guztiak";

        List<Artikulua> iragaziak = new ArrayList<>();
        for (Artikulua a : guztiak) {
            boolean testPasa = testua.isEmpty()
                || a.getArtikuluKodea().toLowerCase().contains(testua)
                || a.getIzenburua().toLowerCase().contains(testua)
                || a.getDeskribapenaSegurua().toLowerCase().contains(testua);
            boolean katPasa = katSel.equals("Kategoria guztiak") || a.getKategoriaIzena().equals(katSel);
            boolean egPasa  = egSel.equals("Egoera guztiak")     || a.getEgoeraTestua().equalsIgnoreCase(egSel);
            if (testPasa && katPasa && egPasa) {
                iragaziak.add(a);
            }
        }
        erakutsiDatuak(iragaziak);
    }

    /** Bilaketa-eremuak eta filtroak garbitzen ditu eta zerrenda osoa erakusten du. */
    @FXML
    private void garbitu() {
        txtBilaketa.clear();
        cbKategoria.getSelectionModel().selectFirst();
        cbEgoera.getSelectionModel().selectFirst();
        erakutsiDatuak(guztiak);
    }

    /** Artikulu berri bat gehitzeko elkarrizketa-koadroa irekitzen du. */
    @FXML
    public void artikuluBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ArtikuluaBerria.fxml"));
            Parent root = loader.load();
            ArtikuluaBerriController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initOwner(taula.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Artikulu berria");
            dialog.setScene(new Scene(root, 480, 520));
            dialog.setResizable(false);

            ctrl.setOnGorde(this::kargatu);
            dialog.showAndWait();
        } catch (Exception e) {
            System.err.println("InbentarioController.artikuluBerria: " + e.getMessage());
        }
    }
}
