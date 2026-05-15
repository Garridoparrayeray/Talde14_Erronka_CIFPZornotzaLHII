package dao;

import model.Kokalekua;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;
import utils.BiltegiLokala;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Kokalekuen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class KokalekuaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(KokalekuaDAO.class);

    /**
     * Datu-basetik kokaleku guztiak lortzen ditu, bakoitzeko artikulu
     * kopuruarekin.
     *
     * @return Kokalekua objektuen zerrenda; hutsik egon daiteke
     */
    public static List<Kokalekua> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.getKokalekuak();
        }
        List<Kokalekua> zerrenda = new ArrayList<>();
        String sql = "SELECT k.id_kokalekua, k.armairua, k.apala, k.bha_da, "
                + "COUNT(a.id_artikulua) AS kop "
                + "FROM KOKALEKUA k "
                + "LEFT JOIN ARTIKULUA a ON k.id_kokalekua = a.id_kokalekua "
                + "GROUP BY k.id_kokalekua, k.armairua, k.apala, k.bha_da "
                + "ORDER BY k.id_kokalekua";
        try (Connection con = DBKonexioa.getKonexioa();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kokalekua k = new Kokalekua(
                        rs.getString("armairua"),
                        rs.getString("apala"),
                        rs.getBoolean("bha_da"));
                k.setKokalekuId(rs.getInt("id_kokalekua"));
                k.setArtikuluKopurua(rs.getInt("kop"));
                zerrenda.add(k);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: errorea", e);
        }
        return zerrenda;
    }

    /**
     * Kokaleku guztiak Kokalekua objektu gisa itzultzen ditu (ComboBox-erako),
     * artikulu kopururik gabe.
     *
     * @return Kokalekua objektuen zerrenda; hutsik egon daiteke
     */
    public static List<Kokalekua> getZerrenda() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.getKokalekuakZerrenda();
        }
        List<Kokalekua> zerrenda = new ArrayList<>();
        String sql = "SELECT id_kokalekua, armairua, apala, bha_da FROM KOKALEKUA ORDER BY id_kokalekua";
        try (Connection con = DBKonexioa.getKonexioa();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kokalekua k = new Kokalekua(rs.getString("armairua"), rs.getString("apala"), rs.getBoolean("bha_da"));
                k.setKokalekuId(rs.getInt("id_kokalekua"));
                zerrenda.add(k);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getZerrenda: errorea", e);
        }
        return zerrenda;
    }

    /**
     * Kokaleku berria gordetzen du datu-basean.
     *
     * @param armairua Armairuaren kodea (letra larriz normalizatua)
     * @param apala    Apalaren identifikatzailea
     * @param bhaDa    true bada Bolumen Handiko Armairua
     * @return Ondo gorde bada true, bestela false
     */
    public static boolean gehitu(String armairua, String apala, boolean bhaDa) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kokalekuaGehitu(armairua, apala, bhaDa);
        }
        String sql = "INSERT INTO KOKALEKUA (armairua, apala, bha_da) VALUES (?, ?, ?)";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, armairua);
            ps.setString(2, apala);
            ps.setBoolean(3, bhaDa);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "gehitu: OK - {0}-{1}", new Object[] { armairua, apala });
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: errorea", e);
            return false;
        }
    }

    /**
     * Kokaleku baten datuak eguneratzen ditu datu-basean.
     *
     * @param id       Eguneratu beharreko kokalekuaren IDa
     * @param armairua Armairuaren kode berria
     * @param apala    Apalaren identifikatzaile berria
     * @param bhaDa    true bada Bolumen Handiko Armairua
     * @return Ondo eguneratu bada true, bestela false
     */
    public static boolean eguneratu(int id, String armairua, String apala, boolean bhaDa) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kokalekuaEguneratu(id, armairua, apala, bhaDa);
        }
        String sql = "UPDATE KOKALEKUA SET armairua=?, apala=?, bha_da=? WHERE id_kokalekua=?";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, armairua);
            ps.setString(2, apala);
            ps.setBoolean(3, bhaDa);
            ps.setInt(4, id);
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
     * Kokaleku bat datu-basetik ezabatzen du.
     *
     * @param id Ezabatu beharreko kokalekuaren IDa
     * @return Ondo ezabatu bada true, bestela false
     */
    public static boolean ezabatu(int id) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kokalekuaEzabatu(id);
        }
        String sql = "DELETE FROM KOKALEKUA WHERE id_kokalekua = ?";
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
