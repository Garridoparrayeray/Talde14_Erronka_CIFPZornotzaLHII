package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConexioa {

    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        if (System.getenv("DB_URL") != null) {
            URL = System.getenv("DB_URL");
        } else {
            URL = "jdbc:mariadb://localhost:3306/erronka_galduak";
        }
        if (System.getenv("DB_USER") != null) {
            USER = System.getenv("DB_USER");
        } else {
            USER = "bermeo_udaltzain";
        }
        if (System.getenv("DB_PASS") != null) {
            PASS = System.getenv("DB_PASS");
        } else {
            PASS = "udaltzainpw";
        }
    }

    private static Connection konexioa = null;

    private DBConexioa() {}

    public static Connection getKonexioa() throws SQLException {
        if (konexioa == null || konexioa.isClosed()) {
            konexioa = DriverManager.getConnection(URL, USER, PASS);
        }
        return konexioa;
    }

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
