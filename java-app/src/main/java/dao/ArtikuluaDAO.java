package dao;

import model.Artikulua;
import model.EgoeraArtikulua;
import model.Kategoria;
import model.Kokalekua;
import utils.DBConexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArtikuluaDAO {

    public static List<Artikulua> getGuztiak() {
        List<Artikulua> zerrenda = new ArrayList<>();

        String sql = "SELECT a.id_artikulua, a.a_izena, a.a_deskribapena, a.egoera, " +
                     "a.sarrera_data, a.iraungitze_data, a.argazkia, a.iragankorra, " +
                     "k.id_kategoria, k.izena AS kat_izena, " +
                     "ko.id_kokalekua, ko.armairua, ko.apala, ko.bha_da " +
                     "FROM ARTIKULUA a " +
                     "LEFT JOIN KATEGORIA k  ON a.id_kategoria  = k.id_kategoria " +
                     "LEFT JOIN KOKALEKUA ko ON a.id_kokalekua = ko.id_kokalekua";

        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Artikulua a = new Artikulua(
                    rs.getString("id_artikulua"),
                    rs.getString("a_izena"),
                    rs.getString("a_deskribapena"),
                    null, null,
                    rs.getDate("sarrera_data"),
                    rs.getString("argazkia")
                );

                String egoeraStr = rs.getString("egoera");
                if (egoeraStr != null) {
                    switch (egoeraStr) {
                        case "aurkitua"   -> a.aldatuEgoera(EgoeraArtikulua.BILTEGIAN);
                        case "bueltatua"  -> a.aldatuEgoera(EgoeraArtikulua.ITZULITA);
                        case "artxibatua" -> a.aldatuEgoera(EgoeraArtikulua.IRAUNGITA);
                        case "iraungita"  -> a.aldatuEgoera(EgoeraArtikulua.IRAUNGITA);
                    }
                }

                if (rs.getInt("id_kategoria") != 0) {
                    a.setKategoria(new Kategoria(rs.getInt("id_kategoria"), rs.getString("kat_izena")));
                }

                if (rs.getInt("id_kokalekua") != 0) {
                    a.setKokalekua(new Kokalekua(
                        rs.getString("armairua"),
                        rs.getString("apala"),
                        rs.getBoolean("bha_da")
                    ));
                }

                zerrenda.add(a);
            }
        } catch (SQLException e) {
            System.err.println("ArtikuluaDAO.getGuztiak errorea: " + e.getMessage());
        }
        return zerrenda;
    }
}
