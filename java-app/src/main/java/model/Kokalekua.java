package model;

import java.io.Serializable;

/**
 * Biltegiko kokaleku fisiko bat adierazten duen eredua (armairua eta apala).
 *
 * @author Yeray Garrido
 */
public class Kokalekua implements Serializable {

    private static final long serialVersionUID = 1L;

    private int kokalekuId;
    private String armairua;
    private String apala;
    private boolean bhaDa;
    private int artikuluKopurua;

    /**
     * Kokalekuaren eraikitzailea.
     *
     * @param armairua Armairuaren kodea edo izena
     * @param apala    Apalaren kodea edo izena
     * @param bhaDa    Behin-Behineko Harrera Alderdian badago true
     */
    public Kokalekua(String armairua, String apala, boolean bhaDa) {
        this.armairua = armairua;
        this.apala = apala;
        this.bhaDa = bhaDa;
    }

    /**
     * Kokalekuaren deskripzio osoa itzultzen du, BHA marka barne.
     *
     * @return Kokalekuaren testu osoa
     */
    public String getKokalekuOsoa() {
        if (bhaDa) {
            return armairua + " - " + apala + " [BHA]";
        } else {
            return armairua + " - " + apala;
        }
    }

    /**
     * Kokalekuaren mota itzultzen du.
     *
     * @return "BHA" edo "Arrunta"
     */
    public String getMota() {
        if (bhaDa) {
            return "BHA";
        }
        return "Arrunta";
    }

    /**
     * Kokalekuan dauden artikulu kopurua kate gisa itzultzen du.
     *
     * @return Artikulu kopurua String gisa
     */
    public String getArtikuluKopuruaStr() {
        return String.valueOf(artikuluKopurua);
    }

    // Getters & Setters
    /**
     * Kokalekuaren datu-baseko IDa itzultzen du.
     *
     * @return Kokalekuaren IDa
     */
    public int getKokalekuId() {
        return kokalekuId;
    }

    /**
     * Kokalekuaren datu-baseko IDa ezartzen du.
     *
     * @param id Ezarri beharreko IDa
     */
    public void setKokalekuId(int id) {
        this.kokalekuId = id;
    }

    /**
     * Armairuaren kodea edo izena itzultzen du.
     *
     * @return Armairua
     */
    public String getArmairua() {
        return armairua;
    }

    /**
     * Apalaren kodea edo izena itzultzen du.
     *
     * @return Apala
     */
    public String getApala() {
        return apala;
    }

    /**
     * Kokalekua BHA-n dagoen ala ez itzultzen du.
     *
     * @return BHA-n badago true
     */
    public boolean isBhaDa() {
        return bhaDa;
    }

    /**
     * Kokalekuan dauden artikulu kopurua itzultzen du.
     *
     * @return Artikulu kopurua
     */
    public int getArtikuluKopurua() {
        return artikuluKopurua;
    }

    /**
     * Kokalekuan dauden artikulu kopurua ezartzen du.
     *
     * @param artikuluKopurua Ezarri beharreko kopurua
     */
    public void setArtikuluKopurua(int artikuluKopurua) {
        this.artikuluKopurua = artikuluKopurua;
    }

    @Override
    public String toString() {
        return getKokalekuOsoa();
    }
}
