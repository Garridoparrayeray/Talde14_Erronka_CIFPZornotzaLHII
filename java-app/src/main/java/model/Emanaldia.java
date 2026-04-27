package model;

import java.util.Date;

public class Emanaldia {

    private int emanaldiId;
    private Date emateData;
    private String dokumentuBidea;
    private String oharrak;
    private Artikulua artikulua;
    private Hartzailea hartzailea;
    private Langilea langilea;

    public Emanaldia(Artikulua artikulua, Hartzailea hartzailea,
                     Langilea langilea, Date emateData) {
        this.artikulua = artikulua;
        this.hartzailea = hartzailea;
        this.langilea = langilea;
        this.emateData = emateData;
    }

    public void gordeDokumentua(String bidea) {
        this.dokumentuBidea = bidea;
    }

    public boolean amaituEmanaldia() {
        if (dokumentuBidea != null && !dokumentuBidea.isEmpty()) {
            artikulua.aldatuEgoera(
                EgoeraArtikulua.ITZULITA);
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
