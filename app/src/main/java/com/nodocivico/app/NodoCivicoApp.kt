package com.nodocivico.app

import android.app.Application
import com.nodocivico.app.data.local.AppDatabase
import com.nodocivico.app.data.repository.ReportRepository

class NodoCivicoApp : Application() {

    // Instancia única de la base de datos accesible desde toda la app
    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    // Repositorio accesible desde los ViewModels vía la Application
    val reportRepository: ReportRepository by lazy {
        ReportRepository(database)
    }

    override fun onCreate() {
        super.onCreate()
        // Room se inicializa de forma lazy la primera vez que se accede
        // TODO Entregable 3: inicializar cliente HTTP y ConnectivityReceiver
    }
}