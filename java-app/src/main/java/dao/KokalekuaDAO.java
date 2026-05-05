package dao;

import model.Kokalekua;
import utils.DBConexioa;
import utils.LogKudeatzailea;

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
     */
    public static List<Kokalekua> getGuztiak() {
        List<Kokalekua> zerrenda = new ArrayList<>();
        String sql = "SELECT k.id_kokalekua, k.armairua, k.apala, k.bha_da, "
                + "COUNT(a.id_artikulua) AS kop "
                + "FROM KOKALEKUA k "
                + "LEFT JOIN ARTIKULUA a ON k.id_kokalekua = a.id_kokalekua "
                + "GROUP BY k.id_kokalekua, k.armairua, k.apala, k.bha_da "
                + "ORDER BY k.id_kokalekua";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kokalekua k = new Kokalekua(
                        rs.getString("armairua"),
                        rs.getString("apala"),
                        rs.getBoolean("bha_da")
                );
                k.setKokalekuId(rs.getInt("id_kokalekua"));
                k.setArtikuluKopurua(rs.getInt("kop"));
                zerrenda.add(k);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Kokaleku guztiak Kokalekua objektu gisa itzultzen ditu (ComboBox-erako).
     */
    public static List<Kokalekua> getZerrenda() {
        List<Kokalekua> zerrenda = new ArrayList<Kokalekua>();
        String sql = "SELECT id_kokalekua, armairua, apala, bha_da FROM KOKALEKUA ORDER BY id_kokalekua";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kokalekua k = new Kokalekua(rs.getString("armairua"), rs.getString("apala"), rs.getBoolean("bha_da"));
                k.setKokalekuId(rs.getInt("id_kokalekua"));
                zerrenda.add(k);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getZerrenda: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Kokaleku berria gordetzen du datu-basean.
     */
    public static boolean gehitu(String armairua, String apala, boolean bhaDa) {
        String sql = "INSERT INTO KOKALEKUA (armairua, apala, bha_da) VALUES (?, ?, ?)";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, armairua);
            ps.setString(2, apala);
            ps.setBoolean(3, bhaDa);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: datu-baseko errorea", e);
            return false;
        }
    }
}
