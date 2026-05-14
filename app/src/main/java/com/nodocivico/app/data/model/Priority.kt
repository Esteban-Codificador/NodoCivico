package com.nodocivico.app.data.model

/**
 * Nivel de prioridad de un reporte ciudadano.
 *
 * En el Entregable 2 este enum se persistirá como String dentro de Room
 * mediante un TypeConverter sencillo. Por ahora es sólo parte del modelo
 * de dominio en memoria.
 */
enum class Priority(val label: String) {
    BAJA("Baja"),
    MEDIA("Media"),
    ALTA("Alta");

    companion object {
        fun fromLabel(label: String): Priority =
            values().firstOrNull { it.label.equals(label, ignoreCase = true) } ?: MEDIA
    }
}
