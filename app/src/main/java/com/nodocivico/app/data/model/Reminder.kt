package com.nodocivico.app.data.model

/**
 * Recordatorio asociado a un reporte.
 *
 * Permite que el usuario programe un aviso local para hacer seguimiento
 * (por ejemplo: "revisar luminaria de la calle 12 mañana a las 9 a.m.").
 *
 * En el Entregable 3 estos recordatorios se enlazan con AlarmManager y un
 * ReminderReceiver que mostrará la notificación local.
 */
data class Reminder(
    val id: String,
    val reportId: String,
    val message: String,
    val triggerAtMillis: Long,
    val isActive: Boolean = true
)
