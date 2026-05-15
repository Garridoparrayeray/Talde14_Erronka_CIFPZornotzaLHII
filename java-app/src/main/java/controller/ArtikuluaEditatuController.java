package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import javafx.stage.FileChooser;
import model.Artikulua;
import model.Kategoria;
import model.Kokalekua;
import utils.AppConfig;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;
import utils.XMLExportazioa;

/**
 * Biltegiko artikulu baten datuak editatzeko formularioaren kontroladorea.
 * Artikuluaren izena, kategoria, kokalekua, deskribapena eta argazkia
 * eguneratzeko
 * aukera ematen du.
 *
 * @author Yeray Garrido
 */
public class ArtikuluaEditatuController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(ArtikuluaEditatuController.class);

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

    /**
     * Kontroladorea hasieratzen du: kategoria eta kokaleku ComboBox-ak betetzen
     * ditu eta errore-etiketa ezkutatzen du.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbKategoria.getItems().setAll(KategoriaDAO.getGuztiak());
        cbKokalekua.getItems().setAll(KokalekuaDAO.getZerrenda());
        UIKudeatzailea.ezkutuFormularioErrorea(lblErrorea);
    }

    /**
     * Artikuluaren datuak formularioan kargatzen ditu, erabiltzaileak editatzeko
     * prest utziz.
     *
     * @param a Editatu beharreko artikulua
     */
    public void kargatu(Artikulua a) {
        this.artikulua = a;

        lblTitulua.setText("Editatu: " + a.getArtikuluKodea());
        txtIzena.setText(a.getIzenburua());
        String deskribapena = a.getDeskribapena();
        if (deskribapena.equals("—")) {
            txtDeskribapena.setText("");
        } else {
            txtDeskribapena.setText(deskribapena);
        }

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

    /**
     * Fitxategi-hautagailu bat irekitzen du erabiltzaileak argazki bat
     * hautatu ahal izateko.
     */
    @FXML
    private void hautatuArgazkia() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Argazkia hautatu");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Irudiak (JPG, PNG)", "*.jpg", "*.jpeg", "*.png", "*.webp"));
        File f = fc.showOpenDialog(txtIzena.getScene().getWindow());
        if (f != null) {
            argazkiFile = f;
            lblArgazkiIzena.setText(f.getName());
        }
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta artikuluaren aldaketak datu-basean
     * gordetzen ditu. Argazki berria aukeratu bada, karpetara kopiatzen du.
     */
    @FXML
    private void gorde() {
        String izena = txtIzena.getText().trim();
        String deskribapena = txtDeskribapena.getText().trim();
        if (izena.isEmpty() || deskribapena.isEmpty()) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Izena eta deskribapena bete behar dira.");
            return;
        }

        int idKat = 0;
        if (cbKategoria.getValue() != null) {
            idKat = cbKategoria.getValue().getKategoriaId();
        }
        int idKok = 0;
        if (cbKokalekua.getValue() != null) {
            idKok = cbKokalekua.getValue().getKokalekuId();
        }

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
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Errorea gordetzean.");
        }
    }

    /**
     * Formularioa ixten du eta inbentario bistara itzultzen da.
     */
    @FXML
    private void itxi() {
        UIKudeatzailea.kargatuPanela("/view/Inbentario.fxml");
    }

    /**
     * Argazkia artikulu_irudiak/ karpetara kopiatzen du izen berri batekin.
     *
     * @param origen Kopiatu beharreko jatorrizko fitxategia
     * @return Kopiaren izen berria, edo null errorea izanez gero
     */
    private String kopiatuArgazkia(File origen) {
        try {
            File irudiDir = new File(AppConfig.getArtikuluIrudiakBidea());
            if (!irudiDir.exists()) {
                irudiDir.mkdirs();
            }
            String nombre = origen.getName();
            int dot = nombre.lastIndexOf('.');
            String ext;
            if (dot >= 0) {
                ext = nombre.substring(dot).toLowerCase();
            } else {
                ext = "";
            }
            String izena = "img_" + System.currentTimeMillis() + ext;
            Files.copy(origen.toPath(), new File(irudiDir, izena).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return izena;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "kopiatuArgazkia: errorea", e);
            return null;
        }
    }
}
