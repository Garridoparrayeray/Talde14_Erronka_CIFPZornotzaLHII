# ERRONKA-BERMEO — Documentación Técnica Completa

> Sistema de gestión de objetos perdidos para el Ayuntamiento de Bermeo (Bermeoko Udala).

---

## Índice

1. [Visión general del proyecto](#1-visión-general-del-proyecto)
2. [Tecnologías nuevas — qué son y para qué sirven](#2-tecnologías-nuevas)
   - 2.1 [Docker y Docker Compose](#21-docker-y-docker-compose)
   - 2.2 [MariaDB — base de datos relacional](#22-mariadb)
   - 2.3 [JavaFX y FXML](#23-javafx-y-fxml)
   - 2.4 [Maven — gestor de dependencias y build](#24-maven)
   - 2.5 [BCrypt — hash de contraseñas](#25-bcrypt)
   - 2.6 [Nginx — servidor web](#26-nginx)
   - 2.7 [XML, XSLT, XPath y XQuery](#27-xml-xslt-xpath-y-xquery)
   - 2.8 [Adminer — interfaz web para la base de datos](#28-adminer)
   - 2.9 [Licencia — GNU GPL v3](#29-licencia)
3. [Controles de tabla y UI dinámica en JavaFX](#3-controles-de-tabla-y-ui-dinámica-en-javafx)
   - 3.1 [TableView — la tabla de datos](#31-tableview--la-tabla-de-datos)
   - 3.2 [setCellValueFactory — cómo se conecta cada columna al modelo](#32-setcellvaluefactory--cómo-se-conecta-cada-columna-al-modelo)
   - 3.3 [Cargar y actualizar datos en la tabla](#33-cargar-y-actualizar-datos-en-la-tabla)
   - 3.4 [Selección de filas y botones reactivos](#34-selección-de-filas-y-botones-reactivos)
   - 3.5 [Filtrado y búsqueda en tiempo real](#35-filtrado-y-búsqueda-en-tiempo-real)
   - 3.6 [ComboBox — desplegable de opciones](#36-combobox--desplegable-de-opciones)
   - 3.7 [Ventanas modales y popups](#37-ventanas-modales-y-popups)
   - 3.8 [Diálogos — Alert, TextInputDialog, Confirmation](#38-diálogos--alert-textinputdialog-confirmation)
   - 3.9 [UI dinámica sin TableView — tarjetas con VBox y HBox](#39-ui-dinámica-sin-tableview--tarjetas-con-vbox-y-hbox)
   - 3.10 [Layouts dinámicos — VBox, HBox, StackPane, Region](#310-layouts-dinámicos--vbox-hbox-stackpane-region)
   - 3.11 [Visibilidad y gestión de nodos en tiempo de ejecución](#311-visibilidad-y-gestión-de-nodos-en-tiempo-de-ejecución)
   - 3.12 [Callbacks entre controladores](#312-callbacks-entre-controladores)
4. [Arquitectura del proyecto](#4-arquitectura-del-proyecto)
5. [Base de datos](#5-base-de-datos)
   - 5.1 [Tablas](#51-tablas)
   - 5.2 [Roles y usuarios de base de datos](#52-roles-y-usuarios-de-base-de-datos)
   - 5.3 [Triggers](#53-triggers)
   - 5.4 [Datos iniciales (seed)](#54-datos-iniciales)
   - 5.5 [Procedimientos almacenados](#55-procedimientos-almacenados)
6. [Aplicación Java (escritorio)](#6-aplicación-java-escritorio)
   - 6.1 [Patrones de diseño usados](#61-patrones-de-diseño-usados)
   - 6.2 [Capa de modelos](#62-capa-de-modelos)
   - 6.3 [Capa DAO](#63-capa-dao)
   - 6.4 [Capa de utilidades](#64-capa-de-utilidades)
   - 6.5 [Capa de controladores](#65-capa-de-controladores)
   - 6.6 [Capa de vistas (FXML)](#66-capa-de-vistas-fxml)
7. [Frontend web](#7-frontend-web)
   - 7.1 [Internacionalización (i18n)](#71-internacionalización-i18n)
   - 7.2 [Modo Oscuro y Diseño Responsive](#72-modo-oscuro-y-diseño-responsive)
   - 7.3 [Gestión de Datos XML en Cliente](#73-gestión-de-datos-xml-en-cliente)
   - 7.4 [Validación y Generación de Reclamaciones](#74-validación-y-generación-de-reclamaciones)
10. [Empaquetado y Distribución](#10-empaquetado-y-distribución)
11. [Cómo arrancar el proyecto](#11-cómo-arrancar-el-proyecto)
12. [Flujo completo de datos](#12-flujo-completo-de-datos)
13. [Javadoc en euskera](#13-javadoc--documentación-en-euskera)

---

## 1. Visión general del proyecto

**ERRONKA-BERMEO** es una aplicación de gestión de objetos perdidos con tres capas:

| Capa | Tecnología | Para quién |
|------|-----------|-----------|
| Aplicación de escritorio | Java 21 + JavaFX | Empleados municipales (udaltzainak) |
| Base de datos | MariaDB 11 | Almacenamiento compartido |
| Portal web | HTML/CSS/JS + Nginx | Ciudadanos que buscan objetos |

Las tres capas corren en **contenedores Docker** y se comunican entre sí a través de una red virtual.

---

## 2. Tecnologías nuevas

### 2.1 Docker y Docker Compose

**¿Qué es Docker?**  
Docker es una herramienta que permite empaquetar una aplicación junto con todo lo que necesita para funcionar (sistema operativo, librerías, configuración) en un **contenedor**. Un contenedor es como una caja sellada: dentro siempre hay exactamente lo mismo, sin importar en qué ordenador se ejecute.

**¿Qué problema resuelve?**  
Elimina el clásico problema de "en mi ordenador funciona". Como el contenedor lleva su propio entorno, el programa se comporta igual en cualquier máquina.

**¿Qué es Docker Compose?**  
Es una herramienta que permite arrancar **varios contenedores a la vez** y definir cómo se conectan entre sí, usando un único fichero de configuración (`docker-compose.yml`).

**Fichero `docker-compose.yml` de este proyecto:**

```yaml
services:
  db:           # Contenedor con MariaDB
  java-app:     # Contenedor con la aplicación JavaFX
  web:          # Contenedor con Nginx (portal web)
  adminer:      # Contenedor con interfaz web para la BD
```

**Conceptos clave de Docker Compose:**

| Concepto | Qué hace | Ejemplo en el proyecto |
|---------|---------|----------------------|
| `image:` | Imagen base del contenedor | `mariadb:11` — usa MariaDB versión 11 |
| `build:` | Construye la imagen desde un Dockerfile | `./java-app/Dockerfile` |
| `ports:` | Mapea puerto del contenedor al host | `8000:80` → accedes con `localhost:8000` |
| `environment:` | Variables de entorno dentro del contenedor | `DB_URL`, `DB_USER`, `DB_PASS` |
| `volumes:` | Carpetas compartidas entre host y contenedor | `partekatutako_datuak` |
| `depends_on:` | Espera a que otro servicio esté listo | `java-app` espera a `db` |
| `networks:` | Red virtual entre contenedores | `erronka-net` |
| `healthcheck:` | Comprueba si el servicio está sano | Ping a MariaDB cada 10 segundos |

**Gestión de variables de entorno (`.env`):**  
El proyecto utiliza un archivo `.env` para centralizar la configuración sin hardcodear datos sensibles:

```properties
DB_URL=jdbc:mariadb://db:3306/erronka_galduak
DB_USER=admin
DB_PASS=admin123
EXPORT_BIDEA=/app/partekatutako_datuak
IRUDIAK_BIDEA=/app/artikulu_irudiak
```

**Acceso Gráfico (noVNC):**  
Dado que la aplicación JavaFX corre dentro de un contenedor Linux (Ubuntu), se ha implementado un stack de visualización:
1. **Xvfb**: Crea un servidor de pantalla virtual en memoria.
2. **x11vnc**: Expone la pantalla virtual mediante el protocolo VNC.
3. **noVNC**: Convierte el tráfico VNC a WebSockets para poder ver la interfaz desde cualquier navegador en el puerto **6080**.

**Dockerfile (para la app Java):**  
El `Dockerfile` es el "manual de construcción" del contenedor. El de este proyecto usa **multi-stage build** (construcción en dos fases):

```
Fase 1 (builder): Maven + JDK 21
  → descarga dependencias
  → compila el código
  → genera app.jar

Fase 2 (runtime): JRE 21 slim
  → solo copia el .jar de la fase anterior
  → instala librerías gráficas para JavaFX
  → ejecuta: java -jar app.jar
```

La ventaja del multi-stage es que la imagen final es mucho más pequeña (no lleva Maven ni el JDK completo).

**Comandos útiles:**

```bash
# Arrancar todos los servicios
docker compose up -d

# Parar todos los servicios
docker compose down

# Ver logs de un servicio
docker compose logs java-app

# Reconstruir la imagen de Java
docker compose build java-app

# Ver contenedores en ejecución
docker ps
```

---

### 2.2 MariaDB

**¿Qué es?**  
MariaDB es un sistema gestor de bases de datos relacionales (SGBD), compatible con MySQL y open source. Almacena los datos en tablas relacionadas entre sí mediante claves foráneas.

**Diferencias con lo que ya sabías (JDBC básico):**

Lo que ya sabías: conectarte a una BD con JDBC y hacer `SELECT`, `INSERT`, `UPDATE`, `DELETE`.

Lo **nuevo** en este proyecto:

- **Triggers** (ver sección 4.3): código SQL que se ejecuta automáticamente cuando ocurre un evento en una tabla.
- **Roles de base de datos** (ver sección 4.2): sistema de permisos para que cada usuario solo pueda hacer lo que le corresponde.
- **Colación UTF8MB4**: configuración especial para soportar caracteres del euskera (ñ, ü, ä...).
- **Health checks**: el contenedor de la BD verifica que MariaDB está lista antes de dejar arrancar los demás contenedores.
- **Usuario de solo lectura para la web** (`bezero_web`): acceso restringido para el portal público.

**Configuración de conexión en el proyecto:**  
La clase `DBKonexioa.java` busca la conexión en este orden:
1. Variables de entorno (`DB_URL`, `DB_USER`, `DB_PASS`) — usado dentro de Docker
2. Fichero `application.properties` — usado en desarrollo local
3. Si no encuentra ninguna, activa el **modo offline**

---

### 2.3 JavaFX y FXML

**¿Qué es JavaFX?**  
JavaFX es la librería estándar de Java para construir **interfaces gráficas de escritorio** (ventanas, botones, tablas, formularios). Reemplaza a la antigua librería Swing.

**¿Qué es FXML?**  
FXML es un formato de archivo XML que describe la **estructura visual** de una pantalla (qué elementos hay y dónde están). Separa el diseño de la lógica: el diseñador edita el FXML y el programador escribe el controlador en Java.

**Cómo funciona en este proyecto:**

```
login.fxml  ←→  LoginController.java
    |                    |
 [diseño]           [lógica]
 botones            qué pasa al pulsar
 campos             validaciones
```

**Estructura de un controlador JavaFX:**

```java
public class LoginController {

    // @FXML conecta la variable Java con el elemento del FXML que tiene ese id
    @FXML private TextField txtErabiltzailea;
    @FXML private PasswordField txtPasahitza;
    @FXML private Label lblErrorea;

    // Se ejecuta automáticamente al cargar la pantalla
    @FXML
    public void initialize() {
        // setup inicial
    }

    // Se ejecuta al pulsar el botón "Sartu" (definido en el FXML con onAction="#sartu")
    @FXML
    private void sartu() {
        String user = txtErabiltzailea.getText();
        // ...
    }
}
```

**Elementos principales de JavaFX usados:**

| Elemento | Para qué sirve |
|---------|---------------|
| `StackPane` | Contenedor que apila paneles (se usa para cambiar de vista sin cerrar la ventana) |
| `TableView` | Tabla con columnas y filas, conectada a una lista de objetos |
| `TableColumn` | Columna de una tabla |
| `ObservableList` | Lista que actualiza la tabla automáticamente al cambiar |
| `ComboBox` | Desplegable para seleccionar opciones |
| `DatePicker` | Selector de fecha |
| `TextField` / `PasswordField` | Campo de texto / contraseña |
| `Label` | Texto estático |
| `Button` | Botón |
| `ImageView` | Mostrar imágenes |
| `FileChooser` | Selector de archivos del sistema |
| `Alert` | Ventana emergente de error/info/confirmación |
| `FXMLLoader` | Carga un fichero FXML y lo convierte en un nodo visual |

**Cómo se carga una pantalla:**

```java
// Cargar un FXML y obtener su controlador
FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
Parent root = loader.load();
LoginController ctrl = loader.getController();

// Mostrar en una nueva ventana
Stage stage = new Stage();
stage.setScene(new Scene(root));
stage.show();
```

**Cambio dinámico de paneles** (sin abrir ventana nueva):  
El proyecto usa un `StackPane contentArea` en el layout principal. Para cambiar de sección, simplemente se carga el nuevo FXML y se reemplaza el contenido:

```java
// UIKudeatzailea.kargatuPanela() hace esto:
FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlBidea));
Node nodoa = loader.load();
edukiGunea.getChildren().setAll(nodoa);
```

**X11 y pantalla gráfica en Docker:**  
JavaFX necesita un servidor de pantalla para mostrar la ventana. En Linux, se usa X11. El `docker-compose.yml` monta el socket X11 del host para que la aplicación dentro del contenedor pueda mostrar su ventana en la pantalla física:

```yaml
environment:
  - DISPLAY=${DISPLAY}
volumes:
  - /tmp/.X11-unix:/tmp/.X11-unix
```

---

### 2.4 Maven

**¿Qué es?**  
Maven es una herramienta de **construcción y gestión de dependencias** para Java. Equivale a lo que sería `npm` para JavaScript o `pip` para Python.

**¿Qué problema resuelve?**  
Sin Maven, tendrías que descargar manualmente los `.jar` de cada librería (MariaDB driver, JavaFX, BCrypt...) y añadirlos al classpath. Maven lo hace automáticamente.

**Fichero `pom.xml`:**  
Es el fichero de configuración de Maven. Define:
- El nombre y versión del proyecto
- Las dependencias (librerías externas)
- Cómo se construye el proyecto

**Dependencias del proyecto:**

```xml
<!-- Driver de MariaDB para conectarse desde Java -->
<dependency>
    <groupId>org.mariadb.jdbc</groupId>
    <artifactId>mariadb-java-client</artifactId>
    <version>3.3.3</version>
</dependency>

<!-- JavaFX - interfaz gráfica -->
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

<!-- BCrypt para contraseñas -->
<dependency>
    <groupId>org.mindrot</groupId>
    <artifactId>jbcrypt</artifactId>
    <version>0.4</version>
</dependency>

<!-- JUnit para tests -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <version>5.10.1</version>
</dependency>
```

**Maven Shade Plugin:**  
Genera un **fat JAR** (un único `.jar` que contiene la aplicación y todas sus dependencias). Esto simplifica el despliegue: en lugar de necesitar todas las librerías por separado, basta con ejecutar un solo archivo.

```bash
# Compilar y generar el JAR
mvn package

# Ejecutar el JAR resultante
java -jar target/app.jar
```

---

### 2.5 BCrypt

**¿Qué es?**  
BCrypt es un **algoritmo de hash para contraseñas**. Cuando un usuario crea su contraseña, no se guarda la contraseña en texto plano sino su hash (una cadena ilegible que no puede revertirse a la contraseña original).

**¿Por qué no basta con SHA o MD5?**  
SHA y MD5 son rápidos, y eso los hace vulnerables a ataques de diccionario (probar millones de contraseñas por segundo). BCrypt es deliberadamente lento y usa un **salt** (valor aleatorio) para que dos contraseñas iguales produzcan hashes diferentes.

**Uso en el proyecto:**

```java
import org.mindrot.jbcrypt.BCrypt;

// Al crear un empleado — guardar contraseña hasheada
String hash = BCrypt.hashpw("miContraseña", BCrypt.gensalt());
// hash: "$2a$10$xyz..." (nunca se puede revertir a "miContraseña")

// Al hacer login — verificar sin desencriptar
boolean ok = BCrypt.checkpw(passwordIntroducida, hashGuardadoEnBD);
```

**Dónde se usa:** `LangileaDAO.gehitu()` (crear empleado) y `LangileaDAO.login()` (autenticación).

---

### 2.6 Nginx

**¿Qué es?**  
Nginx (se pronuncia "engine-x") es un **servidor web** de alto rendimiento. En este proyecto sirve el portal web para los ciudadanos.

**¿Qué hace?**  
Cuando el ciudadano abre el navegador en `http://localhost:8000`, Nginx recibe la petición y devuelve los ficheros HTML, CSS, JS e imágenes del portal.

**Configuración en Docker Compose:**

```yaml
web:
  image: nginx:alpine
  ports:
    - "8000:80"          # Puerto 8000 en el host → puerto 80 en el contenedor
  volumes:
    - ./frontend:/usr/share/nginx/html   # El contenido web
    - ./partekatutako_datuak:/datuak     # Carpeta compartida con la app Java
```

**Carpeta compartida (`partekatutako_datuak`):**  
La aplicación Java exporta los artículos a un XML en esta carpeta. Nginx la monta como `/datuak` para que el portal web pueda leerla. Así la app Java y la web comparten datos sin que Java tenga que exponer una API REST.

---

### 2.7 XML, XSLT, XPath y XQuery

**¿Qué es XML?**  
XML (eXtensible Markup Language) es un formato de texto estructurado para almacenar e intercambiar datos, similar a HTML pero pensado para datos, no para presentación.

**En el proyecto:** La app Java exporta el inventario a un fichero XML que el portal web puede leer.

```xml
<?xml version="1.0" encoding="UTF-8"?>
<artikuluak>
  <artikulua>
    <id>1</id>
    <izena>Cartera de cuero</izena>
    <deskribapena>Cartera marrón con monedero</deskribapena>
    <kategoria>Accesorios personales</kategoria>
    <kokalekua>F-004 / Apala 3</kokalekua>
    <egoera>BILTEGIAN</egoera>
    <sarreraData>2025-01-15</sarreraData>
    <iraungitzeData>2027-01-15</iraungitzeData>
    <argazkia>irudiak/cartera.jpg</argazkia>
  </artikulua>
</artikuluak>
```

**¿Qué es XSLT?**  
XSLT (eXtensible Stylesheet Language Transformations) permite **transformar** un XML en otro formato (HTML, otro XML, texto plano). Funciona como una plantilla que dice "para cada `<artikulua>` del XML, genera este HTML".

```xml
<!-- artikuluak.xsl — fragmento -->
<xsl:for-each select="artikuluak/artikulua">
  <div class="card">
    <h3><xsl:value-of select="izena"/></h3>
    <p><xsl:value-of select="deskribapena"/></p>
  </div>
</xsl:for-each>
```

**¿Qué es XPath?**  
XPath es un lenguaje para **navegar y seleccionar nodos** dentro de un documento XML. Es a XML lo que SQL es a una base de datos.

```xpath
/artikuluak/artikulua[egoera='BILTEGIAN']   → todos los artículos en almacén
/artikuluak/artikulua[kategoria='Giltze']   → solo las llaves
count(/artikuluak/artikulua)                → número total de artículos
```

**¿Qué es XQuery?**  
XQuery es un lenguaje más potente que XPath para consultar documentos XML. Permite hacer operaciones similares a SQL (filtrar, ordenar, agrupar) sobre datos XML.

```xquery
for $a in doc("artikuluak.xml")/artikuluak/artikulua
where $a/egoera = 'BILTEGIAN'
order by $a/sarreraData
return $a/izena
```

**¿Qué es DTD?**  
DTD (Document Type Definition) define la **estructura válida** de un XML: qué elementos puede tener, en qué orden, cuáles son obligatorios. Equivale a definir el "esquema" del XML.

---

### 2.8 Adminer

**¿Qué es?**  
Adminer es una **interfaz web** para gestionar bases de datos directamente desde el navegador. Permite ver tablas, ejecutar consultas SQL, importar/exportar datos...

**En el proyecto:** Disponible en `http://localhost:8081`.

```
Servidor:  db
Usuario:   admin
Contraseña: admin123
Base de datos: erronka_galduak
```

Útil durante el desarrollo para inspeccionar los datos sin necesidad de instalar un cliente SQL.

---

### 2.9 Licencia

Este proyecto está bajo la licencia **GNU General Public License v3 (GPLv3)**. Esto significa que es Software Libre: tienes libertad para ejecutar, estudiar, compartir y modificar el software, siempre que las obras derivadas mantengan la misma licencia.

---

## 3. Controles de tabla y UI dinámica en JavaFX

Esta sección explica en detalle cómo se construyen y controlan las tablas, listas y paneles dinámicos de la aplicación. Todo el código de los ejemplos proviene directamente de los controladores del proyecto.

---

### 3.1 TableView — la tabla de datos

`TableView<T>` es el componente de JavaFX para mostrar datos en filas y columnas, como una hoja de cálculo. La `T` es el tipo de objeto que representa cada fila.

**Declaración en el FXML:**
```xml
<TableView fx:id="taula">
    <columns>
        <TableColumn fx:id="colKodea"     text="Kodea"/>
        <TableColumn fx:id="colIzena"     text="Izena"/>
        <TableColumn fx:id="colKategoria" text="Kategoria"/>
        <TableColumn fx:id="colEgoera"    text="Egoera"/>
    </columns>
</TableView>
```

**Declaración en el controlador (InbentarioController):**
```java
@FXML private TableView<Artikulua>          taula;
@FXML private TableColumn<Artikulua, String> colKodea;
@FXML private TableColumn<Artikulua, String> colIzena;
@FXML private TableColumn<Artikulua, String> colKategoria;
@FXML private TableColumn<Artikulua, String> colEgoera;
```

- `TableView<Artikulua>` — cada fila es un objeto `Artikulua`
- `TableColumn<Artikulua, String>` — la columna pertenece a `Artikulua` y muestra un `String`

---

### 3.2 setCellValueFactory — cómo se conecta cada columna al modelo

`setCellValueFactory` le dice a cada columna **de qué campo del objeto obtener su valor**. Se configura una vez en `initialize()` y JavaFX lo aplica automáticamente a cada fila.

**Código real de InbentarioController:**
```java
@Override
public void initialize(URL url, ResourceBundle rb) {
    colKodea.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getArtikuluKodea())
    );
    colIzena.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getIzenburua())
    );
    colKategoria.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getKategoriaIzena())
    );
    colEgoera.setCellValueFactory(
        c -> new SimpleStringProperty(c.getValue().getEgoeraTestua())
    );
}
```

**¿Qué es `c -> ...`?**  
Es una **lambda** (función anónima). `c` es un objeto `CellDataFeatures` que contiene la fila. `c.getValue()` devuelve el objeto de esa fila (un `Artikulua`). La columna llama a esta lambda por cada fila que necesita mostrar.

**¿Qué es `SimpleStringProperty`?**  
JavaFX trabaja con "propiedades observables" en lugar de `String` directos. `SimpleStringProperty` envuelve un `String` en una propiedad que JavaFX puede vigilar y actualizar automáticamente si cambia. Cuando usas `getCellValueFactory` siempre debes devolver una propiedad, no un `String`.

```java
// Correcto — devuelve una Property
c -> new SimpleStringProperty(c.getValue().getIzenburua())

// También válido con PropertyValueFactory (solo si los getters siguen la convención JavaBean)
new PropertyValueFactory<>("izenburua")  // llama a getIzenburua() automáticamente
```

En este proyecto se usa **siempre la lambda con `SimpleStringProperty`** porque permite transformar el valor antes de mostrarlo (por ejemplo, `getEgoeraTestua()` convierte el enum en texto legible).

---

### 3.3 Cargar y actualizar datos en la tabla

**Código real de InbentarioController:**
```java
private List<Artikulua> guztiak;  // lista completa (para el filtrado)

private void kargatu() {
    guztiak = ArtikuluaDAO.getGuztiak();   // consulta a la BD
    taula.getItems().setAll(guztiak);      // pone los datos en la tabla
    desaktibatiBotoiak();
}

private void erakutsiDatuak(List<Artikulua> datuak) {
    taula.getItems().setAll(datuak);       // reemplaza lo que hay
}
```

**`taula.getItems()`** devuelve la `ObservableList` interna de la tabla.  
**`.setAll(lista)`** vacía la lista actual y añade todos los elementos de la nueva — la tabla se actualiza automáticamente en pantalla.

**Equivalencias:**

| Operación | Código |
|-----------|--------|
| Poner todos los datos | `taula.getItems().setAll(lista)` |
| Añadir un elemento | `taula.getItems().add(objeto)` |
| Eliminar un elemento | `taula.getItems().remove(objeto)` |
| Vaciar la tabla | `taula.getItems().clear()` |
| Obtener todos los elementos | `taula.getItems()` |

**¿Cuándo llamar a `kargatu()`?**  
Siempre que los datos de la BD cambien: al guardar un nuevo registro, al borrar, al editar. De esta forma la tabla siempre muestra el estado real de la base de datos.

---

### 3.4 Selección de filas y botones reactivos

Cuando el usuario hace clic en una fila de la tabla, se puede reaccionar a esa selección. En este proyecto los botones Editatu, Ezabatu e Irudia están **desactivados hasta que se selecciona una fila**.

**Código real de InbentarioController:**
```java
// En initialize() — escuchar cambios de selección
taula.getSelectionModel().selectedItemProperty()
    .addListener((obs, old, sel) -> {
        boolean dago = sel != null;          // ¿hay algo seleccionado?
        btnEditatu.setDisable(!dago);        // activar/desactivar botón
        btnEzabatu.setDisable(!dago);
        // Si tiene foto, también habilitar el botón de imagen
        boolean duArgazkia = dago
            && sel.getArgazkiBidea() != null
            && !sel.getArgazkiBidea().isEmpty();
        btnIrudia.setDisable(!duArgazkia);
    });
```

**Explicación:**
- `getSelectionModel()` — gestiona qué fila está seleccionada
- `selectedItemProperty()` — propiedad observable que cambia al seleccionar otra fila
- `addListener((obs, old, sel) -> {...})` — lambda que se ejecuta **cada vez** que cambia la selección
  - `obs` — la propiedad que cambió
  - `old` — el valor anterior (la fila que estaba antes seleccionada)
  - `sel` — el nuevo valor (la fila seleccionada ahora; es `null` si no hay ninguna)

**Obtener el elemento seleccionado en un botón:**
```java
@FXML
private void editatu() {
    Artikulua sel = taula.getSelectionModel().getSelectedItem();
    if (sel == null) return;   // defensa: nunca procesar si no hay selección
    // ... usar sel
}
```

---

### 3.5 Filtrado y búsqueda en tiempo real

El proyecto guarda la **lista completa** en `guztiak` y filtra sobre ella sin volver a la BD. Esto hace el filtrado instantáneo.

**Código real de InbentarioController:**
```java
@FXML
private void bilatu() {
    if (guztiak == null) return;
    String testua = txtBilaketa.getText().trim().toLowerCase();
    String katSel  = cbKategoria.getValue();
    String egSel   = cbEgoera.getValue();

    List<Artikulua> iragaziak = new ArrayList<>();
    for (Artikulua a : guztiak) {
        boolean testPasa = testua.isEmpty()
            || a.getArtikuluKodea().toLowerCase().contains(testua)
            || a.getIzenburua().toLowerCase().contains(testua)
            || a.getDeskribapenaSegurua().toLowerCase().contains(testua);

        boolean katPasa = katSel == null
            || katSel.equals("Kategoria guztiak")
            || a.getKategoriaIzena().equals(katSel);

        boolean egPasa = egSel == null
            || egSel.equals("Egoera guztiak")
            || a.getEgoeraTestua().equalsIgnoreCase(egSel);

        if (testPasa && katPasa && egPasa) {
            iragaziak.add(a);
        }
    }
    erakutsiDatuak(iragaziak);   // mostrar solo los filtrados
}

@FXML
private void garbitu() {
    txtBilaketa.clear();
    cbKategoria.getSelectionModel().selectFirst();
    cbEgoera.getSelectionModel().selectFirst();
    erakutsiDatuak(guztiak);     // volver a mostrar todos
}
```

**Patrón:** mantener siempre dos listas:
- `guztiak` — lista completa, se rellena solo al cargar/actualizar datos
- `iragaziak` — lista filtrada, se pasa a la tabla según los filtros activos

---

### 3.6 ComboBox — desplegable de opciones

`ComboBox<String>` muestra una lista desplegable. Se rellena en `initialize()`.

**Código real de InbentarioController y LangileakController:**
```java
// Rellenar con opciones fijas
cbEgoera.getItems().addAll("Egoera guztiak", "Biltegian", "Itzulita", "Iraungita");
cbEgoera.getSelectionModel().selectFirst();   // seleccionar el primero por defecto

// Rellenar con datos de la BD (LangileakController)
cbRola.getItems().add("Rol guztiak");
List<String[]> rolak = LangileaDAO.getRolak();
for (String[] r : rolak) {
    cbRola.getItems().add(r[1]);  // r[1] es el nombre del rol
}
cbRola.getSelectionModel().selectFirst();

// Leer el valor seleccionado
String katSel = cbKategoria.getValue();
if (katSel.equals("Kategoria guztiak")) { ... }

// Volver al primero (al limpiar filtros)
cbKategoria.getSelectionModel().selectFirst();
```

**Relleno dinámico desde los propios datos** (InbentarioController):
```java
private void beteteKategoriaCombo() {
    cbKategoria.getItems().clear();
    cbKategoria.getItems().add("Kategoria guztiak");
    for (Artikulua a : guztiak) {
        String kat = a.getKategoriaIzena();
        if (!kat.equals("—") && !cbKategoria.getItems().contains(kat)) {
            cbKategoria.getItems().add(kat);  // solo añadir si no está ya
        }
    }
    cbKategoria.getSelectionModel().selectFirst();
}
```

---

### 3.7 Ventanas modales y popups

Una ventana **modal** bloquea la ventana padre hasta que se cierra. Se usa en este proyecto para editar un artículo o ver su foto sin perder el contexto del inventario.

**Código real de InbentarioController — abrir popup de imagen:**
```java
@FXML
private void ikusiIrudia() {
    Artikulua sel = taula.getSelectionModel().getSelectedItem();
    if (sel == null || sel.getArgazkiBidea() == null) return;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/IrudiaPopup.fxml"));
    Parent root = loader.load();

    // Pasar datos al controlador del popup
    IrudiaPopupController ctrl = loader.getController();
    ctrl.kargatu(sel);

    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);  // bloquea toda la app
    stage.setTitle(sel.getArtikuluKodea() + " — argazkia");
    stage.setScene(new Scene(root));
    stage.show();
}
```

**Código real de LangileakController — editar con `showAndWait`:**
```java
@FXML
public void langileaEditatu() {
    Langilea sel = taula.getSelectionModel().getSelectedItem();
    if (sel == null) return;

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LangileaEditu.fxml"));
    Parent root = loader.load();

    LangileaEdituController ctrl = loader.getController();
    ctrl.setLangilea(sel);
    ctrl.setOnUpdateCallback(() -> kargatu());  // al cerrar, recargar tabla

    Stage stage = new Stage();
    stage.initOwner(taula.getScene().getWindow());    // ventana padre
    stage.initModality(Modality.WINDOW_MODAL);        // bloquea solo la ventana padre
    stage.setTitle("Langilea Editatu");
    stage.setScene(new Scene(root));
    stage.showAndWait();   // espera a que se cierre antes de continuar
}
```

**Diferencias entre tipos de modalidad:**

| `Modality` | Comportamiento |
|-----------|---------------|
| `NONE` | No bloquea nada (ventana normal) |
| `WINDOW_MODAL` | Bloquea solo la ventana padre (`initOwner`) |
| `APPLICATION_MODAL` | Bloquea toda la aplicación |

**`show()` vs `showAndWait()`:**
- `show()` — abre la ventana y continúa ejecutando el código siguiente
- `showAndWait()` — abre la ventana y **espera** a que se cierre; el código siguiente no se ejecuta hasta entonces

---

### 3.8 Diálogos — Alert, TextInputDialog, Confirmation

Los diálogos son ventanas emergentes predefinidas de JavaFX para interactuar con el usuario.

**Alert de confirmación antes de borrar** (código real de InbentarioController):
```java
@FXML
private void ezabatu() {
    Artikulua sel = taula.getSelectionModel().getSelectedItem();
    if (sel == null) return;

    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Ezabatu");
    confirm.setHeaderText("Artikulua ezabatu: " + sel.getArtikuluKodea());
    confirm.setContentText("Ziur zaude? Eragiketa hau ezin da desegin.");
    Optional<ButtonType> resp = confirm.showAndWait();

    if (resp.isPresent() && resp.get() == ButtonType.OK) {
        boolean ok = ArtikuluaDAO.ezabatu(sel.getArtikuluKodea());
        if (ok) {
            XMLExportazioa.exportatu();
            kargatu();
        } else {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setHeaderText("Ezin da ezabatu");
            err.setContentText("Artikuluak emanaldia edo mugimendua dauka.");
            err.showAndWait();
        }
    }
}
```

**TextInputDialog para editar texto** (código real de KategoriakController):
```java
private void editatuKategoria(Kategoria k) {
    TextInputDialog dlg = new TextInputDialog(k.getIzena()); // texto inicial
    dlg.setTitle("Kategoria editatu");
    dlg.setHeaderText(null);
    dlg.setContentText("Kategoriaren izena:");
    dlg.initOwner(vboxKategoriak.getScene().getWindow());

    Optional<String> result = dlg.showAndWait();
    result.ifPresent(izena -> {         // solo si el usuario pulsó OK
        String trimmed = izena.trim();
        if (!trimmed.isEmpty()) {
            KategoriaDAO.aldatuIzena(k.getKategoriaId(), trimmed);
            kargatu();
        }
    });
}
```

**Tipos de Alert y cuándo usarlos:**

| `AlertType` | Icono | Cuándo usarlo |
|-------------|-------|---------------|
| `INFORMATION` | ℹ | Confirmar que algo fue bien |
| `WARNING` | ⚠ | Aviso de algo sospechoso |
| `ERROR` | ✖ | Informar de un fallo |
| `CONFIRMATION` | ? | Pedir confirmación antes de una acción destructiva |

**`Optional<ButtonType>` — ¿por qué?**  
`showAndWait()` devuelve un `Optional` porque el usuario puede cerrar el diálogo sin pulsar ningún botón. El `Optional` obliga a gestionar ese caso:
```java
// Forma 1 — con isPresent()
Optional<ButtonType> resp = confirm.showAndWait();
if (resp.isPresent() && resp.get() == ButtonType.OK) { ... }

// Forma 2 — con ifPresent() (más compacta)
confirm.showAndWait().ifPresent(btn -> {
    if (btn == ButtonType.OK) { ... }
});
```

---

### 3.9 UI dinámica sin TableView — tarjetas con VBox y HBox

No todas las vistas usan `TableView`. Las secciones de categorías y reclamaciones construyen su UI **en tiempo de ejecución** creando nodos Java directamente (sin FXML). Esto da más control visual pero requiere más código.

**Código real de KategoriakController — generar tarjetas:**
```java
private HBox sortuTxartela(Kategoria k, int kopurua) {
    // Parte de información (izquierda)
    VBox info = new VBox(4);                         // VBox con gap de 4px
    Label lblIzena = new Label(k.getIzena());
    lblIzena.getStyleClass().addAll("font-bold", "text-dark");

    Label lblKop = new Label(kopurua + " artikulu");
    lblKop.getStyleClass().add("text-muted");

    info.getChildren().addAll(lblIzena, lblKop);
    HBox.setHgrow(info, Priority.ALWAYS);            // que ocupe todo el espacio libre

    // Botones (derecha)
    Button btnEdita = new Button("Editatu");
    btnEdita.getStyleClass().add("btn-outline");
    btnEdita.setOnAction(e -> editatuKategoria(k));  // lambda con la acción

    Button btnEzabatu = new Button("Ezabatu");
    btnEzabatu.getStyleClass().add("btn-danger");
    btnEzabatu.setOnAction(e -> ezabatuKategoria(k));

    HBox botoiak = new HBox(8, btnEdita, btnEzabatu); // HBox con gap 8px

    // Tarjeta completa
    HBox txartela = new HBox(15);
    txartela.getStyleClass().add("card");
    txartela.setPadding(new Insets(15));
    txartela.getChildren().addAll(info, botoiak);

    return txartela;
}
```

**Cómo se añaden las tarjetas al VBox del FXML:**
```java
private void kargatu() {
    vboxKategoriak.getChildren().clear();   // limpiar tarjetas anteriores

    List<Kategoria> kategoriak = KategoriaDAO.getGuztiak();
    HBox row = null;
    int idx = 0;

    for (Kategoria k : kategoriak) {
        if (idx % 3 == 0) {                // nueva fila cada 3 tarjetas
            row = new HBox(20);
            vboxKategoriak.getChildren().add(row);
        }
        HBox txartela = sortuTxartela(k, getKopurua(k));
        HBox.setHgrow(txartela, Priority.ALWAYS);
        row.getChildren().add(txartela);
        idx++;
    }
}
```

**Lista de reclamaciones con clic** (código real de ErreklamazioakController):
```java
private VBox sortuListaItem(Erreklamazioa err, boolean aktiboa) {
    Label lblIzena = new Label(err.getJabeIzena() + " " + err.getJabeAbizena());
    lblIzena.getStyleClass().addAll("font-bold", "text-dark");

    Label lblDesk = new Label(err.getDeskribapenBilatua());
    lblDesk.setMaxWidth(240);
    lblDesk.setWrapText(false);

    VBox item = new VBox(lblIzena, lblDesk);
    item.getStyleClass().add(aktiboa ? "list-item-active" : "list-item");

    // Añadir listener de clic directamente al nodo
    item.setOnMouseClicked(e -> hautatu(err, item));

    return item;
}
```

**Cambiar el estilo de la tarjeta seleccionada:**
```java
private void hautatu(Erreklamazioa err, VBox item) {
    // Quitar resaltado del anterior
    if (itemHautatua != null) {
        itemHautatua.getStyleClass().removeAll("list-item-active");
        itemHautatua.getStyleClass().add("list-item");
    }
    // Resaltar el nuevo
    itemHautatua = item;
    item.getStyleClass().removeAll("list-item");
    item.getStyleClass().add("list-item-active");

    hautatua = err;
    // Actualizar panel de detalle con los datos del seleccionado
    lblJabeIzena.setText(err.getJabeIzena() + " " + err.getJabeAbizena());
    lblKontaktua.setText(err.getJabeTelefonoa() + " · " + err.getJabeEmaila());
}
```

---

### 3.10 Layouts dinámicos — VBox, HBox, StackPane, Region

Estos contenedores son la base de cualquier UI construida por código:

| Contenedor | Comportamiento | Uso en el proyecto |
|-----------|---------------|-------------------|
| `VBox` | Apila hijos **verticalmente** | Lista de reclamaciones, panel de detalle |
| `HBox` | Apila hijos **horizontalmente** | Fila de botones, tarjeta con info + botones |
| `StackPane` | Apila hijos **uno encima del otro** | `contentArea` para cambiar paneles |
| `Region` | Nodo invisible que ocupa espacio | Separador flexible entre elementos |

**`HBox.setHgrow(nodo, Priority.ALWAYS)`:**  
Le dice al `HBox` que ese nodo ocupe todo el espacio horizontal disponible. Se usa para empujar los botones a la derecha de la tarjeta:
```java
HBox.setHgrow(info, Priority.ALWAYS);   // info se expande
// → [info .........info] [btn1] [btn2]
```

**`Region` como separador flexible:**
```java
Region spacer = new Region();
HBox.setHgrow(spacer, Priority.ALWAYS);
HBox header = new HBox(lblId, spacer, badge);
// → [E-001     ............     IREKIA]
```

**`Insets` — márgenes internos:**
```java
txartela.setPadding(new Insets(15));          // 15px en todos los lados
VBox.setMargin(lblData, new Insets(4, 0, 0, 0)); // top=4, right=0, bottom=0, left=0
```

---

### 3.11 Visibilidad y gestión de nodos en tiempo de ejecución

Los botones de acción de una reclamación (Ebatzi, Baztertu, Ireki Berriz) son mutuamente excluyentes. Se muestran u ocultan según el estado.

**Código real de ErreklamazioakController:**
```java
boolean irekita = "irekita".equals(egoera);

btnEbatzi.setVisible(irekita);     // ¿se ve?
btnEbatzi.setManaged(irekita);     // ¿ocupa espacio en el layout?
btnBaztertu.setVisible(irekita);
btnBaztertu.setManaged(irekita);

btnIrekiBerriz.setVisible(!irekita);
btnIrekiBerriz.setManaged(!irekita);
```

**`setVisible` vs `setManaged`:**

| Método | El nodo se ve | El nodo ocupa espacio |
|--------|--------------|----------------------|
| `setVisible(true)` | Sí | Sí |
| `setVisible(false)` | No | **Sí** (hueco invisible) |
| `setVisible(false)` + `setManaged(false)` | No | **No** (los otros nodos se recolocan) |

Siempre que ocultes un nodo que no debe dejar hueco, usa **ambos** juntos.

---

### 3.12 Callbacks entre controladores

Cuando se abre una ventana secundaria (editar un empleado, por ejemplo), al cerrarla hay que refrescar la tabla de la ventana padre. Se resuelve pasando un **callback** (función) al controlador hijo.

**Código real de LangileakController:**
```java
// Controlador padre — pasa una función al hijo
LangileaEdituController ctrl = loader.getController();
ctrl.setLangilea(sel);
ctrl.setOnUpdateCallback(() -> kargatu());   // "cuando acabes, llama a kargatu()"

stage.showAndWait();
// kargatu() se ejecutará cuando el hijo llame al callback
```

**En el controlador hijo (LangileaEdituController):**
```java
private Runnable onUpdateCallback;

public void setOnUpdateCallback(Runnable callback) {
    this.onUpdateCallback = callback;
}

// Al guardar los cambios:
private void gorde() {
    boolean ok = LangileaDAO.eguneratu(langilea);
    if (ok && onUpdateCallback != null) {
        onUpdateCallback.run();   // ejecuta kargatu() del padre
    }
    stage.close();
}
```

**`Runnable`** es una interfaz de Java que representa una tarea sin parámetros ni valor de retorno. Con lambda se escribe simplemente `() -> kargatu()`.

**Patrón equivalente en ArtikuluaEdituController** — misma idea con una referencia de método:
```java
// InbentarioController pasa this::kargatu
ctrl.kargatu(sel, this::kargatu);
// this::kargatu es equivalente a () -> this.kargatu()
```

---

## 4. Arquitectura del proyecto

```
┌─────────────────────────────────────────────────────────┐
│                    Docker Network                        │
│                                                         │
│  ┌──────────────┐    ┌──────────────┐    ┌───────────┐ │
│  │  java-app    │    │     db       │    │   web     │ │
│  │  (JavaFX)    │───▶│  (MariaDB)   │    │  (Nginx)  │ │
│  │              │    │              │    │           │ │
│  │  Puerto X11  │    │  Puerto 3306 │    │ Puerto 80 │ │
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
         │                              │
    Puerto 8000                    Pantalla
    (navegador)                    (JavaFX GUI)
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

---

## 5. Base de datos

### 5.1 Tablas

La base de datos `erronka_galduak` tiene **12 tablas principales**:

| Tabla | Descripción |
|-------|-------------|
| `ROLA` | Roles del sistema (Administratzailea, Udaltzaina, Erregistratzailea) |
| `LANGILEA_ROLA` | Tabla intermedia para registrar la fecha de asignación de roles a empleados |
| `LANGILEA` | Empleados municipales con contraseña hasheada |
| `KATEGORIA` | Categorías de objetos (Accesorios, Gafas, Llaves, Otros) |
| `KOKALEKUA` | Ubicaciones físicas de almacenamiento (armario + balda) |
| `HARTZAILEA` | Base abstracta para quien recoge el objeto |
| `JABEA` | Persona física que reclama un objeto (con NAN) |
| `ERAKUNDEA` | Organización que reclama un objeto (con IFZ) |
| `ARTIKULUA` | Objetos perdidos del inventario |
| `ERREKLAMAZIOA` | Reclamaciones de ciudadanos |
| `JAKINARAZPENA` | Notificaciones enviadas a los reclamantes |
| `EMANALDIA` | Registro de entrega de objetos a sus dueños |
| `MUGIMENDUA` | Auditoría completa de todos los movimientos |
| `AURKITZAILEA` | Datos de quien encontró el objeto |

**Jerarquía de HARTZAILEA (herencia en base de datos):**

```
HARTZAILEA (abstracto)
├── JABEA (persona física - NAN)
└── ERAKUNDEA (organización - IFZ)
```

Se implementa con tablas separadas que comparten la clave primaria `id_hartzaile`.

**Estados de un artículo (columna `egoera` en BD):**

```
'aurkitua'   → objeto en almacén, disponible
'bueltatua'  → devuelto a su dueño
'iraungita'  → plazo de custodia expirado
'dohantzan'  → donado a una organización
```

**Estados de una reclamación (columna `errek_egoera` en BD):**

```
'irekita'    → reclamación pendiente de resolver
'ebatzita'   → se encontró match y se entregó
'baztertuta' → reclamación rechazada
```

### 5.2 Roles y usuarios de base de datos

Los **roles de base de datos** son una función avanzada que permite definir conjuntos de permisos y asignarlos a usuarios. Esto es diferente a los roles de aplicación (que son los del campo `rola` en la tabla `LANGILEA`).

| Rol BD | Permisos |
|--------|---------|
| `admin_rola` | Todos los privilegios sobre todas las tablas |
| `udaltzain_rola` | Operaciones completas en datos operativos. Acceso de solo lectura a `LANGILEA`, `ROLA` y `LANGILEA_ROLA` |
| `bezero_rola` | Acceso público: consulta de artículos y categorías. Permiso para insertar nuevas reclamaciones y datos de contacto |

| Usuario BD | Contraseña | Rol | Usado por |
|-----------|-----------|-----|----------|
| `admin` | `admin123` | `admin_rola` | Administradores |
| `udaltzain1`, `udaltzain2` | `udal123` | `udaltzain_rola` | Empleados |
| `admin` | `admin123` | — | Conexión de la app Java |
| `bezero_web` | `bezeropw` | `bezero_rola` | Portal web (solo lectura) |

### 5.3 Triggers

Los **triggers** son código SQL que se ejecuta **automáticamente** cuando ocurre un evento en una tabla (INSERT, UPDATE, DELETE). No se llaman desde Java — la base de datos los invoca sola.

**Triggers del proyecto:**

**1. `trg_historial_estado_artikulua`**  
Se activa cuando cambia el estado de un artículo. Inserta automáticamente una fila en `MUGIMENDUA` (auditoría).
```sql
-- Cuando un artículo pasa de BILTEGIAN a ITZULITA, 
-- se crea solo el registro de auditoría
AFTER UPDATE ON ARTIKULUA
FOR EACH ROW
IF OLD.egoera <> NEW.egoera THEN
    INSERT INTO MUGIMENDUA ...
END IF;
```

**2. `trg_validar_fechas_ins` y `trg_validar_fechas_upd`**  
Validan que la fecha de caducidad sea posterior a la fecha de entrada.
```sql
BEFORE INSERT ON ARTIKULUA
FOR EACH ROW
IF NEW.iraungitze_data <= NEW.sarrera_data THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Data baliogabea';
END IF;
```

**3. `trg_emanaldia_eguneratu_artikulua`**  
Cuando se crea una entrega (`EMANALDIA`), actualiza automáticamente el estado del artículo a `ITZULITA`.
```sql
AFTER INSERT ON EMANALDIA
FOR EACH ROW
UPDATE ARTIKULUA SET egoera = 'ITZULITA' WHERE id_artikulu = NEW.id_artikulu;
```

**4. `trg_blokeatu_bueltatuak`**  
Impide modificar artículos que ya han sido devueltos.
```sql
BEFORE UPDATE ON ARTIKULUA
FOR EACH ROW
IF OLD.egoera = 'ITZULITA' THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ezin da aldatu itzulitako artikulua';
END IF;
```

### 5.4 Datos iniciales

El fichero `03-seed.sql` inserta datos de prueba:
- 3 roles, 3 empleados (admin + 2 udaltzain)
- 4 categorías y una malla completa de ubicaciones (Armarios A-G, baldas 1-6)
- 2 artículos de ejemplo (cartera + gafas)
- 3 reclamaciones de muestra

### 5.5 Procedimientos almacenados

El sistema delega lógica compleja en la base de datos mediante procedimientos:

- **`sp_iraungitakoak_kudeatu`**: Automatiza el cambio de estado a `iraungita` para todos los objetos cuya `iraungitze_data` sea anterior a la fecha actual.
- **`sp_erreklamazioa_ebatzi`**: Gestiona la resolución de una reclamación de forma atómica. Dentro de una **transacción**, marca la reclamación como `ebatzita` y genera automáticamente el registro de entrega (`EMANALDIA`) con los datos del reclamante asociado.

---

---

## 6. Aplicación Java (escritorio)

### 6.1 Patrones de diseño usados

Estos patrones son soluciones estándar a problemas comunes de diseño software:

**MVC (Model-View-Controller):**
- **Model**: clases en `model/` (Artikulua, Langilea, Erreklamazioa...)
- **View**: ficheros `.fxml` en `view/`
- **Controller**: clases en `controller/` (InbentarioController, LoginController...)

**DAO (Data Access Object):**
Cada entidad tiene su propio DAO que centraliza todas las operaciones de base de datos. El controlador no escribe SQL directamente — llama al DAO.
```
InbentarioController → ArtikuluaDAO → MariaDB
```

**Singleton:**  
Garantiza que solo exista **una instancia** de una clase en toda la aplicación.
- `DBKonexioa`: una sola conexión a la BD compartida por toda la app
- `Sesio`: un único estado de sesión de usuario
- `LogKudeatzailea`: un único sistema de logging

**Clase estática pura (`BiltegiLokala`):**  
`BiltegiLokala` es completamente estática — todos sus métodos son `static` y no existe ninguna instancia. Centraliza toda la lógica de almacén local en métodos de clase directamente accesibles.

**Strategy (Modo Online/Offline):**  
Los DAOs comprueban `ModoKudeatzailea.isOffline()` y delegan a `BiltegiLokala` (almacén local estático) o a la BD según corresponda. El controlador no sabe en qué modo está.

**Herencia:**  
`Hartzailea` (abstracto) → `Jabea` y `Erakundea`  
`Langilea` → `Administratzailea`

### 6.2 Capa de modelos

Ubicación: `java-app/src/main/java/model/`

| Clase | Campos clave | Descripción |
|-------|-------------|-------------|
| `Langilea` | `langileId`, `izena`, `abizena`, `erabiltzailea`, `pasahitzaHash`, `rola` | Empleado municipal |
| `Administratzailea` | (hereda de Langilea) | Empleado con permisos de admin |
| `Artikulua` | `artikuluKodea`, `izenburua`, `egoera`, `kategoria`, `kokalekua`, `iraungitzeData` | Objeto perdido |
| `Erreklamazioa` | `erreklamazioData`, `egoera`, `hartzailea`, `kategoria` | Reclamación de ciudadano |
| `Emanaldia` | `emateData`, `dokumentuBidea`, `artikulua`, `hartzailea` | Entrega de objeto |
| `Hartzailea` | `telefonoa`, `emaila`, `helbidea` | Receptor abstracto |
| `Jabea` | `nan`, `izena`, `abizena` | Persona física |
| `Erakundea` | `ifz`, `izenFiskala`, `erakundeMota` | Organización |
| `Kategoria` | `kategoriaId`, `izena` | Categoría de objeto |
| `Kokalekua` | `armairua`, `apala`, `bhaDa`, `artikuluKopurua` | Ubicación física |
| `Aurkitzailea` | `izena`, `telefonoa`, `aurkitzekokalekua` | Quien encontró el objeto |
| `Jakinarazpena` | `bidalketa_data`, `kanala`, `mezua`, `irakurria` | Notificación al reclamante |

**Enumeraciones (Enum):**  
Las enumeraciones son tipos con un conjunto fijo de valores posibles.

```java
// EgoeraArtikulua.java
public enum EgoeraArtikulua {
    BILTEGIAN, BHA_N_GORDETA, ITZULITA, IRAUNGITA, DOHANTZAN
}
```

Otras enumeraciones clave:
- **`EgoeraErreklamazioa`**: `IREKITA`, `EBATZITA`, `BAZTERTUTA`.
- **`MugimenduMota`**: `SARRERA`, `BARNE_MUGIMENDUA`, `IRTEERA_JABEA`, `IRTEERA_ERAKUNDEA`, `IRAUNGITZE_ALERTA`.
- **`Kanala`**: `SMS`, `EMAIL`, `APP`.

**Lógica de Negocio en los Modelos:**

**`Jakinarazpena.java` — Sistema de Avisos:**  
Este modelo gestiona la comunicación con el ciudadano. Genera automáticamente un mensaje cuando se detecta una coincidencia entre una reclamación y un objeto del inventario.
```java
public Jakinarazpena(Erreklamazioa errek, Artikulua art, Kanala kanala) {
    this.mezua = "Zure erreklamazioarekin bat datorren artikulua aurkitu da: " + art.getIzenburua();
    // ...
}
```

Otras clases contienen lógica propia del dominio:
- **`Artikulua.kalkulatuIraungitzea()`**: Implementa la regla municipal de los **2 años (730 días)** para el cálculo automático de la fecha de caducidad desde el momento de la entrada.
- **`Erreklamazioa.bilatuBateragarriak()`**: Contiene un algoritmo de búsqueda por palabras clave que fragmenta la descripción buscada y la compara con los artículos disponibles en el inventario para sugerir coincidencias automáticas.
- **`Artikulua.getEgoeraTestua()`**: Centraliza la traducción de los estados internos del sistema a etiquetas legibles para el usuario en la interfaz.

```java

// Uso:
Artikulua art = new Artikulua();
art.setEgoera(EgoeraArtikulua.BILTEGIAN);

if (art.getEgoera() == EgoeraArtikulua.ITZULITA) { ... }
```

**DTOs (Data Transfer Objects):**  
Clases simples que transportan datos entre capas, sin lógica de negocio:
- `AzkenMugimendua`: último movimiento para mostrar en el panel
- `MugimenduLerroa`: fila de la tabla de auditoría
- `KategoriaKopurua`: estadística por categoría para gráficos

### 6.3 Capa DAO

Ubicación: `java-app/src/main/java/dao/`

Cada DAO contiene métodos estáticos que ejecutan las consultas SQL y devuelven objetos del modelo.

**Patrón estándar de un método DAO:**

```java
public static List<Artikulua> getGuztiak() {
    // 1. Comprobar modo offline
    if (ModoKudeatzailea.isOffline()) {
        return BiltegiLokala.getArtikuluak();  // almacén local estático
    }

    // 2. Conectar y ejecutar SQL
    List<Artikulua> lista = new ArrayList<>();
    String sql = "SELECT * FROM ARTIKULUA ORDER BY sarrera_data DESC";
    
    try (Connection con = DBKonexioa.getKonexioa();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        
        while (rs.next()) {
            Artikulua art = new Artikulua(
                rs.getString("id_artikulua"),
                rs.getString("a_izena"),
                rs.getString("a_deskribapena"),
                null, null,
                rs.getDate("sarrera_data"),
                rs.getString("argazkia")
            );
            // ...
            lista.add(art);
        }
    } catch (SQLException e) {
        LogKudeatzailea.lortu(ArtikuluaDAO.class).severe("Error: " + e.getMessage());
    }
    
    return lista;
}
```

**DAOs del proyecto:**

| DAO | Responsabilidad |
|-----|----------------|
| `LangileaDAO` | Autenticación, CRUD de empleados |
| `ArtikuluaDAO` | CRUD de artículos, generación de códigos |
| `KategoriaDAO` | CRUD de categorías |
| `KokalekuaDAO` | CRUD de ubicaciones |
| `ErreklamazioaDAO` | Gestión de reclamaciones |
| `EmanaldiaDAO` | Registro de entregas |
| `MugimenduDAO` | Consultas de auditoría |
| `EstadistikaDAO` | Estadísticas para el panel |
| `AurkitzaileaDAO` | Datos de quien encontró el objeto |
| `BackupDAO` | Exportación/backup de la BD |

### 6.3.1 Lógica avanzada y Seguridad en DAOs

Los DAOs implementan lógica crítica que garantiza la integridad y seguridad del sistema:

**Gestión de Transacciones (`EmanaldiaDAO`):**  
Al formalizar una entrega, se requiere actualizar múltiples tablas (`HARTZAILEA`, `JABEA`/`ERAKUNDEA`, `EMANALDIA`, `MUGIMENDUA`). Para evitar datos inconsistentes, se desactivan los commits automáticos y se gestiona manualmente la transacción:
```java
con.setAutoCommit(false);
// ... operaciones de guardado ...
con.commit(); // Solo si todo ha ido bien
```

**Seguridad de Acceso (`LangileaDAO`):**  
Implementa la autenticación utilizando **BCrypt**. El DAO recupera el hash de la base de datos y utiliza `BCrypt.checkpw` para validar la identidad sin exponer la contraseña real en ninguna capa.

**Automatización del Ciclo de Vida (`ArtikuluaDAO`):**  
- **Generación de códigos:** Crea identificadores con formato `G-NNN-AA` (ej. G-001-26).
- **Cálculo de caducidad:** Al insertar un artículo, calcula automáticamente la fecha de expiración sumando **730 días** (2 años) a la fecha de entrada.
- **Mantenimiento:** El método `iraungituakEguneratu` ejecuta un `UPDATE` masivo para marcar como `iraungita` los objetos cuya fecha actual supere la de caducidad.

**Exportación de Datos (`BackupDAO`):**  
Permite generar un script SQL completo recorriendo las tablas del sistema, escapando caracteres especiales para garantizar que el archivo resultante sea ejecutable.

**Delegación Offline:**  
Todos los métodos incluyen una comprobación de `ModoKudeatzailea.isOffline()`. Si no hay conexión, la petición se redirige a `BiltegiLokala`, haciendo que la aplicación sea resiliente a fallos de red.

### 6.4 Capa de utilidades

Ubicación: `java-app/src/main/java/utils/`  
Esta capa contiene clases de apoyo que centralizan la lógica transversal (configuración, UI, seguridad, persistencia XML).

| Utilidad | Función Principal |
| :--- | :--- |
| **`AppConfig`** | Centraliza las rutas de archivos. Prioriza variables de entorno sobre `application.properties`. |
| **`DBKonexioa`** | Singleton para la conexión JDBC. Maneja la reconexión automática si el socket se cierra. |
| **`Sesio`** | Almacena de forma estática el objeto `Langilea` logueado y sus privilegios de administrador. |
| **`UIKudeatzailea`** | **Motor de UI**: Carga FXMLs, gestiona el sistema de "Toasts" (mensajes temporales) y crea "Overlays" (modales ligeros) sin abrir ventanas nuevas. |
| **`XMLExportazioa`** | Genera el catálogo para la web. Incluye validación por **Regex** (`G-\d{3}-\d{2}`) y lanza `XMLPatroiException` si los datos son corruptos. |
| **`XMLInportazioa`** | Lee el XML para actualizar estados masivamente en la base de datos (p. ej. sincronizar cambios desde el portal web). |
| **`ModoKudeatzailea`** | Detecta la salud de la conexión DB al arranque y decide si la app opera en modo Online u Offline. |
| **`LogKudeatzailea`** | Configura la rotación de archivos de log y gestiona el archivo `insert_log.txt` para auditoría física. |

#### 6.4.1 Sistema de Notificaciones (Toasts)
Para evitar interrumpir al usuario con constantes diálogos de alerta, `UIKudeatzailea` implementa un sistema de **Toasts**:
- Se inyecta una `Label` animada en el `StackPane` principal.
- Utiliza un `PauseTransition` de 3 segundos antes de eliminarse automáticamente.
- Colores semánticos: verde para éxito (`#DEF7EC`) y rojo para errores (`#FDE8E8`).

#### 6.4.2 Exportación XML Segura
La clase `XMLExportazioa` no solo genera texto; garantiza que el XML sea válido para Nginx y el Portal Web:
1. **Validación**: Comprueba que el código del artículo y el título no contengan caracteres de control.
2. **Escapado**: El método `esc()` convierte caracteres reservados (`&`, `<`, `>`, `"`) en entidades XML seguras.
3. **Portabilidad**: Convierte rutas de archivos locales en nombres de archivos relativos para que las imágenes se vean correctamente en el navegador.

---

## 8. Modo offline y fichero `.dat`

El modo offline es una característica de resiliencia que permite trabajar en situaciones de red inestable o caída del servidor de base de datos.

### 8.1 Funcionamiento y Sincronización
El sistema se basa en `BiltegiLokala`, que actúa como una base de datos en memoria y en disco (`store.dat`).

**El ciclo de vida es el siguiente:**
1. **Detección**: `ModoKudeatzailea.detektatu()` intenta un `ping` a la base de datos.
2. **Modo Online**: 
   - Se descarga todo el contenido de la DB a `BiltegiLokala`.
   - Se procesa la **Cola de Sincronización**: si había cambios hechos en offline, se suben a la DB.
3. **Modo Offline**:
   - Las peticiones de los DAOs se redirigen a `BiltegiLokala`.
   - **Cola de Eragiketak**: Cada `INSERT`, `UPDATE` o `DELETE` se guarda en una lista de `ItxaronEragiketa` serializable para ser procesada cuando vuelva la conexión.

### 8.2 ¿Qué es un fichero `.dat` y la serialización?

**Serialización** es el proceso de convertir un objeto Java (con todos sus campos y los objetos que contiene) en una secuencia de bytes que se puede guardar en disco. En este proyecto, guardamos la clase `DatuakPoltsa`.

```java
private static class DatuakPoltsa implements Serializable {
    private static final long serialVersionUID = 2L;
    List<Langilea> langileak = new ArrayList<>();
    List<Artikulua> artikuluak = new ArrayList<>();
    List<ItxaronEragiketa> itxaronEragiketak = new ArrayList<>(); // Operaciones pendientes
    // ...
}
```

---

## 9. Sistema de logs

La aplicación implementa dos niveles de registro gestionados por `LogKudeatzailea`:

1. **Logs de Aplicación (`app-N.log`)**:
   - Utiliza `java.util.logging`.
   - Configura **rotación automática**: Máximo 5MB por archivo, manteniendo hasta 3 archivos históricos.
   - Filtra logs por paquetes (`controller.*`, `dao.*`, etc.) para evitar ruido de librerías externas.

2. **Log de Operaciones (`insert_log.txt`)**:
   - Registro físico independiente en la carpeta de exportación.
   - Registra cada operación de inserción con marca de tiempo, tabla afectada e ID del registro.
   - Formato: `[2026-05-13 14:23:01] INSERT INTO ARTIKULUA | ID: G-092-26 | OK`.

    public static Connection getKonexioa() throws SQLException {
        if (konexioa == null || konexioa.isClosed()) {
            // Leer URL de env var o properties
            String url = System.getenv("DB_URL");
            if (url == null) url = properties.getProperty("db.url");
            konexioa = DriverManager.getConnection(url, user, pass);
        }
        return konexioa;
    }
}
```

**`Sesio.java` — Sesión del usuario:**  
Almacena el empleado que ha iniciado sesión. Al ser campos estáticos, son accesibles desde cualquier clase sin pasar el objeto por parámetro.

```java
// Iniciar sesión
Sesio.hasiera(langilea, langilea.isAdmin());

// Obtener el usuario actual desde cualquier controller
Langilea actual = Sesio.getLangilea();
boolean esAdmin = Sesio.isAdmin();

// Cerrar sesión
Sesio.itxi();
```

**`UIKudeatzailea.java` — Helper de interfaz:**

```java
// Mostrar un alert de error
UIKudeatzailea.erakutsiErrorea("Error", "Ez da aurkitu");

// Cargar un FXML en el StackPane registrado (cambio de sección)
UIKudeatzailea.kargatuPanela("/view/Inbentario.fxml");

// Cargar un nodo ya construido (con FXMLLoader manual)
UIKudeatzailea.kargatuPanela(nodoa);

// Cambiar de ventana completa
UIKudeatzailea.aldatuLeihoa(boton, "/view/MainLayout.fxml", true);
```

**`ModoKudeatzailea.java` — Detección online/offline:**

```java
// Se ejecuta al arrancar la app
public static void detektatu() {
    try {
        DBKonexioa.getKonexioa();
        offline = false;  // BD disponible
    } catch (SQLException e) {
        offline = true;   // Sin BD — activar modo offline
        BiltegiLokala.kargatu();   // carga datos del store.dat si existe
    }
}
```

**`AppConfig.java` — Rutas de ficheros:**  
Centraliza todas las rutas de archivos para que sean fáciles de cambiar.

```java
AppConfig.getExportBidea()          // → partekatutako_datuak/ (base compartida)
AppConfig.getIrudiakBidea()         // → partekatutako_datuak/irudiak/ (para el XML/web)
AppConfig.getXmlBidea()             // → partekatutako_datuak/artikuluak.xml
AppConfig.getArtikuluIrudiakBidea() // → artikulu_irudiak/ (fotos originales de app Java)
AppConfig.getSinaduraBidea()        // → partekatutako_datuak/sinadurak/ (firmas PDF)
```

La prioridad de configuración es: **variable de entorno → `application.properties` → valor por defecto**. Esto permite que la app funcione igual en Docker (con `env vars`) y en local (con el fichero `.properties`).

**`XMLExportazioa.java` — Exportación a XML:**  
Cuando se añade o modifica un artículo, se llama a este método para actualizar el XML que lee el portal web. Incluye validación con regex de campos obligatorios, lanzando `XMLPatroiException` si algún valor no cumple el patrón esperado.

```java
// Genera el fichero partekatutako_datuak/artikuluak.xml
XMLExportazioa.exportatu();
```

### 6.5 Capa de controladores

Ubicación: `java-app/src/main/java/controller/`

Los controladores gestionan la lógica de la interfaz y la comunicación con la capa DAO.

### 6.5.1 Lógica de Navegación y Sesión

La aplicación utiliza un sistema de **Layouts** (`MainLayout` y `AdminLayout`) con un `StackPane` central donde se cargan dinámicamente las diferentes vistas mediante `UIKudeatzailea`.

- **`LoginController`**: Punto de entrada que valida las credenciales y redirige según el rol (Admin o Staff).
- **`MainController` / `AdminController`**: Gestionan el menú lateral, resaltando el botón activo mediante la clase CSS `.nav-item-active`. El Admin puede volver a la interfaz de usuario normal sin cerrar sesión.

### 6.5.2 Gestión de Archivos e Imágenes

Implementada en `ErregistroaController` y `ArtikuluaEditatuController`:
1.  **Copia Física**: Al seleccionar una imagen o un documento de firma (PDF/JPG), el sistema genera un nombre único basado en el *timestamp* (`img_171558...jpg`).
2.  **Rutas Configurables**: Utiliza `AppConfig` para determinar las rutas de destino (`artikulu_irudiak/` o `sinadurak/`).
3.  **Persistencia**: En la base de datos solo se guarda el nombre del archivo, permitiendo que la aplicación sea portable entre diferentes entornos siempre que se mantenga el volumen de Docker.

### 6.5.3 Lógica de Negocio en Controladores Clave

| Controlador | Funcionalidad Destacada |
| :--- | :--- |
| **`ErreklamazioakController`** | Genera una interfaz dinámica de tarjetas (`VBox`) en lugar de tablas. Implementa un sistema de **matching automático** para sugerir artículos que coincidan con la descripción de la reclamación. |
| **`IraungitakoakController`** | Gestiona la "Mugarria 5": detecta artículos con más de 2 años en el almacén y permite iniciar el proceso de entrega directamente a favor del aurkitzailea o una donación. |
| **`AuditoriaController`** | Permite visualizar el historial de movimientos y exportar los datos a un archivo **CSV** utilizando `PrintWriter` con codificación UTF-8. |
| **`InbentarioController`** | Incluye la lógica de **Importación/Exportación XML**. La exportación actualiza el portal web, mientras que la importación permite sincronizar estados masivamente desde un archivo externo. |
| **`EmanaldiaController`** | Formulario dual. Permite alternar entre la entrega a una persona física (DNI) o a una entidad (IFZ). Gestiona la copia del documento de identidad escaneado o firma. |
| **`PanelaController`** | Dashboard principal. Al iniciarse, fuerza la ejecución de `ArtikuluaDAO.iraungituakEguneratu()` para asegurar que las estadísticas mostradas sean reales. |

### 6.5.4 Comunicación entre Ventanas (Callbacks)

Para actualizar las tablas después de una edición, se utiliza un patrón de **callbacks** o recargas delegadas:

1. El controlador principal (`LangileakController`) abre la vista de edición.
2. Tras cerrar el modal o cambiar el panel, se invoca de nuevo al método `kargatu()` del padre para reflejar los cambios realizados en la base de datos inmediatamente.

```java
// Ejemplo de carga en UIKudeatzailea usada por los controladores
public static void kargatuPanela(Node nodoa) {
    edukiGunea.getChildren().setAll(nodoa);
}
```

### 6.6 Capa de vistas (FXML)

Ubicación: `java-app/src/main/java/view/`

**Estructura de un FXML:**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?import javafx.scene.layout.*?>
<?import javafx.scene.control.*?>

<!-- fx:controller enlaza este FXML con su controlador Java -->
<BorderPane xmlns:fx="http://javafx.com/fxml"
            fx:controller="controller.InbentarioController">
    
    <top>
        <TextField fx:id="txtBilaketa" promptText="Bilatu..."/>
    </top>
    
    <center>
        <!-- fx:id debe coincidir con el @FXML del controlador -->
        <TableView fx:id="tablaArtikuluak">
            <columns>
                <TableColumn fx:id="colIzena" text="Izena"/>
                <TableColumn fx:id="colEgoera" text="Egoera"/>
            </columns>
        </TableView>
    </center>
    
    <bottom>
        <!-- onAction llama al método del controlador -->
        <Button text="Berria" onAction="#artikuluBerria"/>
    </bottom>
</BorderPane>
```

**Ficheros FXML del proyecto:**

| FXML | Pantalla |
|------|---------|
| `login.fxml` | Pantalla de inicio de sesión |
| `MainLayout.fxml` | Layout principal (empleados) |
| `AdminLayout.fxml` | Layout de administrador |
| `Panela.fxml` | Dashboard con estadísticas |
| `Inbentario.fxml` | Tabla de artículos (inventario) |
| `Erregistroa.fxml` | Formulario para registrar objeto |
| `Erreklamazioak.fxml` | Lista de reclamaciones |
| `ErreklamazioaBerria.fxml` | Nueva reclamación |
| `Emanaldia.fxml` | Formulario de entrega |
| `GalduDabenak.fxml` | Artículos próximos a caducar |
| `Kategoriak.fxml` | Lista de categorías |
| `KategoriaBerria.fxml` | Nueva categoría |
| `Kokalekuak.fxml` | Lista de ubicaciones |
| `KokalekuaBerria.fxml` | Nueva ubicación |
| `Langileak.fxml` | Lista de empleados |
| `LangileBerria.fxml` | Nuevo empleado |
| `LangileaEditu.fxml` | Editar empleado |
| `ArtikuluaEditu.fxml` | Editar artículo |
| `Auditoria.fxml` | Historial de auditoría |
| `IrudiaPopup.fxml` | Ventana emergente para ver foto |

---

## 7. Frontend web

Ubicación: `frontend/`

El portal web permite a los ciudadanos ver los objetos perdidos encontrados por el ayuntamiento y presentar reclamaciones.

**Tecnologías:**

- **HTML5 / CSS3 / JavaScript (Vanilla)**: No se usan frameworks externos para mantener la ligereza y sostenibilidad.
- **Nginx**: Servidor web que expone el portal en el puerto 8000.

### 7.1 Internacionalización (i18n)

El sistema de traducción es dinámico y no requiere recargar la página:
- **Diccionarios**: Definidos en `main.js` (`dictIndex`) y `objektu-zerrenda.js` (`dictCatalog`) para Euskera y Castellano.
- **Atributo `data-i18n`**: Los elementos HTML marcados con este atributo se actualizan automáticamente al cambiar el idioma.
- **Persistencia**: Se utiliza `localStorage` para recordar la preferencia de idioma del ciudadano en futuras visitas.

### 7.2 Modo Oscuro y Diseño Responsive

- **Arquitectura CSS**: Basada en variables (`:root`) que cambian según la clase `.dark-theme`.
- **Responsive**: El uso de **CSS Grid** en el catálogo y **Flexbox** en la navegación garantiza que la web sea 100% funcional en móviles.
- **Accesibilidad**: Se han definido fuentes locales (Inter y Montserrat) con estrategias de `font-display: swap` para optimizar la carga.

### 7.3 Gestión de Datos XML en Cliente

El portal es capaz de leer y procesar datos estructurados sin una base de datos directa:
- **Carga dinámica**: JavaScript (`fetch`) lee el archivo `artikuluak.xml` exportado por la App Java.
- **Filtrado en cliente**: El buscador y el selector de categorías filtran los nodos del DOM en tiempo real basándose en los datos del XML.
- **Transformación**: Se incluye un archivo `artikuluak.xsl` para transformar el inventario en una tabla HTML estéticamente agradable si se visualiza el XML directamente.

### 7.4 Validación y Generación de Reclamaciones

Una de las piezas clave es la lógica del formulario de reclamación:
1. **Validación de Identidad**: Se aplica una expresión regular (`/^[0-9]{8}[a-zA-Z]$/`) para asegurar que el DNI introducido es válido antes de procesar nada.
2. **Generación de XML**: El sistema construye un documento XML en memoria con los datos del formulario.
3. **Descarga de comprobante**: Se utiliza un objeto `Blob` para forzar la descarga de la reclamación en formato XML, permitiendo al ciudadano guardarla o enviarla por otros medios.

---

## 8. Modo offline y fichero `.dat`

El modo offline permite que la aplicación funcione **sin conexión a la base de datos** (por ejemplo, si el servidor MariaDB no está disponible).

---

## 9. Sistema de logs

**¿Qué es el logging?**  
El logging (registro de eventos) es el proceso de guardar en ficheros lo que hace la aplicación: errores, operaciones importantes, advertencias. Permite diagnosticar problemas en producción.

---

## 10. Flujo completo de datos

**Datos del inventario:**  
La web lee el fichero `datuak/artikuluak.xml` (generado por la app Java) mediante JavaScript y XPath para mostrar los artículos sin necesidad de una API.

**Transformaciones XSLT:**  
El directorio `xslt/` contiene transformaciones que convierten el XML de artículos en HTML para su presentación web directa.

### 7.1 Multi-idioma dinámico (i18n)
El portal web incluye un sistema de internacionalización desarrollado en Vanilla JS. Utiliza diccionarios JSON (`dictIndex` y `dictCatalog`) con las traducciones en Euskera (EU) y Castellano (ES).
- La preferencia del usuario se guarda persistentemente en `localStorage.getItem('appLang')`.
- Al cambiar de idioma, se buscan los elementos con el atributo `data-i18n` y se actualizan dinámicamente sin recargar la página.

### 7.2 Modo Oscuro (Dark Theme)
El diseño soporta un modo claro y oscuro gestionado mediante una clase `.dark-theme` en el `<body>` y variables CSS (`:root`).
- La selección se guarda en `localStorage.getItem('appTheme')`.
- Cambia automáticamente las paletas de colores principales (`--blue-900`, `--gray-50`, `--white`, etc.) garantizando el contraste y la accesibilidad visual.

### 7.3 Validaciones y Generación XML en cliente
Antes de permitir la descarga del archivo XML de una reclamación, JavaScript intercepta el formulario:
1. **Validación estricta:** Comprueba mediante Expresiones Regulares (Regex) que el DNI/NAN del reclamante tenga un formato válido de 8 números y 1 letra (`/^[0-9]{8}[a-zA-Z]$/`).
2. **Sanitización:** Escapa caracteres especiales (`&`, `<`, `>`) para prevenir inyecciones y no corromper la estructura XML.
3. **Generación Local:** Crea el documento XML y lanza su descarga automáticamente mediante un `Blob`, generando un nombre de archivo dinámico basado en el nombre y apellidos del reclamante.


## 8. Modo offline y fichero `.dat`

El modo offline permite que la aplicación funcione **sin conexión a la base de datos** (por ejemplo, si el servidor MariaDB no está disponible).

---

### 8.1 ¿Qué es un fichero `.dat` y la serialización?

**Serialización** es el proceso de convertir un objeto Java (con todos sus campos y los objetos que contiene) en una secuencia de bytes que se puede guardar en disco. **Deserialización** es el proceso inverso: leer esos bytes y reconstruir el objeto exactamente como estaba.

En Java, para que un objeto sea serializable basta con que su clase implemente `Serializable`:

```java
private static class DatuakPoltsa implements Serializable {
    private static final long serialVersionUID = 1L;  // identificador de versión
    List<Langilea> langileak = new ArrayList<>();
    List<Artikulua> artikuluak = new ArrayList<>();
    // ... resto de listas
}
```

**`serialVersionUID`**: número que Java usa para verificar que la clase que intentas deserializar es compatible con la que se usó al serializar. Si cambias la clase (añades/quitas campos) y no actualizas este número, Java lanzará una excepción al intentar leer un `.dat` antiguo.

**Guardar a disco:**
```java
// ObjectOutputStream envuelve un FileOutputStream
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("store.dat"))) {
    oos.writeObject(poltsa);  // convierte el objeto completo a bytes
}
```

**Leer desde disco:**
```java
// ObjectInputStream envuelve un FileInputStream
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("store.dat"))) {
    DatuakPoltsa poltsa = (DatuakPoltsa) ois.readObject();  // reconstruye el objeto
}
```

El fichero resultante (`store.dat`) **no es legible por humanos** — es binario. Para inspeccionarlo hay que deserializarlo desde Java o usar Adminer/MariaDB directamente.

**Ubicación del fichero:**
```
~/.erronka-bermeo/store.dat       (Linux/Mac — por defecto)
C:\Users\TU_USUARIO\.erronka-bermeo\store.dat  (Windows)
$OFFLINE_DATA_PATH/store.dat      (si la variable de entorno está definida)
```

---

### 8.2 `BiltegiLokala` — cómo funciona

`BiltegiLokala` es la clase que gestiona el almacén local. Tiene dos roles:
1. **Caché**: copia de los datos reales de la BD, siempre actualizada
2. **Fallback offline**: los DAOs la usan si la BD no está disponible

**Ciclo de vida completo:**

```
App arranca
    ├── Hay BD (online)
    │     ① Carga store.dat en memoria (si existe)
    │     ② sincronizatuDBtik() → sobreescribe con datos reales de BD
    │     ③ Guarda store.dat actualizado
    │
    └── Sin BD (offline)
          ① Carga store.dat (datos de la última sesión online)
          └── Si store.dat no existe → listas vacías (login fallará)

App se usa normalmente
    └── Cada operación (añadir, editar, borrar) → actualiza store.dat inmediatamente

App se cierra → Main.stop() (automático en JavaFX)
    ├── Online → sincronizatuDBtik() → store.dat con los datos más recientes
    └── Offline → gorde() → conserva los datos que había
```

**Cómo los DAOs usan BiltegiLokala de forma transparente:**

```java
// En cada DAO — el controlador nunca sabe en qué modo está
public static List<Artikulua> getGuztiak() {
    if (ModoKudeatzailea.isOffline()) {
        return BiltegiLokala.getArtikuluak();  // clase estática, sin getInstance()
    }
    // Código SQL normal para la BD
    String sql = "SELECT * FROM ARTIKULUA ...";
    // ...
}
```

**Implementación como clase estática:**  
`BiltegiLokala` no usa `getInstance()` ni patrón Singleton. Todos sus campos son `static` (un único mapa en la JVM) y se accede directamente: `BiltegiLokala.getArtikuluak()`, `BiltegiLokala.login(user, pass)`, etc. Esto simplifica el acceso desde cualquier DAO.

---

### 8.3 Cómo implementar serialización en tus propias clases

Si en un proyecto propio quisieras implementar algo similar:

**Paso 1** — La clase que quieres serializar debe implementar `Serializable`:
```java
import java.io.Serializable;

public class MiClase implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nombre;
    private int edad;
    // getters y setters normales
}
```

**Paso 2** — Cualquier clase anidada o campo de objeto también debe ser `Serializable`:
```java
// Si MiClase tiene un campo de tipo OtraClase, OtraClase también debe ser Serializable
private OtraClase dato;  // OtraClase implements Serializable
```

**Paso 3** — Guardar:
```java
try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("fichero.dat"))) {
    oos.writeObject(miObjeto);
} catch (IOException e) {
    // gestionar error
}
```

**Paso 4** — Cargar:
```java
try (ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream("fichero.dat"))) {
    MiClase miObjeto = (MiClase) ois.readObject();
} catch (IOException | ClassNotFoundException e) {
    // gestionar error
}
```

**Cuándo actualizar `serialVersionUID`**: cada vez que cambies la estructura de la clase (añadas/elimines/renombres campos). Si no lo actualizas, los `.dat` antiguos seguirán siendo compatibles — pero podrían tener campos que ya no existen, lo que puede causar comportamientos inesperados.

---

## 9. Sistema de logs

**¿Qué es el logging?**  
El logging (registro de eventos) es el proceso de guardar en ficheros lo que hace la aplicación: errores, operaciones importantes, advertencias. Permite diagnosticar problemas en producción.

**`LogKudeatzailea.java`:**

```java
// Obtener el logger de una clase
Logger log = LogKudeatzailea.lortu(ArtikuluaDAO.class);

// Niveles de log (de menos a más grave)
log.fine("Mensaje de debug detallado");
log.info("Operación completada correctamente");
log.warning("Algo sospechoso pero no crítico");
log.severe("Error grave");
```

**Configuración:**
- Ficheros de log: `~/.erronka-bermeo/logs/erronka-bermeo.log`
- Rotación automática: máx. 5MB por fichero, 3 ficheros (los más viejos se borran)
- Formato: `[2025-03-15 14:23:01] [SEVERE] ArtikuluaDAO: Error al insertar artículo`

---

## 12. Flujo completo de datos

**Registro de un objeto perdido:**

```
1. Udaltzain encuentra objeto
2. Abre ErregistroaController → rellena formulario
3. Selecciona categoría (KategoriaDAO.getGuztiak())
4. Selecciona ubicación (KokalekuaDAO.getGuztiak())
5. Opcionalmente sube foto → se guarda en artikulu_irudiak/
6. Pulsa "Gorde" → ArtikuluaDAO.gehitu(...)
   ├── Genera código G-NNN-YY
   ├── INSERT INTO ARTIKULUA ...
   └── TRIGGER crea entrada en MUGIMENDUA (auditoría)
7. XMLExportazioa.exportatu() → actualiza artikuluak.xml
8. El portal web muestra el objeto al ciudadano
```

**Reclamación y entrega:**

```
1. Ciudadano ve el objeto en la web → llama al ayuntamiento
2. Udaltzain abre ErreklamazioaBerriController
3. Registra datos del reclamante (NAN/nombre)
4. Erreklamazioa.bilatuBateragarriak() → busca artículos que coincidan
5. Si hay match → EmanaldiaController
   ├── Registra la entrega (INSERT INTO EMANALDIA)
   ├── TRIGGER actualiza ARTIKULUA.egoera = 'ITZULITA'
   └── TRIGGER registra en MUGIMENDUA
6. Se marca la reclamación como EBATZITA
```

---

## 10. Empaquetado y Distribución

Para facilitar el uso de la aplicación en equipos que no tienen Java instalado, el proyecto incluye un script de empaquetado nativo para Windows (`build-windows.bat`).

**Proceso de creación del ejecutable:**
1. **Maven**: Se genera el *fat JAR* con todas las dependencias.
2. **jpackage**: Esta herramienta de Java 21 empaqueta el JAR junto con una **Runtime de Java (JRE) mínima** y las librerías de JavaFX necesarias.
3. **Resultado**: Se genera un archivo `.exe` en `target/Galdutakoak-1.0.exe` que funciona de forma independiente.

```batch
jpackage --input target --main-jar app.jar --type exe --name Galdutakoak ...
```

---

## 11. Cómo arrancar el proyecto

**Requisitos previos:**
- Docker Desktop instalado y ejecutándose
- En Linux: ejecutar `xhost +local:docker` para permitir la GUI

**Scripts de automatización:**  
Se han incluido scripts para simplificar el ciclo de vida de los contenedores:

| Script | Función |
|--------|---------|
| `start-linux.sh` / `start-windows.bat` | Verifica Docker, construye imágenes y arranca el entorno. |
| `stop-linux.sh` / `stop-windows.bat` | Detiene los contenedores de forma segura. |

**Ejecución en Linux:**
```bash
./start-linux.sh
```

# O manualmente:
xhost +local:docker
docker compose up -d
```

**Arrancar (Windows):**
1. Ejecutar `start-windows.bat` (No se requiere servidor X11 externo gracias a noVNC).

**URLs de acceso:**

| Servicio | URL |
|---------|-----|
| Portal web ciudadanos | http://localhost:8000 |
| Adminer (gestión BD) | http://localhost:8081 |
| Aplicación JavaFX (noVNC) | http://localhost:6080/vnc.html?autoconnect=1&resize=scale |

**Credenciales de la aplicación Java:**

| Usuario | Contraseña | Rol |
|---------|-----------|-----|
| `admin` | `admin123` | Administratzailea (acceso total) |
| `udaltzain1` | `udal123` | Udaltzaina |
| `udaltzain2` | `udal123` | Udaltzaina |

**Parar:**

```bash
./stop-linux.sh
# O:
docker compose down
```

**Limpiar datos (borrar BD):**

```bash
docker compose down -v  # -v elimina también los volúmenes (datos de la BD)
```

---

## 13. Javadoc — documentación en euskera

Toda la base de código está documentada con Javadoc **íntegramente en euskera** (Basque). Esto incluye clases, métodos, parámetros (`@param`) y valores de retorno (`@return`).

**Cobertura de Javadoc:**

| Paquete | Clases documentadas |
|---------|-------------------|
| `controller` | AdminController, AdminPanelaController, ArtikuluaEdituController, ErreklamazioaBerriController, ErreklamazioakController, InbentarioController, IraungitakoakController, IrudiaPopupController, KategoriaBerriController, KategoriakController, KokalekuaBerriController, KokalekuakController, LangileBerriController, LangileaEdituController, LangileakController, LoginController, MainController |
| `dao` | ArtikuluaDAO, AurkitzaileaDAO, BackupDAO, EmanaldiaDAO, ErreklamazioaDAO, EstadistikaDAO, KategoriaDAO, KokalekuaDAO, LangileaDAO, MugimenduDAO |
| `model` | Artikulua, Aurkitzailea, AzkenMugimendua, Emanaldia, Erakundea, Erreklamazioa, Hartzailea, Jabea, Jakinarazpena, Kategoria, KategoriaKopurua, Kokalekua, Langilea, MugimenduLerroa |
| `utils` | AppConfig, BiltegiLokala, DBKonexioa, LogKudeatzailea, ModoKudeatzailea, Sesio, UIKudeatzailea, XMLExportazioa, XMLInportazioa |

**Ejemplo de Javadoc en euskera:**

```java
/**
 * Datu-basean erregistratutako langile baten autentifikazioa egiten du.
 * BCrypt bidez konparatzen du sartutako pasahitza gordetako hash-arekin.
 *
 * @param erabiltzailea Erabiltzailearen izena (username)
 * @param pasahitza     Sartu nahi diren testu garbiko pasahitza
 * @return  Langilea objektua autentifikazioa arrakastatsua bada, null bestela
 */
public static Langilea login(String erabiltzailea, String pasahitza) { ... }
```

**Generar la documentación HTML:**
```bash
cd java-app
mvn javadoc:javadoc
# Resultado en: target/site/apidocs/index.html
```

---

*Documentación actualizada el 2026-05-13 para el proyecto ERRONKA-BERMEO v1.0.*
