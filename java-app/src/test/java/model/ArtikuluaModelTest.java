package model;

import org.junit.jupiter.api.*;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Artikulua modeloaren metodo garrantzitsuenak probatzen dituen test-a.
 * Datu-baserik behar ez du — objektu hutsak erabiltzen ditu.
 *
 * @author Yeray Garrido
 */
@DisplayName("Artikulua modelo-testa")
public class ArtikuluaModelTest {

    private static Artikulua artikulua(String kodea, String izena) {
        return new Artikulua(kodea, izena, null, "", "", new Date(), null);
    }

    @Test
    @DisplayName("getEgoeraTestua: egoera hasierakoa BILTEGIAN → 'Biltegian'")
    public void testEgoeraBiltegian() {
        Artikulua a = artikulua("G-001-26", "Proba");
        assertEquals("Biltegian", a.getEgoeraTestua());
    }

    @Test
    @DisplayName("getEgoeraTestua: ITZULITA → 'Itzulita'")
    public void testEgoeraItzulita() {
        Artikulua a = artikulua("G-002-26", "Proba");
        a.aldatuEgoera(EgoeraArtikulua.ITZULITA);
        assertEquals("Itzulita", a.getEgoeraTestua());
    }

    @Test
    @DisplayName("getEgoeraTestua: IRAUNGITA → 'Iraungita'")
    public void testEgoeraIraungita() {
        Artikulua a = artikulua("G-003-26", "Proba");
        a.aldatuEgoera(EgoeraArtikulua.IRAUNGITA);
        assertEquals("Iraungita", a.getEgoeraTestua());
    }

    @Test
    @DisplayName("getKategoriaIzena: kategoria null → '—'")
    public void testKategoriaNullaDash() {
        Artikulua a = artikulua("G-004-26", "Proba");
        a.setKategoria(null);
        assertEquals("—", a.getKategoriaIzena());
    }

    @Test
    @DisplayName("getKategoriaIzena: kategoria ezarrita → izena")
    public void testKategoriaIzena() {
        Artikulua a = artikulua("G-005-26", "Proba");
        Kategoria k = new Kategoria(1, "Giltzak");
        a.setKategoria(k);
        assertEquals("Giltzak", a.getKategoriaIzena());
    }

    @Test
    @DisplayName("getDeskribapenaSegurua: null → '—'")
    public void testDeskribapenaNull() {
        Artikulua a = new Artikulua("G-006-26", "Proba", null, "", "", new Date(), null);
        assertEquals("—", a.getDeskribapenaSegurua());
    }

    @Test
    @DisplayName("getDeskribapenaSegurua: testua itzultzen du")
    public void testDeskribapenaTestua() {
        Artikulua a = new Artikulua("G-007-26", "Proba", "Larruzko zorroa", "", "", new Date(), null);
        assertEquals("Larruzko zorroa", a.getDeskribapenaSegurua());
    }

    @Test
    @DisplayName("getKokalekuaIzena: null → '—'")
    public void testKokalekuaNull() {
        Artikulua a = artikulua("G-008-26", "Proba");
        a.setKokalekua(null);
        assertEquals("—", a.getKokalekuaIzena());
    }

    @Test
    @DisplayName("getArtikuluKodea: konstruktore-kodea itzultzen du")
    public void testArtikuluKodea() {
        Artikulua a = artikulua("G-001-25", "Proba");
        assertEquals("G-001-25", a.getArtikuluKodea());
    }

    @Test
    @DisplayName("Artikulua bi instantzia independenteak dira")
    public void testIndependentzia() {
        Artikulua a1 = artikulua("G-001-25", "Proba1");
        Artikulua a2 = artikulua("G-002-25", "Proba2");
        assertNotEquals(a1.getArtikuluKodea(), a2.getArtikuluKodea());
    }
}
