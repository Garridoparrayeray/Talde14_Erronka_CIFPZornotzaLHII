package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Kategoria;
import utils.BiltegiLokala;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Kategorien datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class KategoriaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(KategoriaDAO.class);

    /**
     * Kategoria berria gordetzen du datu-basean.
     *
     * @param izena Kategoriaren izena
     * @return Ondo gorde bada true
     */
    public static boolean gehitu(String izena) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kategoriaGehitu(izena);
        }
        String sql = "INSERT INTO KATEGORIA (izena) VALUES (?)";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izena);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "gehitu: OK - {0}", izena);
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Kategoria baten izena eguneratzen du.
     *
     * @param id Kategoriaren identifikagailua
     * @param izenaOso Izen berria
     * @return Ondo eguneratu bada true
     */
    public static boolean aldatuIzena(int id, String izenaOso) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kategoriaAldatuIzena(id, izenaOso);
        }
        String sql = "UPDATE KATEGORIA SET izena = ? WHERE id_kategoria = ?";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izenaOso);
            ps.setInt(2, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "aldatuIzena: OK - id={0}, izena={1}", new Object[]{id, izenaOso});
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "aldatuIzena: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Kategoria bat ezabatzen du datu-basetik.
     *
     * @param id Ezabatu beharreko kategoriaren identifikagailua
     * @return Ondo ezabatu bada true
     */
    public static boolean ezabatu(int id) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.kategoriaEzabatu(id);
        }
        String sql = "DELETE FROM KATEGORIA WHERE id_kategoria = ?";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "ezabatu: OK - id={0}", id);
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ezabatu: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Datu-basetik kategoria guztiak lortzen ditu.
     *
     * @return Kategorien zerrenda
     */
    public static List<Kategoria> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.getKategoriak();
        }
        List<Kategoria> kategoriak = new ArrayList<>();
        String sql = "SELECT id_kategoria, izena FROM KATEGORIA";

        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                kategoriak.add(new Kategoria(rs.getInt("id_kategoria"), rs.getString("izena")));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: datu-baseko errorea", e);
        }
        return kategoriak;
    }
}
