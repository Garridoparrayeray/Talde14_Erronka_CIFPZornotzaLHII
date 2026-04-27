package model;

import java.util.Date;

public class Jakinarazpena {

    private int jakinarazpenaId;
    private Date bidaltzaData;
    private Kanala kanala;
    private String mezua;
    private boolean irakurrita;
    private Erreklamazioa erreklamazioa;
    private Artikulua artikulua;

    public Jakinarazpena(Erreklamazioa erreklamazioa, Artikulua artikulua, Kanala kanala) {
        this.erreklamazioa = erreklamazioa;
        this.artikulua = artikulua;
        this.kanala = kanala;
        this.bidaltzaData = new Date();
        this.irakurrita = false;
        this.mezua = "Zure erreklamazioarekin bat datorren artikulua aurkitu da: "
                   + artikulua.getIzenburua();
    }

    public void bidali() {
    }

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
        return kanala + " | " + mezua + " | " + (irakurrita ? "IRAKURRITA" : "IRAKURRI GABE");
    }
}
