package utils;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Interfaze grafikoaren kudeaketa errazteko klase laguntzailea.
 *
 * @author Yeray Garrido
 */
public class UIKudeatzailea {

    private static StackPane unekoEdukiGunea;

    /**
     * Eduki-gune globalaren erreferentzia ezartzen du overlay eta panel
     * eragiketak egiteko.
     *
     * @param edukiGunea Erregistratu beharreko StackPane nagusia
     */
    public static void setEdukiGunea(StackPane edukiGunea) {
        unekoEdukiGunea = edukiGunea;
    }

    // ─── OVERLAY OINARRIA ────────────────────────────────────────────────────
    private static StackPane sortuOinarriaEdukiarekin(String titulua, Node edukia, Node... botoiak) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setMaxWidth(Double.MAX_VALUE);
        overlay.setMaxHeight(Double.MAX_VALUE);

        Label lblTit = new Label(titulua);
        lblTit.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#111827;");

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.getChildren().addAll(botoiak);

        VBox karta = new VBox(16, lblTit, edukia, btnBox);
        karta.setMaxWidth(420);
        karta.setStyle("-fx-background-color:white;-fx-background-radius:10;"
                + "-fx-padding:30;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.25),20,0,0,4);");
        StackPane.setAlignment(karta, Pos.CENTER);
        overlay.getChildren().add(karta);
        return overlay;
    }

    private static Button btnPrimario(String testua) {
        Button b = new Button(testua);
        b.setStyle("-fx-background-color:#3457D5;-fx-text-fill:white;-fx-font-weight:bold;"
                + "-fx-background-radius:6;-fx-padding:9 20;-fx-cursor:hand;");
        return b;
    }

    private static Button btnOutline(String testua) {
        Button b = new Button(testua);
        b.setStyle("-fx-background-color:transparent;-fx-border-color:#D1D5DB;"
                + "-fx-border-radius:6;-fx-text-fill:#374151;"
                + "-fx-padding:9 20;-fx-cursor:hand;");
        return b;
    }

    /**
     * Overlay bat erakusten du eduki librea duena (TextField, GridPane...).
     * "Gorde" sakatzean {@code onGorde} deitzen da; "Utzi" sakatzean itxi.
     *
     * @param titulua Dialogoaren titulua
     * @param edukia Formularioko edukia (Node)
     * @param onGorde Gorde sakatzean exekutatzen den ekintza
     */
    public static void erakutsiFormOverlay(String titulua, Node edukia, Runnable onGorde) {
        if (unekoEdukiGunea == null) {
            return;
        }
        Button btnUtzi = btnOutline("Utzi");
        Button btnGorde = btnPrimario("Gorde");
        StackPane overlay = sortuOinarriaEdukiarekin(titulua, edukia, btnUtzi, btnGorde);
        unekoEdukiGunea.getChildren().add(overlay);
        btnUtzi.setOnAction(e -> unekoEdukiGunea.getChildren().remove(overlay));
        btnGorde.setOnAction(e -> {
            unekoEdukiGunea.getChildren().remove(overlay);
            onGorde.run();
        });
    }

    /**
     * Pantailaren gainean agertzen den mezu labur bat erakusten du 3 segundoz,
     * leihorik ireki gabe.
     *
     * @param mezua Erakutsi beharreko testua
     * @param arrakasta true → berde (OK); false → gorri (errorea)
     */
    public static void erakutsiToast(String mezua, boolean arrakasta) {
        if (unekoEdukiGunea == null) {
            return;
        }
        Label toast = new Label(mezua);
        toast.setMaxWidth(Double.MAX_VALUE);
        toast.setAlignment(Pos.CENTER);
        toast.setWrapText(true);
        if (arrakasta) {
            toast.setStyle("-fx-background-color:#DEF7EC;-fx-text-fill:#03543F;"
                    + "-fx-padding:12 20;-fx-font-weight:bold;-fx-font-size:13px;");
        } else {
            toast.setStyle("-fx-background-color:#FDE8E8;-fx-text-fill:#9B1C1C;"
                    + "-fx-padding:12 20;-fx-font-weight:bold;-fx-font-size:13px;");
        }
        StackPane.setAlignment(toast, Pos.TOP_CENTER);
        unekoEdukiGunea.getChildren().add(toast);
        PauseTransition pausa = new PauseTransition(Duration.seconds(3));
        pausa.setOnFinished(e -> unekoEdukiGunea.getChildren().remove(toast));
        pausa.play();
    }

    // ─── PANELAK ─────────────────────────────────────────────────────────────
    /**
     * FXML panel bat kargatzen du aurrez erregistratutako gune nagusian.
     *
     * @param fxmlBidea FXML fitxategiaren bidea
     */
    public static void kargatuPanela(String fxmlBidea) {
        if (unekoEdukiGunea == null) {
            return;
        }
        try {
            Node nodoa = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            kargatuPanela(nodoa);
        } catch (Exception e) {
            erakutsiToast("Errorea bista kargatzean: " + fxmlBidea, false);
        }
    }

    /**
     * Nodo bat kargatzen du aurrez erregistratutako gune nagusian.
     *
     * @param nodoa Dagoeneko kargatutako nodo grafikoa
     */
    public static void kargatuPanela(Node nodoa) {
        if (unekoEdukiGunea != null) {
            unekoEdukiGunea.getChildren().setAll(nodoa);
        }
    }

    // ─── FORMULARIO ERROREAK ─────────────────────────────────────────────────
    /**
     * Formulario-errore etiketa bete eta ikusgai jartzen du.
     *
     * @param lblErrorea Ikusgai jarri beharreko errore-etiketa
     * @param mezua      Erakutsi beharreko errore-mezua
     */
    public static void erakutsiFormularioErrorea(Label lblErrorea, String mezua) {
        if (lblErrorea != null) {
            lblErrorea.setText(mezua);
            lblErrorea.setVisible(true);
            lblErrorea.setManaged(true);
        }
    }

    /**
     * Formulario-errore etiketa ezkutatzen du eta espazio-lekua askatzen du.
     *
     * @param lblErrorea Ezkutatu beharreko errore-etiketa
     */
    public static void ezkutuFormularioErrorea(Label lblErrorea) {
        if (lblErrorea != null) {
            lblErrorea.setVisible(false);
            lblErrorea.setManaged(false);
        }
    }

    // ─── TAULA ZELULAK ───────────────────────────────────────────────────────
    /**
     * Taula-zutabe baten zelulak testua bilduz errendatzeko konfiguratzen du.
     *
     * @param <T>     Taularen errenkada-mota
     * @param zutabea Konfiguratu beharreko zutabea
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

    // ─── LEIHO ALDAKETA ──────────────────────────────────────────────────────
    /**
     * Uneko leihoaren erroa FXML berri batez ordezkatzen du, aukeran
     * maximizatuz.
     *
     * @param egungoNodoa Uneko eszena-grafoko edozein nodo (Stage lortzeko)
     * @param fxmlBidea   Kargatu beharreko FXML fitxategiaren bidea
     * @param maximizatu  true bada leihoa maximizatu egiten du
     */
    public static void aldatuLeihoa(Node egungoNodoa, String fxmlBidea, boolean maximizatu) {
        try {
            Parent erroa = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            Stage leihoa = (Stage) egungoNodoa.getScene().getWindow();
            leihoa.getScene().setRoot(erroa);
            if (maximizatu) {
                leihoa.setMaximized(true);
            }
        } catch (Exception e) {
            erakutsiToast("Errorea leihoa aldatzean: " + fxmlBidea, false);
        }
    }
}
