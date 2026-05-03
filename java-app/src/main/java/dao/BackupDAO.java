package dao;

import utils.DBConexioa;

import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Datu-basearen babes-kopiak kudeatzeko DAO klasea.
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
     * @return Sortutako fitxategiaren bide osoa
     * @throws Exception Idazketa edo konexio errorea
     */
    public static String eginBabesKopia() throws Exception {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

        File exportDir = new File("/app/exportazioak");
        if (!exportDir.exists()) {
            exportDir = new File(System.getProperty("user.home") + "/galdutakoak_backups");
            exportDir.mkdirs();
        }
        String fitxategiIzena = exportDir.getAbsolutePath() + "/backup_" + data + ".sql";

        try (FileWriter fw = new FileWriter(fitxategiIzena);
             Connection con = DBConexioa.getKonexioa();
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
                            String val = rs.getString(i);
                            if (val == null) {
                                sb.append("NULL");
                            } else {
                                sb.append("'").append(val.replace("'", "\\'")).append("'");
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
        }
        return fitxategiIzena;
    }
}
