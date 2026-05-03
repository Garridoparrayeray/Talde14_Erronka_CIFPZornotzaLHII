# Erronka — Falta dena eta nola osatu

> Kodea aztertu ondoren identifikatutako funtzio falta edo erdizka egindakoak.

---

## 1. Funtzio kritikoak (bukatugabeak)

### 1.1 Artikulua editatu
**Non falta:** Ez dago artikuluak editatzeko inprimakiena ez kontroladorerik.  
**Non gehitu:** `InbentarioController.java` → botoi berria + elkarrizketa-koadro berria.

**Pausoak:**
1. Sortu `ArtikuluaEditu.fxml` (`ArtikuluaBerria.fxml`-ren kopia aldatuta)
2. Sortu `ArtikuluaEdituController.java`
3. `ArtikuluaDAO`-n gehitu `eguneratu()` metodoa:
```java
public static boolean eguneratu(String kodea, String izena, String deskribapena,
                                 int idKat, int idKok) {
    String sql = "UPDATE ARTIKULUA SET a_izena=?, a_deskribapena=?, " +
                 "id_kategoria=?, id_kokalekua=? WHERE id_artikulua=?";
    try (Connection con = DBConexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, izena);
        ps.setString(2, deskribapena);
        if (idKat > 0) ps.setInt(3, idKat); else ps.setNull(3, java.sql.Types.INTEGER);
        if (idKok > 0) ps.setInt(4, idKok); else ps.setNull(4, java.sql.Types.INTEGER);
        ps.setString(5, kodea);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("ArtikuluaDAO.eguneratu: " + e.getMessage());
        return false;
    }
}
```
4. `InbentarioController`-n gehitu "Editatu" botoia taulan edo goialdean:
```java
@FXML
public void artikuluaEditatu() {
    Artikulua sel = taula.getSelectionModel().getSelectedItem();
    if (sel == null) return;
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ArtikuluaEditu.fxml"));
        Parent root = loader.load();
        ArtikuluaEdituController ctrl = loader.getController();
        ctrl.setArtikulua(sel);
        ctrl.setOnGorde(this::kargatu);
        Stage dialog = new Stage();
        dialog.initOwner(taula.getScene().getWindow());
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Artikulua editatu");
        dialog.setScene(new Scene(root, 480, 520));
        dialog.setResizable(false);
        dialog.showAndWait();
    } catch (Exception e) {
        System.err.println("InbentarioController.artikuluaEditatu: " + e.getMessage());
    }
}
```

---

### 1.2 Artikulua ezabatu
**Non falta:** `InbentarioController`-k ez dauka ezabatze funtziorik.

**DAO metodoa:**
```java
// ArtikuluaDAO.java
public static boolean ezabatu(String kodea) {
    String sql = "DELETE FROM ARTIKULUA WHERE id_artikulua = ?";
    try (Connection con = DBConexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, kodea);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("ArtikuluaDAO.ezabatu: " + e.getMessage());
        return false;
    }
}
```

**Kontroladorean (baieztapenarekin):**
```java
@FXML
public void artikuluaEzabatu() {
    Artikulua sel = taula.getSelectionModel().getSelectedItem();
    if (sel == null) return;

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Artikulua ezabatu");
    confirm.setHeaderText(null);
    confirm.setContentText("'" + sel.getIzenburua() + "' ezabatuko da. Ziur zaude?");
    confirm.initOwner(taula.getScene().getWindow());
    confirm.showAndWait().ifPresent(btn -> {
        if (btn == ButtonType.OK) {
            ArtikuluaDAO.ezabatu(sel.getArtikuluKodea());
            kargatu();
        }
    });
}
```

---

### 1.3 Langilea editatu eta ezabatu
**Non falta:** `LangileakController`-k bakarrik gehitzen du, ez editatu/ezabatu.

**DAO metodoak:**
```java
// LangileaDAO.java
public static boolean eguneratu(int id, String izena, String abizena,
                                  String erabiltzailea, int idRola) {
    String sql = "UPDATE LANGILEA SET izena=?, abizena=?, erabiltzailea=?, id_rola=? " +
                 "WHERE id_langile=?";
    try (Connection con = DBConexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, izena);
        ps.setString(2, abizena);
        ps.setString(3, erabiltzailea);
        ps.setInt(4, idRola);
        ps.setInt(5, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("LangileaDAO.eguneratu: " + e.getMessage());
        return false;
    }
}

public static boolean ezabatu(int id) {
    String sql = "DELETE FROM LANGILEA WHERE id_langile = ?";
    try (Connection con = DBConexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("LangileaDAO.ezabatu: " + e.getMessage());
        return false;
    }
}
```

---

### 1.4 Kokalekua editatu
**Non falta:** `KokalekuakController`-k gehitzen du bakarrik. Editatzeko `TextInputDialog` erabil daiteke (`KategoriakController`-ren antzera):

```java
// KokalekuakController.java
private void editatuKokalekua(Kokalekua k) {
    TextInputDialog dlg = new TextInputDialog(k.getArmairua());
    dlg.setTitle("Kokalekua editatu");
    dlg.setContentText("Armairua:");
    dlg.initOwner(taula.getScene().getWindow());
    dlg.showAndWait().ifPresent(val -> {
        if (!val.trim().isEmpty()) {
            KokalekuaDAO.eguneratu(k.getKokalekuId(), val.trim(), k.getApala(), k.isBhaDa());
            kargatu();
        }
    });
}
```

**DAO metodoa beharko da:**
```java
// KokalekuaDAO.java
public static boolean eguneratu(int id, String armairua, String apala, boolean bhaDa) {
    String sql = "UPDATE KOKALEKUA SET armairua=?, apala=?, bha_da=? WHERE id_kokalekua=?";
    try (Connection con = DBConexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, armairua);
        ps.setString(2, apala);
        ps.setBoolean(3, bhaDa);
        ps.setInt(4, id);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("KokalekuaDAO.eguneratu: " + e.getMessage());
        return false;
    }
}
```

---

## 2. Stub metodoak (beteta egon behar luketenak)

Hauek `model/` paketean daude eta ezer egiten ez duten gorputzekin:

| Fitxategia | Metodoa | Azalpena |
|-----------|---------|---------|
| `Langilea.java:41` | `erregistratuArtikulua()` | Beti `false` itzultzen du |
| `Langilea.java:50` | `erregistratuErreklamazioa()` | Beti `false` itzultzen du |
| `Langilea.java:59` | `kudeatuEmanaldia()` | Beti `false` itzultzen du |
| `Langilea.java:67` | `getTrazabilitatea()` | `null` itzultzen du |
| `Jakinarazpena.java:39` | `bidali()` | Gorputz hutsa |

> Oharra: metodo hauek ez dira kontroladoreetan erabiltzen, beraz ez dute eraginkortasunik orain. Etorkizuneko arkitekturarako utzitakoak dira.

---

## 3. XMLExportazioa UI-an integratu

`utils/XMLExportazioa.java` existitzen da baina ez da inon deitzen. `AdminPanelaController`-n gehitu daiteke:

**FXML-en botoi berri bat:**
```xml
<Button text="XML Exportatu" onAction="#exportatuXML" styleClass="btn-outline"/>
```

**Kontroladorean:**
```java
// AdminPanelaController.java
@FXML
public void exportatuXML() {
    try {
        utils.XMLExportazioa.exportatu();
        lblAzkenKopia.setText("XML exportatua: " +
            java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        lblAzkenKopia.getStyleClass().removeAll("text-danger", "text-success");
        lblAzkenKopia.getStyleClass().add("text-success");
    } catch (Exception e) {
        lblAzkenKopia.setText("XML errorea: " + e.getMessage());
        lblAzkenKopia.getStyleClass().add("text-danger");
    }
}
```

---

## 4. Inbentarioko data-iragazkia

`GalduDabenakController`-k data-iragazkia dauka baztertutakoentzat bakarrik. `InbentarioController`-n ere gehitu daiteke:

```java
// FXML-en
// <DatePicker fx:id="dpHasiera"/> <DatePicker fx:id="dpAmaiera"/>

// Iragaztean
.filter(a -> {
    if (a.getSarreraData() == null) return true;
    LocalDate data = new java.sql.Date(a.getSarreraData().getTime()).toLocalDate();
    LocalDate has = dpHasiera.getValue();
    LocalDate ama = dpAmaiera.getValue();
    if (has != null && data.isBefore(has)) return false;
    if (ama != null && data.isAfter(ama))  return false;
    return true;
})
```

---

## 5. Auditoria zutabearen mapaketa

`AuditoriaController.java:32`-n:
```java
colXehetasunak.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getArtikuluId()));
```
"Xehetasunak" zutabeak artikuluaren IDa erakusten du. Deskribapena erakustea ere komenigarria litzateke (edo biak). Egungo mapaketa:

| FXML Zutabea | Metodoa | Egokia? |
|-------------|---------|---------|
| `colData` | `getData()` | ✓ |
| `colLangilea` | `getLangilea()` | ✓ |
| `colEkintza` | `getEkintza()` | ✓ |
| `colXehetasunak` | `getArtikuluId()` | Aldatu `getDeskribapena()` + `getArtikuluId()` konbinaziora |

---

## 6. Laburpen taula: zer falta / zer dago

| Funtzioa | Egoera | Lehentasuna |
|----------|--------|-------------|
| Artikulua editatu | ❌ Falta | Alta |
| Artikulua ezabatu | ❌ Falta | Alta |
| Langilea editatu | ❌ Falta | Alta |
| Langilea ezabatu | ❌ Falta | Media |
| Kokalekua editatu | ❌ Falta | Media |
| XML exportazioa UI-an | ❌ Ez lotuta | Media |
| Data-iragazkia inbentarioan | ❌ Falta | Baja |
| Aplikazioa offlinen erabiltzea | ❌ Falta | Baja |

| Artikulua gehitu | ✅ Eginda | — |
| Artikulua erregistratu (pantaila osoa) | ✅ Eginda | — |
| Erreklamazioa sortu | ✅ Eginda | — |
| Erreklamazioa ebatzi/baztertu | ✅ Eginda | — |
| Emanaldia formalizatu | ✅ Eginda | — |
| Kategoria gehitu/editatu/ezabatu | ✅ Eginda | — |
| Kokalekua gehitu | ✅ Eginda | — |
| Langilea gehitu | ✅ Eginda | — |
| Auditoria ikusgai | ✅ Eginda | — |
| Babes-kopia SQL | ✅ Eginda | — |
| Login + rol kontrola | ✅ Eginda | — |
| Admin panela | ✅ Eginda | — |
