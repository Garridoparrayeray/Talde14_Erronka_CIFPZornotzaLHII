package utils;

import model.Langilea;

/**
 * Uneko saioaren langile aktiboa gordetzen duen singleton estatikoa.
 *
 * @author Yeray Garrido
 */
public class Sesio {

    private static Langilea langileAktiboa = null;
    private static boolean adminDa = false;

    private Sesio() {
    }

    /**
     * Saioa hasten du langile aktiboarekin.
     *
     * @param langilea Saioa hasten duen langilea
     * @param admin Administratzailea bada true
     */
    public static void hasiera(Langilea langilea, boolean admin) {
        langileAktiboa = langilea;
        adminDa = admin;
    }

    /**
     * Uneko saioko langile aktiboa itzultzen du.
     *
     * @return Langile aktiboa, edo null saioa ez bada hasi
     */
    public static Langilea getLangilea() {
        return langileAktiboa;
    }

    /**
     * Uneko langilea administratzailea den egiaztatzen du.
     *
     * @return Administratzailea bada true
     */
    public static boolean isAdmin() {
        return adminDa;
    }

    /**
     * Saioa ixten du eta langile aktiboa garbitzen du.
     */
    public static void itxi() {
        langileAktiboa = null;
        adminDa = false;
    }
}
