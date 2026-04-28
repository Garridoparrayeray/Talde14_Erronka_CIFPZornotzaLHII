package controller;

import dao.EstadistikaDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import utils.DBConexioa;
import utils.Sesio;

import java.io.File;
import java.io.FileWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AdminPanelaController implements Initializable {

    @FXML private Label lblLangileak;
    @FXML private Label lblArtikuluak;
    @FXML private Label lblKategoriak;
    @FXML private Label lblKokalekuak;
    @FXML private Label lblDbEgoera;
    @FXML private Label lblAzkenKopia;
    @FXML private Label lblIzena;

    private static String azkenKopiaDatea = "Inoiz ez";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (Sesio.getLangilea() != null && lblIzena != null) {
            lblIzena.setText(Sesio.getLangilea().getIzena() + " " + Sesio.getLangilea().getAbizena());
        }

        lblLangileak.setText(String.valueOf(EstadistikaDAO.langileKopurua()));
        lblArtikuluak.setText(String.valueOf(EstadistikaDAO.artikuluGuztienKopurua()));
        lblKategoriak.setText(String.valueOf(EstadistikaDAO.kategoriaKopurua()));
        lblKokalekuak.setText(String.valueOf(EstadistikaDAO.kokalekuakKopurua()));

        boolean konektatuta = EstadistikaDAO.dbKonexioaEgiaztatu();
        if (konektatuta) {
            lblDbEgoera.setText("Konektatuta");
            lblDbEgoera.setStyle("-fx-text-fill: #03543F;");
        } else {
            lblDbEgoera.setText("Errorea");
            lblDbEgoera.setStyle("-fx-text-fill: #9B1C1C;");
        }

        lblAzkenKopia.setText(azkenKopiaDatea);
    }

    @FXML
    public void egiaztatuKonexioa() {
        boolean ok = EstadistikaDAO.dbKonexioaEgiaztatu();
        if (ok) {
            lblDbEgoera.setText("Konektatuta");
            lblDbEgoera.setStyle("-fx-text-fill: #03543F;");
        } else {
            lblDbEgoera.setText("Errorea");
            lblDbEgoera.setStyle("-fx-text-fill: #9B1C1C;");
        }
    }

    @FXML
    public void eginBabesKopia() {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String fitxategiIzena = "/app/exportazioak/backup_" + data + ".sql";

        // Tokala fitxategia existitzen ez bada (IDE lokalean)
        File exportDir = new File("/app/exportazioak");
        if (!exportDir.exists()) {
            exportDir = new File(System.getProperty("user.home") + "/galdutakoak_backups");
            exportDir.mkdirs();
            fitxategiIzena = exportDir.getAbsolutePath() + "/backup_" + data + ".sql";
        }

        String[] taulak = {"ROLA", "LANGILEA", "KATEGORIA", "KOKALEKUA",
                           "HARTZAILEA", "JABEA", "ERAKUNDEA", "ARTIKULUA",
                           "ERREKLAMAZIOA", "EMANALDIA", "MUGIMENDUA", "JAKINARAZPENA"};

        try (FileWriter fw = new FileWriter(fitxategiIzena);
             Connection con = DBConexioa.getKonexioa();
             Statement st = con.createStatement()) {

            fw.write("-- Babes-kopia: " + data + "\n");
            fw.write("USE erronka_galduak;\n\n");

            for (String taula : taulak) {
                fw.write("-- " + taula + "\n");
                fw.write("DELETE FROM " + taula + ";\n");

                ResultSet rs = st.executeQuery("SELECT * FROM " + taula);
                ResultSetMetaData meta = rs.getMetaData();
                int cols = meta.getColumnCount();

                while (rs.next()) {
                    StringBuilder sb = new StringBuilder("INSERT INTO " + taula + " VALUES (");
                    for (int i = 1; i <= cols; i++) {
                        String val = rs.getString(i);
                        if (val == null) sb.append("NULL");
                        else sb.append("'").append(val.replace("'", "\\'")).append("'");
                        if (i < cols) sb.append(", ");
                    }
                    sb.append(");\n");
                    fw.write(sb.toString());
                }
                fw.write("\n");
            }

            azkenKopiaDatea = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
            lblAzkenKopia.setText(azkenKopiaDatea);
            lblAzkenKopia.setStyle("-fx-text-fill: #03543F;");

        } catch (Exception e) {
            lblAzkenKopia.setText("Errorea: " + e.getMessage());
            lblAzkenKopia.setStyle("-fx-text-fill: #9B1C1C;");
        }
    }
}
