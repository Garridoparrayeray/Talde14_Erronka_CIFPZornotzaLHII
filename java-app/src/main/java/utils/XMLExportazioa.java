package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import dao.ArtikuluaDAO;
import model.Artikulua;

/**
 * Artikuluen XML fitxategia sortzen du partekatutako_datuak/ karpetan. Eremu
 * kritikoak adierazpen erregularren bidez balioztatzen dira eta patroia
 * betetzen ez duten artikuluak saltatu egiten dira.
 *
 * @author Yeray Garrido
 */
public class XMLExportazioa {

    private static final Logger LOG = LogKudeatzailea.lortu(XMLExportazioa.class);
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    private static final Pattern KODEA_PATROIA = Pattern.compile("^G-\\d{3}-\\d{2}$");
    private static final Pattern IZENA_PATROIA = Pattern.compile("^[^\\x00-\\x1F]{1,200}$");

    private XMLExportazioa() {
    }

    // ─── Exportazio erregelak ────────────────────────────────────────────────────
    /**
     * Eremu batek XML patroia betetzen ez duenean jaurtzen den salbuespena.
     * XMLExportazioa klasearen barnean definituta dago biak fitxategi berean
     * mantentzeko.
     */
    public static class XMLPatroiException extends Exception {

        private static final long serialVersionUID = 1L;
        private final String eremua;
        private final String balioa;

        /**
         * Salbuespena sortzen du eremu eta balio problematikoarekin.
         *
         * @param eremua XML eremu okerrarena izena
         * @param balioa Patroia betetzen ez duen balioa
         */
        public XMLPatroiException(String eremua, String balioa) {
            super("XML baliogabea — '" + eremua + "' eremua ez du patroia betetzen: [" + balioa + "]");
            this.eremua = eremua;
            this.balioa = balioa;
        }

        /**
         * Patroia betetzen ez duen eremu-izena itzultzen du.
         *
         * @return Eremu-izena
         */
        public String getEremua() {
            return eremua;
        }

        /**
         * Patroia betetzen ez duen balioa itzultzen du.
         *
         * @return Baliogabeko balioa
         */
        public String getBalioa() {
            return balioa;
        }
    }

    // ─── Exportazioa ─────────────────────────────────────────────────────────
    /**
     * Biltegiko artikuluak XML fitxategira exportatzen ditu. Patroia betetzen
     * ez duten artikuluak ohartarazpen batekin saltatzen dira; gainerakoak
     * fitxategian idazten dira.
     */
    public static void exportatu() {
        List<Artikulua> zerrenda = ArtikuluaDAO.getGuztiak();
        String bidea = AppConfig.getXmlBidea();

        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<artikuluak>\n");

        for (Artikulua a : zerrenda) {
            try {
                balioztatu("id", a.getArtikuluKodea(), KODEA_PATROIA);
                balioztatu("izena", a.getIzenburua(), IZENA_PATROIA);
            } catch (XMLPatroiException e) {
                LOG.log(Level.WARNING, "exportatu: patroi-errorea saltatu — {0}", e.getMessage());
                continue;
            }
            sb.append("  <artikulua>\n");
            sb.append("    <id>").append(esc(a.getArtikuluKodea())).append("</id>\n");
            sb.append("    <izena>").append(esc(a.getIzenburua())).append("</izena>\n");
            sb.append("    <deskribapena>").append(esc(a.getDeskribapena())).append("</deskribapena>\n");
            sb.append("    <kategoria>");
            if (a.getKategoria() != null) {
                sb.append(esc(a.getKategoria().getIzena()));
            }
            sb.append("</kategoria>\n");

            sb.append("    <egoera>");
            if (a.getEgoera() != null) {
                sb.append(a.getEgoera().name());
            }
            sb.append("</egoera>\n");

            sb.append("    <sarreraData>");
            if (a.getSarreraData() != null) {
                sb.append(SDF.format(a.getSarreraData()));
            }
            sb.append("</sarreraData>\n");

            sb.append("    <argazkia>").append(esc(a.getArgazkiBidea())).append("</argazkia>\n");
            sb.append("  </artikulua>\n");
        }

        sb.append("</artikuluak>");

        try {
            File fitxategia = new File(bidea);
            File karpeta = fitxategia.getParentFile();
            if (karpeta != null && !karpeta.exists()) {
                karpeta.mkdirs();
            }

            try (FileWriter fw = new FileWriter(fitxategia)) {
                fw.write(sb.toString());
                LOG.log(Level.INFO, "XML exportatua: {0}", bidea);
            }
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "exportatu: idazketa-errorea — {0}", e.getMessage());
        }
    }

    // ─── Laguntzaileak ────────────────────────────────────────────────────────
    /**
     * Balio bat adierazpen erregularraren patroiaren aurka egiaztatzen du.
     *
     * @param eremua   Egiaztatzen den eremu-izena (erroreen mezuetarako)
     * @param balioa   Egiaztatu beharreko testua
     * @param patroia  Aplikatu beharreko adierazpen erregularra
     * @throws XMLPatroiException Balioa null bada edo patroia ez badu betetzen
     */
    private static void balioztatu(String eremua, String balioa, Pattern patroia)
            throws XMLPatroiException {
        if (balioa == null || !patroia.matcher(balioa).matches()) {
            throw new XMLPatroiException(eremua, String.valueOf(balioa));
        }
    }

    /**
     * Testu bat XML-erako bihurketa egiten du karaktere bereziak ihes sekuentziekin
     * ordezkatuz.
     *
     * @param s Bihurtu beharreko testua (null onartzen da)
     * @return XML-bateragarria den testua; null bada, kate hutsa itzultzen du
     */
    private static String esc(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;");
    }
}
