package com.nodocivico.app.data.model

/**
 * Estado de seguimiento de un reporte.
 *
 *  - OPEN: el reporte fue creado pero todavia no tiene gestion.
 *  - IN_PROGRESS: alguien lo esta atendiendo o se programo una accion.
 *  - RESOLVED: el caso fue resuelto.
 *  - CLOSED: el caso esta cerrado definitivamente.
 */
enum class ReportStatus(val label: String) {
    OPEN("Abierto"),
    IN_PROGRESS("En proceso"),
    RESOLVED("Resuelto"),
    CLOSED("Cerrado");

    companion object {
        fun fromLabel(label: String): ReportStatus =
            values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: OPEN
    }
}
