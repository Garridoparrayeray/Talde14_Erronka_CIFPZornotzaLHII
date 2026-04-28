package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Langilea;
import utils.Sesio;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    @FXML private StackPane adminContentArea;

    @FXML private Button btnAdminPanela;
    @FXML private Button btnLangileak;
    @FXML private Button btnKategoriak;
    @FXML private Button btnKokalekuak;
    @FXML private Button btnAuditoria;

    @FXML private Label lblLangileIzena;
    @FXML private Label lblLangileRola;
    @FXML private Label lblInitialak;

    private List<Button> navBotoiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navBotoiak = List.of(btnAdminPanela, btnLangileak, btnKategoriak, btnKokalekuak, btnAuditoria);

        Langilea l = Sesio.getLangilea();
        if (l != null) {
            lblLangileIzena.setText(l.getIzena() + " " + l.getAbizena());
            lblLangileRola.setText("Administratzailea");
            String ini = String.valueOf(l.getIzena().charAt(0)) + l.getAbizena().charAt(0);
            lblInitialak.setText(ini.toUpperCase());
        }

        setAktibo(btnAdminPanela);
        kargatu("/view/AdminPanela.fxml");
    }

    @FXML public void loadAdminPanela() { setAktibo(btnAdminPanela); kargatu("/view/AdminPanela.fxml"); }
    @FXML public void loadLangileak()   { setAktibo(btnLangileak);   kargatu("/view/Langileak.fxml"); }
    @FXML public void loadKategoriak()  { setAktibo(btnKategoriak);  kargatu("/view/Kategoriak.fxml"); }
    @FXML public void loadKokalekuak()  { setAktibo(btnKokalekuak);  kargatu("/view/Kokalekuak.fxml"); }
    @FXML public void loadAuditoria()   { setAktibo(btnAuditoria);   kargatu("/view/Auditoria.fxml"); }

    @FXML
    public void volverAUser() {
        kargatuLeihoa("/view/MainLayout.fxml");
    }

    @FXML
    public void itxiSaioa() {
        Sesio.itxi();
        kargatuLeihoa("/view/login.fxml");
    }

    // ---- helpers ----

    private void setAktibo(Button aktibo) {
        for (Button b : navBotoiak) {
            b.getStyleClass().removeAll("nav-item-active", "nav-item");
            b.getStyleClass().add("nav-item");
        }
        aktibo.getStyleClass().removeAll("nav-item");
        aktibo.getStyleClass().add("nav-item-active");
    }

    private void kargatu(String fxmlBidea) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(fxmlBidea));
            adminContentArea.getChildren().setAll(node);
        } catch (Exception e) {
            System.err.println("AdminController.kargatu (" + fxmlBidea + "): " + e.getMessage());
        }
    }

    private void kargatuLeihoa(String fxmlBidea) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlBidea));
            Stage stage = (Stage) adminContentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("AdminController.kargatuLeihoa (" + fxmlBidea + "): " + e.getMessage());
        }
    }
}
