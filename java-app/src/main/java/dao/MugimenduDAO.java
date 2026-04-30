package dao;

import utils.DBConexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mugimenduen (auditoria) datu-baseko eragiketak kudeatzen dituen DAO klasea.
 */
public class MugimenduDAO {

    /**
     * Mugimenduen erregistro guztiak itzultzen ditu auditoria taulako ordena deszendentearekin.
     * @return String[][] matrizea: [data, langilea, ekintza, artikulua]
     */
    public static List<String[]> getGuztiak() {
        List<String[]> zerrenda = new ArrayList<String[]>();
        String sql = "SELECT m.data, " +
                     "COALESCE(CONCAT(l.izena, ' ', l.abizena), '—') AS langilea, " +
                     "m.deskribapena, " +
                     "COALESCE(m.id_artikulua, '—') AS artikulua " +
                     "FROM MUGIMENDUA m " +
                     "LEFT JOIN LANGILEA l ON m.id_langile = l.id_langile " +
                     "ORDER BY m.data DESC";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new String[]{
                    rs.getString("data"),
                    rs.getString("langilea"),
                    rs.getString("deskribapena"),
                    rs.getString("artikulua")
                });
            }
        } catch (SQLException e) {
            System.err.println("MugimenduDAO.getGuztiak: " + e.getMessage());
        }
        return zerrenda;
    }
}
