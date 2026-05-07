package model;

/**
 * Galdu diren objektuak jasotzeko erakundea adierazten duen eredua. Hartzailea
 * klasearen azpiklasea da.
 *
 * @author Yeray Garrido
 */
public class Erakundea extends Hartzailea {

    private String ifz;
    private String izenFiskala;
    private String erakundeMota;

    /**
     * Erakundearen eraikitzailea.
     *
     * @param ifz Identifikazio Fiskaleko Zenbakia
     * @param izenFiskala Erakundearen izen ofiziala
     * @param erakundeMota Erakundearen mota (GKE, udala, etab.)
     * @param telefonoa Kontaktu telefonoa
     */
    public Erakundea(String ifz, String izenFiskala, String erakundeMota, String telefonoa) {
        super(0, telefonoa, null);
        this.ifz = ifz;
        this.izenFiskala = izenFiskala;
        this.erakundeMota = erakundeMota;
    }

    /**
     * @return Erakundearen IFZ identifikazio gisa
     */
    @Override
    public String getIdentifikazioa() {
        return ifz;
    }

    // Getters
    /**
     * Erakundearen Identifikazio Fiskaleko Zenbakia itzultzen du.
     *
     * @return IFZ testua
     */
    public String getIfz() {
        return ifz;
    }

    /**
     * Erakundearen izen ofiziala itzultzen du.
     *
     * @return Izen fiskala
     */
    public String getIzenFiskala() {
        return izenFiskala;
    }

    /**
     * Erakundearen mota itzultzen du.
     *
     * @return Erakundearen mota (GKE, udala, etab.)
     */
    public String getErakundeMota() {
        return erakundeMota;
    }
}
