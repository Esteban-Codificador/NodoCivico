package com.nodocivico.app.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.nodocivico.app.NodoCivicoApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConnectivityReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "ConnectivityReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if (activeNetwork != null && activeNetwork.isConnected) {
            Log.d(TAG, "Conexion detectada, iniciando sincronizacion...")
            Toast.makeText(context, "Conexion recuperada. Sincronizando...", Toast.LENGTH_SHORT).show()

            val app = context.applicationContext as? NodoCivicoApp ?: return
            val repository = app.reportRepository

            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val result = repository.sync()
                    Log.d(TAG, "Sync automatico: pushed=${result.pushed}, pulled=${result.pulled}, errors=${result.errors}")
                    if (result.pushed > 0 || result.pulled > 0) {
                        CoroutineScope(Dispatchers.Main).launch {
                            Toast.makeText(
                                context,
                                "Sincronizados: ${result.pushed} subidos, ${result.pulled} descargados",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error en sync automatico: ${e.message}")
                } finally {
                    pendingResult.finish()
                }
            }
        } else {
            Log.d(TAG, "Sin conexion a internet")
            Toast.makeText(context, "Sin conexion a internet", Toast.LENGTH_SHORT).show()
        }
    }
}
