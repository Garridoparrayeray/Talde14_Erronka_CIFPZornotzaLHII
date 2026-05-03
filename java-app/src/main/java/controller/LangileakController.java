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
import model.Langilea;

/**
 * Langileen zerrenda eta bilaketa kudeatzen duen kontroladorea.
 * @author Yeray Garrido
 */
public class LangileakController implements Initializable {

    @FXML private TableView<Langilea>           taula;
    @FXML private TableColumn<Langilea, String> colLangilea;
    @FXML private TableColumn<Langilea, String> colErabiltzailea;
    @FXML private TableColumn<Langilea, String> colSaila;
    @FXML private TableColumn<Langilea, String> colRola;
    @FXML private TableColumn<Langilea, String> colEgoera;
    @FXML private TableColumn<Langilea, String> colAzkenSarrera;
    @FXML private TextField    txtBilaketa;
    @FXML private ComboBox<String> cbRola;

    private List<Langilea> guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colLangilea.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue().getIzenOsoa()));
        colErabiltzailea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getErabiltzailea()));
        colSaila.setCellValueFactory(c         -> new SimpleStringProperty("—"));
        colRola.setCellValueFactory(c          -> new SimpleStringProperty(c.getValue().getRola()));
        colEgoera.setCellValueFactory(c        -> new SimpleStringProperty("Aktibo"));
        colAzkenSarrera.setCellValueFactory(c  -> new SimpleStringProperty("—"));

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

    /** Testu eta rol-iragazkiarekin langileen zerrenda iragazten du. */
    @FXML
    private void bilatu() {
        if (guztiak == null) {
            return;
        }
        String testua = txtBilaketa.getText().trim().toLowerCase();
        String rolSel = cbRola.getValue();

        if (rolSel == null) rolSel = "Rol guztiak";

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

    /** Langile berri bat gehitzeko elkarrizketa-koadroa irekitzen du. */
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
