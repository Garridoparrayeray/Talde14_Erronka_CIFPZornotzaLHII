package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.BackupDAO;
import dao.EstadistikaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import utils.AppConfig;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Administrazio-paneleko estatistikak, konexio-egoera eta babes-kopiak
 * kudeatzen dituen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class AdminPanelaController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(AdminPanelaController.class);

    @FXML
    private Label lblLangileak;
    @FXML
    private Label lblArtikuluak;
    @FXML
    private Label lblKategoriak;
    @FXML
    private Label lblKokalekuak;
    @FXML
    private Label lblDbEgoera;
    @FXML
    private Label lblAzkenKopia;
    @FXML
    private Label lblIzena;

    private static final String KOPIA_FITXATEGIA = "azken_kopia.txt";

    private AdminController adminController;

    /**
     * AdminController erreferentzia ezartzen du, bista-aldaketa eskaerak gune
     * nagusiari bidaltzeko.
     *
     * @param adminController Nagusiko AdminController instantzia
     */
    public void setAdminController(AdminController adminController) {
        this.adminController = adminController;
    }

    /**
     * Kontroladorea hasieratzen du: estatistika-etiketak, DB konexio-egoera eta
     * azken babes-kopiaren data erakusten ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Sesio.getLangilea() != null && lblIzena != null) {
            lblIzena.setText(Sesio.getLangilea().getIzena() + " " + Sesio.getLangilea().getAbizena());
        }

        lblLangileak.setText(String.valueOf(EstadistikaDAO.langileKopurua()));
        lblArtikuluak.setText(String.valueOf(EstadistikaDAO.artikuluGuztienKopurua()));
        lblKategoriak.setText(String.valueOf(EstadistikaDAO.kategoriaKopurua()));
        lblKokalekuak.setText(String.valueOf(EstadistikaDAO.kokalekuakKopurua()));

        boolean konektatuta = DBKonexioa.egiaztatu();
        ezarriDbEgoera(konektatuta);

        lblAzkenKopia.setText(irakurriAzkenKopiaData());
    }

    /**
     * Datu-basearekin konexioa egiaztatzen du eta etiketa eguneratzen du.
     */
    @FXML
    public void egiaztatuKonexioa() {
        boolean ok = DBKonexioa.egiaztatu();
        ezarriDbEgoera(ok);
    }

    /**
     * Datu-basearen babes-kopia SQL fitxategi batean gordetzen du.
     */
    @FXML
    public void eginBabesKopia() {
        try {
            BackupDAO.eginBabesKopia();
            String data = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            gordaAzkenKopiaData(data);
            lblAzkenKopia.setText(data);
            lblAzkenKopia.getStyleClass().removeAll("badge-neutral", "text-danger", "text-success");
            lblAzkenKopia.getStyleClass().add("text-success");
            UIKudeatzailea.erakutsiToast("Babes-kopia ondo gorde da: backup_" + java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd")) + ".sql", true);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "eginBabesKopia: errorea", e);
            lblAzkenKopia.getStyleClass().removeAll("badge-neutral", "text-success");
            lblAzkenKopia.getStyleClass().add("text-danger");
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da babes-kopia egin.", false);
        }
    }

    private static String irakurriAzkenKopiaData() {
        File f = new File(AppConfig.getExportBidea(), KOPIA_FITXATEGIA);
        if (f.exists()) {
            try {
                String data = Files.readString(f.toPath(), StandardCharsets.UTF_8).trim();
                if (!data.isEmpty()) {
                    return data;
                }
            } catch (IOException e) {
                LOG.log(Level.WARNING, "irakurriAzkenKopiaData: errorea", e);
            }
        }
        return "Ez dago azken kopiarik";
    }

    private static void gordaAzkenKopiaData(String data) {
        try {
            File dir = new File(AppConfig.getExportBidea());
            dir.mkdirs();
            Files.writeString(new File(dir, KOPIA_FITXATEGIA).toPath(),
                    data, StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "gordaAzkenKopiaData: errorea", e);
        }
    }

    /**
     * Langile berri bat gehitzeko bista kargatzen du AdminController bidez.
     */
    @FXML
    private void LangileBerriaShorcut() {
        if (adminController != null) {
            adminController.loadLangileBerria();
        }
    }

    /**
     * Kategoria berri bat gehitzeko bista kargatzen du AdminController bidez.
     */
    @FXML
    private void KategoriaBerriaShorcut() {
        if (adminController != null) {
            adminController.loadKategoriaBerria();
        }
    }

    /**
     * DB konexio-egoeraren etiketa eguneratzen du kolore eta testuarekin.
     *
     * @param konektatuta Konektatuta badago true
     */
    private void ezarriDbEgoera(boolean konektatuta) {
        lblDbEgoera.getStyleClass().removeAll("text-success", "text-danger");
        if (konektatuta) {
            lblDbEgoera.setText("Konektatuta");
            lblDbEgoera.getStyleClass().add("text-success");
        } else {
            lblDbEgoera.setText("Errorea");
            lblDbEgoera.getStyleClass().add("text-danger");
        }
    }
}
