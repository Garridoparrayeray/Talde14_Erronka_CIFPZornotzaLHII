package model;

/**
 * Kategoria bakoitzeko artikulu kopurua gordetzeko eredua. Taula grafikoan
 * datuak behar bezala erakusteko erabiltzen da.
 *
 * @author Yeray Garrido
 */
public class KategoriaKopurua {

    private String kategoriaIzena;
    private int kopurua;

    /**
     * Ereduaren eraikitzailea.
     *
     * @param kategoriaIzena Kategoriaren izena
     * @param kopurua        Zenbat artikulu dauden kategoria horretan
     */
    public KategoriaKopurua(String kategoriaIzena, int kopurua) {
        this.kategoriaIzena = kategoriaIzena;
        this.kopurua = kopurua;
    }

    // ── Getterrak eta Setterrak ─────────────────────────────────────────────
    /**
     * Kategoriaren izena itzultzen du, null bada "—".
     *
     * @return Kategoriaren izena, edo "—" null bada
     */
    public String getKategoriaIzena() {
        if (kategoriaIzena != null) {
            return kategoriaIzena;
        }
        return "—";
    }

    /**
     * Kategoriaren izena ezartzen du.
     *
     * @param kategoriaIzena Ezarri beharreko izena
     */
    public void setKategoriaIzena(String kategoriaIzena) {
        this.kategoriaIzena = kategoriaIzena;
    }

    /**
     * Kategoriako artikulu kopurua itzultzen du.
     *
     * @return Kopurua
     */
    public int getKopurua() {
        return kopurua;
    }

    /**
     * Artikulu kopurua kate gisa itzultzen du.
     *
     * @return Kopurua String gisa
     */
    public String getKopuruaStr() {
        return String.valueOf(kopurua);
    }

    /**
     * Kategoriako artikulu kopurua ezartzen du.
     *
     * @param kopurua Ezarri beharreko kopurua
     */
    public void setKopurua(int kopurua) {
        this.kopurua = kopurua;
    }
}
