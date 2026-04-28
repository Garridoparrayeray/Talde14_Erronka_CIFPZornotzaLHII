package dao;

import utils.DBConexioa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadistikaDAO {

    public static int biltegianKopurua() {
        return kontatuEgoera("aurkitua");
    }

    public static int bueltatakoKopurua() {
        return kontatuEgoera("bueltatua");
    }

    public static int iraungituakKopurua() {
        return kontatuEgoera("iraungita");
    }

    public static int iraungitzearKopurua() {
        String sql = "SELECT COUNT(*) FROM ARTIKULUA " +
                     "WHERE egoera = 'aurkitua' AND iraungitze_data BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)";
        return kontatuSql(sql);
    }

    public static int erreklamazioIrekiakKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM ERREKLAMAZIOA WHERE errek_egoera = 'irekita'");
    }

    public static int langileKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM LANGILEA");
    }

    public static int artikuluGuztienKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM ARTIKULUA");
    }

    public static int kategoriaKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM KATEGORIA");
    }

    public static int kokalekuakKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM KOKALEKUA");
    }

    /** Azken 10 mugimenduak: [id_artikulua, deskribapena, data, langile] */
    public static List<String[]> azkenMugimenduak() {
        List<String[]> zerrenda = new ArrayList<>();
        String sql = "SELECT m.id_artikulua, m.deskribapena, m.data, " +
                     "COALESCE(CONCAT(l.izena,' ',l.abizena), '—') AS langilea " +
                     "FROM MUGIMENDUA m LEFT JOIN LANGILEA l ON m.id_langile = l.id_langile " +
                     "ORDER BY m.data DESC LIMIT 10";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new String[]{
                    rs.getString("id_artikulua"),
                    rs.getString("deskribapena"),
                    rs.getString("data"),
                    rs.getString("langilea")
                });
            }
        } catch (SQLException e) {
            System.err.println("EstadistikaDAO.azkenMugimenduak: " + e.getMessage());
        }
        return zerrenda;
    }

    /** Kategorien artikulu-kopurua: [izena, kopurua] */
    public static List<String[]> kategoriaKopuruak() {
        List<String[]> zerrenda = new ArrayList<>();
        String sql = "SELECT k.izena, COUNT(a.id_artikulua) AS kop " +
                     "FROM KATEGORIA k LEFT JOIN ARTIKULUA a ON k.id_kategoria = a.id_kategoria " +
                     "GROUP BY k.id_kategoria, k.izena ORDER BY kop DESC";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new String[]{rs.getString("izena"), rs.getString("kop")});
            }
        } catch (SQLException e) {
            System.err.println("EstadistikaDAO.kategoriaKopuruak: " + e.getMessage());
        }
        return zerrenda;
    }

    public static boolean dbKonexioaEgiaztatu() {
        try (Connection con = DBConexioa.getKonexioa()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // ---- helpers ----

    private static int kontatuEgoera(String egoera) {
        String sql = "SELECT COUNT(*) FROM ARTIKULUA WHERE egoera = ?";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, egoera);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("EstadistikaDAO.kontatuEgoera: " + e.getMessage());
        }
        return 0;
    }

    private static int kontatuSql(String sql) {
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("EstadistikaDAO.kontatuSql: " + e.getMessage());
        }
        return 0;
    }
}
