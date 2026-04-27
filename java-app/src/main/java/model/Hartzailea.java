package model;

public abstract class Hartzailea {

    private int hartzaileId;
    private String telefonoa;
    private String emaila;
    private String helbidea;

    public Hartzailea(int hartzaileId, String telefonoa, String emaila) {
        this.hartzaileId = hartzaileId;
        this.telefonoa = telefonoa;
        this.emaila = emaila;
    }

    public abstract String getIdentifikazioa();

    // Getters & Setters
    public int getHartzaileId() { return hartzaileId; }
    public String getTelefonoa() { return telefonoa; }
    public String getEmaila() { return emaila; }
    public String getHelbidea() { return helbidea; }
    public void setHelbidea(String helbidea) { this.helbidea = helbidea; }

    @Override
    public String toString() {
        return getIdentifikazioa() + " - " + telefonoa;
    }
}
