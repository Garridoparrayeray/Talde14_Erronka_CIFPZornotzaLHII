package controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.EstadistikaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.AzkenMugimendua;
import model.KategoriaKopurua;
import utils.LogKudeatzailea;
import utils.Sesio;

/**
 * Hasierako paneleko estatistikak eta azken mugimenduak erakusten dituen
 * kontroladorea.
 *
 * @author Yeray Garrido
 */
public class PanelaController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(PanelaController.class);

    @FXML
    private Label lblEgunon;
    @FXML
    private Label lblData;
    @FXML
    private Label lblBiltegian;
    @FXML
    private Label lblErreklamazioIrekiak;
    @FXML
    private Label lblIraungitzear;
    @FXML
    private Label lblIraungituak;

    @FXML
    private TableView<AzkenMugimendua> tblMugimenduak;
    @FXML
    private TableColumn<AzkenMugimendua, String> colArtikulua;
    @FXML
    private TableColumn<AzkenMugimendua, String> colMugimenduId;
    @FXML
    private TableColumn<AzkenMugimendua, String> colLangilea;
    @FXML
    private TableColumn<AzkenMugimendua, String> colData;

    @FXML
    private TableView<KategoriaKopurua> tblKategoriak;
    @FXML
    private TableColumn<KategoriaKopurua, String> colKategoria;
    @FXML
    private TableColumn<KategoriaKopurua, String> colKopurua;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Sesio.getLangilea() != null) {
            lblEgunon.setText("Egun on, " + Sesio.getLangilea().getIzena());
        }
        lblData.setText("Gaur, " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd MMM yyyy")));

        lblBiltegian.setText(String.valueOf(EstadistikaDAO.biltegianKopurua()));
        lblErreklamazioIrekiak.setText(String.valueOf(EstadistikaDAO.erreklamazioIrekiakKopurua()));
        lblIraungitzear.setText(String.valueOf(EstadistikaDAO.iraungitzearKopurua()));
        lblIraungituak.setText(String.valueOf(EstadistikaDAO.iraungituakKopurua()));

        colArtikulua.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluId()));
        colMugimenduId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeskribapena()));
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getData()));
        colLangilea.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getLangilea()));
        tblMugimenduak.getItems().setAll(EstadistikaDAO.azkenMugimenduak());

        colKategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKategoriaIzena()));
        colKopurua.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKopuruaStr()));
        tblKategoriak.getItems().setAll(EstadistikaDAO.kategoriaKopuruak());
    }
}
