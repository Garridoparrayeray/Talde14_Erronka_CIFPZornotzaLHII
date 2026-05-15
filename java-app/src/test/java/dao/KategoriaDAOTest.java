package dao;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import model.Kategoria;
import utils.DBKonexioa;

/**
 * KategoriaDAO klasearen CRUD eragiketak probatzeko Test Unitarioak.
 *
 * @author Eder Martin Mosquera
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KategoriaDAOTest {

    private static int testKategoriaId = -1;
    private static final String TEST_IZENA = "JUNIT Test Kategoria";
    private static final String TEST_IZENA_BERRIA = "JUNIT Kategoria Eguneratua";

    @BeforeAll
    @DisplayName("Datu-basearen konexioa egiaztatu testak hasi aurretik")
    public static void setUp() {
        assertDoesNotThrow(() -> {
            Connection con = DBKonexioa.getKonexioa();
            assertNotNull(con, "Datu-basearekin konexioa ez da null izan behar");
        }, "Ez luke salbuespenik bota behar datu-basera konektatzean");
    }

    @Test
    @Order(1)
    @DisplayName("1. INSERT: Kategoria berria ondo gehitzen dela frogatu")
    public void testGehituKategoria() {
        boolean emaitza = KategoriaDAO.gehitu(TEST_IZENA);
        assertTrue(emaitza, "Kategoria ondo txertatu behar da datu-basean");
        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        assertFalse(kategoriak.isEmpty(), "Kategorien zerrenda ezin da hutsik egon datu-basean");

        for (Kategoria k : kategoriak) {
            if (k.getIzena().equals(TEST_IZENA)) {
                testKategoriaId = k.getKategoriaId();
                break;
            }
        }

        assertNotEquals(-1, testKategoriaId, "Gehitutako test kategoriaren IDa lortu behar dugu DBtik");
    }

    @Test
    @Order(2)
    @DisplayName("2. SELECT: Kategoriak datu-basetik ondo irakurtzen direla frogatu")
    public void testLortuKategoriak() {
        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();

        boolean aurkitua = kategoriak.stream()
                .anyMatch(k -> k.getKategoriaId() == testKategoriaId && k.getIzena().equals(TEST_IZENA));

        assertTrue(aurkitua, "Sortutako kategoria zerrendan agertu behar da");
    }

    @Test
    @Order(3)
    @DisplayName("3. UPDATE: Kategoriaren izena ondo eguneratzen dela frogatu")
    public void testEguneratuKategoria() {
        KategoriaDAO.aldatuIzena(testKategoriaId, TEST_IZENA_BERRIA);
        boolean emaitza = KategoriaDAO.aldatuIzena(testKategoriaId, TEST_IZENA_BERRIA);
        assertTrue(emaitza, "Eguneraketa prozesuak true itzuli behar du");

        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();

        assertAll("Eguneraketa egiaztapenak",
                () -> assertTrue(kategoriak.stream().anyMatch(k -> k.getIzena().equals(TEST_IZENA_BERRIA)), "Kategoriaren izen berria agertu behar da"),
                () -> assertFalse(kategoriak.stream().anyMatch(k -> k.getIzena().equals(TEST_IZENA)), "Izen zaharra ez da agertu behar")
        );
    }

    @Test
    @Order(4)
    @DisplayName("4. DELETE: Kategoria ondo ezabatzen dela frogatu")
    public void testEzabatuKategoria() {
        boolean emaitza = KategoriaDAO.ezabatu(testKategoriaId);
        assertTrue(emaitza, "Ezabatze prozesuak true itzuli behar du");

        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        boolean aurkitua = kategoriak.stream().anyMatch(k -> k.getKategoriaId() == testKategoriaId);

        assertFalse(aurkitua, "Ezabatu ondoren, kategoria hori ez litzateke zerrendan egon behar");
    }
}
