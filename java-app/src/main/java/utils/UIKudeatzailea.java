package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Interfaze grafikoaren kudeaketa errazteko klase laguntzailea.
 * Erroreak erakutsi, panelak kargatu eta leihoak aldatzeko metodoak eskaintzen ditu.
 * @author Yeray Garrido
 */
public class UIKudeatzailea {

    /**
     * Errore mezu bat erakusten du pantailan Alert leiho baten bidez.
     * @param titulua Alerta leihoaren titulua
     * @param mezua Erakutsi beharreko errore mezua
     */
    public static void erakutsiErrorea(String titulua, String mezua) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle(titulua);
        alerta.setHeaderText(null);
        alerta.setContentText(mezua);
        alerta.showAndWait();
    }

    /**
     * FXML panel bat kargatzen du zehaztutako StackPane gunean.
     * @param contentArea Panela kargatuko den gunea
     * @param fxmlBidea FXML fitxategiaren bidea
     */
    public static void kargatuPanela(StackPane contentArea, String fxmlBidea) {
        try {
            Node node = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            contentArea.getChildren().setAll(node);
        } catch (Exception e) {
            erakutsiErrorea("Errorea bista kargatzean", "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }

    /**
     * Leiho berri bat kargatzen du eta uneko leihoa ordezkatzen du.
     * @param egungoNodoa Uneko leihoaren edozein nodo (Stage lortzeko)
     * @param fxmlBidea Leiho berriaren FXML bidea
     * @param maximizatu Leihoa maximizatuta agertuko den ala ez
     */
    public static void aldatuLeihoa(Node egungoNodoa, String fxmlBidea, boolean maximizatu) {
        try {
            Parent root = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            Stage stage = (Stage) egungoNodoa.getScene().getWindow();
            stage.setScene(new Scene(root));
            
            if (maximizatu) {
                stage.setMaximized(true);
            }
        } catch (Exception e) {
            erakutsiErrorea("Errorea leihoa aldatzean", "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }
}