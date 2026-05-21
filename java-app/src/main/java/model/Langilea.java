package model;

import java.io.Serializable;
import java.util.List;

/**
 * Sistemako langile bat adierazten duen eredua. Administratzailea klasearen
 * oinarria da. {@code BiltegiLokala}-k serializatu egiten du offline
 * gordetzeko, beraz {@code Serializable} interfazea ezartzen du.
 *
 * @author Yeray Garrido
 */
public class Langilea implements Serializable {

    private static final long serialVersionUID = 1L;

    private final int langileId;
    private String izena;
    private String abizena;
    private String erabiltzailea;
    private String pasahitzaHash;
    private String rola;

    /**
     * Langilearen eraikitzailea.
     *
     * @param langileId Langilearen identifikagailu bakarra
     * @param izena Langilearen izena
     * @param abizena Langilearen abizena
     * @param erabiltzailea Saioa hasteko erabiltzaile-izena
     * @param pasahitza Pasahitzaren hash-a (BCrypt)
     */
    public Langilea(int langileId, String izena, String abizena,
            String erabiltzailea, String pasahitza) {
        this.langileId = langileId;
        this.izena = izena;
        this.abizena = abizena;
        this.erabiltzailea = erabiltzailea;
        this.pasahitzaHash = pasahitza;
        this.rola = "";
    }

    /**
     * Artikulu bat erregistratzen du langilearen izenean. Azpiklaseek
     * gainidatzi behar dute.
     *
     * @param artikulua Erregistratu beharreko artikulua
     * @return Ondo erregistratu bada true
     */
    public boolean erregistratuArtikulua(Artikulua artikulua) {
        return false;
    }

    /**
     * Erreklamazino bat erregistratzen du langilearen izenean. Azpiklaseek
     * gainidatzi behar dute.
     *
     * @param erreklamazioa Erregistratu beharreko erreklamazioa
     * @return Ondo erregistratu bada true
     */
    public boolean erregistratuErreklamazioa(Erreklamazioa erreklamazioa) {
        return false;
    }

    /**
     * Emanaldia bat kudeatzen du. Azpiklaseek gainidatzi behar dute.
     *
     * @param emanaldia Kudeatu beharreko emanaldia
     * @return Ondo kudeatu bada true
     */
    public boolean kudeatuEmanaldia(Emanaldia emanaldia) {
        return false;
    }

    /**
     * Langileari lotutako mugimenduen trazabilitatea itzultzen du.
     *
     * @return Mugimenduen zerrenda
     */
    public List<Mugimendua> getTrazabilitatea() {
        return null;
    }

    /**
     * Langilearen datu-baseko IDa itzultzen du.
     *
     * @return Langilearen IDa
     */
    public int getLangileId() {
        return langileId;
    }

    /**
     * Langilearen izena itzultzen du.
     *
     * @return Izena
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Langilearen izena ezartzen du.
     *
     * @param izena Ezarri beharreko izena
     */
    public void setIzena(String izena) {
        this.izena = izena;
    }

    /**
     * Langilearen abizena itzultzen du.
     *
     * @return Abizena
     */
    public String getAbizena() {
        return abizena;
    }

    /**
     * Langilearen abizena ezartzen du.
     *
     * @param abizena Ezarri beharreko abizena
     */
    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    /**
     * Izena eta abizena elkarturik itzultzen du.
     *
     * @return Izena eta abizena zuriunez bereizita
     */
    public String getIzenOsoa() {
        return izena + " " + abizena;
    }

    /**
     * Saioa hasteko erabiltzaile-izena itzultzen du.
     *
     * @return Erabiltzaile-izena
     */
    public String getErabiltzailea() {
        return erabiltzailea;
    }

    /**
     * Saioa hasteko erabiltzaile-izena ezartzen du.
     *
     * @param erabiltzailea Ezarri beharreko erabiltzaile-izena
     */
    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    /**
     * BCrypt hash-a itzultzen du; offline autentifikaziorako.
     *
     * @return Pasahitzaren BCrypt hash-a
     */
    public String getPasahitzaHash() {
        return pasahitzaHash;
    }

    /**
     * BCrypt hash berria ezartzen du pasahitza aldatzean.
     *
     * @param hash Ezarri beharreko BCrypt hash berria
     */
    public void setPasahitzaHash(String hash) {
        this.pasahitzaHash = hash;
    }

    /**
     * Langilearen sistema-rola itzultzen du.
     *
     * @return Rola (adib. "Administratzailea", "Ikuslea")
     */
    public String getRola() {
        return rola;
    }

    /**
     * Langilearen sistema-rola ezartzen du.
     *
     * @param rola Ezarri beharreko rola
     */
    public void setRola(String rola) {
        this.rola = rola;
    }

    /**
     * Langilea administratzailea den egiaztatzen du erola konparatuz.
     *
     * @return Administratzailea bada true
     */
    public boolean isAdmin() {
        return "Administratzailea".equalsIgnoreCase(this.rola);
    }

    /**
     * Langilea ikuslea den egiaztatzen du erola konparatuz. Ikusleak artikuluak
     * ikusi eta erregistratu baino ezin dituzte egin.
     *
     * @return Ikuslea bada true
     */
    public boolean isIkuslea() {
        return "Ikuslea".equalsIgnoreCase(this.rola);
    }

    /**
     * Langilearen testuzko adierazpena itzultzen du.
     *
     * @return Izena, abizena eta erabiltzaile-izena
     */
    @Override
    public String toString() {
        return izena + " " + abizena + " (" + erabiltzailea + ")";
    }
}
