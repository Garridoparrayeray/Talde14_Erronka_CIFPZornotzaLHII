package utils;

import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;

/**
 * DB konexioa detektatu eta online/offline modua kudeatzen duen klase
 * estatikoa.
 *
 * @author Yeray Garrido
 */
public class ModoKudeatzailea {

    private static final Logger LOG = LogKudeatzailea.lortu(ModoKudeatzailea.class);
    private static volatile boolean offlineModo = false;

    private ModoKudeatzailea() {
    }

    /**
     * Abiaraztean DB konexioa egiaztatu eta modua ezartzen du. Online bada,
     * BiltegiLokala DB-tik sinkronizatzen du hurrengo offline-erako. DB
     * eskuragarri ez bada, offline modura aldatzen da automatikoki.
     */
    public static void detektatu() {
        try {
            java.sql.Connection con = DBKonexioa.getKonexioa();
            offlineModo = (con == null || con.isClosed());
        } catch (Exception e) {
            LOG.log(Level.INFO, "DB ez dago eskuragarri, offline modura: {0}", e.getMessage());
            offlineModo = true;
        }
        if (offlineModo) {
            LOG.info("OFFLINE modua aktibo — store.dat fitxategia erabiltzen da.");
            BiltegiLokala.hasieratu();
        } else {
            LOG.info("ONLINE modua aktibo — DB konexioa erabiliko da.");
            ArtikuluaDAO.iraungituakEguneratu();
            BiltegiLokala.sincronizatuDBtik();
        }
    }

    /**
     * Aplikazioa offline moduan dagoen ala ez itzultzen du.
     *
     * @return true DB eskuragarri ez bada; false online badago
     */
    public static boolean isOffline() {
        return offlineModo;
    }

    /**
     * Offline modua eskuz ezartzen du (probetarako edo konexio-aldaketetarako).
     * Offline modura aldatzen bada, BiltegiLokala instantziatzen du.
     *
     * @param offline true offline modura aldatzeko; false online jartzeko
     */
    public static void setOffline(boolean offline) {
        offlineModo = offline;
        if (offline) {
            BiltegiLokala.hasieratu();
        }
    }
}
