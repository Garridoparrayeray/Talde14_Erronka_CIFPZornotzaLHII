package utils;

import java.util.HashMap;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Interfaze grafikoaren kudeaketa errazteko klase laguntzailea. Erroreak
 * erakutsi, panelak kargatu eta leihoak aldatzeko metodoak eskaintzen ditu.
 *
 * @author Yeray Garrido
 */
public class UIKudeatzailea {

    private static final Map<StackPane, StackPane> wrapperrak = new HashMap<>();

    /**
     * Errore mezu bat erakusten du pantailan Alert leiho baten bidez.
     *
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
     *
     * @param contentArea Panela kargatuko den gunea
     * @param fxmlBidea FXML fitxategiaren bidea
     */
    public static void kargatuPanela(StackPane contentArea, String fxmlBidea) {
        try {
            StackPane aurrekoa = wrapperrak.remove(contentArea);
            if (aurrekoa != null) {
                aurrekoa.prefWidthProperty().unbind();
                aurrekoa.prefHeightProperty().unbind();
            }

            Node nodoa = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            StackPane envoltorio = new StackPane(nodoa);
            StackPane.setAlignment(nodoa, javafx.geometry.Pos.TOP_LEFT);
            envoltorio.prefWidthProperty().bind(contentArea.widthProperty());
            envoltorio.prefHeightProperty().bind(contentArea.heightProperty());

            wrapperrak.put(contentArea, envoltorio);
            contentArea.getChildren().setAll(envoltorio);
        } catch (Exception e) {
            erakutsiErrorea("Errorea bista kargatzean",
                    "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }

    /**
     * Aurretik kargatutako nodo bat StackPane gunean kokatzen du.
     *
     * @param contentArea Panela kokatuko den gunea
     * @param nodoa Dagoeneko kargatutako nodo grafikoa
     */
    public static void kargatuPanela(StackPane contentArea, Node nodoa) {
        StackPane aurrekoa = wrapperrak.remove(contentArea);
        if (aurrekoa != null) {
            aurrekoa.prefWidthProperty().unbind();
            aurrekoa.prefHeightProperty().unbind();
        }

        StackPane envoltorio = new StackPane(nodoa);
        StackPane.setAlignment(nodoa, javafx.geometry.Pos.TOP_LEFT);
        envoltorio.prefWidthProperty().bind(contentArea.widthProperty());
        envoltorio.prefHeightProperty().bind(contentArea.heightProperty());

        wrapperrak.put(contentArea, envoltorio);
        contentArea.getChildren().setAll(envoltorio);
    }

    /**
     * Leiho berri bat kargatzen du eta uneko leihoa ordezkatzen du.
     *
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
            erakutsiErrorea("Errorea leihoa aldatzean",
                    "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }
}
