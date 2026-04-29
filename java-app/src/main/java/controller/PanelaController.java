package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import dao.EstadistikaDAO;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.KategoriaKopurua;
import utils.Sesio;

public class PanelaController implements Initializable {

    @FXML private Label lblEgunon;
    @FXML private Label lblData;
    @FXML private Label lblBiltegian;
    @FXML private Label lblErreklamazioIrekiak;
    @FXML private Label lblIraungitzear;
    @FXML private Label lblIraungituak;

    @FXML private TableView<String[]> tblMugimenduak;
    @FXML private TableColumn<String[], String> colArtikulua;
    @FXML private TableColumn<String[], String> colMugimenduId;
    @FXML private TableColumn<String[], String> colLangilea;
    @FXML private TableColumn<String[], String> colData;

    @FXML private TableView<KategoriaKopurua> tblKategoriak;
    @FXML private TableColumn<KategoriaKopurua, String> colKategoria;
    @FXML private TableColumn<KategoriaKopurua, String> colKopurua;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Erabiltzailea
        if (Sesio.getLangilea() != null) {
            lblEgunon.setText("Egun on, " + Sesio.getLangilea().getIzena());
        }
        lblData.setText("Gaur, " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        // panelako informazioa
        lblBiltegian.setText(String.valueOf(EstadistikaDAO.biltegianKopurua()));
        lblErreklamazioIrekiak.setText(String.valueOf(EstadistikaDAO.erreklamazioIrekiakKopurua()));
        lblIraungitzear.setText(String.valueOf(EstadistikaDAO.iraungitzearKopurua()));
        lblIraungituak.setText(String.valueOf(EstadistikaDAO.iraungituakKopurua()));

        // Azken mugimenduak taula
        colArtikulua.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[0]));
        colMugimenduId.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[1]));
        colLangilea.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[3]));
        colData.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue()[2]));

        List<String[]> mugimenduak = EstadistikaDAO.azkenMugimenduak();
        tblMugimenduak.setItems(FXCollections.observableArrayList(mugimenduak));

        // Kategoriak taula
        colKategoria.setCellValueFactory(c -> {
            if (c.getValue().getKategoriaIzena() != null) {
                return new javafx.beans.property.SimpleStringProperty(c.getValue().getKategoriaIzena());
            } else {
                return new javafx.beans.property.SimpleStringProperty("—");
            }
        });
        
        colKopurua.setCellValueFactory(c -> {
            return new javafx.beans.property.SimpleStringProperty(String.valueOf(c.getValue().getKopurua()));
        });

        List<KategoriaKopurua> kategoriak = EstadistikaDAO.kategoriaKopuruak();
        tblKategoriak.setItems(FXCollections.observableArrayList(kategoriak));
    }
}
