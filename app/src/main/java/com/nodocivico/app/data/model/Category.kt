package com.nodocivico.app.data.model

/**
 * Categoría a la que pertenece un reporte (Alumbrado, Aseo, Servicios, etc.).
 *
 * Para el Entregable 1 se trabaja con una lista en memoria definida en
 * [SampleData]. En el Entregable 2 esta clase se convertirá en una @Entity
 * de Room con su propio DAO para mantener un catálogo persistente.
 */
data class Category(
    val id: String,
    val name: String,
    val description: String = ""
)
