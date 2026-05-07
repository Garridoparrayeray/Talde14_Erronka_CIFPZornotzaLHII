package utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Interfaze grafikoaren kudeaketa errazteko klase laguntzailea. Erroreak
 * erakutsi, panelak kargatu eta leihoak aldatzeko metodoak eskaintzen ditu.
 *
 * @author Yeray Garrido
 */
public class UIKudeatzailea {

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
     * @param edukiGunea Panela kargatuko den gunea
     * @param fxmlBidea FXML fitxategiaren bidea
     */
    public static void kargatuPanela(StackPane edukiGunea, String fxmlBidea) {
        try {
            kargatuPanela(edukiGunea, (Node) FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea)));
        } catch (Exception e) {
            erakutsiErrorea("Errorea bista kargatzean",
                    "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }

    /**
     * Aurretik kargatutako nodo bat StackPane gunean kokatzen du.
     *
     * @param edukiGunea Panela kokatuko den gunea
     * @param nodoa Dagoeneko kargatutako nodo grafikoa
     */
    public static void kargatuPanela(StackPane edukiGunea, Node nodoa) {
        edukiGunea.getChildren().setAll(nodoa);
    }

    /**
     * TableColumn bateko testua hitz-jauziarekin erakusten du, "..." moztu gabe.
     * Deskribapen luzeak dituzten zutabeetarako erabili initialize() barruan.
     *
     * @param <T>     Taulako errenkadaren mota generikoa
     * @param zutabea Testu osoa erakutsi nahi den zutabea
     */
    public static <T> void ehundatuZelulak(TableColumn<T, String> zutabea) {
        zutabea.setCellFactory(col -> new TableCell<T, String>() {
            private final Text testua = new Text();
            {
                testua.wrappingWidthProperty().bind(col.widthProperty().subtract(10));
                setGraphic(testua);
            }
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    testua.setText("");
                } else {
                    testua.setText(item);
                }
            }
        });
    }

    /**
     * Leiho berri bat kargatzen du eta uneko leihoa ordezkatzen du.
     *
     * @param egungoNodoa Uneko leihoaren edozein nodo (Stage lortzeko)
     * @param fxmlBidea   Leiho berriaren FXML bidea
     * @param maximizatu  Leihoa maximizatuta agertuko den ala ez
     */
    public static void aldatuLeihoa(Node egungoNodoa, String fxmlBidea, boolean maximizatu) {
        try {
            Parent erroa = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            Stage leihoa = (Stage) egungoNodoa.getScene().getWindow();

            // Uneko dimentsioak gorde — Scene berria sortzean leihoa ez jauzi dadin
            boolean zenMaximizatua = leihoa.isMaximized();
            double zabalera = leihoa.getScene().getWidth();
            double altuera = leihoa.getScene().getHeight();

            // Maximizazioa kendu Scene aldatu aurretik (Windows-en beharrezkoa)
            leihoa.setMaximized(false);
            leihoa.setScene(new Scene(erroa, zabalera, altuera));

            if (maximizatu || zenMaximizatua) {
                leihoa.setMaximized(true);
            }
        } catch (Exception e) {
            erakutsiErrorea("Errorea leihoa aldatzean",
                    "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }
}
