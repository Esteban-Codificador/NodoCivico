package com.nodocivico.app

import android.app.Application
import com.nodocivico.app.data.local.AppDatabase
import com.nodocivico.app.data.repository.ReportRepository
import com.nodocivico.app.network.RetrofitClient

class NodoCivicoApp : Application() {

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(this)
    }

    val reportRepository: ReportRepository by lazy {
        ReportRepository(database, RetrofitClient.api)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
