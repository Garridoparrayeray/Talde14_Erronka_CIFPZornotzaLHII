package eus.zornotza.galdutakoak.model;

public class Kategoria {

    private int kategoriaId;
    private String izena;

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
