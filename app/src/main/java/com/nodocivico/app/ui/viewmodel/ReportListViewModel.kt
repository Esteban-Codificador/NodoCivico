package com.nodocivico.app.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.nodocivico.app.NodoCivicoApp
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.repository.ReportRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed class ReportListUiState {
    object Loading : ReportListUiState()
    object Empty : ReportListUiState()
    data class Success(val reports: List<Report>) : ReportListUiState()
}

class ReportListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository =
        (application as NodoCivicoApp).reportRepository

    val uiState: StateFlow<ReportListUiState> = repository.allReports
        .map { list ->
            if (list.isEmpty()) ReportListUiState.Empty
            else ReportListUiState.Success(list)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReportListUiState.Loading
        )
}