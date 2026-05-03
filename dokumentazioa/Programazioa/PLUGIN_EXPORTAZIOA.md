# Plugin eta Exportazio Sistema

> Nola gehitu exportazio-plugin berriak aplikazioan. CSV, PDF eta XML adibideak.

---

## 1. Arkitektura

Exportazio-pluginak `utils/` paketean daude. Bakoitzak metodo estatiko bat dauka:

```
utils/
├── XMLExportazioa.java   ← existitzen da
├── CSVExportazioa.java   ← SORTU
└── PDFExportazioa.java   ← SORTU (iThemis/OpenPDF beharko da)
```

Patroi estandarra:
1. DAO-tik datuak eskuratu
2. Fitxategia idatzi erabiltzailearen `home` direktorioan (edo `EXPORT_BIDEA` aldagaian)
3. `AdminPanelaController`-tik deitu

---

## 2. CSV Exportazioa

### 2.1 Sortu `CSVExportazioa.java`

```java
package utils;

import dao.ArtikuluaDAO;
import dao.ErreklamazioaDAO;
import model.Artikulua;
import model.Erreklamazioa;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Datuak CSV fitxategi gisa exportatzen dituen klase laguntzailea.
 */
public class CSVExportazioa {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Artikulu guztiak CSV fitxategi batean exportatzen ditu.
     * @return Sortutako fitxategiaren bide osoa
     * @throws IOException Idazketa errorea
     */
    public static String exportatuArtikuluak() throws IOException {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String fitxategia = System.getProperty("user.home") + "/galdutakoak_backups/artikuluak_" + data + ".csv";

        new java.io.File(System.getProperty("user.home") + "/galdutakoak_backups").mkdirs();

        List<Artikulua> zerrenda = ArtikuluaDAO.getGuztiak();

        try (FileWriter fw = new FileWriter(fitxategia)) {
            // Goiburua
            fw.write("Kodea,Izena,Deskribapena,Kategoria,Kokalekua,Egoera,Sarrera data\n");

            for (Artikulua a : zerrenda) {
                fw.write(
                    csvEsc(a.getArtikuluKodea()) + "," +
                    csvEsc(a.getIzenburua())     + "," +
                    csvEsc(a.getDeskribapenaSegurua()) + "," +
                    csvEsc(a.getKategoriaIzena()) + "," +
                    csvEsc(a.getKokalekuaIzena()) + "," +
                    csvEsc(a.getEgoeraTestua())   + "," +
                    (a.getSarreraData() != null ? SDF.format(a.getSarreraData()) : "") +
                    "\n"
                );
            }
        }
        return fitxategia;
    }

    /**
     * Erreklamazio guztiak CSV fitxategi batean exportatzen ditu.
     * @return Sortutako fitxategiaren bide osoa
     * @throws IOException Idazketa errorea
     */
    public static String exportatuErreklamazioak() throws IOException {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String fitxategia = System.getProperty("user.home") + "/galdutakoak_backups/erreklamazioak_" + data + ".csv";

        new java.io.File(System.getProperty("user.home") + "/galdutakoak_backups").mkdirs();

        List<Erreklamazioa> zerrenda = ErreklamazioaDAO.getGuztiak();

        try (FileWriter fw = new FileWriter(fitxategia)) {
            fw.write("ID,Data,Jabea,NAN,Telefonoa,Kategoria,Deskribapena,Egoera\n");

            for (Erreklamazioa e : zerrenda) {
                fw.write(
                    e.getErreklamazioId() + "," +
                    csvEsc(e.getDataFormatua())        + "," +
                    csvEsc(e.getJabeIzena() + " " + e.getJabeAbizena()) + "," +
                    csvEsc(e.getJabeNan())             + "," +
                    csvEsc(e.getJabeTelefonoa())       + "," +
                    csvEsc(e.getKategoriaIzena())      + "," +
                    csvEsc(e.getDeskribapena())         + "," +
                    csvEsc(e.getEgoeraTestua())        +
                    "\n"
                );
            }
        }
        return fitxategia;
    }

    /** CSV balioa bikoitz-komatxo artean jartzen du karaktere bereziak baditu. */
    private static String csvEsc(String s) {
        if (s == null) return "";
        if (s.contains(",") || s.contains("\"") || s.contains("\n")) {
            return "\"" + s.replace("\"", "\"\"") + "\"";
        }
        return s;
    }
}
```

### 2.2 AdminPanelaController-en integratu

```java
// AdminPanelaController.java
@FXML
public void exportatuCSV() {
    try {
        String fitx = CSVExportazioa.exportatuArtikuluak();
        lblAzkenKopia.setText("CSV: " + new java.io.File(fitx).getName());
        lblAzkenKopia.getStyleClass().removeAll("text-danger", "text-success");
        lblAzkenKopia.getStyleClass().add("text-success");
    } catch (Exception e) {
        lblAzkenKopia.setText("CSV errorea: " + e.getMessage());
        lblAzkenKopia.getStyleClass().add("text-danger");
    }
}
```

### 2.3 FXML-en botoia

```xml
<Button text="CSV Exportatu" onAction="#exportatuCSV" styleClass="btn-outline"/>
```

---

## 3. PDF Txostena (OpenPDF)

### 3.1 pom.xml-en dependentzia gehitu

```xml
<!-- pom.xml — dependencies barruan -->
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.30</version>
</dependency>
```

### 3.2 Sortu `PDFExportazioa.java`

```java
package utils;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import dao.ArtikuluaDAO;
import model.Artikulua;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Artikuluen PDF txostena sortzen du OpenPDF liburutegiarekin.
 */
public class PDFExportazioa {

    public static String exportatuArtikuluak() throws Exception {
        String data = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
        String fitxategia = System.getProperty("user.home") + "/galdutakoak_backups/artikuluak_" + data + ".pdf";

        new java.io.File(System.getProperty("user.home") + "/galdutakoak_backups").mkdirs();

        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(fitxategia));
        doc.open();

        // Izenburua
        Font tituluFontea = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph titulua = new Paragraph("Galdutakoak — Inbentarioa", tituluFontea);
        titulua.setAlignment(Element.ALIGN_CENTER);
        doc.add(titulua);
        doc.add(new Paragraph(" "));

        // Data
        doc.add(new Paragraph("Sortze-data: " +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
        doc.add(new Paragraph(" "));

        // Taula
        PdfPTable taula = new PdfPTable(5);
        taula.setWidthPercentage(100);
        taula.setWidths(new float[]{1.5f, 3f, 2f, 2f, 1.5f});

        // Goiburua
        Font goiburuFontea = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        for (String izena : new String[]{"Kodea", "Izena", "Kategoria", "Kokalekua", "Egoera"}) {
            PdfPCell zelda = new PdfPCell(new Phrase(izena, goiburuFontea));
            zelda.setBackgroundColor(new java.awt.Color(0x1D, 0x4E, 0xD8));
            zelda.setPadding(6);
            taula.addCell(zelda);
        }

        // Datuak
        Font datuen = FontFactory.getFont(FontFactory.HELVETICA, 9);
        List<Artikulua> zerrenda = ArtikuluaDAO.getGuztiak();
        for (Artikulua a : zerrenda) {
            taula.addCell(new Phrase(a.getArtikuluKodea(), datuen));
            taula.addCell(new Phrase(a.getIzenburua(), datuen));
            taula.addCell(new Phrase(a.getKategoriaIzena(), datuen));
            taula.addCell(new Phrase(a.getKokalekuaIzena(), datuen));
            taula.addCell(new Phrase(a.getEgoeraTestua(), datuen));
        }

        doc.add(taula);
        doc.close();
        return fitxategia;
    }
}
```

### 3.3 Kontroladorean deitu

```java
@FXML
public void exportatuPDF() {
    try {
        String fitx = PDFExportazioa.exportatuArtikuluak();
        lblAzkenKopia.setText("PDF: " + new java.io.File(fitx).getName());
        lblAzkenKopia.getStyleClass().add("text-success");
    } catch (Exception e) {
        lblAzkenKopia.setText("PDF errorea: " + e.getMessage());
        lblAzkenKopia.getStyleClass().add("text-danger");
    }
}
```

---

## 4. Fitxategi-hautatzailea (FileChooser) — non gorde aukeratzeko

Erabiltzaileak fitxategiaren kokalekua aukeratzeko:

```java
@FXML
public void exportatuCSVHautatu() {
    FileChooser fc = new FileChooser();
    fc.setTitle("Gorde CSV gisa");
    fc.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV fitxategia (*.csv)", "*.csv")
    );
    fc.setInitialFileName("artikuluak.csv");

    File fitxategia = fc.showSaveDialog(lblAzkenKopia.getScene().getWindow());
    if (fitxategia != null) {
        try {
            // Gorde aukeratutako bidean
            CSVExportazioa.exportatuArtikuluakBidean(fitxategia.getAbsolutePath());
            lblAzkenKopia.setText("Gordeta: " + fitxategia.getName());
        } catch (Exception e) {
            lblAzkenKopia.setText("Errorea: " + e.getMessage());
        }
    }
}
```

---

## 5. Laburpena: Plugin berri bat gehitzeko pausoak

1. **Sortu `utils/NireExportazioa.java`** — `exportatu()` metodo estatikoarekin
2. **Gehitu dependentzia `pom.xml`-en** (PDF bezalako kanpoko liburutegientzat)
3. **`AdminPanelaController`-n gehitu `@FXML` metodoa** plugina deituko duena
4. **`AdminPanela.fxml`-n gehitu botoia** `onAction="#nireMetodoa"` atributurekin
5. **Probatu** `mvn compile` eta exekutatu

| Plugin | Dependentzia | Formatu estandarra |
|--------|-------------|-------------------|
| XML | Sartu dago | `XMLExportazioa.java` |
| CSV | Ez behar | `CSVExportazioa.java` (sortu) |
| PDF | `openpdf:1.3.30` | `PDFExportazioa.java` (sortu) |
| Excel | `poi-ooxml:5.2.5` | `ExcelExportazioa.java` (sortu) |
