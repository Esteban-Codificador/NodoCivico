package com.nodocivico.app.api

/**
 * Contrato de la API REST que se implementará en el Entregable 3.
 *
 * Este archivo no realiza llamadas todavía. Sirve como referencia de los
 * endpoints y formatos de mensaje que el equipo debe respetar tanto en el
 * cliente Android como en el servidor (Flask, según recomienda el enunciado).
 *
 * Convención general:
 *  - Base URL configurable en BuildConfig en el Entregable 3.
 *  - Content-Type: application/json en peticiones y respuestas.
 *  - Códigos: 200/201 éxito, 400 validación, 404 no encontrado, 500 servidor.
 *
 * El esquema detallado de cada endpoint está documentado en docs/api_contract.md.
 */
object ApiContract {

    const val DEFAULT_BASE_URL: String = "http://10.0.2.2:5000/api/v1/"

    object Endpoints {
        const val REPORTS: String        = "reports"
        const val REPORT_DETAIL: String  = "reports/{id}"
        const val REPORT_STATUS: String  = "reports/{id}/status"
        const val CATEGORIES: String     = "categories"
        const val USERS: String          = "users"
        const val HEALTH: String         = "health"
    }

    object Fields {
        const val ID: String         = "id"
        const val TITLE: String      = "title"
        const val DESCRIPTION: String = "description"
        const val CATEGORY_ID: String = "category_id"
        const val PRIORITY: String   = "priority"
        const val STATUS: String     = "status"
        const val LOCATION: String   = "location"
        const val CREATED_AT: String = "created_at"
    }
}
