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
 * Singleton — {@code getInstance()} bidez lortu.</p>
 */
public class BiltegiLocala {

    private static final Logger LOG = LogKudeatzailea.lortu(BiltegiLocala.class);
    private static final String FITXATEGIA_IZENA = "store.dat";
    private static BiltegiLocala INSTANCE;

    private DatuakPoltsa poltsa;

    private BiltegiLocala() {
        poltsa = kargatu();
        gorde();
    }

    public static synchronized BiltegiLocala getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BiltegiLocala();
        }
        return INSTANCE;
    }

    // ─── Fitxategi kudeaketa ──────────────────────────────────────────────────
    private static Path getFitxategi() {
        String env = System.getenv("OFFLINE_DATA_PATH");
        Path dir = (env != null && !env.isEmpty())
                ? Paths.get(env)
                : Paths.get(System.getProperty("user.home"), ".erronka-bermeo");
        try {
            Files.createDirectories(dir);
        } catch (IOException e) {
            LOG.log(Level.WARNING, "Direktorioa ezin sortu: {0}", dir);
        }
        return dir.resolve(FITXATEGIA_IZENA);
    }

    private DatuakPoltsa kargatu() {
        Path f = getFitxategi();
        if (Files.exists(f)) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f.toFile()))) {
                LOG.log(Level.INFO, "Offline datuak kargatzen: {0}", f);
                return (DatuakPoltsa) ois.readObject();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "store.dat ezin kargatu, hasierako datuak sortzen", e);
            }
        }
        return hasieratuDatuak();
    }

    public synchronized void gorde() {
        Path f = getFitxategi();
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f.toFile()))) {
            oos.writeObject(poltsa);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Offline datuak ezin gorde: {0}", e.getMessage());
        }
    }

    /**
     * DB-tik datuak kargatu eta kaxa eguneratzen du.
     * ModoKudeatzailea.detektatu()-k deitzen du online dagoenean, kaxa beti
     * datu errealak eduki ditzan.
     */
    public synchronized void sincronizatuDBtik() {
        try {
            List<String[]> rolak = LangileaDAO.getRolak();
            if (!rolak.isEmpty()) {
                poltsa.rolak = new ArrayList<>(rolak);
            }

            List<Langilea> langileak = LangileaDAO.getGuztiak();
            if (!langileak.isEmpty()) {
                poltsa.langileak = new ArrayList<>(langileak);
                poltsa.langileNextId = langileak.stream()
                        .mapToInt(Langilea::getLangileId).max().orElse(0) + 1;
            }

            List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
            if (!kategoriak.isEmpty()) {
                poltsa.kategoriak = new ArrayList<>(kategoriak);
                poltsa.kategoriaNextId = kategoriak.stream()
                        .mapToInt(Kategoria::getKategoriaId).max().orElse(0) + 1;
            }

            List<Kokalekua> kokalekuak = KokalekuaDAO.getZerrenda();
            if (!kokalekuak.isEmpty()) {
                poltsa.kokalekuak = new ArrayList<>(kokalekuak);
                poltsa.kokalekuNextId = kokalekuak.stream()
                        .mapToInt(Kokalekua::getKokalekuId).max().orElse(0) + 1;
            }

            List<Artikulua> artikuluak = ArtikuluaDAO.getGuztiak();
            poltsa.artikuluak = new ArrayList<>(artikuluak);

            List<Erreklamazioa> erreklamazioak = ErreklamazioaDAO.getGuztiak();
            if (!erreklamazioak.isEmpty()) {
                poltsa.erreklamazioak = new ArrayList<>(erreklamazioak);
                poltsa.erreklamazioNextId = erreklamazioak.stream()
                        .mapToInt(Erreklamazioa::getErreklamazioId).max().orElse(0) + 1;
            }

            List<MugimenduLerroa> mugimenduak = MugimenduDAO.getGuztiak();
            poltsa.mugimenduak = new ArrayList<>(mugimenduak);

            gorde();
            LOG.log(Level.INFO, "BiltegiLocala DB-tik sinkronizatuta: {0} artikulu, {1} langile.",
                    new Object[]{artikuluak.size(), langileak.size()});
        } catch (Exception e) {
            LOG.log(Level.WARNING, "DB sinkronizazioa huts egin du: {0}", e.getMessage());
        }
    }

    private DatuakPoltsa hasieratuDatuak() {
        LOG.info("store.dat ez da aurkitu — poltsa hutsa sortzen da. DB-tik sinkronizatuko da.");
        return new DatuakPoltsa();
    }

    // ─── LANGILEA ─────────────────────────────────────────────────────────────
    public List<Langilea> getLangileak() {
        return new ArrayList<>(poltsa.langileak);
    }

    public List<String[]> getRolak() {
        return new ArrayList<>(poltsa.rolak);
    }

    public synchronized boolean langileaGehitu(String izena, String abizena,
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
        gorde();
        return true;
    }

    public Langilea login(String erabiltzailea, String pasahitza) {
        for (Langilea l : poltsa.langileak) {
            if (l.getErabiltzailea().equals(erabiltzailea)) {
                if (BCrypt.checkpw(pasahitza, l.getPasahitzaHash())) {
                    return l;
                }
                return null;
            }
        }
        return null;
    }

    public synchronized boolean langileaEguneratu(int id, String izena, String abizena,
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
                gorde();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean langileaEzabatu(int id) {
        boolean removed = poltsa.langileak.removeIf(l -> l.getLangileId() == id);
        if (removed) {
            gorde();
        }
        return removed;
    }

    private String getRolaDesk(int idRola) {
        for (String[] r : poltsa.rolak) {
            if (Integer.parseInt(r[0]) == idRola) {
                return r[1];
            }
        }
        return "Udaltzaina";
    }

    // ─── ARTIKULUA ────────────────────────────────────────────────────────────
    public List<Artikulua> getArtikuluak() {
        return new ArrayList<>(poltsa.artikuluak);
    }

    public Artikulua getArtikuluaByKodea(String kodea) {
        return poltsa.artikuluak.stream()
                .filter(a -> a.getArtikuluKodea().equals(kodea))
                .findFirst().orElse(null);
    }

    public synchronized String artikuluaGehitu(String izena, String deskribapena,
            boolean iragankorra, int idKategoria, int idKokalekua,
            java.sql.Date sarreraData, String argazkiBidea) {
        String kodea = sortuArtikuluKodea(sarreraData);
        Artikulua a = new Artikulua(kodea, izena, deskribapena, null, null, sarreraData, argazkiBidea);
        if (idKategoria > 0) {
            poltsa.kategoriak.stream().filter(k -> k.getKategoriaId() == idKategoria)
                    .findFirst().ifPresent(a::setKategoria);
        }
        if (idKokalekua > 0) {
            poltsa.kokalekuak.stream().filter(k -> k.getKokalekuId() == idKokalekua)
                    .findFirst().ifPresent(a::setKokalekua);
        }
        poltsa.artikuluak.add(a);
        String now = now();
        poltsa.mugimenduak.add(0, new MugimenduLerroa(now, "—",
                "Artikulua sisteman erregistratu da: " + kodea, kodea));
        gorde();
        return kodea;
    }

    public synchronized boolean artikuluaEguneratu(String kodea, String izena,
            String deskribapena, int idKategoria, int idKokalekua, String argazkiBidea) {
        for (Artikulua a : poltsa.artikuluak) {
            if (a.getArtikuluKodea().equals(kodea)) {
                a.setIzenburua(izena);
                a.setDeskribapena(deskribapena);
                a.setKategoria(idKategoria > 0
                        ? poltsa.kategoriak.stream().filter(k -> k.getKategoriaId() == idKategoria).findFirst().orElse(null)
                        : null);
                a.setKokalekua(idKokalekua > 0
                        ? poltsa.kokalekuak.stream().filter(k -> k.getKokalekuId() == idKokalekua).findFirst().orElse(null)
                        : null);
                if (argazkiBidea != null && !argazkiBidea.isEmpty()) {
                    a.setArgazkiBidea(argazkiBidea);
                }
                gorde();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean artikuluaEzabatu(String kodea) {
        boolean removed = poltsa.artikuluak.removeIf(a -> a.getArtikuluKodea().equals(kodea));
        if (removed) {
            gorde();
        }
        return removed;
    }

    private String sortuArtikuluKodea(java.sql.Date sarreraData) {
        String urteStr = String.format("%02d", Calendar.getInstance().get(Calendar.YEAR) % 100);
        if (sarreraData != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(sarreraData);
            urteStr = String.format("%02d", cal.get(Calendar.YEAR) % 100);
        }
        final String urte = urteStr;
        long count = poltsa.artikuluak.stream()
                .filter(a -> a.getArtikuluKodea().endsWith("-" + urte))
                .count();
        return String.format("G-%03d-%s", count + 1, urteStr);
    }

    // ─── KATEGORIA ────────────────────────────────────────────────────────────
    public List<Kategoria> getKategoriak() {
        return new ArrayList<>(poltsa.kategoriak);
    }

    public synchronized boolean kategoriaGehitu(String izena) {
        poltsa.kategoriak.add(new Kategoria(poltsa.kategoriaNextId++, izena));
        gorde();
        return true;
    }

    public synchronized boolean kategoriaAldatuIzena(int id, String izena) {
        for (Kategoria k : poltsa.kategoriak) {
            if (k.getKategoriaId() == id) {
                k.setIzena(izena);
                gorde();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean kategoriaEzabatu(int id) {
        boolean removed = poltsa.kategoriak.removeIf(k -> k.getKategoriaId() == id);
        if (removed) {
            gorde();
        }
        return removed;
    }

    // ─── KOKALEKUA ────────────────────────────────────────────────────────────
    public List<Kokalekua> getKokalekuak() {
        List<Kokalekua> result = new ArrayList<>();
        for (Kokalekua k : poltsa.kokalekuak) {
            int kop = (int) poltsa.artikuluak.stream()
                    .filter(a -> a.getKokalekua() != null && a.getKokalekua().getKokalekuId() == k.getKokalekuId())
                    .count();
            k.setArtikuluKopurua(kop);
            result.add(k);
        }
        return result;
    }

    public List<Kokalekua> getKokalekuakZerrenda() {
        return new ArrayList<>(poltsa.kokalekuak);
    }

    public synchronized boolean kokalekuaGehitu(String armairua, String apala, boolean bhaDa) {
        Kokalekua k = new Kokalekua(armairua, apala, bhaDa);
        k.setKokalekuId(poltsa.kokalekuNextId++);
        poltsa.kokalekuak.add(k);
        gorde();
        return true;
    }

    public synchronized boolean kokalekuaEguneratu(int id, String armairua, String apala, boolean bhaDa) {
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
                gorde();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean kokalekuaEzabatu(int id) {
        boolean removed = poltsa.kokalekuak.removeIf(k -> k.getKokalekuId() == id);
        if (removed) {
            gorde();
        }
        return removed;
    }

    // ─── ERREKLAMAZIOA ────────────────────────────────────────────────────────
    public List<Erreklamazioa> getErreklamazioak() {
        return new ArrayList<>(poltsa.erreklamazioak);
    }

    public synchronized boolean erreklamazioaUpdateEgoera(String id, String egoera) {
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
                gorde();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean erreklamazioaGorde(String nan, String izena, String abizena,
            String telefonoa, String emaila, int kategoriaId, String deskribapena, int idLangile) {
        Jabea jabea = lortuEdoSortuJabea(nan, izena, abizena, telefonoa, emaila);
        Erreklamazioa e = new Erreklamazioa(jabea, deskribapena, new Date());
        e.setErreklamazioId(poltsa.erreklamazioNextId++);
        if (kategoriaId > 0) {
            poltsa.kategoriak.stream().filter(k -> k.getKategoriaId() == kategoriaId)
                    .findFirst().ifPresent(e::setKategoria);
        }
        poltsa.erreklamazioak.add(e);
        gorde();
        return true;
    }

    // ─── EMANALDIA ────────────────────────────────────────────────────────────
    public synchronized boolean formalizatu(String idArtikulua, String nan, String izena,
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
        poltsa.mugimenduak.add(0, new MugimenduLerroa(now(), langileIzena,
                "Artikulua " + izena + " " + abizena + "-ri eman zaio.", idArtikulua));
        gorde();
        return true;
    }

    public synchronized boolean formalizatuErakundea(String idArtikulua, String ift,
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
        poltsa.mugimenduak.add(0, new MugimenduLerroa(now(), langileIzena,
                "Artikulua " + izenOfiziala + " erakundeari eman zaio.", idArtikulua));
        gorde();
        return true;
    }

    // ─── AURKITZAILEA ─────────────────────────────────────────────────────────
    public synchronized boolean aurkitzaileaGehitu(String idArtikulua, String izena,
            String abizena, String telefonoa, String emaila, String aurkipenLekua) {
        Aurkitzailea a = new Aurkitzailea(izena, abizena,
                telefonoa.isEmpty() ? null : telefonoa,
                emaila.isEmpty() ? null : emaila,
                aurkipenLekua.isEmpty() ? null : aurkipenLekua,
                idArtikulua);
        a.setAurkitzaileaId(poltsa.aurkitzaileNextId++);
        poltsa.aurkitzaileak.add(a);
        gorde();
        return true;
    }

    public Aurkitzailea getAurkitzaileaByArtikulua(String idArtikulua) {
        return poltsa.aurkitzaileak.stream()
                .filter(a -> idArtikulua.equals(a.getIdArtikulua()))
                .findFirst().orElse(null);
    }

    // ─── MUGIMENDUA ───────────────────────────────────────────────────────────
    public List<MugimenduLerroa> getMugimenduak() {
        return new ArrayList<>(poltsa.mugimenduak);
    }

    public List<AzkenMugimendua> getAzkenMugimenduak() {
        List<AzkenMugimendua> result = new ArrayList<>();
        int max = Math.min(10, poltsa.mugimenduak.size());
        for (int i = 0; i < max; i++) {
            MugimenduLerroa m = poltsa.mugimenduak.get(i);
            result.add(new AzkenMugimendua(m.getArtikuluId(), m.getEkintza(), m.getData(), m.getLangilea()));
        }
        return result;
    }

    // ─── ESTADISTIKAK ─────────────────────────────────────────────────────────
    public int biltegianKopurua() {
        return (int) poltsa.artikuluak.stream().filter(a -> a.getEgoera() == EgoeraArtikulua.BILTEGIAN).count();
    }

    public int bueltatakoKopurua() {
        return (int) poltsa.artikuluak.stream().filter(a -> a.getEgoera() == EgoeraArtikulua.ITZULITA).count();
    }

    public int iraungituakKopurua() {
        return (int) poltsa.artikuluak.stream().filter(a -> a.getEgoera() == EgoeraArtikulua.IRAUNGITA).count();
    }

    public int iraungitzearKopurua() {
        Date orain = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(orain);
        cal.add(Calendar.DAY_OF_YEAR, 30);
        Date hemendik30 = cal.getTime();
        return (int) poltsa.artikuluak.stream()
                .filter(a -> a.getEgoera() == EgoeraArtikulua.BILTEGIAN
                && a.getIraungitzeData() != null
                && !a.getIraungitzeData().before(orain)
                && !a.getIraungitzeData().after(hemendik30))
                .count();
    }

    public int erreklamazioIrekiakKopurua() {
        return (int) poltsa.erreklamazioak.stream()
                .filter(e -> e.getEgoera() == EgoeraErreklamazioa.IREKITA).count();
    }

    public int langileKopurua() {
        return poltsa.langileak.size();
    }

    public int artikuluGuztienKopurua() {
        return poltsa.artikuluak.size();
    }

    public int kategoriaKopurua() {
        return poltsa.kategoriak.size();
    }

    public int kokalekuakKopurua() {
        return poltsa.kokalekuak.size();
    }

    public List<KategoriaKopurua> kategoriaKopuruak() {
        List<KategoriaKopurua> result = new ArrayList<>();
        for (Kategoria k : poltsa.kategoriak) {
            long kop = poltsa.artikuluak.stream()
                    .filter(a -> a.getKategoria() != null && a.getKategoria().getKategoriaId() == k.getKategoriaId())
                    .count();
            result.add(new KategoriaKopurua(k.getIzena(), (int) kop));
        }
        result.sort((a, b) -> b.getKopurua() - a.getKopurua());
        return result;
    }

    // ─── Laguntzaileak ────────────────────────────────────────────────────────
    private Jabea lortuEdoSortuJabea(String nan, String izena, String abizena,
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

    private Erakundea lortuEdoSortuErakundea(String ift, String izenOfiziala,
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

    private String getLangileIzena(int idLangile) {
        if (idLangile <= 0) {
            return "—";
        }
        return poltsa.langileak.stream()
                .filter(l -> l.getLangileId() == idLangile)
                .map(l -> l.getIzena() + " " + l.getAbizena())
                .findFirst().orElse("—");
    }

    private static String now() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    // ─── Datu-poltsa (Serializable) ───────────────────────────────────────────
    private static class DatuakPoltsa implements Serializable {

        private static final long serialVersionUID = 1L;
        List<Langilea> langileak = new ArrayList<>();
        List<String[]> rolak = new ArrayList<>();
        List<Artikulua> artikuluak = new ArrayList<>();
        List<Kategoria> kategoriak = new ArrayList<>();
        List<Kokalekua> kokalekuak = new ArrayList<>();
        List<Jabea> jabeak = new ArrayList<>();
        List<Erakundea> erakundeak = new ArrayList<>();
        List<Erreklamazioa> erreklamazioak = new ArrayList<>();
        List<Aurkitzailea> aurkitzaileak = new ArrayList<>();
        List<MugimenduLerroa> mugimenduak = new ArrayList<>();
        int langileNextId = 1;
        int kategoriaNextId = 1;
        int kokalekuNextId = 1;
        int erreklamazioNextId = 1;
        int hartzaileNextId = 1;
        int aurkitzaileNextId = 1;
    }
}
