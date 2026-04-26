
# ERRONKA · Bermeoko Udala
## Galduen eta aurkituen kudeaketa-sistema

> Ekosistema osoa edukiontzietan: datu-basea, JavaFX aplikazioa eta web atari publikoa.
> Komando bakar batekin abiarazten da.

---

## 📋 Aurkibidea

1. [Proiektuaren deskripzioa](#-proiektuaren-deskripzioa)
2. [Arkitektura](#-arkitektura)
3. [Aurretiko eskakizunak](#-aurretiko-eskakizunak)
4. [Abiaraztea Linux-en](#-abiaraztea-linux-en)
5. [Abiaraztea Windows-en](#-abiaraztea-windows-en)
6. [Sarbideak eta kredentzialak](#-sarbideak-eta-kredentzialak)
7. [Erabilera arrunta](#-erabilera-arrunta)
8. [Datu-basea](#%EF%B8%8F-datu-basea)
9. [Karpeta-egitura](#-karpeta-egitura)
10. [Arazoen ebazpena](#-arazoen-ebazpena)
11. [Aurkezpena](#-aurkezpena)
12. [Egileak](#-egileak)

---

## 📖 Proiektuaren deskripzioa

**Erronka** Bermeoko Udalaren galdu eta aurkitutako objektuak kudeatzeko sistema bat
da. Bi sarbide ditu:

- **JavaFX aplikazio bat** Langileentzat (back-office) — objektuak
  erregistratu, kokatu, jabeari eman, erreklamazioak baliozta tu, etab.
- **Web atari publiko bat** herritarrentzat (front-office) — aurkitutako objektuen
  katalogoa kontsultatu eta galera-erreklamazioak bidali.

Datu-base bat partekatzen dute (MariaDB), eta XML fitxategien bidez ere komunikatzen
dira: JavaFX-ek artikuluen XML-ak esportatzen ditu eta webguneak haiek kontsumitzen
ditu.

### Funtzionalitate nagusiak

- Galdutako objektuen CRUD osoa (sortu, ikusi, editatu, ezabatu)
- Kokalekuak gehi BHA (bolumen handikoak) kudeatu
- Jabe eta erakundeen (udala, bankua, Izenpe, Osakidetza...) kudeaketa
- Emanaldien sinadurak eta dokumentuak gorde
- XML inportazio/esportazioa, XSD eta DTD bidez balioztatuta
- Web atari responsive-a (mugikorra, tableta, mahaigaina)
- Modu iluna (jasangarritasuna)
- Datu-baseko rolak eta segurtasuna
- Audit trail (mugimenduen historia)
- Trigger eta stored procedure-ak

---

## 🏛 Arkitektura

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
                  │   Adminer    │================================
                  |              | LOCALHOST ATARIA. Pisu txikikoa
                  │  Port: 8081  │================================
                  └──────────────┘

  Fitxategi-trukea:  ./partekatutako_datuak/*.xml
                     (Java idatzi → Web irakurri)
```

### Edukiontziak

| Zerbitzua | Irudia | Portua | Funtzioa |
|-----------|--------|--------|----------|
| `db` | mariadb:11 | 3306 | Datu-base nagusia |
| `java-app` | (eraikia) | — | JavaFX back-office |
| `web` | nginx:alpine | 8000 | Web atari publikoa |
| `adminer` | adminer:latest | 8081 | BD-aren web UI |

---

## 🔧 Aurretiko eskakizunak

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

## 🐧 Abiaraztea Linux-en

### Behin egin behar dena (lehen aldiz)

```bash
chmod +x start-linux.sh stop-linux.sh
```

### Abiarazi (klik bakarra)

```bash
./start-linux.sh
```

Script-ak hau egiten du automatikoki:

1. `xhost +local:docker` exekutatzen du (X11 baimena Docker-i)
2. Edukiontzi guztiak eraikitzen eta abiarazten ditu (`docker compose up --build -d`)
3. Egoera erakusten du

### Geldiarazi

```bash
./stop-linux.sh
```

Datuak gordeta gelditzen dira `db_data` volume-an. Dena garbitzeko (BD ezabatuz):

```bash
docker compose down -v
```

---

## Abiaraztea Windows-en

### Behin egin behar dena (lehen aldiz)

#### 1. VcXsrv konfiguratu

1. Ireki **XLaunch** (Hasiera menutik)
2. Aukerak:
   - **Multiple windows** → Hurrengoa
   - **Start no client** → Hurrengoa
   - **Disable access control** ← *ezinbestekoa*
   - Hurrengoa → Bukatu
3. Konfigurazioa gorde dezakezu fitxategi batean (`config.xlaunch`) berriz erabiltzeko

#### 2. Docker Desktop ireki

Itxaron prest egon arte (sistemako tray-ko ikonoak berdez egon behar du).

### Abiarazi (klik bakarra)

1. Ziurtatu VcXsrv martxan dagoela (XLaunch erabilita)
2. Ziurtatu Docker Desktop martxan dagoela
3. **Bi klik** `start-windows.bat`-en

Script-ak hau egiaztatzen du:

1. Docker Desktop martxan dagoen
2. VcXsrv martxan dagoen
3. Edukiontzi guztiak eraikitzen eta abiarazten ditu
4. Egoera erakusten du

### Geldiarazi

**Bi klik** `stop-windows.bat`-en.

---

## Sarbideak eta kredentzialak

### Web atari publikoa (herritarrak)
- 🌐 http://localhost:8000

### Adminer (datu-basearen UI)
- 🌐 http://localhost:8081
- **Sistema:** MySQL
- **Server:** `db`
- **Erabiltzailea:** `bermeo_udaltzain`
- **Pasahitza:** `udaltzainpw`
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

## 🛠 Erabilera arrunta

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

`db/init/` karpetan dauden `.sql` fitxategi guztiak automatikoki exekutatzen dira
edukiontzia LEHEN aldiz abiaraztean (alfabetiko ordenan):

- `01-schema.sql` — taulen egitura
- `02-roles.sql` — rolak eta erabiltzaileak
- `03-seed.sql` — adibidezko datuak

> **Garrantzitsua:** Schema aldatu ondoren, datu-basea berrabiarazi behar da
> hutsetik. `docker compose down -v` exekutatu eta gero `./start-linux.sh`.

---

## 📁 Karpeta-egitura

```
erronka/
├── docker-compose.yml             ← Linux orkestrazioa
├── docker-compose.windows.yml     ← Windows orkestrazioa
├── start-linux.sh                 ← Linux abiarazlea
├── stop-linux.sh                  ← Linux geldiarazlea
├── start-windows.bat              ← Windows abiarazlea
├── stop-windows.bat               ← Windows geldiarazlea
│
├── db/
│   └── init/                      ← BD hasieratze-script-ak
│       ├── 01-schema.sql
│       ├── 02-roles.sql
│       └── 03-seed.sql
│
├── java-app/                      ← JavaFX proiektua
│   ├── Dockerfile
│   ├── pom.xml
│   └── src/main/java/
│       ├── app/
│       ├──----- Main.java              
│       ├──----- Launcher.java     
│       ├── controller/            ← FXML kontrolatzaileak
│       ├── DAO/                   ← Datu-basera sarbidea
│       ├── model/                 ← Datu-ereduak
│       ├── utils/                 ← Db.java + laguntzaileak
│       └── view/                  ← FXML eta style.css
│
├── frontend/                      ← Web atariaren kodea
│   ├── index.html
│   ├── css/
│   ├── js/
│   ├── data/                      ← i18n JSON-ak
│   ├── xml/                       ← XML adibideak + DTD/XSD
│   ├── xslt/                      ← XSLT eraldaketak
│   ├── xpath/                     ← XPath kontsultak
│   └── xquery/                    ← XQuery kontsultak
│
├── partekatutako_datuak/          ← XML-ak (Java → Web)
│   ├── artikuluak.xml
│   ├── kategoriak.xml
│   └── estatistikak.xml
│
├── artikulu_irudiak/              ← Objektuen argazkiak
│
└── docs/                          ← Dokumentazioa
    ├── memoria.pdf
    ├── eredu_kontzeptuala.pdf
    ├── eredu_logikoa.pdf
    └── erabiltzaile_eskuliburua.pdf
```

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

Geldiarazi tokiko MariaDB/MySQL-a, edo aldatu portua compose-an
(adibidez `"3307:3306"` jarri).

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
Egiaztatu DB_URL ondo dagoela:
```bash
docker exec erronka_desktop env | grep DB_URL
# Hau atera beharko luke:
# DB_URL=jdbc:mariadb://db:3306/erronka_galduak
```

Eta `db` ostatuaren erresoluzioa:
```bash
docker exec erronka_desktop ping -c 2 db
```

---

## 🎤 Aurkezpena

Aurkezpen bizian erakusteko bidea:

1. **Linux makinatik aurkeztu** (irakaskuntzakoa edo norberarena):
   ```bash
   ./start-linux.sh
   ```
2. Bizpahiru segundotan:
   - JavaFX leihoa irekiko da
   - Webgunea `http://localhost:8000`-ean atzigarri
   - Adminer `http://localhost:8081`-ean atzigarri
3. Aurkeztu bakoitza:
   - **JavaFX**: erregistro bat sortu, kategoria iragazi, emanaldi bat egin
   - **Web**: katalogoa erakutsi, erreklamazio-formularioa bete (XML sortzen du)
   - **Adminer**: datu-baseko taula bat zabaldu, JOIN bat egin
4. Aurkezpen-bukaeran:
   ```bash
   ./stop-linux.sh
   ```

---

## 📚 Dokumentazio gehigarria

- 📄 `docs/memoria.pdf` — proiektuaren memoria osoa
- 🎨 `docs/mockup_program.pdf` — JavaFX mockup-a (Miro estiloan)
- 🎨 `docs/mockup_lmar.pdf` — Web mockup-a
- 🗄 `docs/eredu_kontzeptuala.pdf` — E-R diagrama
- 🗄 `docs/eredu_logikoa.pdf` — eskema erlazionala
- 📘 `docs/erabiltzaile_eskuliburua.pdf` — triptiko formatuan

---

## 👥 Egileak

**Erronka taldea** · 1. DAW · 2026
- [Izen-abizenak]
- [Izen-abizenak]
- [Izen-abizenak]

**Bezero hipotetikoa:** Bermeoko Udala (LEFPS Zornotza GLHBI)

---

## 📜 Lizentzia

Hezkuntza-proiektua. Kode-zatiak hezkuntza erabilerarako bakarrik.
© 2026 Erronka taldea — LEFPS Zornotza GLHBI
```

---

## ⚠ Tres cosas que tienes que ajustar a mano

1. **Egileak (línea ~440)**: pon vuestros nombres reales
2. **Karpeta-egitura (línea ~340)**: ajusta `eus/bermeo/udala/...` a tu paquete real
3. **Adibidezko kredentzialak (login)**: si quieres listar usuario/contraseña de prueba para el JavaFX, añádelo en la sección "Sarbideak"