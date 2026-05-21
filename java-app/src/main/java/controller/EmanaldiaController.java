package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import dao.EmanaldiaDAO;
import dao.ErreklamazioaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.Artikulua;
import model.EgoeraArtikulua;
import model.Erreklamazioa;
import utils.AppConfig;
import utils.LogKudeatzailea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Emanaldien formularioa kudeatzen duen kontroladorea. Pertsona (NAN) zein
 * erakundea (IFZ) onartzen ditu.
 *
 * @author Yeray Garrido
 */
public class EmanaldiaController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(EmanaldiaController.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    private ComboBox<String> cbArtikulua;
    @FXML
    private Label lblArtikuluKodea;
    @FXML
    private Label lblArtikuluIzena;
    @FXML
    private Label lblArtikuluKokalekua;
    @FXML
    private Label lblArtikuluSarrera;
    @FXML
    private Label lblArtikuluEgoera;

    // Toggle
    @FXML
    private RadioButton rbPertsona;
    @FXML
    private RadioButton rbErakundea;
    @FXML
    private VBox boxPertsona;
    @FXML
    private VBox boxErakundea;

    // Pertsona eremuak
    @FXML
    private TextField txtNan;
    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtAbizena;

    // Erakundea eremuak
    @FXML
    private TextField txtIft;
    @FXML
    private TextField txtIzenOfiziala;

    // Kontaktua (biak)
    @FXML
    private TextField txtTelefonoa;
    @FXML
    private TextField txtEmaila;
    @FXML
    private TextField txtHelbidea;
    @FXML
    private TextArea txtOharrak;

    @FXML
    private CheckBox chkNortasuna;
    @FXML
    private CheckBox chkSinadura;
    @FXML
    private Label lblErrorea;
    @FXML
    private Label lblArchivoSinadura;

    private ArrayList<Artikulua> artikuluak;
    private File archivoSinadura;
    private int erreklamazioId = -1;
    private String atzeraFxmlPath = null;

    /**
     * Kontroladorea hasieratzen du: artikulu konboxa betetzen du eta
     * eremu guztiak hasierako egoeran uzten ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artikuluak = new ArrayList<>();
        beteteArtikuluCombo();
        ezkutuArtikuluInfo();
        ezkutuErrorea();
        archivoSinadura = null;
    }

    /**
     * RadioButton-en arabera pertsona edo erakundearen eremuak
     * erakutsi/ezkutatu.
     */
    @FXML
    private void aldatuHartzaileMota() {
        boolean erakundea = rbErakundea != null && rbErakundea.isSelected();
        if (boxPertsona != null) {
            boxPertsona.setVisible(!erakundea);
            boxPertsona.setManaged(!erakundea);
        }
        if (boxErakundea != null) {
            boxErakundea.setVisible(erakundea);
            boxErakundea.setManaged(erakundea);
        }
    }

    /**
     * Erreklamazioaren datuak formularioan aurre-betetzen ditu emanaldia
     * egiteko prest.
     *
     * @param err Datuak hartu beharreko erreklamazioa
     */
    public void setErreklamazioa(Erreklamazioa err) {
        this.erreklamazioId = err.getErreklamazioId();
        if (rbPertsona != null) {
            rbPertsona.setSelected(true);
        }
        aldatuHartzaileMota();
        if (err.getJabeNan() != null) {
            txtNan.setText(err.getJabeNan());
        } else {
            txtNan.setText("");
        }
        if (err.getJabeIzena() != null) {
            txtIzena.setText(err.getJabeIzena());
        } else {
            txtIzena.setText("");
        }
        if (err.getJabeAbizena() != null) {
            txtAbizena.setText(err.getJabeAbizena());
        } else {
            txtAbizena.setText("");
        }
        if (err.getJabeTelefonoa() != null) {
            txtTelefonoa.setText(err.getJabeTelefonoa());
        } else {
            txtTelefonoa.setText("");
        }
        if (err.getJabeEmaila() != null) {
            txtEmaila.setText(err.getJabeEmaila());
        } else {
            txtEmaila.setText("");
        }
        List<Artikulua> bateragarriak = err.bilatuBateragarriak(artikuluak);
        if (!bateragarriak.isEmpty()) {
            int idx = artikuluak.indexOf(bateragarriak.get(0));
            if (idx >= 0) {
                cbArtikulua.getSelectionModel().select(idx);
                artikuluaHautatu();
            }
        }
    }

    /**
     * Iraungitako artikuluaren emanaldia formularioan aurre-betetzen du,
     * aurkitzailearen datuak hartuta pertsona edo erakundea moduan.
     *
     * @param artikulua     Iraungitako artikulua
     * @param erakundeaDa   true bada erakundea modua aktibatzen du
     * @param aurkIzena     Aurkitzailearen izena (pertsona moduan)
     * @param aurkAbizena   Aurkitzailearen abizena (pertsona moduan)
     * @param aurkTelefonoa Aurkitzailearen telefonoa
     * @param aurkEmaila    Aurkitzailearen emaila
     */
    public void setIraungitakoa(Artikulua artikulua, boolean erakundeaDa,
            String aurkIzena, String aurkAbizena,
            String aurkTelefonoa, String aurkEmaila) {
        this.atzeraFxmlPath = "/view/Iraungitakoak.fxml";
        cbArtikulua.getItems().clear();
        artikuluak.clear();
        artikuluak.add(artikulua);
        cbArtikulua.getItems().add(artikulua.getArtikuluKodea() + " – " + artikulua.getIzenburua());
        cbArtikulua.getSelectionModel().selectFirst();
        artikuluaHautatu();
        lblArtikuluEgoera.setText("Iraungita");
        cbArtikulua.setDisable(true);

        if (erakundeaDa) {
            if (rbErakundea != null) {
                rbErakundea.setSelected(true);
            }
        } else {
            if (rbPertsona != null) {
                rbPertsona.setSelected(true);
            }
            if (aurkIzena != null && txtIzena != null) {
                txtIzena.setText(aurkIzena);
            }
            if (aurkAbizena != null && txtAbizena != null) {
                txtAbizena.setText(aurkAbizena);
            }
            if (aurkTelefonoa != null && txtTelefonoa != null) {
                txtTelefonoa.setText(aurkTelefonoa);
            }
            if (aurkEmaila != null && txtEmaila != null) {
                txtEmaila.setText(aurkEmaila);
            }
        }
        aldatuHartzaileMota();
    }

    /**
     * Fitxategi-hautatzailea irekitzen du sinadura-dokumentua aukeratzeko.
     */
    @FXML
    private void hautaketaSinadura() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sinadura dokumentua hautatu");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Dokumentuak (PDF, JPG, PNG)", "*.pdf", "*.jpg", "*.jpeg", "*.png"));
        File f = fc.showOpenDialog(cbArtikulua.getScene().getWindow());
        if (f != null) {
            archivoSinadura = f;
            if (lblArchivoSinadura != null) { // Check for null before accessing
                lblArchivoSinadura.setText(f.getName());
            }
            if (chkSinadura != null) { // Check for null before accessing
                chkSinadura.setSelected(true);
            }
        } else {
            if (chkSinadura != null) { // If no file selected, uncheck
                chkSinadura.setSelected(false);
            }
        }
    }

    private void beteteArtikuluCombo() {
        cbArtikulua.getItems().clear();
        artikuluak.clear();
        List<Artikulua> guztiak = ArtikuluaDAO.getGuztiak();
        for (Artikulua a : guztiak) {
            if (a.getEgoera() == EgoeraArtikulua.BILTEGIAN) {
                artikuluak.add(a);
                cbArtikulua.getItems().add(a.getArtikuluKodea() + " – " + a.getIzenburua());
            }
        }
    }

    /**
     * ComboBox-eko hautaketa aldatzean artikuluaren informazioa freskatzen du.
     */
    @FXML
    private void artikuluaHautatu() {
        int idx = cbArtikulua.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= artikuluak.size()) {
            ezkutuArtikuluInfo();
            return;
        }
        Artikulua sel = artikuluak.get(idx);
        lblArtikuluKodea.setText(sel.getArtikuluKodea());
        lblArtikuluIzena.setText(sel.getIzenburua());
        String kok;
        if (sel.getKokalekua() != null) {
            kok = sel.getKokalekua().getKokalekuOsoa();
        } else {
            kok = "—";
        }
        lblArtikuluKokalekua.setText(kok);
        String data;
        if (sel.getSarreraData() != null) {
            data = SDF.format(sel.getSarreraData());
        } else {
            data = "—";
        }
        lblArtikuluSarrera.setText(data);
        lblArtikuluEgoera.setText("Biltegian");
        erakutsiArtikuluInfo();
        ezkutuErrorea();
    }

    @FXML
    private void formalizatu() {
        int idx = cbArtikulua.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= artikuluak.size()) {
            erakutsiErrorea("Artikulu bat hautatu behar da.");
            return;
        }
        String idArtikulua = artikuluak.get(idx).getArtikuluKodea();
        String telefonoa = txtTelefonoa.getText().trim();
        String emaila = txtEmaila.getText().trim();
        String helbidea = txtHelbidea.getText().trim();
        String oharrak = "";
        if (txtOharrak != null) {
            oharrak = txtOharrak.getText().trim();
        }
        int idLangile = 0;
        if (Sesio.getLangilea() != null) {
            idLangile = Sesio.getLangilea().getLangileId();
        }

        if (archivoSinadura == null) {
            erakutsiErrorea("Sinadura fitxategia derrigorrezkoa da. Hautatu dokumentu bat.");
            return;
        }

        String dokumentuBidea = null;
        String kopia = kopiatuSinadura(archivoSinadura);
        if (kopia != null) {
            dokumentuBidea = kopia;
        }

        boolean erakundeaDa = rbErakundea != null && rbErakundea.isSelected();
        boolean ok;

        if (erakundeaDa) {
            String ift = txtIft.getText().trim();
            String izenOfiziala = txtIzenOfiziala.getText().trim();
            if (ift.isEmpty() || izenOfiziala.isEmpty()) {
                erakutsiErrorea("(*) IFZ eta izen ofiziala bete behar dira.");
                return;
            }
            ok = EmanaldiaDAO.formalizatuErakundea(idArtikulua, ift, izenOfiziala,
                    telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea);
        } else {
            String nan = txtNan.getText().trim();
            String izena = txtIzena.getText().trim();
            String abizena = txtAbizena.getText().trim();
            if (nan.isEmpty() || izena.isEmpty() || abizena.isEmpty()) {
                erakutsiErrorea("(*) eremuak bete behar dira: NAN, Izena eta Abizena.");
                return;
            }
            ok = EmanaldiaDAO.formalizatu(idArtikulua, nan, izena, abizena,
                    telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea);
        }

        if (ok) {
            if (erreklamazioId > 0) {
                ErreklamazioaDAO.updateEgoera(String.valueOf(erreklamazioId), "ebatzita");
                UIKudeatzailea.kargatuPanela("/view/Erreklamazioak.fxml");
            } else if (atzeraFxmlPath != null) { // If coming from Iraungitakoak
                UIKudeatzailea.kargatuPanela(atzeraFxmlPath);
            } else {
                garbitu();
                erakutsiErrorea("Emanaldia ondo formalizatu da.");
            }
        } else {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    @FXML
    private void utzi() {
        if (atzeraFxmlPath != null || erreklamazioId > 0) { // If coming from Erreklamazioak or Iraungitakoak
            String dest;
            if (atzeraFxmlPath != null) {
                dest = atzeraFxmlPath;
            } else {
                dest = "/view/Erreklamazioak.fxml";
            }
            UIKudeatzailea.kargatuPanela(dest);
        } else {
            garbitu();
        }
    }

    private void garbitu() {
        beteteArtikuluCombo();
        cbArtikulua.setValue(null);
        ezkutuArtikuluInfo();
        if (rbPertsona != null) {
            rbPertsona.setSelected(true);
        }
        aldatuHartzaileMota();
        txtNan.clear();
        txtIzena.clear();
        txtAbizena.clear();
        if (txtIft != null) {
            txtIft.clear();
        }
        if (txtIzenOfiziala != null) {
            txtIzenOfiziala.clear();
        }
        txtTelefonoa.clear();
        txtEmaila.clear();
        txtHelbidea.clear();
        if (txtOharrak != null) {
            txtOharrak.clear();
        }
        if (chkNortasuna != null) {
            chkNortasuna.setSelected(false);
        }
        if (chkSinadura != null) {
            chkSinadura.setSelected(false);
        }
        archivoSinadura = null;
        if (lblArchivoSinadura != null) {
            lblArchivoSinadura.setText("Ez da fitxategirik hautatu.");
        }
        ezkutuErrorea();
    }

    private void erakutsiArtikuluInfo() {
        lblArtikuluKodea.setVisible(true);
        lblArtikuluKodea.setManaged(true);
        lblArtikuluIzena.setVisible(true);
        lblArtikuluIzena.setManaged(true);
        lblArtikuluKokalekua.setVisible(true);
        lblArtikuluKokalekua.setManaged(true);
        lblArtikuluSarrera.setVisible(true);
        lblArtikuluSarrera.setManaged(true);
        lblArtikuluEgoera.setVisible(true);
        lblArtikuluEgoera.setManaged(true);
    }

    private void ezkutuArtikuluInfo() {
        lblArtikuluKodea.setVisible(false);
        lblArtikuluKodea.setManaged(false);
        lblArtikuluIzena.setVisible(false);
        lblArtikuluIzena.setManaged(false);
        lblArtikuluKokalekua.setVisible(false);
        lblArtikuluKokalekua.setManaged(false);
        lblArtikuluSarrera.setVisible(false);
        lblArtikuluSarrera.setManaged(false);
        lblArtikuluEgoera.setVisible(false);
        lblArtikuluEgoera.setManaged(false);
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

    /**
     * Sinadura fitxategia 'sinadurak/' karpetara kopiatzen du. DB-n
     * fitxategi-izena soilik gordetzen da (bidea gabe).
     *
     * @param origen Jatorrizko fitxategia
     * @return Fitxategi-izena (adib. sinadura_1234567.pdf) edo null errorea
     *         bada
     */
    private String kopiatuSinadura(File origen) {
        try {
            File sinaduraDir = new File(AppConfig.getSinaduraBidea());
            if (!sinaduraDir.exists()) {
                sinaduraDir.mkdirs();
            }
            String nombre = origen.getName();
            int dot = nombre.lastIndexOf('.');
            String ext;
            if (dot >= 0) {
                ext = nombre.substring(dot).toLowerCase();
            } else {
                ext = "";
            }
            String izena = "sinadura_" + System.currentTimeMillis() + ext;
            File destino = new File(sinaduraDir, izena);
            Files.copy(origen.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return izena;
        } catch (IOException e) {
            LOG.log(Level.WARNING, "kopiatuSinadura: sinadura kopiatzeko errorea", e);
            UIKudeatzailea.erakutsiToast("Ezin izan da sinadura dokumentua gorde.", false);
            return null;
        }
    }
}
