package com.nodocivico.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nodocivico.app.NodoCivicoApp
import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.model.ReportStatus
import com.nodocivico.app.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

sealed class SaveResult {
    object Success : SaveResult()
    data class Error(val message: String) : SaveResult()
}

class CreateReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository =
        (application as NodoCivicoApp).reportRepository

    private val _saveResult = MutableSharedFlow<SaveResult>()
    val saveResult: SharedFlow<SaveResult> = _saveResult

    fun save(
        title: String,
        description: String,
        categoryId: String,
        priority: Priority,
        location: String,
        offline: Boolean
    ) {
        viewModelScope.launch {
            val report = Report(
                id = "0",
                title = title,
                description = description,
                categoryId = categoryId,
                priority = priority,
                status = ReportStatus.ABIERTO,
                location = location,
                createdAtMillis = System.currentTimeMillis(),
                pendingSync = offline
            )
            repository.insert(report)
            _saveResult.emit(SaveResult.Success)
        }
    }
}