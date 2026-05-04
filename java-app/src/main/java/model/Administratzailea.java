package model;

import java.util.List;

/**
 * Langilearen azpiklasea, administrazio-baimen osoa duen erabiltzailea
 * adierazten duena.
 *
 * @author Yeray Garrido
 */
public class Administratzailea extends Langilea {

    /**
     * Administratzailearen eraikitzailea.
     *
     * @param langileId Langilearen identifikagailu bakarra
     * @param izena Administratzailearen izena
     * @param abizena Administratzailearen abizena
     * @param erabiltzailea Saioa hasteko erabiltzaile-izena
     * @param pasahitza Pasahitzaren hash-a (BCrypt)
     */
    public Administratzailea(int langileId, String izena, String abizena, String erabiltzailea, String pasahitza) {
        super(langileId, izena, abizena, erabiltzailea, pasahitza);
    }

    /**
     * Kategorien kudeaketa baimena duen eragiketa.
     */
    public void kudeatuKategoriak() {
    }

    /**
     * Kokalekuen kudeaketa baimena duen eragiketa.
     */
    public void kudeatuKokalekuak() {
    }

    /**
     * Erabiltzaileen kudeaketa baimena duen eragiketa.
     */
    public void kudeatuErabiltzaileak() {
    }

    /**
     * Erreklamazio guztiak ikusteko baimena duen metodoa.
     *
     * @return Erreklamazioen zerrenda
     */
    public List<Erreklamazioa> ikusiErreklamazioGuztiak() {
        return null;
    }
}
