package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Biltegian dagoen galdu den objektu bat adierazten duen eredua.
 *
 * @author Yeray Garrido
 */
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

    /**
     * Artikuluaren eraikitzailea.
     *
     * @param artikuluKodea Artikuluaren kode bakarra (adib. G-001-26)
     * @param izenburua Artikuluaren izen laburra
     * @param deskribapena Artikuluaren deskripzio osoa
     * @param marka Markaren izena
     * @param kolorea Kolorea
     * @param sarreraData Biltegira sartu zen data
     * @param argazkiBidea Argazkiaren fitxategi-bidea
     */
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

    /**
     * Artikuluaren gordailutze-epea iraungitu den egiaztatzen du.
     *
     * @return Iraungitze-data igarota badago true
     */
    public boolean iraungipenaEgiaztatu() {
        return iraungitzeData != null && new Date().after(iraungitzeData);
    }

    /**
     * Artikuluaren egoera eguneratzen du.
     *
     * @param berria Egoera berria
     */
    public void aldatuEgoera(EgoeraArtikulua berria) {
        this.egoera = berria;
    }

    /**
     * Sarrera-datatik 2 urtera iraungitze-data kalkulatzen du.
     */
    public void kalkulatuIraungitzea() {
        if (sarreraData == null) {
            return;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(sarreraData);
        cal.add(Calendar.DAY_OF_YEAR, 730);
        this.iraungitzeData = cal.getTime();
    }

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    public String getSarreraDataFormatua() {
        if (sarreraData == null) {
            return "—";
        }
        return SDF.format(sarreraData);
    }

    public String getEgoeraTestua() {
        if (egoera == null) {
            return "—";
        }
        switch (egoera) {
            case BILTEGIAN:
                return "Biltegian";
            case ITZULITA:
                return "Itzulita";
            case IRAUNGITA:
                return "Iraungita";
            case BHA_N_GORDETA:
                return "BHA-n gordeta";
            case DOHANTZAN:
                return "Dohantzan";
            default:
                return egoera.name();
        }
    }

    public String getKategoriaIzena() {
        if (kategoria == null) {
            return "—";
        }
        return kategoria.getIzena();
    }

    public String getKokalekuaIzena() {
        if (kokalekua == null) {
            return "—";
        }
        return kokalekua.getKokalekuOsoa();
    }

    public String getDeskribapenaSegurua() {
        if (deskribapena != null) {
            return deskribapena;
        }
        return "—";
    }

    /**
     * Artikuluaren laburpen-katea itzultzen du erregistroetarako.
     *
     * @return Kodea, izenburua, marka eta egoera katetuta
     */
    public String getDatuak() {
        return artikuluKodea + " | " + izenburua + " | " + marka + " | " + egoera;
    }

    // Getters & Setters
    public int getArtikuluId() {
        return artikuluId;
    }

    public String getArtikuluKodea() {
        return artikuluKodea;
    }

    public String getIzenburua() {
        return izenburua;
    }

    public String getDeskribapena() {
        return deskribapena;
    }

    public String getMarka() {
        return marka;
    }

    public String getKolorea() {
        return kolorea;
    }

    public Date getSarreraData() {
        return sarreraData;
    }

    public Date getIraungitzeData() {
        return iraungitzeData;
    }

    public String getArgazkiBidea() {
        return argazkiBidea;
    }

    public EgoeraArtikulua getEgoera() {
        return egoera;
    }

    public Kategoria getKategoria() {
        return kategoria;
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    public Kokalekua getKokalekua() {
        return kokalekua;
    }

    public void setKokalekua(Kokalekua kokalekua) {
        this.kokalekua = kokalekua;
    }

    @Override
    public String toString() {
        return getDatuak();
    }
}
