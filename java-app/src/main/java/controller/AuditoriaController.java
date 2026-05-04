package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.MugimenduDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.MugimenduLerroa;

/**
 * Auditoria-taulako mugimenduen erregistroa erakusten duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class AuditoriaController implements Initializable {

    @FXML
    private TableView<MugimenduLerroa> taula;
    @FXML
    private TableColumn<MugimenduLerroa, String> colData;
    @FXML
    private TableColumn<MugimenduLerroa, String> colLangilea;
    @FXML
    private TableColumn<MugimenduLerroa, String> colEkintza;
    @FXML
    private TableColumn<MugimenduLerroa, String> colXehetasunak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getData()));
        colLangilea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLangilea()));
        colEkintza.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEkintza()));
        colXehetasunak.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluId()));
        kargatu();
    }

    private void kargatu() {
        List<MugimenduLerroa> datuak = MugimenduDAO.getGuztiak();
        taula.getItems().setAll(datuak);
    }

    /**
     * Taulako datuak datu-basetik berriro kargatzen ditu.
     */
    @FXML
    private void freskatu() {
        kargatu();
    }
}
