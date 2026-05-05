package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Erreklamazio baten informazioa gordetzen duen modelo klasea.
 *
 * @author Yeray Garrido
 */
public class Erreklamazioa {

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

    public String getJabeIzena() {
        return hartzailea.getIzena();
    }

    public String getJabeAbizena() {
        return hartzailea.getAbizena();
    }

    public String getJabeTelefonoa() {
        if (hartzailea.getTelefonoa() == null) {
            return "—";
        } else {
            return hartzailea.getTelefonoa();
        }
    }

    public String getJabeEmaila() {
        if (hartzailea.getEmaila() == null) {
            return "—";
        } else {
            return hartzailea.getEmaila();
        }
    }

    public String getJabeNan() {
        return hartzailea.getNan();
    }

    public String getKategoriaIzena() {
        if (kategoria != null) {
            return kategoria.getIzena();
        } else {
            return "—";
        }
    }

    public String getEgoeraTestua() {
        if (egoera != null) {
            return egoera.toString().toLowerCase();
        } else {
            return "irekita";
        }
    }

    // Getters
    public int getErreklamazioId() {
        return erreklamazioId;
    }

    public void setErreklamazioId(int erreklamazioId) {
        this.erreklamazioId = erreklamazioId;
    }

    public Date getErreklamazioData() {
        return erreklamazioData;
    }

    public void setErreklamazioData(Date erreklamazioData) {
        this.erreklamazioData = erreklamazioData;
    }

    public String getDeskribapenBilatua() {
        return deskribapenBilatua;
    }

    public void setDeskribapenBilatua(String deskribapenBilatua) {
        this.deskribapenBilatua = deskribapenBilatua;
    }

    public EgoeraErreklamazioa getEgoera() {
        return egoera;
    }

    public void setEgoera(EgoeraErreklamazioa egoera) {
        this.egoera = egoera;
    }

    public Hartzailea getHartzailea() {
        return hartzailea;
    }

    public void setHartzailea(Hartzailea hartzailea) {
        this.hartzailea = hartzailea;
    }

    public Kategoria getKategoria() {
        return kategoria;
    }

    public void setKategoria(Kategoria kategoria) {
        this.kategoria = kategoria;
    }

    public Langilea getLangilea() {
        return langilea;
    }

    public void setLangilea(Langilea langilea) {
        this.langilea = langilea;
    }

    public String getIdString() {
        return String.valueOf(this.erreklamazioId);
    }

    public String getDataFormatua() {
        if (this.erreklamazioData == null) {
            return "—";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(this.erreklamazioData);
    }

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
