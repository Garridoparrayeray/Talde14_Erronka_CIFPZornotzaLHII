
# ERRONKA · Bermeoko Udala
## Galduen eta aurkituen kudeaketa-sistema

> Ekosistema osoa edukiontzietan: datu-basea, JavaFX aplikazioa eta web atari publikoa.
> Komando bakar batekin abiarazten da: `git clone [url] && docker compose up`

---

## Aurkibidea

1. [Proiektuaren deskripzioa](#proiektuaren-deskripzioa)
2. [Arkitektura](#arkitektura)
3. [Karpeta-egitura eta Errubrika](#karpeta-egitura-eta-errubrika)
4. [Abiaraztea](#abiaraztea)
5. [Sarbideak eta kredentzialak](#sarbideak-eta-kredentzialak)
6. [Egileak](#egileak)

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
│   │   └── SekuentziaDiagrama.sqd  ← Sekuentzi-diagrama
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

## Abiaraztea

### Linux

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

> Hasierako datu-baseko erabiltzaileak `db/init/03-seed.sql` fitxategian daude.

### Rolak

| Rola | Eskumenak | Erabiltzailea |
|------|-----------|---------------|
| `rol_admin` | Guztia | `bermeo_admin` |
| `rol_udaltzain` | CRUD osoa | `bermeo_udaltzain` |
| `rol_bezeroa` | Irakurtze + erreklamazioa | `bermeo_bezeroa` |

---

## Egileak

**Erronka taldea** · 1. DAW · 2026

- Yeray Garrido
- Eder Martin

**Bezeroa:** Bermeoko Udala · CIFP Zornotza LHII

---

*Hezkuntza-proiektua. © 2026 Erronka taldea — CIFP Zornotza LHII*
