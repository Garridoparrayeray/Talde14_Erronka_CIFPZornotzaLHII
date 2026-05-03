package model;

/**
 * Azken mugimenduaren datu laburra adierazten duen DTO klasea.
 * Paneleko taula azkarrean erakusteko erabiltzen da.
 * @author Yeray Garrido
 */
public class AzkenMugimendua {

    private String artikuluId;
    private String deskribapena;
    private String data;
    private String langilea;

    /**
     * AzkenMugimendua-ren eraikitzailea.
     * @param artikuluId Artikuluaren kode bakarra
     * @param deskribapena Mugimenduaren azalpen laburra
     * @param data Mugimenduaren data eta ordua
     * @param langilea Eragiketa burutu duen langilearen izena
     */
    public AzkenMugimendua(String artikuluId, String deskribapena, String data, String langilea) {
        this.artikuluId = artikuluId;
        this.deskribapena = deskribapena;
        this.data = data;
        this.langilea = langilea;
    }

    public String getArtikuluId() { return artikuluId; }
    public String getDeskribapena() { return deskribapena; }
    public String getData() { return data; }
    public String getLangilea() { return langilea; }
}
