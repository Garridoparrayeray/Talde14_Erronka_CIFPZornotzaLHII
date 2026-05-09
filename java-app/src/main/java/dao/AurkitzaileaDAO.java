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
 *
 * @author Yeray Garrido
 */
public class AurkitzaileaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(AurkitzaileaDAO.class);

    /**
     * Aurkitzailearen datuak gordetzen ditu artikuluarekin lotuta.
     *
     * @param idArtikulua  Aurkitzailearekin lotutako artikuluaren kodea
     * @param izena        Aurkitzailearen izena
     * @param abizena      Aurkitzailearen abizena
     * @param telefonoa    Aurkitzailearen telefonoa (hutsik bada null gordetzen da)
     * @param emaila       Aurkitzailearen helbide elektronikoa (hutsik bada null)
     * @param aurkipenLekua Objektua aurkitu zen lekua (hutsik bada null)
     * @return Ondo gorde bada true, bestela false
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
            if (telefonoa.isEmpty()) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, telefonoa);
            }
            if (emaila.isEmpty()) {
                ps.setNull(4, java.sql.Types.VARCHAR);
            } else {
                ps.setString(4, emaila);
            }
            if (aurkipenLekua.isEmpty()) {
                ps.setNull(5, java.sql.Types.VARCHAR);
            } else {
                ps.setString(5, aurkipenLekua);
            }
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
     * @param idArtikulua Aurkitzailea bilatu beharreko artikuluaren kodea
     * @return Aurkitzailea objektua, edo null ez bada existitzen
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
