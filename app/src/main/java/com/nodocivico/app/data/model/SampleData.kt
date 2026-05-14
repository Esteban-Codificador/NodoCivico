package com.nodocivico.app.data.model

/**
 * Datos de muestra en memoria.
 *
 * Sirve para que las pantallas del Entregable 1 muestren contenido realista
 * sin necesidad de tener todavía Room ni la API conectados. En el Entregable 2
 * estos datos se reemplazan por consultas reales al [ReportRepository] sobre
 * la base de datos local.
 */
object SampleData {

    val categories: List<Category> = listOf(
        Category("cat_alumbrado", "Alumbrado", "Postes, luminarias, transformadores"),
        Category("cat_aseo", "Aseo", "Basuras, escombros, contenedores"),
        Category("cat_seguridad", "Seguridad", "Riesgos, daños, situaciones sospechosas"),
        Category("cat_servicios", "Servicios públicos", "Agua, energía, gas, internet")
    )

    val sampleReports: List<Report> = listOf(
        Report(
            id = "rep_001",
            title = "Luminaria dañada en la calle 12",
            description = "El poste de la esquina no enciende desde hace dos noches.",
            categoryId = "cat_alumbrado",
            priority = Priority.ALTA,
            status = ReportStatus.ABIERTO,
            location = "Calle 12 con carrera 7",
            createdAtMillis = System.currentTimeMillis() - 2L * 60 * 60 * 1000,
            pendingSync = true
        ),
        Report(
            id = "rep_002",
            title = "Basura acumulada en el parque central",
            description = "Bolsas acumuladas junto al kiosco; mal olor en la zona.",
            categoryId = "cat_aseo",
            priority = Priority.MEDIA,
            status = ReportStatus.EN_PROCESO,
            location = "Parque central, sector kiosco",
            createdAtMillis = System.currentTimeMillis() - 6L * 60 * 60 * 1000,
            evidenceUri = "local://evidencia/foto_basura.jpg",
            pendingSync = false
        ),
        Report(
            id = "rep_003",
            title = "Fuga de agua frente al bloque B",
            description = "Filtración continua desde el andén; ya se reportó pero sigue activa.",
            categoryId = "cat_servicios",
            priority = Priority.ALTA,
            status = ReportStatus.CERRADO,
            location = "Sector residencial, frente al bloque B",
            createdAtMillis = System.currentTimeMillis() - 60L * 60 * 1000,
            pendingSync = false
        )
    )

    fun categoryName(id: String): String =
        categories.firstOrNull { it.id == id }?.name ?: "Sin categoría"
}
