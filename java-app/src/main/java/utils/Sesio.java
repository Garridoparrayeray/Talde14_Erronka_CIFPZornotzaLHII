package utils;

import model.Langilea;

public class Sesio {

    private static Langilea langileAktiboa = null;
    private static boolean adminDa = false;

    private Sesio() {}

    public static void hasiera(Langilea langilea, boolean admin) {
        langileAktiboa = langilea;
        adminDa = admin;
    }

    public static Langilea getLangilea() { return langileAktiboa; }
    public static boolean isAdmin()      { return adminDa; }

    public static void itxi() {
        langileAktiboa = null;
        adminDa = false;
    }
}