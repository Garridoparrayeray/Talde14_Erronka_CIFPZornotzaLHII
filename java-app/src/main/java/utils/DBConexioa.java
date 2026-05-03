package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Datu-basearekin konexio bakarra mantentzen duen singleton klasea.
 * Konfigurazioaren lehentasuna: ingurune-aldagaiak > application.properties.
 * @author Yeray Garrido
 */
public class DBConexioa {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        // 1. Lehentasuna: ingurune-aldagaiak (Docker / produkzioa)
        // 2. Bigarren aukera: application.properties (.env-tik eratorria, garapen lokala)
        Properties props = new Properties();
        try (InputStream is = DBConexioa.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception ignored) {}

        if (System.getenv("DB_URL") != null) {
            URL = System.getenv("DB_URL");
        } else {
            URL = props.getProperty("DB_URL", "jdbc:mariadb://localhost:3306/erronka_galduak");
        }
        if (System.getenv("DB_USER") != null) {
            USER = System.getenv("DB_USER");
        } else {
            USER = props.getProperty("DB_USER", "root");
        }
        if (System.getenv("DB_PASS") != null) {
            PASS = System.getenv("DB_PASS");
        } else {
            PASS = props.getProperty("DB_PASS", "");
        }
    }

    private static Connection konexioa = null;

    private DBConexioa() {}

    /**
     * Datu-basearekin konexioa itzultzen du, beharrezkoa bada berria sortuz.
     * @return Konexio aktiboa
     * @throws SQLException Konexio-errorea bada
     */
    public static Connection getKonexioa() throws SQLException {
        if (konexioa == null || konexioa.isClosed()) {
            konexioa = DriverManager.getConnection(URL, USER, PASS);
        }
        return konexioa;
    }

    /**
     * Datu-basearekin konexioa ixten du.
     */
    public static void itxi() {
        try {
            if (konexioa != null && !konexioa.isClosed()) {
                konexioa.close();
                konexioa = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
