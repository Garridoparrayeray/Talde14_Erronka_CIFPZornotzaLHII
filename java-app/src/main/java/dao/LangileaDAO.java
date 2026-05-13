package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import model.Administratzailea;
import model.Langilea;
import utils.BiltegiLokala;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Langileen datu-baseko eragiketak kudeatzen dituen DAO klasea. Langileak
 * lortzeko, gehitzeko, eguneratzeko, ezabatzeko eta autentifikatzeko metodoak
 * eskaintzen ditu.
 *
 * @author Eder Martin
 */
public class LangileaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(LangileaDAO.class);

    /**
     * Langile guztiak itzultzen ditu datu-basetik (taulan erakusteko).
     *
     * @return Langile guztien zerrenda; hutsik egon daiteke
     */
    public static List<Langilea> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.getLangileak();
        }
        List<Langilea> zerrenda = new ArrayList<>();
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, l.pasahitza_hash, r.deskribapena AS rola "
                + "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola "
                + "ORDER BY l.id_langile";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Langilea l = new Langilea(
                        rs.getInt("id_langile"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("erabiltzailea"),
                        rs.getString("pasahitza_hash")
                );
                l.setRola(rs.getString("rola"));
                zerrenda.add(l);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: errorea", e);
        }
        return zerrenda;
    }

    /**
     * Datu-basetik rol guztiak lortzen ditu [id, deskribapena] bikote gisa.
     *
     * @return Rol zerrenda, bakoitza String[]{id, deskribapena} gisa
     */
    public static List<String[]> getRolak() {
        if (ModoKudeatzailea.isOffline()) {
            return new ArrayList<>(BiltegiLokala.getRolak());
        }
        List<String[]> zerrenda = new ArrayList<>();
        String sql = "SELECT id_rola, deskribapena FROM ROLA ORDER BY id_rola";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new String[]{
                    rs.getString("id_rola"),
                    rs.getString("deskribapena")
                });
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getRolak: errorea", e);
        }
        return zerrenda;
    }

    /**
     * Langile berria gordetzen du datu-basean pasahitza BCrypt bidez zifratuta.
     *
     * @param izena Langilearen izena
     * @param abizena Langilearen abizena
     * @param erabiltzailea Erabiltzaile-izena (bakarra izan behar da)
     * @param pasahitza Argizko pasahitza (hash eginda gordeko da)
     * @param idRola Langileari esleitu beharreko rolaren IDa
     * @return Ondo gorde bada true, bestela false
     */
    public static boolean gehitu(String izena, String abizena, String erabiltzailea, String pasahitza, int idRola) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.langileaGehitu(izena, abizena, erabiltzailea, pasahitza, idRola);
        }
        String hash = BCrypt.hashpw(pasahitza, BCrypt.gensalt(10));
        String sql = "INSERT INTO LANGILEA (izena, abizena, erabiltzailea, pasahitza_hash, id_rola) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izena);
            ps.setString(2, abizena);
            ps.setString(3, erabiltzailea);
            ps.setString(4, hash);
            ps.setInt(5, idRola);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "gehitu: OK - {0}", erabiltzailea);
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: errorea", e);
            return false;
        }
    }

    /**
     * Erabiltzailea eta pasahitza egiaztatzen ditu eta dagokion Langilea
     * objektua itzultzen du.
     *
     * @param erabiltzailea Saioa hasteko erabiltzaile-izena
     * @param pasahitza Argizko pasahitza BCrypt bidez egiaztatuko dena
     * @return Langilea (edo Administratzailea) ondo autentifikatu bada, null
     * bestela
     */
    public static Langilea login(String erabiltzailea, String pasahitza) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.login(erabiltzailea, pasahitza);
        }
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, l.pasahitza_hash, r.deskribapena AS rola "
                + "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola "
                + "WHERE l.erabiltzailea = ?";

        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, erabiltzailea);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGordea = rs.getString("pasahitza_hash");

                    // Pasahitza egiaztatu BCrypt-ekin
                    boolean pasahitzaZuzena = BCrypt.checkpw(pasahitza, hashGordea);
                    if (!pasahitzaZuzena) {
                        return null;
                    }

                    int id = rs.getInt("id_langile");
                    String iz = rs.getString("izena");
                    String ab = rs.getString("abizena");
                    String er = rs.getString("erabiltzailea");
                    String rol = rs.getString("rola");

                    // Rola egiaztatu eta langile mota egokia itzuli
                    Langilea langilea;
                    if ("Administratzailea".equals(rol)) {
                        langilea = new Administratzailea(id, iz, ab, er, hashGordea);
                    } else {
                        langilea = new Langilea(id, iz, ab, er, hashGordea);
                    }
                    // DB-ko rola objektuan gorde (ez hardcodeatu kontroladorean)
                    langilea.setRola(rol);
                    return langilea;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "login: errorea", e);
        }
        return null;
    }

    /**
     * Langilea baten datuak eguneratzen ditu datu-basean.
     *
     * @param id Langilearen identifikatzailea
     * @param izena Langilearen izen berria
     * @param abizena Langilearen abizen berria
     * @param erabiltzailea Erabiltzaile izen berria
     * @param idRola Rolaren ID berria
     * @return true ondo eguneratu bada, false bestela
     */
    public static boolean eguneratu(int id, String izena, String abizena, String erabiltzailea, int idRola) {
        return eguneratu(id, izena, abizena, erabiltzailea, idRola, null);
    }

    /**
     * Langilearen datuak eguneratzen ditu, aukeran pasahitza ere aldatuz.
     *
     * @param id Langilearen identifikatzailea
     * @param izena Langilearen izen berria
     * @param abizena Langilearen abizen berria
     * @param erabiltzailea Erabiltzaile izen berria
     * @param idRola Rolaren ID berria
     * @param pasahitzaBerria Pasahitz berria (null edo hutsa bada, ez da
     * aldatzen)
     * @return true ondo eguneratu bada, false bestela
     */
    public static boolean eguneratu(int id, String izena, String abizena, String erabiltzailea, int idRola, String pasahitzaBerria) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.langileaEguneratu(id, izena, abizena, erabiltzailea, idRola, pasahitzaBerria);
        }
        StringBuilder sql = new StringBuilder("UPDATE LANGILEA SET izena=?, abizena=?, erabiltzailea=?, id_rola=?");
        boolean pasahitzaAldatu = pasahitzaBerria != null && !pasahitzaBerria.isEmpty();
        if (pasahitzaAldatu) {
            sql.append(", pasahitza_hash=?");
        }
        sql.append(" WHERE id_langile=?");

        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            ps.setString(1, izena);
            ps.setString(2, abizena);
            ps.setString(3, erabiltzailea);
            ps.setInt(4, idRola);
            int idx = 5;
            if (pasahitzaAldatu) {
                String hash = BCrypt.hashpw(pasahitzaBerria, BCrypt.gensalt(10));
                ps.setString(idx++, hash);
            }
            ps.setInt(idx, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "eguneratu: OK - id={0}", id);
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "eguneratu: errorea", e);
            return false;
        }
    }

    /**
     * Langilea datu-basetik ezabatzen du.
     *
     * @param id Ezabatu beharreko langilearen IDa
     * @return true ondo ezabatu bada, false bestela
     */
    public static boolean ezabatu(int id) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.langileaEzabatu(id);
        }
        String sql = "DELETE FROM LANGILEA WHERE id_langile = ?";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "ezabatu: OK - id={0}", id);
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ezabatu: errorea", e);
            return false;
        }
    }

}
