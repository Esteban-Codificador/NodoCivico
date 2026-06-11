package com.nodocivico.app.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // URL base de la API. Cambiar por la URL del servidor (ngrok, emulador, etc.)
    // Para emulador Android: http://10.0.2.2:5000/
    // Para dispositivo físico con ngrok: https://abc123.ngrok-free.app/
    // Para emulador Android usa: http://10.0.2.2:5050/
    // Para dispositivo fisico en la misma red: http://<IP_DE_TU_PC>:5050/
    var baseUrl: String = "http://10.0.2.2:5050/"
        set(value) {
            field = value
            _api = null
        }

    @Volatile
    private var _api: ApiService? = null

    val api: ApiService
        get() = _api ?: synchronized(this) {
            _api ?: Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
                .also { _api = it }
        }
}
