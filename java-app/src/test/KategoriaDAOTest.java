package dao;

import model.Kategoria;
import org.junit.jupiter.api.*;
import utils.DBConexioa;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
            Connection con = DBConexioa.getKonexioa();
            assertNotNull(con, "Datu-basearekin konexioa ez da null izan behar");
        }, "Ez luke salbuespenik bota behar datu-basera konektatzean");
    }

    @Test
    @Order(1)
    @DisplayName("1. INSERT: Kategoria berria ondo gehitzen dela frogatu")
    public void testGehituKategoria() {
        // Suposatzen dugu KategoriaDAO-k 'gehitu' metodo bat duela.
        // (KategoriaDAO.gehitu(TEST_IZENA) edo KategoriaDAO.gehitu(kategoria_objektua) bada, egokitu mesedez)
        Kategoria katBerria = new Kategoria();
        katBerria.setIzena(TEST_IZENA);
        // KategoriaDAO.gehitu() String bat jasotzen du eta boolean bat itzultzen du
        boolean emaitza = KategoriaDAO.gehitu(TEST_IZENA);
        assertTrue(emaitza, "Kategoria ondo txertatu behar da datu-basean");
        
        // Hemen zure benetako DAO metodoa deitu (Adib: KategoriaDAO.gehitu(katBerria))
        // Emaitza txertatzea ondo badoa, zerrendan agertu beharko litzateke
        
        // Egiaztatu zerrendan agertzen dela eta bere ID-a lortu
        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        assertFalse(kategoriak.isEmpty(), "Kategorien zerrenda ezin da hutsik egon datu-basean");
        
        // Kategoriaren ID-a bilatu eta gorde hurrengo testetarako
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
        // DAO-aren aldatuIzena metodoa erabiliz
        KategoriaDAO.aldatuIzena(testKategoriaId, TEST_IZENA_BERRIA);
        // Eguneraketa egin eta true itzultzen duela egiaztatu
        boolean emaitza = KategoriaDAO.aldatuIzena(testKategoriaId, TEST_IZENA_BERRIA);
        assertTrue(emaitza, "Eguneraketa prozesuak true itzuli behar du");
        
        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        
        // assertAll erabilera
        assertAll("Eguneraketa egiaztapenak",
            () -> assertTrue(kategoriak.stream().anyMatch(k -> k.getIzena().equals(TEST_IZENA_BERRIA)), "Kategoriaren izen berria agertu behar da"),
            () -> assertFalse(kategoriak.stream().anyMatch(k -> k.getIzena().equals(TEST_IZENA)), "Izen zaharra ez da agertu behar")
        );
    }

    @Test
    @Order(4)
    @DisplayName("4. DELETE: Kategoria ondo ezabatzen dela frogatu")
    public void testEzabatuKategoria() {
        // Hemen zure KategoriaDAO.ezabatu(testKategoriaId) deitu
        boolean emaitza = KategoriaDAO.ezabatu(testKategoriaId);
        assertTrue(emaitza, "Ezabatze prozesuak true itzuli behar du");
        
        List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
        boolean aurkitua = kategoriak.stream().anyMatch(k -> k.getKategoriaId() == testKategoriaId);
        
        // Egiaztatu ezabatu ondoren ez dela existitzen
        assertFalse(aurkitua, "Ezabatu ondoren, kategoria hori ez litzateke zerrendan egon behar");
    }
}