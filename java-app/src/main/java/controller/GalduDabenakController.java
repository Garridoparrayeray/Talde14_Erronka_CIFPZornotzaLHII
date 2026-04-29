package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.ErreklamazioaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Erreklamazioa;

public class GalduDabenakController implements Initializable {

    @FXML private TableView<Erreklamazioa>            taulaNagusia;
    @FXML private TableColumn<Erreklamazioa, String>  colZbk;
    @FXML private TableColumn<Erreklamazioa, String>  colData;
    @FXML private TableColumn<Erreklamazioa, String>  colIzena;
    @FXML private TableColumn<Erreklamazioa, String>  colAbizena;
    @FXML private TableColumn<Erreklamazioa, String>  colTelefonoa;
    @FXML private TableColumn<Erreklamazioa, String>  colEmaila;
    @FXML private TableColumn<Erreklamazioa, String>  colKategoria;
    @FXML private TableColumn<Erreklamazioa, String>  colDeskribapena;
    @FXML private TableColumn<Erreklamazioa, String>  colEgoera;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colZbk.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(taulaNagusia.getItems().indexOf(data.getValue()) + 1)));
        
        colData.setCellValueFactory(data -> {
            java.util.Date date = data.getValue().getErreklamazioData();
            if (date != null) {
                return new SimpleStringProperty(date.toString());
            } else {
                return new SimpleStringProperty("—");
            }
        });
        
        colIzena.setCellValueFactory(data -> {
            if (data.getValue().getHartzailea() != null) {
                if (data.getValue().getHartzailea() instanceof model.Jabea) {
                    return new SimpleStringProperty(((model.Jabea) data.getValue().getHartzailea()).getIzena());
                } else {
                    return new SimpleStringProperty("Erakundea");
                }
            } else {
                return new SimpleStringProperty("—");
            }
        });
            
        colAbizena.setCellValueFactory(data -> {
            if (data.getValue().getHartzailea() != null) {
                if (data.getValue().getHartzailea() instanceof model.Jabea) {
                    return new SimpleStringProperty(((model.Jabea) data.getValue().getHartzailea()).getAbizena());
                } else {
                    return new SimpleStringProperty("—");
                }
            } else {
                return new SimpleStringProperty("—");
            }
        });
            
        colTelefonoa.setCellValueFactory(data -> {
            if (data.getValue().getHartzailea() != null) {
                if (data.getValue().getHartzailea() instanceof model.Jabea) {
                    return new SimpleStringProperty(((model.Jabea) data.getValue().getHartzailea()).getTelefonoa());
                } else {
                    return new SimpleStringProperty("—");
                }
            } else {
                return new SimpleStringProperty("—");
            }
        });
            
        colEmaila.setCellValueFactory(data -> {
            if (data.getValue().getHartzailea() != null) {
                if (data.getValue().getHartzailea() instanceof model.Jabea) {
                    return new SimpleStringProperty(((model.Jabea) data.getValue().getHartzailea()).getEmaila());
                } else {
                    return new SimpleStringProperty("—");
                }
            } else {
                return new SimpleStringProperty("—");
            }
        });
            
        colKategoria.setCellValueFactory(data -> {
            if (data.getValue().getKategoria() != null) {
                return new SimpleStringProperty(data.getValue().getKategoria().getIzena());
            } else {
                return new SimpleStringProperty("—");
            }
        });
            
        colDeskribapena.setCellValueFactory(data -> {
            if (data.getValue().getDeskribapenBilatua() != null) {
                return new SimpleStringProperty(data.getValue().getDeskribapenBilatua());
            } else {
                return new SimpleStringProperty("—");
            }
        });
            
        colEgoera.setCellValueFactory(data -> {
            if (data.getValue().getEgoera() != null) {
                return new SimpleStringProperty(data.getValue().getEgoera().toString().toLowerCase());
            } else {
                return new SimpleStringProperty("irekita");
            }
        });

        kargatu();
    }

    private void kargatu() {
        List<Erreklamazioa> datuak = ErreklamazioaDAO.getGuztiak();
        ObservableList<Erreklamazioa> lista = FXCollections.observableArrayList(datuak);
        taulaNagusia.setItems(lista);
    }

    @FXML
    private void freskatu() {
        kargatu();
    }
}
