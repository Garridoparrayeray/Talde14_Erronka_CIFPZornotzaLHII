package app;

/**
 * Fat JAR-erako sarrera-puntua. Main Application azpiklasea denez, JVM-ak
 * module-path gabe ez du onartzen zuzenean; klase honen bidez saihesten da.
 *
 * @author Yeray Garrido
 */
public class Launcher {

    /**
     * Aplikazioaren sarrera-puntua. Main klaseko main() metodora bideratzen du.
     *
     * @param args Komando-lerroko argumentuak
     */
    public static void main(String[] args) {
        Main.main(args);
    }
}
