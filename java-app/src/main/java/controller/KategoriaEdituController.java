package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.KategoriaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.Kategoria;
import utils.UIKudeatzailea;

/**
 * Kategoria bat editatzeko pantailaren kontroladorea.
 *
 * @author Yeray Garrido
 */
public class KategoriaEdituController implements Initializable {

    @FXML private TextField txtIzena;
    @FXML private Label lblErrorea;

    private Kategoria kategoria;

    /**
     * Kontroladorea hasieratzen du; ez du hasierako ekintza berezirik.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    /**
     * Editatu beharreko kategoria ezartzen du eta formularioa betetzen du.
     *
     * @param k Editatu beharreko kategoria
     */
    public void setKategoria(Kategoria k) {
        this.kategoria = k;
        txtIzena.setText(k.getIzena());
    }

    /**
     * Formularioko izena egiaztatzen du eta kategoria datu-basean eguneratzen du.
     */
    @FXML
    private void eguneratu() {
        String izena = txtIzena.getText().trim();
        if (izena.isEmpty()) {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Kategoriaren izena ezin da hutsik egon.");
            return;
        }
        boolean ondo = KategoriaDAO.aldatuIzena(kategoria.getKategoriaId(), izena);
        if (ondo) {
            UIKudeatzailea.erakutsiToast("Kategoria eguneratu da.", true);
            itxi();
        } else {
            UIKudeatzailea.erakutsiFormularioErrorea(lblErrorea, "Errorea datu-basean eguneratzean.");
        }
    }

    /** Aldaketak gorde gabe zerrendara itzultzen du. */
    @FXML
    private void utzi() {
        itxi();
    }

    /** Kategorien zerrendara itzultzen da formularioa itxiz. */
    private void itxi() {
        UIKudeatzailea.kargatuPanela("/view/Kategoriak.fxml");
    }
}
