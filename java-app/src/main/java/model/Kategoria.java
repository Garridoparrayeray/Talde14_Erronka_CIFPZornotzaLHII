package model;

/**
 * Artikuluen sailkapen kategoria bat adierazten duen eredua.
 * @author Yeray Garrido
 */
public class Kategoria {

    private int kategoriaId;
    private String izena;

    /**
     * Kategoriaren eraikitzailea.
     * @param kategoriaId Kategoriaren identifikagailu bakarra
     * @param izena Kategoriaren izena
     */
    public Kategoria(int kategoriaId, String izena) {
        this.kategoriaId = kategoriaId;
        this.izena = izena;
    }

    // Getters
    public int getKategoriaId() { return kategoriaId; }
    public String getIzena() { return izena; }

    @Override
    public String toString() {
        return izena;
    }
}
