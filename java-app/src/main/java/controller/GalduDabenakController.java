package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dao.ErreklamazioaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import model.EgoeraErreklamazioa;
import model.Erreklamazioa;

/**
 * Galdu diren gauzen erreklamazioen zerrenda eta iragazketa kudeatzen duen
 * kontroladorea.
 *
 * @author Yeray Garrido
 */
public class GalduDabenakController implements Initializable {

    @FXML
    private TableView<Erreklamazioa> taula;
    @FXML
    private TableColumn<Erreklamazioa, String> colZbk;
    @FXML
    private TableColumn<Erreklamazioa, String> colData;
    @FXML
    private TableColumn<Erreklamazioa, String> colIzena;
    @FXML
    private TableColumn<Erreklamazioa, String> colAbizena;
    @FXML
    private TableColumn<Erreklamazioa, String> colTelefonoa;
    @FXML
    private TableColumn<Erreklamazioa, String> colEmaila;
    @FXML
    private TableColumn<Erreklamazioa, String> colKategoria;
    @FXML
    private TableColumn<Erreklamazioa, String> colDeskribapena;
    @FXML
    private TableColumn<Erreklamazioa, String> colEgoera;

    @FXML
    private CheckBox cbBaztertuak;
    @FXML
    private CheckBox cbEgindak;
    @FXML
    private HBox hboxDateFilter;
    @FXML
    private DatePicker dpHasiera;
    @FXML
    private DatePicker dpAmaiera;

    private List<Erreklamazioa> guztiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colZbk.setCellValueFactory(c -> new SimpleStringProperty(String.valueOf(c.getValue().getErreklamazioId())));
        colData.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDataFormatua()));
        colIzena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getJabeIzena()));
        colAbizena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getJabeAbizena()));
        colTelefonoa.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getJabeTelefonoa()));
        colEmaila.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getJabeEmaila()));
        colKategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getKategoriaIzena()));
        colDeskribapena.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDeskribapena()));
        colEgoera.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEgoeraTestua()));

        hboxDateFilter.setVisible(false);
        hboxDateFilter.setManaged(false);

        kargatu();
    }

    private void kargatu() {
        guztiak = ErreklamazioaDAO.getGuztiak();
        iragaziAktualizatu();
    }

    private void iragaziAktualizatu() {
        boolean baztertuakErakutsi = cbBaztertuak.isSelected();
        List<Erreklamazioa> iragaziak = new ArrayList<>();

        for (Erreklamazioa e : guztiak) {
            EgoeraErreklamazioa egoera = e.getEgoera();

            if (egoera == EgoeraErreklamazioa.EBATZITA && !cbEgindak.isSelected()) {
                continue;
            }

            if (egoera == EgoeraErreklamazioa.BAZTERTUTA) {
                if (!baztertuakErakutsi) {
                    continue;
                }
                if (e.getErreklamazioData() != null) {
                    LocalDate data = new java.sql.Date(e.getErreklamazioData().getTime()).toLocalDate();
                    LocalDate hasiera = dpHasiera.getValue();
                    LocalDate amaiera = dpAmaiera.getValue();
                    if (hasiera != null && data.isBefore(hasiera)) {
                        continue;
                    }
                    if (amaiera != null && data.isAfter(amaiera)) {
                        continue;
                    }
                }
            }

            iragaziak.add(e);
        }

        taula.getItems().setAll(iragaziak);
    }

    /**
     * Taulako datuak datu-basetik berriro kargatzen ditu.
     */
    @FXML
    private void freskatu() {
        kargatu();
    }

    /**
     * Baztertutako erreklamazioak erakusteko checkboxaren aldaketa kudeatu eta
     * data-iragazkia erakutsi/ezkutatu egiten du.
     */
    @FXML
    private void aldatuBaztertuak() {
        boolean checked = cbBaztertuak.isSelected();
        hboxDateFilter.setVisible(checked);
        hboxDateFilter.setManaged(checked);
        if (!checked) {
            dpHasiera.setValue(null);
            dpAmaiera.setValue(null);
        }
        iragaziAktualizatu();
    }

    /**
     * Ebatzitako erreklamazioak erakusteko checkboxaren aldaketa kudeatzen du.
     */
    @FXML
    private void aldatuEgindak() {
        iragaziAktualizatu();
    }

    /**
     * Data-iragazkia aldatzean taula freskatzeko deia egiten du.
     */
    @FXML
    private void iragaziDataBidez() {
        iragaziAktualizatu();
    }

}
