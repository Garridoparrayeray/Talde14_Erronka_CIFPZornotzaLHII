package model;

import java.io.Serializable;

/**
 * Auditoria-taulako mugimenduen errenkada bat adierazten duen DTO klasea.
 *
 * @author Yeray Garrido
 */
public class MugimenduLerroa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String data;
    private String langilea;
    private String ekintza;
    private String artikuluId;

    /**
     * MugimenduLerroa-ren eraikitzailea.
     *
     * @param data       Mugimenduaren data eta ordua
     * @param langilea   Eragiketa burutu duen langilearen izena
     * @param ekintza    Eginikako ekintzaren deskribapena
     * @param artikuluId Artikuluaren kode bakarra
     */
    public MugimenduLerroa(String data, String langilea, String ekintza, String artikuluId) {
        this.data = data;
        this.langilea = langilea;
        this.ekintza = ekintza;
        this.artikuluId = artikuluId;
    }

    /**
     * Mugimenduaren data eta ordua itzultzen du.
     *
     * @return Data eta ordua katea
     */
    public String getData() {
        return data;
    }

    /**
     * Eragiketa burutu duen langilearen izena itzultzen du.
     *
     * @return Langilearen izena
     */
    public String getLangilea() {
        return langilea;
    }

    /**
     * Eginikako ekintzaren deskribapena itzultzen du.
     *
     * @return Ekintzaren deskribapena
     */
    public String getEkintza() {
        return ekintza;
    }

    /**
     * Lotutako artikuluaren kode bakarra itzultzen du.
     *
     * @return Artikuluaren ID testua
     */
    public String getArtikuluId() {
        return artikuluId;
    }
}
