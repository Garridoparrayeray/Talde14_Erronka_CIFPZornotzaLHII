package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Aurkitzailea;
import utils.BiltegiLocala;
import utils.DBConexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * AURKITZAILEA taulako eragiketak kudeatzen dituen DAO klasea.
 */
public class AurkitzaileaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(AurkitzaileaDAO.class);

    /**
     * Aurkitzailearen datuak gordetzen ditu artikuluarekin lotuta.
     *
     * @return Ondo gorde bada true
     */
    public static boolean gehitu(String idArtikulua, String izena, String abizena,
            String telefonoa, String emaila, String aurkipenLekua) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.aurkitzaileaGehitu(idArtikulua, izena, abizena, telefonoa, emaila, aurkipenLekua);
        }
        String sql = "INSERT INTO AURKITZAILEA (izena, abizena, telefonoa, emaila, aurkipen_lekua, id_artikulua) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, izena);
            ps.setString(2, abizena);
            ps.setString(3, telefonoa.isEmpty() ? null : telefonoa);
            ps.setString(4, emaila.isEmpty() ? null : emaila);
            ps.setString(5, aurkipenLekua.isEmpty() ? null : aurkipenLekua);
            ps.setString(6, idArtikulua);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Artikulu baten aurkitzailearen datuak itzultzen ditu.
     *
     * @return Aurkitzailea edo null ez bada
     */
    public static Aurkitzailea getByArtikulua(String idArtikulua) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.getAurkitzaileaByArtikulua(idArtikulua);
        }
        String sql = "SELECT * FROM AURKITZAILEA WHERE id_artikulua = ? LIMIT 1";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idArtikulua);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Aurkitzailea a = new Aurkitzailea(
                        rs.getString("izena"),
                        rs.getString("abizena"),
                        rs.getString("telefonoa"),
                        rs.getString("emaila"),
                        rs.getString("aurkipen_lekua"),
                        idArtikulua
                );
                a.setAurkitzaileaId(rs.getInt("id_aurkitzailea"));
                return a;
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getByArtikulua: datu-baseko errorea", e);
        }
        return null;
    }
}
