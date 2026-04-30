package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.KategoriaKopurua;
import utils.DBConexioa;

/**
 * Datu-baseko estatistikak eta kopuru orokorrak lortzeko DAO klasea.
 * @author Yeray Garrido
 */
public class EstadistikaDAO {

    /**
     * Biltegian dauden artikulu kopurua itzultzen du.
     * @return Artikulu kopurua
     */
    public static int biltegianKopurua() {
        return kontatuEgoera("aurkitua");
    }

    /**
     * Bueltatutako artikulu kopurua itzultzen du.
     * @return Artikulu kopurua
     */
    public static int bueltatakoKopurua() {
        return kontatuEgoera("bueltatua");
    }

    /**
     * Iraungita dauden artikulu kopurua itzultzen du.
     * @return Artikulu kopurua
     */
    public static int iraungituakKopurua() {
        return kontatuEgoera("iraungita");
    }

    /**
     * Hurrengo 30 egunetan iraungiko diren artikulu kopurua itzultzen du.
     * @return Artikulu kopurua
     */
    public static int iraungitzearKopurua() {
        String sql = "SELECT COUNT(*) FROM ARTIKULUA " +
                     "WHERE egoera = 'aurkitua' AND iraungitze_data BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)";
        return kontatuSql(sql);
    }

    /**
     * Irekita dauden erreklamazio kopurua itzultzen du.
     * @return Erreklamazio kopurua
     */
    public static int erreklamazioIrekiakKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM ERREKLAMAZIOA WHERE errek_egoera = 'irekita'");
    }

    /**
     * Sisteman erregistratuta dauden langile kopurua itzultzen du.
     * @return Langile kopurua
     */
    public static int langileKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM LANGILEA");
    }

    /**
     * Datu-basean dauden artikulu guztien kopurua itzultzen du.
     * @return Artikulu kopurua
     */
    public static int artikuluGuztienKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM ARTIKULUA");
    }

    /**
     * Kategoria kopurua itzultzen du.
     * @return Kategoria kopurua
     */
    public static int kategoriaKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM KATEGORIA");
    }

    /**
     * Kokaleku kopurua itzultzen du.
     * @return Kokaleku kopurua
     */
    public static int kokalekuakKopurua() {
        return kontatuSql("SELECT COUNT(*) FROM KOKALEKUA");
    }

    /** 
     * Azken 10 mugimenduak lortzen ditu datu-basetik.
     * @return Azken mugimenduen zerrenda
     */
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

    /** 
     * Kategoria bakoitzak zenbat artikulu dituen lortzen du.
     * @return Kategoria bakoitzaren izena eta artikulu kopurua
     */
    public static List<KategoriaKopurua> kategoriaKopuruak() {
        List<KategoriaKopurua> zerrenda = new ArrayList<>();
        String sql = "SELECT k.izena, COUNT(a.id_artikulua) AS kop " +
                     "FROM KATEGORIA k LEFT JOIN ARTIKULUA a ON k.id_kategoria = a.id_kategoria " +
                     "GROUP BY k.id_kategoria, k.izena ORDER BY kop DESC";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String izena = rs.getString("izena");
                int kop = rs.getInt("kop");
                
                // Eredua sortu eta zerrendan gehitu
                KategoriaKopurua katKopurua = new KategoriaKopurua(izena, kop);
                zerrenda.add(katKopurua);
            }
        } catch (SQLException e) {
            System.err.println("EstadistikaDAO.kategoriaKopuruak: " + e.getMessage());
        }
        return zerrenda;
    }

    /**
     * Datu-basearekiko konexioa ondo dabilen egiaztatzen du.
     * @return Konexioa badago true, bestela false
     */
    public static boolean dbKonexioaEgiaztatu() {
        try (Connection con = DBConexioa.getKonexioa()) {
            return con != null && !con.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    // ── Laguntzaileak ────────────────────────────────────────────────────────

    /**
     * Emandako egoera batean dauden artikuluak kontatzen ditu.
     * @param egoera Bilatu beharreko artikuluaren egoera
     * @return Artikulu kopurua
     */
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
