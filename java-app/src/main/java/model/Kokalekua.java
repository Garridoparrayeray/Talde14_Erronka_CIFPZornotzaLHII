package model;

public class Kokalekua {

    private int kokalekuId;
    private String armairua;
    private String apala;
    private boolean bhaDa;

    public Kokalekua(String armairua, String apala, boolean bhaDa) {
        this.armairua = armairua;
        this.apala = apala;
        this.bhaDa = bhaDa;
    }

    public String getKokalekuOsoa() {
        return armairua + " - " + apala + (bhaDa ? " [BHA]" : "");
    }

    // Getters
    public int getKokalekuId() { return kokalekuId; }
    public String getArmairua() { return armairua; }
    public String getApala() { return apala; }
    public boolean isBhaDa() { return bhaDa; }

    @Override
    public String toString() {
        return getKokalekuOsoa();
    }
}
