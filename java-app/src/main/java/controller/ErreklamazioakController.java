package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.ArtikuluaDAO;
import dao.ErreklamazioaDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Artikulua;
import model.Erreklamazioa;

/**
 * Erreklamazioen zerrendaren eta xehetasunen kontroladorea.
 * @author Yeray Garrido
 */
public class ErreklamazioakController implements Initializable {

    @FXML private VBox  listVBox;
    @FXML private Label lblKopurua;

    @FXML private VBox   panelXehetasuna;
    @FXML private Label  lblIdErreklam;
    @FXML private Label  lblEgoeraBadge;
    @FXML private Label  lblJabeIzena;
    @FXML private Label  lblKontaktua;
    @FXML private Label  lblDeskribapenaTestua;
    @FXML private VBox   vboxBateragarriak;
    @FXML private Button btnEbatzi;
    @FXML private Button btnBaztertu;
    @FXML private Button btnIrekiBerriz;

    private List<Erreklamazioa> erreklamazioak;
    private Erreklamazioa       hautatua;
    private VBox                itemHautatua;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        kargatu();
    }

    // ── Datuak kargatu ───────────────────────────────────────────────────────

    private void kargatu() {
        erreklamazioak = ErreklamazioaDAO.getGuztiak();
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

    // ── Lista item sortu ──────────────────────────────────────────────────────

    private VBox sortuListaItem(Erreklamazioa err, boolean aktiboa) {
        // Goiburua: ID + egoera badge
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

        // Jabearen izena eta abizena
        String izena   = err.getJabeIzena();
        String abizena = err.getJabeAbizena();
        Label lblIzena = new Label(izena + " " + abizena);
        lblIzena.getStyleClass().addAll("font-bold", "text-dark");
        lblIzena.setStyle("-fx-font-size: 15px;");

        // Deskribapena laburtu (elipsia)
        String deskribapena = err.getDeskribapenBilatua();
        if (deskribapena == null) {
            deskribapena = "";
        }
        Label lblDesk = new Label(deskribapena);
        lblDesk.getStyleClass().add("text-muted");
        lblDesk.setMaxWidth(240);
        lblDesk.setWrapText(false);
        lblDesk.setEllipsisString("...");

        // Data
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

        // Klikatzean xehetasunak erakutsi
        item.setOnMouseClicked(e -> hautatu(err, item));
        return item;
    }

    // ── Elementua hautatu ────────────────────────────────────────────────────

    private void hautatu(Erreklamazioa err, VBox item) {
        // Aurreko hautaketa kendu
        if (itemHautatua != null) {
            itemHautatua.getStyleClass().removeAll("list-item-active");
            itemHautatua.getStyleClass().add("list-item");
        }
        itemHautatua = item;
        item.getStyleClass().removeAll("list-item");
        item.getStyleClass().add("list-item-active");

        hautatua = err;

        // ID
        lblIdErreklam.setText("E-" + err.getErreklamazioId());

        // Egoera badge
        String egoera = err.getEgoeraTestua();
        lblEgoeraBadge.setText(egoeraBadgeTestua(egoera));
        lblEgoeraBadge.getStyleClass().setAll(egoeraBadgeKlasea(egoera));

        // Jabearen datuak
        lblJabeIzena.setText(err.getJabeIzena() + " " + err.getJabeAbizena());
        lblKontaktua.setText(err.getJabeTelefonoa() + " · " + err.getJabeEmaila());

        // Deskribapena
        String desk = err.getDeskribapenBilatua();
        if (desk != null) {
            lblDeskribapenaTestua.setText(desk);
        } else {
            lblDeskribapenaTestua.setText("");
        }

        // Bat-etortze posibleak — biltegiko artikuluekin konparatu
        erakutsiBateragarriak(err);

        // Botoia egoeraren arabera erakutsi edo ezkutatu
        boolean irekita = "irekita".equals(egoera);
        btnEbatzi.setVisible(irekita);
        btnEbatzi.setManaged(irekita);
        btnBaztertu.setVisible(irekita);
        btnBaztertu.setManaged(irekita);
        btnIrekiBerriz.setVisible(!irekita);
        btnIrekiBerriz.setManaged(!irekita);
    }

    // ── Bat-etortze posibleak ────────────────────────────────────────────────

    private void erakutsiBateragarriak(Erreklamazioa err) {
        vboxBateragarriak.getChildren().clear();

        // Biltegiko artikulu guztiak kargatu eta bat datozenak bilatu
        List<Artikulua> guztiak = ArtikuluaDAO.getGuztiak();
        List<Artikulua> bateragarriak = err.bilatuBateragarriak(guztiak);

        if (bateragarriak.isEmpty()) {
            Label lblHuts = new Label("Bat-etortze posiblerik ez.");
            lblHuts.getStyleClass().add("text-muted");
            vboxBateragarriak.getChildren().add(lblHuts);
            return;
        }

        // Artikulu bat-etortze bakoitza txartel moduan erakutsi
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

    // ── Panel hutsa ──────────────────────────────────────────────────────────

    private void panelHutsak() {
        lblIdErreklam.setText("—");
        lblEgoeraBadge.setText("—");
        lblJabeIzena.setText("Ez dago erreklamaziorik");
        lblKontaktua.setText("");
        lblDeskribapenaTestua.setText("");
        vboxBateragarriak.getChildren().clear();
        btnEbatzi.setVisible(false);      btnEbatzi.setManaged(false);
        btnBaztertu.setVisible(false);    btnBaztertu.setManaged(false);
        btnIrekiBerriz.setVisible(false); btnIrekiBerriz.setManaged(false);
    }

    // ── Egoera aldatu ────────────────────────────────────────────────────────

    @FXML
    private void ebatzi() {
        aldatuEgoera("ebatzita");
    }

    @FXML
    private void baztertu() {
        aldatuEgoera("baztertuta");
    }

    @FXML
    private void irekiBerriz() {
        aldatuEgoera("irekita");
    }

    private void aldatuEgoera(String egoera) {
        if (hautatua == null) {
            return;
        }
        boolean ok = ErreklamazioaDAO.updateEgoera(String.valueOf(hautatua.getErreklamazioId()), egoera);
        if (ok) {
            kargatu();
        }
    }

    // ── Erreklamazino berria ─────────────────────────────────────────────────

    @FXML
    public void erreklamazioaBerria() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ErreklamazioaBerria.fxml"));
            Parent root = loader.load();
            ErreklamazioaBerriController ctrl = loader.getController();

            Stage dialog = new Stage();
            dialog.initOwner(listVBox.getScene().getWindow());
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setTitle("Erreklamazino berria");
            dialog.setScene(new Scene(root, 520, 540));
            dialog.setResizable(false);

            ctrl.setOnGorde(this::kargatu);

            dialog.showAndWait();
        } catch (Exception e) {
            System.err.println("ErreklamazioakController.erreklamazioaBerria errorea: " + e.getMessage());
        }
    }

    // ── Laguntzaileak ────────────────────────────────────────────────────────

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
