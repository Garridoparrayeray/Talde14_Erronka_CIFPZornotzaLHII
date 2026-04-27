package eus.zornotza.galdutakoak.model;

import java.util.List;

public class Administratzailea extends Langilea {

    public Administratzailea(int langileId, String izena, String abizena, String erabiltzailea, String pasahitza) {
        super(langileId, izena, abizena, erabiltzailea, pasahitza);
    }

    public void kudeatuKategoriak() {
    }

    public void kudeatuKokalekuak() {
    }

    public void kudeatuErabiltzaileak() {
    }

    public List<Erreklamazioa> ikusiErreklamazioGuztiak() {
        return null;
    }
}
