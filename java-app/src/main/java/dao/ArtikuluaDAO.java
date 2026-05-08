package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.Artikulua;
import model.EgoeraArtikulua;
import model.Kategoria;
import model.Kokalekua;
import utils.BiltegiLocala;
import utils.DBConexioa;
import utils.InsertLogailea;
import utils.LogKudeatzailea;
import utils.ModoKudeatzailea;

/**
 * Artikuluen datu-baseko eragiketak kudeatzen dituen DAO klasea.
 *
 * @author Yeray Garrido
 */
public class ArtikuluaDAO {

    private static final Logger LOG = LogKudeatzailea.lortu(ArtikuluaDAO.class);

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
     * @param argazkiBidea Argazkiaren bide erlatiboa (null bada hutsik)
     * @return Sortutako artikulu-kodea, edo null errorea bada
     */
    public static String gehitu(String izena, String deskribapena, boolean iragankorra,
            int idKategoria, int idKokalekua, java.sql.Date sarreraData, String argazkiBidea) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.artikuluaGehitu(izena, deskribapena, iragankorra, idKategoria, idKokalekua, sarreraData, argazkiBidea);
        }
        String kodea = sortuKodea(sarreraData);
        if (kodea == null) {
            return null;
        }

        java.sql.Date iraungData = null;
        if (sarreraData != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(sarreraData);
            cal.add(java.util.Calendar.DAY_OF_YEAR, 730);
            iraungData = new java.sql.Date(cal.getTimeInMillis());
        }

        String sql = "INSERT INTO ARTIKULUA (id_artikulua, a_izena, a_deskribapena, egoera, iragankorra, "
                + "sarrera_data, iraungitze_data, argazkia, id_kategoria, id_kokalekua) "
                + "VALUES (?, ?, ?, 'aurkitua', ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kodea);
            ps.setString(2, izena);
            ps.setString(3, deskribapena);
            ps.setBoolean(4, iragankorra);
            ps.setDate(5, sarreraData);
            ps.setDate(6, iraungData);
            if (argazkiBidea != null && !argazkiBidea.isEmpty()) {
                ps.setString(7, argazkiBidea);
            } else {
                ps.setNull(7, java.sql.Types.VARCHAR);
            }
            if (idKategoria > 0) {
                ps.setInt(8, idKategoria);
            } else {
                ps.setNull(8, java.sql.Types.INTEGER);
            }
            if (idKokalekua > 0) {
                ps.setInt(9, idKokalekua);
            } else {
                ps.setNull(9, java.sql.Types.INTEGER);
            }
            boolean ok = ps.executeUpdate() > 0;
            InsertLogailea.erregistratu("ARTIKULUA", kodea, ok);

            if (ok) {
                String mugDesc = "Artikulua sisteman erregistratu da: " + kodea;
                String sqlMug = "INSERT INTO MUGIMENDUA (deskribapena, id_artikulua) VALUES (?, ?)";
                try (PreparedStatement psMug = con.prepareStatement(sqlMug)) {
                    psMug.setString(1, mugDesc);
                    psMug.setString(2, kodea);
                    boolean mugOk = psMug.executeUpdate() > 0;
                    InsertLogailea.erregistratu("MUGIMENDUA", kodea, mugOk);
                }
                return kodea;
            }
            return null;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "gehitu: datu-baseko errorea", e);
            return null;
        }
    }

    /**
     * Iraungitze-data gainditu duten artikuluak 'iraungita' egoerara pasatzen
     * ditu. Aplikazioa abiaraztean deitzen da automatikoki online moduan.
     *
     * @return Eguneratutako artikulu kopurua
     */
    public static int iraungituakEguneratu() {
        if (ModoKudeatzailea.isOffline()) {
            return 0;
        }
        String sql = "UPDATE ARTIKULUA SET egoera = 'iraungita' "
                + "WHERE egoera = 'aurkitua' AND iraungitze_data < CURDATE()";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            int kopurua = ps.executeUpdate();
            if (kopurua > 0) {
                LOG.log(Level.INFO, "iraungituakEguneratu: {0} artikulu iraungita markatu dira.", kopurua);
            }
            return kopurua;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "iraungituakEguneratu: datu-baseko errorea", e);
            return 0;
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
            LOG.log(Level.SEVERE, "sortuKodea: datu-baseko errorea", e);
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
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.getArtikuluaByKodea(kodea);
        }
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
            LOG.log(Level.SEVERE, "getByKodea: datu-baseko errorea", e);
        }
        return null;
    }

    /**
     * Datu-basetik artikulu guztiak lortzen ditu.
     *
     * @return Artikuluen zerrenda
     */
    public static List<Artikulua> getGuztiak() {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.getArtikuluak();
        }
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
                Artikulua a = new Artikulua(
                        rs.getString("id_artikulua"),
                        rs.getString("a_izena"),
                        rs.getString("a_deskribapena"),
                        null, null,
                        rs.getDate("sarrera_data"),
                        rs.getString("argazkia")
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
                    a.setKokalekua(new Kokalekua(
                            rs.getString("armairua"),
                            rs.getString("apala"),
                            rs.getBoolean("bha_da")
                    ));
                }

                zerrenda.add(a);
            }
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "getGuztiak: datu-baseko errorea", e);
        }
        return zerrenda;
    }

    /**
     * Artikuluaren datuak eguneratzen ditu datu-basean.
     *
     * @param kodea Aldatu beharreko artikuluaren kodea
     * @param izena Izen berria
     * @param deskribapena Deskribapen berria
     * @param idKategoria Kategoria berria (0 bada ez da aldatzen)
     * @param idKokalekua Kokaleku berria (0 bada null ezartzen da)
     * @param argazkiBidea Argazki bide berria (null bada ez da aldatzen)
     * @return Ondo eguneratu bada true
     */
    public static boolean eguneratu(String kodea, String izena, String deskribapena,
            int idKategoria, int idKokalekua, String argazkiBidea) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.artikuluaEguneratu(kodea, izena, deskribapena, idKategoria, idKokalekua, argazkiBidea);
        }
        StringBuilder sql = new StringBuilder(
                "UPDATE ARTIKULUA SET a_izena=?, a_deskribapena=?");
        if (idKategoria > 0) {
            sql.append(", id_kategoria=?");
        } else {
            sql.append(", id_kategoria=NULL");
        }
        if (idKokalekua > 0) {
            sql.append(", id_kokalekua=?");
        } else {
            sql.append(", id_kokalekua=NULL");
        }
        if (argazkiBidea != null && !argazkiBidea.isEmpty()) {
            sql.append(", argazkia=?");
        }
        sql.append(" WHERE id_artikulua=?");

        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql.toString())) {
            int idx = 1;
            ps.setString(idx++, izena);
            ps.setString(idx++, deskribapena);
            if (idKategoria > 0) {
                ps.setInt(idx++, idKategoria);
            }
            if (idKokalekua > 0) {
                ps.setInt(idx++, idKokalekua);
            }
            if (argazkiBidea != null && !argazkiBidea.isEmpty()) {
                ps.setString(idx++, argazkiBidea);
            }
            ps.setString(idx, kodea);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "eguneratu: datu-baseko errorea", e);
            return false;
        }
    }

    /**
     * Artikulua datu-basetik ezabatzen du. Mugimendua eta EMANALDIA badago,
     * ezin da ezabatu (FK RESTRICT).
     *
     * @param kodea Ezabatu beharreko artikuluaren kodea
     * @return Ondo ezabatu bada true
     */
    public static boolean ezabatu(String kodea) {
        if (ModoKudeatzailea.isOffline()) {
            return BiltegiLocala.artikuluaEzabatu(kodea);
        }
        String sql = "DELETE FROM ARTIKULUA WHERE id_artikulua=?";
        try (Connection con = DBConexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kodea);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "ezabatu: datu-baseko errorea", e);
            return false;
        }
    }
}
