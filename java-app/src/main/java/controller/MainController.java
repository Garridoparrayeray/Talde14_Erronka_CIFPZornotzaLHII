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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Langilea;
import utils.Sesio;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML private StackPane contentArea;

    @FXML private Button btnPanela;
    @FXML private Button btnInbentarioa;
    @FXML private Button btnErregistroa;
    @FXML private Button btnErreklamazioak;
    @FXML private Button btnEmanaldia;
    @FXML private Button btnGalduDabenak;

    @FXML private Label  lblLangileIzena;
    @FXML private Label  lblLangileRola;
    @FXML private Label  lblInitialak;
    @FXML private VBox   boxAdminSwitch;

    private List<Button> navBotoiak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        navBotoiak = List.of(btnPanela, btnInbentarioa, btnErregistroa, btnErreklamazioak, btnEmanaldia, btnGalduDabenak);

        Langilea l = Sesio.getLangilea();
        if (l != null) {
            lblLangileIzena.setText(l.getIzena() + " " + l.getAbizena());
            if (Sesio.isAdmin()) {
                lblLangileRola.setText("Administratzailea");
            } else {
                lblLangileRola.setText("Udaltzaingoa");
            }
            String ini = String.valueOf(l.getIzena().charAt(0)) + l.getAbizena().charAt(0);
            lblInitialak.setText(ini.toUpperCase());
        }

        // Admin switch botoia ezkutu langile arruntentzat
        if (boxAdminSwitch != null) {
            boxAdminSwitch.setVisible(Sesio.isAdmin());
            boxAdminSwitch.setManaged(Sesio.isAdmin());
        }

        setAktibo(btnPanela);
        kargatu("/view/Panela.fxml");
    }

    @FXML public void loadPanela()         { setAktibo(btnPanela);         kargatu("/view/Panela.fxml"); }
    @FXML public void loadInbentarioa()    { setAktibo(btnInbentarioa);    kargatu("/view/Inbentario.fxml"); }
    @FXML public void loadErregistroa()    { setAktibo(btnErregistroa);    kargatu("/view/Erregistroa.fxml"); }
    @FXML public void loadErreklamazioak() { setAktibo(btnErreklamazioak); kargatu("/view/Erreklamazioak.fxml"); }
    @FXML public void loadEmanaldia()      { setAktibo(btnEmanaldia);      kargatu("/view/Emanaldia.fxml"); }
    @FXML public void loadGalduDabenak()   { setAktibo(btnGalduDabenak);   kargatu("/view/GalduDabenak.fxml"); }

    @FXML
    public void irAAdmin() {
        if (!Sesio.isAdmin()) return;
        kargatuLeihoa("/view/AdminLayout.fxml");
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
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            System.err.println("MainController.kargatu (" + fxmlBidea + "): " + e.getMessage());
        }
    }

    private void kargatuLeihoa(String fxmlBidea) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlBidea));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            System.err.println("MainController.kargatuLeihoa (" + fxmlBidea + "): " + e.getMessage());
        }
    }
}
