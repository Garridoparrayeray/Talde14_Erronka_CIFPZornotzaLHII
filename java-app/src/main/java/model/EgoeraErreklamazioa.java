package model;

/**
 * Erreklamazioaren egoera posibleak adierazten dituen enumerazioa. IREKITA:
 * oraindik ebatzi gabe; EBATZITA: artikulua eman zaio jabeari; BAZTERTUTA:
 * baztertu da.
 *
 * @author Yeray Garrido
 */
public enum EgoeraErreklamazioa {
    /** Erreklamazioa oraindik ebatzi gabe dago. */
    IREKITA,
    /** Erreklamazioa ebatzi da eta artikulua eman zaio jabeari. */
    EBATZITA,
    /** Erreklamazioa baztertu da. */
    BAZTERTUTA
}
