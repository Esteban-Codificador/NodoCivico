package com.nodocivico.app.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utilidades de formato de fecha/hora para presentación.
 *
 * Mantener esta lógica centralizada evita duplicar SimpleDateFormat por toda
 * la app y facilita cambiarla cuando el equipo decida usar java.time o un
 * formateador localizado en los siguientes entregables.
 */
object DateFormats {

    private val shortDate = SimpleDateFormat("dd/MM/yyyy", Locale("es", "CO"))

    fun shortDate(timestampMillis: Long): String = shortDate.format(Date(timestampMillis))

    /**
     * Devuelve una etiqueta humana aproximada: "Hace 2 h", "Hoy", "Ayer", "07/05/2026".
     */
    fun humanRelative(timestampMillis: Long): String {
        val nowMillis = System.currentTimeMillis()
        val diff = nowMillis - timestampMillis
        val oneHour = 60L * 60 * 1000
        val oneDay = 24L * oneHour
        return when {
            diff < oneHour -> {
                val minutes = (diff / (60 * 1000)).coerceAtLeast(1)
                "Hace $minutes min"
            }
            diff < oneDay -> {
                val hours = (diff / oneHour).coerceAtLeast(1)
                "Hace $hours h"
            }
            diff < 2 * oneDay -> "Ayer"
            else -> shortDate(timestampMillis)
        }
    }
}
