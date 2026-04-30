package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Kategoria;
import utils.DBConexioa;

/**
 * Kategorien datu-baseko eragiketak kudeatzen dituen DAO klasea.
 * @author Yeray Garrido
 */
public class KategoriaDAO {

    /**
     * Datu-basetik kategoria guztiak lortzen ditu.
     * @return Kategorien zerrenda
     */
    /**
     * Kategoria berria gordetzen du datu-basean.
     */
    public static boolean gehitu(String izena) {
        String sql = "INSERT INTO KATEGORIA (izena) VALUES (?)";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izena);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("KategoriaDAO.gehitu: " + e.getMessage());
            return false;
        }
    }

    public static boolean aldatuIzena(int id, String izenaOso) {
        String sql = "UPDATE KATEGORIA SET izena = ? WHERE id_kategoria = ?";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izenaOso);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("KategoriaDAO.aldatuIzena: " + e.getMessage());
            return false;
        }
    }

    public static boolean ezabatu(int id) {
        String sql = "DELETE FROM KATEGORIA WHERE id_kategoria = ?";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("KategoriaDAO.ezabatu: " + e.getMessage());
            return false;
        }
    }

    public static List<Kategoria> getGuztiak() {
        List<Kategoria> kategoriak = new ArrayList<>();
        String sql = "SELECT id_kategoria, izena FROM KATEGORIA";

        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Kategoria k = new Kategoria(rs.getInt("id_kategoria"), rs.getString("izena"));
                kategoriak.add(k);
            }
        } catch (SQLException e) {
            System.err.println("Errorea KategoriaDAO.getGuztiak: " + e.getMessage());
        }
        return kategoriak;
    }
}