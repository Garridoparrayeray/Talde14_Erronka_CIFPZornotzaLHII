# ERRONKA: Bermeoko Udalaren Web Atari Publikoa

## 1. Sarrera
Dokumentu honek **ERRONKA Bermeoko Udala** proiektuaren **Frontend (Web Ataria)** azaltzen du. Webgune hau herritarrei zuzenduta dago, eta bi helburu nagusi ditu: galdutako objektuen katalogoa kontsultatzea eta objektu bat berreskuratzeko erreklamazioak modu digitalean egitea.

Aplikazioa **Single Page Application (SPA)** baten antzera portatzen den arren, teknologia estandar eta arinetan garatu da (HTML5, CSS3, Vanilla JavaScript), framework astunik (React edo Angular) erabili gabe. Datuen kudeaketa **XML ekosistema** baten bidez egiten da.

---

## 2. Fitxategien Egitura eta Arkitektura
Webgunearen fitxategiak modu logikoan antolatuta daude mantentze-lanak errazteko:

*   **`/index.html`**: Hasierako orria. Bertan aurkitzen da informazio nagusia (Hero), nola funtzionatzen duen azaltzen duen prozesua, kategoria nagusiak eta erreklamazioa egiteko inprimakia.
*   **`/html/objektu-zerrenda.html`**: Katalogo osoaren orria. Bertan artikulu guztiak bistaratzen dira, bilatzaile eta iragazkiekin.
*   **`/css/style.css`**: Webguneko estilo guztiak biltzen dituen fitxategia. CSS aldagaiak (`:root`) erabili dira koloreak eta diseinu-sistema kudeatzeko.
*   **`/js/main.js`**: Hasierako orriaren logika (Nazioartekotzea, form-aren balidazioa eta XML sorrera, karrusela...).
*   **`/js/objektu-zerrenda.js`**: Katalogoaren orriko logika (XML irakurketa, iragazkiak, bilatzailea).
*   **`/datuak/artikuluak.xml`**: Webguneak kontsumitzen duen datu-base estatikoa. Back-office (Java) aplikazioak sortzen du.

---

## 3. Funtzionalitate Nagusiak (JavaScript Logika)

### 3.1. Nazioartekotzea (Elebitasuna)
Webgunea euskaraz eta gaztelaniaz dago eskuragarri.
*   Hizkuntza-hiztegiak JS fitxategietan definitu dira objektu gisa (`dictIndex` eta `dictCatalog`).
*   HTML elementuek `data-i18n` atributua dute gakoekin (adib. `<span data-i18n="nav_home">Hasiera</span>`).
*   Erabiltzaileak hizkuntza aldatzen duenean, `setLang()` funtzioak testu guztiak dinamikoki eguneratzen ditu eta aukeraketa `localStorage`-n gordetzen du hurrengo bisitetarako.

### 3.2. Modu Iluna (Dark Mode)
Erabiltzailearen esperientzia hobetzeko, Modu Argia eta Modu Iluna integratu dira.
*   `initTheme()` funtzioak kudeatzen du. Goiko menuko ilargi/eguzki botoia sakatzean, `<body>` etiketari `.dark-theme` klasea gehitzen zaio.
*   Klase horrek CSS aldagaiak berridazten ditu (adibidez, atzealde zuria beltz bihurtuz).
*   Aukeraketa `localStorage`-n gordetzen da (`appTheme`).

### 3.3. Datuen Karga Dinamikoa (DOMParser)
Webguneak ez du Backend API (JSON) batera deirik egiten. Horren ordez, **XML fitxategia zuzenean irakurtzen du**:
1.  `fetch('/datuak/artikuluak.xml')` bidez datuak eskuratzen ditu.
2.  `DOMParser()` erabiliz XML-a egitura nabigagarri bihurtzen du.
3.  XML nodoak (`<izena>`, `<deskribapena>`, `<argazkia>`) irakurri eta HTML elementu (Txartelak) bihurtzen ditu dinamikoki DOMean txertatuz.

### 3.4. Bilatzailea eta Iragazkiak
Katalogoaren orrian bilaketa aurreratua inplementatu da:
*   **Iragazki konbinatuak:** Erabiltzaileak testu bidez bilatu dezake eta, aldi berean, kategoria baten arabera iragazi.
*   Kategoriak dinamikoki sortzen dira XML fitxategia irakurtzerakoan aurkitutako kategoria ezberdinetatik (`Set` bat erabiliz bikoiztuak ekiditeko).
*   Erendimendua hobetzeko, elementuak orrikatuta bezala agertzen dira (hasieran 6 elementu), "Gehiago ikusi" botoiarekin gehiago bistaratzeko.

### 3.5. Erreklamazioen Inprimakia eta XML Sorrera
Erreklamazio inprimakiak garrantzi handia du:
1.  **Balidazioak:** JavaScript bidez (eta Adierazpen Erregularrak erabiliz), NAN formatua (8 zenbaki + letra 1) eta Telefonoa (9 zenbaki) ondo idatzita daudela ziurtatzen da.
2.  **Datuen Bilketa:** Informazio guztia `FormData` bidez jasotzen da. Karaktere bereziak (`<`, `>`, `&`) ihes egiten dira (escaped) injezkioak ekiditeko.
3.  **XML Deskarga:** Backend batera bidali beharrean, Javascript-ek **XML fitxategi bat sortzen du memorian** DTD eta XSD erreferentziekin, eta bezeroaren nabigatzailean deskarga behartzen du (`erreklamazioa_Izena.xml`). 

---

## 4. Diseinua eta CSS Estiloak

### 4.1. Diseinu-Sistema (CSS Aldagaiak)
Estilo-orri nagusiak (`style.css`) aldagaiekin (`:root`) definitzen du webgunearen itxura. Horrek koherentzia bermatzen du:
*   **Koloreak:** Udalaren marka islatzeko urdin gamak (`--blue-600` pisu handienarekin) eta kolore neutralak (`--gray-900` testuetarako).
*   **Tipografia:** Letra-tipo modernoak eta irakurgarriak erabili dira: `Inter` gorputzeko testuetarako eta `Montserrat` izenburuetarako. Letra-tipo hauek `fonts/` karpetan daude gordeta (Woff2 formatuan) karga azkarra bermatzeko.

### 4.2. Egokitzapena (Responsive Design)
Webgunea erabat moldagarria da edozein gailutarako:
*   `@media` kontsultak (Media queries) erabili dira pantaila tamaina ezberdinetan (900px, 640px, 600px, 540px) diseinua aldatzeko.
*   Mahaigaineko menu horizontala, pantaila txikietan ezkutatu eta **Mugikorreko Menu (Hanburgesa)** bihurtzen da.
*   Elementuen antolaketa `CSS Grid` eta `Flexbox` bidez eginda dagoenez, zutabe anitzeko saretak zutabe bakarreko bihurtzen dira mugikorrean.

### 4.3. Bootstrap Osagaiak
Proiektua nagusiki CSS propioarekin egina egon arren, **Bootstrap 5** liburutegia (CSS eta JS) gehitu da osagai zehatz eta konplexu batzuk errazteko:
*   **Modal-ak:** Pribatutasun-politika bistaratzeko.
*   **Toast-ak:** Erreklamazioa arrakastaz egin denean, pantailaren beheko aldean jakinarazpen berde bat (Toast) erakusteko.

---

## 5. Datuen Egitura (XML Ekosistema)
Webgune hau guztiz bateragarria da XML estandarrekin:

*   **Balidazioa:** Webguneak XSD (`artikuluak.xsd`) eta DTD (`artikuluak.dtd`) fitxategiak ditu, sortzen diren erreklamazioak eta irakurtzen diren artikuluak formatu zehatz bat (adibidez, data formatua) jarraitzen dutela bermatzeko.
*   **XSLT Eraldaketak:** Datuak zuzenean formatu gordinean ikusi nahi badira, `xslt/artikuluak.xsl` fitxategiak XML dokumentua zuzenean **HTML taula egituratu eta estilizatu** batean eraldatzen du nabigatzailean bertan (JS-ren beharrik gabe).
*   **XPath/XQuery:** Bermeoko udalaren datu-baseko elementu espezifikoak aurkitzeko kontsulta estandarizatuak prestatu dira proiektuaren barruan.