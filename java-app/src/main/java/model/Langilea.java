package model;

import java.util.List;

public class Langilea {

    private int langileId;
    private String izena;
    private String abizena;
    private String erabiltzailea;
    private String pasahitzaHash;

    public Langilea(int langileId, String izena, String abizena, String erabiltzailea, String pasahitza) {
        this.langileId = langileId;
        this.izena = izena;
        this.abizena = abizena;
        this.erabiltzailea = erabiltzailea;
        this.pasahitzaHash = pasahitza;
    }

    public boolean erregistratuArtikulua(Artikulua a) {
        return false;
    }

    public boolean erregistratuErreklamazioa(Erreklamazioa e) {
        return false;
    }

    public boolean kudeatuEmanaldia(Emanaldia em) {
        return false;
    }

    public List<Mugimendua> getTrazabilitatea() {
        return null;
    }

    // Getters & Setters
    public int getLangileId() { return langileId; }
    public String getIzena() { return izena; }
    public String getAbizena() { return abizena; }
    public String getErabiltzailea() { return erabiltzailea; }

    @Override
    public String toString() {
        return izena + " " + abizena + " (" + erabiltzailea + ")";
    }
}
