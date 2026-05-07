package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Biltegian dagoen galdu den objektu bat adierazten duen eredua.
 *
 * @author Yeray Garrido
 */
public class Artikulua implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * Sarrera-data formatu irakurgarrian itzultzen du (dd/MM/yyyy).
     *
     * @return Sarrera-data formateaturiko katea, edo "—" null bada
     */
    public String getSarreraDataFormatua() {
        if (sarreraData == null) {
            return "—";
        }
        return SDF.format(sarreraData);
    }

    /**
     * Artikuluaren egoeraren testu erabiltzaileentzako itzultzen du.
     *
     * @return Egoeraren euskarazko testua
     */
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

    /**
     * Artikuluaren kategoriaren izena itzultzen du.
     *
     * @return Kategoriaren izena, edo "—" kategoria null bada
     */
    public String getKategoriaIzena() {
        if (kategoria == null) {
            return "—";
        }
        return kategoria.getIzena();
    }

    /**
     * Artikuluaren kokalekuaren deskripzio osoa itzultzen du.
     *
     * @return Kokalekuaren testu osoa, edo "—" kokalekua null bada
     */
    public String getKokalekuaIzena() {
        if (kokalekua == null) {
            return "—";
        }
        return kokalekua.getKokalekuOsoa();
    }

    /**
     * Deskribapena itzultzen du, null bada "—" itzultzen du.
     *
     * @return Deskribapena katea, edo "—" null bada
     */
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
    /**
     * Artikuluaren datu-baseko ID zenbakia itzultzen du.
     *
     * @return Artikuluaren IDa
     */
    public int getArtikuluId() {
        return artikuluId;
    }

    /**
     * Artikuluaren kode bakarra itzultzen du.
     *
     * @return Artikuluaren kode testua (adib. G-001-26)
     */
    public String getArtikuluKodea() {
        return artikuluKodea;
    }

    /**
     * Artikuluaren izen laburra itzultzen du.
     *
     * @return Izenburua
     */
    public String getIzenburua() {
        return izenburua;
    }

    /**
     * Artikuluaren izen laburra ezartzen du.
     *
     * @param izenburua Ezarri beharreko izenburua
     */
    public void setIzenburua(String izenburua) {
        this.izenburua = izenburua;
    }

    /**
     * Artikuluaren deskripzio osoa itzultzen du.
     *
     * @return Deskribapena
     */
    public String getDeskribapena() {
        return deskribapena;
    }

    /**
     * Artikuluaren deskribapena ezartzen du.
     *
     * @param deskribapena Ezarri beharreko deskribapena
     */
    public void setDeskribapena(String deskribapena) {
        this.deskribapena = deskribapena;
    }

    /**
     * Artikuluaren markaren izena itzultzen du.
     *
     * @return Marka
     */
    public String getMarka() {
        return marka;
    }

    /**
     * Artikuluaren kolorea itzultzen du.
     *
     * @return Kolorea
     */
    public String getKolorea() {
        return kolorea;
    }

    /**
     * Biltegira sartu zen data itzultzen du.
     *
     * @return Sarrera-data
     */
    public Date getSarreraData() {
        return sarreraData;
    }

    /**
     * Artikuluaren gordailutze-epearen azken data itzultzen du.
     *
     * @return Iraungitze-data
     */
    public Date getIraungitzeData() {
        return iraungitzeData;
    }

    /**
     * Argazkiaren fitxategi-bidea itzultzen du.
     *
     * @return Argazkiaren bidea
     */
    public String getArgazkiBidea() {
        return argazkiBidea;
    }

    /**
     * Argazkiaren fitxategi-bidea ezartzen du.
     *
     * @param argazkiBidea Ezarri beharreko argazki-bidea
     */
    public void setArgazkiBidea(String argazkiBidea) {
        this.argazkiBidea = argazkiBidea;
    }

    /**
     * Artikuluaren uneko egoera itzultzen du.
     *
     * @return Egoera enumerazioa
     */
    public EgoeraArtikulua getEgoera() {
        return egoera;
    }

    /**
     * Artikuluari lotutako kategoria itzultzen du.
     *
     * @return Kategoria objektua
     */
    public Kategoria getKategoria() {
        return kategoria;
    }

    /**
     * Artikuluaren kategoria ezartzen du.
     *
     * @param kategoria Ezarri beharreko kategoria
     */
    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    /**
     * Artikuluari lotutako kokalekua itzultzen du.
     *
     * @return Kokalekua objektua
     */
    public Kokalekua getKokalekua() {
        return kokalekua;
    }

    /**
     * Artikuluaren kokalekua ezartzen du.
     *
     * @param kokalekua Ezarri beharreko kokalekua
     */
    public void setKokalekua(Kokalekua kokalekua) {
        this.kokalekua = kokalekua;
    }

    @Override
    public String toString() {
        return getDatuak();
    }
}
