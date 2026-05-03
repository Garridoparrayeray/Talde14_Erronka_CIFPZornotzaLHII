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
import model.Kokalekua;

/**
 * Kokalekuen zerrenda eta kokaleku berria gehitzeko kontroladorea.
 * @author Yeray Garrido
 */
public class KokalekuakController implements Initializable {

    @FXML private TableView<Kokalekua>           taula;
    @FXML private TableColumn<Kokalekua, String> colId;
    @FXML private TableColumn<Kokalekua, String> colArmairua;
    @FXML private TableColumn<Kokalekua, String> colApala;
    @FXML private TableColumn<Kokalekua, String> colArtikuluak;
    @FXML private TableColumn<Kokalekua, String> colMota;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(c         -> new SimpleStringProperty(String.valueOf(c.getValue().getKokalekuId())));
        colArmairua.setCellValueFactory(c   -> new SimpleStringProperty(c.getValue().getArmairua()));
        colApala.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue().getApala()));
        colArtikuluak.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluKopuruaStr()));
        colMota.setCellValueFactory(c       -> new SimpleStringProperty(c.getValue().getMota()));
        kargatu();
    }

    private void kargatu() {
        List<Kokalekua> datuak = KokalekuaDAO.getGuztiak();
        taula.getItems().setAll(datuak);
    }

    /** Kokaleku berri bat gehitzeko elkarrizketa-koadroa irekitzen du. */
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
