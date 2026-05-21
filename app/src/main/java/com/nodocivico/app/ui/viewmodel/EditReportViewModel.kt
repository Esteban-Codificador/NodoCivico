package com.nodocivico.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nodocivico.app.NodoCivicoApp
import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository =
        (application as NodoCivicoApp).reportRepository

    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report

    private val _saveResult = MutableSharedFlow<SaveResult>()
    val saveResult: SharedFlow<SaveResult> = _saveResult

    fun load(id: Int) {
        viewModelScope.launch {
            _report.value = repository.getById(id)
        }
    }

    fun update(
        title: String,
        description: String,
        categoryId: String,
        priority: Priority,
        location: String
    ) {
        val current = _report.value ?: return
        viewModelScope.launch {
            val updated = current.copy(
                title = title,
                description = description,
                categoryId = categoryId,
                priority = priority,
                location = location,
                pendingSync = true
            )
            repository.update(updated)
            _saveResult.emit(SaveResult.Success)
        }
    }
}