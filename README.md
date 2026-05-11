# ERRONKA · Bermeoko Udala
## Galduen eta aurkituen kudeaketa-sistema

> Ekosistema osoa edukiontzietan: datu-basea, JavaFX aplikazioa eta web atari publikoa.  
> Komando bakar batekin abiarazten da: `git clone [url] && docker compose up -d`

---

## Aurkibidea

1. [Proiektuaren deskripzioa](#proiektuaren-deskripzioa)
2. [Arkitektura](#arkitektura)
3. [Aurretiko eskakizunak](#aurretiko-eskakizunak)
4. [Abiaraztea Linux-en](#abiaraztea-linux-en)
5. [Abiaraztea Windows-en](#abiaraztea-windows-en)
6. [JavaFX aplikazioa natiboa sortu](#javafx-aplikazioa-natiboa-sortu)
7. [Sarbideak eta kredentzialak](#sarbideak-eta-kredentzialak)
8. [Erabilera arrunta](#erabilera-arrunta)
9. [Datu-basea](#datu-basea)
10. [Karpeta-egitura](#karpeta-egitura)
11. [Errubrika-mapa](#errubrika-mapa)
12. [Arazoen ebazpena](#arazoen-ebazpena)
13. [Egileak](#egileak)

---

## Proiektuaren deskripzioa

**Erronka** Bermeoko Udalaren galdu eta aurkitutako objektuak kudeatzeko sistema integratua da. Bi sarbide ditu:

- **JavaFX aplikazio bat** langileentzat (back-office): objektuak erregistratu, kokatu, jabeari eman, erreklamazioak balioztatu, bat-etortzeak bilatu, iraungitakoak kudeatu…
- **Web atari publiko bat** herritarrentzat (front-office): aurkitutako objektuen katalogoa kontsultatu eta galera-erreklamazioak bidali.

Datu-base bat partekatzen dute (MariaDB). JavaFX-ek artikuluen XML-ak esportatzen ditu eta webguneak haiek kontsumitzen ditu.

### Funtzionalitate nagusiak — JavaFX

| Atalean | Zer egiten den |
|---------|----------------|
| **Erregistroa** | Artikulu berriak sartu: kategoria, kokalekua, argazkia, aurkitzailearen datuak, iraungikorra/ez |
| **Inbentarioa** | Artikulu guztiak ikusi + filtratu (izena, kategoria, egoera). Editatu, ezabatu, argazkia ikusi |
| **Emanaldia** | Artikulua jabeari edo erakundeari entregatu. Sinadura-dokumentua gorde |
| **Erreklamazioak** | Herritarren galera-erreklamazioak ikusi. Bat-etortze posibleak automatikoki erakutsi |
| **Galdu dabenak** | Erreklamazio berriak sortu. Filtratutako zerrenda ikusi |
| **Iraungitakoak** | 2 urteko epea igaro duten artikuluak. Aurkitzaileari edo erakundeari eskaini |
| **Auditoria** | Ekintza guztien historia. CSV-ra esportatu |
| **Admin panela** | Langileak, kategoriak, kokalekuak kudeatu. Datu-basearen backup egin |
| **Offline modua** | DB konexiorik gabe ere funtzionatzen du `store.dat` fitxategiarekin |

### Funtzionalitate nagusiak — Web

- Katalogo responsive-a (mugikorra, tableta, mahaigaina)
- XML fitxategiak bistaratu (XSD + DTD balioztatuta)
- XSLT eraldaketa (XML → XHTML)
- Erreklamazio-formularioa JavaScript balidazioarekin
- Modu iluna

---

## Arkitektura

```
┌─────────────────────────────────────────────────────────────┐
│                  ERRONKA EKOSISTEMA                         │
└─────────────────────────────────────────────────────────────┘

  ┌──────────────────┐         ┌──────────────────┐
  │   JavaFX App     │         │   Web Portala    │
  │  (Langileak)     │         │  (Herritarrak)   │
  │  Port: 6080(VNC) │         │   Port: 8000     │
  └────────┬─────────┘         └────────┬─────────┘
           │                            │
           │      ┌──────────────┐      │
           └─────►│   MariaDB    │◄─────┘
                  │  Port: 3306  │
                  └──────┬───────┘
                         │
                  ┌──────▼───────┐
                  │   Adminer    │
                  │  Port: 8081  │
                  └──────────────┘

  Fitxategi-trukea:
    ./partekatutako_datuak/artikuluak.xml  ← Java idatzi, Web irakurri
    ./artikulu_irudiak/                    ← Java + Web partekatzen dute
    ~/.erronka-bermeo/store.dat            ← Offline cache (bakoitzaren makinan)
```

| Zerbitzua | Irudia | Portua | Funtzioa |
|-----------|--------|--------|----------|
| `db` | mariadb:11 | 3306 | Datu-base nagusia |
| `java-app` | (eraikia) | 6080 (noVNC) | JavaFX back-office |
| `web` | nginx:alpine | 8000 | Web atari publikoa |
| `adminer` | adminer:latest | 8081 | BD-aren web UI |

### JavaFX MVC patroia

```
src/main/java/
├── app/          → Launcher (fat JAR sarrera), Main (JavaFX Application)
├── controller/   → FXML kontrolatzaileak (MVC: C)
├── model/        → Eredu-klaseak — Hartzailea (abstraktoa), Jabea, Erakundea,
│                   Artikulua, Langilea, Administratzailea, Erreklamazioa…
├── dao/          → Datu-basera sarbidea (CRUD)
├── utils/        → DBKonexioa, AppConfig, BiltegiLokala, LogKudeatzailea,
│                   XMLExportazioa, XMLInportazioa, InsertLogailea, UIKudeatzailea
└── view/         → FXML leihoak + style.css (MVC: V)
```

---

## Aurretiko eskakizunak

| Sistema | Eskakizuna |
|---------|------------|
| **Linux** | `docker` + `docker compose` |
| **Windows** | [Docker Desktop](https://www.docker.com/products/docker-desktop/) |

---

## Abiaraztea Linux-en

```bash
# Lehen aldiz — baimenak eman
chmod +x start-linux.sh stop-linux.sh

# Abiarazi (Docker behar da)
./start-linux.sh

# JavaFX aplikazioa nabigatzailean (noVNC)
# http://localhost:6080/vnc.html?autoconnect=1&resize=scale

# Web ataria
# http://localhost:8000

# Geldiarazi
./stop-linux.sh
```

---

## Abiaraztea Windows-en

1. **Docker Desktop** ireki eta prest egon arte itxaron
2. `start-windows.bat` exekutatu (bi klik)
3. Nabigatzailean ireki:
   - JavaFX: **http://localhost:6080/vnc.html?autoconnect=1&resize=scale**
   - Web: **http://localhost:8000**

> VcXsrv edo X server beharrik gabe — JavaFX nabigatzailean (noVNC) irekitzen da.

---

## JavaFX aplikazioa natiboa sortu

Docker gabe exekutatzeko, binario natiboa sortu daiteke.  
**DB konexioa:** `localhost:3306` behar da (Docker martxan egon behar da).

### Linux — binario natiboa (JVM bundled)

```bash
cd java-app
mvn package

# App-image sortu
JAVAFX=$(find ~/.m2/repository/org/openjfx -name "*linux*.jar" ! -name "*sources*" | tr '\n' ':' | sed 's/:$//')
jpackage \
  --input target \
  --main-jar galdutakoak-1.0-SNAPSHOT.jar \
  --main-class app.Launcher \
  --module-path "$JAVAFX" \
  --add-modules javafx.controls,javafx.fxml \
  --name galdutakoak \
  --type app-image \
  --dest target

# Exekutatu
target/galdutakoak/bin/galdutakoak
```

### Windows — .exe instalatzailea

Windows makinan Java 21 JDK eta Maven instalatuta egon behar dira.  
`build-windows.bat` fitxategia exekutatu (bi klik):

```
java-app/build-windows.bat
```

`target\Galdutakoak-1.0.exe` sortuko da — JVM barne darama, ez du Java instalatuta behar.

### JAR soilik (Java instalatuta behar du)

```bash
# Linux / Windows (Java 21 JDK behar du)
java -jar target/galdutakoak-1.0-SNAPSHOT.jar
```

---

## Sarbideak eta kredentzialak

| Zerbitzua | URL | Erabiltzailea | Pasahitza |
|-----------|-----|---------------|-----------|
| Web ataria | http://localhost:8000 | — | — |
| Adminer | http://localhost:8081 | `admin` | `admin123` |
| DB zuzena | localhost:3306 | `admin` | `admin123` |

**Adminer konfigurazioa:**
- Sistema: `MySQL`
- Zerbitzaria: `db`
- Datu-basea: `erronka_galduak`

### JavaFX aplikazioko erabiltzaileak

Hasierako pasahitza guztientzat: **`1234`**

| Izena | Erabiltzailea | Rola |
|-------|--------------|------|
| Miren Agirre | `admin` | Administratzailea |
| Jon Zabala | `langile1` | Langilea |
| Ander Txurru | `ikusle1` | Ikuslea (irakurketa soilik) |

---

## Erabilera arrunta

### Edukiontzien egoera
```bash
docker compose ps
```

### Logak jarraitu
```bash
docker compose logs -f          # Denak
docker compose logs -f java-app # JavaFX soilik
docker compose logs -f db       # DB soilik
```

### JavaFX log fitxategia (tokian)
```bash
# Linux
tail -f logs/app-0.log
grep "SEVERE\|WARNING" logs/app-0.log

# Windows
# proiektua_karpeta\logs\app-0.log
```

### INSERT log fitxategia
```bash
cat partekatutako_datuak/insert_log.txt
```

### Zerbitzu bat berreraiki (kodea aldatu ondoren)
```bash
docker compose up --build java-app
```

### Datu-basearen backup manuala
```bash
docker exec erronka_db mariadb-dump \
  -uadmin -padmin123 erronka_galduak \
  > backup_$(date +%Y%m%d).sql
```

### Backup bat kargatu
```bash
docker exec -i erronka_db mariadb \
  -uadmin -padmin123 erronka_galduak \
  < backup_20260421.sql
```

### Datu-basea hutsetik berrabiarazi
```bash
docker compose down -v   # KONTUZ: datuak ezabatzen dira
./start-linux.sh
```

---

## Datu-basea

### Hasieratze-fitxategiak

`db/init/` karpetako fitxategiak automatikoki exekutatzen dira edukiontzia **lehen aldiz** abiaraztean:

| Fitxategia | Edukia |
|------------|--------|
| `01-schema.sql` | Taula guztiak sortu |
| `02-roles.sql` | Rolak eta DB erabiltzaileak |
| `03-seed.sql` | Hasierako datuak |
| `04-trigger.sql` | UPDATE + DELETE triggerrak |
| `05-procedures.sql` | Gordetako prozedurak |

### Taula nagusiak

| Taula | Deskripzioa |
|-------|-------------|
| `ROLA` | Erabiltzaile-rolak |
| `LANGILEA` | Udaleko langileak |
| `KATEGORIA` | Objektuen sailkapena |
| `KOKALEKUA` | Biltegiko kokapenak (BHA barne) |
| `HARTZAILEA` | Jabea/erakundearen entitate nagusia |
| `JABEA` | Pertsona fisikoa (HARTZAILEA azpiklase) |
| `ERAKUNDEA` | Erakunde juridikoa (HARTZAILEA azpiklase) |
| `AURKITZAILEA` | Objektua aurkitu zuen pertsona |
| `ARTIKULUA` | Galdutako objektua |
| `ERREKLAMAZIOA` | Herritarren erreklamazioak |
| `EMANALDIA` | Objektuaren entrega jabearentzat |
| `MUGIMENDUA` | Audit trail (ekintza guztiak) |
| `JAKINARAZPENA` | Abisuak |

### DB rolak

| DB Rola | Eskumenak |
|---------|-----------|
| `admin_rola` | Guztia |
| `langile_rola` | CRUD osoa (LANGILEA/ROLA irakurketa soilik) |
| `ikusle_rola` | Artikuluak erregistratu soilik |

---

## Karpeta-egitura

```
erronka-bermeo/
│
├──  db/
│   └── init/
│       ├── 01-schema.sql          ← Taula guztiak
│       ├── 02-roles.sql           ← Rolak eta DB erabiltzaileak
│       ├── 03-seed.sql            ← Hasierako datuak
│       ├── 04-trigger.sql         ← Triggerrak (UPDATE + DELETE)
│       └── 05-procedures.sql      ← Gordetako prozedurak
│
├──  java-app/
│   ├── Dockerfile
│   ├── pom.xml
│   ├── build-windows.bat          ← Windows .exe sortzeko scripta
│   └── src/main/java/
│       ├── app/                   ← Launcher, Main
│       ├── controller/            ← MVC kontrolatzaileak (23 klase)
│       ├── dao/                   ← CRUD (ArtikuluaDAO, LangileaDAO…)
│       ├── model/                 ← Eredu-klaseak (Hartzailea abstraktoa)
│       ├── utils/                 ← Laguntzaileak (DB, XML, Log, Offline)
│       └── view/                  ← FXML + style.css (23 pantaila)
│
├──  frontend/
│   ├── index.html                 ← Orri nagusia (responsive, Bootstrap)
│   ├── css/style.css
│   ├── js/
│   ├── html/
│   ├── xml/                       ← XML + XSD + DTD
│   ├── xslt/                      ← XSLT eraldaketak
│   ├── xpath/                     ← XPath kontsultak
│   └── xquery/                    ← XQuery kontsultak
│
├──  dokumentazioa/
│   ├── GarapenIngurunea/          ← Klase, erabilera-kasu, sekuentzi diagramak
│   ├── DatuBaseak/                ← E-R eta diseinu logikoa
│   ├── Programazioa/              ← Mockup-a, eskuliburua
│   ├── MarkaLengoaia/             ← Web mockup-a, eskuliburua
│   ├── Digitalizazioa/            ← Dashboard, datuen bizi-zikloa
│   └── Jasangarritasuna/          ← Auditoretza txostena
│
├──  partekatutako_datuak/       ← artikuluak.xml + insert_log.txt
├──  artikulu_irudiak/           ← Objektuen argazkiak
├──  logs/                       ← JavaFX log fitxategiak
│
├── .env                           ← DB kredentzialak
├── docker-compose.yml
├── start-linux.sh / stop-linux.sh
└── start-windows.bat / stop-windows.bat
```

---

## Errubrika-mapa

| Modulua | Irizpidea | Kokalekua |
|---------|-----------|-----------|
| **GARAPEN_INGURUNEA** | GitHub + commit historia | Repo osoa |
| | ≥4 test unitario mota | `java-app/src/test/` |
| | Saio-logak + errore-logak | `java-app/src/main/java/utils/LogKudeatzailea.java` |
| | Properties fitxategia | `java-app/src/main/resources/application.properties` |
| | Javadoc (@author) | `java-app/src/main/java/**` |
| | Klase-diagrama | `dokumentazioa/GarapenIngurunea/` |
| | Erabilera-kasuen diagrama | `dokumentazioa/GarapenIngurunea/` |
| | Sekuentzi-diagrama | `dokumentazioa/GarapenIngurunea/` |
| **DATU_BASEAK** | Diseinu fisikoa (SQL) | `db/init/01-schema.sql` |
| | SELECT/INSERT/UPDATE/DELETE | `db/init/01-schema.sql` |
| | Triggerrak (DELETE + UPDATE) | `db/init/04-trigger.sql` |
| | Gordetako prozedura | `db/init/05-procedures.sql` |
| | Diseinu kontzeptuala | `dokumentazioa/DatuBaseak/diseinu_kontzeptuala/` |
| | Diseinu logikoa | `dokumentazioa/DatuBaseak/diseinu_logikoa/` |
| | 3 rol + erabiltzaileak | `db/init/02-roles.sql` |
| | Urruneko atzigarritasuna | `docker-compose.yml` |
| **PROGRAMAZIOA** | DB konexioa + CRUD (≥3 taula) | `java-app/src/main/java/dao/` |
| | MVC patroia | `controller/ + model/ + view/` |
| | Klase abstraktoa | `model/Hartzailea.java` |
| | Herentzia + override + metodo ez-toString | `model/Jabea.java`, `model/Erakundea.java` |
| | Salbuespen pertsonalizatua | `utils/XMLExportazioa.java` (XMLPatroiException) |
| | ArrayList + datu-egitura dinamikoak | `controller/KategoriakController.java` eta beste |
| | INSERT emaitzak fitxategira | `utils/InsertLogailea.java` → `insert_log.txt` |
| | Segurtasun-kopia (offline) | `utils/BiltegiLokala.java` |
| | Bi erabiltzaile-modu | `utils/Sesio.java` (admin / langile) |
| | XML kudeaketa + regex balidazioa | `utils/XMLExportazioa.java`, `utils/XMLInportazioa.java` |
| | MVC mezuak (OK/EZ OK) | `utils/UIKudeatzailea.java` (toast sistema) |
| | Mockup-a | `dokumentazioa/Programazioa/` |
| | Eskuliburua | `dokumentazioa/Programazioa/` |
| **MARKA_LENGOAIA** | HTML + CSS + JS responsive | `frontend/` |
| | Bootstrap osagaiak | `frontend/index.html` |
| | XML + XSD + DTD | `frontend/xml/` |
| | XSLT (XML → XHTML) | `frontend/xslt/` |
| | XPath kontsultak | `frontend/xpath/` |
| | XQuery kontsultak | `frontend/xquery/` |
| | Web mockup mugikorra | `dokumentazioa/MarkaLengoaia/` |
| | Eskuliburua | `dokumentazioa/MarkaLengoaia/` |
| **DIGITALIZAZIOA** | Dockerizazioa (≥3 edukiontzi) | `docker-compose.yml` |
| | Dashboard | `dokumentazioa/Digitalizazioa/` |
| | Datuen bizi-zikloaren analisia | `dokumentazioa/Digitalizazioa/` |
| | Teknologia proposamena | `dokumentazioa/Digitalizazioa/` |
| **JASANGARRITASUNA** | Ekodiseinu web | `frontend/` |
| | Auditoretza txostena | `dokumentazioa/Jasangarritasuna/` |
| | Kodearen mantentze-erraztasuna | `dokumentazioa/Jasangarritasuna/` |

---

## Arazoen ebazpena

### `port is already allocated`
```bash
# Linux — zer darabil portua
sudo lsof -i :3306
# Windows
netstat -ano | findstr :3306
```
Geldiarazi tokiko MariaDB-a edo aldatu portua `docker-compose.yml`-an (`"3307:3306"`).

### Datu-basea ez da abiarazten
```bash
docker compose logs db
# `db_data` volume-an datu zaharrak badaude:
docker compose down -v && ./start-linux.sh
```

### JavaFX ezin da konektatu DB-ra
```bash
docker exec erronka_desktop env | grep DB_URL
docker exec erronka_desktop ping -c 2 db
```

### Linux: `cannot open display`
```bash
echo $DISPLAY   # :0 edo :1 ikusi beharko zenuke
xhost +local:docker
```

### Windows: JavaFX ez da agertzen noVNC-n
1. Itxaron 20 segundo edukiontziak abiarazi ondoren
2. URL zuzena: `http://localhost:6080/vnc.html?autoconnect=1&resize=scale`
3. `docker compose restart java-app`

### Offline modua aktibatu denean
DB konexiorik ez badago, aplikazioa automatikoki offline moduan abiarazten da `~/.erronka-bermeo/store.dat` fitxategiarekin. Backup egiteko funtzioa ez dago erabilgarri offline moduan.

---

## Egileak

**Erronka taldea** · 1. DAW · 2026

- Yeray Garrido Parrayera
- Eder Martin

**Bezeroa:** Bermeoko Udala · CIFP Zornotza LHII

---

*Hezkuntza-proiektua © 2026 · CIFP Zornotza LHII*
