package model;

import java.io.Serializable;

/**
 * Galdu den objektua aurkitu eta udaletxera ekarri duen pertsona.
 * Bi urteko iraungitze-epea igaro ostean, jabea ez bada agertu,
 * aurkitzaileari jakinarazi behar zaio.
 *
 * @author Yeray Garrido
 */
public class Aurkitzailea implements Serializable {

    private static final long serialVersionUID = 1L;

    private int aurkitzaileaId;
    private String izena;
    private String abizena;
    private String telefonoa;
    private String emaila;
    private String aurkipenLekua;
    private String idArtikulua;

    /**
     * Aurkitzailearen eraikitzailea.
     *
     * @param izena        Aurkitzailearen izena
     * @param abizena      Aurkitzailearen abizena
     * @param telefonoa    Harremanetarako telefonoa (null bada ezezaguna)
     * @param emaila       Harremanetarako helbide elektronikoa (null bada ezezaguna)
     * @param aurkipenLekua Artikulua aurkitu zen lekua (null bada ezezaguna)
     * @param idArtikulua  Aurkitu den artikuluaren kodea
     */
    public Aurkitzailea(String izena, String abizena, String telefonoa,
            String emaila, String aurkipenLekua, String idArtikulua) {
        this.izena = izena;
        this.abizena = abizena;
        this.telefonoa = telefonoa;
        this.emaila = emaila;
        this.aurkipenLekua = aurkipenLekua;
        this.idArtikulua = idArtikulua;
    }

    /**
     * Aurkitzailearen izen eta abizena elkarturik itzultzen du.
     *
     * @return Izen osoa
     */
    public String getIzenOsoa() {
        return izena + " " + abizena;
    }

    /**
     * Datu-baseko identifikagailua itzultzen du.
     *
     * @return Aurkitzailearen IDa
     */
    public int getAurkitzaileaId() {
        return aurkitzaileaId;
    }

    /**
     * Datu-baseko identifikagailua ezartzen du.
     *
     * @param id Ezarri beharreko IDa
     */
    public void setAurkitzaileaId(int id) {
        this.aurkitzaileaId = id;
    }

    /**
     * Aurkitzailearen izena itzultzen du.
     *
     * @return Izena
     */
    public String getIzena() {
        return izena;
    }

    /**
     * Aurkitzailearen abizena itzultzen du.
     *
     * @return Abizena
     */
    public String getAbizena() {
        return abizena;
    }

    /**
     * Aurkitzailearen telefonoa itzultzen du.
     *
     * @return Telefonoa, edo null ez bada ezaguna
     */
    public String getTelefonoa() {
        return telefonoa;
    }

    /**
     * Aurkitzailearen helbide elektronikoa itzultzen du.
     *
     * @return Emaila, edo null ez bada ezaguna
     */
    public String getEmaila() {
        return emaila;
    }

    /**
     * Artikulua aurkitu zen lekua itzultzen du.
     *
     * @return Aurkipen lekua, edo null ez bada ezaguna
     */
    public String getAurkipenLekua() {
        return aurkipenLekua;
    }

    /**
     * Lotutako artikuluaren kodea itzultzen du.
     *
     * @return Artikuluaren IDa
     */
    public String getIdArtikulua() {
        return idArtikulua;
    }

    /**
     * Aurkitzailearen testuzko adierazpena itzultzen du.
     * Telefonoa badago hori erakusten du, bestela emaila.
     *
     * @return Izena, abizena eta harremanetarako datu bat
     */
    @Override
    public String toString() {
        String kontaktua;
        if (telefonoa != null) {
            kontaktua = telefonoa;
        } else {
            kontaktua = emaila;
        }
        return izena + " " + abizena + " (" + kontaktua + ")";
    }
}
