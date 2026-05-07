package model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Erreklamazio baten informazioa gordetzen duen modelo klasea.
 *
 * @author Yeray Garrido
 */
public class Erreklamazioa implements Serializable {

    private static final long serialVersionUID = 1L;

    private int erreklamazioId;
    private Date erreklamazioData;
    private String deskribapenBilatua;
    private EgoeraErreklamazioa egoera;
    private Hartzailea hartzailea;
    private Kategoria kategoria;
    private Langilea langilea;

    /**
     * Erreklamazioaren eraikitzailea.
     *
     * @param hartzailea Erreklamazioa egiten duen pertsona edo erakundea
     * @param deskribapenBilatua Galdutako objektuaren deskribapena
     * @param erreklamazioData Erreklamazioa sortu den data
     */
    public Erreklamazioa(Hartzailea hartzailea, String deskribapenBilatua,
            Date erreklamazioData) {
        this.hartzailea = hartzailea;
        this.deskribapenBilatua = deskribapenBilatua;
        this.erreklamazioData = erreklamazioData;
        this.egoera = EgoeraErreklamazioa.IREKITA;
    }

    /**
     * Biltegian dauden artikuluen artean, erreklamazio honekin bat datozenak
     * bilatzen ditu.
     *
     * @param zerrenda Biltegian dauden artikuluen zerrenda
     * @return Bateragarriak diren artikuluen zerrenda
     */
    public List<Artikulua> bilatuBateragarriak(List<Artikulua> zerrenda) {
        List<Artikulua> emaitzak = new ArrayList<>();
        for (Artikulua a : zerrenda) {
            if (a.getEgoera() == model.EgoeraArtikulua.BILTEGIAN) {
                boolean bat = false;
                if (deskribapenBilatua != null && a.getDeskribapena() != null) {
                    String[] hitzak = deskribapenBilatua.toLowerCase().split("\\s+");
                    for (String h : hitzak) {
                        if (a.getDeskribapena().toLowerCase().contains(h)) {
                            bat = true;
                            break;
                        }
                    }
                }
                if (bat) {
                    emaitzak.add(a);
                }
            }
        }
        return emaitzak;
    }

    /**
     * Erreklamazioaren egoera eguneratzen du.
     *
     * @param egoera Egoera berria (IREKITA, EBATZITA, BAZTERTUTA)
     */
    public void eguneratuEgoera(EgoeraErreklamazioa egoera) {
        this.egoera = egoera;
    }

    /**
     * Erreklamazioaren jabearen izena itzultzen du.
     *
     * @return Jabearen izena
     */
    public String getJabeIzena() {
        return hartzailea.getIzena();
    }

    /**
     * Erreklamazioaren jabearen abizena itzultzen du.
     *
     * @return Jabearen abizena
     */
    public String getJabeAbizena() {
        return hartzailea.getAbizena();
    }

    /**
     * Jabearen telefono zenbakia itzultzen du.
     *
     * @return Telefonoa, edo "—" null bada
     */
    public String getJabeTelefonoa() {
        if (hartzailea.getTelefonoa() == null) {
            return "—";
        } else {
            return hartzailea.getTelefonoa();
        }
    }

    /**
     * Jabearen helbide elektronikoa itzultzen du.
     *
     * @return Emaila, edo "—" null bada
     */
    public String getJabeEmaila() {
        if (hartzailea.getEmaila() == null) {
            return "—";
        } else {
            return hartzailea.getEmaila();
        }
    }

    /**
     * Jabearen NAN zenbakia itzultzen du.
     *
     * @return NAN testua
     */
    public String getJabeNan() {
        return hartzailea.getNan();
    }

    /**
     * Lotutako kategoriaren izena itzultzen du.
     *
     * @return Kategoriaren izena, edo "—" null bada
     */
    public String getKategoriaIzena() {
        if (kategoria != null) {
            return kategoria.getIzena();
        } else {
            return "—";
        }
    }

    /**
     * Erreklamazioaren egoeraren testu laburra itzultzen du.
     *
     * @return Egoeraren testu txikia, edo "irekita" null bada
     */
    public String getEgoeraTestua() {
        if (egoera != null) {
            return egoera.toString().toLowerCase();
        } else {
            return "irekita";
        }
    }

    // Getters
    /**
     * Erreklamazioaren datu-baseko IDa itzultzen du.
     *
     * @return Erreklamazioaren IDa
     */
    public int getErreklamazioId() {
        return erreklamazioId;
    }

    /**
     * Erreklamazioaren IDa ezartzen du.
     *
     * @param erreklamazioId Ezarri beharreko IDa
     */
    public void setErreklamazioId(int erreklamazioId) {
        this.erreklamazioId = erreklamazioId;
    }

    /**
     * Erreklamazioa sortu zen data itzultzen du.
     *
     * @return Erreklamazioaren data
     */
    public Date getErreklamazioData() {
        return erreklamazioData;
    }

    /**
     * Erreklamazioaren data ezartzen du.
     *
     * @param erreklamazioData Ezarri beharreko data
     */
    public void setErreklamazioData(Date erreklamazioData) {
        this.erreklamazioData = erreklamazioData;
    }

    /**
     * Galdutako objektuaren deskribapena itzultzen du.
     *
     * @return Deskribapen bilatua
     */
    public String getDeskribapenBilatua() {
        return deskribapenBilatua;
    }

    /**
     * Galdutako objektuaren deskribapena ezartzen du.
     *
     * @param deskribapenBilatua Ezarri beharreko deskribapena
     */
    public void setDeskribapenBilatua(String deskribapenBilatua) {
        this.deskribapenBilatua = deskribapenBilatua;
    }

    /**
     * Erreklamazioaren uneko egoera itzultzen du.
     *
     * @return Egoera enumerazioa
     */
    public EgoeraErreklamazioa getEgoera() {
        return egoera;
    }

    /**
     * Erreklamazioaren egoera ezartzen du.
     *
     * @param egoera Ezarri beharreko egoera
     */
    public void setEgoera(EgoeraErreklamazioa egoera) {
        this.egoera = egoera;
    }

    /**
     * Erreklamazioa egin duen hartzailea itzultzen du.
     *
     * @return Hartzailea objektua
     */
    public Hartzailea getHartzailea() {
        return hartzailea;
    }

    /**
     * Erreklamazioaren hartzailea ezartzen du.
     *
     * @param hartzailea Ezarri beharreko hartzailea
     */
    public void setHartzailea(Hartzailea hartzailea) {
        this.hartzailea = hartzailea;
    }

    /**
     * Lotutako kategoria itzultzen du.
     *
     * @return Kategoria objektua
     */
    public Kategoria getKategoria() {
        return kategoria;
    }

    /**
     * Erreklamazioaren kategoria ezartzen du.
     *
     * @param kategoria Ezarri beharreko kategoria
     */
    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    /**
     * Erreklamazioa erregistratu duen langilea itzultzen du.
     *
     * @return Langilea objektua
     */
    public Langilea getLangilea() {
        return langilea;
    }

    /**
     * Erreklamazioaren langilea ezartzen du.
     *
     * @param langilea Ezarri beharreko langilea
     */
    public void setLangilea(Langilea langilea) {
        this.langilea = langilea;
    }

    /**
     * Erreklamazioaren IDa kate gisa itzultzen du.
     *
     * @return Erreklamazioaren ID testua
     */
    public String getIdString() {
        return String.valueOf(this.erreklamazioId);
    }

    /**
     * Erreklamazioaren data formatu irakurgarrian itzultzen du (dd/MM/yyyy).
     *
     * @return Erreklamazioaren data formateaturiko katea, edo "—" null bada
     */
    public String getDataFormatua() {
        if (this.erreklamazioData == null) {
            return "—";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.erreklamazioData);
    }

    /**
     * Galdutako objektuaren deskribapena itzultzen du, null bada "—".
     *
     * @return Deskribapena katea, edo "—" null bada
     */
    public String getDeskribapena() {
        if (this.deskribapenBilatua != null) {
            return this.deskribapenBilatua;
        }
        return "—";
    }

    @Override
    public String toString() {
        return erreklamazioId + " | " + deskribapenBilatua + " | " + egoera;
    }
}
