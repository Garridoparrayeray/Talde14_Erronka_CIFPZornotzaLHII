package model;

import java.io.Serializable;

/**
 * Artikuluen sailkapen kategoria bat adierazten duen eredua.
 *
 * @author Yeray Garrido
 */
public class Kategoria implements Serializable {

    private static final long serialVersionUID = 1L;

    private int kategoriaId;
    private String izena;

    /**
     * Kategoriaren eraikitzailea.
     *
     * @param kategoriaId Kategoriaren identifikagailu bakarra
     * @param izena       Kategoriaren izena
     */
    public Kategoria(int kategoriaId, String izena) {
        this.kategoriaId = kategoriaId;
        this.izena = izena;
    }

    // Getters
    /**
     * Kategoriaren datu-baseko IDa itzultzen du.
     *
     * @return Kategoriaren IDa
     */
    public int getKategoriaId() {
        return kategoriaId;
    }

    /**
     * Kategoriaren izena itzultzen du.
     *
     * @return Izena
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Kategoriaren izena ezartzen du.
     *
     * @param izena Ezarri beharreko izena
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    @Override
    public String toString() {
        return izena;
    }
}
