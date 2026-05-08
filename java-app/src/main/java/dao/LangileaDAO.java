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
import utils.BiltegiLocala;
import utils.DBConexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Langileen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Eder Martin Langileak datu-basetik lortzeko, langile berriak
 * gehitzeko eta autentifikatzeko metodoak ditu.
 */
public class LangileaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(LangileaDAO.class);

    /**
     * Langile guztiak itzultzen ditu datu-basetik (taulan erakusteko).
     */
    public static List<Langilea> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.getLangileak();
        }
        List<Langilea> zerrenda = new ArrayList<>();
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, l.pasahitza_hash, r.deskribapena AS rola "
                + "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola "
                + "ORDER BY l.id_langile";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
    public static ArrayList<String[]> getRolak() {
        if (ModoKudeatzailea.isOffline()) {
            return new ArrayList<>(BiltegiLocala.getRolak());
        }
        ArrayList<String[]> zerrenda = new ArrayList<>();
        String sql = "SELECT id_rola, deskribapena FROM ROLA ORDER BY id_rola";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
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
     */
    public static boolean gehitu(String izena, String abizena, String erabiltzailea, String pasahitza, int idRola) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.langileaGehitu(izena, abizena, erabiltzailea, pasahitza, idRola);
        }
        String hash = BCrypt.hashpw(pasahitza, BCrypt.gensalt(10));
        String sql = "INSERT INTO LANGILEA (izena, abizena, erabiltzailea, pasahitza_hash, id_rola) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izena);
            ps.setString(2, abizena);
            ps.setString(3, erabiltzailea);
            ps.setString(4, hash);
            ps.setInt(5, idRola);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: errorea", e);
            return false;
        }
    }

    /**
     * Erabiltzailea eta pasahitza egiaztatzen du.
     *
     * @return Langilea (edo Administratzailea) ala null autentifikazioa huts
     * egiten badu.
     */
    public static Langilea login(String erabiltzailea, String pasahitza) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.login(erabiltzailea, pasahitza);
        }
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, l.pasahitza_hash, r.deskribapena AS rola "
                + "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola "
                + "WHERE l.erabiltzailea = ?";

        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {

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

    public static boolean eguneratu(int id, String izena, String abizena, String erabiltzailea, int idRola, String pasahitzaBerria) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.langileaEguneratu(id, izena, abizena, erabiltzailea, idRola, pasahitzaBerria);
        }
        StringBuilder sql = new StringBuilder("UPDATE LANGILEA SET izena=?, abizena=?, erabiltzailea=?, id_rola=?");
        boolean pasahitzaAldatu = pasahitzaBerria != null && !pasahitzaBerria.isEmpty();
        if (pasahitzaAldatu) {
            sql.append(", pasahitza_hash=?");
        }
        sql.append(" WHERE id_langile=?");

        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
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
            return ps.executeUpdate() > 0;
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
            return BiltegiLocala.langileaEzabatu(id);
        }
        String sql = "DELETE FROM LANGILEA WHERE id_langile = ?";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ezabatu: errorea", e);
            return false;
        }
    }

}
