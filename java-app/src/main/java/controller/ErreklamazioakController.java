package controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import dao.ErreklamazioaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Artikulua;
import model.Erreklamazioa;
import utils.LogKudeatzailea;
import utils.UIKudeatzailea;

/**
 * Erreklamazioen zerrendaren eta xehetasunen kontroladorea.
 *
 * @author Yeray Garrido
 */
public class ErreklamazioakController implements Initializable {

    private static final Logger LOG = LogKudeatzailea.lortu(ErreklamazioakController.class);

    @FXML
    private VBox listVBox;
    @FXML
    private Label lblKopurua;
    @FXML
    private Button btnFiltroIrekiak;
    @FXML
    private Button btnFiltroBaztertuak;
    @FXML
    private Button btnFiltroEgindak;

    @FXML
    private VBox panelXehetasuna;
    @FXML
    private Label lblIdErreklam;
    @FXML
    private Label lblEgoeraBadge;
    @FXML
    private Label lblJabeIzena;
    @FXML
    private Label lblKontaktua;
    @FXML
    private Label lblDeskribapenaTestua;
    @FXML
    private VBox vboxBateragarriak;
    @FXML
    private Button btnEbatzi;
    @FXML
    private Button btnBaztertu;
    @FXML
    private Button btnIrekiBerriz;

    private List<Erreklamazioa> erreklamazioak;
    private Erreklamazioa hautatua;
    private VBox itemHautatua;
    private String egoeraFiltro = "irekita";

    /**
     * Kontroladorea hasieratzen du eta erreklamazioak kargatzen ditu.
     *
     * @param url FXML fitxategiaren kokapena
     * @param rb  Erabilitako baliabide-sorta
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kargatu();
    }

    private void kargatu() {
        List<Erreklamazioa> guztiak = ErreklamazioaDAO.getGuztiak();
        erreklamazioak = new ArrayList<>();
        for (Erreklamazioa e : guztiak) {
            if (e.getEgoeraTestua().equals(egoeraFiltro)) {
                erreklamazioak.add(e);
            }
        }
        lblKopurua.setText(String.valueOf(erreklamazioak.size()));

        listVBox.getChildren().clear();
        itemHautatua = null;
        hautatua = null;

        int i = 0;
        for (Erreklamazioa err : erreklamazioak) {
            VBox item = sortuListaItem(err, i == 0);
            listVBox.getChildren().add(item);
            i++;
        }

        if (!erreklamazioak.isEmpty()) {
            VBox primerItem = (VBox) listVBox.getChildren().get(0);
            hautatu(erreklamazioak.get(0), primerItem);
        } else {
            panelHutsak();
        }
    }

    /**
     * Erreklamazio baten lista-elementua sortzen du.
     *
     * @param err     Erakutsi beharreko erreklamazioa
     * @param aktiboa Hasieratik aktibo erakustea nahi bada true
     * @return Lista-elementuaren VBox nodoa
     */
    private VBox sortuListaItem(Erreklamazioa err, boolean aktiboa) {
        Label lblId = new Label("E-" + err.getErreklamazioId());
        lblId.getStyleClass().add("text-muted");
        lblId.setStyle("-fx-font-size: 12px;");

        String egoeraStr = err.getEgoeraTestua();
        Label badge = new Label(egoeraBadgeTestua(egoeraStr));
        badge.getStyleClass().add(egoeraBadgeKlasea(egoeraStr));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox header = new HBox(lblId, spacer, badge);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        String izena = err.getJabeIzena();
        String abizena = err.getJabeAbizena();
        Label lblIzena = new Label(izena + " " + abizena);
        lblIzena.getStyleClass().addAll("font-bold", "text-dark");
        lblIzena.setStyle("-fx-font-size: 15px;");

        String deskribapena = err.getDeskribapenBilatua();
        if (deskribapena == null) {
            deskribapena = "";
        }
        Label lblDesk = new Label(deskribapena);
        lblDesk.getStyleClass().add("text-muted");
        lblDesk.setWrapText(true);

        String dataStr = "";
        if (err.getErreklamazioData() != null) {
            dataStr = err.getErreklamazioData().toString();
        }
        Label lblData = new Label("Jasoa: " + dataStr);
        lblData.getStyleClass().add("text-muted");
        lblData.setStyle("-fx-font-size: 12px;");
        VBox.setMargin(lblData, new Insets(4, 0, 0, 0));

        VBox item = new VBox(header, lblIzena, lblDesk, lblData);
        if (aktiboa) {
            item.getStyleClass().add("list-item-active");
        } else {
            item.getStyleClass().add("list-item");
        }

        item.setOnMouseClicked(e -> hautatu(err, item));
        return item;
    }

    /**
     * Erreklamazio bat hautatzen du eta xehetasunak eskuineko panelean
     * erakusten ditu.
     *
     * @param err  Hautatu beharreko erreklamazioa
     * @param item Aktibo markatu beharreko lista-elementua
     */
    private void hautatu(Erreklamazioa err, VBox item) {
        if (itemHautatua != null) {
            itemHautatua.getStyleClass().removeAll("list-item-active");
            itemHautatua.getStyleClass().add("list-item");
        }
        itemHautatua = item;
        item.getStyleClass().removeAll("list-item");
        item.getStyleClass().add("list-item-active");

        hautatua = err;

        lblIdErreklam.setText("E-" + err.getErreklamazioId());

        String egoera = err.getEgoeraTestua();
        lblEgoeraBadge.setText(egoeraBadgeTestua(egoera));
        lblEgoeraBadge.getStyleClass().setAll(egoeraBadgeKlasea(egoera));

        lblJabeIzena.setText(err.getJabeIzena() + " " + err.getJabeAbizena());
        lblKontaktua.setText(err.getJabeTelefonoa() + " · " + err.getJabeEmaila());

        String desk = err.getDeskribapenBilatua();
        if (desk != null) {
            lblDeskribapenaTestua.setText(desk);
        } else {
            lblDeskribapenaTestua.setText("");
        }

        erakutsiBateragarriak(err);

        boolean irekita = "irekita".equals(egoera);
        btnEbatzi.setVisible(irekita);
        btnEbatzi.setManaged(irekita);
        btnBaztertu.setVisible(irekita);
        btnBaztertu.setManaged(irekita);
        btnIrekiBerriz.setVisible(!irekita);
        btnIrekiBerriz.setManaged(!irekita);
    }

    /**
     * Erreklamazioaren deskribapena biltegiko artikuluekin alderatuz
     * bat-etortze posibleak erakusten ditu.
     *
     * @param err Bat-etortzeak bilatu beharreko erreklamazioa
     */
    private void erakutsiBateragarriak(Erreklamazioa err) {
        vboxBateragarriak.getChildren().clear();
        List<Artikulua> guztiak = ArtikuluaDAO.getGuztiak();
        List<Artikulua> bateragarriak = err.bilatuBateragarriak(guztiak);

        if (bateragarriak.isEmpty()) {
            Label lblHuts = new Label("Bat-etortze posiblerik ez.");
            lblHuts.getStyleClass().add("text-muted");
            vboxBateragarriak.getChildren().add(lblHuts);
            return;
        }

        for (Artikulua a : bateragarriak) {
            Label lblId = new Label(a.getArtikuluKodea());
            lblId.getStyleClass().add("text-muted");
            lblId.setStyle("-fx-font-size: 11px;");

            Label lblIzena = new Label(a.getIzenburua());
            lblIzena.getStyleClass().addAll("font-bold", "text-dark");
            lblIzena.setStyle("-fx-font-size: 13px;");

            String katIzena = "—";
            if (a.getKategoria() != null) {
                katIzena = a.getKategoria().getIzena();
            }
            Label lblKat = new Label(katIzena);
            lblKat.getStyleClass().add("text-muted");
            lblKat.setStyle("-fx-font-size: 11px;");

            VBox txartela = new VBox(lblId, lblIzena, lblKat);
            txartela.setStyle("-fx-padding: 8; -fx-background-color: #F9FAFB; -fx-background-radius: 6;");
            vboxBateragarriak.getChildren().add(txartela);
        }
    }

    /**
     * Eskuineko xehetasun-panela hutsik uzten du erreklamaziorik ez dagoenean.
     */
    private void panelHutsak() {
        lblIdErreklam.setText("—");
        lblEgoeraBadge.setText("—");
        lblJabeIzena.setText("Ez dago erreklamaziorik");
        lblKontaktua.setText("");
        lblDeskribapenaTestua.setText("");
        vboxBateragarriak.getChildren().clear();
        btnEbatzi.setVisible(false);
        btnEbatzi.setManaged(false);
        btnBaztertu.setVisible(false);
        btnBaztertu.setManaged(false);
        btnIrekiBerriz.setVisible(false);
        btnIrekiBerriz.setManaged(false);
    }

    /**
     * Hautatutako erreklamazioa ebazteko emanaldiaren formularioa irekitzen du,
     * jabea eta bat-etortze posibleak aurrez beteta.
     */
    @FXML
    private void ebatzi() {
        if (hautatua == null) {
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Emanaldia.fxml"));
            javafx.scene.Parent root = loader.load();
            EmanaldiaController ctrl = loader.getController();
            ctrl.setErreklamazioa(hautatua);
            UIKudeatzailea.kargatuPanela(root);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ebatzi: Emanaldia FXML kargatzean errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da emanaldia formularioa kargatu.", false);
        }
    }

    /**
     * Hautatutako erreklamazioa baztertuta gisa markatzen du.
     */
    @FXML
    private void baztertu() {
        aldatuEgoera("baztertuta");
    }

    /**
     * Hautatutako erreklamazioa berriro irekita gisa markatzen du.
     */
    @FXML
    private void irekiBerriz() {
        aldatuEgoera("irekita");
    }

    /**
     * Hautatutako erreklamazioaren egoera datu-basean eguneratzen du.
     *
     * @param egoera Ezarri beharreko egoera testua
     */
    private void aldatuEgoera(String egoera) {
        if (hautatua == null) {
            return;
        }
        boolean ok = ErreklamazioaDAO.updateEgoera(String.valueOf(hautatua.getErreklamazioId()), egoera);
        if (ok) {
            kargatu();
        }
    }

    /**
     * Iragazkia irekitako erreklamazioetara aldatzen du.
     */
    @FXML
    private void filtroIrekiak() {
        egoeraFiltro = "irekita";
        btnFiltroIrekiak.getStyleClass().setAll("btn-primary");
        btnFiltroBaztertuak.getStyleClass().setAll("btn-outline");
        btnFiltroEgindak.getStyleClass().setAll("btn-outline");
        kargatu();
    }

    /**
     * Iragazkia baztertutako erreklamazioetara aldatzen du.
     */
    @FXML
    private void filtroBaztertutak() {
        egoeraFiltro = "baztertuta";
        btnFiltroIrekiak.getStyleClass().setAll("btn-outline");
        btnFiltroBaztertuak.getStyleClass().setAll("btn-primary");
        btnFiltroEgindak.getStyleClass().setAll("btn-outline");
        kargatu();
    }

    /**
     * Iragazkia ebatzitako erreklamazioetara aldatzen du.
     */
    @FXML
    private void filtroEgindak() {
        egoeraFiltro = "ebatzita";
        btnFiltroIrekiak.getStyleClass().setAll("btn-outline");
        btnFiltroBaztertuak.getStyleClass().setAll("btn-outline");
        btnFiltroEgindak.getStyleClass().setAll("btn-primary");
        kargatu();
    }

    /**
     * Erreklamazino berri bat sortzeko formularioa contentArea-n kargatzen du.
     */
    @FXML
    public void erreklamazioaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ErreklamazioaBerria.fxml"));
            Node nodoa = loader.load();
            UIKudeatzailea.kargatuPanela(nodoa);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "erreklamazioaBerria: FXML kargatzean errorea", e);
            UIKudeatzailea.erakutsiToast("Errorea: ezin izan da formularioa kargatu.", false);
        }
    }

    /**
     * Egoera kode batetik erakusteko testua itzultzen du.
     *
     * @param egoera Egoera kode testua
     * @return Erabiltzaileari erakusteko etiketa
     */
    private String egoeraBadgeTestua(String egoera) {
        if (egoera == null) {
            return "Irekia";
        }
        if (egoera.equals("irekita")) {
            return "Irekia";
        } else if (egoera.equals("ebatzita")) {
            return "Ebatzita";
        } else if (egoera.equals("baztertuta")) {
            return "Baztertuta";
        } else {
            return egoera;
        }
    }

    /**
     * Egoera kode batetik CSS klase-izena itzultzen du badgearen kolorerako.
     *
     * @param egoera Egoera kode testua
     * @return CSS klase-izena
     */
    private String egoeraBadgeKlasea(String egoera) {
        if (egoera == null) {
            return "badge-neutral";
        }
        if (egoera.equals("irekita")) {
            return "badge-neutral";
        } else if (egoera.equals("ebatzita")) {
            return "badge-success";
        } else if (egoera.equals("baztertuta")) {
            return "badge-warning";
        } else {
            return "badge-neutral";
        }
    }
}
