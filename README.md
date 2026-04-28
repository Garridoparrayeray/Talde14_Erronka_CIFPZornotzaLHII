
# ERRONKA · Bermeoko Udala
## Galduen eta aurkituen kudeaketa-sistema

> Ekosistema osoa edukiontzietan: datu-basea, JavaFX aplikazioa eta web atari publikoa.
> Komando bakar batekin abiarazten da: `git clone [url] && docker compose up`

---

## Aurkibidea

1. [Proiektuaren deskripzioa](#proiektuaren-deskripzioa)
2. [Arkitektura](#arkitektura)
3. [Aurretiko eskakizunak](#aurretiko-eskakizunak)
4. [Abiaraztea Linux-en](#abiaraztea-linux-en)
5. [Abiaraztea Windows-en](#abiaraztea-windows-en)
6. [Sarbideak eta kredentzialak](#sarbideak-eta-kredentzialak)
7. [Erabilera arrunta](#erabilera-arrunta)
8. [Datu-basea](#datu-basea)
9. [Karpeta-egitura eta Errubrika](#karpeta-egitura-eta-errubrika)
10. [Arazoen ebazpena](#arazoen-ebazpena)
11. [Egileak](#egileak)

---

## Proiektuaren deskripzioa

**Erronka** Bermeoko Udalaren galdu eta aurkitutako objektuak kudeatzeko sistema bat da. Bi sarbide ditu:

- **JavaFX aplikazio bat** Langileentzat (back-office) — objektuak erregistratu, kokatu, jabeari eman, erreklamazioak balioztatu, etab.
- **Web atari publiko bat** herritarrentzat (front-office) — aurkitutako objektuen katalogoa kontsultatu eta galera-erreklamazioak bidali.

Datu-base bat partekatzen dute (MariaDB), eta XML fitxategien bidez ere komunikatzen dira: JavaFX-ek artikuluen XML-ak esportatzen ditu eta webguneak haiek kontsumitzen ditu.

### Funtzionalitate nagusiak

- Galdutako objektuen CRUD osoa (sortu, ikusi, editatu, ezabatu)
- Kokalekuak gehi BHA (bolumen handikoak) kudeatu
- Jabe eta erakundeen kudeaketa
- Emanaldien sinadurak eta dokumentuak gorde
- XML inportazio/esportazioa, XSD eta DTD bidez balioztatuta
- Web atari responsive-a (mugikorra, tableta, mahaigaina)
- Modu iluna (jasangarritasuna)
- Datu-baseko rolak eta segurtasuna
- Audit trail (mugimenduen historia)
- Trigger eta stored procedure-ak

---

## Arkitektura

```
┌─────────────────────────────────────────────────────────────┐
│                  ERRONKA EKOSISTEMA                         │
└─────────────────────────────────────────────────────────────┘

  ┌──────────────────┐         ┌──────────────────┐
  │   JavaFX App     │         │   Web Portala    │
  │  (Udaltzaingoa)  │         │  (Herritarrak)   │
  │   Port: GUI      │         │   Port: 8000     │
  └────────┬─────────┘         └────────┬─────────┘
           │                            │
           │      ┌──────────────┐      │
           └─────▶│   MariaDB    │◀─────┘
                  │  Port: 3306  │
                  └──────┬───────┘
                         │
                  ┌──────▼───────┐
                  │   Adminer    │
                  │  Port: 8081  │
                  └──────────────┘

  Fitxategi-trukea:  ./partekatutako_datuak/*.xml
                     (Java idatzi → Web irakurri)
```

| Zerbitzua | Irudia | Portua | Funtzioa |
|-----------|--------|--------|----------|
| `db` | mariadb:11 | 3306 | Datu-base nagusia |
| `java-app` | (eraikia) | — | JavaFX back-office |
| `web` | nginx:alpine | 8000 | Web atari publikoa |
| `adminer` | adminer:latest | 8081 | BD-aren web UI |

---

## Aurretiko eskakizunak

### Linux (CachyOS, Arch, Ubuntu, Fedora, etab.)

- **Docker** eta **Docker Compose**:
  ```bash
  # CachyOS / Arch
  sudo pacman -S docker docker-compose

  # Ubuntu / Debian
  sudo apt install docker.io docker-compose-plugin
  ```
- **xhost** (X11 baimenetarako):
  ```bash
  sudo pacman -S xorg-xhost     # Arch / CachyOS
  sudo apt install x11-xserver-utils   # Ubuntu
  ```
- Erabiltzailea `docker` taldean egon behar da:
  ```bash
  sudo usermod -aG docker $USER
  # Ondoren saioa itxi eta ireki berriro
  ```

### Windows 11

- **Docker Desktop** instalatuta eta martxan
  → https://www.docker.com/products/docker-desktop/
- **VcXsrv** (X server JavaFX-erako)
  → https://sourceforge.net/projects/vcxsrv/

---

## Abiaraztea Linux-en

### Behin egin behar dena (lehen aldiz)

```bash
# Lehen aldiz
chmod +x start-linux.sh stop-linux.sh

# Abiarazi
./start-linux.sh

# Geldiarazi
./stop-linux.sh
```

### Windows

1. **Docker Desktop** ireki eta itxaron prest egon arte
2. **VcXsrv (XLaunch)** ireki → *Disable access control* aktibatu
3. `start-windows.bat` exekutatu (bi klik)

### Aurretiko eskakizunak

**Linux:** `docker`, `docker-compose`, `xorg-xhost`
**Windows:** [Docker Desktop](https://www.docker.com/products/docker-desktop/) + [VcXsrv](https://sourceforge.net/projects/vcxsrv/)

---

## Sarbideak eta kredentzialak

| Zerbitzua | URL | Erabiltzailea | Pasahitza |
|-----------|-----|---------------|-----------|
| Web ataria | http://localhost:8000 | — | — |
| Adminer (BD UI) | http://localhost:8081 | `bermeo_udaltzain` | `udaltzainpw` |
| BD zuzena | localhost:3306 | `bermeo_udaltzain` | `udaltzainpw` |

**Adminer konfigurazioa:**
- **Sistema:** MySQL
- **Server:** `db`
- **Datu-basea:** `erronka_galduak`

### JavaFX aplikazioa (back-office)
- Leiho gisa agertuko da (X11/VcXsrv bidez)
- Hasierako kredentzialak `db/init/03-seed.sql` fitxategian daude

### Datu-basea zuzenean (terminala)
```bash
# Edukiontzi barrutik
docker exec -it erronka_db mariadb -ubermeo_udaltzain -pudaltzainpw erronka_galduak

# Tokiko makinatik (mariadb-client beharrezkoa)
mariadb -h 127.0.0.1 -P 3306 -ubermeo_udaltzain -pudaltzainpw erronka_galduak
```

---

## Erabilera arrunta

### Edukiontzien egoera
```bash
docker compose ps
```

### Logak ikusi (denak)
```bash
docker compose logs -f
```

### Logak ikusi (zerbitzu bat)
```bash
docker compose logs -f java-app
docker compose logs -f db
docker compose logs -f web
```

### Zerbitzu bat berreraiki (kodea aldatu ondoren)
```bash
docker compose up --build java-app
```

### Datu-basearen segurtasun-kopia (backup)
```bash
docker exec erronka_db mariadb-dump \
  -ubermeo_udaltzain -pudaltzainpw erronka_galduak \
  > kopia_$(date +%Y%m%d).sql
```

### Backup-a kargatu
```bash
docker exec -i erronka_db mariadb \
  -ubermeo_udaltzain -pudaltzainpw erronka_galduak \
  < kopia_20260421.sql
```

### Edukiontzi baten barrura sartu
```bash
docker exec -it erronka_db bash
docker exec -it erronka_desktop bash
```

---

## Datu-basea

### Taula nagusiak

| Taula | Deskripzioa |
|-------|-------------|
| `rola` | Erabiltzaile-rolak (admin, langilea, bezeroa) |
| `langilea` | Udaltzaingoko langileak |
| `kategoria` | Objektuen sailkapena |
| `kokalekua` | Biltegiko kokapenak (A-001 ... G-006 + BHA) |
| `hartzailea` | Jabea/erakundearen super-entitatea |
| `jabea` | Pertsona fisikoa (espezializazioa) |
| `erakundea` | Erakunde juridikoa (espezializazioa) |
| `artikulua` | Galdutako objektua |
| `erreklamazioa` | Herritarren erreklamazioak |
| `emanaldia` | Objektuaren entrega |
| `mugimendua` | Audit trail |
| `jakinarazpena` | Abisuak jabeei |

### Rolak (segurtasuna)

| Rola | Eskumenak | Erabiltzailea |
|------|-----------|---------------|
| `rol_admin` | Guztia | `bermeo_admin` |
| `rol_udaltzain` | CRUD osoa | `bermeo_udaltzain` |
| `rol_bezeroa` | Irakurtze + erreklamazioa sartu | `bermeo_bezeroa` |

### Hasieratze-fitxategiak

`db/init/` karpetan dauden `.sql` fitxategi guztiak automatikoki exekutatzen dira edukiontzia LEHEN aldiz abiaraztean (alfabetiko ordenan):

- `01-schema.sql` — taulen egitura
- `02-roles.sql` — rolak eta erabiltzaileak
- `03-seed.sql` — adibidezko datuak

> **Garrantzitsua:** Schema aldatu ondoren, datu-basea berrabiarazi behar da hutsetik. `docker compose down -v` exekutatu eta gero `./start-linux.sh`.

---

## Karpeta-egitura eta Errubrika

```
erronka-bermeo/
│
├── 📁 db/                          ← DATU_BASEAK: Script-ak
│   └── init/
│       ├── 01-schema.sql           ← Diseinu fisikoa + Triggerrak + Prozedurak
│       ├── 02-roles.sql            ← Rolak eta erabiltzaileak (segurtasuna)
│       └── 03-seed.sql             ← Datu-lagin adierazgarriak
│
├── 📁 java-app/                    ← PROGRAMAZIOA: JavaFX aplikazioa
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/
│       ├── app/                    ← Launcher, Main
│       ├── controller/             ← MVC: kontrolatzaileak
│       ├── DAO/                    ← Datu-basera sarbidea (CRUD)
│       ├── model/                  ← MVC: eredu-klaseak (herentzia, abstraktoa)
│       ├── utils/                  ← DB konexioa, XML, log laguntzaileak
│       └── view/                   ← MVC: FXML leihoak + style.css
│
├── 📁 frontend/                    ← MARKA_LENGOAIA: Web ataria
│   ├── index.html                  ← Orri nagusia (responsive, Bootstrap)
│   ├── css/                        ← Estilo-orriak
│   ├── js/                         ← JavaScript funtzioak (balidazioa)
│   ├── html/                       ← Orri osagarriak
│   ├── datuak/                     ← Java-tik jasotako XML-ak
│   ├── xml/                        ← XML fitxategiak + XSD + DTD
│   ├── xslt/                       ← XSLT eraldaketak (XML → XHTML)
│   ├── xpath/                      ← XPath kontsultak (web scraping)
│   └── xquery/                     ← XQuery kontsultak
│
├── 📁 dokumentazioa/               ← Dokumentazio guztia moduluz modul
│   ├── GarapenIngurunea/           ← GARAPEN_INGURUNEA: Diagramak
│   │   ├── KlaseDiagrama.cld       ← Klase-diagrama
│   │   ├── UseCaseDiagrama.ucd     ← Erabilera-kasuen diagrama
│   │   ├── SekuentziaDiagrama.sqd  ← Sekuentzi-diagrama
│   │   └── Mock-Up-ak.pdf          ← Mockup-ak
│   ├── DatuBaseak/                 ← DATU_BASEAK: Diseinu dokumentazioa
│   │   ├── diseinu_kontzeptuala/   ← E-R diagramak (banakakoak + taldekoa)
│   │   └── diseinu_logikoa/        ← Eskema erlazionala (banakakoak + taldekoa)
│   ├── Programazioa/               ← Mockup-a, eskuliburua
│   ├── MarkaLengoaia/              ← Web mockup-a, eskuliburua, Bootstrap zerrenda
│   ├── Digitalizazioa/             ← Dashboard, datuen bizi-zikloa
│   └── Jasangarritasuna/           ← Auditoretza eta jasangarritasun txostena
│
├── 📁 Eranskinak/                  ← Taldeko dokumentuak
│   ├── ERANSKIN1_TaldearenKontratoa.pdf
│   ├── ERANSKIN2_Parametroak.pdf
│   ├── ERANSKIN3_Proposamena.docx
│   └── ERANSKIN4_PlanifikazioaEtaKontrolPuntuak.docx
│
├── 📁 partekatutako_datuak/        ← XML fitxategiak (Java → Web)
├── 📁 artikulu_irudiak/            ← Objektuen argazkiak
│
├── docker-compose.yml              ← DIGITALIZAZIOA: Linux orkestrazioa
├── docker-compose.windows.yml      ← Windows orkestrazioa
├── start-linux.sh / stop-linux.sh
└── start-windows.bat / stop-windows.bat
```

### Errubrika-mapa

| Modulua | Errubrika-irizpidea | Kokalekua |
|---------|---------------------|-----------|
| **GARAPEN_INGURUNEA** | GitHub biltegia + commit historia | Repo osoa |
| | Test unitarioak (≥4 mota) | `java-app/src/test/` |
| | Log fitxategiak (saio + errore) | `java-app/src/main/java/utils/` |
| | Javadoc | `java-app/src/main/java/**` |
| | Klase-diagrama | `dokumentazioa/GarapenIngurunea/KlaseDiagrama.cld` |
| | Erabilera-kasuen diagrama | `dokumentazioa/GarapenIngurunea/UseCaseDiagrama.ucd` |
| | Sekuentzi-diagrama | `dokumentazioa/GarapenIngurunea/SekuentziaDiagrama.sqd` |
| **DATU_BASEAK** | Diseinu fisikoa (SQL script-a) | `db/init/01-schema.sql` |
| | SELECT / INSERT / UPDATE / DELETE | `db/init/01-schema.sql` |
| | Trigger-ak (DELETE + UPDATE) | `db/init/01-schema.sql` |
| | Prozedura gordea | `db/init/01-schema.sql` |
| | Diseinu kontzeptuala (banakakoa + taldekoa) | `dokumentazioa/DatuBaseak/diseinu_kontzeptuala/` |
| | Diseinu logikoa (banakakoa + taldekoa) | `dokumentazioa/DatuBaseak/diseinu_logikoa/` |
| | Rolak eta erabiltzaileak | `db/init/02-roles.sql` |
| | Urruneko atzigarritasuna (Docker) | `docker-compose.yml` |
| **PROGRAMAZIOA** | CRUD + DB konexioa | `java-app/src/main/java/DAO/` |
| | MVC patroia | `java-app/src/main/java/{controller,model,view}/` |
| | Herentzia + klase abstraktoa | `java-app/src/main/java/model/` |
| | Salbuespenak (ohikoa + pertsonalizatua) | `java-app/src/main/java/` |
| | ArrayList + datu-egitura dinamikoak | `java-app/src/main/java/` |
| | XML fitxategien kudeaketa | `java-app/src/main/java/utils/` |
| | Fitxategi bitarrak (backup) | `java-app/src/main/java/utils/` |
| | Swing/JavaFX leihoak | `java-app/src/main/java/view/` |
| | Aplikazioaren mockup-a | `dokumentazioa/Programazioa/` |
| | Erabiltzailearen eskuliburua | `dokumentazioa/Programazioa/` |
| **MARKA_LENGOAIA** | HTML + CSS + JS (responsive) | `frontend/` |
| | Bootstrap osagaiak | `frontend/index.html` |
| | XML fitxategiak + XSD + DTD | `frontend/xml/` |
| | XSLT eraldaketa (XML → XHTML) | `frontend/xslt/` |
| | XPath kontsultak (web scraping) | `frontend/xpath/` |
| | XQuery kontsultak | `frontend/xquery/` |
| | Web mockup-a (mugikorra) | `dokumentazioa/MarkaLengoaia/` |
| | Erabiltzailearen eskuliburua | `dokumentazioa/MarkaLengoaia/` |
| **DIGITALIZAZIOA** | Dockerizazioa (3 edukiontzi) | `docker-compose.yml` |
| | Dashboard | `dokumentazioa/Digitalizazioa/` |
| | Datuen bizi-zikloaren analisia | `dokumentazioa/Digitalizazioa/` |
| | Teknologia proposamena | `dokumentazioa/Digitalizazioa/` |
| **JASANGARRITASUNA** | Ekodiseinu-estrategiak (web) | `frontend/` |
| | Jasangarritasun-auditoretza | `dokumentazioa/Jasangarritasuna/` |
| | Kodearen mantentze-erraztasuna | `dokumentazioa/Jasangarritasuna/` |

---

## Arazoen ebazpena

### Linux: `xhost: command not found`
```bash
sudo pacman -S xorg-xhost   # Arch / CachyOS
sudo apt install x11-xserver-utils   # Ubuntu
```

### Linux: `cannot open display`
1. Egiaztatu `$DISPLAY` aldagaia ezarrita dagoen:
   ```bash
   echo $DISPLAY     # `:0` edo `:1` agertu beharko luke
   ```
2. Wayland erabiltzen baduzu, XWayland behar duzu:
   ```bash
   sudo pacman -S xorg-xwayland
   ```
3. `xhost +local:docker` exekutatu saio grafikoaren barruan (ez SSH bidez).

### Windows: JavaFX leihoa ez agertu
1. Egiaztatu **VcXsrv** martxan dagoela (sistemako tray-an X-aren ikonoa).
2. Egiaztatu **Disable access control** aktibatuta egon zela XLaunch konfiguratzean.
3. Suebakia (firewall) `vcxsrv.exe`-ri konexioak baimentzen ari zaion.
4. Edukiontzia berrabiarazi:
   ```bat
   docker compose -f docker-compose.windows.yml restart java-app
   ```

### `port is already allocated`
Beste prozesu batek portua erabiltzen du. Egiaztatu zer:
```bash
# Linux
sudo lsof -i :3306
# Windows
netstat -ano | findstr :3306
```

Geldiarazi tokiko MariaDB/MySQL-a, edo aldatu portua compose-an (`"3307:3306"` jarri).

### Datu-basea ez da abiarazten
```bash
docker compose logs db
```

Errore ohikoenak:
- **`db_data` volume-an datu zaharrak** → `docker compose down -v` (kontuz, datuak galtzen dira)
- **Sintaxi-errorea SQL fitxategi batean** → log-ek lerroa adieraziko dute

### Datu-basea hutsetik berrabiarazi (datuak galduz)
```bash
docker compose down -v
./start-linux.sh
```

### JavaFX ezin da konektatu BD-ra
```bash
docker exec erronka_desktop env | grep DB_URL
# Hau atera beharko luke:
# DB_URL=jdbc:mariadb://db:3306/erronka_galduak

docker exec erronka_desktop ping -c 2 db
```

---

## Egileak

**Erronka taldea** · 1. DAW · 2026

- Yeray Garrido
- Eder Martin

**Bezeroa:** Bermeoko Udala · CIFP Zornotza LHII

---

*Hezkuntza-proiektua. © 2026 Erronka taldea — CIFP Zornotza LHII*
