package model;

/**
 * Inbentarioko mugimenduen mota posibleak adierazten dituen enumerazioa.
 * SARRERA: biltegira sartu; BARNE_MUGIMENDUA: barruan mugitu;
 * IRTEERA_JABEA/ERAKUNDEA: atera; IRAUNGITZE_ALERTA: iraungitze-oharra sortu
 * da.
 *
 * @author Yeray Garrido
 */
public enum MugimenduMota {
    /** Artikulua biltegira sartu da. */
    SARRERA,
    /** Artikulua biltegiaren barnean mugitu da kokalekuz. */
    BARNE_MUGIMENDUA,
    /** Artikulua jabeak jaso du. */
    IRTEERA_JABEA,
    /** Artikulua erakunde batek jaso du. */
    IRTEERA_ERAKUNDEA,
    /** Artikuluaren iraungitze-alerta sortu da. */
    IRAUNGITZE_ALERTA
}
