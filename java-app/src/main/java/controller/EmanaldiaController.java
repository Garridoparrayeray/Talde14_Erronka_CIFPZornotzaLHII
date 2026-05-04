package controller;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import dao.ArtikuluaDAO;
import dao.EmanaldiaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.Artikulua;
import model.EgoeraArtikulua;
import utils.Sesio;

/**
 * Emanaldien formularioa kudeatzen duen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class EmanaldiaController implements Initializable {

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

    // Biltegiko artikuluen zerrenda (ComboBox-arekin parekatua)
    private ArrayList<Artikulua> artikuluak;
    private File archivoSinadura;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        artikuluak = new ArrayList<Artikulua>();
        beteteArtikuluCombo();
        ezkutuArtikuluInfo();
        ezkutuErrorea();
        archivoSinadura = null;
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
            garbitu();
            erakutsiErrorea("Emanaldia ondo formalizatu da.");
        } else {
            erakutsiErrorea("Errorea gordetzean. Egiaztatu datuak.");
        }
    }

    /**
     * Aldaketak gorde gabe formularioa garbitzen du.
     */
    @FXML
    private void utzi() {
        garbitu();
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

    /**
     * Hautaturiko artikuluaren informazio-etiketa guztiak erakusten ditu.
     */
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

    /**
     * Artikuluaren informazio-etiketa guztiak ezkutatzen ditu.
     */
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
