package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.LangileaDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Langilea;

public class LangileaEdituController implements Initializable {

    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtAbizena;
    @FXML
    private TextField txtErabiltzailea;
    @FXML
    private ComboBox<String> cbRola;
    @FXML
    private Label lblErrorea;
    @FXML
    private Button btnUtzi;

    private Langilea langilea;
    private Runnable onUpdateCallback;
    private List<String[]> rolak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolak = LangileaDAO.getRolak();
        for (String[] r : rolak) {
            cbRola.getItems().add(r[1]);
        }
    }

    // Metodo honek taulatik aukeratutako langilea jasotzen du eta formularioko datuak betetzen ditu
    public void setLangilea(Langilea l) {
        this.langilea = l;
        txtIzena.setText(l.getIzena());
        txtAbizena.setText(l.getAbizena());
        txtErabiltzailea.setText(l.getErabiltzailea());
        cbRola.getSelectionModel().select(l.getRola());
    }

    // Datu-basean eguneratu ondoren taula nagusia freskatu ahal izateko
    public void setOnUpdateCallback(Runnable callback) {
        this.onUpdateCallback = callback;
    }

    @FXML
    public void eguneratu(ActionEvent event) {
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();
        String erabiltzailea = txtErabiltzailea.getText().trim();
        String rolaDeskribapena = cbRola.getValue();

        if (izena.isEmpty() || abizena.isEmpty() || erabiltzailea.isEmpty() || rolaDeskribapena == null) {
            erakutsiErrorea("Eremu guztiak bete behar dira.");
            return;
        }

        int idRola = -1;
        for (String[] r : rolak) {
            if (r[1].equals(rolaDeskribapena)) {
                idRola = Integer.parseInt(r[0]);
                break;
            }
        }

        boolean ondo = LangileaDAO.eguneratu(langilea.getLangileId(), izena, abizena, erabiltzailea, idRola);

        if (ondo) {
            if (onUpdateCallback != null) onUpdateCallback.run(); // Taula nagusia freskatu
            itxiLeihoa();
        } else {
            erakutsiErrorea("Errorea gertatu da datu-basean eguneratzean.");
        }
    }

    @FXML
    public void utzi(ActionEvent event) { itxiLeihoa(); }

    private void erakutsiErrorea(String mezua) {
        lblErrorea.setText(mezua);
        lblErrorea.setVisible(true);
        lblErrorea.setManaged(true);
    }

    private void itxiLeihoa() {
        Stage stage = (Stage) btnUtzi.getScene().getWindow();
        stage.close();
    }
}