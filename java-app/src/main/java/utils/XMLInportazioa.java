package utils;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import dao.ArtikuluaDAO;
import model.Artikulua;
import model.EgoeraArtikulua;

/**
 * XML fitxategi batetik artikuluen datuak irakurri eta datu-basea eguneratzen
 * duen utilitate estatikoa. Artikulu bakoitzaren egoera XML-tik hartzen da eta
 * DB-n eguneratzen da, artikulua dagoeneko existitzen bada soilik.
 *
 * @author Yeray Garrido
 */
public class XMLInportazioa {

    private static final Logger LOG = LogKudeatzailea.lortu(XMLInportazioa.class);

    private XMLInportazioa() {
    }

    /**
     * Inportazio-prozesuaren emaitza-laburpena gordetzen duen barneko klasea.
     */
    public static class Emaitza {

        /**
         * Ondo eguneratutako artikuluen kopurua.
         */
        public final int eguneratuak;
        /**
         * Saltatu diren edo huts egin duten artikuluen kopurua.
         */
        public final int saltaturak;
        /**
         * Artikulu bakoitzaren emaitza deskribatzen duten mezuen zerrenda.
         */
        public final List<String> mezuak;

        /**
         * Emaitza-objektua sortzen du inportazioaren laburpenarekin.
         *
         * @param eguneratuak Eguneratutako artikuluen kopurua
         * @param saltaturak Saltatutako edo huts egindakoen kopurua
         * @param mezuak Eragiketa bakoitzaren deskribapena duten mezuak
         */
        Emaitza(int eguneratuak, int saltaturak, List<String> mezuak) {
            this.eguneratuak = eguneratuak;
            this.saltaturak = saltaturak;
            this.mezuak = mezuak;
        }
    }

    /**
     * XML fitxategia irakurri eta DB eguneratzen du.
     *
     * @param fitxategia Inportatu beharreko XML fitxategia
     * @return Emaitza: eguneratutako eta saltatutako kopuruak
     */
    public static Emaitza inportatu(File fitxategia) {
        int eguneratuak = 0;
        int saltaturak = 0;
        List<String> mezuak = new ArrayList<>();

        try {
            DocumentBuilderFactory fabrika = DocumentBuilderFactory.newInstance();
            fabrika.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder eraikitzailea = fabrika.newDocumentBuilder();
            Document doc = eraikitzailea.parse(fitxategia);
            doc.getDocumentElement().normalize();

            NodeList artikuluNodes = doc.getElementsByTagName("artikulua");
            List<Artikulua> dbArtikuluak = ArtikuluaDAO.getGuztiak();

            for (int i = 0; i < artikuluNodes.getLength(); i++) {
                Element el = (Element) artikuluNodes.item(i);
                String id = testua(el, "id");
                String egoera = testua(el, "egoera");

                if (id.isEmpty()) {
                    saltaturak++;
                    mezuak.add("Saltatu: ID hutsa [" + i + "]");
                    continue;
                }

                // DB-n existitzen den egiaztatu
                boolean existitzen = false;
                for (Artikulua a : dbArtikuluak) {
                    if (a.getArtikuluKodea().equals(id)) {
                        existitzen = true;
                        break;
                    }
                }
                if (!existitzen) {
                    saltaturak++;
                    mezuak.add("Saltatu (ez da existitzen): " + id);
                    continue;
                }

                // Egoera XML→DB mapaketa
                String dbEgoera = xmlEgoeraMapatu(egoera);
                if (dbEgoera == null) {
                    saltaturak++;
                    mezuak.add("Saltatu (egoera ezezaguna): " + id + " → " + egoera);
                    continue;
                }

                boolean ok = eguneratuEgoera(id, dbEgoera);
                if (ok) {
                    eguneratuak++;
                    mezuak.add("OK: " + id + " → " + dbEgoera);
                } else {
                    saltaturak++;
                    mezuak.add("HUTS DB-n: " + id);
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "inportatu: XML irakurketa errorea", e);
            mezuak.add("ERROREA: " + e.getMessage());
        }

        return new Emaitza(eguneratuak, saltaturak, mezuak);
    }

    /**
     * Artikulu baten egoera datu-basean eguneratzen du.
     *
     * @param kodea Eguneratu beharreko artikuluaren kodea
     * @param dbEgoera DB-n gordetzeko egoera balioa
     * @return Ondo eguneratu bada true, bestela false
     */
    private static boolean eguneratuEgoera(String kodea, String dbEgoera) {
        String sql = "UPDATE ARTIKULUA SET egoera=? WHERE id_artikulua=?";
        try (Connection con = DBKonexioa.getKonexioa(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dbEgoera);
            ps.setString(2, kodea);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "eguneratuEgoera: errorea", e);
            return false;
        }
    }

    /**
     * XML-ko egoera balioa DB-ko egoera balioetara mapatu egiten du.
     *
     * @param xmlEgoera XML fitxategitik irakurritako egoera testua
     * @return DB-ko egoera balioa, edo null ezezaguna bada
     */
    private static String xmlEgoeraMapatu(String xmlEgoera) {
        if (xmlEgoera == null) {
            return null;
        }
        switch (xmlEgoera.toUpperCase()) {
            case "BILTEGIAN":
                return "aurkitua";
            case "ITZULITA":
                return "bueltatua";
            case "IRAUNGITA":
                return "iraungita";
            default:
                return null;
        }
    }

    /**
     * XML elementu baten etiketa-edukia testu gisa itzultzen du.
     *
     * @param el Edukia irakurri beharreko XML elementua
     * @param etiketa Bilatu beharreko etiketa-izena
     * @return Etiketaren testua (trim eginda), edo kate hutsa existitzen ez
     * bada
     */
    private static String testua(Element el, String etiketa) {
        NodeList nl = el.getElementsByTagName(etiketa);
        if (nl.getLength() == 0) {
            return "";
        }
        String val = nl.item(0).getTextContent();
        if (val == null) {
            return "";
        }
        return val.trim();
    }
}
