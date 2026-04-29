package dao;

import model.Kategoria;
import utils.DBConexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class KategoriaDAO {

    public static List<Kategoria> getGuztiak() {
        List<Kategoria> zerrenda = new ArrayList<>();
        String sql = "SELECT id_kategoria, izena FROM KATEGORIA ORDER BY izena";

        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                zerrenda.add(new Kategoria(rs.getInt("id_kategoria"), rs.getString("izena")));
            }
        } catch (SQLException e) {
            System.err.println("KategoriaDAO.getGuztiak errorea: " + e.getMessage());
        }
        return zerrenda;
    }
}
