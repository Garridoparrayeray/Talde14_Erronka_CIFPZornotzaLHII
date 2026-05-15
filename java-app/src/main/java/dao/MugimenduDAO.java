package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.MugimenduLerroa;
import utils.BiltegiLokala;
import utils.DBKonexioa;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Mugimenduen (auditoria) datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class MugimenduDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(MugimenduDAO.class);

    /**
     * Mugimendua berria txertatzen du auditoria taulan.
     *
     * @param deskribapena Ekintzaren deskribapena
     * @param idArtikulua  Lotutako artikuluaren kodea
     * @param idLangile    Langilearen IDa (0 bada sistema-ekintza)
     * @return Ondo txertatu bada true
     */
    public static boolean gehitu(String deskribapena, String idArtikulua, int idLangile) {
        if (ModoKudeatzailea.isOffline()) {
            return false;
        }
        String sql = "INSERT INTO MUGIMENDUA (deskribapena, id_artikulua, id_langile) VALUES (?, ?, ?)";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, deskribapena);
            ps.setString(2, idArtikulua);
            if (idLangile > 0) {
                ps.setInt(3, idLangile);
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }
            boolean ok = ps.executeUpdate() > 0;
            if (ok) {
                LOG.log(Level.INFO, "gehitu: OK - artikulua={0}", idArtikulua);
            }
            return ok;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Mugimenduen erregistro guztiak itzultzen ditu auditoria taulako ordena
     * deszendentearekin.
     *
     * @return MugimenduLerroa objektuen zerrenda, denbora-ordenan beherantz;
     *         hutsik egon daiteke
     */
    public static List<MugimenduLerroa> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLokala.getMugimenduak();
        }
        List<MugimenduLerroa> zerrenda = new ArrayList<>();
        String sql = "SELECT m.data, "
                + "COALESCE(CONCAT(l.izena, ' ', l.abizena), '—') AS langilea, "
                + "m.deskribapena, "
                + "COALESCE(m.id_artikulua, '—') AS artikulua "
                + "FROM MUGIMENDUA m "
                + "LEFT JOIN LANGILEA l ON m.id_langile = l.id_langile "
                + "ORDER BY m.data DESC";
        try (Connection con = DBKonexioa.getKonexioa();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                zerrenda.add(new MugimenduLerroa(
                        rs.getString("data"),
                        rs.getString("langilea"),
                        rs.getString("deskribapena"),
                        rs.getString("artikulua")));
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: datu-baseko errorea", e);
        }
        return zerrenda;
    }
}
