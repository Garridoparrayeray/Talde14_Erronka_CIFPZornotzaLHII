package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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
import model.Erreklamazioa;

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

    private List<Erreklamazioa> erreklamazioak;
    private Erreklamazioa       hautatua;
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
        // ID + egoera 
        Label lblId = new Label("E-" + err.getErreklamazioId());
        lblId.getStyleClass().addAll("text-muted");
        lblId.setStyle("-fx-font-size: 12px;");

        String egoeraStr;
        if (err.getEgoera() != null) {
            egoeraStr = err.getEgoera().toString().toLowerCase();
        } else {
            egoeraStr = "irekita";
        }
        Label badge = new Label(egoeraBadgeTestua(egoeraStr));
        badge.getStyleClass().add(egoeraBadgeKlasea(egoeraStr));

        Region spacer = new Region();
        HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

        HBox header = new HBox(lblId, spacer, badge);
        header.setAlignment(javafx.geometry.Pos.CENTER_LEFT);

        String izena;
        String abizena;
        if (err.getHartzailea() != null) {
            if (err.getHartzailea() instanceof model.Jabea) {
                model.Jabea j = (model.Jabea) err.getHartzailea();
                izena = j.getIzena();
                abizena = j.getAbizena();
            } else {
                izena = "Erakundea";
                abizena = "";
            }
        } else {
            izena = "";
            abizena = "";
        }
        Label lblIzena = new Label(izena + " " + abizena);
        lblIzena.getStyleClass().addAll("font-bold", "text-dark");
        lblIzena.setStyle("-fx-font-size: 15px;");

        String deskribapena;
        if (err.getDeskribapenBilatua() != null) {
            deskribapena = err.getDeskribapenBilatua();
        } else {
            deskribapena = "";
        }
        Label lblDesk = new Label(deskribapena);
        lblDesk.getStyleClass().add("text-muted");
        lblDesk.setMaxWidth(240);
        lblDesk.setWrapText(false);
        lblDesk.setEllipsisString("...");

        String dataStr;
        if (err.getErreklamazioData() != null) {
            dataStr = err.getErreklamazioData().toString();
        } else {
            dataStr = "";
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

    // ── Elementua hautatu ────────────────────────────────────────────────────

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

        String egoera;
        if (err.getEgoera() != null) {
            egoera = err.getEgoera().toString().toLowerCase();
        } else {
            egoera = "irekita";
        }
        lblEgoeraBadge.setText(egoeraBadgeTestua(egoera));
        lblEgoeraBadge.getStyleClass().setAll(egoeraBadgeKlasea(egoera));

        String izena;
        String abizena;
        String tel;
        String email;
        if (err.getHartzailea() != null) {
            if (err.getHartzailea() instanceof model.Jabea) {
                model.Jabea jabea = (model.Jabea) err.getHartzailea();
                izena = jabea.getIzena();
                abizena = jabea.getAbizena();
                tel = jabea.getTelefonoa();
                email = jabea.getEmaila();
            } else {
                izena = "Erakundea";
                abizena = "";
                tel = "";
                email = "";
            }
        } else {
            izena = "";
            abizena = "";
            tel = "";
            email = "";
        }
        lblJabeIzena.setText(izena + " " + abizena);
        
        lblKontaktua.setText(tel + " · " + email);
        
        if (err.getDeskribapenBilatua() != null) {
            lblDeskribapenaTestua.setText(err.getDeskribapenBilatua());
        } else {
            lblDeskribapenaTestua.setText("");
        }

        String katIzena;
        if (err.getKategoria() != null) {
            katIzena = err.getKategoria().getIzena();
        } else {
            katIzena = "—";
        }
        lblBatEtortzeInfo.setText("Kategoria: " + katIzena);

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
