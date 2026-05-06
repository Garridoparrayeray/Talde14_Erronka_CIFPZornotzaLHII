package utils;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

/**
 * Konfigurazioaren irakurketa zentralizatzen duen klasea.
 * Lehentasuna: ingurune-aldagaiak > application.properties > balioak lehenetsiak.
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

    private static String get(String key, String defVal) {
        String env = System.getenv(key);
        if (env != null && !env.isEmpty()) {
            return env;
        }
        return PROPS.getProperty(key, defVal);
    }

    /** Partekatutako datuak karpetaren bide absolutua. */
    public static String getExportBidea() {
        String base = get("EXPORT_BIDEA", "partekatutako_datuak");
        File f = new File(base);
        if (!f.isAbsolute()) {
            f = new File(System.getProperty("user.dir"), base);
        }
        return f.getAbsolutePath();
    }

    /** Irudien azpikarpeta (exportBidea/irudiak) - XML/web biderako. */
    public static String getIrudiakBidea() {
        return getExportBidea() + File.separator + "irudiak";
    }

    /** XML fitxategiaren bide osoa. */
    public static String getXmlBidea() {
        return getExportBidea() + File.separator + "artikuluak.xml";
    }

    /** artikulu_irudiak/ karpeta - aplikazioak erabiltzen duen argazki-biltegi nagusia. */
    public static String getArtikuluIrudiakBidea() {
        String val = get("IRUDIAK_BIDEA", "artikulu_irudiak");
        File f = new File(val);
        if (!f.isAbsolute()) {
            f = new File(System.getProperty("user.dir"), val);
        }
        return f.getAbsolutePath();
    }
}
