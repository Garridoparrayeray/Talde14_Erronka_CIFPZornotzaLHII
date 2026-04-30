package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.KokalekuaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Kokalekuen taula kudeatzen duen kontroladorea.
 */
public class KokalekuakController implements Initializable {

    @FXML private TableView<String[]>           taula;
    @FXML private TableColumn<String[], String> colId;
    @FXML private TableColumn<String[], String> colArmairua;
    @FXML private TableColumn<String[], String> colApala;
    @FXML private TableColumn<String[], String> colArtikuluak;
    @FXML private TableColumn<String[], String> colMota;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(c           -> new SimpleStringProperty(c.getValue()[0]));
        colArmairua.setCellValueFactory(c     -> new SimpleStringProperty(c.getValue()[1]));
        colApala.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue()[2]));
        colArtikuluak.setCellValueFactory(c   -> new SimpleStringProperty(c.getValue()[3]));
        colMota.setCellValueFactory(c         -> new SimpleStringProperty(c.getValue()[4]));
        kargatu();
    }

    private String[][] lortuDatuakDimentsioBitan() {
        List<String[]> lista = KokalekuaDAO.getGuztiak();
        String[][] datuak = new String[lista.size()][5];
        for (int i = 0; i < lista.size(); i++) {
            datuak[i] = lista.get(i);
        }
        return datuak;
    }

    private void kargatu() {
        String[][] datuak = lortuDatuakDimentsioBitan();
        taula.getItems().clear();
        for (String[] fila : datuak) {
            taula.getItems().add(fila);
        }
    }

    @FXML
    public void kokalekuaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KokalekuaBerria.fxml"));
            Parent root = loader.load();
            KokalekuaBerriController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initOwner(taula.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Apal berria");
            dialog.setScene(new Scene(root, 400, 320));
            dialog.setResizable(false);

            ctrl.setOnGorde(this::kargatu);
            dialog.showAndWait();
        } catch (Exception e) {
            System.err.println("KokalekuakController.kokalekuaBerria: " + e.getMessage());
        }
    }
}
