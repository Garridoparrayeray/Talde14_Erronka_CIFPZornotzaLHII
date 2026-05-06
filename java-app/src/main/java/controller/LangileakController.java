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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import model.Langilea;
import utils.UIKudeatzailea;

/**
 * Langileen zerrenda eta bilaketa kudeatzen duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class LangileakController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private TableView<Langilea> taula;
    @FXML
    private TableColumn<Langilea, String> colLangilea;
    @FXML
    private TableColumn<Langilea, String> colErabiltzailea;
    @FXML
    private TableColumn<Langilea, String> colSaila;
    @FXML
    private TableColumn<Langilea, String> colRola;
    @FXML
    private TableColumn<Langilea, String> colEgoera;
    @FXML
    private TableColumn<Langilea, String> colAzkenSarrera;
    @FXML
    private TextField txtBilaketa;
    @FXML
    private ComboBox<String> cbRola;

    private List<Langilea> guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colLangilea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getIzenOsoa()));
        colErabiltzailea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getErabiltzailea()));
        colSaila.setCellValueFactory(c -> new SimpleStringProperty("—"));
        colRola.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getRola()));
        colEgoera.setCellValueFactory(c -> new SimpleStringProperty("Aktibo"));
        colAzkenSarrera.setCellValueFactory(c -> new SimpleStringProperty("—"));

        cbRola.getItems().add("Rol guztiak");
        List<String[]> rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
        cbRola.getSelectionModel().selectFirst();

        kargatu();
    }

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
            boolean testPasa = testua.isEmpty()
                    || l.getIzenOsoa().toLowerCase().contains(testua)
                    || l.getErabiltzailea().toLowerCase().contains(testua);
            boolean rolPasa = rolSel.equals("Rol guztiak") || l.getRola().equals(rolSel);
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
            LangileBerriController ctrl = loader.getController();
            ctrl.setContentArea(contentArea);
            UIKudeatzailea.kargatuPanela(contentArea, nodoa);
        } catch (Exception e) {
            System.err.println("LangileakController.langileaBerria: " + e.getMessage());
        }
    }
    
    /**
     * Taulan aukeratutako langilea datu-basetik ezabatzen du.
     * Ezabatu aurretik, erabiltzaileari baieztapena eskatzen dio erroreak ekiditeko.
     */
    @FXML
    public void langileaEzabatu() {
        Langilea sel = taula.getSelectionModel().getSelectedItem();
        
        if (sel == null) {
            erakutsiAlerta(Alert.AlertType.WARNING, "Kontuz", "Aukeratu langile bat taulan ezabatzeko.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Langilea ezabatu");
        confirm.setHeaderText("Langilea behin betiko ezabatuko da");
        confirm.setContentText("Ziur zaude '" + sel.getErabiltzailea() + "' erabiltzailea ezabatu nahi duzula?");
        confirm.initOwner(taula.getScene().getWindow());

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                boolean ondo = LangileaDAO.ezabatu(sel.getLangileId());
                if (ondo) {
                    kargatu(); // Taula datu berriekin freskatu
                    erakutsiAlerta(Alert.AlertType.INFORMATION, "Eginda", "Langilea ondo ezabatu da.");
                } else {
                    erakutsiAlerta(Alert.AlertType.ERROR, "Errorea", "Ezin izan da langilea ezabatu. Agian beste datu batzuekin lotuta dago.");
                }
            }
        });
    }

    /**
     * Taulan aukeratutako langilea editatzeko leihoa irekitzen du.
     */
    @FXML
    public void langileaEditatu() {
        Langilea sel = taula.getSelectionModel().getSelectedItem();
        
        if (sel == null) {
            erakutsiAlerta(Alert.AlertType.WARNING, "Kontuz", "Aukeratu langile bat taulan editatzeko.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LangileaEditu.fxml"));
            Parent root = loader.load();
            
            LangileaEdituController ctrl = loader.getController();
            ctrl.setLangilea(sel);
            ctrl.setOnUpdateCallback(() -> kargatu()); // Leihoa istean taula freskatzeko
            
            Stage stage = new Stage();
            stage.initOwner(taula.getScene().getWindow());
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Langilea Editatu");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (Exception e) {
            erakutsiAlerta(Alert.AlertType.ERROR, "Errorea", "Ezin izan da editatzeko leihoa kargatu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Erabiltzaileari informazio, abisu edo errore mezuak erakusteko metodo laguntzailea.
     * 
     * @param mota Alertaren mota (INFO, WARNING, ERROR...)
     * @param titulua Alertaren izenburua
     * @param mezua Erakutsi beharreko testua
     */
    private void erakutsiAlerta(Alert.AlertType mota, String titulua, String mezua) {
        Alert alert = new Alert(mota);
        alert.setTitle(titulua);
        alert.setHeaderText(null);
        alert.setContentText(mezua);
        alert.showAndWait();
    }
}
