# Nodo Cívico

> Aplicación móvil Android para **reportes vecinales, seguimiento comunitario y acciones coordinadas de microgestión urbana**.
>
> Los habitantes de un barrio, conjunto residencial, campus o sector pueden registrar incidencias reales de su entorno (alumbrado, aseo, seguridad, servicios públicos), consultar su estado, dar seguimiento a cada caso y coordinar acciones derivadas.

![Entregable](https://img.shields.io/badge/Entregable-1%20de%203-2563eb)
![Plataforma](https://img.shields.io/badge/Android-API%2026%2B-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-7F52FF?logo=kotlin&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.4-02303A?logo=gradle&logoColor=white)
![Uso](https://img.shields.io/badge/Uso-Académico-lightgrey)

---

## Contexto académico

Proyecto final de la asignatura **Aplicaciones Móviles**.

- **Institución / programa:** Unidades Tecnologicas de Santander
- **Docente:** GENNER ANDRÉS CARRILLO RUEDA
- **Periodo:** TERCER CORTE
- **Equipo:** Ver sección de los integrantes

> El documento del proyecto está en `docs/Proyecto_Final_de_Aplicaciones_Móviles.pdf`.

---

## Estado del proyecto

| Entregable | Valor | Estado                |
|---|---|-----------------------|
| **1.** Base funcional y navegación | 20 % | **Entregado ahorita** |
| **2.** Persistencia y lógica offline (Room, CRUD) | 35 % | Planeado              |
| **3.** Integración, receivers y cierre (API REST, BroadcastReceiver) | 45 % | Planeado              |

---

## Cobertura del Entregable 1

El enunciado exige cinco entregables concretos para esta primera fase. El mapeo es directo:

| Requisito de la rúbrica | Cómo se cumple en este repositorio |
|---|---|
| Proyecto Android creado y organizado | Estructura por capas en `app/src/main/java/com/nodocivico/app/` con paquetes separados `ui/`, `data/`, `api/`, `util/`. |
| Definición del dominio y entidades principales | `data/model/` contiene `Report`, `Category`, `User`, `Reminder`, `FollowUp`, `SyncEvent` y los enums `Priority` y `ReportStatus`. |
| Navegación entre fragments funcionando | `res/navigation/nav_graph.xml` + `MainActivity` con `NavHostFragment` y `BottomNavigationView`. |
| Pantalla inicial, home y al menos dos vistas funcionales enlazadas | Implementadas **7 vistas**: Splash → Home → Lista → Detalle → Crear → Sincronización → Ajustes. |
| Estructura preliminar de la interfaz | Layouts XML en `res/layout/` que reproducen el prototipo HTML de referencia con paleta unificada y componentes Material 3. |
| Primer diseño del contrato de API o esquema JSON | `docs/api_contract.md` con endpoints, schemas JSON y reglas de validación, complementado con `api/ApiContract.kt`. |

> Aunque los criterios piden "al menos dos vistas", el proyecto ya tiene **7 fragments** funcionando. Se hizo asi porque ya se deja una base solida para arrancar con el segundo entregable

---

## Stack tecnológico

- **Lenguaje:** Kotlin 1.9.22
- **Build:** Gradle 8.4 (Kotlin DSL) + Android Gradle Plugin 8.2.2
- **SDK:** `compileSdk 34` · `targetSdk 34` · `minSdk 26`
- **UI:** Material 3 (`com.google.android.material:material:1.11.0`)
- **Navegación:** AndroidX Navigation Component 2.7.7 (fragment + UI)
- **Otras dependencias activas:** `core-ktx`, `appcompat`, `fragment-ktx`, `recyclerview`, `lifecycle-viewmodel-ktx`, `core-splashscreen`
- **Dependencias previstas (Entregables 2/3):** Room, Retrofit/OkHttp, AlarmManager
- **JDK:** 17 (embedded JBR de Android Studio)

---

## Estructura del proyecto

```
NodoCivico/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/nodocivico/app/
│       │   ├── NodoCivicoApp.kt          Application class
│       │   ├── MainActivity.kt           NavHost + BottomNavigation
│       │   ├── data/model/               Entidades de dominio
│       │   ├── api/ApiContract.kt        Endpoints planeados
│       │   ├── ui/                       Fragments organizados por feature
│       │   │   ├── splash/
│       │   │   ├── home/
│       │   │   ├── reports/              List, Detail, Create, Adapter
│       │   │   ├── sync/
│       │   │   └── settings/
│       │   └── util/DateFormats.kt
│       └── res/                          Layouts, drawables, themes, navigation, menu
└── docs/
    ├── README.md                         Guía técnica de importación
    └── api_contract.md                   Contrato de la API REST
```

---

## Cómo ejecutar el proyecto

### Prerrequisitos

- **Android Studio** Iguana 2023.2.1 o superior
- **JDK 17** (viene embebido con Android Studio)
- **Android SDK 34** instalado vía `Tools → SDK Manager`
- Un emulador o dispositivo físico con **API 26+** (Android 8 o superior)

### Pasos

```bash
git clone https://github.com/Esteban-Codificador/NodoCivico.git
cd NodoCivico
```

1. Abrir Android Studio → `File → Open…` → seleccionar la carpeta raíz `NodoCivico/` (la que contiene `settings.gradle.kts`).
2. Aceptar la descarga de Gradle 8.4 cuando lo pida (primera sincronización ~2–4 min).
3. Crear/seleccionar un emulador en `Device Manager` con API ≥ 26.
4. Botón **▶ Run 'app'**.

### Solución a problemas comunes

| Problema | Acción |
|---|---|
| `SDK location not found` | Crear `local.properties` en la raíz con `sdk.dir=/ruta/al/Android/Sdk`. |
| `Gradle JDK incorrect` | `File → Settings → Build Tools → Gradle → Gradle JDK = 17 (embedded JBR)`. |
| Caché de recursos viciada | `Build → Clean Project` → `Build → Rebuild Project`. |

---

## Hoja de ruta

### Entregable 2 — Persistencia y lógica offline (35 %)

- Capa `data/local/` con `AppDatabase`, `ReportDao`, `CategoryDao`, `Converters` (Room).
- `ReportRepository` exponiendo `Flow<List<Report>>` para reactividad.
- Migración del `ReportAdapter` a `ListAdapter + DiffUtil`.
- CRUD real desde `CreateReportFragment`, `EditReportFragment` y `ReportDetailFragment`.
- Nuevas vistas: `EditReportFragment` y `CalendarRemindersFragment` (cierre del requisito de >8 vistas).
- Validación con `TextInputLayout.error` y estados vacío/carga/error en la lista.

### Entregable 3 — Integración, receivers y cierre (45 %)

- API REST en Flask siguiendo `docs/api_contract.md`.
- Cliente Retrofit + OkHttp con base URL en `BuildConfig`.
- `SyncManager` con cola de `SyncEvent` y reintentos exponenciales.
- Tres BroadcastReceiver funcionales:
  - `BootReceiver` — reprogramar `Reminder` activos al reiniciar el dispositivo.
  - `ConnectivityReceiver` — disparar `SyncManager.flush()` al recuperar red.
  - `ReminderReceiver` — mostrar notificación local al vencer un recordatorio.
- Indicador visual de conectividad en `HomeFragment` y `SyncStatusFragment`.

---

## Capturas

> Pantallas del Entregable 1 ejecutándose en dispositivo Android (API 26+, build de debug).

<table>
  <tr>
    <td align="center" width="33%">
      <img src="docs/screenshots/01-home.jpg" width="240" alt="Pantalla de inicio"/>
      <br/><sub><b>Inicio</b></sub>
      <br/><sub>Resumen general · estado offline · accesos rápidos</sub>
    </td>
    <td align="center" width="33%">
      <img src="docs/screenshots/02-reportes.jpg" width="240" alt="Lista de reportes"/>
      <br/><sub><b>Reportes recientes</b></sub>
      <br/><sub>RecyclerView con pills de estado y FAB</sub>
    </td>
    <td align="center" width="33%">
      <img src="docs/screenshots/03-nuevo-reporte.jpg" width="240" alt="Formulario de nuevo reporte"/>
      <br/><sub><b>Nuevo reporte</b></sub>
      <br/><sub>Formulario con validación · guardado offline</sub>
    </td>
  </tr>
  <tr>
    <td align="center" width="33%">
      <img src="docs/screenshots/04-sincronizacion.jpg" width="240" alt="Estado de sincronización"/>
      <br/><sub><b>Sincronización</b></sub>
      <br/><sub>Enviados · cola local · botón de reintento</sub>
    </td>
    <td align="center" width="33%">
      <img src="docs/screenshots/05-ajustes.jpg" width="240" alt="Preferencias"/>
      <br/><sub><b>Ajustes</b></sub>
      <br/><sub>Tema · notificaciones · filtro por estado</sub>
    </td>
    <td width="33%"></td>
  </tr>
</table>

> La pantalla **Detalle del reporte** se navega tocando cualquier ítem de la lista de reportes; se añadirá su captura en el Entregable 2 cuando esté conectada al CRUD real.

## Autores

| Nombre                            | Rol         | Contacto                                                    |
|-----------------------------------|-------------|-------------------------------------------------------------|
| Esteban Alberto Avila Corredor    | Estudiante  | eaavila@uts.edu.co / https://github.com/Esteban-Codificador |
| Juan Sebastian Ropero Amado       | Estuadiante | jsropero@uts.edu.co)                                        |
| Ariana Alexandra Duran Grimaldos  | Estudiante  | Aalexandraduran@uts.edu.co / https://github.com/Arianaduran25|


---