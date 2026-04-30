package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
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
            // Aurreko panelaren size-binding askatu
            for (Node child : contentArea.getChildren()) {
                if (child instanceof Region) {
                    ((Region) child).prefWidthProperty().unbind();
                    ((Region) child).prefHeightProperty().unbind();
                }
            }

            Node node = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));

            // Panel berria beti contentArea-ren tamaina bete dezan lotu
            if (node instanceof Region) {
                Region r = (Region) node;
                StackPane.setAlignment(r, javafx.geometry.Pos.TOP_LEFT);
                r.prefWidthProperty().bind(contentArea.widthProperty());
                r.prefHeightProperty().bind(contentArea.heightProperty());
            }

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

            // Uneko dimentsioak gorde — Scene berria sortzean leihoa ez jauzi dadin
            boolean zenMaximizatua = stage.isMaximized();
            double w = stage.getScene().getWidth();
            double h = stage.getScene().getHeight();

            // Maximizazioa kendu Scene aldatu aurretik (Windows-en beharrezkoa)
            stage.setMaximized(false);
            stage.setScene(new Scene(root, w, h));

            if (maximizatu || zenMaximizatua) {
                stage.setMaximized(true);
            }
        } catch (Exception e) {
            erakutsiErrorea("Errorea leihoa aldatzean", "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }
}