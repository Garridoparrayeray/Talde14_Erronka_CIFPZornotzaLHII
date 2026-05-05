package model;

import java.util.List;

/**
 * Sistemako langile bat adierazten duen eredua. Administratzailea klasearen
 * oinarria da.
 *
 * @author Yeray Garrido
 */
public class Langilea {

    private int langileId;
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
    public Langilea(int langileId, String izena, String abizena, String erabiltzailea, String pasahitza) {
        this.langileId = langileId;
        this.izena = izena;
        this.abizena = abizena;
        this.erabiltzailea = erabiltzailea;
        this.pasahitzaHash = pasahitza;
        this.rola = "";
    }

    /**
     * Artikulu bat erregistratzen du langilearen izenean.
     *
     * @param a Erregistratu beharreko artikulua
     * @return Ondo erregistratu bada true
     */
    public boolean erregistratuArtikulua(Artikulua a) {
        return false;
    }

    /**
     * Erreklamazino bat erregistratzen du langilearen izenean.
     *
     * @param e Erregistratu beharreko erreklamazioa
     * @return Ondo erregistratu bada true
     */
    public boolean erregistratuErreklamazioa(Erreklamazioa e) {
        return false;
    }

    /**
     * Emanaldia bat kudeatzen du.
     *
     * @param em Kudeatu beharreko emanaldia
     * @return Ondo kudeatu bada true
     */
    public boolean kudeatuEmanaldia(Emanaldia em) {
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

    // Getters & Setters
    public int getLangileId() {
        return langileId;
    }

    public String getIzena() {
        return izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public String getIzenOsoa() {
        return izena + " " + abizena;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public String getRola() {
        return rola;
    }

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

    @Override
    public String toString() {
        return izena + " " + abizena + " (" + erabiltzailea + ")";
    }
}
