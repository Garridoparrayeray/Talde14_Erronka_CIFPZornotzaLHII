package model;

import java.util.Date;

/**
 * Inbentarioko edozein mugimendu erregistratzeko modelo klasea.
 *
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
    /**
     * Mugimenduaren datu-baseko IDa itzultzen du.
     *
     * @return Mugimenduaren IDa
     */
    public int getMugimenduId() {
        return mugimenduId;
    }

    /**
     * Mugimendua gertatu zen data eta ordua itzultzen du.
     *
     * @return Data eta ordua
     */
    public Date getDataOrdua() {
        return dataOrdua;
    }

    /**
     * Mugimenduaren mota itzultzen du.
     *
     * @return MugimenduMota enumerazioa
     */
    public MugimenduMota getMota() {
        return mota;
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
     * Eragiketa burutu duen langilea itzultzen du.
     *
     * @return Langilea objektua
     */
    public Langilea getLangilea() {
        return langilea;
    }

    /**
     * Mugitu den artikulua itzultzen du.
     *
     * @return Artikulua objektua
     */
    public Artikulua getArtikulua() {
        return artikulua;
    }

    @Override
    public String toString() {
        return mota + " | " + artikulua.getArtikuluKodea() + " | "
                + langilea.getIzena() + " | " + dataOrdua;
    }
}
