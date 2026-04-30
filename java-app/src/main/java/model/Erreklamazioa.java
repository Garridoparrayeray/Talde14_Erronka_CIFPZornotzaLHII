package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Erreklamazio baten informazioa gordetzen duen modelo klasea.
 * @author Yeray Garrido
 */
public class Erreklamazioa {

    private int erreklamazioId;
    private Date erreklamazioData;
    private String deskribapenBilatua;
    private String markaBilatua;
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
     * Biltegian dauden artikuluen artean, erreklamazio honekin bat datozenak bilatzen ditu.
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
                if (bat) emaitzak.add(a);
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

    // Helpers para la vista (evitan instanceof en el controller)
    public String getJabeIzena() {
        if (hartzailea instanceof Jabea) {
            return ((Jabea) hartzailea).getIzena();
        } else {
            return "—";
        }
    }

    public String getJabeAbizena() {
        if (hartzailea instanceof Jabea) {
            return ((Jabea) hartzailea).getAbizena();
        } else {
            return "—";
        }
    }

    public String getJabeTelefonoa() {
        if (hartzailea instanceof Jabea) {
            return ((Jabea) hartzailea).getTelefonoa();
        } else {
            return "—";
        }
    }

    public String getJabeEmaila() {
        if (hartzailea instanceof Jabea) {
            return ((Jabea) hartzailea).getEmaila();
        } else {
            return "—";
        }
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
    public int getErreklamazioId() { return erreklamazioId; }
    public void setErreklamazioId(int erreklamazioId) { this.erreklamazioId = erreklamazioId; }
    public Date getErreklamazioData() { return erreklamazioData; }
    public void setErreklamazioData(Date erreklamazioData) { this.erreklamazioData = erreklamazioData; }
    public String getDeskribapenBilatua() { return deskribapenBilatua; }
    public void setDeskribapenBilatua(String deskribapenBilatua) { this.deskribapenBilatua = deskribapenBilatua; }
    public String getMarkaBilatua() { return markaBilatua; }
    public void setMarkaBilatua(String markaBilatua) { this.markaBilatua = markaBilatua; }
    public EgoeraErreklamazioa getEgoera() { return egoera; }
    public void setEgoera(EgoeraErreklamazioa egoera) { this.egoera = egoera; }
    public Hartzailea getHartzailea() { return hartzailea; }
    public void setHartzailea(Hartzailea hartzailea) { this.hartzailea = hartzailea; }
    public Kategoria getKategoria() { return kategoria; }
    public void setKategoria(Kategoria kategoria) { this.kategoria = kategoria; }
    public Langilea getLangilea() { return langilea; }
    public void setLangilea(Langilea langilea) { this.langilea = langilea; }

    @Override
    public String toString() {
        return erreklamazioId + " | " + deskribapenBilatua + " | " + egoera;
    }
}
