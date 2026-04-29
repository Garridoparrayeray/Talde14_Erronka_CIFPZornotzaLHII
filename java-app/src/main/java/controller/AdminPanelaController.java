package controller;

import dao.BackupDAO;
import dao.EstadistikaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import utils.Sesio;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminPanelaController implements Initializable {

    @FXML private Label lblLangileak;
    @FXML private Label lblArtikuluak;
    @FXML private Label lblKategoriak;
    @FXML private Label lblKokalekuak;
    @FXML private Label lblDbEgoera;
    @FXML private Label lblAzkenKopia;
    @FXML private Label lblIzena;

    private static String azkenKopiaDatea = "Inoiz ez";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Sesio.getLangilea() != null && lblIzena != null) {
            lblIzena.setText(Sesio.getLangilea().getIzena() + " " + Sesio.getLangilea().getAbizena());
        }

        lblLangileak.setText(String.valueOf(EstadistikaDAO.langileKopurua()));
        lblArtikuluak.setText(String.valueOf(EstadistikaDAO.artikuluGuztienKopurua()));
        lblKategoriak.setText(String.valueOf(EstadistikaDAO.kategoriaKopurua()));
        lblKokalekuak.setText(String.valueOf(EstadistikaDAO.kokalekuakKopurua()));

        boolean konektatuta = EstadistikaDAO.dbKonexioaEgiaztatu();
        ezarriDbEgoera(konektatuta);

        lblAzkenKopia.setText(azkenKopiaDatea);
    }

    @FXML
    public void egiaztatuKonexioa() {
        boolean ok = EstadistikaDAO.dbKonexioaEgiaztatu();
        ezarriDbEgoera(ok);
    }

    @FXML
    public void eginBabesKopia() {
        try {
            BackupDAO.eginBabesKopia();
            azkenKopiaDatea = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            lblAzkenKopia.setText(azkenKopiaDatea);
            lblAzkenKopia.getStyleClass().removeAll("text-danger", "text-success");
            lblAzkenKopia.getStyleClass().add("text-success");
        } catch (Exception e) {
            lblAzkenKopia.setText("Errorea: " + e.getMessage());
            lblAzkenKopia.getStyleClass().removeAll("text-danger", "text-success");
            lblAzkenKopia.getStyleClass().add("text-danger");
        }
    }

    // ── Laguntzaileak ────────────────────────────────────────────────────────

    private void ezarriDbEgoera(boolean konektatuta) {
        lblDbEgoera.getStyleClass().removeAll("text-success", "text-danger");
        if (konektatuta) {
            lblDbEgoera.setText("Konektatuta");
            lblDbEgoera.getStyleClass().add("text-success");
        } else {
            lblDbEgoera.setText("Errorea");
            lblDbEgoera.getStyleClass().add("text-danger");
        }
    }
}
