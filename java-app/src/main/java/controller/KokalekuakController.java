package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.KokalekuaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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

    /**
     * Taulan aukeratutako kokalekua datu-basetik ezabatzen du.
     */
    @FXML
    public void kokalekuaEzabatu() {
        Kokalekua sel = taula.getSelectionModel().getSelectedItem();
        
        if (sel == null) {
            erakutsiAlerta(Alert.AlertType.WARNING, "Kontuz", "Aukeratu kokaleku bat taulan ezabatzeko.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Kokalekua ezabatu");
        confirm.setHeaderText("Kokalekua behin betiko ezabatuko da");
        confirm.setContentText("Ziur zaude kokaleku hau ezabatu nahi duzula?");
        confirm.initOwner(taula.getScene().getWindow());

        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                boolean ondo = KokalekuaDAO.ezabatu(sel.getKokalekuId());
                if (ondo) {
                    kargatu();
                    erakutsiAlerta(Alert.AlertType.INFORMATION, "Eginda", "Kokalekua ondo ezabatu da.");
                } else {
                    erakutsiAlerta(Alert.AlertType.ERROR, "Errorea", "Ezin izan da kokalekua ezabatu. Agian objektuekin lotuta dago.");
                }
            }
        });
    }

    /**
     * Taulan aukeratutako kokalekua editatzeko elkarrizketa-koadroa erakusten du.
     */
    @FXML
    public void kokalekuaEditatu() {
        Kokalekua sel = taula.getSelectionModel().getSelectedItem();
        
        if (sel == null) {
            erakutsiAlerta(Alert.AlertType.WARNING, "Kontuz", "Aukeratu kokaleku bat taulan editatzeko.");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Kokalekua editatu");
        dialog.setHeaderText("Aldatu kokalekuaren datuak");
        dialog.initOwner(taula.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 10));

        TextField txtArmairua = new TextField(sel.getArmairua());
        txtArmairua.setPromptText("Armairua");
        
        TextField txtApala = new TextField(sel.getApala());
        txtApala.setPromptText("Apala");
        
        CheckBox chkBha = new CheckBox("BHA (Bolumen Handiko Armairua)");
        chkBha.setSelected(sel.isBhaDa());

        grid.add(new Label("Armairua:"), 0, 0);
        grid.add(txtArmairua, 1, 0);
        grid.add(new Label("Apala:"), 0, 1);
        grid.add(txtApala, 1, 1);
        grid.add(chkBha, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                boolean ondo = KokalekuaDAO.eguneratu(
                    sel.getKokalekuId(), 
                    txtArmairua.getText().trim(), 
                    txtApala.getText().trim(), 
                    chkBha.isSelected()
                );
                if (ondo) {
                    kargatu(); 
                    erakutsiAlerta(Alert.AlertType.INFORMATION, "Eginda", "Kokalekua ondo eguneratu da.");
                } else {
                    erakutsiAlerta(Alert.AlertType.ERROR, "Errorea", "Ezin izan da kokalekua eguneratu.");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void erakutsiAlerta(Alert.AlertType mota, String titulua, String mezua) {
        Alert alert = new Alert(mota);
        alert.setTitle(titulua);
        alert.setHeaderText(null);
        alert.setContentText(mezua);
        alert.showAndWait();
    }
}
