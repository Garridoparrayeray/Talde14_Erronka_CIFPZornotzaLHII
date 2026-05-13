package model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Artikulua jaso dezakeen edozein entitate adierazten duen klase abstraktua.
 * Jabea eta Erakundea klaseek heredatzen dute.
 *
 * @author Yeray Garrido
 */
public abstract class Hartzailea implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * Hartzailearen datu-baseko IDa itzultzen du.
     *
     * @return Hartzailearen IDa
     */
    public int getHartzaileId() {
        return hartzaileId;
    }

    /**
     * Hartzailearen datu-baseko IDa ezartzen du. Offline moduan
     * {@code BiltegiLokala}-k erabiltzen du ID berria esleitzeko.
     *
     * @param id Ezarri beharreko IDa
     */
    public void setHartzaileId(int id) {
        this.hartzaileId = id;
    }

    /**
     * Jabearen izena itzultzen du, Jabea azpiklaseak gainidatzita. Oinarrizko
     * hartzailean "—" itzultzen du.
     *
     * @return Izena, edo "—" ez bada Jabea
     */
    public String getIzena() {
        return "—";
    }

    /**
     * Jabearen abizena itzultzen du, Jabea azpiklaseak gainidatzita. Oinarrizko
     * hartzailean "—" itzultzen du.
     *
     * @return Abizena, edo "—" ez bada Jabea
     */
    public String getAbizena() {
        return "—";
    }

    /**
     * Jabearen NAN zenbakia itzultzen du, Jabea azpiklaseak gainidatzita.
     * Oinarrizko hartzailean "—" itzultzen du.
     *
     * @return NAN testua, edo "—" ez bada Jabea
     */
    public String getNan() {
        return "—";
    }

    /**
     * Hartzailearen telefono zenbakia itzultzen du.
     *
     * @return Telefonoa
     */
    public String getTelefonoa() {
        return telefonoa;
    }

    /**
     * Hartzailearen helbide elektronikoa itzultzen du.
     *
     * @return Emaila
     */
    public String getEmaila() {
        return emaila;
    }

    /**
     * Hartzailearen helbidea itzultzen du.
     *
     * @return Helbidea
     */
    public String getHelbidea() {
        return helbidea;
    }

    /**
     * Hartzailearen helbidea ezartzen du.
     *
     * @param helbidea Ezarri beharreko helbidea
     */
    public void setHelbidea(String helbidea) {
        this.helbidea = helbidea;
    }

    /**
     * Hartzailearen testuzko adierazpena itzultzen du.
     *
     * @return Identifikazioa eta telefonoa
     */
    @Override
    public String toString() {
        return getIdentifikazioa() + " - " + telefonoa;
    }

    /**
     * Hash-kode bat itzultzen du hartzailearen eremuetan oinarrituta.
     *
     * @return Hash-kodea
     */
    @Override
    public int hashCode() {
        return Objects.hash(emaila, hartzaileId, helbidea, telefonoa);
    }

    /**
     * Bi hartzaile berdinak diren egiaztatzen du eremu guztiak konparatuz.
     *
     * @param obj Konparatu beharreko objektua
     * @return Berdinak badira true
     */
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
        return Objects.equals(emaila, other.emaila)
                && hartzaileId == other.hartzaileId
                && Objects.equals(helbidea, other.helbidea)
                && Objects.equals(telefonoa, other.telefonoa);
    }
}
