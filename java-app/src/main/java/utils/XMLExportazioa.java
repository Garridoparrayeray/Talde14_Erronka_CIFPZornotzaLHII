package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import dao.ArtikuluaDAO;
import model.Artikulua;

/**
 * Artikuluen XML fitxategia sortzen du /app/exportazioak/ karpetan. Nginx-ek
 * zerbitzatzen du datuak/ bidez web-etik irakurtzeko.
 *
 * @author Yeray Garrido
 */
public class XMLExportazioa {

    private static final String BIDEA;

    static {
        if (System.getenv("EXPORT_BIDEA") != null) {
            BIDEA = System.getenv("EXPORT_BIDEA");
        } else {
            BIDEA = "/app/partekatutako_datuak/artikuluak.xml";
        }
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Biltegiko artikulu guztiak XML fitxategi batera exportatzen ditu.
     * Nginx-ek zerbitzatzen du fitxategia web-etik irakurtzeko.
     */
    public static void exportatu() {
        List<Artikulua> zerrenda = ArtikuluaDAO.getGuztiak();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<artikuluak>\n");

        for (Artikulua a : zerrenda) {
            sb.append("  <artikulua>\n");
            sb.append("    <id>").append(esc(a.getArtikuluKodea())).append("</id>\n");
            sb.append("    <izena>").append(esc(a.getIzenburua())).append("</izena>\n");
            sb.append("    <deskribapena>").append(esc(a.getDeskribapena())).append("</deskribapena>\n");
            if (a.getKategoria() != null) {
                sb.append("    <kategoria>").append(esc(a.getKategoria().getIzena())).append("</kategoria>\n");
            } else {
                sb.append("    <kategoria></kategoria>\n");
            }
            if (a.getEgoera() != null) {
                sb.append("    <egoera>").append(a.getEgoera().name()).append("</egoera>\n");
            } else {
                sb.append("    <egoera></egoera>\n");
            }
            if (a.getSarreraData() != null) {
                sb.append("    <sarreraData>").append(SDF.format(a.getSarreraData())).append("</sarreraData>\n");
            } else {
                sb.append("    <sarreraData></sarreraData>\n");
            }
            if (a.getArgazkiBidea() != null) {
                sb.append("    <argazkia>").append(esc(a.getArgazkiBidea())).append("</argazkia>\n");
            } else {
                sb.append("    <argazkia></argazkia>\n");
            }
            sb.append("  </artikulua>\n");
        }

        sb.append("</artikuluak>");

        try (FileWriter fw = new FileWriter(BIDEA)) {
            fw.write(sb.toString());
        } catch (IOException e) {
            System.err.println("XMLExportazioa errorea: " + e.getMessage());
        }
    }

    /**
     * XML karaktere bereziak ihes-sekuentziekin ordezkatzen ditu.
     *
     * @param s Garbitu beharreko katea
     * @return XML-erako segurua den katea
     */
    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
