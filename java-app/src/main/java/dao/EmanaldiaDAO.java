package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.DBConexioa;
import utils.LogKudeatzailea;

/**
 * Emanaldien datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class EmanaldiaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(EmanaldiaDAO.class);

    /**
     * Emanaldia formalizatzen du: hartzailea sortu/bilatu, emanaldia gorde,
     * artikuluaren egoera eguneratu eta mugimendua erregistratu.
     *
     * @return Ondo joan bada true
     */
    public static boolean formalizatu(String idArtikulua, String nan, String izena,
            String abizena, String telefonoa, String emaila,
            String helbidea, String oharrak, int idLangile,
            String dokumentuBidea) {
        Connection con = null;
        try {
            con = DBConexioa.getKonexioa();
            con.setAutoCommit(false);

            int idHartzailea = lortuEdoSortuJabea(con, nan, izena, abizena, telefonoa, emaila, helbidea);
            if (idHartzailea <= 0) {
                con.rollback();
                return false;
            }

            String sqlEm = "INSERT INTO EMANALDIA (emate_data, oharrak, dokumentu_bidea, id_artikulua, id_hartzailea, id_langile) "
                    + "VALUES (CURDATE(), ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlEm)) {
                ps.setString(1, oharrak.isEmpty() ? null : oharrak);
                ps.setString(2, dokumentuBidea);
                ps.setString(3, idArtikulua);
                ps.setInt(4, idHartzailea);
                if (idLangile > 0) {
                    ps.setInt(5, idLangile);
                } else {
                    ps.setNull(5, java.sql.Types.INTEGER);
                }
                ps.executeUpdate();
            }

            String sqlUp = "UPDATE ARTIKULUA SET egoera = 'bueltatua' WHERE id_artikulua = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlUp)) {
                ps.setString(1, idArtikulua);
                ps.executeUpdate();
            }

            String deskMug = "Artikulua " + izena + " " + abizena + "-ri eman zaio.";
            String sqlMug = "INSERT INTO MUGIMENDUA (deskribapena, id_artikulua, id_langile) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlMug)) {
                ps.setString(1, deskMug);
                ps.setString(2, idArtikulua);
                if (idLangile > 0) {
                    ps.setInt(3, idLangile);
                } else {
                    ps.setNull(3, java.sql.Types.INTEGER);
                }
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "formalizatu: datu-baseko errorea", e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "formalizatu: rollback errorea", ex);
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    LOG.log(Level.WARNING, "formalizatu: konexio itxiera errorea", ex);
                }
            }
        }
    }

    /**
     * NAN bidez jabea bilatzen du edo, ez badago, sortu egiten du HARTZAILEA
     * eta JABEA tauletan.
     *
     * @return Jaberen id_hartzailea, edo -1 errorea bada
     */
    private static int lortuEdoSortuJabea(Connection con, String nan, String izena,
            String abizena, String telefonoa,
            String emaila, String helbidea) throws SQLException {
        String sqlBilatu = "SELECT id_hartzailea FROM JABEA WHERE nan = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBilatu)) {
            ps.setString(1, nan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_hartzailea");
            }
        }

        String sqlH = "INSERT INTO HARTZAILEA (telefonoa, emaila, helbidea) VALUES (?, ?, ?)";
        int idH;
        try (PreparedStatement ps = con.prepareStatement(sqlH, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, telefonoa.isEmpty() ? null : telefonoa);
            ps.setString(2, emaila.isEmpty() ? null : emaila);
            ps.setString(3, helbidea.isEmpty() ? null : helbidea);
            ps.executeUpdate();
            ResultSet gen = ps.getGeneratedKeys();
            if (!gen.next()) {
                return -1;
            }
            idH = gen.getInt(1);
        }

        String sqlJ = "INSERT INTO JABEA (id_hartzailea, nan, izena, abizena) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlJ)) {
            ps.setInt(1, idH);
            ps.setString(2, nan);
            ps.setString(3, izena);
            ps.setString(4, abizena);
            ps.executeUpdate();
        }

        return idH;
    }
}
