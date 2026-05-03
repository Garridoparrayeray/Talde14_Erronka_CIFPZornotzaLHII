package model;

import java.util.Date;

/**
 * Artikulu bat hartzaile bati ematearen eragiketa adierazten duen eredua.
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
     * Emanaldiari eraikitzailea.
     * @param artikulua Ematen den artikulua
     * @param hartzailea Artikulua jasotzen duen hartzailea
     * @param langilea Eragiketa kudeatzen duen langilea
     * @param emateData Emateko data
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
     * @param bidea Dokumentuaren fitxategi-bidea
     */
    public void gordeDokumentua(String bidea) {
        this.dokumentuBidea = bidea;
    }

    /**
     * Emanaldia amaitutzat markatzen du eta artikuluaren egoera eguneratzen du.
     * @return Dokumentua badago eta emanaldia ondo amaitu bada true
     */
    public boolean amaituEmanaldia() {
        if (dokumentuBidea != null && !dokumentuBidea.isEmpty()) {
            artikulua.aldatuEgoera(model.EgoeraArtikulua.ITZULITA);
            return true;
        }
        return false;
    }

    // Getters
    public int getEmanaldiId() { return emanaldiId; }
    public Date getEmateData() { return emateData; }
    public String getDokumentuBidea() { return dokumentuBidea; }
    public String getOharrak() { return oharrak; }
    public void setOharrak(String oharrak) { this.oharrak = oharrak; }
    public Artikulua getArtikulua() { return artikulua; }
    public Hartzailea getHartzailea() { return hartzailea; }
    public Langilea getLangilea() { return langilea; }

    @Override
    public String toString() {
        return emanaldiId + " | " + artikulua.getArtikuluKodea() + " -> "
             + hartzailea.getIdentifikazioa();
    }
}
