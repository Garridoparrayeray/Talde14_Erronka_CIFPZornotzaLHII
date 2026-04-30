package controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import dao.ErreklamazioaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Erreklamazioa;

public class GalduDabenakController implements Initializable {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    @FXML private TableView<String[]>           taula;
    @FXML private TableColumn<String[], String> colZbk;
    @FXML private TableColumn<String[], String> colData;
    @FXML private TableColumn<String[], String> colIzena;
    @FXML private TableColumn<String[], String> colAbizena;
    @FXML private TableColumn<String[], String> colTelefonoa;
    @FXML private TableColumn<String[], String> colEmaila;
    @FXML private TableColumn<String[], String> colKategoria;
    @FXML private TableColumn<String[], String> colDeskribapena;
    @FXML private TableColumn<String[], String> colEgoera;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colZbk.setCellValueFactory(c         -> new SimpleStringProperty(c.getValue()[0]));
        colData.setCellValueFactory(c        -> new SimpleStringProperty(c.getValue()[1]));
        colIzena.setCellValueFactory(c       -> new SimpleStringProperty(c.getValue()[2]));
        colAbizena.setCellValueFactory(c     -> new SimpleStringProperty(c.getValue()[3]));
        colTelefonoa.setCellValueFactory(c   -> new SimpleStringProperty(c.getValue()[4]));
        colEmaila.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue()[5]));
        colKategoria.setCellValueFactory(c   -> new SimpleStringProperty(c.getValue()[6]));
        colDeskribapena.setCellValueFactory(c-> new SimpleStringProperty(c.getValue()[7]));
        colEgoera.setCellValueFactory(c      -> new SimpleStringProperty(c.getValue()[8]));

        kargatu();
    }

    private String[][] lortuDatuakDimentsioBitan() {
        List<Erreklamazioa> lista = ErreklamazioaDAO.getGuztiak();
        String[][] datuak = new String[lista.size()][9];

        for (int i = 0; i < lista.size(); i++) {
            Erreklamazioa e = lista.get(i);
            String data = "—";
            if (e.getErreklamazioData() != null) {
                data = SDF.format(e.getErreklamazioData());
            }
            datuak[i][0] = String.valueOf(e.getErreklamazioId());
            datuak[i][1] = data;
            datuak[i][2] = e.getJabeIzena();
            datuak[i][3] = e.getJabeAbizena();
            datuak[i][4] = e.getJabeTelefonoa();
            datuak[i][5] = e.getJabeEmaila();
            datuak[i][6] = e.getKategoriaIzena();
            datuak[i][7] = e.getDeskribapenBilatua() != null ? e.getDeskribapenBilatua() : "—";
            datuak[i][8] = e.getEgoeraTestua();
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
