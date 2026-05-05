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
import utils.DBConexioa;
import utils.LogKudeatzailea;

/**
 * Langileen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 * Langileak datu-basetik lortzeko, langile berriak gehitzeko eta
 * autentifikatzeko metodoak ditu.
 *
 * @author Yeray Garrido
 */
public class LangileaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(LangileaDAO.class);

    /**
     * Langile guztiak itzultzen ditu datu-basetik (taulan erakusteko).
     */
    public static List<Langilea> getGuztiak() {
        List<Langilea> zerrenda = new ArrayList<>();
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, r.deskribapena AS rola "
                + "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola "
                + "ORDER BY l.id_langile";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Langilea l = new Langilea(
                        rs.getInt("id_langile"),
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("erabiltzailea"),
                        null
                );
                l.setRola(rs.getString("rola"));
                zerrenda.add(l);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Datu-basetik rol guztiak lortzen ditu [id, deskribapena] bikote gisa.
     *
     * @return Rol zerrenda, bakoitza String[]{id, deskribapena} gisa
     */
    public static ArrayList<String[]> getRolak() {
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
            LOG.log(Level.SEVERE, "getRolak: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Langile berria gordetzen du datu-basean pasahitza BCrypt bidez zifratuta.
     */
    public static boolean gehitu(String izena, String abizena, String erabiltzailea, String pasahitza, int idRola) {
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
            LOG.log(Level.SEVERE, "gehitu: datu-baseko errorea", e);
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
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, l.pasahitza_hash, r.deskribapena AS rola "
                + "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola "
                + "WHERE l.erabiltzailea = ?";

        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, erabiltzailea);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGordea = rs.getString("pasahitza_hash");

                    boolean pasahitzaZuzena = BCrypt.checkpw(pasahitza, hashGordea);
                    if (!pasahitzaZuzena) {
                        return null;
                    }

                    int id = rs.getInt("id_langile");
                    String iz = rs.getString("izena");
                    String ab = rs.getString("abizena");
                    String er = rs.getString("erabiltzailea");
                    String rol = rs.getString("rola");

                    Langilea langilea;
                    if ("Administratzailea".equals(rol)) {
                        langilea = new Administratzailea(id, iz, ab, er, hashGordea);
                    } else {
                        langilea = new Langilea(id, iz, ab, er, hashGordea);
                    }
                    langilea.setRola(rol);
                    return langilea;
                }
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "login: datu-baseko errorea", e);
        }
        return null;
    }
}
