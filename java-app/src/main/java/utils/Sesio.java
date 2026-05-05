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

    public static Langilea getLangilea() {
        return langileAktiboa;
    }

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
