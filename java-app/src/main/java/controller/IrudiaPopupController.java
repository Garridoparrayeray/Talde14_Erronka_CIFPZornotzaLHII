package controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Artikulua;
import utils.AppConfig;

/**
 * Artikuluaren argazkia eta deskribapena erakusten dituen popup kontroladorea.
 */
public class IrudiaPopupController implements Initializable {

    @FXML private ImageView imgArgazkia;
    @FXML private Label lblKodea;
    @FXML private Label lblDeskribapena;
    @FXML private Label lblNoImage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Artikuluaren datuak kargatzen ditu popup-ean.
     *
     * @param artikulua Erakutsi beharreko artikulua
     */
    public void kargatu(Artikulua artikulua) {
        if (artikulua == null) return;

        lblKodea.setText(artikulua.getArtikuluKodea() + " — " + artikulua.getIzenburua());
        lblDeskribapena.setText(artikulua.getDeskribapenaSegurua());

        String argazkiBidea = artikulua.getArgazkiBidea();
        if (argazkiBidea != null && !argazkiBidea.isEmpty()) {
            // Fitxategi-izen hutsa bada, artikulu_irudiak/ karpetan bilatzen du
            File irudiFile = new File(argazkiBidea);
            if (!irudiFile.isAbsolute()) {
                irudiFile = new File(AppConfig.getArtikuluIrudiakBidea(), argazkiBidea);
            }
            if (irudiFile.exists()) {
                try {
                    Image img = new Image(irudiFile.toURI().toString());
                    imgArgazkia.setImage(img);
                    imgArgazkia.setVisible(true);
                    imgArgazkia.setManaged(true);
                    return;
                } catch (Exception ignored) {
                }
            }
        }
        // Argazkirik ez
        imgArgazkia.setVisible(false);
        imgArgazkia.setManaged(false);
        if (lblNoImage != null) {
            lblNoImage.setVisible(true);
            lblNoImage.setManaged(true);
        }
    }

    @FXML
    private void itxi() {
        Stage stage = (Stage) lblKodea.getScene().getWindow();
        stage.close();
    }
}
