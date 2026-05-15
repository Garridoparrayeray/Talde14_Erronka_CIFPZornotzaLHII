package model;

import java.util.Date;

/**
 * Artikulu bat hartzaile bati ematearen eragiketa adierazten duen eredua.
 *
 * @author Yeray Garrido
 */
public class Emanaldia {

    private int emanaldiId;
    private Date emateData;
    private String dokumentuBidea;
    private String oharrak;
    private Artikulua artikulua;
    private Hartzailea hartzailea;
    private Langilea langilea;

    /**
     * Emanaldiaren eraikitzailea.
     *
     * @param artikulua  Ematen den artikulua
     * @param hartzailea Artikulua jasotzen duen hartzailea
     * @param langilea   Eragiketa kudeatzen duen langilea
     * @param emateData  Emateko data
     */
    public Emanaldia(Artikulua artikulua, Hartzailea hartzailea,
            Langilea langilea, Date emateData) {
        this.artikulua = artikulua;
        this.hartzailea = hartzailea;
        this.langilea = langilea;
        this.emateData = emateData;
    }

    /**
     * Sinadura-dokumentuaren bidea gordetzen du.
     *
     * @param bidea Dokumentuaren fitxategi-bidea
     */
    public void gordeDokumentua(String bidea) {
        this.dokumentuBidea = bidea;
    }

    /**
     * Emanaldia amaitutzat markatzen du eta artikuluaren egoera eguneratzen du.
     *
     * @return Dokumentua badago eta emanaldia ondo amaitu bada true
     */
    public boolean amaituEmanaldia() {
        if (dokumentuBidea != null && !dokumentuBidea.isEmpty()) {
            artikulua.aldatuEgoera(EgoeraArtikulua.ITZULITA);
            return true;
        }
        return false;
    }

    // Getters
    /**
     * Emanaldiren datu-baseko IDa itzultzen du.
     *
     * @return Emanaldiaren IDa
     */
    public int getEmanaldiId() {
        return emanaldiId;
    }

    /**
     * Emanaldiaren data itzultzen du.
     *
     * @return Emanaldiaren data
     */
    public Date getEmateData() {
        return emateData;
    }

    /**
     * Sinadura-dokumentuaren fitxategi-bidea itzultzen du.
     *
     * @return Dokumentuaren bidea
     */
    public String getDokumentuBidea() {
        return dokumentuBidea;
    }

    /**
     * Emanaldiari buruzko oharrak itzultzen du.
     *
     * @return Oharrak
     */
    public String getOharrak() {
        return oharrak;
    }

    /**
     * Emanaldiari buruzko oharrak ezartzen ditu.
     *
     * @param oharrak Ezarri beharreko oharrak
     */
    public void setOharrak(String oharrak) {
        this.oharrak = oharrak;
    }

    /**
     * Emanaldiarekin lotutako artikulua itzultzen du.
     *
     * @return Artikulua objektua
     */
    public Artikulua getArtikulua() {
        return artikulua;
    }

    /**
     * Artikulua jaso duen hartzailea itzultzen du.
     *
     * @return Hartzailea objektua
     */
    public Hartzailea getHartzailea() {
        return hartzailea;
    }

    /**
     * Emanaldia kudeatzen duen langilea itzultzen du.
     *
     * @return Langilea objektua
     */
    public Langilea getLangilea() {
        return langilea;
    }

    @Override
    public String toString() {
        return emanaldiId + " | " + artikulua.getArtikuluKodea() + " -> "
                + hartzailea.getIdentifikazioa();
    }
}
