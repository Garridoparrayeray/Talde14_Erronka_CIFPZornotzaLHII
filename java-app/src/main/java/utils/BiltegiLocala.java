package utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import dao.ArtikuluaDAO;
import dao.ErreklamazioaDAO;
import dao.KategoriaDAO;
import dao.KokalekuaDAO;
import dao.LangileaDAO;
import dao.MugimenduDAO;
import model.Administratzailea;
import model.Artikulua;
import model.Aurkitzailea;
import model.AzkenMugimendua;
import model.EgoeraArtikulua;
import model.EgoeraErreklamazioa;
import model.Erakundea;
import model.Erreklamazioa;
import model.Jabea;
import model.Kategoria;
import model.KategoriaKopurua;
import model.Kokalekua;
import model.Langilea;
import model.MugimenduLerroa;

/**
 * DB datuen kopia lokala, offline erabilerarako.
 *
 * <p>
 * Aplikazioa online dagoenean {@code sincronizatuDBtik()} metodoak datu-baseko
 * eduki guztia memoria eta {@code store.dat} fitxategian gordetzen du.
 * Aplikazioa ixten denean ere sinkronizatu egiten da {@code Main.stop()}-etik.
 * Hurrengo exekuzioan DB eskuragarri ez bada, azken sinkronizazioko datuak
 * erabiltzen dira fitxategitik irakurrita.</p>
 *
 * <p>
 * Klase Estatikoa — Metodo guztiak zuzenean deitu.</p>
 */
public class BiltegiLocala {

    private static final Logger LOG = LogKudeatzailea.lortu(BiltegiLocala.class);
    private static final String FITXATEGIA_IZENA = "store.dat";

    private static DatuakPoltsa poltsa;

    public static synchronized void hasieratu() {
        if (poltsa == null) {
            poltsa = kargatu();
            gorde();
        }
    }

    // ─── Fitxategi kudeaketa ──────────────────────────────────────────────────
    private static Path getFitxategi() {
        Path dir = Paths.get(AppConfig.getExportBidea());
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Direktorioa ezin sortu: {0}", dir);
        }
        return dir.resolve(FITXATEGIA_IZENA);
    }

    private static DatuakPoltsa kargatu() {
        Path f = getFitxategi();
        if (Files.exists(f)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.toFile()))) {
                LOG.log(Level.INFO, "Offline datuak kargatzen: {0}", f);
                return (DatuakPoltsa) ois.readObject();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "store.dat ezin kargatu, hasierako datuak sortzen: {0}", e.getMessage());
                e.printStackTrace();
            }
        }
        return hasieratuDatuak();
    }

    public static synchronized void gorde() {
        Path f = getFitxategi();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f.toFile()))) {
            oos.writeObject(poltsa);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Offline datuak ezin gorde: {0}", e.toString());
            e.printStackTrace();
        }
    }

    /**
     * DB-tik datuak kargatu eta kaxa eguneratzen du.
     * ModoKudeatzailea.detektatu()-k deitzen du online dagoenean, kaxa beti
     * datu errealak eduki ditzan.
     */
    public static synchronized void sincronizatuDBtik() {
        try {
            hasieratu();
            // 1. Igo pendiente dauden operazioak
            sinkronizatuIlara();

            List<String[]> rolak = LangileaDAO.getRolak();
            if (!rolak.isEmpty()) {
                poltsa.rolak = new ArrayList<>(rolak);
            }

            List<Langilea> langileak = LangileaDAO.getGuztiak();
            if (!langileak.isEmpty()) {
                poltsa.langileak = new ArrayList<>(langileak);
                int maxLangile = 0;
                for (Langilea l : langileak) {
                    if (l.getLangileId() > maxLangile) {
                        maxLangile = l.getLangileId();
                    }
                }
                poltsa.langileNextId = maxLangile + 1;
            }

            List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
            if (!kategoriak.isEmpty()) {
                poltsa.kategoriak = new ArrayList<>(kategoriak);
                int maxKat = 0;
                for (Kategoria k : kategoriak) {
                    if (k.getKategoriaId() > maxKat) {
                        maxKat = k.getKategoriaId();
                    }
                }
                poltsa.kategoriaNextId = maxKat + 1;
            }

            List<Kokalekua> kokalekuak = KokalekuaDAO.getZerrenda();
            if (!kokalekuak.isEmpty()) {
                poltsa.kokalekuak = new ArrayList<>(kokalekuak);
                int maxKok = 0;
                for (Kokalekua k : kokalekuak) {
                    if (k.getKokalekuId() > maxKok) {
                        maxKok = k.getKokalekuId();
                    }
                }
                poltsa.kokalekuNextId = maxKok + 1;
            }

            List<Artikulua> artikuluak = ArtikuluaDAO.getGuztiak();
            poltsa.artikuluak = new ArrayList<>(artikuluak);

            List<Erreklamazioa> erreklamazioak = ErreklamazioaDAO.getGuztiak();
            if (!erreklamazioak.isEmpty()) {
                poltsa.erreklamazioak = new ArrayList<>(erreklamazioak);
                int maxErrek = 0;
                for (Erreklamazioa e : erreklamazioak) {
                    if (e.getErreklamazioId() > maxErrek) {
                        maxErrek = e.getErreklamazioId();
                    }
                }
                poltsa.erreklamazioNextId = maxErrek + 1;
            }

            List<MugimenduLerroa> mugimenduak = MugimenduDAO.getGuztiak();
            poltsa.mugimenduak = new LinkedList<>(mugimenduak);

            LOG.log(Level.INFO, "BiltegiLocala DB-tik sinkronizatuta: {0} artikulu, {1} langile.",
                    new Object[]{artikuluak.size(), langileak.size()});
        } catch (Exception e) {
            LOG.log(Level.WARNING, "DB sinkronizazioa huts egin du: {0}", e.getMessage());
            e.printStackTrace();
        } finally {
            gorde(); // Beti gorde, nahiz eta errore bat egon
        }
    }

    private static void sinkronizatuIlara() {
        if (poltsa.itxaronEragiketak == null || poltsa.itxaronEragiketak.isEmpty()) {
            return;
        }

        LOG.info("Offline moduan egindako " + poltsa.itxaronEragiketak.size() + " eragiketa aurkitu dira. DB-ra igotzen...");
        java.util.Map<String, String> artikuluIdMap = new java.util.HashMap<>();

        for (ItxaronEragiketa op : new ArrayList<>(poltsa.itxaronEragiketak)) {
            try {
                Object[] a = op.argumentuak;
                switch (op.entitatea) {
                    case "LANGILEA":
                        if ("INSERT".equals(op.ekintza)) {
                            LangileaDAO.gehitu((String) a[0], (String) a[1], (String) a[2], (String) a[3], (Integer) a[4]);
                        } else if ("UPDATE".equals(op.ekintza)) {
                            LangileaDAO.eguneratu((Integer) a[0], (String) a[1], (String) a[2], (String) a[3], (Integer) a[4], (String) a[5]);
                        } else if ("DELETE".equals(op.ekintza)) {
                            LangileaDAO.ezabatu((Integer) a[0]);
                        }
                        break;
                    case "ARTIKULUA":
                        if ("INSERT".equals(op.ekintza)) {
                            String oldId = (String) a[0];
                            String newId = dao.ArtikuluaDAO.gehitu((String) a[1], (String) a[2], (Boolean) a[3], (Integer) a[4], (Integer) a[5], (java.sql.Date) a[6], (String) a[7]);
                            if (newId != null && !newId.equals(oldId)) {
                                artikuluIdMap.put(oldId, newId);
                            }
                        } else if ("UPDATE".equals(op.ekintza)) {
                            String id = artikuluIdMap.getOrDefault((String) a[0], (String) a[0]);
                            dao.ArtikuluaDAO.eguneratu(id, (String) a[1], (String) a[2], (Integer) a[3], (Integer) a[4], (String) a[5]);
                        } else if ("DELETE".equals(op.ekintza)) {
                            String id = artikuluIdMap.getOrDefault((String) a[0], (String) a[0]);
                            dao.ArtikuluaDAO.ezabatu(id);
                        }
                        break;
                    case "KATEGORIA":
                        if ("INSERT".equals(op.ekintza)) {
                            dao.KategoriaDAO.gehitu((String) a[0]);
                        } else if ("UPDATE".equals(op.ekintza)) {
                            dao.KategoriaDAO.aldatuIzena((Integer) a[0], (String) a[1]);
                        } else if ("DELETE".equals(op.ekintza)) {
                            dao.KategoriaDAO.ezabatu((Integer) a[0]);
                        }
                        break;
                    case "KOKALEKUA":
                        if ("INSERT".equals(op.ekintza)) {
                            dao.KokalekuaDAO.gehitu((String) a[0], (String) a[1], (Boolean) a[2]);
                        } else if ("UPDATE".equals(op.ekintza)) {
                            dao.KokalekuaDAO.eguneratu((Integer) a[0], (String) a[1], (String) a[2], (Boolean) a[3]);
                        } else if ("DELETE".equals(op.ekintza)) {
                            dao.KokalekuaDAO.ezabatu((Integer) a[0]);
                        }
                        break;
                    case "ERREKLAMAZIOA":
                        if ("INSERT".equals(op.ekintza)) {
                            dao.ErreklamazioaDAO.gorde((String) a[0], (String) a[1], (String) a[2], (String) a[3], (String) a[4], (Integer) a[5], (String) a[6], (Integer) a[7]);
                        } else if ("UPDATE_EGOERA".equals(op.ekintza)) {
                            dao.ErreklamazioaDAO.updateEgoera((String) a[0], (String) a[1]);
                        }
                        break;
                    case "EMANALDIA":
                        String emId = artikuluIdMap.getOrDefault((String) a[0], (String) a[0]);
                        if ("FORMALIZATU".equals(op.ekintza)) {
                            dao.EmanaldiaDAO.formalizatu(emId, (String) a[1], (String) a[2], (String) a[3], (String) a[4], (String) a[5], (String) a[6], (String) a[7], (Integer) a[8], (String) a[9]);
                        } else if ("FORMALIZATU_ERAKUNDEA".equals(op.ekintza)) {
                            dao.EmanaldiaDAO.formalizatuErakundea(emId, (String) a[1], (String) a[2], (String) a[3], (String) a[4], (String) a[5], (String) a[6], (Integer) a[7], (String) a[8]);
                        }
                        break;
                    case "AURKITZAILEA":
                        if ("INSERT".equals(op.ekintza)) {
                            String auId = artikuluIdMap.getOrDefault((String) a[0], (String) a[0]);
                            dao.AurkitzaileaDAO.gehitu(auId, (String) a[1], (String) a[2], (String) a[3], (String) a[4], (String) a[5]);
                        }
                        break;
                }
            } catch (Exception e) {
                LOG.warning("Ezin izan da prozesatu eragiketa (" + op.entitatea + " - " + op.ekintza + "): " + e.getMessage());
            }
        }

        poltsa.itxaronEragiketak.clear();
        gorde();
    }

    private static void gehituItxaronEragiketa(String entitatea, String ekintza, Object[] argumentuak) {
        if (poltsa.itxaronEragiketak == null) {
            poltsa.itxaronEragiketak = new ArrayList<>();
        }
        poltsa.itxaronEragiketak.add(new ItxaronEragiketa(entitatea, ekintza, argumentuak));
    }

    private static DatuakPoltsa hasieratuDatuak() {
        LOG.info("store.dat ez da aurkitu — poltsa hutsa sortzen da. DB-tik sinkronizatuko da.");
        return new DatuakPoltsa();
    }

    // ─── LANGILEA ─────────────────────────────────────────────────────────────
    public static List<Langilea> getLangileak() {
        return new ArrayList<>(poltsa.langileak);
    }

    public static List<String[]> getRolak() {
        return new ArrayList<>(poltsa.rolak);
    }

    public static synchronized boolean langileaGehitu(String izena, String abizena,
            String erabiltzailea, String pasahitza, int idRola) {
        String hash = BCrypt.hashpw(pasahitza, BCrypt.gensalt(10));
        String rolaDesk = getRolaDesk(idRola);
        Langilea l;
        if ("Administratzailea".equals(rolaDesk)) {
            l = new Administratzailea(poltsa.langileNextId, izena, abizena, erabiltzailea, hash);
        } else {
            l = new Langilea(poltsa.langileNextId, izena, abizena, erabiltzailea, hash);
        }
        l.setRola(rolaDesk);
        poltsa.langileak.add(l);
        poltsa.langileNextId++;
        gehituItxaronEragiketa("LANGILEA", "INSERT", new Object[]{izena, abizena, erabiltzailea, pasahitza, idRola});
        gorde();
        return true;
    }

    public static Langilea login(String erabiltzailea, String pasahitza) {
        LOG.info("Login lokala intentatzen: '" + erabiltzailea + "'. Langile kopurua: " + poltsa.langileak.size());
        for (Langilea l : poltsa.langileak) {
            if (l.getErabiltzailea().equals(erabiltzailea)) {
                String gordetako = l.getPasahitzaHash();
                LOG.info("Erabiltzailea aurkituta. Gordetako pasahitza DB-n: " + (gordetako == null ? "NULL" : "DAGO"));
                if (gordetako != null) {
                    try {
                        if (gordetako.startsWith("$2a$") || gordetako.startsWith("$2b$") || gordetako.startsWith("$2y$")) {
                            if (BCrypt.checkpw(pasahitza, gordetako)) {
                                LOG.info("Login OK (BCrypt)");
                                return l;
                            }
                        } else if (gordetako.equals(pasahitza)) {
                            LOG.info("Login OK (Testu planoa)");
                            return l;
                        }
                        LOG.warning("Pasahitza ez da zuzena erabiltzailearentzat: " + erabiltzailea);
                    } catch (Exception e) {
                        LOG.warning("Errorea pasahitza egiaztatzean: " + e.getMessage());
                    }
                } else {
                    LOG.warning("Gordetako pasahitza NULL da, ezin da egiaztatu.");
                }
                return null;
            }
        }
        LOG.warning("Erabiltzailea ez da aurkitu biltegi lokalean.");
        return null;
    }

    public static synchronized boolean langileaEguneratu(int id, String izena, String abizena,
            String erabiltzailea, int idRola, String pasahitzaBerria) {
        for (Langilea l : poltsa.langileak) {
            if (l.getLangileId() == id) {
                l.setIzena(izena);
                l.setAbizena(abizena);
                l.setErabiltzailea(erabiltzailea);
                l.setRola(getRolaDesk(idRola));
                if (pasahitzaBerria != null && !pasahitzaBerria.isEmpty()) {
                    l.setPasahitzaHash(BCrypt.hashpw(pasahitzaBerria, BCrypt.gensalt(10)));
                }
                gehituItxaronEragiketa("LANGILEA", "UPDATE", new Object[]{id, izena, abizena, erabiltzailea, idRola, pasahitzaBerria});
                gorde();
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean langileaEzabatu(int id) {
        boolean removed = false;
        java.util.Iterator<Langilea> it = poltsa.langileak.iterator();
        while (it.hasNext()) {
            if (it.next().getLangileId() == id) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            gehituItxaronEragiketa("LANGILEA", "DELETE", new Object[]{id});
            gorde();
        }
        return removed;
    }

    private static String getRolaDesk(int idRola) {
        for (String[] r : poltsa.rolak) {
            if (Integer.parseInt(r[0]) == idRola) {
                return r[1];
            }
        }
        return "Langilea";
    }

    // ─── ARTIKULUA ────────────────────────────────────────────────────────────
    public static List<Artikulua> getArtikuluak() {
        return new ArrayList<>(poltsa.artikuluak);
    }

    public static Artikulua getArtikuluaByKodea(String kodea) {
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getArtikuluKodea().equals(kodea)) {
                return a;
            }
        }
        return null;
    }

    public static synchronized String artikuluaGehitu(String izena, String deskribapena,
            boolean iragankorra, int idKategoria, int idKokalekua,
            java.sql.Date sarreraData, String argazkiBidea) {
        String kodea = sortuArtikuluKodea(sarreraData);
        Artikulua a = new Artikulua(kodea, izena, deskribapena, null, null, sarreraData, argazkiBidea);
        if (idKategoria > 0) {
            for (Kategoria k : poltsa.kategoriak) {
                if (k.getKategoriaId() == idKategoria) {
                    a.setKategoria(k);
                    break;
                }
            }
        }
        if (idKokalekua > 0) {
            for (Kokalekua k : poltsa.kokalekuak) {
                if (k.getKokalekuId() == idKokalekua) {
                    a.setKokalekua(k);
                    break;
                }
            }
        }
        poltsa.artikuluak.add(a);
        String now = now();
        poltsa.mugimenduak.addFirst(new MugimenduLerroa(now, "—",
                "Artikulua sisteman erregistratu da: " + kodea, kodea));
        gehituItxaronEragiketa("ARTIKULUA", "INSERT", new Object[]{kodea, izena, deskribapena, iragankorra, idKategoria, idKokalekua, sarreraData, argazkiBidea});
        gorde();
        return kodea;
    }

    public static synchronized boolean artikuluaEguneratu(String kodea, String izena,
            String deskribapena, int idKategoria, int idKokalekua, String argazkiBidea) {
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getArtikuluKodea().equals(kodea)) {
                a.setIzenburua(izena);
                a.setDeskribapena(deskribapena);
                if (idKategoria > 0) {
                    Kategoria aurkituaK = null;
                    for (Kategoria k : poltsa.kategoriak) {
                        if (k.getKategoriaId() == idKategoria) {
                            aurkituaK = k;
                            break;
                        }
                    }
                    a.setKategoria(aurkituaK);
                } else {
                    a.setKategoria(null);
                }
                if (idKokalekua > 0) {
                    Kokalekua aurkituaKok = null;
                    for (Kokalekua k : poltsa.kokalekuak) {
                        if (k.getKokalekuId() == idKokalekua) {
                            aurkituaKok = k;
                            break;
                        }
                    }
                    a.setKokalekua(aurkituaKok);
                } else {
                    a.setKokalekua(null);
                }
                if (argazkiBidea != null && !argazkiBidea.isEmpty()) {
                    a.setArgazkiBidea(argazkiBidea);
                }
                gehituItxaronEragiketa("ARTIKULUA", "UPDATE", new Object[]{kodea, izena, deskribapena, idKategoria, idKokalekua, argazkiBidea});
                gorde();
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean artikuluaEzabatu(String kodea) {
        boolean removed = false;
        java.util.Iterator<Artikulua> it = poltsa.artikuluak.iterator();
        while (it.hasNext()) {
            if (it.next().getArtikuluKodea().equals(kodea)) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            gehituItxaronEragiketa("ARTIKULUA", "DELETE", new Object[]{kodea});
            gorde();
        }
        return removed;
    }

    private static String sortuArtikuluKodea(java.sql.Date sarreraData) {
        String urteStr = String.format("%02d", Calendar.getInstance().get(Calendar.YEAR) % 100);
        if (sarreraData != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sarreraData);
            urteStr = String.format("%02d", cal.get(Calendar.YEAR) % 100);
        }
        final String urte = urteStr;
        long count = 0;
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getArtikuluKodea().endsWith("-" + urte)) {
                count++;
            }
        }
        return String.format("G-%03d-%s", count + 1, urteStr);
    }

    // ─── KATEGORIA ────────────────────────────────────────────────────────────
    public static List<Kategoria> getKategoriak() {
        return new ArrayList<>(poltsa.kategoriak);
    }

    public static synchronized boolean kategoriaGehitu(String izena) {
        poltsa.kategoriak.add(new Kategoria(poltsa.kategoriaNextId++, izena));
        gehituItxaronEragiketa("KATEGORIA", "INSERT", new Object[]{izena});
        gorde();
        return true;
    }

    public static synchronized boolean kategoriaAldatuIzena(int id, String izena) {
        for (Kategoria k : poltsa.kategoriak) {
            if (k.getKategoriaId() == id) {
                k.setIzena(izena);
                gehituItxaronEragiketa("KATEGORIA", "UPDATE", new Object[]{id, izena});
                gorde();
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean kategoriaEzabatu(int id) {
        boolean removed = false;
        java.util.Iterator<Kategoria> it = poltsa.kategoriak.iterator();
        while (it.hasNext()) {
            if (it.next().getKategoriaId() == id) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            gehituItxaronEragiketa("KATEGORIA", "DELETE", new Object[]{id});
            gorde();
        }
        return removed;
    }

    // ─── KOKALEKUA ────────────────────────────────────────────────────────────
    public static List<Kokalekua> getKokalekuak() {
        List<Kokalekua> result = new ArrayList<>();
        for (Kokalekua k : poltsa.kokalekuak) {
            int kop = 0;
            for (Artikulua a : poltsa.artikuluak) {
                if (a.getKokalekua() != null && a.getKokalekua().getKokalekuId() == k.getKokalekuId()) {
                    kop++;
                }
            }
            k.setArtikuluKopurua(kop);
            result.add(k);
        }
        return result;
    }

    public static List<Kokalekua> getKokalekuakZerrenda() {
        return new ArrayList<>(poltsa.kokalekuak);
    }

    public static synchronized boolean kokalekuaGehitu(String armairua, String apala, boolean bhaDa) {
        Kokalekua k = new Kokalekua(armairua, apala, bhaDa);
        k.setKokalekuId(poltsa.kokalekuNextId++);
        poltsa.kokalekuak.add(k);
        gehituItxaronEragiketa("KOKALEKUA", "INSERT", new Object[]{armairua, apala, bhaDa});
        gorde();
        return true;
    }

    public static synchronized boolean kokalekuaEguneratu(int id, String armairua, String apala, boolean bhaDa) {
        for (int i = 0; i < poltsa.kokalekuak.size(); i++) {
            if (poltsa.kokalekuak.get(i).getKokalekuId() == id) {
                Kokalekua berria = new Kokalekua(armairua, apala, bhaDa);
                berria.setKokalekuId(id);
                poltsa.kokalekuak.set(i, berria);
                for (Artikulua a : poltsa.artikuluak) {
                    if (a.getKokalekua() != null && a.getKokalekua().getKokalekuId() == id) {
                        a.setKokalekua(berria);
                    }
                }
                gehituItxaronEragiketa("KOKALEKUA", "UPDATE", new Object[]{id, armairua, apala, bhaDa});
                gorde();
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean kokalekuaEzabatu(int id) {
        boolean removed = false;
        java.util.Iterator<Kokalekua> it = poltsa.kokalekuak.iterator();
        while (it.hasNext()) {
            if (it.next().getKokalekuId() == id) {
                it.remove();
                removed = true;
            }
        }
        if (removed) {
            gehituItxaronEragiketa("KOKALEKUA", "DELETE", new Object[]{id});
            gorde();
        }
        return removed;
    }

    // ─── ERREKLAMAZIOA ────────────────────────────────────────────────────────
    public static List<Erreklamazioa> getErreklamazioak() {
        return new ArrayList<>(poltsa.erreklamazioak);
    }

    public static synchronized boolean erreklamazioaUpdateEgoera(String id, String egoera) {
        int idInt;
        try {
            idInt = Integer.parseInt(id);
        } catch (NumberFormatException e) {
            return false;
        }
        for (Erreklamazioa e : poltsa.erreklamazioak) {
            if (e.getErreklamazioId() == idInt) {
                try {
                    e.setEgoera(EgoeraErreklamazioa.valueOf(egoera.toUpperCase()));
                } catch (IllegalArgumentException ex) {
                    e.setEgoera(EgoeraErreklamazioa.IREKITA);
                }
                gehituItxaronEragiketa("ERREKLAMAZIOA", "UPDATE_EGOERA", new Object[]{id, egoera});
                gorde();
                return true;
            }
        }
        return false;
    }

    public static synchronized boolean erreklamazioaGorde(String nan, String izena, String abizena,
            String telefonoa, String emaila, int kategoriaId, String deskribapena, int idLangile) {
        Jabea jabea = lortuEdoSortuJabea(nan, izena, abizena, telefonoa, emaila);
        Erreklamazioa e = new Erreklamazioa(jabea, deskribapena, new Date());
        e.setErreklamazioId(poltsa.erreklamazioNextId++);
        if (kategoriaId > 0) {
            for (Kategoria k : poltsa.kategoriak) {
                if (k.getKategoriaId() == kategoriaId) {
                    e.setKategoria(k);
                    break;
                }
            }
        }
        poltsa.erreklamazioak.add(e);
        gehituItxaronEragiketa("ERREKLAMAZIOA", "INSERT", new Object[]{nan, izena, abizena, telefonoa, emaila, kategoriaId, deskribapena, idLangile});
        gorde();
        return true;
    }

    // ─── EMANALDIA ────────────────────────────────────────────────────────────
    public static synchronized boolean formalizatu(String idArtikulua, String nan, String izena,
            String abizena, String telefonoa, String emaila,
            String helbidea, String oharrak, int idLangile, String dokumentuBidea) {
        lortuEdoSortuJabea(nan, izena, abizena, telefonoa, emaila);
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getArtikuluKodea().equals(idArtikulua)) {
                a.aldatuEgoera(EgoeraArtikulua.ITZULITA);
                break;
            }
        }
        String langileIzena = getLangileIzena(idLangile);
        poltsa.mugimenduak.addFirst(new MugimenduLerroa(now(), langileIzena,
                "Artikulua " + izena + " " + abizena + "-ri eman zaio.", idArtikulua));
        gehituItxaronEragiketa("EMANALDIA", "FORMALIZATU", new Object[]{idArtikulua, nan, izena, abizena, telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea});
        gorde();
        return true;
    }

    public static synchronized boolean formalizatuErakundea(String idArtikulua, String ift,
            String izenOfiziala, String telefonoa, String emaila,
            String helbidea, String oharrak, int idLangile, String dokumentuBidea) {
        lortuEdoSortuErakundea(ift, izenOfiziala, telefonoa, emaila, helbidea);
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getArtikuluKodea().equals(idArtikulua)) {
                a.aldatuEgoera(EgoeraArtikulua.ITZULITA);
                break;
            }
        }
        String langileIzena = getLangileIzena(idLangile);
        poltsa.mugimenduak.addFirst(new MugimenduLerroa(now(), langileIzena,
                "Artikulua " + izenOfiziala + " erakundeari eman zaio.", idArtikulua));
        gehituItxaronEragiketa("EMANALDIA", "FORMALIZATU_ERAKUNDEA", new Object[]{idArtikulua, ift, izenOfiziala, telefonoa, emaila, helbidea, oharrak, idLangile, dokumentuBidea});
        gorde();
        return true;
    }

    // ─── AURKITZAILEA ─────────────────────────────────────────────────────────
    public static synchronized boolean aurkitzaileaGehitu(String idArtikulua, String izena,
            String abizena, String telefonoa, String emaila, String aurkipenLekua) {
        String tel;
        if (telefonoa.isEmpty()) {
            tel = null;
        } else {
            tel = telefonoa;
        }
        String ema;
        if (emaila.isEmpty()) {
            ema = null;
        } else {
            ema = emaila;
        }
        String lekua;
        if (aurkipenLekua.isEmpty()) {
            lekua = null;
        } else {
            lekua = aurkipenLekua;
        }
        Aurkitzailea a = new Aurkitzailea(izena, abizena, tel, ema, lekua, idArtikulua);
        a.setAurkitzaileaId(poltsa.aurkitzaileNextId++);
        poltsa.aurkitzaileak.add(a);
        gehituItxaronEragiketa("AURKITZAILEA", "INSERT", new Object[]{idArtikulua, izena, abizena, telefonoa, emaila, aurkipenLekua});
        gorde();
        return true;
    }

    public static Aurkitzailea getAurkitzaileaByArtikulua(String idArtikulua) {
        for (Aurkitzailea a : poltsa.aurkitzaileak) {
            if (idArtikulua.equals(a.getIdArtikulua())) {
                return a;
            }
        }
        return null;
    }

    // ─── MUGIMENDUA ───────────────────────────────────────────────────────────
    public static List<MugimenduLerroa> getMugimenduak() {
        return new ArrayList<>(poltsa.mugimenduak);
    }

    public static List<AzkenMugimendua> getAzkenMugimenduak() {
        List<AzkenMugimendua> result = new ArrayList<>();
        int max = Math.min(10, poltsa.mugimenduak.size());
        for (int i = 0; i < max; i++) {
            MugimenduLerroa m = poltsa.mugimenduak.get(i);
            result.add(new AzkenMugimendua(m.getArtikuluId(), m.getEkintza(), m.getData(), m.getLangilea()));
        }
        return result;
    }

    // ─── ESTADISTIKAK ─────────────────────────────────────────────────────────
    public static int biltegianKopurua() {
        int count = 0;
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getEgoera() == EgoeraArtikulua.BILTEGIAN) {
                count++;
            }
        }
        return count;
    }

    public static int bueltatakoKopurua() {
        int count = 0;
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getEgoera() == EgoeraArtikulua.ITZULITA) {
                count++;
            }
        }
        return count;
    }

    public static int iraungituakKopurua() {
        int count = 0;
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getEgoera() == EgoeraArtikulua.IRAUNGITA) {
                count++;
            }
        }
        return count;
    }

    public static int iraungitzearKopurua() {
        Date orain = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(orain);
        cal.add(Calendar.DAY_OF_YEAR, 30);
        Date hemendik30 = cal.getTime();
        int count = 0;
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getEgoera() == EgoeraArtikulua.BILTEGIAN && a.getIraungitzeData() != null
                    && !a.getIraungitzeData().before(orain) && !a.getIraungitzeData().after(hemendik30)) {
                count++;
            }
        }
        return count;
    }

    public static int erreklamazioIrekiakKopurua() {
        int count = 0;
        for (Erreklamazioa e : poltsa.erreklamazioak) {
            if (e.getEgoera() == EgoeraErreklamazioa.IREKITA) {
                count++;
            }
        }
        return count;
    }

    public static int langileKopurua() {
        return poltsa.langileak.size();
    }

    public static int artikuluGuztienKopurua() {
        return poltsa.artikuluak.size();
    }

    public static int kategoriaKopurua() {
        return poltsa.kategoriak.size();
    }

    public static int kokalekuakKopurua() {
        return poltsa.kokalekuak.size();
    }

    public static List<KategoriaKopurua> kategoriaKopuruak() {
        List<KategoriaKopurua> result = new ArrayList<>();
        for (Kategoria k : poltsa.kategoriak) {
            int kop = 0;
            for (Artikulua a : poltsa.artikuluak) {
                if (a.getKategoria() != null && a.getKategoria().getKategoriaId() == k.getKategoriaId()) {
                    kop++;
                }
            }
            result.add(new KategoriaKopurua(k.getIzena(), kop));
        }
        java.util.Collections.sort(result, new java.util.Comparator<KategoriaKopurua>() {
            @Override
            public int compare(KategoriaKopurua a, KategoriaKopurua b) {
                return b.getKopurua() - a.getKopurua();
            }
        });
        return result;
    }

    // ─── Laguntzaileak ────────────────────────────────────────────────────────
    private static Jabea lortuEdoSortuJabea(String nan, String izena, String abizena,
            String telefonoa, String emaila) {
        for (Jabea j : poltsa.jabeak) {
            if (nan.equals(j.getNan())) {
                return j;
            }
        }
        Jabea jabea = new Jabea(nan, izena, abizena, telefonoa, emaila);
        jabea.setHartzaileId(poltsa.hartzaileNextId++);
        poltsa.jabeak.add(jabea);
        return jabea;
    }

    private static Erakundea lortuEdoSortuErakundea(String ift, String izenOfiziala,
            String telefonoa, String emaila, String helbidea) {
        for (Erakundea e : poltsa.erakundeak) {
            if (ift.equals(e.getIfz())) {
                return e;
            }
        }
        Erakundea er = new Erakundea(ift, izenOfiziala, null, telefonoa);
        er.setHartzaileId(poltsa.hartzaileNextId++);
        poltsa.erakundeak.add(er);
        return er;
    }

    private static String getLangileIzena(int idLangile) {
        if (idLangile <= 0) {
            return "—";
        }
        for (Langilea l : poltsa.langileak) {
            if (l.getLangileId() == idLangile) {
                return l.getIzena() + " " + l.getAbizena();
            }
        }
        return "—";
    }

    private static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private static class ItxaronEragiketa implements Serializable {

        private static final long serialVersionUID = 1L;
        String entitatea;
        String ekintza;
        Object[] argumentuak;

        public ItxaronEragiketa(String entitatea, String ekintza, Object[] argumentuak) {
            this.entitatea = entitatea;
            this.ekintza = ekintza;
            this.argumentuak = argumentuak;
        }
    }

    // ─── Datu-poltsa (Serializable) ───────────────────────────────────────────
    private static class DatuakPoltsa implements Serializable {

        private static final long serialVersionUID = 2L;
        List<Langilea> langileak = new ArrayList<>();
        List<String[]> rolak = new ArrayList<>();
        List<Artikulua> artikuluak = new ArrayList<>();
        List<Kategoria> kategoriak = new ArrayList<>();
        List<Kokalekua> kokalekuak = new ArrayList<>();
        List<Jabea> jabeak = new ArrayList<>();
        List<Erakundea> erakundeak = new ArrayList<>();
        List<Erreklamazioa> erreklamazioak = new ArrayList<>();
        List<Aurkitzailea> aurkitzaileak = new ArrayList<>();
        LinkedList<MugimenduLerroa> mugimenduak = new LinkedList<>();
        List<ItxaronEragiketa> itxaronEragiketak = new ArrayList<>();
        int langileNextId = 1;
        int kategoriaNextId = 1;
        int kokalekuNextId = 1;
        int erreklamazioNextId = 1;
        int hartzaileNextId = 1;
        int aurkitzaileNextId = 1;
    }
}
