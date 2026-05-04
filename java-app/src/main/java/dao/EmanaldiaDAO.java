package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import utils.DBConexioa;

/**
 * Emanaldien datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class EmanaldiaDAO {

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

            // 1. Jabea bilatu NAN bidez edo sortu berria
            int idHartzailea = lortuEdoSortuJabea(con, nan, izena, abizena, telefonoa, emaila, helbidea);
            if (idHartzailea <= 0) {
                con.rollback();
                return false;
            }

            // 2. EMANALDIA txertatu
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

            // 3. ARTIKULUA egoera eguneratu → bueltatua
            String sqlUp = "UPDATE ARTIKULUA SET egoera = 'bueltatua' WHERE id_artikulua = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlUp)) {
                ps.setString(1, idArtikulua);
                ps.executeUpdate();
            }

            // 4. MUGIMENDUA txertatu (auditoria)
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
            System.err.println("EmanaldiaDAO.formalizatu: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Rollback errorea: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    System.err.println("Konexio itxiera errorea: " + ex.getMessage());
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
        // NAN bidez bilatu
        String sqlBilatu = "SELECT id_hartzailea FROM JABEA WHERE nan = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBilatu)) {
            ps.setString(1, nan);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_hartzailea");
            }
        }

        // Ez badago, HARTZAILEA sortu
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

        // JABEA sortu
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
