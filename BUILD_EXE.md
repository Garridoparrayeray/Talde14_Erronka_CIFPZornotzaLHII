# Galdutakoak — Proceso de creación del .exe

## Requisitos previos

- **JDK 25** instalado (Eclipse Adoptium recomendado) — necesario para jpackage
- **Maven 3.9+** instalado
- **Java 21** disponible (puede ser el JRE de VS Code o un JDK propio)

---

## 1. Instalar Maven (si no está instalado)

Descargar y extraer Maven manualmente:

```powershell
Invoke-WebRequest -Uri "https://downloads.apache.org/maven/maven-3/3.9.15/binaries/apache-maven-3.9.15-bin.zip" -OutFile "$env:USERPROFILE\maven.zip"
Expand-Archive -Path "$env:USERPROFILE\maven.zip" -DestinationPath "C:\maven" -Force
```

Añadir al PATH de la sesión actual:

```powershell
$env:PATH += ";C:\maven\apache-maven-3.9.15\bin"
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot"
```

Verificar:

```powershell
mvn -version
```

---

## 2. Compilar el proyecto

Desde la carpeta `java-app/`:

```powershell
cd java-app
mvn clean package -DskipTests
```

Esto genera `target/galdutakoak-1.0-SNAPSHOT.jar`.

---

## 3. Copiar dependencias

jpackage necesita todas las dependencias en una misma carpeta:

```powershell
mvn dependency:copy-dependencies -DoutputDirectory=target/libs
Copy-Item "target\galdutakoak-1.0-SNAPSHOT.jar" "target\libs\"
```

---

## 4. Crear el ejecutable con jpackage

```powershell
& "C:\Program Files\Eclipse Adoptium\jdk-25.0.3.9-hotspot\bin\jpackage.exe" `
  --input target\libs `
  --name "Galdutakoak" `
  --main-jar galdutakoak-1.0-SNAPSHOT.jar `
  --main-class app.Launcher `
  --type app-image `
  --dest installer `
  --app-version 1.0
```

> Si ya existe la carpeta `installer\Galdutakoak` de una ejecución anterior, eliminarla primero:
> ```powershell
> Remove-Item -Recurse -Force installer\Galdutakoak
> ```

---

## 5. Resultado

Se genera la carpeta `java-app/installer/Galdutakoak/` con esta estructura:

```
Galdutakoak/
├── Galdutakoak.exe      ← ejecutable principal
├── app/                 ← JARs de la aplicación y dependencias
├── runtime/             ← JRE embebido (no necesita Java instalado)
└── partekatutako_datuak/
```

---

## 6. Distribuir y ejecutar

**Mover la carpeta a cualquier ubicación:**

```powershell
Move-Item "java-app\installer\Galdutakoak" "C:\MiApp\Galdutakoak"
```

Ejecutar simplemente con doble clic en `Galdutakoak.exe` o desde PowerShell:

```powershell
& "C:\MiApp\Galdutakoak\Galdutakoak.exe"
```

> **Importante:** hay que mover **toda la carpeta**, no solo el `.exe`.
> El ejecutable depende de `runtime/` (JRE embebido) y `app/` (JARs).
> Sin esas subcarpetas el programa no arranca.

---

## 7. Requisitos en el equipo destino

- **Ninguno** — el JRE está embebido dentro de `runtime/`
- La base de datos MariaDB debe estar accesible (o usar modo offline con `store.dat`)

---

## Notas

- `--type app-image` genera una carpeta portable sin instalador
- Para generar un instalador `.exe` (con wizard) usar `--type exe` — requiere **WiX Toolset v3** instalado desde [wixtoolset.org](https://wixtoolset.org)
- El punto de entrada es `app.Launcher` y no `app.Main` porque JavaFX requiere esta clase intermediaria cuando se ejecuta desde un fat JAR sin module-path
