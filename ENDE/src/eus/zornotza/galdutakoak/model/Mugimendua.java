package eus.zornotza.galdutakoak.model;

import eus.zornotza.galdutakoak.model.enums.MugimenduMota;
import java.util.Date;

public class Mugimendua {

    private int mugimenduId;
    private Date dataOrdua;
    private MugimenduMota mota;
    private String deskribapena;
    private Langilea langilea;
    private Artikulua artikulua;

    public Mugimendua(Langilea langilea, Artikulua artikulua,
                      MugimenduMota mota, String deskribapena) {
        this.langilea = langilea;
        this.artikulua = artikulua;
        this.mota = mota;
        this.deskribapena = deskribapena;
        this.dataOrdua = new Date();
    }

    public void erregistratuLoga() {
    }

    // Getters
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
