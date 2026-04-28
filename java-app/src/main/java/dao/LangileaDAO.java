package dao;

import model.Administratzailea;
import model.Langilea;
import org.mindrot.jbcrypt.BCrypt;
import utils.DBConexioa;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LangileaDAO {

    /**
     * Erabiltzailea eta pasahitza egiaztatzen du.
     * @return Langilea (edo Administratzailea) ala null autentifikazioa huts egiten badu.
     */
    public static Langilea login(String erabiltzailea, String pasahitza) {
        String sql = "SELECT l.id_langile, l.izena, l.abizena, l.erabiltzailea, l.pasahitza_hash, r.deskribapena AS rola " +
                     "FROM LANGILEA l JOIN ROLA r ON l.id_rola = r.id_rola " +
                     "WHERE l.erabiltzailea = ?";

        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, erabiltzailea);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String hashGordea = rs.getString("pasahitza_hash");

                if (!BCrypt.checkpw(pasahitza, hashGordea)) return null;

                int id     = rs.getInt("id_langile");
                String iz  = rs.getString("izena");
                String ab  = rs.getString("abizena");
                String er  = rs.getString("erabiltzailea");
                String rol = rs.getString("rola");

                if ("Administratzailea".equals(rol)) {
                    return new Administratzailea(id, iz, ab, er, hashGordea);
                } else {
                    return new Langilea(id, iz, ab, er, hashGordea);
                }
            }
        } catch (SQLException e) {
            System.err.println("LangileaDAO.login errorea: " + e.getMessage());
        }
        return null;
    }
}
