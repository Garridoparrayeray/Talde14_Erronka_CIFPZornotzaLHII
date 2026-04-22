package eus.zornotza.galdutakoak.model;

public class Jabea extends Hartzailea {

    private String nan;
    private String izena;
    private String abizena;

    public Jabea(String nan, String izena, String abizena, String telefonoa, String emaila) {
        super(0, telefonoa, emaila);
        this.nan = nan;
        this.izena = izena;
        this.abizena = abizena;
    }

    @Override
    public String getIdentifikazioa() {
        return nan;
    }

    // Getters
    public String getNan() { return nan; }
    public String getIzena() { return izena; }
    public String getAbizena() { return abizena; }
}
