package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import utils.BiltegiLokala;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

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
     * @param idArtikulua  Eman beharreko artikuluaren kodea
     * @param nan          Jabearen NAN zenbakia
     * @param izena        Jabearen izena
     * @param abizena      Jabearen abizena
     * @param telefonoa    Harremanetarako telefonoa
     * @param emaila       Harremanetarako emaila
     * @param helbidea     Jabearen helbidea
     * @param oharrak      Emanaldiaren oharrak (hutsik bada null gordetzen da)
     * @param idLangile    Eragiketa kudeatzen duen langilearen IDa
     * @param dokumentuBidea Sinadura-dokumentuaren fitxategi-izena
     * @return Ondo joan bada true
     */
    public static boolean formalizatu(String idArtikulua, String nan, String izena,
            String abizena, String telefonoa, String emaila,
            String helbidea, String oharrak, int idLangile,
            String dokumentuBidea) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.formalizatu(idArtikulua, nan, izena, abizena, telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea);
        }
        Connection con = null;
        try {
            con = DBKonexioa.getKonexioa();
            con.setAutoCommit(false);

            int idHartzailea = lortuEdoSortuJabea(con, nan, izena, abizena, telefonoa, emaila, helbidea);
            if (idHartzailea <= 0) {
                con.rollback();
                return false;
            }

            // ARTIKULUA egoera trg_emanaldia_eguneratu_artikulua triggerrak aldatzen du
            String deskMug = "Artikulua " + izena + " " + abizena + "-ri eman zaio.";
            gordeEmanaldiaEtaMugimendua(con, idArtikulua, idHartzailea, deskMug, idLangile, oharrak, dokumentuBidea);

            con.commit();
            LOG.log(Level.INFO, "formalizatu: OK - artikulua={0}, nan={1}", new Object[]{idArtikulua, nan});
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
     * Emanaldia formalizatzen du erakundearekin (IFZ bidez).
     *
     * @param idArtikulua    Eman beharreko artikuluaren kodea
     * @param ift            Erakundearen IFZ zenbakia
     * @param izenOfiziala   Erakundearen izen ofiziala
     * @param telefonoa      Harremanetarako telefonoa
     * @param emaila         Harremanetarako emaila
     * @param helbidea       Erakundearen helbidea
     * @param oharrak        Emanaldiaren oharrak (hutsik bada null gordetzen da)
     * @param idLangile      Eragiketa kudeatzen duen langilearen IDa
     * @param dokumentuBidea Sinadura-dokumentuaren fitxategi-izena
     * @return Ondo joan bada true
     */
    public static boolean formalizatuErakundea(String idArtikulua, String ift,
            String izenOfiziala, String telefonoa, String emaila,
            String helbidea, String oharrak, int idLangile, String dokumentuBidea) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.formalizatuErakundea(idArtikulua, ift, izenOfiziala, telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea);
        }
        Connection con = null;
        try {
            con = DBKonexioa.getKonexioa();
            con.setAutoCommit(false);

            int idHartzailea = lortuEdoSortuErakundea(con, ift, izenOfiziala, telefonoa, emaila, helbidea);
            if (idHartzailea <= 0) {
                con.rollback();
                return false;
            }

            String deskMug = "Artikulua " + izenOfiziala + " erakundeari eman zaio.";
            gordeEmanaldiaEtaMugimendua(con, idArtikulua, idHartzailea, deskMug, idLangile, oharrak, dokumentuBidea);

            con.commit();
            LOG.log(Level.INFO, "formalizatuErakundea: OK - artikulua={0}, ift={1}", new Object[]{idArtikulua, ift});
            return true;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "formalizatuErakundea: datu-baseko errorea", e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    LOG.log(Level.SEVERE, "rollback errorea", ex);
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                    LOG.log(Level.WARNING, "itxiera errorea", ex);
                }
            }
        }
    }

    private static void gordeEmanaldiaEtaMugimendua(Connection con, String idArtikulua, int idHartzailea,
            String mugimenduDesk, int idLangile, String oharrak, String dokumentuBidea) throws SQLException {

        String sqlEm = "INSERT INTO EMANALDIA (emate_data, oharrak, dokumentu_bidea, id_artikulua, id_hartzailea, id_langile) "
                + "VALUES (CURDATE(), ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlEm)) {
            if (oharrak.isEmpty()) {
                ps.setNull(1, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, oharrak);
            }
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

        // ARTIKULUA egoera trg_emanaldia_eguneratu_artikulua triggerrak aldatzen du
        String sqlMug = "INSERT INTO MUGIMENDUA (deskribapena, id_artikulua, id_langile) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlMug)) {
            ps.setString(1, mugimenduDesk);
            ps.setString(2, idArtikulua);
            if (idLangile > 0) {
                ps.setInt(3, idLangile);
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            ps.executeUpdate();
        }
    }

    /**
     * IFZ bidez erakundea bilatzen du edo sortu egiten du.
     *
     * @return id_hartzailea, edo -1 errorea bada
     */
    private static int lortuEdoSortuErakundea(Connection con, String ift, String izenOfiziala,
            String telefonoa, String emaila, String helbidea) throws SQLException {
        String sqlBilatu = "SELECT id_hartzailea FROM ERAKUNDEA WHERE ift = ?";
        try (PreparedStatement ps = con.prepareStatement(sqlBilatu)) {
            ps.setString(1, ift);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_hartzailea");
            }
        }

        String sqlH = "INSERT INTO HARTZAILEA (telefonoa, emaila, helbidea) VALUES (?, ?, ?)";
        int idH;
        try (PreparedStatement ps = con.prepareStatement(sqlH, Statement.RETURN_GENERATED_KEYS)) {
            if (telefonoa.isEmpty()) {
                ps.setNull(1, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, telefonoa);
            }
            if (emaila.isEmpty()) {
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(2, emaila);
            }
            if (helbidea.isEmpty()) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, helbidea);
            }
            ps.executeUpdate();
            ResultSet gen = ps.getGeneratedKeys();
            if (!gen.next()) {
                return -1;
            }
            idH = gen.getInt(1);
        }

        String sqlE = "INSERT INTO ERAKUNDEA (id_hartzailea, ift, izen_ofiziala) VALUES (?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sqlE)) {
            ps.setInt(1, idH);
            ps.setString(2, ift);
            ps.setString(3, izenOfiziala);
            ps.executeUpdate();
        }

        return idH;
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
            if (telefonoa.isEmpty()) {
                ps.setNull(1, java.sql.Types.VARCHAR);
            } else {
                ps.setString(1, telefonoa);
            }
            if (emaila.isEmpty()) {
                ps.setNull(2, java.sql.Types.VARCHAR);
            } else {
                ps.setString(2, emaila);
            }
            if (helbidea.isEmpty()) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, helbidea);
            }
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
