package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.AzkenMugimendua;
import model.KategoriaKopurua;
import utils.BiltegiLokala;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Datu-baseko estatistikak eta kopuru orokorrak lortzeko DAO klasea.
 *
 * @author Yeray Garrido
 */
public class EstadistikaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(EstadistikaDAO.class);

    /**
     * Biltegian dauden artikulu kopurua itzultzen du.
     *
     * @return Artikulu kopurua
     */
    public static int biltegianKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.biltegianKopurua();
        }
        return kontatuEgoera("aurkitua");
    }

    /**
     * Itzulitako artikulu kopurua itzultzen du.
     *
     * @return Itzulitako artikulu kopurua
     */
    public static int bueltatakoKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.bueltatakoKopurua();
        }
        return kontatuEgoera("bueltatua");
    }

    /**
     * Iraungitako artikulu kopurua itzultzen du.
     *
     * @return Iraungitako artikulu kopurua
     */
    public static int iraungituakKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.iraungituakKopurua();
        }
        return kontatuEgoera("iraungita");
    }

    /**
     * Hurrengo 30 egunetan iraungitzear dauden artikulu kopurua itzultzen du.
     *
     * @return Iraungitzear dauden artikulu kopurua
     */
    public static int iraungitzearKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.iraungitzearKopurua();
        }
        String sql = "SELECT COUNT(*) FROM ARTIKULUA "
                + "WHERE egoera = 'aurkitua' AND iraungitze_data BETWEEN CURDATE() AND DATE_ADD(CURDATE(), INTERVAL 30 DAY)";
        return kontatuSql(sql);
    }

    /**
     * Irekitako erreklamazio kopurua itzultzen du.
     *
     * @return Irekitako erreklamazio kopurua
     */
    public static int erreklamazioIrekiakKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.erreklamazioIrekiakKopurua();
        }
        return kontatuSql("SELECT COUNT(*) FROM ERREKLAMAZIOA WHERE errek_egoera = 'irekita'");
    }

    /**
     * Sistemako langile guztien kopurua itzultzen du.
     *
     * @return Langile kopurua
     */
    public static int langileKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.langileKopurua();
        }
        return kontatuSql("SELECT COUNT(*) FROM LANGILEA");
    }

    /**
     * Datu-baseko artikulu guztien kopurua itzultzen du.
     *
     * @return Artikulu guztien kopurua
     */
    public static int artikuluGuztienKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.artikuluGuztienKopurua();
        }
        return kontatuSql("SELECT COUNT(*) FROM ARTIKULUA");
    }

    /**
     * Kategorien kopurua itzultzen du.
     *
     * @return Kategoria kopurua
     */
    public static int kategoriaKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kategoriaKopurua();
        }
        return kontatuSql("SELECT COUNT(*) FROM KATEGORIA");
    }

    /**
     * Kokalekuen kopurua itzultzen du.
     *
     * @return Kokaleku kopurua
     */
    public static int kokalekuakKopurua() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kokalekuakKopurua();
        }
        return kontatuSql("SELECT COUNT(*) FROM KOKALEKUA");
    }

    /**
     * Azken 10 mugimenduen zerrenda itzultzen du, data deszendentearen arabera.
     *
     * @return Azken mugimenduak AzkenMugimendua zerrenda gisa
     */
    public static List<AzkenMugimendua> azkenMugimenduak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.getAzkenMugimenduak();
        }
        List<AzkenMugimendua> zerrenda = new ArrayList<>();
        String sql = "SELECT m.id_artikulua, m.deskribapena, m.data, "
                + "COALESCE(CONCAT(l.izena,' ',l.abizena), '—') AS langilea "
                + "FROM MUGIMENDUA m LEFT JOIN LANGILEA l ON m.id_langile = l.id_langile "
                + "ORDER BY m.data DESC LIMIT 10";
        try (Connection con = DBKonexioa.getKonexioa();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new AzkenMugimendua(
                        rs.getString("id_artikulua"),
                        rs.getString("deskribapena"),
                        rs.getString("data"),
                        rs.getString("langilea")));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "azkenMugimenduak: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Kategoria bakoitzak zenbat artikulu dituen lortzen du.
     *
     * @return Kategoria bakoitzaren izena eta artikulu kopurua
     */
    public static List<KategoriaKopurua> kategoriaKopuruak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kategoriaKopuruak();
        }
        List<KategoriaKopurua> zerrenda = new ArrayList<>();
        String sql = "SELECT k.izena, COUNT(a.id_artikulua) AS kop "
                + "FROM KATEGORIA k LEFT JOIN ARTIKULUA a ON k.id_kategoria = a.id_kategoria "
                + "GROUP BY k.id_kategoria, k.izena ORDER BY kop DESC";
        try (Connection con = DBKonexioa.getKonexioa();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new KategoriaKopurua(rs.getString("izena"), rs.getInt("kop")));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "kategoriaKopuruak: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Emandako egoera batean dauden artikuluak kontatzen ditu.
     *
     * @param egoera Bilatu beharreko artikuluaren egoera
     * @return Artikulu kopurua
     */
    private static int kontatuEgoera(String egoera) {
        String sql = "SELECT COUNT(*) FROM ARTIKULUA WHERE egoera = ?";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, egoera);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "kontatuEgoera: datu-baseko errorea", e);
        }
        return 0;
    }

    /**
     * SQL COUNT kontsulta bat exekutatzen du eta emaitza itzultzen du.
     *
     * @param sql Exekutatu beharreko SQL COUNT kontsulta
     * @return Kontatutako kopurua, edo 0 errorea bada
     */
    private static int kontatuSql(String sql) {
        try (Connection con = DBKonexioa.getKonexioa();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "kontatuSql: datu-baseko errorea", e);
        }
        return 0;
    }
}
