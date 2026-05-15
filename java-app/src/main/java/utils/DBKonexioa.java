package utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Datu-basearekin konexio bakarra mantentzen duen singleton klasea.
 * Konfigurazioaren lehentasuna: ingurune-aldagaiak > application.properties.
 *
 * @author Yeray Garrido
 */
public class DBKonexioa {

    private static final String URL = lortuPropietatea("DB_URL", "jdbc:mariadb://localhost:3306/erronka_galduak");
    private static final String USER = lortuPropietatea("DB_USER", "root");
    private static final String PASS = lortuPropietatea("DB_PASS", "");

    private static String lortuPropietatea(String gakoa, String defektuz) {
        String env = System.getenv(gakoa);
        if (env != null) {
            return env;
        }
        Properties props = new Properties();
        try (InputStream is = DBKonexioa.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                props.load(is);
            }
        } catch (Exception ignored) {
        }
        return props.getProperty(gakoa, defektuz);
    }

    private static Connection konexioa = null;

    /**
     * Datu-basearekin konexioa itzultzen du, beharrezkoa bada berria sortuz.
     *
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
     * Datu-basearekiko konexioa ondo dabilen egiaztatzen du.
     *
     * @return Konexioa badago true, bestela false
     */
    public static boolean egiaztatu() {
        if (ModoKudeatzailea.isOffline()) {
            return false;
        }
        try (Connection con = getKonexioa()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
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
