package dao;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import utils.AppConfig;
import utils.DBKonexioa;
import utils.ModoKudeatzailea;

/**
 * Datu-basearen babes-kopiak kudeatzeko DAO klasea.
 *
 * @author Yeray Garrido
 */
public class BackupDAO {

    private static final String[] TAULAK = {
            "ROLA", "LANGILEA", "KATEGORIA", "KOKALEKUA",
            "HARTZAILEA", "JABEA", "ERAKUNDEA", "ARTIKULUA",
            "ERREKLAMAZIOA", "EMANALDIA", "MUGIMENDUA", "JAKINARAZPENA"
    };

    /**
     * Datu-basearen babes-kopia SQL fitxategi batean gordetzen du.
     *
     * @return Sortutako fitxategiaren bide osoa
     * @throws Exception Offline moduan deitzen bada edo IO/SQL errorea
     *                   gertatzen bada
     */
    public static String eginBabesKopia() throws Exception {
        if (ModoKudeatzailea.isOffline()) {
            throw new Exception("Aplikazioa offline moduan dago. Ezin da datu-basearen babes-kopiarik egin.");
        }

        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

        File exportDir = new File(AppConfig.getExportBidea());
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        String fitxategiIzena = exportDir.getAbsolutePath() + File.separator + "backup_" + data + ".sql";

        try (FileWriter fw = new FileWriter(fitxategiIzena);
                Connection con = DBKonexioa.getKonexioa();
                Statement st = con.createStatement()) {

            fw.write("-- Babes-kopia: " + data + "\n");
            fw.write("USE erronka_galduak;\n\n");

            for (String taula : TAULAK) {
                fw.write("-- " + taula + "\n");
                fw.write("DELETE FROM " + taula + ";\n");

                try (ResultSet rs = st.executeQuery("SELECT * FROM " + taula)) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int cols = meta.getColumnCount();

                    while (rs.next()) {
                        StringBuilder sb = new StringBuilder("INSERT INTO " + taula + " VALUES (");
                        for (int i = 1; i <= cols; i++) {
                            String bal = rs.getString(i);
                            if (bal == null) {
                                sb.append("NULL");
                            } else {
                                sb.append("'").append(bal.replace("'", "\\'")).append("'");
                            }
                            if (i < cols) {
                                sb.append(", ");
                            }
                        }
                        sb.append(");\n");
                        fw.write(sb.toString());
                    }
                }
                fw.write("\n");
            }
        } catch (Exception e) {
            throw new Exception("Babes-kopian errorea: " + e.getMessage(), e);
        }
        return fitxategiIzena;
    }
}
