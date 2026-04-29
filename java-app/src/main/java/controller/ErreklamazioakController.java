package controller;

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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ErreklamazioakController implements Initializable {

    @FXML private VBox  listVBox;
    @FXML private Label lblKopurua;

    @FXML private VBox  panelXehetasuna;
    @FXML private Label lblIdErreklam;
    @FXML private Label lblEgoeraBadge;
    @FXML private Label lblJabeIzena;
    @FXML private Label lblKontaktua;
    @FXML private Label lblDeskribapenaTestua;
    @FXML private Label lblBatEtortzeInfo;
    @FXML private Button btnEbatzi;
    @FXML private Button btnBaztertu;
    @FXML private Button btnIrekiBerriz;

    private List<String[]> erreklamazioak;
    private String[]       hautatua;
    private VBox           itemHautatua;

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
        for (String[] err : erreklamazioak) {
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

    private VBox sortuListaItem(String[] err, boolean aktiboa) {
        // ID + egoera badge
        Label lblId = new Label("E-" + err[0]);
        lblId.getStyleClass().addAll("text-muted");
        lblId.setStyle("-fx-font-size: 12px;");

        Label badge = new Label(egoeraBadgeTestua(err[8]));
        badge.getStyleClass().add(egoeraBadgeKlasea(err[8]));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox header = new HBox(lblId, spacer, badge);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        Label lblIzena = new Label(err[2] + " " + err[3]);
        lblIzena.getStyleClass().addAll("font-bold", "text-dark");
        lblIzena.setStyle("-fx-font-size: 15px;");

        Label lblDesk = new Label(err[7] != null ? err[7] : "");
        lblDesk.getStyleClass().add("text-muted");
        lblDesk.setMaxWidth(240);
        lblDesk.setWrapText(false);
        lblDesk.setEllipsisString("...");

        Label lblData = new Label("Jasoa: " + (err[1] != null ? err[1] : ""));
        lblData.getStyleClass().add("text-muted");
        lblData.setStyle("-fx-font-size: 12px;");
        VBox.setMargin(lblData, new Insets(4, 0, 0, 0));

        VBox item = new VBox(header, lblIzena, lblDesk, lblData);
        item.getStyleClass().add(aktiboa ? "list-item-active" : "list-item");

        item.setOnMouseClicked(e -> hautatu(err, item));
        return item;
    }

    // ── Elementua hautatu ────────────────────────────────────────────────────

    private void hautatu(String[] err, VBox item) {
        if (itemHautatua != null) {
            itemHautatua.getStyleClass().removeAll("list-item-active");
            itemHautatua.getStyleClass().add("list-item");
        }
        itemHautatua = item;
        item.getStyleClass().removeAll("list-item");
        item.getStyleClass().add("list-item-active");

        hautatua = err;

        lblIdErreklam.setText("E-" + err[0]);

        String egoera = err[8] != null ? err[8] : "irekita";
        lblEgoeraBadge.setText(egoeraBadgeTestua(egoera));
        lblEgoeraBadge.getStyleClass().setAll(egoeraBadgeKlasea(egoera));

        lblJabeIzena.setText(err[2] + " " + err[3]);
        lblKontaktua.setText(err[4] + " · " + err[5]);
        lblDeskribapenaTestua.setText(err[7] != null ? err[7] : "");
        lblBatEtortzeInfo.setText("Kategoria: " + (err[6] != null ? err[6] : "—"));

        boolean irekita = "irekita".equals(egoera);
        btnEbatzi.setVisible(irekita);
        btnEbatzi.setManaged(irekita);
        btnBaztertu.setVisible(irekita);
        btnBaztertu.setManaged(irekita);
        btnIrekiBerriz.setVisible(!irekita);
        btnIrekiBerriz.setManaged(!irekita);
    }

    private void panelHutsak() {
        lblIdErreklam.setText("—");
        lblEgoeraBadge.setText("—");
        lblJabeIzena.setText("Ez dago erreklamaziorik");
        lblKontaktua.setText("");
        lblDeskribapenaTestua.setText("");
        lblBatEtortzeInfo.setText("");
        btnEbatzi.setVisible(false);     btnEbatzi.setManaged(false);
        btnBaztertu.setVisible(false);   btnBaztertu.setManaged(false);
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
        boolean ok = ErreklamazioaDAO.updateEgoera(hautatua[0], egoera);
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
            dialog.setTitle("Erreklamazino berria");
            dialog.setScene(new Scene(root, 520, 540));
            dialog.initModality(Modality.APPLICATION_MODAL);
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
        switch (egoera) {
            case "irekita":   return "Irekia";
            case "ebatzita":  return "Ebatzita";
            case "baztertuta": return "Baztertuta";
            default:          return egoera;
        }
    }

    private String egoeraBadgeKlasea(String egoera) {
        if (egoera == null) {
            return "badge-neutral";
        }
        switch (egoera) {
            case "irekita":   return "badge-neutral";
            case "ebatzita":  return "badge-success";
            case "baztertuta": return "badge-warning";
            default:          return "badge-neutral";
        }
    }
}
