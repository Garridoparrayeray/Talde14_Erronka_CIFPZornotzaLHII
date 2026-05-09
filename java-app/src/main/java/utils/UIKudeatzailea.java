package utils;

import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

    /** Eduki-gune globalaren erreferentzia ezartzen du overlay eta panel eragiketak egiteko. */
    public static void setEdukiGunea(StackPane edukiGunea) {
        unekoEdukiGunea = edukiGunea;
    }

    // ─── OVERLAY OINARRIA ────────────────────────────────────────────────────

    private static StackPane sortuOinarria(String titulua, String mezua, Node... botoiak) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");
        overlay.setMaxWidth(Double.MAX_VALUE);
        overlay.setMaxHeight(Double.MAX_VALUE);

        Label lblTit = new Label(titulua);
        lblTit.setStyle("-fx-font-size:16px;-fx-font-weight:bold;-fx-text-fill:#111827;");
        lblTit.setWrapText(true);

        Label lblMsg = new Label(mezua);
        lblMsg.setStyle("-fx-font-size:13px;-fx-text-fill:#374151;");
        lblMsg.setWrapText(true);
        lblMsg.setMaxWidth(340);

        HBox btnBox = new HBox(10);
        btnBox.setAlignment(Pos.CENTER_RIGHT);
        btnBox.getChildren().addAll(botoiak);

        VBox karta = new VBox(16, lblTit, lblMsg, btnBox);
        karta.setMaxWidth(400);
        karta.setStyle("-fx-background-color:white;-fx-background-radius:10;"
                + "-fx-padding:30;-fx-effect:dropshadow(gaussian,rgba(0,0,0,0.25),20,0,0,4);");
        StackPane.setAlignment(karta, Pos.CENTER);
        overlay.getChildren().add(karta);
        return overlay;
    }

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

    // ─── ALERTA (overlay edota Alert fallback) ───────────────────────────────

    /**
     * Alerta mezu bat overlay gisa erakusten du, OS leihorik ireki gabe.
     *
     * @param mota    Alertaren mota (ERROR, WARNING, INFORMATION...)
     * @param titulua Dialogoaren titulua
     * @param mezua   Erakutsi beharreko mezua
     */
    public static void erakutsiAlerta(Alert.AlertType mota, String titulua, String mezua) {
        if (unekoEdukiGunea == null) {
            Alert a = new Alert(mota, mezua, ButtonType.OK);
            a.setTitle(titulua);
            a.setHeaderText(null);
            a.showAndWait();
            return;
        }
        Button btnOk = btnPrimario("Ados");
        StackPane overlay = sortuOinarria(titulua, mezua, btnOk);
        unekoEdukiGunea.getChildren().add(overlay);
        btnOk.setOnAction(e -> unekoEdukiGunea.getChildren().remove(overlay));
    }

    /**
     * Errore mezu bat overlay gisa erakusten du.
     *
     * @param titulua Dialogoaren titulua
     * @param mezua   Erakutsi beharreko errore mezua
     */
    public static void erakutsiErrorea(String titulua, String mezua) {
        erakutsiAlerta(Alert.AlertType.ERROR, titulua, mezua);
    }

    // ─── FORM OVERLAY (edukia libreki definitzeko) ───────────────────────────

    /**
     * Overlay bat erakusten du eduki librea duena (TextField, GridPane...).
     * "Gorde" sakatzean {@code onGorde} deitzen da; "Utzi" sakatzean itxi.
     *
     * @param titulua  Dialogoaren titulua
     * @param edukia   Formularioko edukia (Node)
     * @param onGorde  Gorde sakatzean exekutatzen den ekintza
     */
    public static void erakutsiFormOverlay(String titulua, Node edukia, Runnable onGorde) {
        if (unekoEdukiGunea == null) return;
        Button btnUtzi  = btnOutline("Utzi");
        Button btnGorde = btnPrimario("Gorde");
        StackPane overlay = sortuOinarriaEdukiarekin(titulua, edukia, btnUtzi, btnGorde);
        unekoEdukiGunea.getChildren().add(overlay);
        btnUtzi.setOnAction(e  -> unekoEdukiGunea.getChildren().remove(overlay));
        btnGorde.setOnAction(e -> {
            unekoEdukiGunea.getChildren().remove(overlay);
            onGorde.run();
        });
    }

    // ─── TOAST ───────────────────────────────────────────────────────────────

    /**
     * Pantailaren gainean agertzen den mezu labur bat erakusten du 3 segundoz,
     * leihorik ireki gabe.
     *
     * @param mezua     Erakutsi beharreko testua
     * @param arrakasta true → berde (OK);  false → gorri (errorea)
     */
    public static void erakutsiToast(String mezua, boolean arrakasta) {
        if (unekoEdukiGunea == null) return;
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
        if (unekoEdukiGunea == null) return;
        try {
            Node nodoa = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            kargatuPanela(nodoa);
        } catch (Exception e) {
            erakutsiErrorea("Errorea bista kargatzean",
                    "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
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

    /** Formulario-errore etiketa bete eta ikusgai jartzen du. */
    public static void erakutsiFormularioErrorea(Label lblErrorea, String mezua) {
        if (lblErrorea != null) {
            lblErrorea.setText(mezua);
            lblErrorea.setVisible(true);
            lblErrorea.setManaged(true);
        }
    }

    /** Formulario-errore etiketa ezkutatzen du eta espazio-lekua askatzen du. */
    public static void ezkutuFormularioErrorea(Label lblErrorea) {
        if (lblErrorea != null) {
            lblErrorea.setVisible(false);
            lblErrorea.setManaged(false);
        }
    }

    // ─── TAULA ZELULAK ───────────────────────────────────────────────────────

    /** Taula-zutabe baten zelulak testua bilduz errendatzeko konfiguratzen du. */
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
                testua.setText((empty || item == null) ? "" : item);
            }
        });
    }

    // ─── LEIHO ALDAKETA ──────────────────────────────────────────────────────

    /** Uneko leihoaren erroa FXML berri batez ordezkatzen du, aukeran maximizatuz. */
    public static void aldatuLeihoa(Node egungoNodoa, String fxmlBidea, boolean maximizatu) {
        try {
            Parent erroa = FXMLLoader.load(UIKudeatzailea.class.getResource(fxmlBidea));
            Stage leihoa = (Stage) egungoNodoa.getScene().getWindow();
            leihoa.getScene().setRoot(erroa);
            if (maximizatu) leihoa.setMaximized(true);
        } catch (Exception e) {
            erakutsiErrorea("Errorea leihoa aldatzean",
                    "Ezin izan da kargatu: " + fxmlBidea + "\n\nArrazoia: " + e.getMessage());
        }
    }
}
