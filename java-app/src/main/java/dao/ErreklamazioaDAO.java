package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.EgoeraErreklamazioa;
import model.Erreklamazioa;
import utils.BiltegiLocala;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Erreklamazioen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class ErreklamazioaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(ErreklamazioaDAO.class);

    /**
     * Datu-basetik erreklamazio guztiak lortzen ditu objektu bidez mapatuta.
     *
     * @return Erreklamazioen zerrenda
     */
    public static List<Erreklamazioa> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.getErreklamazioak();
        }
        List<Erreklamazioa> erreklamazioak = new ArrayList<>();

        String sql = "SELECT e.id_erreklamazio, e.erreklamazio_data, e.deskribapen_bilatua, e.errek_egoera, "
                + "j.nan, j.izena AS jabe_izena, j.abizena AS jabe_abizena, h.telefonoa, h.emaila, "
                + "k.id_kategoria, k.izena AS kategoria_izena "
                + "FROM ERREKLAMAZIOA e "
                + "LEFT JOIN HARTZAILEA h ON e.id_hartzailea = h.id_hartzailea "
                + "LEFT JOIN JABEA j ON h.id_hartzailea = j.id_hartzailea "
                + "LEFT JOIN KATEGORIA k ON e.id_kategoria = k.id_kategoria";

        try (Connection conn = utils.DBConexioa.getKonexioa(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String nan = rs.getString("nan");
                if (nan == null) {
                    nan = "";
                }

                String jabeIzena = rs.getString("jabe_izena");
                if (jabeIzena == null) {
                    jabeIzena = "";
                }

                String jabeAbizena = rs.getString("jabe_abizena");
                if (jabeAbizena == null) {
                    jabeAbizena = "";
                }

                String telefonoa = rs.getString("telefonoa");
                if (telefonoa == null) {
                    telefonoa = "";
                }

                String emaila = rs.getString("emaila");
                if (emaila == null) {
                    emaila = "";
                }

                model.Jabea jabea = new model.Jabea(
                        nan,
                        jabeIzena,
                        jabeAbizena,
                        telefonoa,
                        emaila
                );

                Erreklamazioa erreklamazioa = new Erreklamazioa(jabea, rs.getString("deskribapen_bilatua"), rs.getDate("erreklamazio_data"));
                erreklamazioa.setErreklamazioId(rs.getInt("id_erreklamazio"));

                int idKat = rs.getInt("id_kategoria");
                if (idKat != 0) {
                    erreklamazioa.setKategoria(new model.Kategoria(idKat, rs.getString("kategoria_izena")));
                }

                String egoeraStr = rs.getString("errek_egoera");
                if (egoeraStr != null) {
                    if (!egoeraStr.isEmpty()) {
                        try {
                            erreklamazioa.setEgoera(EgoeraErreklamazioa.valueOf(egoeraStr.toUpperCase()));
                        } catch (IllegalArgumentException ex) {
                            erreklamazioa.setEgoera(EgoeraErreklamazioa.IREKITA);
                        }
                    }
                }

                erreklamazioak.add(erreklamazioa);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: datu-baseko errorea", e);
        }

        return erreklamazioak;
    }

    /**
     * Erreklamazio baten egoera eguneratzen du datu-basean.
     *
     * @param id Erreklamazioaren IDa
     * @param egoera Egoera berria
     * @return Eguneraketa ondo joan den ala ez
     */
    public static boolean updateEgoera(String id, String egoera) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.erreklamazioaUpdateEgoera(id, egoera);
        }
        String sql = "UPDATE ERREKLAMAZIOA SET errek_egoera = ? WHERE id_erreklamazio = ?";
        try (Connection con = utils.DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, egoera);
            ps.setInt(2, Integer.parseInt(id));
            ps.executeUpdate();
            return true;
        } catch (SQLException | NumberFormatException e) {
            LOG.log(Level.SEVERE, "updateEgoera: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Erreklamazio berri bat gordetzen du datu-basean.
     *
     * @return Ondo gorde den
     */
    public static boolean gorde(String nan, String izena, String abizena, String telefonoa, String emaila, int kategoriaId, String deskribapena, int idLangile) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.erreklamazioaGorde(nan, izena, abizena, telefonoa, emaila, kategoriaId, deskribapena, idLangile);
        }
        int idHartzailea = -1;

        try (Connection con = utils.DBConexioa.getKonexioa()) {

            String checkSql = "SELECT id_hartzailea FROM JABEA WHERE nan = ?";
            try (PreparedStatement psCheck = con.prepareStatement(checkSql)) {
                psCheck.setString(1, nan);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        idHartzailea = rs.getInt("id_hartzailea");
                    }
                }
            }

            if (idHartzailea == -1) {
                String insertH = "INSERT INTO HARTZAILEA (telefonoa, emaila) VALUES (?, ?)";
                try (PreparedStatement psH = con.prepareStatement(insertH, Statement.RETURN_GENERATED_KEYS)) {
                    psH.setString(1, telefonoa);
                    psH.setString(2, emaila);
                    psH.executeUpdate();

                    try (ResultSet rsH = psH.getGeneratedKeys()) {
                        if (rsH.next()) {
                            idHartzailea = rsH.getInt(1);
                        }
                    }
                }

                if (idHartzailea != -1) {
                    String insertJ = "INSERT INTO JABEA (id_hartzailea, nan, izena, abizena) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement psJ = con.prepareStatement(insertJ)) {
                        psJ.setInt(1, idHartzailea);
                        psJ.setString(2, nan);
                        psJ.setString(3, izena);
                        psJ.setString(4, abizena);
                        psJ.executeUpdate();
                    }
                }
            }

            if (idHartzailea != -1) {
                String insertE = "INSERT INTO ERREKLAMAZIOA (erreklamazio_data, errek_egoera, deskribapen_bilatua, id_hartzailea, id_kategoria, id_langile) VALUES (CURDATE(), 'irekita', ?, ?, ?, ?)";
                try (PreparedStatement psE = con.prepareStatement(insertE)) {
                    psE.setString(1, deskribapena);
                    psE.setInt(2, idHartzailea);
                    psE.setInt(3, kategoriaId);

                    if (idLangile > 0) {
                        psE.setInt(4, idLangile);
                    } else {
                        psE.setNull(4, java.sql.Types.INTEGER);
                    }

                    psE.executeUpdate();
                    return true;
                }
            } else {
                return false;
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gorde: datu-baseko errorea", e);
            return false;
        }
    }
}
