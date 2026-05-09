package utils;

import model.*;
import org.junit.jupiter.api.*;
import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * BiltegiLocala klasearen funtzionaltasun guztia probatzen duen test-a.
 * OFFLINE modua erabiltzen du — datu-baserik ez da behar.
 *
 * @author Yeray Garrido
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("BiltegiLocala offline-test")
public class BiltegiLocalaTest {

    @BeforeAll
    static void setUpOffline() {
        ModoKudeatzailea.setOffline(true);
        BiltegiLocala.hasieratu();
    }

    // ─── LANGILEAK ──────────────────────────────────────────────────────────

    @Test @Order(10)
    @DisplayName("Langilea gehitu eta zerrendan agertu")
    public void testLangileaGehituEtaLortu() {
        boolean ok = BiltegiLocala.langileaGehitu("Iker", "Etxebarria", "ikeretx", "udal123", 2);
        assertTrue(ok, "Langilea gehitu behar da");

        List<Langilea> langileak = BiltegiLocala.getLangileak();
        assertTrue(langileak.stream().anyMatch(l -> l.getErabiltzailea().equals("ikeretx")),
                "Langilea zerrendan agertu behar da");
    }

    @Test @Order(11)
    @DisplayName("Login zuzenak arrakasta eman behar du")
    public void testLoginZuzena() {
        BiltegiLocala.langileaGehitu("Login", "Proba", "logintest", "pasahitza123", 2);
        Langilea l = BiltegiLocala.login("logintest", "pasahitza123");
        assertNotNull(l, "Login zuzena ez da null izan behar");
        assertEquals("logintest", l.getErabiltzailea());
    }

    @Test @Order(12)
    @DisplayName("Login okerrak null itzuli behar du")
    public void testLoginOkerra() {
        Langilea l = BiltegiLocala.login("existitzenezdena", "okerrapasahitza");
        assertNull(l, "Login okerrak null itzuli behar du");
    }

    @Test @Order(13)
    @DisplayName("Langilea eguneratu")
    public void testLangileaEguneratu() {
        BiltegiLocala.langileaGehitu("Update", "Proba", "updatetest", "pass123", 2);
        List<Langilea> guztiak = BiltegiLocala.getLangileak();
        int id = guztiak.stream()
                .filter(l -> l.getErabiltzailea().equals("updatetest"))
                .mapToInt(Langilea::getLangileId).findFirst().orElse(-1);
        assertNotEquals(-1, id);

        boolean ok = BiltegiLocala.langileaEguneratu(id, "UpdateBerria", "ProbaB", "updatetest", 2, "");
        assertTrue(ok);
        List<Langilea> ondoren = BiltegiLocala.getLangileak();
        assertTrue(ondoren.stream().anyMatch(l -> l.getIzena().equals("UpdateBerria")));
    }

    @Test @Order(14)
    @DisplayName("Langilea ezabatu")
    public void testLangileaEzabatu() {
        BiltegiLocala.langileaGehitu("Ezabatu", "Proba", "ezabattest", "pass", 2);
        List<Langilea> before = BiltegiLocala.getLangileak();
        int id = before.stream()
                .filter(l -> l.getErabiltzailea().equals("ezabattest"))
                .mapToInt(Langilea::getLangileId).findFirst().orElse(-1);
        assertNotEquals(-1, id);

        boolean ok = BiltegiLocala.langileaEzabatu(id);
        assertTrue(ok);
        List<Langilea> after = BiltegiLocala.getLangileak();
        assertFalse(after.stream().anyMatch(l -> l.getErabiltzailea().equals("ezabattest")));
    }

    // ─── KATEGORIAK ─────────────────────────────────────────────────────────

    @Test @Order(20)
    @DisplayName("Kategoria gehitu eta zerrendan agertu")
    public void testKategoriaGehitu() {
        boolean ok = BiltegiLocala.kategoriaGehitu("TestKategoria");
        assertTrue(ok);
        List<Kategoria> kategoriak = BiltegiLocala.getKategoriak();
        assertTrue(kategoriak.stream().anyMatch(k -> k.getIzena().equals("TestKategoria")));
    }

    @Test @Order(21)
    @DisplayName("Kategoria eguneratu")
    public void testKategoriaEguneratu() {
        BiltegiLocala.kategoriaGehitu("EguneratuBehar");
        List<Kategoria> list = BiltegiLocala.getKategoriak();
        int id = list.stream().filter(k -> k.getIzena().equals("EguneratuBehar"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(-1);
        assertNotEquals(-1, id);

        boolean ok = BiltegiLocala.kategoriaAldatuIzena(id, "EguneratutaOk");
        assertTrue(ok);
        assertTrue(BiltegiLocala.getKategoriak().stream()
                .anyMatch(k -> k.getIzena().equals("EguneratutaOk")));
    }

    @Test @Order(22)
    @DisplayName("Kategoria ezabatu")
    public void testKategoriaEzabatu() {
        BiltegiLocala.kategoriaGehitu("EzabatuKat");
        int id = BiltegiLocala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("EzabatuKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(-1);
        assertNotEquals(-1, id);
        assertTrue(BiltegiLocala.kategoriaEzabatu(id));
        assertFalse(BiltegiLocala.getKategoriak().stream()
                .anyMatch(k -> k.getKategoriaId() == id));
    }

    // ─── KOKALEKUAK ─────────────────────────────────────────────────────────

    @Test @Order(30)
    @DisplayName("Kokalekua gehitu eta zerrendan agertu")
    public void testKokalekuaGehitu() {
        boolean ok = BiltegiLocala.kokalekuaGehitu("A", "1", false);
        assertTrue(ok);
        List<Kokalekua> koks = BiltegiLocala.getKokalekuak();
        assertTrue(koks.stream().anyMatch(k -> k.getArmairua().equals("A") && k.getApala().equals("1")));
    }

    @Test @Order(31)
    @DisplayName("Kokalekua eguneratu")
    public void testKokalekuaEguneratu() {
        BiltegiLocala.kokalekuaGehitu("B", "2", false);
        int id = BiltegiLocala.getKokalekuak().stream()
                .filter(k -> k.getArmairua().equals("B") && k.getApala().equals("2"))
                .mapToInt(Kokalekua::getKokalekuId).findFirst().orElse(-1);
        assertNotEquals(-1, id);
        assertTrue(BiltegiLocala.kokalekuaEguneratu(id, "B", "3", false));
        assertTrue(BiltegiLocala.getKokalekuak().stream()
                .anyMatch(k -> k.getKokalekuId() == id && k.getApala().equals("3")));
    }

    @Test @Order(32)
    @DisplayName("Kokalekua ezabatu")
    public void testKokalekuaEzabatu() {
        BiltegiLocala.kokalekuaGehitu("C", "99", false);
        int id = BiltegiLocala.getKokalekuak().stream()
                .filter(k -> k.getArmairua().equals("C") && k.getApala().equals("99"))
                .mapToInt(Kokalekua::getKokalekuId).findFirst().orElse(-1);
        assertNotEquals(-1, id);
        assertTrue(BiltegiLocala.kokalekuaEzabatu(id));
        assertFalse(BiltegiLocala.getKokalekuak().stream()
                .anyMatch(k -> k.getKokalekuId() == id));
    }

    // ─── ARTIKULUAK ─────────────────────────────────────────────────────────

    private static final Date DATA = Date.valueOf("2025-01-15");

    @Test @Order(40)
    @DisplayName("Artikulua gehitu eta zerrendan agertu")
    public void testArtikuluaGehitu() {
        BiltegiLocala.kategoriaGehitu("ArtKat");
        BiltegiLocala.kokalekuaGehitu("X", "1", false);
        int idKat = BiltegiLocala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("ArtKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);
        int idKok = BiltegiLocala.getKokalekuak().stream()
                .filter(k -> k.getArmairua().equals("X"))
                .mapToInt(Kokalekua::getKokalekuId).findFirst().orElse(0);

        String kodea = BiltegiLocala.artikuluaGehitu("Zorroa", "Larruzko zorroa gorria",
                false, idKat, idKok, DATA, "");
        assertNotNull(kodea, "Gehitutako artikuluak kode bat eduki behar du");
        assertTrue(kodea.startsWith("G-"), "Kodeak G- aurrizkiarekin hasi behar du: " + kodea);
    }

    @Test @Order(41)
    @DisplayName("Artikulua bilatu kodearengatik")
    public void testArtikuluaBilatuKodea() {
        BiltegiLocala.kategoriaGehitu("BilKat");
        int idKat = BiltegiLocala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("BilKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        String kodea = BiltegiLocala.artikuluaGehitu("Betaurrekoak", "Betaurrekoak berde",
                false, idKat, 0, DATA, "");
        assertNotNull(kodea);

        Artikulua a = BiltegiLocala.getArtikuluaByKodea(kodea);
        assertNotNull(a, "Kodearen bidez aurkitu behar da");
        assertEquals("Betaurrekoak", a.getIzenburua());
    }

    @Test @Order(42)
    @DisplayName("Artikulua eguneratu")
    public void testArtikuluaEguneratu() {
        BiltegiLocala.kategoriaGehitu("EgKat");
        int idKat = BiltegiLocala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("EgKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        String kodea = BiltegiLocala.artikuluaGehitu("GiltzeZaharra", "Giltza bat",
                false, idKat, 0, DATA, "");
        assertNotNull(kodea);

        boolean ok = BiltegiLocala.artikuluaEguneratu(kodea, "GiltzeBerria", "Giltza berri bat", idKat, 0, "");
        assertTrue(ok);
        assertEquals("GiltzeBerria", BiltegiLocala.getArtikuluaByKodea(kodea).getIzenburua());
    }

    @Test @Order(43)
    @DisplayName("Artikulua ezabatu")
    public void testArtikuluaEzabatu() {
        BiltegiLocala.kategoriaGehitu("EzKat");
        int idKat = BiltegiLocala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("EzKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        String kodea = BiltegiLocala.artikuluaGehitu("EzabatuArt", "Ezabatu behar da",
                false, idKat, 0, DATA, "");
        assertNotNull(kodea);

        boolean ok = BiltegiLocala.artikuluaEzabatu(kodea);
        assertTrue(ok);
        assertNull(BiltegiLocala.getArtikuluaByKodea(kodea), "Ezabatu ondoren null itzuli behar da");
    }

    // ─── ESTADISTIKAK ────────────────────────────────────────────────────────

    @Test @Order(50)
    @DisplayName("Estadistika: biltegian kopurua >= 0")
    public void testBiltegianKopurua() {
        int kop = BiltegiLocala.biltegianKopurua();
        assertTrue(kop >= 0, "Biltegian kopurua negatiboa ezin da izan");
    }

    @Test @Order(51)
    @DisplayName("Estadistika: kategoria kopurua >= 0")
    public void testKategoriaKopurua() {
        int kop = BiltegiLocala.kategoriaKopurua();
        assertTrue(kop >= 0);
    }

    @Test @Order(52)
    @DisplayName("Estadistika: langile kopurua >= 0")
    public void testLangileKopurua() {
        int kop = BiltegiLocala.langileKopurua();
        assertTrue(kop >= 0);
    }

    @Test @Order(53)
    @DisplayName("Estadistika: artikulu guztien kopurua zerrendarekiko koherentea")
    public void testArtikuluGuztienKopurua() {
        int kop = BiltegiLocala.artikuluGuztienKopurua();
        int zerrendaKop = BiltegiLocala.getArtikuluak().size();
        assertEquals(zerrendaKop, kop,
                "artikuluGuztienKopurua eta getArtikuluak().size() berdinak izan behar dira");
    }

    // ─── ERREKLAMAZIOAK ──────────────────────────────────────────────────────

    @Test @Order(60)
    @DisplayName("Erreklamazioa gorde eta zerrendan agertu")
    public void testErreklamazioaGorde() {
        BiltegiLocala.kategoriaGehitu("ErrKat");
        int idKat = BiltegiLocala.getKategoriak().stream()
                .filter(k -> k.getIzena().equals("ErrKat"))
                .mapToInt(Kategoria::getKategoriaId).findFirst().orElse(0);

        boolean ok = BiltegiLocala.erreklamazioaGorde(
                "12345678A", "Mikel", "Uriarte", "600000001", "mikel@test.com",
                idKat, "Larruzko zorro gorri bat", 1);
        assertTrue(ok, "Erreklamazioa gorde behar da");
        assertFalse(BiltegiLocala.getErreklamazioak().isEmpty(),
                "Erreklamazioen zerrenda ez da hutsik egon behar");
    }

    @Test @Order(61)
    @DisplayName("Erreklamazioaren egoera eguneratu")
    public void testErreklamazioaEgoera() {
        List<Erreklamazioa> errek = BiltegiLocala.getErreklamazioak();
        if (errek.isEmpty()) return;
        String id = String.valueOf(errek.get(0).getErreklamazioId());
        boolean ok = BiltegiLocala.erreklamazioaUpdateEgoera(id, "ebatzita");
        assertTrue(ok);
    }
}
