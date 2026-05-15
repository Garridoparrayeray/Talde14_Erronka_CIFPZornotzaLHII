package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Aplikazioaren log sistema konfiguratzen duen klase laguntzailea.
 * java.util.logging erabiliz fitxategi birakarietan idazten du.
 * Log fitxategiak: ~/erronka-bermeo/logs/app-N.log (5 MB max, 3 fitxategi).
 *
 * @author Yeray Garrido
 */
public class LogKudeatzailea {

    private static final String LOG_DIREKTORIOA = System.getProperty("user.home") + "/erronka-bermeo/logs";
    private static volatile boolean hasieratua = false;

    /**
     * Log sistema hasieratzen du fitxategi eta kontsolako kudeatzaileekin.
     * Bigarren deietan ez du ezer egiten.
     */
    public static synchronized void hasieratu() {
        if (hasieratua) {
			return;
		}
        try {
            Files.createDirectories(Paths.get(LOG_DIREKTORIOA));

            Logger erroa = Logger.getLogger("");
            for (Handler h : erroa.getHandlers()) {
                erroa.removeHandler(h);
            }
            erroa.setLevel(Level.INFO);

            FileHandler fitxategiaKudeatzailea = new FileHandler(
                    LOG_DIREKTORIOA + "/app-%g.log", 5 * 1024 * 1024, 3, true);
            fitxategiaKudeatzailea.setLevel(Level.INFO);
            fitxategiaKudeatzailea.setFormatter(sortuFormatzailea());
            fitxategiaKudeatzailea.setFilter(erregistroa -> {
                String izena = erregistroa.getLoggerName();
                if (izena == null) {
					return false;
				}
                return izena.startsWith("app.") || izena.startsWith("controller.")
                        || izena.startsWith("dao.") || izena.startsWith("model.")
                        || izena.startsWith("utils.") || izena.startsWith("view.");
            });
            erroa.addHandler(fitxategiaKudeatzailea);

            ConsoleHandler kontsolaKudeatzailea = new ConsoleHandler();
            kontsolaKudeatzailea.setLevel(Level.WARNING);
            kontsolaKudeatzailea.setFormatter(sortuFormatzailea());
            erroa.addHandler(kontsolaKudeatzailea);

            hasieratua = true;
            Logger.getLogger(LogKudeatzailea.class.getName())
                    .info("Log sistema hasieratu da: " + LOG_DIREKTORIOA);

        } catch (IOException e) {
            System.err.println("LogKudeatzailea: ezin izan da log fitxategia sortu: " + e.getMessage());
        }
    }

    private static final DateTimeFormatter INSERT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * INSERT eragiketaren emaitza fitxategira idazten du.
     *
     * @param taula   Taula-izena (adib. "ARTIKULUA")
     * @param kodea   Sortu den erregistroaren identifikagailua
     * @param emaitza true txertaketa ondo egin bada
     */
    public static void erregistratu(String taula, String kodea, boolean emaitza) {
        String bidea = AppConfig.getExportBidea() + File.separator + "insert_log.txt";
        try {
            Files.createDirectories(Paths.get(AppConfig.getExportBidea()));
            try (PrintWriter pw = new PrintWriter(new FileWriter(bidea, true))) {
                String emaitzaTestua;
                if (emaitza) {
                    emaitzaTestua = "OK";
                } else {
                    emaitzaTestua = "HUTS";
                }
                pw.printf("[%s] INSERT INTO %s | ID: %-15s | %s%n",
                        LocalDateTime.now().format(INSERT_FMT), taula, kodea, emaitzaTestua);
            }
        } catch (IOException e) {
            System.err.println("LogKudeatzailea: ezin idatzi insert_log — " + e.getMessage());
        }
    }

    /**
     * Klase baten Logger-a itzultzen du.
     *
     * @param klasea Logger-a nahi den klasea
     * @return Klaseari dagokion Logger
     */
    public static Logger lortu(Class<?> klasea) {
        return Logger.getLogger(klasea.getName());
    }

    /**
     * Log erregistroak formateatzeko Formatter bat sortzen du. Formatua:
     * [data ordua] [maila] KlaseIzena: mezua.
     *
     * @return Konfiguratutako Formatter instantzia
     */
    private static Formatter sortuFormatzailea() {
        return new SimpleFormatter() {
            private static final String FORMATUA = "[%1$tF %1$tT] [%2$-7s] %3$s: %4$s%n";

            @Override
            public synchronized String format(LogRecord erregistroa) {
                String mezua = formatMessage(erregistroa);
                String emaitza = String.format(FORMATUA,
                        new Date(erregistroa.getMillis()),
                        erregistroa.getLevel().getName(),
                        erregistroa.getSourceClassName().replaceAll(".*\\.", ""),
                        mezua);
                if (erregistroa.getThrown() != null) {
                    StringWriter sw = new StringWriter();
                    erregistroa.getThrown().printStackTrace(new PrintWriter(sw));
                    emaitza += sw;
                }
                return emaitza;
            }
        };
    }
}
