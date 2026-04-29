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