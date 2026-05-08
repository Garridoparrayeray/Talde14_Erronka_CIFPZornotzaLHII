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
 * duen utilitate estatikoa.
 * Artikulu bakoitzaren egoera XML-tik hartzen da eta DB-n eguneratzen da,
 * artikulua dagoeneko existitzen bada soilik.
 */
public class XMLInportazioa {

    private static final Logger LOG = LogKudeatzailea.lortu(XMLInportazioa.class);

    private XMLInportazioa() {}

    /**
     * Emaitza-laburpena.
     */
    public static class Emaitza {
        public final int eguneratuak;
        public final int saltaturak;
        public final List<String> mezuak;

        Emaitza(int eguneratuak, int saltaturak, List<String> mezuak) {
            this.eguneratuak = eguneratuak;
            this.saltaturak  = saltaturak;
            this.mezuak      = mezuak;
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
        int saltaturak  = 0;
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
                String id     = testua(el, "id");
                String egoera = testua(el, "egoera");

                if (id.isEmpty()) {
                    saltaturak++;
                    mezuak.add("Saltatu: ID hutsa [" + i + "]");
                    continue;
                }

                // DB-n existitzen den egiaztatu
                boolean existitzen = dbArtikuluak.stream()
                        .anyMatch(a -> a.getArtikuluKodea().equals(id));
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

    private static boolean eguneratuEgoera(String kodea, String dbEgoera) {
        String sql = "UPDATE ARTIKULUA SET egoera=? WHERE id_artikulua=?";
        try (Connection con = utils.DBConexioa.getKonexioa();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dbEgoera);
            ps.setString(2, kodea);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "eguneratuEgoera: errorea", e);
            return false;
        }
    }

    private static String xmlEgoeraMapatu(String xmlEgoera) {
        if (xmlEgoera == null) return null;
        switch (xmlEgoera.toUpperCase()) {
            case "BILTEGIAN": return "aurkitua";
            case "ITZULITA":  return "bueltatua";
            case "IRAUNGITA": return "iraungita";
            default:          return null;
        }
    }

    private static String testua(Element el, String etiketa) {
        NodeList nl = el.getElementsByTagName(etiketa);
        if (nl.getLength() == 0) return "";
        String val = nl.item(0).getTextContent();
        return val != null ? val.trim() : "";
    }
}
