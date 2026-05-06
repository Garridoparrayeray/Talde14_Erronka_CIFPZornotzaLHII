package model;

/**
 * Galdu duen objektuaren jabe fisikoa adierazten duen eredua. Hartzailea
 * klasearen azpiklasea da.
 *
 * @author Yeray Garrido
 */
public class Jabea extends Hartzailea {

    private static final long serialVersionUID = 1L;

    private String nan;
    private String izena;
    private String abizena;

    /**
     * Jabearen eraikitzailea.
     *
     * @param nan Nortasun Agiri Nazionala
     * @param izena Jabearen izena
     * @param abizena Jabearen abizena
     * @param telefonoa Kontaktu telefonoa
     * @param emaila Kontaktu helbide elektronikoa
     */
    public Jabea(String nan, String izena, String abizena, String telefonoa, String emaila) {
        super(0, telefonoa, emaila);
        this.nan = nan;
        this.izena = izena;
        this.abizena = abizena;
    }

    /**
     * @return Jabearen NAN zenbakia identifikazio gisa
     */
    @Override
    public String getIdentifikazioa() {
        return nan;
    }

    public String getIzena() {
        return izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public String getNan() {
        return nan;
    }
}
