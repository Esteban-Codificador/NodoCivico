package com.nodocivico.app.data.model

/**
 * Usuario local de la aplicación.
 *
 * Para el Entregable 1 se usa una identificación simple: nombre + sector.
 * El enunciado permite "identificación local" sin necesidad de un sistema
 * de autenticación complejo. En el Entregable 3 este modelo se sincronizará
 * con la API REST mediante el endpoint POST /users.
 */
data class User(
    val id: String,
    val displayName: String,
    val sector: String,
    val isActive: Boolean = true
)
