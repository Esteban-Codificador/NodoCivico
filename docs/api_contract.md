# Contrato de API REST — Nodo Cívico

Documento de diseño del servicio HTTP que la app consumirá a partir del Entregable 3.

Cumple con el requisito del enunciado: *"Primer diseño del contrato de API o esquema JSON esperado"* (Entregable 1).

---

## Generalidades

- **Stack sugerido por el enunciado:** Flask (Python).
- **Base URL en desarrollo:** `http://10.0.2.2:5000/api/v1/` (`10.0.2.2` es el alias del host desde el emulador).
- **Formato:** JSON, `Content-Type: application/json; charset=utf-8`.
- **Codificación de fechas:** ISO 8601 UTC (`2026-05-13T14:32:00Z`).
- **Autenticación:** el enunciado dice "no requiere autenticación compleja". Se usará header `X-Client-Id: <uuid>` generado en la primera ejecución de la app y persistido en SharedPreferences.
- **Códigos de respuesta:**
  - `200 OK` — lectura exitosa.
  - `201 Created` — creación exitosa.
  - `204 No Content` — actualización/borrado exitosos sin payload.
  - `400 Bad Request` — payload inválido. Devuelve `{"error":"...","fields":{...}}`.
  - `404 Not Found` — recurso inexistente.
  - `409 Conflict` — id duplicado.
  - `500 Internal Server Error` — error no controlado del servidor.

---

## Endpoints

### Salud del servicio

`GET /api/v1/health`

Respuesta `200`:

```json
{ "status": "ok", "version": "0.1.0", "timestamp": "2026-05-13T14:32:00Z" }
```

Lo usa la app al arrancar para confirmar que la API está disponible y refrescar el indicador de conectividad.

---

### Reportes

#### Listar

`GET /api/v1/reports?status=&category_id=&priority=&page=1&size=20`

Respuesta `200`:

```json
{
  "page": 1,
  "size": 20,
  "total": 12,
  "items": [
    {
      "id": "rep_001",
      "title": "Luminaria dañada en la calle 12",
      "description": "El poste de la esquina no enciende desde hace dos noches.",
      "category_id": "cat_alumbrado",
      "priority": "alta",
      "status": "abierto",
      "location": "Calle 12 con carrera 7",
      "evidence_uri": null,
      "created_at": "2026-05-13T12:32:00Z",
      "updated_at": "2026-05-13T12:32:00Z"
    }
  ]
}
```

#### Detalle

`GET /api/v1/reports/{id}`

Respuesta `200`: objeto reporte (mismos campos que arriba).
Respuesta `404`: `{ "error": "report_not_found" }`.

#### Crear

`POST /api/v1/reports`

Cuerpo de petición:

```json
{
  "id": "rep_001",
  "title": "Luminaria dañada en la calle 12",
  "description": "El poste de la esquina no enciende.",
  "category_id": "cat_alumbrado",
  "priority": "alta",
  "status": "abierto",
  "location": "Calle 12 con carrera 7",
  "evidence_uri": null,
  "created_at": "2026-05-13T12:32:00Z"
}
```

Reglas de validación:

- `title` requerido, 5–120 caracteres.
- `description` requerido, ≥ 10 caracteres.
- `category_id` requerido, debe existir en `/categories`.
- `priority` ∈ {`baja`, `media`, `alta`}.
- `status` ∈ {`abierto`, `en_proceso`, `cerrado`}.
- `location` requerido, ≥ 3 caracteres.

Respuesta `201`: el reporte creado.
Respuesta `400`:

```json
{
  "error": "validation_failed",
  "fields": {
    "title": "min_length",
    "category_id": "unknown"
  }
}
```

#### Actualizar estado

`PUT /api/v1/reports/{id}/status`

Cuerpo:

```json
{ "status": "en_proceso", "note": "Asignado al técnico de zona." }
```

Respuesta `200`: el reporte actualizado.

#### Editar reporte completo

`PUT /api/v1/reports/{id}`

Mismo cuerpo que `POST` (sin `id`). Útil para el `EditReportFragment` del Entregable 2.

#### Eliminar

`DELETE /api/v1/reports/{id}` → `204 No Content`.

---

### Categorías

`GET /api/v1/categories`

```json
[
  { "id": "cat_alumbrado",  "name": "Alumbrado",          "description": "Postes, luminarias, transformadores" },
  { "id": "cat_aseo",       "name": "Aseo",               "description": "Basuras, escombros, contenedores" },
  { "id": "cat_seguridad",  "name": "Seguridad",          "description": "Riesgos, daños, situaciones sospechosas" },
  { "id": "cat_servicios",  "name": "Servicios públicos", "description": "Agua, energía, gas, internet" }
]
```

El servidor publica un catálogo fijo en el Entregable 3. No hay endpoint de creación para esta versión.

---

### Usuarios

#### Registrar / identificar

`POST /api/v1/users`

```json
{ "id": "user_uuid_local", "display_name": "Ana", "sector": "Bloque B" }
```

Si el `id` existe, responde `200` con el usuario actualizado.
Si no existe, lo crea y responde `201`.

---

## Convenciones de payload

- Los `id` son strings generados por la app antes de enviar al servidor. Esto permite trabajar offline-first: la app crea el `id` localmente y la API lo respeta tal cual al sincronizar.
- `created_at` se envía siempre desde el cliente. El servidor sólo lo valida.
- `updated_at` lo gestiona el servidor.
- Los enums se serializan como string en minúsculas con guión bajo: `"abierto"`, `"en_proceso"`, `"cerrado"`.

---

## Errores tratados por la app

| Código | Acción del cliente |
|---|---|
| 200/201/204 | Marcar `SyncEvent.isSent = true`. |
| 400 | Mostrar mensajes de validación; no reintenta hasta que el usuario edite. |
| 404 | Si era `PUT/DELETE`, marcar el reporte local como huérfano. |
| 409 | Reenviar con re-generación de id si el conflicto es por colisión. |
| 5xx | Reintento exponencial: 5 s, 15 s, 60 s, 5 min. |
| Sin red | El `ConnectivityReceiver` posterga el envío y guarda el evento en la cola local. |

---

## Notas sobre Flask (referencia para el Entregable 3)

Implementación mínima sugerida en Python:

- Un `app.py` con `Flask`, `flask-cors` y `sqlite3` (o `flask-sqlalchemy`).
- Estructurar por blueprints: `reports_bp`, `categories_bp`, `users_bp`.
- Servir en `0.0.0.0:5000` para que el emulador llegue por `10.0.2.2:5000`.
- Persistencia en `data.db` (SQLite) — el mismo motor que usará Room en el cliente, por simetría.

El cliente Android se configurará con `BuildConfig.API_BASE_URL` definido por `buildType`.
