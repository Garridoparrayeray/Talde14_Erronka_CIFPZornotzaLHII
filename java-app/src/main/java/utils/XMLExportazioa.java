package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import dao.ArtikuluaDAO;
import model.Artikulua;

/**
 * Artikuluen XML fitxategia sortzen du partekatutako_datuak/ karpetan.
 * Nginx-ek zerbitzatzen du datuak/ bidez web-etik irakurtzeko.
 * Windows eta Docker biak onartzen ditu AppConfig bidez.
 */
public class XMLExportazioa {

    private static final Logger LOG = LogKudeatzailea.lortu(XMLExportazioa.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * Biltegiko artikulu guztiak XML fitxategi batera exportatzen ditu.
     * Nginx-ek zerbitzatzen du fitxategia web-etik irakurtzeko.
     */
    public static void exportatu() {
        List<Artikulua> zerrenda = ArtikuluaDAO.getGuztiak();

        File exportDir = new File(AppConfig.getExportBidea());
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<artikuluak>\n");

        for (Artikulua a : zerrenda) {
            sb.append("  <artikulua>\n");
            sb.append("    <id>").append(esc(a.getArtikuluKodea())).append("</id>\n");
            sb.append("    <izena>").append(esc(a.getIzenburua())).append("</izena>\n");
            sb.append("    <deskribapena>").append(esc(a.getDeskribapena())).append("</deskribapena>\n");
            sb.append("    <kategoria>").append(a.getKategoria() != null ? esc(a.getKategoria().getIzena()) : "").append("</kategoria>\n");
            sb.append("    <kokalekua>").append(a.getKokalekua() != null ? esc(a.getKokalekua().getKokalekuOsoa()) : "").append("</kokalekua>\n");
            sb.append("    <egoera>").append(a.getEgoera() != null ? a.getEgoera().name() : "").append("</egoera>\n");
            sb.append("    <sarreraData>").append(a.getSarreraData() != null ? SDF.format(a.getSarreraData()) : "").append("</sarreraData>\n");
            sb.append("    <iraungitzeData>").append(a.getIraungitzeData() != null ? SDF.format(a.getIraungitzeData()) : "").append("</iraungitzeData>\n");
            // Argazkia: bide erlatiboa (irudiak/G-001-26.jpg) web-etik irakurtzeko
            sb.append("    <argazkia>").append(a.getArgazkiBidea() != null ? esc(a.getArgazkiBidea()) : "").append("</argazkia>\n");
            sb.append("  </artikulua>\n");
        }

        sb.append("</artikuluak>");

        File xmlFile = new File(AppConfig.getXmlBidea());
        try (FileWriter fw = new FileWriter(xmlFile)) {
            fw.write(sb.toString());
            LOG.info("XML exportatuta: " + xmlFile.getAbsolutePath() + " (" + zerrenda.size() + " artikulu)");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "exportatu: XML fitxategi idazketa errorea: " + xmlFile.getAbsolutePath(), e);
        }
    }

    private static String esc(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
