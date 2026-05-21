package model;

/**
 * Artikulu baten egoera posibleak adierazten dituen enumerazioa. BILTEGIAN:
 * gordeta; BHA_N_GORDETA: behin-behineko harreran; ITZULITA: jabeak jaso du;
 * IRAUNGITA: epea igaro da; DOHANTZAN: dohaintzan eman da.
 *
 * @author Yeray Garrido
 */
public enum EgoeraArtikulua {
    /** Artikulua biltegian gordeta dago, oraindik jaso gabe. */
    BILTEGIAN,
    /** Artikulua Behin-Behineko Harrera Alderdian gordeta dago. */
    BHA_N_GORDETA,
    /** Artikulua jabeak jaso du. */
    ITZULITA,
    /** Gordailutze-epea iraungitu da. */
    IRAUNGITA,
    /** Artikulua dohaintzan eman da. */
    DOHANTZAN
}
