package com.nodocivico.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nodocivico.app.NodoCivicoApp
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.model.ReportStatus
import com.nodocivico.app.data.repository.ReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReportDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository =
        (application as NodoCivicoApp).reportRepository

    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report

    fun load(id: Int) {
        viewModelScope.launch {
            _report.value = repository.getById(id)
        }
    }

    fun cycleStatus() {
        val current = _report.value ?: return
        val next = when (current.status) {
            ReportStatus.OPEN        -> ReportStatus.IN_PROGRESS
            ReportStatus.IN_PROGRESS -> ReportStatus.RESOLVED
            ReportStatus.RESOLVED    -> ReportStatus.CLOSED
            ReportStatus.CLOSED      -> ReportStatus.OPEN
        }
        viewModelScope.launch {
            val updated = repository.updateStatus(current, next)
            _report.value = updated
        }
    }

    fun delete() {
        val current = _report.value ?: return
        viewModelScope.launch {
            repository.delete(current)
        }
    }
}
