# ERRONKA-BERMEO — Documentación Técnica Completa

> Sistema de gestión de objetos perdidos para el Ayuntamiento de Bermeo (Bermeoko Udala).

---

## Índice

1. [Visión General y Tecnologías](#1-visión-general-y-tecnologías)
2. [Arquitectura del Proyecto](#2-arquitectura-del-proyecto)
3. [Base de Datos (MariaDB)](#3-base-de-datos-mariadb)
4. [Aplicación Java (Back-office)](#4-aplicación-java-back-office)
5. [Lógica de UI Dinámica en JavaFX](#5-lógica-de-ui-dinámica-en-javafx)
6. [Frontend Web (Portal Ciudadano)](#6-frontend-web-portal-ciudadano)
7. [Resiliencia y Modo Offline](#7-resiliencia-y-modo-offline)
8. [Sistema de Logging](#8-sistema-de-logging)
9. [Estrategia de Testing](#9-estrategia-de-testing)
10. [Operaciones y Distribución](#10-operaciones-y-distribución)
11. [Guía de Inicio Rápido](#11-guía-de-inicio-rápido)
12. [Flujos de Trabajo](#12-flujos-de-trabajo)
13. [Documentación Técnica (Javadoc)](#13-documentación-técnica-javadoc)

---

## 1. Visión General y Tecnologías

**ERRONKA-BERMEO** es una aplicación de gestión de objetos perdidos con tres capas:

1. **JavaFX (Java 21)**: Herramienta para el back-office (empleados).
2. **MariaDB 11**: Base de datos relacional compartida.
3. **Nginx & Vanilla JS**: Portal público para ciudadanos.

### 1.1 Dockerización
Todo el ecosistema corre en contenedores Docker mediante `docker-compose.yml`. Se utiliza **multi-stage build** para la aplicación Java y **noVNC** para servir la interfaz gráfica a través del navegador (puerto 6080).

### 1.2 Tecnologías de Soporte
- **Maven**: Gestión de dependencias (MariaDB Driver, BCrypt, JavaFX).
- **BCrypt**: Hashing seguro de contraseñas.
- **XML/XSLT/XSD/DTD**: Intercambio de datos entre Java y Web, con validación de esquema.
- **Adminer**: Gestión visual de la base de datos (puerto 8081).
- **Licencia**: GNU GPL v3.

---

## 2. Arquitectura del Proyecto

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                        │
│                                                         │
│  ┌──────────────┐    ┌──────────────┐    ┌───────────┐ │
│  │  java-app    │    │     db       │    │   web     │ │
│  │  (JavaFX)    │───▶│  (MariaDB)   │    │  (Nginx)  │ │
│  │              │    │              │    │           │ │
│  │  noVNC:6080  │    │  Puerto 3306 │    │ Puerto 80 │ │
│  └──────────────┘    └──────────────┘    └─────┬─────┘ │
│         │                                      │       │
│         └──────── partekatutako_datuak ─────────┘       │
│                    (carpeta compartida)                  │
│                                                         │
│  ┌──────────────┐                                       │
│  │   adminer    │                                       │
│  │  Puerto 8081 │                                       │
│  └──────────────┘                                       │
└─────────────────────────────────────────────────────────┘
```

**Patrón MVC aplicado a toda la app:**

```
VIEW (FXML)  ←──▶  CONTROLLER (Java)  ←──▶  MODEL (Java)
                         │
                         ▼
                      DAO (Java)
                         │
                         ▼
                   DATABASE (MariaDB)
```

### 2.1 Tecnologías detalladas

#### MariaDB
Sistema gestor de bases de datos relacional open source, compatible con MySQL. Almacena los datos en tablas relacionadas mediante claves foráneas.

Lo nuevo respecto a JDBC básico:
- **Triggers**: código SQL que se ejecuta automáticamente cuando ocurre un evento en una tabla.
- **Roles de base de datos**: permisos granulares por usuario.
- **Colación UTF8MB4**: soporte para caracteres del euskera.
- **Health checks**: la BD verifica que MariaDB está lista antes de arrancar los demás contenedores.

La clase `DBKonexioa.java` busca la conexión en este orden:
1. Variables de entorno (`DB_URL`, `DB_USER`, `DB_PASS`) — usado dentro de Docker
2. Fichero `application.properties` — usado en desarrollo local
3. Si no encuentra ninguna, activa el **modo offline**

#### JavaFX y FXML
JavaFX es la librería estándar de Java para interfaces gráficas de escritorio. FXML es un formato XML que describe la estructura visual de una pantalla, separando diseño de lógica.

```
login.fxml  ←→  LoginController.java
    |                    |
 [diseño]           [lógica]
 botones            qué pasa al pulsar
 campos             validaciones
```

Elementos principales de JavaFX usados:

| Elemento | Para qué sirve |
|---------|---------------|
| `StackPane` | Contenedor que apila paneles (cambio de vista sin cerrar ventana) |
| `TableView` | Tabla con columnas y filas, conectada a una lista de objetos |
| `ComboBox` | Desplegable para seleccionar opciones |
| `DatePicker` | Selector de fecha |
| `TextField` / `PasswordField` | Campo de texto / contraseña |
| `Label` | Texto estático |
| `Button` | Botón |
| `ImageView` | Mostrar imágenes |
| `FileChooser` | Selector de archivos del sistema |
| `Alert` | Ventana emergente de error/info/confirmación |
| `FXMLLoader` | Carga un fichero FXML y lo convierte en un nodo visual |
| `VBox` / `HBox` | Contenedores verticales/horizontales para UI dinámica |

#### Maven
Herramienta de construcción y gestión de dependencias. Equivale a `npm` para JavaScript o `pip` para Python.

Dependencias del proyecto (`pom.xml`):
```xml
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>3.3.3</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.2</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.2</version>
</dependency>
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.10.1</version>
    <scope>test</scope>
</dependency>
```

#### BCrypt
Algoritmo de hash para contraseñas. Deliberadamente lento y con salt aleatorio para resistir ataques de diccionario.

```java
// Al crear un empleado
String hash = BCrypt.hashpw("miContraseña", BCrypt.gensalt());

// Al hacer login
boolean ok = BCrypt.checkpw(passwordIntroducida, hashGuardadoEnBD);
```

Usado en: `LangileaDAO.gehitu()` (crear empleado) y `LangileaDAO.login()` (autenticación).

#### Nginx
Servidor web que sirve el portal para los ciudadanos en el puerto 8000. Monta `partekatutako_datuak/` como `/datuak` para que el portal pueda leer el XML exportado por la app Java.

#### XML, XSLT, XPath, XQuery, DTD, XSD
- **XML**: formato de intercambio de datos entre Java y Web (`artikuluak.xml`)
- **XSLT** (`xslt/artikuluak.xsl`): transforma el XML en HTML para visualización directa
- **XPath** (`xpath/kontsultak.txt`): consultas de nodos dentro del XML
- **XQuery** (`xquery/kontsultak.xquery`): consultas avanzadas sobre datos XML
- **DTD** (`dtd/artikuluak.dtd`): define la estructura válida del XML
- **XSD** (`xsd/artikuluak.xsd`): esquema XML con tipos de datos

#### Adminer
Interfaz web para gestionar la base de datos desde el navegador. Disponible en `http://localhost:8081`.

```
Servidor:  db
Usuario:   admin
Contraseña: admin123
Base de datos: erronka_galduak
```

---

## 3. Base de Datos (MariaDB)

### 3.1 Tablas

La base de datos `erronka_galduak` incluye 12 tablas:

| Tabla | Descripción |
|-------|-------------|
| `ROLA` | Roles del sistema |
| `LANGILEA` | Empleados municipales con contraseña hasheada (BCrypt) |
| `KATEGORIA` | Categorías de objetos |
| `KOKALEKUA` | Ubicaciones físicas de almacenamiento (armario + balda + BHA flag) |
| `HARTZAILEA` | Base abstracta para quien recoge el objeto |
| `JABEA` | Persona física (NAN, izena, abizena, herria, pk, probintzia) |
| `ERAKUNDEA` | Organización (IFZ, izen_ofiziala) |
| `ARTIKULUA` | Objetos perdidos del inventario |
| `ERREKLAMAZIOA` | Reclamaciones de ciudadanos |
| `JAKINARAZPENA` | Notificaciones enviadas a los reclamantes |
| `EMANALDIA` | Registro de entrega de objetos (UNIQUE en id_artikulua — un objeto solo se entrega una vez) |
| `MUGIMENDUA` | Auditoría completa de todos los movimientos |
| `AURKITZAILEA` | Datos de quien encontró el objeto |

**Jerarquía de HARTZAILEA (herencia en base de datos):**

```
HARTZAILEA (abstracto)
├── JABEA (persona física - NAN)
└── ERAKUNDEA (organización - IFZ)
```

**Estados de un artículo (`egoera` ENUM):**

```
'aurkitua'   → objeto en almacén, disponible (valor por defecto)
'bueltatua'  → devuelto a su dueño
'artxibatua' → archivado manualmente
'iraungita'  → plazo de custodia expirado
```

**Estados de una reclamación (`errek_egoera` ENUM):**

```
'irekita'    → reclamación pendiente de resolver (valor por defecto)
'ebatzita'   → resuelta, objeto entregado
'baztertuta' → reclamación rechazada
```

### 3.2 Roles y usuarios de base de datos

**Roles (3):**

| Rol BD | Permisos |
|--------|---------|
| `admin_rola` | ALL PRIVILEGES sobre `erronka_galduak.*` |
| `langile_rola` | SELECT/INSERT/UPDATE/DELETE en tablas operativas; solo SELECT en `LANGILEA` y `ROLA` |
| `ikusle_rola` | SELECT en `KATEGORIA`, `KOKALEKUA`, `ROLA`; SELECT+INSERT en `ARTIKULUA`, `MUGIMENDUA`, `AURKITZAILEA` |

**Usuarios (3):**

| Usuario BD | Contraseña | Rol |
|-----------|-----------|-----|
| `admin` | `admin123` | `admin_rola` |
| `langile1` | `langile123` | `langile_rola` |
| `ikusle1` | `ikusle123` | `ikusle_rola` |

### 3.3 Triggers (5)

Los triggers se ejecutan automáticamente cuando ocurre un evento en una tabla. No se llaman desde Java.

- **`trg_historial_estado_artikulua`** (AFTER UPDATE en ARTIKULUA): cuando cambia el estado, inserta en `MUGIMENDUA` con `mota` específica (`irteera_jabea`, `iraungitze_alerta` o `barne_mugimendua` según el nuevo estado).
- **`trg_validar_fechas_ins`** (BEFORE INSERT en ARTIKULUA): valida que `iraungitze_data` no sea anterior a `sarrera_data`; lanza SQLSTATE `45000` si es inválida.
- **`trg_validar_fechas_upd`** (BEFORE UPDATE en ARTIKULUA): misma validación de fechas en actualizaciones.
- **`trg_emanaldia_eguneratu_artikulua`** (AFTER INSERT en EMANALDIA): cuando se crea una entrega, actualiza automáticamente `ARTIKULUA.egoera` a `bueltatua`.
- **`trg_blokeatu_bueltatuak`** (BEFORE UPDATE en ARTIKULUA): impide modificar artículos ya marcados como `bueltatua`; lanza SQLSTATE `45000`.

### 3.4 Procedimientos almacenados (2)

- **`sp_iraungitakoak_kudeatu`**: actualiza a `iraungita` los artículos con `egoera='aurkitua'` cuya `iraungitze_data < CURDATE()`. Devuelve el número de registros actualizados mediante parámetro OUT `p_eguneratutako_kopurua`.
- **`sp_erreklamazioa_ebatzi`**: resuelve una reclamación de forma atómica. Recibe id de reclamación, id de artículo, id de empleado y notas; marca la reclamación como `ebatzita` y crea el registro `EMANALDIA`. Incluye manejo de errores con ROLLBACK.

### 3.5 Datos iniciales (`03-seed.sql`)

- **3 roles**: Administratzailea, Langilea, Ikuslea
- **3 empleados**: Miren Agirre (`admin`), Jon Zabala (`langile1`), Ander Txurru (`ikusle1`) — contraseña `1234` (hash BCrypt)
- **6 categorías**: Osagarri pertsonalak, Betaurrekoak, Giltzak, Teknologia, Arropa eta osagarriak, Bestelakoak
- **37 ubicaciones**: armarios A, B, D, E, F, G × 6 baldas (1-6) = 36 normales + 1 especial (Logela / BHA)
- **7 artículos**: G-001-26 a G-007-26 (6 en `aurkitua`, 1 en `bueltatua`)
- **4 receptores** (HARTZAILEA/JABEA)
- **3 reclamaciones**: 2 abiertas (`irekita`), 1 resuelta (`ebatzita`)

---

## 4. Aplicación Java (Back-office)

### 4.1 Patrones de diseño

**MVC**: Model (`model/`) + View (`view/*.fxml`) + Controller (`controller/`)

**DAO**: cada entidad tiene su DAO que centraliza las operaciones SQL. El controlador nunca escribe SQL directamente.

**Clase estática pura**: `DBKonexioa`, `ModoKudeatzailea`, `BiltegiLokala`, `LogKudeatzailea`, `Sesio` y `AppConfig` son clases con todos sus métodos y campos `static`. No tienen constructores privados vacíos — simplemente no se instancian.

**`DBKonexioa`** en particular reemplazó el bloque `static {}` por un método privado `lortuPropietatea(String gakoa, String defektuz)` que carga el valor desde variable de entorno o `application.properties`, inicializando los campos directamente en la declaración:

```java
private static final String URL = lortuPropietatea("DB_URL", "jdbc:mariadb://localhost:3306/erronka_galduak");
private static final String USER = lortuPropietatea("DB_USER", "root");
private static final String PASS = lortuPropietatea("DB_PASS", "");
```

**Strategy (Modo Online/Offline)**: los DAOs comprueban `ModoKudeatzailea.isOffline()` y delegan a `BiltegiLokala` o a la BD según corresponda.

**Herencia**: `Hartzailea` (abstracto) → `Jabea` y `Erakundea`; `Langilea` → `Administratzailea`

### 4.2 Modelos (`model/`)

| Clase | Descripción |
|-------|-------------|
| `Artikulua` | Objeto perdido. Calcula automáticamente `iraungitzeData` (730 días desde entrada). |
| `Erreklamazioa` | Reclamación de ciudadano. Contiene `bilatuBateragarriak()` para matching por palabras clave. |
| `Emanaldia` | Registro de entrega. |
| `Langilea` | Empleado municipal con BCrypt. |
| `Administratzailea` | Hereda de `Langilea`, permisos de admin. |
| `Hartzailea` | Base abstracta para receptor. |
| `Jabea` | Persona física (NAN). |
| `Erakundea` | Organización (IFZ). |
| `Kategoria` | Categoría de objeto. |
| `Kokalekua` | Ubicación física (armario + balda). |
| `Aurkitzailea` | Quien encontró el objeto. |
| `Jakinarazpena` | Notificación al reclamante. |
| `AzkenMugimendua` | DTO: último movimiento para el panel. |
| `MugimenduLerroa` | DTO: fila de la tabla de auditoría. |
| `KategoriaKopurua` | DTO: estadística por categoría. |
| `Mugimendua` | Registro de auditoría. |

**Enumeraciones:**

| Enum | Valores |
|------|---------|
| `EgoeraArtikulua` | `BILTEGIAN`, `BHA_N_GORDETA`, `ITZULITA`, `IRAUNGITA`, `DOHANTZAN` |
| `EgoeraErreklamazioa` | `IREKITA`, `EBATZITA`, `BAZTERTUTA` |
| `MugimenduMota` | `SARRERA`, `BARNE_MUGIMENDUA`, `IRTEERA_JABEA`, `IRTEERA_ERAKUNDEA`, `IRAUNGITZE_ALERTA` |
| `Kanala` | `SMS`, `EMAIL`, `APP` |

### 4.3 DAOs (`dao/`)

| DAO | Responsabilidad |
|-----|----------------|
| `ArtikuluaDAO` | CRUD de artículos, generación de códigos `G-NNN-AA`, actualización masiva de caducidades |
| `LangileaDAO` | Autenticación BCrypt, CRUD de empleados |
| `KategoriaDAO` | CRUD de categorías |
| `KokalekuaDAO` | CRUD de ubicaciones |
| `ErreklamazioaDAO` | Gestión de reclamaciones |
| `EmanaldiaDAO` | Registro de entregas con transacciones manuales (`setAutoCommit(false)`) |
| `MugimenduDAO` | Consultas de auditoría |
| `EstadistikaDAO` | Estadísticas para el panel |
| `AurkitzaileaDAO` | Datos de quien encontró el objeto |
| `BackupDAO` | Exportación/backup de la BD |

Patrón estándar de cada método DAO:

```java
public static List<Artikulua> getGuztiak() {
    if (ModoKudeatzailea.isOffline()) {
        return BiltegiLokala.getArtikuluak();
    }
    // SQL normal contra la BD
}
```

### 4.4 Utilidades (`utils/`)

| Utilidad | Función |
|---------|---------|
| `AppConfig` | Rutas de ficheros. Prioridad: variable de entorno → `application.properties` → valor por defecto |
| `DBKonexioa` | Conexión JDBC singleton. Usa `lortuPropietatea()` para inicializar URL/USER/PASS sin bloque `static {}` |
| `Sesio` | Almacena de forma estática el `Langilea` logueado y sus privilegios |
| `UIKudeatzailea` | Carga FXMLs, gestiona Toasts (mensajes temporales animados) y overlays sin ventanas nuevas |
| `XMLExportazioa` | Genera `artikuluak.xml`. Valida con Regex (`^G-\d{3}-\d{2}$`) y lanza `XMLPatroiException` si los datos no cumplen el formato |
| `XMLInportazioa` | Lee XML para actualizar estados masivamente en la base de datos |
| `ModoKudeatzailea` | Detecta la salud de la conexión DB al arranque; decide modo Online u Offline |
| `LogKudeatzailea` | Configura rotación de logs y gestiona `insert_log.txt` |
| `BiltegiLokala` | Almacén local serializable (`store.dat`). Toda la clase es estática |

**Rutas de `AppConfig`:**

```java
AppConfig.getExportBidea()          // → partekatutako_datuak/
AppConfig.getXmlBidea()             // → partekatutako_datuak/artikuluak.xml
AppConfig.getArtikuluIrudiakBidea() // → artikulu_irudiak/
AppConfig.getSinaduraBidea()        // → partekatutako_datuak/sinadurak/
```

**`XMLExportazioa` — patrones de validación:**

```java
/** Artikulu-kodearen patroia: G-NNN-AA formatua (adib. G-001-26). */
private static final Pattern KODEA_PATROIA = Pattern.compile("^G-\\d{3}-\\d{2}$");

/** Artikulu-izenaren patroia: 1-200 karaktere, kontrol-karaktererik gabe. */
private static final Pattern IZENA_PATROIA = Pattern.compile("^[^\\x00-\\x1F]{1,200}$");
```

**`ModoKudeatzailea` — detección online/offline:**

```java
public static void detektatu() {
    try {
        Connection con = DBKonexioa.getKonexioa();
        if (con == null || con.isClosed()) {
            offlineModo = true;
        } else {
            offlineModo = false;
        }
    } catch (Exception e) {
        offlineModo = true;
    }
    if (offlineModo) {
        BiltegiLokala.hasieratu();
    } else {
        ArtikuluaDAO.iraungituakEguneratu();
        BiltegiLokala.sincronizatuDBtik();
    }
}
```

### 4.5 Controladores (`controller/`)

| Controlador | Funcionalidad |
|-------------|--------------|
| `LoginController` | Valida credenciales y redirige según rol |
| `MainController` | Layout principal con menú lateral y navegación dinámica |
| `AdminController` | Layout de administrador |
| `PanelaController` | Dashboard. Fuerza `iraungituakEguneratu()` al arrancar |
| `AdminPanelaController` | Panel de administración |
| `InbentarioController` | Tabla de artículos con filtrado multi-parámetro en tiempo real. Importación/Exportación XML |
| `ErregistroaController` | Formulario de registro de objeto. Copia de imágenes con nombre por timestamp |
| `ArtikuluaEditatuController` | Edición de artículo. Exporta XML automáticamente al guardar |
| `ErreklamazioakController` | UI dinámica de tarjetas (`VBox`). Matching automático de artículos |
| `ErreklamazioaBerriController` | Nueva reclamación |
| `EmanaldiaController` | Formulario dual (persona física / entidad). Gestiona documento de firma |
| `IraungitakoakController` | Artículos con más de 2 años; inicia proceso de entrega o donación |
| `AuditoriaController` | Historial de movimientos. Exporta CSV con `PrintWriter` UTF-8 |
| `GalduDabenakController` | Artículos próximos a caducar |
| `KategoriakController` | Lista de categorías con tarjetas dinámicas |
| `KategoriaBerriController` | Nueva categoría |
| `KategoriaEditatuController` | Editar categoría |
| `KokalekuakController` | Lista de ubicaciones |
| `KokalekuaBerriController` | Nueva ubicación |
| `LangileakController` | Lista de empleados con callback de recarga |
| `LangileBerriController` | Nuevo empleado |
| `LangileaEditatuController` | Editar empleado |
| `IrudiaPopupController` | Ventana emergente para ver foto de artículo |

### 4.6 Vistas FXML (`view/`)

| FXML | Pantalla |
|------|---------|
| `login.fxml` | Inicio de sesión |
| `MainLayout.fxml` | Layout principal (empleados) |
| `AdminLayout.fxml` | Layout de administrador |
| `AdminPanela.fxml` | Panel de administración |
| `Panela.fxml` | Dashboard con estadísticas |
| `Inbentario.fxml` | Tabla de artículos |
| `Erregistroa.fxml` | Formulario de registro |
| `ArtikuluaEditatu.fxml` | Editar artículo |
| `Erreklamazioak.fxml` | Lista de reclamaciones |
| `ErreklamazioaBerria.fxml` | Nueva reclamación |
| `Emanaldia.fxml` | Formulario de entrega |
| `Iraungitakoak.fxml` | Artículos caducados |
| `GalduDabenak.fxml` | Artículos próximos a caducar |
| `Auditoria.fxml` | Historial de auditoría |
| `Kategoriak.fxml` | Lista de categorías |
| `KategoriaBerria.fxml` | Nueva categoría |
| `KategoriaEditatu.fxml` | Editar categoría |
| `Kokalekuak.fxml` | Lista de ubicaciones |
| `KokalekuaBerria.fxml` | Nueva ubicación |
| `Langileak.fxml` | Lista de empleados |
| `LangileBerria.fxml` | Nuevo empleado |
| `LangileaEditatu.fxml` | Editar empleado |
| `IrudiaPopup.fxml` | Popup de imagen |

---

## 5. Lógica de UI Dinámica en JavaFX

### 5.1 TableView — la tabla de datos

`TableView<T>` muestra datos en filas y columnas. Se configura en `initialize()`.

```java
@FXML private TableView<Artikulua>           taula;
@FXML private TableColumn<Artikulua, String> colKodea;
@FXML private TableColumn<Artikulua, String> colIzena;

@Override
public void initialize(URL url, ResourceBundle rb) {
    colKodea.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getArtikuluKodea())
    );
    colIzena.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getIzenburua())
    );
}
```

**Cargar y actualizar datos:**

```java
private List<Artikulua> guztiak;

private void kargatu() {
    guztiak = ArtikuluaDAO.getGuztiak();
    taula.getItems().setAll(guztiak);
}
```

**Selección de filas y botones reactivos:**

```java
taula.getSelectionModel().selectedItemProperty()
    .addListener((obs, old, sel) -> {
        boolean dago = sel != null;
        btnEditatu.setDisable(!dago);
        btnEzabatu.setDisable(!dago);
    });
```

### 5.2 Filtrado en tiempo real

Se mantiene `guztiak` (lista completa) y se filtra sin volver a la BD:

```java
@FXML
private void bilatu() {
    String testua = txtBilaketa.getText().trim().toLowerCase();
    String katSel = cbKategoria.getValue();
    String egSel  = cbEgoera.getValue();

    List<Artikulua> iragaziak = new ArrayList<>();
    for (Artikulua a : guztiak) {
        boolean testuanAurkitu;
        if (testua.isEmpty()) {
            testuanAurkitu = true;
        } else if (a.getArtikuluKodea().toLowerCase().contains(testua)) {
            testuanAurkitu = true;
        } else if (a.getIzenburua().toLowerCase().contains(testua)) {
            testuanAurkitu = true;
        } else if (a.getDeskribapena().toLowerCase().contains(testua)) {
            testuanAurkitu = true;
        } else {
            testuanAurkitu = false;
        }

        boolean katPasa;
        if (katSel == null || katSel.equals("Kategoria guztiak")) {
            katPasa = true;
        } else if (a.getKategoriaIzena().equals(katSel)) {
            katPasa = true;
        } else {
            katPasa = false;
        }

        boolean egPasa;
        if (egSel == null || egSel.equals("Egoera guztiak")) {
            egPasa = true;
        } else if (a.getEgoeraTestua().equalsIgnoreCase(egSel)) {
            egPasa = true;
        } else {
            egPasa = false;
        }

        if (testuanAurkitu && katPasa && egPasa) {
            iragaziak.add(a);
        }
    }
    taula.getItems().setAll(iragaziak);
}
```

### 5.3 Ventanas modales y popups

```java
FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IrudiaPopup.fxml"));
Parent root = loader.load();
IrudiaPopupController ctrl = loader.getController();
ctrl.kargatu(sel);

Stage stage = new Stage();
stage.initModality(Modality.APPLICATION_MODAL);
stage.setScene(new Scene(root));
stage.show();
```

| `Modality` | Comportamiento |
|-----------|---------------|
| `NONE` | No bloquea nada |
| `WINDOW_MODAL` | Bloquea solo la ventana padre |
| `APPLICATION_MODAL` | Bloquea toda la aplicación |

### 5.4 Diálogos — Alert y TextInputDialog

```java
Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
confirm.setHeaderText("Artikulua ezabatu: " + sel.getArtikuluKodea());
Optional<ButtonType> resp = confirm.showAndWait();
if (resp.isPresent() && resp.get() == ButtonType.OK) {
    ArtikuluaDAO.ezabatu(sel.getArtikuluKodea());
}
```

### 5.5 UI dinámica con VBox y HBox

Las secciones de categorías y reclamaciones construyen su UI en tiempo de ejecución:

```java
private HBox sortuTxartela(Kategoria k) {
    VBox info = new VBox(4);
    Label lblIzena = new Label(k.getIzena());
    info.getChildren().add(lblIzena);
    HBox.setHgrow(info, Priority.ALWAYS);

    Button btnEdita = new Button("Editatu");
    btnEdita.setOnAction(e -> editatuKategoria(k));

    HBox txartela = new HBox(15);
    txartela.getStyleClass().add("card");
    txartela.setPadding(new Insets(15));
    txartela.getChildren().addAll(info, btnEdita);
    return txartela;
}
```

### 5.6 Visibilidad y gestión de nodos

```java
btnEbatzi.setVisible(irekita);
btnEbatzi.setManaged(irekita);   // sin setManaged, el nodo invisible sigue ocupando espacio
```

### 5.7 Callbacks entre controladores

```java
// Controlador padre pasa una función al hijo
ctrl.setOnUpdateCallback(() -> kargatu());

// Controlador hijo la ejecuta al guardar
if (onUpdateCallback != null) {
    onUpdateCallback.run();
}
```

---

## 6. Frontend Web (Portal Ciudadano)

Ubicación: `frontend/`

Portal ligero servido por Nginx en el puerto 8000.

```
frontend/
├── index.html
├── css/          style.css, style.min.css
├── fonts/        5 ficheros WOFF2 (Inter, Montserrat)
├── html/         objektu-zerrenda.html
├── js/           main.js, objektu-zerrenda.js
├── dtd/          artikuluak.dtd
├── xsd/          artikuluak.xsd
├── xslt/         artikuluak.xsl
├── xpath/        kontsultak.txt
├── xquery/       kontsultak.xquery
└── datuak/img/   imágenes de muestra
```

### 6.1 Internacionalización (i18n)
- Diccionarios JS (`dictIndex` en `main.js`, `dictCatalog` en `objektu-zerrenda.js`) para EU y ES.
- Atributo `data-i18n` en los elementos HTML: se actualizan automáticamente al cambiar idioma.
- Preferencia guardada en `localStorage`.

### 6.2 Modo Oscuro
- Clase `.dark-theme` en `<body>` con variables CSS (`:root`).
- Preferencia guardada en `localStorage`.
- CSS Grid para catálogo responsive, Flexbox para navegación.

### 6.3 Gestión de datos XML en cliente
- `fetch` lee `artikuluak.xml` exportado por la App Java.
- Filtrado de nodos del DOM en tiempo real.
- `artikuluak.xsl` transforma el XML en tabla HTML para visualización directa.

### 6.4 Validación y generación de reclamaciones
1. Regex en JS valida el DNI antes de procesar (`/^[0-9]{8}[a-zA-Z]$/`).
2. Escapado de caracteres especiales (`&`, `<`, `>`) para XML válido.
3. Generación de XML en memoria y descarga como `Blob`.

---

## 7. Resiliencia y Modo Offline

### 7.1 Serialización `.dat`

`BiltegiLokala` persiste el estado en `store.dat` (binario, no legible por humanos) mediante serialización Java:

```java
private static class DatuakPoltsa implements Serializable {
    private static final long serialVersionUID = 2L;
    List<Langilea> langileak = new ArrayList<>();
    List<Artikulua> artikuluak = new ArrayList<>();
    List<ItxaronEragiketa> itxaronEragiketak = new ArrayList<>();
    // ...
}
```

**Guardar:**
```java
try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("store.dat"))) {
    oos.writeObject(poltsa);
}
```

**Leer:**
```java
try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("store.dat"))) {
    DatuakPoltsa poltsa = (DatuakPoltsa) ois.readObject();
}
```

### 7.2 Ciclo de vida

```
App arranca
    ├── Hay BD (online)
    │     ① Carga store.dat en memoria (si existe)
    │     ② sincronizatuDBtik() → sobreescribe con datos reales de BD
    │     ③ Guarda store.dat actualizado
    │
    └── Sin BD (offline)
          ① Carga store.dat (datos de la última sesión online)
          └── Si store.dat no existe → listas vacías
```

Ubicación del fichero: `partekatutako_datuak/store.dat`

### 7.3 Cola de sincronización

Cada operación en offline se guarda en `ItxaronEragiketa` (serializable). Al volver online, se procesan y suben a la BD.

---

## 8. Sistema de Logging

`LogKudeatzailea` gestiona dos niveles:

**1. Logs de aplicación (`app-N.log`):**
- `java.util.logging` con rotación automática: máx. 5 MB por fichero, 3 ficheros históricos.
- Filtra por paquetes (`controller.*`, `dao.*`, `model.*`, `utils.*`).
- Ubicación: `~/erronka-bermeo/logs/`

**2. Log de operaciones (`insert_log.txt`):**
- Registro físico de cada inserción con marca de tiempo.
- Formato: `[2026-05-15 14:23:01] INSERT INTO ARTIKULUA | ID: G-001-26 | OK`
- Ubicación: `partekatutako_datuak/insert_log.txt`

```java
// Obtener logger de una clase
Logger log = LogKudeatzailea.lortu(ArtikuluaDAO.class);

log.info("Operación completada");
log.warning("Algo sospechoso");
log.severe("Error grave");
```

---

## 9. Estrategia de Testing

Existe un único fichero de test: `src/test/java/dao/KategoriaDAOTest.java`

Cubre:
- Validación de lógica de categorías via DAO.
- Se ejecuta con JUnit 5 (`junit-jupiter-api` y `junit-jupiter-engine` versión 5.10.1).

```bash
mvn test
```

---

## 10. Operaciones y Distribución

### 10.1 Empaquetado nativo (.exe)

Se usa `jpackage` (JDK 21) para generar un ejecutable Windows independiente con JRE incluida:

```batch
jpackage --input target --main-jar app.jar --type exe --name Galdutakoak ...
```

### 10.2 Backups

Los backups SQL se generan en `partekatutako_datuak/` (ej. `backup_20260515_1128.sql`).

Restaurar:
```bash
docker exec -i db mariadb -uadmin -padmin123 erronka_galduak < backup_archivo.sql
```

**Advertencia:** sobrescribe los datos actuales de la BD con los del backup.

---

## 11. Guía de Inicio Rápido

### 11.1 Arranque

**Linux:**
```bash
./start-linux.sh
# O manualmente:
xhost +local:docker
docker compose up -d
```

**Windows:**
```bat
start-windows.bat
```

### 11.2 URLs de acceso

| Servicio | URL |
|---------|-----|
| Portal web ciudadanos | http://localhost:8000 |
| Aplicación JavaFX (noVNC) | http://localhost:6080/vnc.html?autoconnect=1&resize=scale |
| Adminer (gestión BD) | http://localhost:8081 |

### 11.3 Credenciales

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | Administratzailea (acceso total) |
| `langile1` | `langile123` | Langilea |
| `ikusle1` | `ikusle123` | Ikuslea |

Contraseña de todos en la app Java: `1234` (hash BCrypt en BD)

### 11.4 Parar y limpiar

```bash
# Parar
./stop-linux.sh
# O:
docker compose down

# Limpiar también los datos de la BD
docker compose down -v
```

---

## 12. Flujos de Trabajo

### Registro de un objeto perdido

```
1. Empleado encuentra objeto
2. Abre ErregistroaController → rellena formulario
3. Selecciona categoría (KategoriaDAO.getGuztiak())
4. Selecciona ubicación (KokalekuaDAO.getGuztiak())
5. Sube foto → se guarda en artikulu_irudiak/ con nombre timestamp
6. Pulsa "Gorde" → ArtikuluaDAO.gehitu(...)
   ├── Genera código G-NNN-YY
   ├── INSERT INTO ARTIKULUA (egoera='aurkitua', iraungitze_data = entrada + 730 días)
   └── TRIGGER crea entrada en MUGIMENDUA
7. XMLExportazioa.exportatu() → actualiza partekatutako_datuak/artikuluak.xml
8. El portal web muestra el objeto al ciudadano
```

### Reclamación y entrega

```
1. Ciudadano ve el objeto en la web → contacta al ayuntamiento
2. Empleado abre ErreklamazioaBerriController
3. Registra datos del reclamante (NAN/nombre)
4. Erreklamazioa.bilatuBateragarriak() → busca artículos que coincidan por palabras clave
5. Si hay match → EmanaldiaController
   ├── Registra la entrega (INSERT INTO EMANALDIA, transacción manual)
   ├── TRIGGER actualiza ARTIKULUA.egoera = 'bueltatua'
   └── TRIGGER registra en MUGIMENDUA
6. Reclamación marcada como EBATZITA
```

---

## 13. Documentación Técnica (Javadoc)

Toda la base de código está documentada en **Euskera** con Javadoc estándar.

Generar la documentación HTML:
```bash
cd java-app
mvn javadoc:javadoc
# Resultado en: target/site/apidocs/index.html
```

**Cobertura de Javadoc:**

| Paquete | Clases |
|---------|--------|
| `controller` | AdminController, AdminPanelaController, ArtikuluaEditatuController, AuditoriaController, EmanaldiaController, ErregistroaController, ErreklamazioaBerriController, ErreklamazioakController, GalduDabenakController, InbentarioController, IraungitakoakController, IrudiaPopupController, KategoriaBerriController, KategoriaEditatuController, KategoriakController, KokalekuaBerriController, KokalekuakController, LangileaEditatuController, LangileakController, LangileBerriController, LoginController, MainController, PanelaController |
| `dao` | ArtikuluaDAO, AurkitzaileaDAO, BackupDAO, EmanaldiaDAO, ErreklamazioaDAO, EstadistikaDAO, KategoriaDAO, KokalekuaDAO, LangileaDAO, MugimenduDAO |
| `model` | Administratzailea, Artikulua, Aurkitzailea, AzkenMugimendua, Emanaldia, Erakundea, Erreklamazioa, Hartzailea, Jabea, Jakinarazpena, Kategoria, KategoriaKopurua, Kokalekua, Langilea, Mugimendua, MugimenduLerroa |
| `utils` | AppConfig, BiltegiLokala, DBKonexioa, LogKudeatzailea, ModoKudeatzailea, Sesio, UIKudeatzailea, XMLExportazioa, XMLInportazioa |

Ejemplo de Javadoc en euskera:

```java
/**
 * Datu-basean erregistratutako langile baten autentifikazioa egiten du.
 * BCrypt bidez konparatzen du sartutako pasahitza gordetako hash-arekin.
 *
 * @param erabiltzailea Erabiltzailearen izena (username)
 * @param pasahitza     Sartu nahi diren testu garbiko pasahitza
 * @return Langilea objektua autentifikazioa arrakastatsua bada, null bestela
 */
public static Langilea login(String erabiltzailea, String pasahitza) { ... }
```

---

*Documentación actualizada el 2026-05-15 para el proyecto ERRONKA-BERMEO v1.0.*
