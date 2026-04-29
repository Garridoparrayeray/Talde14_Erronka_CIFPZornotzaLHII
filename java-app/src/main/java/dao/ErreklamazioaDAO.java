package dao;

import utils.DBConexioa;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ErreklamazioaDAO {

    /**
     * Erreklamazio guztiak itzultzen ditu jabeen datuekin batera.
     * Itzultzen den String[] bakoitzaren indizeak:
     * [0] id_erreklamazio
     * [1] erreklamazio_data
     * [2] jabe_izena
     * [3] jabe_abizena
     * [4] telefonoa
     * [5] emaila
     * [6] kategoria
     * [7] deskribapen_bilatua
     * [8] errek_egoera
     */
    public static List<String[]> getGuztiak() {
        List<String[]> zerrenda = new ArrayList<>();

        String sql = "SELECT r.id_erreklamazio, r.erreklamazio_data, " +
                     "j.izena AS jabe_izena, j.abizena AS jabe_abizena, " +
                     "h.telefonoa, h.emaila, " +
                     "k.izena AS kategoria, r.deskribapen_bilatua, r.errek_egoera " +
                     "FROM ERREKLAMAZIOA r " +
                     "LEFT JOIN HARTZAILEA h  ON r.id_hartzailea = h.id_hartzailea " +
                     "LEFT JOIN JABEA j       ON h.id_hartzailea = j.id_hartzailea " +
                     "LEFT JOIN KATEGORIA k   ON r.id_kategoria  = k.id_kategoria " +
                     "ORDER BY r.erreklamazio_data DESC";

        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String[] fila = new String[9];
                fila[0] = rs.getString("id_erreklamazio");
                fila[1] = rs.getString("erreklamazio_data");
                fila[2] = rs.getString("jabe_izena")    != null ? rs.getString("jabe_izena")    : "—";
                fila[3] = rs.getString("jabe_abizena")  != null ? rs.getString("jabe_abizena")  : "—";
                fila[4] = rs.getString("telefonoa")     != null ? rs.getString("telefonoa")     : "—";
                fila[5] = rs.getString("emaila")        != null ? rs.getString("emaila")        : "—";
                fila[6] = rs.getString("kategoria")     != null ? rs.getString("kategoria")     : "—";
                fila[7] = rs.getString("deskribapen_bilatua");
                fila[8] = rs.getString("errek_egoera");
                zerrenda.add(fila);
            }
        } catch (SQLException e) {
            System.err.println("ErreklamazioaDAO.getGuztiak errorea: " + e.getMessage());
        }
        return zerrenda;
    }

    /**
     * Erreklamazino berria gordetzen du.
     * NAN dagoeneko badago, hartzaile existentea erabiltzen du.
     * Bestela, HARTZAILEA eta JABEA berria sortzen ditu.
     * @return true arrakastaz gorde bada
     */
    public static boolean gorde(String nan, String izena, String abizena,
                                 String telefonoa, String emaila,
                                 int idKategoria, String deskribapena,
                                 int idLangile) {
        try (Connection con = DBConexioa.getKonexioa()) {
            con.setAutoCommit(false);

            // 1. Egiaztatu NAN badagoen
            int idHartzailea = -1;
            String sqlBilatu = "SELECT id_hartzailea FROM JABEA WHERE nan = ?";
            try (PreparedStatement ps = con.prepareStatement(sqlBilatu)) {
                ps.setString(1, nan);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idHartzailea = rs.getInt("id_hartzailea");
                    }
                }
            }

            // 2. Pertsona berria bada, HARTZAILEA eta JABEA sortu
            if (idHartzailea == -1) {
                String sqlH = "INSERT INTO HARTZAILEA (telefonoa, emaila) VALUES (?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlH, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, telefonoa.isBlank() ? null : telefonoa);
                    ps.setString(2, emaila.isBlank() ? null : emaila);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            idHartzailea = rs.getInt(1);
                        }
                    }
                }

                String sqlJ = "INSERT INTO JABEA (nan, izena, abizena, id_hartzailea) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = con.prepareStatement(sqlJ)) {
                    ps.setString(1, nan);
                    ps.setString(2, izena);
                    ps.setString(3, abizena);
                    ps.setInt(4, idHartzailea);
                    ps.executeUpdate();
                }
            }

            // 3. ERREKLAMAZIOA sortu
            String sqlE = "INSERT INTO ERREKLAMAZIOA " +
                          "(erreklamazio_data, errek_egoera, deskribapen_bilatua, id_hartzailea, id_langile, id_kategoria) " +
                          "VALUES (?, 'irekita', ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(sqlE)) {
                ps.setDate(1, Date.valueOf(LocalDate.now()));
                ps.setString(2, deskribapena);
                ps.setInt(3, idHartzailea);
                ps.setInt(4, idLangile);
                ps.setInt(5, idKategoria);
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("ErreklamazioaDAO.gorde errorea: " + e.getMessage());
            return false;
        }
    }

    public static boolean updateEgoera(String idErreklamazio, String egoera) {
        String sql = "UPDATE ERREKLAMAZIOA SET errek_egoera = ? WHERE id_erreklamazio = ?";
        try (Connection con = DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, egoera);
            ps.setString(2, idErreklamazio);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("ErreklamazioaDAO.updateEgoera errorea: " + e.getMessage());
            return false;
        }
    }
}
