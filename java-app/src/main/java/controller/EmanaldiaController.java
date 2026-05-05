package controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import dao.EmanaldiaDAO;
import dao.ErreklamazioaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import model.Artikulua;
import model.EgoeraArtikulua;
import model.Erreklamazioa;
import utils.LogKudeatzailea;
import utils.Sesio;
import utils.UIKudeatzailea;

/**
 * Emanaldien formularioa kudeatzen duen kontroladorea.
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

    @FXML
    private TextField txtNan;
    @FXML
    private TextField txtIzena;
    @FXML
    private TextField txtAbizena;
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
    private StackPane contentArea = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artikuluak = new ArrayList<Artikulua>();
        beteteArtikuluCombo();
        ezkutuArtikuluInfo();
        ezkutuErrorea();
        archivoSinadura = null;
    }

    /**
     * Erreklamazioa baten datuak formularioan aurrez betetzen ditu eta
     * jabea identifikatzen du, emanaldian erreklamazioa zuzenean ebazteko.
     *
     * @param err       Ebazteko erreklamazioa
     * @param contentArea Itzultzean erabili beharreko StackPane nagusia
     */
    public void setErreklamazioa(Erreklamazioa err, StackPane contentArea) {
        this.erreklamazioId = err.getErreklamazioId();
        this.contentArea = contentArea;
        txtNan.setText(err.getJabeNan() != null ? err.getJabeNan() : "");
        txtIzena.setText(err.getJabeIzena() != null ? err.getJabeIzena() : "");
        txtAbizena.setText(err.getJabeAbizena() != null ? err.getJabeAbizena() : "");
        txtTelefonoa.setText(err.getJabeTelefonoa() != null ? err.getJabeTelefonoa() : "");
        txtEmaila.setText(err.getJabeEmaila() != null ? err.getJabeEmaila() : "");
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
     * Sinadura-dokumentua aukeratzeko fitxategi-hautatzailea irekitzen du.
     */
    @FXML
    private void hautaketaSinadura() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Sinadura dokumentua hautatu");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Dokumentuak (PDF, JPG, PNG)", "*.pdf", "*.jpg", "*.jpeg", "*.png")
        );
        File f = fc.showOpenDialog(cbArtikulua.getScene().getWindow());
        if (f != null) {
            archivoSinadura = f;
            if (lblArchivoSinadura != null) {
                lblArchivoSinadura.setText(f.getName());
            }
        }
    }

    /**
     * ComboBox-a biltegiko artikuluekin betetzen du.
     */
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
     * ComboBox-ean artikulua hautatzean xehetasunak erakusten ditu.
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

        String kok = "—";
        if (sel.getKokalekua() != null) {
            kok = sel.getKokalekua().getKokalekuOsoa();
        }
        lblArtikuluKokalekua.setText(kok);

        String data = "—";
        if (sel.getSarreraData() != null) {
            data = SDF.format(sel.getSarreraData());
        }
        lblArtikuluSarrera.setText(data);
        lblArtikuluEgoera.setText("Biltegian");
        erakutsiArtikuluInfo();
        ezkutuErrorea();
    }

    /**
     * Formularioa egiaztatzen du eta emanaldia datu-basean gordetzen du.
     */
    @FXML
    private void formalizatu() {
        int idx = cbArtikulua.getSelectionModel().getSelectedIndex();
        if (idx < 0 || idx >= artikuluak.size()) {
            erakutsiErrorea("Artikulu bat hautatu behar da.");
            return;
        }

        String nan = txtNan.getText().trim();
        String izena = txtIzena.getText().trim();
        String abizena = txtAbizena.getText().trim();

        if (nan.isEmpty() || izena.isEmpty() || abizena.isEmpty()) {
            erakutsiErrorea("(*) eremuak bete behar dira: NAN, Izena eta Abizena.");
            return;
        }

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

        String idArtikulua = artikuluak.get(idx).getArtikuluKodea();
        String dokumentuBidea = archivoSinadura != null ? archivoSinadura.getAbsolutePath() : null;
        boolean ok = EmanaldiaDAO.formalizatu(idArtikulua, nan, izena, abizena,
                telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea);
        if (ok) {
            if (erreklamazioId > 0) {
                ErreklamazioaDAO.updateEgoera(String.valueOf(erreklamazioId), "ebatzita");
                UIKudeatzailea.kargatuPanela(contentArea, "/view/Erreklamazioak.fxml");
            } else {
                garbitu();
                erakutsiErrorea("Emanaldia ondo formalizatu da.");
            }
        } else {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    /**
     * Aldaketak gorde gabe atzera egiten du: erreklamaziotik etorri bada
     * erreklamazioen zerrendara itzultzen da, bestela formularioa garbitzen du.
     */
    @FXML
    private void utzi() {
        if (contentArea != null) {
            UIKudeatzailea.kargatuPanela(contentArea, "/view/Erreklamazioak.fxml");
        } else {
            garbitu();
        }
    }

    /**
     * Formularioko eremu guztiak hasierako egoerara itzultzen ditu.
     */
    private void garbitu() {
        beteteArtikuluCombo();
        cbArtikulua.setValue(null);
        ezkutuArtikuluInfo();
        txtNan.clear();
        txtIzena.clear();
        txtAbizena.clear();
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
}
