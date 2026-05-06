package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Artikulua;
import model.EgoeraArtikulua;
import model.Kategoria;
import model.Kokalekua;
import utils.DBConexioa;

/**
 * Artikuluen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class ArtikuluaDAO {

    /**
     * Artikulua berria gordetzen du datu-basean, kode automatikoa sortuz.
     *
     * @return Ondo gorde bada true
     */
    /**
     * Artikulu berria gordetzen du datu-basean, kode automatikoa sortuz eta
     * mugimendua erregistratuz.
     *
     * @param izena Artikuluaren izenburua
     * @param deskribapena Artikuluaren deskripzio osoa
     * @param iragankorra Iragankorra bada true
     * @param idKategoria Kategoriaaren identifikagailua (0 bada ez da lotzen)
     * @param idKokalekua Kokalekuaren identifikagailua (0 bada ez da lotzen)
     * @param sarreraData Biltegira sartu zen data
     * @return Ondo gorde bada true
     */
    public static boolean gehitu(String izena, String deskribapena, boolean iragankorra,
            int idKategoria, int idKokalekua, java.sql.Date sarreraData) {
        String kodea = sortuKodea(sarreraData);
        if (kodea == null) {
            return false;
        }

        java.sql.Date iraungData = null;
        if (sarreraData != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sarreraData);
            cal.add(java.util.Calendar.DAY_OF_YEAR, 730);
            iraungData = new java.sql.Date(cal.getTimeInMillis());
        }

        String sql = "INSERT INTO ARTIKULUA (id_artikulua, a_izena, a_deskribapena, egoera, iragankorra, "
                + "sarrera_data, iraungitze_data, id_kategoria, id_kokalekua) VALUES (?, ?, ?, 'aurkitua', ?, ?, ?, ?, ?)";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kodea);
            ps.setString(2, izena);
            ps.setString(3, deskribapena);
            ps.setBoolean(4, iragankorra);
            ps.setDate(5, sarreraData);
            ps.setDate(6, iraungData);
            if (idKategoria > 0) {
                ps.setInt(7, idKategoria);
            } else {
                ps.setNull(7, java.sql.Types.INTEGER);
            }
            if (idKokalekua > 0) {
                ps.setInt(8, idKokalekua);
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            boolean ok = ps.executeUpdate() > 0;

            // Mugimendua sortu (auditoria)
            if (ok) {
                String mugDesc = "Artikulua sisteman erregistratu da: " + kodea;
                String sqlMug = "INSERT INTO MUGIMENDUA (deskribapena, id_artikulua) VALUES (?, ?)";
                try (PreparedStatement psMug = con.prepareStatement(sqlMug)) {
                    psMug.setString(1, mugDesc);
                    psMug.setString(2, kodea);
                    psMug.executeUpdate();
                }
            }
            return ok;
        } catch (SQLException e) {
            System.err.println("ArtikuluaDAO.gehitu: " + e.getMessage());
            return false;
        }
    }

    /**
     * Hurrengo artikulu-kode sekuentziala sortzen du urtearen arabera (G-NNN-AA
     * formatua).
     *
     * @param sarreraData Sarrera-data (urte-atzizkia ateratzeko)
     * @return Kode berria, edo null errorea bada
     */
    private static String sortuKodea(java.sql.Date sarreraData) {
        String urteStr = String.format("%02d", java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) % 100);
        if (sarreraData != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sarreraData);
            urteStr = String.format("%02d", cal.get(java.util.Calendar.YEAR) % 100);
        }
        String sql = "SELECT COUNT(*) + 1 AS hurrengo FROM ARTIKULUA WHERE id_artikulua LIKE ?";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, "G-%-" + urteStr);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int seq = rs.getInt("hurrengo");
                return String.format("G-%03d-%s", seq, urteStr);
            }
        } catch (SQLException e) {
            System.err.println("ArtikuluaDAO.sortuKodea: " + e.getMessage());
        }
        return null;
    }

    /**
     * Artikulua bat kodearen bidez bilatzen du datu-basetik.
     *
     * @param kodea Artikuluaren kode bakarra
     * @return Artikulua objektua, edo null ez badago
     */
    public static Artikulua getByKodea(String kodea) {
        String sql = "SELECT a.id_artikulua, a.a_izena, a.a_deskribapena, a.egoera, "
                + "a.sarrera_data, a.argazkia, "
                + "k.id_kategoria, k.izena AS kat_izena, "
                + "ko.id_kokalekua, ko.armairua, ko.apala, ko.bha_da "
                + "FROM ARTIKULUA a "
                + "LEFT JOIN KATEGORIA k  ON a.id_kategoria  = k.id_kategoria "
                + "LEFT JOIN KOKALEKUA ko ON a.id_kokalekua = ko.id_kokalekua "
                + "WHERE a.id_artikulua = ?";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kodea);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Artikulua a = new Artikulua(
                        rs.getString("id_artikulua"), rs.getString("a_izena"),
                        rs.getString("a_deskribapena"), null, null,
                        rs.getDate("sarrera_data"), rs.getString("argazkia")
                );
                String egoeraStr = rs.getString("egoera");
                if (egoeraStr != null) {
                    switch (egoeraStr) {
                        case "aurkitua":
                            a.aldatuEgoera(EgoeraArtikulua.BILTEGIAN);
                            break;
                        case "bueltatua":
                            a.aldatuEgoera(EgoeraArtikulua.ITZULITA);
                            break;
                        case "artxibatua":
                        case "iraungita":
                            a.aldatuEgoera(EgoeraArtikulua.IRAUNGITA);
                            break;
                        default:
                            break;
                    }
                }
                if (rs.getInt("id_kategoria") != 0) {
                    a.setKategoria(new Kategoria(rs.getInt("id_kategoria"), rs.getString("kat_izena")));
                }
                if (rs.getInt("id_kokalekua") != 0) {
                    a.setKokalekua(new Kokalekua(rs.getString("armairua"), rs.getString("apala"), rs.getBoolean("bha_da")));
                }
                return a;
            }
        } catch (SQLException e) {
            System.err.println("ArtikuluaDAO.getByKodea: " + e.getMessage());
        }
        return null;
    }

    /**
     * Datu-basetik artikulu guztiak lortzen ditu.
     *
     * @return Artikuluen zerrenda
     */
    public static List<Artikulua> getGuztiak() {
        List<Artikulua> zerrenda = new ArrayList<>();

        String sql = "SELECT a.id_artikulua, a.a_izena, a.a_deskribapena, a.egoera, "
                + "a.sarrera_data, a.iraungitze_data, a.argazkia, a.iragankorra, "
                + "k.id_kategoria, k.izena AS kat_izena, "
                + "ko.id_kokalekua, ko.armairua, ko.apala, ko.bha_da "
                + "FROM ARTIKULUA a "
                + "LEFT JOIN KATEGORIA k  ON a.id_kategoria  = k.id_kategoria "
                + "LEFT JOIN KOKALEKUA ko ON a.id_kokalekua = ko.id_kokalekua";

        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Artikulua oinarrizko datuekin sortu
                Artikulua a = new Artikulua(
                        rs.getString("id_artikulua"),
                        rs.getString("a_izena"),
                        rs.getString("a_deskribapena"),
                        null, null,
                        rs.getDate("sarrera_data"),
                        rs.getString("argazkia")
                );

                // Egoera bihurtu — DB-ko balioa enum balioarekin lotu
                String egoeraStr = rs.getString("egoera");
                if (egoeraStr != null) {
                    switch (egoeraStr) {
                        case "aurkitua":
                            a.aldatuEgoera(EgoeraArtikulua.BILTEGIAN);
                            break;
                        case "bueltatua":
                            a.aldatuEgoera(EgoeraArtikulua.ITZULITA);
                            break;
                        case "artxibatua":
                            a.aldatuEgoera(EgoeraArtikulua.IRAUNGITA);
                            break;
                        case "iraungita":
                            a.aldatuEgoera(EgoeraArtikulua.IRAUNGITA);
                            break;
                        default:
                            break;
                    }
                }

                // Kategoria esleitu (badago)
                if (rs.getInt("id_kategoria") != 0) {
                    a.setKategoria(new Kategoria(rs.getInt("id_kategoria"), rs.getString("kat_izena")));
                }

                // Kokalekua esleitu (badago)
                if (rs.getInt("id_kokalekua") != 0) {
                    a.setKokalekua(new Kokalekua(
                            rs.getString("armairua"),
                            rs.getString("apala"),
                            rs.getBoolean("bha_da")
                    ));
                }

                zerrenda.add(a);
            }
        } catch (SQLException e) {
            System.err.println("ArtikuluaDAO.getGuztiak errorea: " + e.getMessage());
        }
        return zerrenda;
    }
}
