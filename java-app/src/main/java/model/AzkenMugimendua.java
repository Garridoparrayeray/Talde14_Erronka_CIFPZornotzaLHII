package model;

/**
 * Azken mugimenduaren datu laburra adierazten duen DTO klasea. Paneleko taula
 * azkarrean erakusteko erabiltzen da.
 *
 * @author Yeray Garrido
 */
public class AzkenMugimendua {

    private String artikuluId;
    private String deskribapena;
    private String data;
    private String langilea;

    /**
     * AzkenMugimendua-ren eraikitzailea.
     *
     * @param artikuluId   Artikuluaren kode bakarra
     * @param deskribapena Mugimenduaren azalpen laburra
     * @param data         Mugimenduaren data eta ordua
     * @param langilea     Eragiketa burutu duen langilearen izena
     */
    public AzkenMugimendua(String artikuluId, String deskribapena, String data, String langilea) {
        this.artikuluId = artikuluId;
        this.deskribapena = deskribapena;
        this.data = data;
        this.langilea = langilea;
    }

    /**
     * Artikuluaren kode bakarra itzultzen du.
     *
     * @return Artikuluaren ID testua
     */
    public String getArtikuluId() {
        return artikuluId;
    }

    /**
     * Mugimenduaren azalpen laburra itzultzen du.
     *
     * @return Deskribapena
     */
    public String getDeskribapena() {
        return deskribapena;
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
}
