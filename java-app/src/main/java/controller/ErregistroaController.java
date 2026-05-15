package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import dao.AurkitzaileaDAO;
import dao.KategoriaDAO;
import dao.KokalekuaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.Kategoria;
import model.Kokalekua;
import utils.AppConfig;
import utils.LogKudeatzailea;
import utils.XMLExportazioa;

/**
 * Artikulu berri bat erregistratzeko formularioaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class ErregistroaController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(ErregistroaController.class);

    // Artikuluaren eremuak
    @FXML
    private TextField txtIzena;
    @FXML
    private ComboBox<Kategoria> cbKategoria;
    @FXML
    private TextArea txtDeskribapena;
    @FXML
    private TextField txtAurkipenLekua;
    @FXML
    private DatePicker dpSarreraData;
    @FXML
    private CheckBox chkIragankorra;
    @FXML
    private ComboBox<Kokalekua> cbKokalekua;

    // Aurkitzailearen eremuak
    @FXML
    private TextField txtAurkIzena;
    @FXML
    private TextField txtAurkAbizena;
    @FXML
    private TextField txtAurkTelefonoa;
    @FXML
    private TextField txtAurkEmaila;

    // Argazkia
    @FXML
    private Label lblArgazkiIzena;

    @FXML
    private Label lblErrorea;

    private File argazkiFile;

    /**
     * Kontroladorea hasieratzen du: kategoria eta kokaleku konboboxak betetzen
     * ditu eta sarrera-data gaurko datarekin ezartzen du.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbKategoria.getItems().setAll(KategoriaDAO.getGuztiak());
        cbKokalekua.getItems().setAll(KokalekuaDAO.getZerrenda());

        dpSarreraData.setValue(LocalDate.now());
        ezkutuErrorea();
    }

    /**
     * Argazkia aukeratzeko fitxategi-hautatzailea irekitzen du.
     */
    @FXML
    private void hautatuArgazkia() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Argazkia hautatu");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Irudiak (JPG, PNG, WEBP)", "*.jpg", "*.jpeg", "*.png", "*.webp"));
        File f = fc.showOpenDialog(txtIzena.getScene().getWindow());
        if (f != null) {
            argazkiFile = f;
            if (lblArgazkiIzena != null) {
                lblArgazkiIzena.setText(f.getName());
            }
        }
    }

    /**
     * Formularioko datuak egiaztatzen ditu eta artikulua datu-basean gordetzen
     * du.
     */
    @FXML
    private void erregistratu() {
        String izena = txtIzena.getText().trim();
        Kategoria kategoria = cbKategoria.getValue();
        String deskribapena = txtDeskribapena.getText().trim();

        if (izena.isEmpty() || kategoria == null || deskribapena.isEmpty()) {
            erakutsiErrorea("(*) eremuak bete behar dira: Izena, Kategoria eta Deskribapena.");
            return;
        }

        String aurkIzena = "";
        if (txtAurkIzena != null) {
            aurkIzena = txtAurkIzena.getText().trim();
        }
        String aurkAbizena = "";
        if (txtAurkAbizena != null) {
            aurkAbizena = txtAurkAbizena.getText().trim();
        }
        if (aurkIzena.isEmpty() || aurkAbizena.isEmpty()) {
            erakutsiErrorea("Aurkitzailearen izena eta abizena bete behar dira.");
            return;
        }
        String aurkTelefonoa = "";
        if (txtAurkTelefonoa != null) {
            aurkTelefonoa = txtAurkTelefonoa.getText().trim();
        }
        String aurkEmaila = "";
        if (txtAurkEmaila != null) {
            aurkEmaila = txtAurkEmaila.getText().trim();
        }
        String aurkipenLekua = "";
        if (txtAurkipenLekua != null) {
            aurkipenLekua = txtAurkipenLekua.getText().trim();
        }

        LocalDate dataLoc = dpSarreraData.getValue();
        if (dataLoc == null) {
            dataLoc = LocalDate.now();
        }
        java.sql.Date sarreraData = java.sql.Date.valueOf(dataLoc);

        int idKat = kategoria.getKategoriaId();
        int idKok = 0;
        Kokalekua kokalekua = cbKokalekua.getValue();
        if (kokalekua != null) {
            idKok = kokalekua.getKokalekuId();
        }

        boolean iragankorra = chkIragankorra != null && chkIragankorra.isSelected();

        // Argazkia: kopiatu partekatutako_datuak/irudiak/ karpetara
        String argazkiBidea = null;
        if (argazkiFile != null) {
            argazkiBidea = kopiatuArgazkia(argazkiFile);
        }

        String kodea = ArtikuluaDAO.gehitu(izena, deskribapena, iragankorra, idKat, idKok, sarreraData, argazkiBidea);
        if (kodea == null) {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
            return;
        }

        // Aurkitzailea gorde
        boolean aurkOk = AurkitzaileaDAO.gehitu(kodea, aurkIzena, aurkAbizena, aurkTelefonoa, aurkEmaila,
                aurkipenLekua);
        if (!aurkOk) {
            LOG.log(Level.WARNING, "Aurkitzailearen datuak ezin izan dira gorde: " + kodea);
        }

        // Iraungitze-egiaztapena (sarrera-data zaharra bada berehala markatu)
        ArtikuluaDAO.iraungituakEguneratu();

        // XML eguneratu
        XMLExportazioa.exportatu();

        garbitu();
        erakutsiErrorea("Artikulua ondo erregistratu da: " + kodea);
    }

    /**
     * Argazki fitxategia artikulu_irudiak/ karpetara kopiatzen du. DB-n
     * fitxategi-izena soilik gordetzen da (bidea gabe).
     *
     * @return Fitxategi-izena (adib. img_1234567.jpg) edo null errorea bada
     */
    private String kopiatuArgazkia(File origen) {
        try {
            File irudiDir = new File(AppConfig.getArtikuluIrudiakBidea());
            if (!irudiDir.exists()) {
                irudiDir.mkdirs();
            }
            String ext = "";
            String nombre = origen.getName();
            int dot = nombre.lastIndexOf('.');
            if (dot >= 0) {
                ext = nombre.substring(dot).toLowerCase();
            }
            String izena = "img_" + System.currentTimeMillis() + ext;
            File destino = new File(irudiDir, izena);
            Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return izena;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "kopiatuArgazkia: irudia kopiatzeko errorea", e);
            return null;
        }
    }

    @FXML
    private void utzi() {
        garbitu();
    }

    private void garbitu() {
        txtIzena.clear();
        cbKategoria.setValue(null);
        txtDeskribapena.clear();
        if (txtAurkipenLekua != null) {
            txtAurkipenLekua.clear();
        }
        dpSarreraData.setValue(LocalDate.now());
        if (chkIragankorra != null) {
            chkIragankorra.setSelected(false);
        }
        cbKokalekua.setValue(null);
        if (txtAurkIzena != null) {
            txtAurkIzena.clear();
        }
        if (txtAurkAbizena != null) {
            txtAurkAbizena.clear();
        }
        if (txtAurkTelefonoa != null) {
            txtAurkTelefonoa.clear();
        }
        if (txtAurkEmaila != null) {
            txtAurkEmaila.clear();
        }
        argazkiFile = null;
        if (lblArgazkiIzena != null) {
            lblArgazkiIzena.setText("Ez da irudirik hautatu.");
        }
        ezkutuErrorea();
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
