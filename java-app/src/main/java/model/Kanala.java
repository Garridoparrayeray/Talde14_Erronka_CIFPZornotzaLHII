package model;

/**
 * Jakinarazpenak bidaltzeko kanal posibleak adierazten dituen enumerazioa. SMS:
 * testu-mezua; EMAIL: helbide elektronikoa; APP: aplikazioaren barneko
 * jakinarazpena.
 *
 * @author Yeray Garrido
 */
public enum Kanala {
    /** Testu-mezua telefono mugikorraren bidez. */
    SMS,
    /** Helbide elektronikoaren bidez. */
    EMAIL,
    /** Aplikazioaren barneko jakinarazpena. */
    APP
}
