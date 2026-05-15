package model;

/**
 * Galdu duen objektuaren jabe fisikoa adierazten duen eredua. Hartzailea
 * klasearen azpiklasea da.
 *
 * @author Yeray Garrido
 */
public class Jabea extends Hartzailea {

    private String nan;
    private String izena;
    private String abizena;

    /**
     * Jabearen eraikitzailea.
     *
     * @param nan       Nortasun Agiri Nazionala
     * @param izena     Jabearen izena
     * @param abizena   Jabearen abizena
     * @param telefonoa Kontaktu telefonoa
     * @param emaila    Kontaktu helbide elektronikoa
     */
    public Jabea(String nan, String izena, String abizena, String telefonoa, String emaila) {
        super(0, telefonoa, emaila);
        this.nan = nan;
        this.izena = izena;
        this.abizena = abizena;
    }

    /**
     * Jabearen identifikazioa NAN gisa itzultzen du.
     *
     * @return NAN testua
     */
    @Override
    public String getIdentifikazioa() {
        return nan;
    }

    /**
     * Jabearen izena itzultzen du.
     *
     * @return Izena
     */
    @Override
	public String getIzena() {
        return izena;
    }

    /**
     * Jabearen abizena itzultzen du.
     *
     * @return Abizena
     */
    @Override
	public String getAbizena() {
        return abizena;
    }

    /**
     * Jabearen NAN zenbakia itzultzen du.
     *
     * @return NAN testua
     */
    @Override
	public String getNan() {
        return nan;
    }
}
