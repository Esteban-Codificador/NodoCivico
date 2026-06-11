package com.nodocivico.app.data.model

object SampleData {

    val categories: List<Category> = listOf(
        Category("Alumbrado", "Alumbrado", "Postes, luminarias, transformadores"),
        Category("Aseo", "Aseo", "Basuras, escombros, contenedores"),
        Category("Seguridad", "Seguridad", "Riesgos, danos, situaciones sospechosas"),
        Category("Servicios publicos", "Servicios publicos", "Agua, energia, gas, internet")
    )

    val sampleReports: List<Report> = listOf(
        Report(
            id = "rep_001",
            title = "Luminaria danada en la calle 12",
            description = "El poste de la esquina no enciende desde hace dos noches.",
            categoryId = "Alumbrado",
            priority = Priority.ALTA,
            status = ReportStatus.OPEN,
            location = "Calle 12 con carrera 7",
            createdAtMillis = System.currentTimeMillis() - 2L * 60 * 60 * 1000,
            pendingSync = true
        ),
        Report(
            id = "rep_002",
            title = "Basura acumulada en el parque central",
            description = "Bolsas acumuladas junto al kiosco; mal olor en la zona.",
            categoryId = "Aseo",
            priority = Priority.MEDIA,
            status = ReportStatus.IN_PROGRESS,
            location = "Parque central, sector kiosco",
            createdAtMillis = System.currentTimeMillis() - 6L * 60 * 60 * 1000,
            pendingSync = false
        ),
        Report(
            id = "rep_003",
            title = "Fuga de agua frente al bloque B",
            description = "Filtracion continua desde el anden; ya se reporto pero sigue activa.",
            categoryId = "Servicios publicos",
            priority = Priority.ALTA,
            status = ReportStatus.CLOSED,
            location = "Sector residencial, frente al bloque B",
            createdAtMillis = System.currentTimeMillis() - 60L * 60 * 1000,
            pendingSync = false
        )
    )

    fun categoryName(id: String): String =
        categories.firstOrNull { it.id == id || it.name == id }?.name ?: id
}
