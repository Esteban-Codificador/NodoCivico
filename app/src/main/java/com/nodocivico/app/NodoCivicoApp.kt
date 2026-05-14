package com.nodocivico.app

import android.app.Application

/**
 * Punto de arranque global de la aplicación.
 *
 * Para el Entregable 1 no se inicializa nada todavía: el objetivo es dejar
 * declarada la clase Application para que en el Entregable 2 se pueda enganchar
 * aquí la base de datos Room (singleton) y en el Entregable 3 el cliente HTTP
 * y los BroadcastReceiver dinámicos.
 */
class NodoCivicoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO Entregable 2: inicializar Room (AppDatabase)
        // TODO Entregable 3: inicializar cliente HTTP y registrar ConnectivityReceiver
    }
}
