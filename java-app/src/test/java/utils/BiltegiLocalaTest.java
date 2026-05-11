package utils;

import model.*;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BiltegiLokala klasearen funtzionaltasun guztia probatzen duen test-a. OFFLINE
 * modua erabiltzen du — datu-baserik ez da behar.
 *
 * @author Yeray Garrido
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BiltegiLokala offline-test")
public class BiltegiLocalaTest {

    @BeforeAll
    static void setUpOffline() {
        ModoKudeatzailea.setOffline(true);
        BiltegiLokala.hasieratu();
    }

    // ─── LANGILEAK ──────────────────────────────────────────────────────────
    @Test
    @Order(10)
    @DisplayName("Langilea gehitu eta zerrendan agertu")
    public void testLangileaGehituEtaLortu() {
        boolean ok = BiltegiLokala.langileaGehitu("Iker", "Etxebarria", "ikeretx", "udal123", 2);
        assertTrue(ok, "Langilea gehitu behar da");

        List<Langilea> langileak = BiltegiLokala.getLangileak();
        assertTrue(langileak.stream().anyMatch(l -> l.getErabiltzailea().equals("ikeretx")),
                "Langilea zerrendan agertu behar da");
    }

    @Test
    @Order(11)
    @DisplayName("Login zuzenak arrakasta eman behar du")
    public void testLoginZuzena() {
        BiltegiLokala.langileaGehitu("Login", "Proba", "logintest", "pasahitza123", 2);
        Langilea l = BiltegiLokala.login("logintest", "pasahitza123");
        assertNotNull(l, "Login zuzena ez da null izan behar");
        assertEquals("logintest", l.getErabiltzailea());
    }

    @Test
    @Order(12)
    @DisplayName("Login okerrak null itzuli behar du")
    public void testLoginOkerra() {
        Langilea l = BiltegiLokala.login("existitzenezdena", "okerrapasahitza");
        assertNull(l, "Login okerrak null itzuli behar du");
    }

    @Test
    @Order(13)
    @DisplayName("Langilea eguneratu")
    public void testLangileaEguneratu() {
        BiltegiLokala.langileaGehitu("Update", "Proba", "updatetest", "pass123", 2);
        List<Langilea> guztiak = BiltegiLokala.getLangileak();
        int id = guztiak.stream()
                .filter(l -> l.getErabiltzailea().equals("updatetest"))
                .mapToInt(Langilea::getLangileId).findFirst().orElse(-1);
        assertNotEquals(-1, id);

        boolean ok = BiltegiLokala.langileaEguneratu(id, "UpdateBerria", "ProbaB", "updatetest", 2, "");
        assertTrue(ok);
        List<Langilea> ondoren = BiltegiLokala.getLangileak();
        assertTrue(ondoren.stream().anyMatch(l -> l.getIzena().equals("UpdateBerria")));
    }

    @Test
    @Order(14)
    @DisplayName("Langilea ezabatu")
    public void testLangileaEzabatu() {
        BiltegiLokala.langileaGehitu("Ezabatu", "Proba", "ezabattest", "pass", 2);
        List<Langilea> before = BiltegiLokala.getLangileak();
        int id = before.stream()
                .filter(l -> l.getErabiltzailea().equals("ezabattest"))
                .mapToInt(Langilea::getLangileId).findFirst().orElse(-1);
        assertNotEquals(-1, id);

        boolean ok = BiltegiLokala.langileaEzabatu(id);
        assertTrue(ok);
        List<Langilea> after = BiltegiLokala.getLangileak();
        assertFalse(after.stream().anyMatch(l -> l.getErabiltzailea().equals("ezabattest")));
    }

    // ─── KATEGORIAK ─────────────────────────────────────────────────────────
    @Test
    @Order(20)
    @DisplayName("Kategoria gehitu eta zerrendan agertu")
    public void testKategoriaGehitu() {
        boolean ok = BiltegiLokala.kategoriaGehitu("TestKategoria");
        assertTrue(ok);
        List<Kategoria> kategoriak = BiltegiLokala.getKategoriak();
        assertTrue(kategoriak.stream().anyMatch(k -> k.getIzena().equals("TestKategoria")));
    }

    @Test
    @Order(21)
    @DisplayName("Kategoria eguneratu")
    public void testKategoriaEguneratu() {
        BiltegiLokala.kategoriaGehitu("EguneratuBehar");
        List<Kategoria> list = BiltegiLokala.getKategoriak();
        int id = list.stream().filter(k -> k.getIzena().equals("EguneratuBehar"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(-1);
        assertNotEquals(-1, id);

        boolean ok = BiltegiLokala.kategoriaAldatuIzena(id, "EguneratutaOk");
        assertTrue(ok);
        assertTrue(BiltegiLokala.getKategoriak().stream()
                .anyMatch(k -> k.getIzena().equals("EguneratutaOk")));
    }

    @Test
    @Order(22)
    @DisplayName("Kategoria ezabatu")
    public void testKategoriaEzabatu() {
        BiltegiLokala.kategoriaGehitu("EzabatuKat");
        int id = BiltegiLokala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("EzabatuKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(-1);
        assertNotEquals(-1, id);
        assertTrue(BiltegiLokala.kategoriaEzabatu(id));
        assertFalse(BiltegiLokala.getKategoriak().stream()
                .anyMatch(k -> k.getKategoriaId() == id));
    }

    // ─── KOKALEKUAK ─────────────────────────────────────────────────────────
    @Test
    @Order(30)
    @DisplayName("Kokalekua gehitu eta zerrendan agertu")
    public void testKokalekuaGehitu() {
        boolean ok = BiltegiLokala.kokalekuaGehitu("A", "1", false);
        assertTrue(ok);
        List<Kokalekua> koks = BiltegiLokala.getKokalekuak();
        assertTrue(koks.stream().anyMatch(k -> k.getArmairua().equals("A") && k.getApala().equals("1")));
    }

    @Test
    @Order(31)
    @DisplayName("Kokalekua eguneratu")
    public void testKokalekuaEguneratu() {
        BiltegiLokala.kokalekuaGehitu("B", "2", false);
        int id = BiltegiLokala.getKokalekuak().stream()
                .filter(k -> k.getArmairua().equals("B") && k.getApala().equals("2"))
                .mapToInt(Kokalekua::getKokalekuId).findFirst().orElse(-1);
        assertNotEquals(-1, id);
        assertTrue(BiltegiLokala.kokalekuaEguneratu(id, "B", "3", false));
        assertTrue(BiltegiLokala.getKokalekuak().stream()
                .anyMatch(k -> k.getKokalekuId() == id && k.getApala().equals("3")));
    }

    @Test
    @Order(32)
    @DisplayName("Kokalekua ezabatu")
    public void testKokalekuaEzabatu() {
        BiltegiLokala.kokalekuaGehitu("C", "99", false);
        int id = BiltegiLokala.getKokalekuak().stream()
                .filter(k -> k.getArmairua().equals("C") && k.getApala().equals("99"))
                .mapToInt(Kokalekua::getKokalekuId).findFirst().orElse(-1);
        assertNotEquals(-1, id);
        assertTrue(BiltegiLokala.kokalekuaEzabatu(id));
        assertFalse(BiltegiLokala.getKokalekuak().stream()
                .anyMatch(k -> k.getKokalekuId() == id));
    }

    // ─── ARTIKULUAK ─────────────────────────────────────────────────────────
    private static final Date DATA = Date.valueOf("2025-01-15");

    @Test
    @Order(40)
    @DisplayName("Artikulua gehitu eta zerrendan agertu")
    public void testArtikuluaGehitu() {
        BiltegiLokala.kategoriaGehitu("ArtKat");
        BiltegiLokala.kokalekuaGehitu("X", "1", false);
        int idKat = BiltegiLokala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("ArtKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);
        int idKok = BiltegiLokala.getKokalekuak().stream()
                .filter(k -> k.getArmairua().equals("X"))
                .mapToInt(Kokalekua::getKokalekuId).findFirst().orElse(0);

        String kodea = BiltegiLokala.artikuluaGehitu("Zorroa", "Larruzko zorroa gorria",
                false, idKat, idKok, DATA, "");
        assertNotNull(kodea, "Gehitutako artikuluak kode bat eduki behar du");
        assertTrue(kodea.startsWith("G-"), "Kodeak G- aurrizkiarekin hasi behar du: " + kodea);
    }

    @Test
    @Order(41)
    @DisplayName("Artikulua bilatu kodearengatik")
    public void testArtikuluaBilatuKodea() {
        BiltegiLokala.kategoriaGehitu("BilKat");
        int idKat = BiltegiLokala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("BilKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        String kodea = BiltegiLokala.artikuluaGehitu("Betaurrekoak", "Betaurrekoak berde",
                false, idKat, 0, DATA, "");
        assertNotNull(kodea);

        Artikulua a = BiltegiLokala.getArtikuluaByKodea(kodea);
        assertNotNull(a, "Kodearen bidez aurkitu behar da");
        assertEquals("Betaurrekoak", a.getIzenburua());
    }

    @Test
    @Order(42)
    @DisplayName("Artikulua eguneratu")
    public void testArtikuluaEguneratu() {
        BiltegiLokala.kategoriaGehitu("EgKat");
        int idKat = BiltegiLokala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("EgKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        String kodea = BiltegiLokala.artikuluaGehitu("GiltzeZaharra", "Giltza bat",
                false, idKat, 0, DATA, "");
        assertNotNull(kodea);

        boolean ok = BiltegiLokala.artikuluaEguneratu(kodea, "GiltzeBerria", "Giltza berri bat", idKat, 0, "");
        assertTrue(ok);
        assertEquals("GiltzeBerria", BiltegiLokala.getArtikuluaByKodea(kodea).getIzenburua());
    }

    @Test
    @Order(43)
    @DisplayName("Artikulua ezabatu")
    public void testArtikuluaEzabatu() {
        BiltegiLokala.kategoriaGehitu("EzKat");
        int idKat = BiltegiLokala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("EzKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        String kodea = BiltegiLokala.artikuluaGehitu("EzabatuArt", "Ezabatu behar da",
                false, idKat, 0, DATA, "");
        assertNotNull(kodea);

        boolean ok = BiltegiLokala.artikuluaEzabatu(kodea);
        assertTrue(ok);
        assertNull(BiltegiLokala.getArtikuluaByKodea(kodea), "Ezabatu ondoren null itzuli behar da");
    }

    // ─── ESTADISTIKAK ────────────────────────────────────────────────────────
    @Test
    @Order(50)
    @DisplayName("Estadistika: biltegian kopurua >= 0")
    public void testBiltegianKopurua() {
        int kop = BiltegiLokala.biltegianKopurua();
        assertTrue(kop >= 0, "Biltegian kopurua negatiboa ezin da izan");
    }

    @Test
    @Order(51)
    @DisplayName("Estadistika: kategoria kopurua >= 0")
    public void testKategoriaKopurua() {
        int kop = BiltegiLokala.kategoriaKopurua();
        assertTrue(kop >= 0);
    }

    @Test
    @Order(52)
    @DisplayName("Estadistika: langile kopurua >= 0")
    public void testLangileKopurua() {
        int kop = BiltegiLokala.langileKopurua();
        assertTrue(kop >= 0);
    }

    @Test
    @Order(53)
    @DisplayName("Estadistika: artikulu guztien kopurua zerrendarekiko koherentea")
    public void testArtikuluGuztienKopurua() {
        int kop = BiltegiLokala.artikuluGuztienKopurua();
        int zerrendaKop = BiltegiLokala.getArtikuluak().size();
        assertEquals(zerrendaKop, kop,
                "artikuluGuztienKopurua eta getArtikuluak().size() berdinak izan behar dira");
    }

    // ─── ERREKLAMAZIOAK ──────────────────────────────────────────────────────
    @Test
    @Order(60)
    @DisplayName("Erreklamazioa gorde eta zerrendan agertu")
    public void testErreklamazioaGorde() {
        BiltegiLokala.kategoriaGehitu("ErrKat");
        int idKat = BiltegiLokala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("ErrKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        boolean ok = BiltegiLokala.erreklamazioaGorde(
                "12345678A", "Mikel", "Uriarte", "600000001", "mikel@test.com",
                idKat, "Larruzko zorro gorri bat", 1);
        assertTrue(ok, "Erreklamazioa gorde behar da");
        assertFalse(BiltegiLokala.getErreklamazioak().isEmpty(),
                "Erreklamazioen zerrenda ez da hutsik egon behar");
    }

    @Test
    @Order(61)
    @DisplayName("Erreklamazioaren egoera eguneratu")
    public void testErreklamazioaEgoera() {
        List<Erreklamazioa> errek = BiltegiLokala.getErreklamazioak();
        if (errek.isEmpty()) {
            return;
        }
        String id = String.valueOf(errek.get(0).getErreklamazioId());
        boolean ok = BiltegiLokala.erreklamazioaUpdateEgoera(id, "ebatzita");
        assertTrue(ok);
    }
}
