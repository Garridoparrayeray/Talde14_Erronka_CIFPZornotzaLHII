package model;

public class Erakundea extends Hartzailea {

    private String ifz;
    private String izenFiskala;
    private String erakundeMota;

    public Erakundea(String ifz, String izenFiskala, String erakundeMota, String telefonoa) {
        super(0, telefonoa, null);
        this.ifz = ifz;
        this.izenFiskala = izenFiskala;
        this.erakundeMota = erakundeMota;
    }

    @Override
    public String getIdentifikazioa() {
        return ifz;
    }

    // Getters
    public String getIfz() { return ifz; }
    public String getIzenFiskala() { return izenFiskala; }
    public String getErakundeMota() { return erakundeMota; }
}
