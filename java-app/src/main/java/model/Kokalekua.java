package model;

/**
 * Biltegiko kokaleku fisiko bat adierazten duen eredua (armairua eta apala).
 *
 * @author Yeray Garrido
 */
public class Kokalekua {

    private int kokalekuId;
    private String armairua;
    private String apala;
    private boolean bhaDa;
    private int artikuluKopurua;

    /**
     * Kokalekuaren eraikitzailea.
     *
     * @param armairua Armairuaren kodea edo izena
     * @param apala Apalaren kodea edo izena
     * @param bhaDa Behin-Behineko Harrera Alderdian badago true
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
    public int getKokalekuId() {
        return kokalekuId;
    }

    public void setKokalekuId(int id) {
        this.kokalekuId = id;
    }

    public String getArmairua() {
        return armairua;
    }

    public String getApala() {
        return apala;
    }

    public boolean isBhaDa() {
        return bhaDa;
    }

    public int getArtikuluKopurua() {
        return artikuluKopurua;
    }

    public void setArtikuluKopurua(int artikuluKopurua) {
        this.artikuluKopurua = artikuluKopurua;
    }

    @Override
    public String toString() {
        return getKokalekuOsoa();
    }
}
