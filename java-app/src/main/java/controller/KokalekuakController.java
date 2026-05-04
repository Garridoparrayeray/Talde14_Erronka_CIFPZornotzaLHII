package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.KokalekuaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import model.Kokalekua;
import utils.UIKudeatzailea;

/**
 * Kokalekuen zerrenda eta kokaleku berria gehitzeko kontroladorea.
 *
 * @author Yeray Garrido
 */
public class KokalekuakController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private TableView<Kokalekua> taula;
    @FXML
    private TableColumn<Kokalekua, String> colId;
    @FXML
    private TableColumn<Kokalekua, String> colArmairua;
    @FXML
    private TableColumn<Kokalekua, String> colApala;
    @FXML
    private TableColumn<Kokalekua, String> colArtikuluak;
    @FXML
    private TableColumn<Kokalekua, String> colMota;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getKokalekuId())));
        colArmairua.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArmairua()));
        colApala.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApala()));
        colArtikuluak.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluKopuruaStr()));
        colMota.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMota()));
        kargatu();
    }

    private void kargatu() {
        List<Kokalekua> datuak = KokalekuaDAO.getGuztiak();
        taula.getItems().setAll(datuak);
    }

    /**
     * Kokaleku berri bat gehitzeko formularioa contentArea-n kargatzen du.
     */
    @FXML
    public void kokalekuaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KokalekuaBerria.fxml"));
            Node nodoa = loader.load();
            KokalekuaBerriController ctrl = loader.getController();
            ctrl.setContentArea(contentArea);
            UIKudeatzailea.kargatuPanela(contentArea, nodoa);
        } catch (Exception e) {
            System.err.println("KokalekuakController.kokalekuaBerria: " + e.getMessage());
        }
    }
}
