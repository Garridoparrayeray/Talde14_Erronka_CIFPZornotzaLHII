package model;

import java.io.Serializable;

/**
 * Galdu den objektua aurkitu eta udaletxera ekarri duen pertsona.
 * Bi urteko iraungitze-epea igaro ostean, jabea ez bada agertu,
 * aurkitzaileari jakinarazi behar zaio.
 */
public class Aurkitzailea implements Serializable {

    private static final long serialVersionUID = 1L;

    private int aurkitzaileaId;
    private String izena;
    private String abizena;
    private String telefonoa;
    private String emaila;
    private String aurkipenLekua;
    private String idArtikulua;

    public Aurkitzailea(String izena, String abizena, String telefonoa,
            String emaila, String aurkipenLekua, String idArtikulua) {
        this.izena = izena;
        this.abizena = abizena;
        this.telefonoa = telefonoa;
        this.emaila = emaila;
        this.aurkipenLekua = aurkipenLekua;
        this.idArtikulua = idArtikulua;
    }

    public String getIzenOsoa() {
        return izena + " " + abizena;
    }

    public int getAurkitzaileaId() { return aurkitzaileaId; }
    public void setAurkitzaileaId(int id) { this.aurkitzaileaId = id; }
    public String getIzena() { return izena; }
    public String getAbizena() { return abizena; }
    public String getTelefonoa() { return telefonoa; }
    public String getEmaila() { return emaila; }
    public String getAurkipenLekua() { return aurkipenLekua; }
    public String getIdArtikulua() { return idArtikulua; }

    @Override
    public String toString() {
        return izena + " " + abizena + " (" + (telefonoa != null ? telefonoa : emaila) + ")";
    }
}
