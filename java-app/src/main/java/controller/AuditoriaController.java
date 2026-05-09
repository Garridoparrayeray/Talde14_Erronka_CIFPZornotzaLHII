package controller;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.MugimenduDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import model.MugimenduLerroa;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Auditoria-taulako mugimenduen erregistroa erakusten duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class AuditoriaController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(AuditoriaController.class);

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
        UIKudeatzailea.ehundatuZelulak(colEkintza);
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

    /**
     * Taulako auditoria-datuak CSV fitxategi batera esportatzen ditu.
     */
    @FXML
    private void exportatuCSV() {
        FileChooser fc = new FileChooser();
        fc.setTitle("CSV gisa gorde");
        fc.setInitialFileName("auditoria.csv");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV fitxategiak", "*.csv"));
        File fitxategia = fc.showSaveDialog(taula.getScene().getWindow());
        if (fitxategia == null) {
            return;
        }
        try (PrintWriter pw = new PrintWriter(fitxategia, StandardCharsets.UTF_8)) {
            pw.println("DATA;LANGILEA;EKINTZA;XEHETASUNAK");
            for (MugimenduLerroa m : taula.getItems()) {
                pw.printf("\"%s\";\"%s\";\"%s\";\"%s\"%n",
                        esc(m.getData()),
                        esc(m.getLangilea()),
                        esc(m.getEkintza()),
                        esc(m.getArtikuluId()));
            }
            UIKudeatzailea.erakutsiToast("CSV fitxategia gorde da: " + fitxategia.getName(), true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "exportatuCSV: errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da CSV fitxategia gorde.", false);
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("\"", "\"\"");
    }
}
