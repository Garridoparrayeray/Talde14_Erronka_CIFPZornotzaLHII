package controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.EstadistikaDAO;
import dao.KategoriaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import model.Kategoria;
import model.KategoriaKopurua;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Kategorien ikuspegi dinamikoa kudeatzen duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class KategoriakController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(KategoriakController.class);

    @FXML
    private StackPane contentArea;
    @FXML
    private VBox vboxKategoriak;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kargatu();
    }

    private void kargatu() {
        vboxKategoriak.getChildren().clear();

        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        List<KategoriaKopurua> kopuruak = EstadistikaDAO.kategoriaKopuruak();

        HBox row = null;
        int idx = 0;
        for (Kategoria k : kategoriak) {
            if (idx % 3 == 0) {
                row = new HBox(20);
                row.setFillHeight(true);
                vboxKategoriak.getChildren().add(row);
            }

            int kop = 0;
            for (KategoriaKopurua kk : kopuruak) {
                if (k.getIzena().equals(kk.getKategoriaIzena())) {
                    kop = kk.getKopurua();
                    break;
                }
            }

            HBox txartela = sortuTxartela(k, kop);
            HBox.setHgrow(txartela, Priority.ALWAYS);
            row.getChildren().add(txartela);
            idx++;
        }

        if (row != null && kategoriak.size() % 3 != 0) {
            int falta = 3 - (kategoriak.size() % 3);
            for (int i = 0; i < falta; i++) {
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);
                row.getChildren().add(spacer);
            }
        }
    }

    /**
     * Kategoria baten txartela sortzen du, editatu eta ezabatu botoiekin.
     *
     * @param k Erakutsi beharreko kategoria
     * @param kopurua Kategoria horretan dauden artikulu kopurua
     * @return Txartelaren HBox nodoa
     */
    private HBox sortuTxartela(Kategoria k, int kopurua) {
        VBox info = new VBox(4);
        Label lblIzena = new Label(k.getIzena());
        lblIzena.getStyleClass().addAll("font-bold", "text-dark");
        String kopText;
        if (kopurua == 1) {
            kopText = "1 artikulu";
        } else {
            kopText = kopurua + " artikulu";
        }
        Label lblKop = new Label(kopText);
        lblKop.getStyleClass().add("text-muted");
        lblKop.setStyle("-fx-font-size: 11px;");
        info.getChildren().addAll(lblIzena, lblKop);
        HBox.setHgrow(info, Priority.ALWAYS);

        Button btnEdita = new Button("Editatu");
        btnEdita.getStyleClass().add("btn-outline");
        btnEdita.setStyle("-fx-padding: 4 12; -fx-font-size: 11px;");
        btnEdita.setOnAction(e -> editatuKategoria(k));

        Button btnEzabatu = new Button("Ezabatu");
        btnEzabatu.getStyleClass().add("btn-danger");
        btnEzabatu.setStyle("-fx-padding: 4 12; -fx-font-size: 11px;");
        btnEzabatu.setOnAction(e -> ezabatuKategoria(k));

        HBox botoiak = new HBox(8, btnEdita, btnEzabatu);
        botoiak.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        HBox txartela = new HBox(15);
        txartela.getStyleClass().add("card");
        txartela.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        txartela.setPadding(new Insets(15));
        txartela.getChildren().addAll(info, botoiak);

        return txartela;
    }

    /**
     * Kategoria baten izena aldatzeko elkarrizketa-koadroa irekitzen du.
     *
     * @param k Editatu beharreko kategoria
     */
    private void editatuKategoria(Kategoria k) {
        TextInputDialog dlg = new TextInputDialog(k.getIzena());
        dlg.setTitle("Kategoria editatu");
        dlg.setHeaderText(null);
        dlg.setContentText("Kategoriaren izena:");
        dlg.initOwner(vboxKategoriak.getScene().getWindow());
        Optional<String> result = dlg.showAndWait();
        result.ifPresent(izena -> {
            String trimmed = izena.trim();
            if (!trimmed.isEmpty()) {
                KategoriaDAO.aldatuIzena(k.getKategoriaId(), trimmed);
                kargatu();
            }
        });
    }

    /**
     * Baieztapen-alerta erakutsi eta kategoria ezabatzen du onartu bada.
     *
     * @param k Ezabatu beharreko kategoria
     */
    private void ezabatuKategoria(Kategoria k) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Kategoria ezabatu");
        confirm.setHeaderText(null);
        confirm.setContentText("'" + k.getIzena() + "' kategoria ezabatuko da. Ziur zaude?");
        confirm.initOwner(vboxKategoriak.getScene().getWindow());
        confirm.showAndWait().ifPresent(btn -> {
            if (btn == ButtonType.OK) {
                KategoriaDAO.ezabatu(k.getKategoriaId());
                kargatu();
            }
        });
    }

    /**
     * Kategoria berri bat gehitzeko formularioa contentArea-n kargatzen du.
     */
    @FXML
    public void kategoriaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KategoriaBerria.fxml"));
            Node nodoa = loader.load();
            KategoriaBerriController ctrl = loader.getController();
            ctrl.setContentArea(contentArea);
            UIKudeatzailea.kargatuPanela(contentArea, nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "kategoriaBerria: FXML kargatzean errorea", e);
        }
    }
}
