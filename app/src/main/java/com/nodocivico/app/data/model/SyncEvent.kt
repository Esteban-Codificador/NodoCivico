package com.nodocivico.app.data.model

/**
 * Evento de sincronización entre la app y la API REST.
 *
 * Cada vez que se crea o modifica un reporte sin internet, se genera un
 * SyncEvent con la acción pendiente. Cuando vuelve la conexión, el
 * ConnectivityReceiver (Entregable 3) consume estos eventos para empujarlos
 * al servidor en orden.
 *
 * En el Entregable 1 esta clase queda declarada para que el panel de
 * sincronización pueda mostrar la cuenta de pendientes (con datos de muestra).
 */
data class SyncEvent(
    val id: String,
    val reportId: String,
    val action: Action,
    val createdAtMillis: Long,
    val isSent: Boolean = false
) {
    enum class Action { CREATE, UPDATE, STATUS_CHANGE, DELETE }
}
