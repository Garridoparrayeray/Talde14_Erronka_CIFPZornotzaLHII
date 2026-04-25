package eus.zornotza.galdutakoak.model;

import eus.zornotza.galdutakoak.model.enums.EgoeraErreklamazioa;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Erreklamazioa {

    private int erreklamazioId;
    private Date erreklamazioData;
    private String deskribapenBilatua;
    private String markaBilatua;
    private EgoeraErreklamazioa egoera;
    private Hartzailea hartzailea;
    private Kategoria kategoria;
    private Langilea langilea;

    public Erreklamazioa(Hartzailea hartzailea, String deskribapenBilatua,
                         String markaBilatua, Date erreklamazioData) {
        this.hartzailea = hartzailea;
        this.deskribapenBilatua = deskribapenBilatua;
        this.markaBilatua = markaBilatua;
        this.erreklamazioData = erreklamazioData;
        this.egoera = EgoeraErreklamazioa.IREKITA;
    }

    public List<Artikulua> bilatuBateragarriak(List<Artikulua> zerrenda) {
        List<Artikulua> emaitzak = new ArrayList<>();
        for (Artikulua a : zerrenda) {
            if (a.getEgoera() == eus.zornotza.galdutakoak.model.enums.EgoeraArtikulua.BILTEGIAN) {
                boolean bat = false;
                if (markaBilatua != null && a.getMarka() != null &&
                    a.getMarka().toLowerCase().contains(markaBilatua.toLowerCase())) {
                    bat = true;
                }
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

    public void eguneratuEgoera(EgoeraErreklamazioa egoera) {
        this.egoera = egoera;
    }

    // Getters
    public int getErreklamazioId() { return erreklamazioId; }
    public Date getErreklamazioData() { return erreklamazioData; }
    public String getDeskribapenBilatua() { return deskribapenBilatua; }
    public String getMarkaBilatua() { return markaBilatua; }
    public EgoeraErreklamazioa getEgoera() { return egoera; }
    public Hartzailea getHartzailea() { return hartzailea; }
    public Kategoria getKategoria() { return kategoria; }
    public void setKategoria(Kategoria kategoria) { this.kategoria = kategoria; }
    public Langilea getLangilea() { return langilea; }
    public void setLangilea(Langilea langilea) { this.langilea = langilea; }

    @Override
    public String toString() {
        return erreklamazioId + " | " + deskribapenBilatua + " | " + egoera;
    }
}
