package model;

/**
 * Auditoria-taulako mugimenduen errenkada bat adierazten duen DTO klasea.
 * @author Yeray Garrido
 */
public class MugimenduLerroa {

    private String data;
    private String langilea;
    private String ekintza;
    private String artikuluId;

    /**
     * MugimenduLerroa-ren eraikitzailea.
     * @param data Mugimenduaren data eta ordua
     * @param langilea Eragiketa burutu duen langilearen izena
     * @param ekintza Eginikako ekintzaren deskribapena
     * @param artikuluId Artikuluaren kode bakarra
     */
    public MugimenduLerroa(String data, String langilea, String ekintza, String artikuluId) {
        this.data = data;
        this.langilea = langilea;
        this.ekintza = ekintza;
        this.artikuluId = artikuluId;
    }

    public String getData() { return data; }
    public String getLangilea() { return langilea; }
    public String getEkintza() { return ekintza; }
    public String getArtikuluId() { return artikuluId; }
}
