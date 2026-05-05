package utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
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

    private static final String LOG_DIREKTORIOA =
            System.getProperty("user.home") + "/erronka-bermeo/logs";
    private static volatile boolean hasieratua = false;

    /**
     * Log sistema hasieratzen du fitxategi eta kontsolako kudeatzaileekin.
     * Bigarren deietan ez du ezer egiten.
     */
    public static synchronized void hasieratu() {
        if (hasieratua) return;
        try {
            Files.createDirectories(Paths.get(LOG_DIREKTORIOA));

            Logger erroa = Logger.getLogger("");
            for (Handler h : erroa.getHandlers()) {
                erroa.removeHandler(h);
            }
            erroa.setLevel(Level.ALL);

            FileHandler fitxategiaKudeatzailea = new FileHandler(
                    LOG_DIREKTORIOA + "/app-%g.log", 5 * 1024 * 1024, 3, true);
            fitxategiaKudeatzailea.setLevel(Level.ALL);
            fitxategiaKudeatzailea.setFormatter(sortuFormatzailea());
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

    /**
     * Klase baten Logger-a itzultzen du.
     *
     * @param klasea Logger-a nahi den klasea
     * @return Klaseari dagokion Logger
     */
    public static Logger lortu(Class<?> klasea) {
        return Logger.getLogger(klasea.getName());
    }

    private static Formatter sortuFormatzailea() {
        return new SimpleFormatter() {
            private static final String FORMATUA = "[%1$tF %1$tT] [%-7s] %s: %s%n";

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
