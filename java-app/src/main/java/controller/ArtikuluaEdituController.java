package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import dao.KategoriaDAO;
import dao.KokalekuaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Artikulua;
import model.Kategoria;
import model.Kokalekua;
import utils.AppConfig;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;
import utils.XMLExportazioa;

public class ArtikuluaEdituController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(ArtikuluaEdituController.class);

    @FXML
    private Label lblTitulua;
    @FXML
    private TextField txtIzena;
    @FXML
    private ComboBox<Kategoria> cbKategoria;
    @FXML
    private ComboBox<Kokalekua> cbKokalekua;
    @FXML
    private TextArea txtDeskribapena;
    @FXML
    private Label lblArgazkiIzena;
    @FXML
    private Label lblErrorea;

    private Artikulua artikulua;
    private File argazkiFile;
    private StackPane contentArea;

    /**
     * Itzultzean erabili beharreko StackPane ezartzen du.
     *
     * @param contentArea Formularioa kargatuta dagoen gunea
     */
    public void setContentArea(StackPane contentArea) {
        this.contentArea = contentArea;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbKategoria.getItems().setAll(new ArrayList<>(KategoriaDAO.getGuztiak()));
        cbKokalekua.getItems().setAll(new ArrayList<>(KokalekuaDAO.getZerrenda()));
        ezkutuErrorea();
    }

    public void kargatu(Artikulua a, StackPane contentArea) {
        this.artikulua = a;
        this.contentArea = contentArea;

        lblTitulua.setText("Editatu: " + a.getArtikuluKodea());
        txtIzena.setText(a.getIzenburua());
        txtDeskribapena.setText(a.getDeskribapenaSegurua().equals("—") ? "" : a.getDeskribapenaSegurua());

        if (a.getArgazkiBidea() != null && !a.getArgazkiBidea().isEmpty()) {
            lblArgazkiIzena.setText(a.getArgazkiBidea());
        }

        if (a.getKategoria() != null) {
            for (Kategoria k : cbKategoria.getItems()) {
                if (k.getKategoriaId() == a.getKategoria().getKategoriaId()) {
                    cbKategoria.setValue(k);
                    break;
                }
            }
        }

        if (a.getKokalekua() != null) {
            for (Kokalekua kok : cbKokalekua.getItems()) {
                if (kok.getKokalekuOsoa().equals(a.getKokalekua().getKokalekuOsoa())) {
                    cbKokalekua.setValue(kok);
                    break;
                }
            }
        }
    }

    @FXML
    private void hautatuArgazkia() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Argazkia hautatu");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Irudiak (JPG, PNG)", "*.jpg", "*.jpeg", "*.png", "*.webp")
        );
        File f = fc.showOpenDialog(txtIzena.getScene().getWindow());
        if (f != null) {
            argazkiFile = f;
            lblArgazkiIzena.setText(f.getName());
        }
    }

    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        String deskribapena = txtDeskribapena.getText().trim();
        if (izena.isEmpty() || deskribapena.isEmpty()) {
            erakutsiErrorea("Izena eta deskribapena bete behar dira.");
            return;
        }

        int idKat = cbKategoria.getValue() != null ? cbKategoria.getValue().getKategoriaId() : 0;
        int idKok = cbKokalekua.getValue() != null ? cbKokalekua.getValue().getKokalekuId() : 0;

        String argazkiBidea = artikulua.getArgazkiBidea();
        if (argazkiFile != null) {
            String kopia = kopiatuArgazkia(argazkiFile);
            if (kopia != null) {
                argazkiBidea = kopia;
            }
        }

        boolean ok = ArtikuluaDAO.eguneratu(artikulua.getArtikuluKodea(), izena, deskribapena,
                idKat, idKok, argazkiBidea);
        if (ok) {
            XMLExportazioa.exportatu();
            itxi();
        } else {
            erakutsiErrorea("Errorea gordetzean.");
        }
    }

    @FXML
    private void itxi() {
        if (contentArea != null) {
            UIKudeatzailea.kargatuPanela(contentArea, "/view/Inbentario.fxml");
        } else {
            ((Stage) txtIzena.getScene().getWindow()).close();
        }
    }

    private String kopiatuArgazkia(File origen) {
        try {
            File irudiDir = new File(AppConfig.getArtikuluIrudiakBidea());
            if (!irudiDir.exists()) {
                irudiDir.mkdirs();
            }
            String nombre = origen.getName();
            int dot = nombre.lastIndexOf('.');
            String ext = dot >= 0 ? nombre.substring(dot).toLowerCase() : "";
            String izena = "img_" + System.currentTimeMillis() + ext;
            Files.copy(origen.toPath(), new File(irudiDir, izena).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return izena;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "kopiatuArgazkia: errorea", e);
            return null;
        }
    }

    private void erakutsiErrorea(String mezua) {
        if (lblErrorea != null) {
            lblErrorea.setText(mezua);
            lblErrorea.setVisible(true);
            lblErrorea.setManaged(true);
        }
    }

    private void ezkutuErrorea() {
        if (lblErrorea != null) {
            lblErrorea.setVisible(false);
            lblErrorea.setManaged(false);
        }
    }
}
