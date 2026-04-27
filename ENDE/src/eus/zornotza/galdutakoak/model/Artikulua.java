package eus.zornotza.galdutakoak.model;

import java.util.Date;

import eus.zornotza.galdutakoak.model.enums.EgoeraArtikulua;

import java.util.Calendar;

public class Artikulua {

    private int artikuluId;
    private String artikuluKodea;
    private String izenburua;
    private String deskribapena;
    private String marka;
    private String kolorea;
    private Date sarreraData;
    private Date iraungitzeData;
    private String argazkiBidea;
    private EgoeraArtikulua egoera;
    private Kategoria kategoria;
    private Kokalekua kokalekua;

    public Artikulua(String artikuluKodea, String izenburua, String deskribapena,
                     String marka, String kolorea, Date sarreraData, String argazkiBidea) {
        this.artikuluKodea = artikuluKodea;
        this.izenburua = izenburua;
        this.deskribapena = deskribapena;
        this.marka = marka;
        this.kolorea = kolorea;
        this.sarreraData = sarreraData;
        this.argazkiBidea = argazkiBidea;
        this.egoera = EgoeraArtikulua.BILTEGIAN;
        kalkulatuIraungitzea();
    }

    public boolean iraungipenaEgiaztatu() {
        return new Date().after(iraungitzeData);
    }

    public void aldatuEgoera(EgoeraArtikulua berria) {
        this.egoera = berria;
    }

    public void kalkulatuIraungitzea() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(sarreraData);
        cal.add(Calendar.DAY_OF_YEAR, 730);
        this.iraungitzeData = cal.getTime();
    }

    public String getDatuak() {
        return artikuluKodea + " | " + izenburua + " | " + marka + " | " + egoera;
    }

    // Getters & Setters
    public int getArtikuluId() { return artikuluId; }
    public String getArtikuluKodea() { return artikuluKodea; }
    public String getIzenburua() { return izenburua; }
    public String getDeskribapena() { return deskribapena; }
    public String getMarka() { return marka; }
    public String getKolorea() { return kolorea; }
    public Date getSarreraData() { return sarreraData; }
    public Date getIraungitzeData() { return iraungitzeData; }
    public String getArgazkiBidea() { return argazkiBidea; }
    public EgoeraArtikulua getEgoera() { return egoera; }
    public Kategoria getKategoria() { return kategoria; }
    public void setKategoria(Kategoria kategoria) { this.kategoria = kategoria; }
    public Kokalekua getKokalekua() { return kokalekua; }
    public void setKokalekua(Kokalekua kokalekua) { this.kokalekua = kokalekua; }

    @Override
    public String toString() {
        return getDatuak();
    }
}
