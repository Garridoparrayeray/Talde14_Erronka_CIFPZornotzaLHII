package model;

import java.util.Date;

/**
 * Inbentarioko edozein mugimendu erregistratzeko modelo klasea.
 * @author Yeray Garrido
 */
public class Mugimendua {

    private int mugimenduId;
    private Date dataOrdua;
    private MugimenduMota mota;
    private String deskribapena;
    private Langilea langilea;
    private Artikulua artikulua;

    /**
     * Mugimenduaren eraikitzailea.
     * 
     * @param langilea Eragiketa burutu duen langilea
     * @param artikulua Mugitu den artikulua
     * @param mota Mugimendu mota (sarrera, irteera, etab.)
     * @param deskribapena Mugimenduaren azalpen laburra
     */
    public Mugimendua(Langilea langilea, Artikulua artikulua,
                      MugimenduMota mota, String deskribapena) {
        this.langilea = langilea;
        this.artikulua = artikulua;
        this.mota = mota;
        this.deskribapena = deskribapena;
        this.dataOrdua = new Date();
    }

    /**
     * Mugimendua sistemaren erregistroan (log) idazten du.
     */
    public void erregistratuLoga() {
    }

    // ── Getterrak ────────────────────────────────────────────────────────────
    public int getMugimenduId() { return mugimenduId; }
    public Date getDataOrdua() { return dataOrdua; }
    public MugimenduMota getMota() { return mota; }
    public String getDeskribapena() { return deskribapena; }
    public Langilea getLangilea() { return langilea; }
    public Artikulua getArtikulua() { return artikulua; }

    @Override
    public String toString() {
        return mota + " | " + artikulua.getArtikuluKodea() + " | "
             + langilea.getIzena() + " | " + dataOrdua;
    }
}
