package utils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * Konfigurazioaren irakurketa zentralizatzen duen klasea. Lehentasuna:
 * ingurune-aldagaiak > application.properties > balioak lehenetsiak.
 *
 * @author Yeray Garrido
 */
public class AppConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = AppConfig.class.getResourceAsStream("/application.properties")) {
            if (is != null) {
                PROPS.load(is);
            }
        } catch (Exception ignored) {
        }
    }

    /**
     * Konfigurazio-balio bat itzultzen du, ingurune-aldagaia > properties >
     * balio lehenetsia lehentasun-ordenan.
     *
     * @param key    Konfigurazio-gakoa
     * @param defVal Balio lehenetsia gakoa ez badago
     * @return Aurkitutako balioa
     */
    private static String get(String key, String defVal) {
        String env = System.getenv(key);
        if (env != null && !env.isEmpty()) {
            return env;
        }
        return PROPS.getProperty(key, defVal);
    }

    /**
     * Partekatutako datuak karpetaren bide absolutua itzultzen du.
     *
     * @return Partekatutako datuak karpetaren bide absolutua
     */
    public static String getExportBidea() {
        String base = get("EXPORT_BIDEA", "partekatutako_datuak");
        File f = new File(base);
        if (!f.isAbsolute()) {
            f = new File(System.getProperty("user.dir"), base);
        }
        return f.getAbsolutePath();
    }

    /**
     * Irudien azpikarpeta (exportBidea/irudiak) XML/web biderako itzultzen du.
     *
     * @return Irudien azpikarpetaren bide osoa
     */
    public static String getIrudiakBidea() {
        return getExportBidea() + File.separator + "irudiak";
    }

    /**
     * XML fitxategiaren bide osoa itzultzen du.
     *
     * @return artikuluak.xml fitxategiaren bide absolutua
     */
    public static String getXmlBidea() {
        return getExportBidea() + File.separator + "artikuluak.xml";
    }

    /**
     * Artikulu-irudiak karpetaren bide absolutua itzultzen du; aplikazioak
     * erabiltzen duen argazki-biltegi nagusia.
     *
     * @return artikulu_irudiak/ karpetaren bide absolutua
     */
    public static String getArtikuluIrudiakBidea() {
        String val = get("IRUDIAK_BIDEA", "artikulu_irudiak");
        File f = new File(val);
        if (!f.isAbsolute()) {
            f = new File(System.getProperty("user.dir"), val);
        }
        return f.getAbsolutePath();
    }

    /**
     * Sinadura-dokumentuen karpetaren bide absolutua itzultzen du.
     *
     * @return sinadurak/ azpikarpetaren bide absolutua
     */
    public static String getSinaduraBidea() {
        return getExportBidea() + File.separator + "sinadurak";
    }
}
