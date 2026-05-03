package model;

import java.util.Date;

/**
 * Bat-etortze posible baten jakinarazpena adierazten duen eredua.
 * Erreklamazino bat eta artikulu bat lotzen ditu, kanalaren bidez bidaltzeko.
 * @author Yeray Garrido
 */
public class Jakinarazpena {

    private int jakinarazpenaId;
    private Date bidaltzaData;
    private Kanala kanala;
    private String mezua;
    private boolean irakurrita;
    private Erreklamazioa erreklamazioa;
    private Artikulua artikulua;

    /**
     * Jakinarazpenaren eraikitzailea. Mezua automatikoki sortzen du.
     * @param erreklamazioa Lotutako erreklamazioa
     * @param artikulua Bat datorren artikulua
     * @param kanala Bidaltzeko kanala
     */
    public Jakinarazpena(Erreklamazioa erreklamazioa, Artikulua artikulua, Kanala kanala) {
        this.erreklamazioa = erreklamazioa;
        this.artikulua = artikulua;
        this.kanala = kanala;
        this.bidaltzaData = new Date();
        this.irakurrita = false;
        this.mezua = "Zure erreklamazioarekin bat datorren artikulua aurkitu da: "
                   + artikulua.getIzenburua();
    }

    /**
     * Jakinarazpena zehaztutako kanalaren bidez bidaltzen du.
     */
    public void bidali() {
    }

    /**
     * Jakinarazpena irakurrita gisa markatzen du.
     */
    public void markatuIrakurrita() {
        this.irakurrita = true;
    }

    // Getters
    public int getJakinarazpenaId() { return jakinarazpenaId; }
    public Date getBidaltzaData() { return bidaltzaData; }
    public Kanala getKanala() { return kanala; }
    public String getMezua() { return mezua; }
    public boolean isIrakurrita() { return irakurrita; }
    public Erreklamazioa getErreklamazioa() { return erreklamazioa; }
    public Artikulua getArtikulua() { return artikulua; }

    @Override
    public String toString() {
        if (irakurrita) {
            return kanala + " | " + mezua + " | " + "IRAKURRITA";
        } else {
            return kanala + " | " + mezua + " | " + "IRAKURRI GABE";
        }
    }
}
