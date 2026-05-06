package dao;

import model.Kokalekua;
import utils.DBConexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Kokalekuen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class KokalekuaDAO {

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
            System.err.println("KokalekuaDAO.getGuztiak: " + e.getMessage());
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
            System.err.println("KokalekuaDAO.getZerrenda: " + e.getMessage());
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
            System.err.println("KokalekuaDAO.gehitu: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kokaleku baten datuak eguneratzen ditu datu-basean.
     */
    public static boolean eguneratu(int id, String armairua, String apala, boolean bhaDa) {
        String sql = "UPDATE KOKALEKUA SET armairua=?, apala=?, bha_da=? WHERE id_kokalekua=?";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, armairua);
            ps.setString(2, apala);
            ps.setBoolean(3, bhaDa);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("KokalekuaDAO.eguneratu: " + e.getMessage());
            return false;
        }
    }

    /**
     * Kokaleku bat datu-basetik ezabatzen du.
     */
    public static boolean ezabatu(int id) {
        String sql = "DELETE FROM KOKALEKUA WHERE id_kokalekua = ?";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("KokalekuaDAO.ezabatu: " + e.getMessage());
            return false;
        }
    }
}
