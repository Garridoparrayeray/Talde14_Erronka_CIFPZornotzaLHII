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

public class GalduDabenakController implements Initializable {

    @FXML private TableView<String[]>            taulaNagusia;
    @FXML private TableColumn<String[], String>  colZbk;
    @FXML private TableColumn<String[], String>  colData;
    @FXML private TableColumn<String[], String>  colIzena;
    @FXML private TableColumn<String[], String>  colAbizena;
    @FXML private TableColumn<String[], String>  colTelefonoa;
    @FXML private TableColumn<String[], String>  colEmaila;
    @FXML private TableColumn<String[], String>  colKategoria;
    @FXML private TableColumn<String[], String>  colDeskribapena;
    @FXML private TableColumn<String[], String>  colEgoera;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colZbk.setCellValueFactory(        data -> new SimpleStringProperty(String.valueOf(taulaNagusia.getItems().indexOf(data.getValue()) + 1)));
        colData.setCellValueFactory(       data -> new SimpleStringProperty(data.getValue()[1]));
        colIzena.setCellValueFactory(      data -> new SimpleStringProperty(data.getValue()[2]));
        colAbizena.setCellValueFactory(    data -> new SimpleStringProperty(data.getValue()[3]));
        colTelefonoa.setCellValueFactory(  data -> new SimpleStringProperty(data.getValue()[4]));
        colEmaila.setCellValueFactory(     data -> new SimpleStringProperty(data.getValue()[5]));
        colKategoria.setCellValueFactory(  data -> new SimpleStringProperty(data.getValue()[6]));
        colDeskribapena.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[7]));
        colEgoera.setCellValueFactory(     data -> new SimpleStringProperty(data.getValue()[8]));

        kargatu();
    }

    private void kargatu() {
        List<String[]> datuak = ErreklamazioaDAO.getGuztiak();
        ObservableList<String[]> lista = FXCollections.observableArrayList(datuak);
        taulaNagusia.setItems(lista);
    }

    @FXML
    private void freskatu() {
        kargatu();
    }
}
