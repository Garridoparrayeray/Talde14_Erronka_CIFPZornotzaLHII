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

/**
 * Auditoria (mugimenduen erregistroa) taula kudeatzen duen kontroladorea.
 */
public class AuditoriaController implements Initializable {

    @FXML private TableView<String[]>           taula;
    @FXML private TableColumn<String[], String> colData;
    @FXML private TableColumn<String[], String> colLangilea;
    @FXML private TableColumn<String[], String> colEkintza;
    @FXML private TableColumn<String[], String> colXehetasunak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colData.setCellValueFactory(c          -> new SimpleStringProperty(c.getValue()[0]));
        colLangilea.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue()[1]));
        colEkintza.setCellValueFactory(c       -> new SimpleStringProperty(c.getValue()[2]));
        colXehetasunak.setCellValueFactory(c   -> new SimpleStringProperty(c.getValue()[3]));
        kargatu();
    }

    private String[][] lortuDatuakDimentsioBitan() {
        List<String[]> lista = MugimenduDAO.getGuztiak();
        String[][] datuak = new String[lista.size()][4];
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
    private void freskatu() {
        kargatu();
    }
}
