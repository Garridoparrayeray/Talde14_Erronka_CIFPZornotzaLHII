package model;

/**
 * Kategoria bakoitzeko artikulu kopurua gordetzeko eredua.
 * Taula grafikoan datuak behar bezala erakusteko erabiltzen da.
 * @author Yeray Garrido
 */
public class KategoriaKopurua {
    private String kategoriaIzena;
    private int kopurua;

    /**
     * Ereduaren eraikitzailea.
     * @param kategoriaIzena Kategoriaren izena
     * @param kopurua Zenbat artikulu dauden kategoria horretan
     */
    public KategoriaKopurua(String kategoriaIzena, int kopurua) {
        this.kategoriaIzena = kategoriaIzena;
        this.kopurua = kopurua;
    }

    // ── Getterrak eta Setterrak ─────────────────────────────────────────────

    public String getKategoriaIzena() {
        return kategoriaIzena;
    }

    public void setKategoriaIzena(String kategoriaIzena) {
        this.kategoriaIzena = kategoriaIzena;
    }

    public int getKopurua() {
        return kopurua;
    }

    public void setKopurua(int kopurua) {
        this.kopurua = kopurua;
    }
}