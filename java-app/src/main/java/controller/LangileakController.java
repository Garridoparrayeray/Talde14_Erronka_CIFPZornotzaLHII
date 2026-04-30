package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dao.LangileaDAO;
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

/**
 * Langileen taula kudeatzen duen kontroladorea.
 */
public class LangileakController implements Initializable {

    @FXML private TableView<String[]>           taula;
    @FXML private TableColumn<String[], String> colLangilea;
    @FXML private TableColumn<String[], String> colErabiltzailea;
    @FXML private TableColumn<String[], String> colSaila;
    @FXML private TableColumn<String[], String> colRola;
    @FXML private TableColumn<String[], String> colEgoera;
    @FXML private TableColumn<String[], String> colAzkenSarrera;
    @FXML private TextField    txtBilaketa;
    @FXML private ComboBox<String> cbRola;

    private String[][] guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colLangilea.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue()[0]));
        colErabiltzailea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()[1]));
        colSaila.setCellValueFactory(c         -> new SimpleStringProperty(c.getValue()[2]));
        colRola.setCellValueFactory(c          -> new SimpleStringProperty(c.getValue()[3]));
        colEgoera.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue()[4]));
        colAzkenSarrera.setCellValueFactory(c  -> new SimpleStringProperty(c.getValue()[5]));

        cbRola.getItems().add("Rol guztiak");
        List<String[]> rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
        cbRola.getSelectionModel().selectFirst();

        kargatu();
    }

    private String[][] lortuDatuakDimentsioBitan() {
        List<String[]> lista = LangileaDAO.getGuztiak();
        String[][] datuak = new String[lista.size()][6];
        for (int i = 0; i < lista.size(); i++) {
            datuak[i] = lista.get(i);
        }
        return datuak;
    }

    private void kargatu() {
        guztiak = lortuDatuakDimentsioBitan();
        erakutsiDatuak(guztiak);
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
        String rolSel = "Rol guztiak";
        if (cbRola != null && cbRola.getValue() != null) {
            rolSel = cbRola.getValue();
        }

        ArrayList<String[]> iragaziak = new ArrayList<String[]>();
        for (String[] fila : guztiak) {
            boolean testPasa = testua.isEmpty()
                || fila[0].toLowerCase().contains(testua)
                || fila[1].toLowerCase().contains(testua);
            boolean rolPasa = rolSel.equals("Rol guztiak") || fila[3].equals(rolSel);
            if (testPasa && rolPasa) {
                iragaziak.add(fila);
            }
        }
        erakutsiDatuak(iragaziak.toArray(new String[0][0]));
    }

    @FXML
    public void langileaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LangileBerria.fxml"));
            Parent root = loader.load();
            LangileBerriController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initOwner(taula.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Langile berria");
            dialog.setScene(new Scene(root, 440, 480));
            dialog.setResizable(false);

            ctrl.setOnGorde(this::kargatu);
            dialog.showAndWait();
        } catch (Exception e) {
            System.err.println("LangileakController.langileaBerria: " + e.getMessage());
        }
    }
}
