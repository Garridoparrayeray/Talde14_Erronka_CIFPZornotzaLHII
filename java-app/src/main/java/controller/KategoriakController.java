package controller;

import java.net.URL;
import java.util.List;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
    private VBox vboxKategoriak;

    /**
     * Kontroladorea hasieratzen du eta kategoriak kargatzen ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kargatu();
    }

    /**
     * Kategoriak datu-basetik kargatzen ditu eta txartelak GridPane batean
     * hiru zutabetan antolatuta erakusten ditu.
     */
    private void kargatu() {
        vboxKategoriak.getChildren().clear();

        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        List<KategoriaKopurua> kopuruak = EstadistikaDAO.kategoriaKopuruak();

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(33.33);
            cc.setHgrow(Priority.ALWAYS);
            grid.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < kategoriak.size(); i++) {
            Kategoria k = kategoriak.get(i);
            int kop = 0;
            for (KategoriaKopurua kk : kopuruak) {
                if (k.getIzena().equals(kk.getKategoriaIzena())) {
                    kop = kk.getKopurua();
                    break;
                }
            }
            grid.add(sortuTxartela(k, kop), i % 3, i / 3);
        }

        vboxKategoriak.getChildren().add(grid);
    }

    /**
     * Kategoria baten txartela sortzen du, editatu eta ezabatu botoiekin.
     *
     * @param k       Erakutsi beharreko kategoria
     * @param kopurua Kategoria horretan dauden artikulu kopurua
     * @return Txartelaren HBox nodoa
     */
    private HBox sortuTxartela(Kategoria k, int kopurua) {
        VBox info = new VBox(4);
        info.setMinWidth(80);
        Label lblIzena = new Label(k.getIzena());
        lblIzena.getStyleClass().addAll("font-bold", "text-dark");
        lblIzena.setWrapText(true);
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
        btnEdita.setMinWidth(javafx.scene.control.Control.USE_PREF_SIZE);
        btnEdita.setOnAction(e -> editatuKategoria(k));

        Button btnEzabatu = new Button("Ezabatu");
        btnEzabatu.getStyleClass().add("btn-danger");
        btnEzabatu.setStyle("-fx-padding: 4 12; -fx-font-size: 11px;");
        btnEzabatu.setMinWidth(javafx.scene.control.Control.USE_PREF_SIZE);
        btnEzabatu.setOnAction(e -> ezabatuKategoria(k, kopurua));

        HBox botoiak = new HBox(8, btnEdita, btnEzabatu);
        botoiak.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        HBox txartela = new HBox(15);
        txartela.getStyleClass().add("card");
        txartela.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        txartela.setPadding(new Insets(15));
        txartela.setMaxWidth(Double.MAX_VALUE);
        txartela.getChildren().addAll(info, botoiak);

        return txartela;
    }

    /**
     * Kategoria bat editatzeko formularioa contentArea-n kargatzen du.
     *
     * @param k Editatu beharreko kategoria
     */
    private void editatuKategoria(Kategoria k) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KategoriaEditatu.fxml"));
            Node nodoa = loader.load();
            KategoriaEditatuController ctrl = loader.getController();
            ctrl.setKategoria(k);
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "editatuKategoria: FXML kargatzean errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da editatzeko formularioa kargatu.", false);
        }
    }

    /**
     * Kategoria bat datu-basetik ezabatzen du, artikulurik ez badu.
     *
     * @param k       Ezabatu beharreko kategoria
     * @param kopurua Kategoria horretan dauden artikulu kopurua
     */
    private void ezabatuKategoria(Kategoria k, int kopurua) {
        if (kopurua > 0) {
            UIKudeatzailea.erakutsiToast("Ezin da ezabatu: kategoriak " + kopurua + " artikulu ditu.", false);
            return;
        }
        KategoriaDAO.ezabatu(k.getKategoriaId());
        kargatu();
        UIKudeatzailea.erakutsiToast("Kategoria ezabatu da.", true);
    }

    /**
     * Kategoria berri bat gehitzeko formularioa contentArea-n kargatzen du.
     */
    @FXML
    public void kategoriaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/KategoriaBerria.fxml"));
            Node nodoa = loader.load();
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "kategoriaBerria: FXML kargatzean errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da formularioa kargatu.", false);
        }
    }
}
