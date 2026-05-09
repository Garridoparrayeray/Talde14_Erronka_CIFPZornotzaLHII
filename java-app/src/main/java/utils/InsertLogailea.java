package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * INSERT eragiketen emaitzak testu-fitxategi batean erregistratzen dituen
 * utilitate estatikoa. Fitxategia: partekatutako_datuak/insert_log.txt
 */
public class InsertLogailea {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private InsertLogailea() {}

    /**
     * INSERT eragiketaren emaitza fitxategira idazten du.
     *
     * @param taula    Taula-izena (adib. "ARTIKULUA")
     * @param kodea    Sortu den erregistroaren identifikagailua
     * @param emaitza  true txertaketa ondo egin bada
     */
    public static void erregistratu(String taula, String kodea, boolean emaitza) {
        String bidea = AppConfig.getExportBidea() + java.io.File.separator + "insert_log.txt";
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
                        LocalDateTime.now().format(FMT),
                        taula,
                        kodea,
                        emaitzaTestua);
            }
        } catch (IOException e) {
            System.err.println("InsertLogailea: ezin idatzi — " + e.getMessage());
        }
    }
}
