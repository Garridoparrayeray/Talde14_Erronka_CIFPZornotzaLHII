# JavaFX TableView — Gida Praktikoa

> Aplikazioaren kodean oinarritutako gida. Adibideak `InbentarioController`, `LangileakController` eta `AuditoriaController` fitxategietatik hartuta.

---

## 1. Oinarrizko egitura

TableView batek hiru pieza behar ditu:
- **TableView\<T\>** — zerrendaren ostalaria
- **TableColumn\<T, String\>** — zutabe bakoitza
- **CellValueFactory** — zutabe bakoitzak zer erakutsi

### FXML adibidea (Inbentario.fxml moduan)

```xml
<TableView fx:id="taula" VBox.vgrow="ALWAYS">
    <columns>
        <TableColumn fx:id="colKodea"       text="Kodea"       prefWidth="100"/>
        <TableColumn fx:id="colIzena"        text="Izena"       prefWidth="200"/>
        <TableColumn fx:id="colKategoria"    text="Kategoria"   prefWidth="140"/>
        <TableColumn fx:id="colEgoera"       text="Egoera"      prefWidth="100"/>
    </columns>
</TableView>
```

### Kontroladorean: @FXML deklarazioa

```java
@FXML private TableView<Artikulua>           taula;
@FXML private TableColumn<Artikulua, String> colKodea;
@FXML private TableColumn<Artikulua, String> colIzena;
@FXML private TableColumn<Artikulua, String> colKategoria;
@FXML private TableColumn<Artikulua, String> colEgoera;
```

---

## 2. CellValueFactory — zutabeak datuei lotu

`initialize()` metodoan konfiguratzen da. Ereduaren getter bat ematen zaio:

```java
@Override
public void initialize(URL url, ResourceBundle rb) {
    colKodea.setCellValueFactory(c ->
        new SimpleStringProperty(c.getValue().getArtikuluKodea()));

    colIzena.setCellValueFactory(c ->
        new SimpleStringProperty(c.getValue().getIzenburua()));

    colKategoria.setCellValueFactory(c ->
        new SimpleStringProperty(c.getValue().getKategoriaIzena()));

    colEgoera.setCellValueFactory(c ->
        new SimpleStringProperty(c.getValue().getEgoeraTestua()));

    kargatu();
}
```

### Balio konplexuak

Ereduak `null` itzul dezake. `getKategoriaIzena()` bezalako metodoak erabiltzea gomendatzen da, haietan null-a babestuta baitago:

```java
// Ongi — modelak babestu du null-a
colKategoria.setCellValueFactory(c ->
    new SimpleStringProperty(c.getValue().getKategoriaIzena())); // "—" itzultzen du null bada

// Txarki — NPE posiblea
colKategoria.setCellValueFactory(c ->
    new SimpleStringProperty(c.getValue().getKategoria().getIzena())); // crash null bada
```

---

## 3. Datuak kargatu eta freskatu

```java
private List<Artikulua> guztiak;

private void kargatu() {
    guztiak = ArtikuluaDAO.getGuztiak();   // DB-tik eskuratu
    taula.getItems().setAll(guztiak);       // taula bete
}

@FXML
private void freskatu() {
    kargatu();
}
```

---

## 4. Iragazketa (Filtroa)

Patroi estandarra aplikazioan: zerrenda osoa memorian gordetzen da (`guztiak`) eta iragazitako azpimultzoa taulan jartzen da.

```java
@FXML
private void bilatu() {
    if (guztiak == null) return;

    String testua  = txtBilaketa.getText().trim().toLowerCase();
    String katSel  = cbKategoria.getValue();
    String egSel   = cbEgoera.getValue();

    if (katSel == null) katSel = "Kategoria guztiak";
    if (egSel  == null) egSel  = "Egoera guztiak";

    final String kat = katSel;
    final String eg  = egSel;

    List<Artikulua> iragaziak = guztiak.stream()
        .filter(a -> testua.isEmpty()
                  || a.getIzenburua().toLowerCase().contains(testua)
                  || a.getArtikuluKodea().toLowerCase().contains(testua))
        .filter(a -> kat.equals("Kategoria guztiak") || a.getKategoriaIzena().equals(kat))
        .filter(a -> eg.equals("Egoera guztiak")     || a.getEgoeraTestua().equalsIgnoreCase(eg))
        .collect(java.util.stream.Collectors.toList());

    taula.getItems().setAll(iragaziak);
}
```

**Garrantzitsua:** ComboBox-eko listener bat gehitu daiteke filtroa automatikoki aplikatzeko:

```java
// initialize() barruan
cbKategoria.valueProperty().addListener((obs, old, nuevo) -> bilatu());
cbEgoera.valueProperty().addListener((obs, old, nuevo)    -> bilatu());
txtBilaketa.textProperty().addListener((obs, old, nuevo)  -> bilatu());
```

---

## 5. Hautaketa kudeatu (elementua klikatzean)

```java
taula.getSelectionModel().selectedItemProperty().addListener(
    (obs, aurrekoa, hautatua) -> {
        if (hautatua != null) {
            erakutsiXehetasunak(hautatua);
        }
    }
);
```

Edo FXML-tik `onMouseClicked` erabili:

```xml
<TableView fx:id="taula" onMouseClicked="#taulaKlikatu">
```

```java
@FXML
private void taulaKlikatu() {
    Artikulua sel = taula.getSelectionModel().getSelectedItem();
    if (sel != null) {
        erakutsiXehetasunak(sel);
    }
}
```

---

## 6. Zelda editagarriak (in-place editing)

Taulan zuzenean editatzeko, bi gauza behar dira: `setEditable(true)` eta `setCellFactory`.

```java
// initialize() barruan
taula.setEditable(true);

colIzena.setCellFactory(javafx.scene.control.cell.TextFieldTableCell.forTableColumn());
colIzena.setOnEditCommit(event -> {
    Artikulua a = event.getRowValue();
    String izenaBerrria = event.getNewValue().trim();
    if (!izenaBerrria.isEmpty()) {
        ArtikuluaDAO.eguneratu(a.getArtikuluKodea(), izenaBerrria);
        kargatu();
    }
});
```

**DAO metodo berria beharko da:**

```java
// ArtikuluaDAO.java
public static boolean eguneratu(String kodea, String izenaBerrria) {
    String sql = "UPDATE ARTIKULUA SET a_izena = ? WHERE id_artikulua = ?";
    try (Connection con = DBKonexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, izenaBerrria);
        ps.setString(2, kodea);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        System.err.println("ArtikuluaDAO.eguneratu: " + e.getMessage());
        return false;
    }
}
```

---

## 7. Zutabe pertsonalizatuak (botoi edo badge batekin)

Adibidea: egoera-badge koloreduna:

```java
colEgoera.setCellFactory(col -> new TableCell<Artikulua, String>() {
    @Override
    protected void updateItem(String egoera, boolean empty) {
        super.updateItem(egoera, empty);
        if (empty || egoera == null) {
            setGraphic(null);
            setText(null);
        } else {
            Label badge = new Label(egoera);
            switch (egoera) {
                case "Biltegian" -> badge.getStyleClass().add("badge-neutral");
                case "Itzulita"  -> badge.getStyleClass().add("badge-success");
                case "Iraungita" -> badge.getStyleClass().add("badge-warning");
                default          -> badge.getStyleClass().add("badge-neutral");
            }
            setGraphic(badge);
            setText(null);
        }
    }
});
```

Adibidea: "Ezabatu" botoia zutabe batean:

```java
TableColumn<Artikulua, Void> colEzabatu = new TableColumn<>("Ekintza");
colEzabatu.setCellFactory(col -> new TableCell<>() {
    private final Button btn = new Button("Ezabatu");
    {
        btn.getStyleClass().add("btn-danger");
        btn.setOnAction(e -> {
            Artikulua a = getTableView().getItems().get(getIndex());
            ezabatuArtikulua(a);
        });
    }
    @Override
    protected void updateItem(Void item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(empty ? null : btn);
    }
});
taula.getColumns().add(colEzabatu);
```

---

## 8. Ordenaketa (Sorting)

TableView-ek klikatzean ordenatzen du automatikoki. `setSortable(false)` erabiliz galaraz daiteke:

```java
colKodea.setSortable(true);    // lehenetsita true da
colEgoera.setSortable(false);  // ezgaitu
```

Kodez ordenatzeko:

```java
colKodea.setSortType(TableColumn.SortType.ASCENDING);
taula.getSortOrder().setAll(colKodea);
taula.sort();
```

---

## 9. Ohiko arazoak eta konponketak

| Arazoa | Kausa | Konponketa |
|--------|-------|-----------|
| Taula hutsik agertzen da | `kargatu()` ez da deitu `initialize()`-n | `initialize()` amaieran `kargatu()` gehitu |
| NPE filtroan | `ComboBox.getValue()` null itzultzen du | Null check gehitu (`if (val == null) val = "guztiak"`) |
| Taulatik elementua aldatu ondoren ez da eguneratzen | `kargatu()` ez da deitu aldaketa ondoren | DAO eragiketaren ondoren `kargatu()` dei |
| Zutabe zabalerak gaizki | FXML-en `prefWidth` falta | `prefWidth` edo `VBox.hgrow="ALWAYS"` gehitu |
| Badge edo botoia zutabean agertzen ez | `setCellValueFactory` + `setCellFactory` batera | `setCellFactory` erabiltzean `setCellValueFactory` kendu |
