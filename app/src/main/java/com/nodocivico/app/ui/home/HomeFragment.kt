package com.nodocivico.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nodocivico.app.R
import com.nodocivico.app.databinding.FragmentHomeBinding
import com.nodocivico.app.ui.viewmodel.ReportListViewModel
import com.nodocivico.app.ui.viewmodel.ReportListUiState
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ReportListUiState.Loading -> {
                            binding.tvStatTotal.text = "…"
                            binding.tvStatPending.text = "…"
                            binding.tvStatSynced.text = "…"
                        }
                        is ReportListUiState.Empty -> {
                            binding.tvStatTotal.text = "0"
                            binding.tvStatPending.text = "0"
                            binding.tvStatSynced.text = "0"
                        }
                        is ReportListUiState.Success -> {
                            val reports = state.reports
                            binding.tvStatTotal.text = reports.size.toString()
                            binding.tvStatPending.text =
                                reports.count { it.pendingSync }.toString()
                            binding.tvStatSynced.text =
                                reports.count { !it.pendingSync }.toString()
                        }
                    }
                }
            }
        }

        binding.btnNewReport.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createReportFragment)
        }
        binding.btnViewReports.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_reportListFragment)
        }
        binding.btnSync.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_syncStatusFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}