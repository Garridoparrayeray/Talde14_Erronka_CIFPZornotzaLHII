package model;

import java.util.Objects;

/**
 * Artikulua jaso dezakeen edozein entitate adierazten duen klase abstraktua.
 * Jabea eta Erakundea klaseek heredatzen dute.
 *
 * @author Yeray Garrido
 */
public abstract class Hartzailea {

    private int hartzaileId;
    private String telefonoa;
    private String emaila;
    private String helbidea;

    /**
     * Hartzailearen eraikitzailea.
     *
     * @param hartzaileId Hartzailearen identifikagailu bakarra
     * @param telefonoa Hartzailearen telefono zenbakia
     * @param emaila Hartzailearen helbide elektronikoa
     */
    public Hartzailea(int hartzaileId, String telefonoa, String emaila) {
        this.hartzaileId = hartzaileId;
        this.telefonoa = telefonoa;
        this.emaila = emaila;
    }

    /**
     * Hartzailearen identifikazio nagusia itzultzen du (NAN edo IFZ).
     *
     * @return Identifikazio testua
     */
    public abstract String getIdentifikazioa();

    // Getters & Setters
    public int getHartzaileId() {
        return hartzaileId;
    }

    /**
     * @return propietarioaren izena (Jabea bada); bestela "—"
     */
    public String getIzena() {
        return "—";
    }

    /**
     * @return propietarioaren abizena (Jabea bada); bestela "—"
     */
    public String getAbizena() {
        return "—";
    }

    /**
     * @return NAN zenbakia (Jabea bada); bestela "—"
     */
    public String getNan() {
        return "—";
    }

    public String getTelefonoa() {
        return telefonoa;
    }

    public String getEmaila() {
        return emaila;
    }

    public String getHelbidea() {
        return helbidea;
    }

    public void setHelbidea(String helbidea) {
        this.helbidea = helbidea;
    }

    @Override
    public String toString() {
        return getIdentifikazioa() + " - " + telefonoa;
    }

    @Override
    public int hashCode() {
        return Objects.hash(emaila, hartzaileId, helbidea, telefonoa);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Hartzailea other = (Hartzailea) obj;
        return Objects.equals(emaila, other.emaila) && hartzaileId == other.hartzaileId
                && Objects.equals(helbidea, other.helbidea) && Objects.equals(telefonoa, other.telefonoa);
    }

}
