package controller;

import java.net.URL;
import java.text.SimpleDateFormat;
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
 * Inbentarioko taula kudeatzen duen kontroladorea.
 */
public class InbentarioController implements Initializable {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    @FXML private TableView<String[]>           taula;
    @FXML private TableColumn<String[], String> colKodea;
    @FXML private TableColumn<String[], String> colIzena;
    @FXML private TableColumn<String[], String> colDeskribapena;
    @FXML private TableColumn<String[], String> colKategoria;
    @FXML private TableColumn<String[], String> colKokalekua;
    @FXML private TableColumn<String[], String> colSarrera;
    @FXML private TableColumn<String[], String> colEgoera;
    @FXML private TextField  txtBilaketa;
    @FXML private ComboBox<String> cbKategoria;
    @FXML private ComboBox<String> cbEgoera;

    // Artikulu guztiak memorian, iragazketarako
    private String[][] guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colKodea.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue()[0]));
        colIzena.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue()[1]));
        colDeskribapena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[2]));
        colKategoria.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue()[3]));
        colKokalekua.setCellValueFactory(c    -> new SimpleStringProperty(c.getValue()[4]));
        colSarrera.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue()[5]));
        colEgoera.setCellValueFactory(c       -> new SimpleStringProperty(c.getValue()[6]));

        // Egoera ComboBox betetu
        cbEgoera.getItems().add("Egoera guztiak");
        cbEgoera.getItems().add("Biltegian");
        cbEgoera.getItems().add("Itzulita");
        cbEgoera.getItems().add("Iraungita");
        cbEgoera.getSelectionModel().selectFirst();

        kargatu();
        beteteKategoriaCombo();
    }

    private void beteteKategoriaCombo() {
        cbKategoria.getItems().clear();
        cbKategoria.getItems().add("Kategoria guztiak");
        for (String[] f : guztiak) {
            String kat = f[3];
            if (!kat.equals("—") && !cbKategoria.getItems().contains(kat)) {
                cbKategoria.getItems().add(kat);
            }
        }
        cbKategoria.getSelectionModel().selectFirst();
    }

    private String[][] lortuDatuakDimentsioBitan() {
        List<Artikulua> lista = ArtikuluaDAO.getGuztiak();
        String[][] datuak = new String[lista.size()][7];
        for (int i = 0; i < lista.size(); i++) {
            Artikulua a = lista.get(i);
            String data = "—";
            if (a.getSarreraData() != null) {
                data = SDF.format(a.getSarreraData());
            }
            String kat = "—";
            if (a.getKategoria() != null) {
                kat = a.getKategoria().getIzena();
            }
            String kok = "—";
            if (a.getKokalekua() != null) {
                kok = a.getKokalekua().getKokalekuOsoa();
            }
            String egoera = "—";
            if (a.getEgoera() != null) {
                egoera = egoeraTestua(a.getEgoera().name());
            }
            datuak[i][0] = a.getArtikuluKodea();
            datuak[i][1] = a.getIzenburua();
            datuak[i][2] = a.getDeskribapena() != null ? a.getDeskribapena() : "—";
            datuak[i][3] = kat;
            datuak[i][4] = kok;
            datuak[i][5] = data;
            datuak[i][6] = egoera;
        }
        return datuak;
    }

    private void kargatu() {
        guztiak = lortuDatuakDimentsioBitan();
        erakutsiDatuak(guztiak);
        beteteKategoriaCombo();
    }

    private void erakutsiDatuak(String[][] datuak) {
        taula.getItems().clear();
        for (String[] fila : datuak) {
            taula.getItems().add(fila);
        }
    }

    @FXML
    private void bilatu() {
        if (guztiak == null) {
            return;
        }
        String testua = "";
        if (txtBilaketa != null && txtBilaketa.getText() != null) {
            testua = txtBilaketa.getText().trim().toLowerCase();
        }
        String katSel = "Kategoria guztiak";
        if (cbKategoria != null && cbKategoria.getValue() != null) {
            katSel = cbKategoria.getValue();
        }
        String egSel = "Egoera guztiak";
        if (cbEgoera != null && cbEgoera.getValue() != null) {
            egSel = cbEgoera.getValue();
        }

        ArrayList<String[]> iragaziak = new ArrayList<String[]>();
        for (String[] fila : guztiak) {
            boolean testPasa = testua.isEmpty()
                || fila[0].toLowerCase().contains(testua)
                || fila[1].toLowerCase().contains(testua)
                || fila[2].toLowerCase().contains(testua);
            boolean katPasa = katSel.equals("Kategoria guztiak") || fila[3].equals(katSel);
            boolean egPasa  = egSel.equals("Egoera guztiak")     || fila[6].equalsIgnoreCase(egSel);
            if (testPasa && katPasa && egPasa) {
                iragaziak.add(fila);
            }
        }
        erakutsiDatuak(iragaziak.toArray(new String[0][0]));
    }

    @FXML
    private void garbitu() {
        if (txtBilaketa != null) {
            txtBilaketa.clear();
        }
        if (cbKategoria != null) {
            cbKategoria.getSelectionModel().selectFirst();
        }
        if (cbEgoera != null) {
            cbEgoera.getSelectionModel().selectFirst();
        }
        erakutsiDatuak(guztiak);
    }

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

    private String egoeraTestua(String egoera) {
        if (egoera == null) {
            return "—";
        }
        if (egoera.equals("BILTEGIAN")) {
            return "Biltegian";
        } else if (egoera.equals("ITZULITA")) {
            return "Itzulita";
        } else if (egoera.equals("IRAUNGITA")) {
            return "Iraungita";
        } else {
            return egoera;
        }
    }
}
