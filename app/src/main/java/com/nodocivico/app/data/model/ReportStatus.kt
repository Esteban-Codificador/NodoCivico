package com.nodocivico.app.data.model

/**
 * Estado de seguimiento de un reporte.
 *
 *  - ABIERTO: el reporte fue creado pero todavía no tiene gestión.
 *  - EN_PROCESO: alguien lo está atendiendo o se programó una acción.
 *  - CERRADO: el caso ya fue resuelto.
 *
 * El estado evoluciona dentro de la app y, en el Entregable 3, también
 * desde el servidor a través de la API REST.
 */
enum class ReportStatus(val label: String) {
    ABIERTO("Abierto"),
    EN_PROCESO("En proceso"),
    CERRADO("Cerrado");

    companion object {
        fun fromLabel(label: String): ReportStatus =
            values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: ABIERTO
    }
}
