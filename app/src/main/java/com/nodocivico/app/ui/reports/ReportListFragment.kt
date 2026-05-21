package com.nodocivico.app.ui.reports

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.nodocivico.app.R
import com.nodocivico.app.databinding.FragmentReportListBinding
import com.nodocivico.app.ui.viewmodel.ReportListUiState
import com.nodocivico.app.ui.viewmodel.ReportListViewModel
import kotlinx.coroutines.launch

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ReportAdapter { report ->
            val action = ReportListFragmentDirections
                .actionReportListFragmentToReportDetailFragment(report.id.toInt())
            findNavController().navigate(action)
        }

        binding.recyclerReports.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReports.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    when (state) {
                        is ReportListUiState.Loading -> {
                            binding.recyclerReports.visibility = View.GONE
                            binding.tvEmpty.visibility = View.GONE
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is ReportListUiState.Empty -> {
                            binding.progressBar.visibility = View.GONE
                            binding.recyclerReports.visibility = View.GONE
                            binding.tvEmpty.visibility = View.VISIBLE
                        }
                        is ReportListUiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvEmpty.visibility = View.GONE
                            binding.recyclerReports.visibility = View.VISIBLE
                            adapter.submitList(state.reports)
                        }
                    }
                }
            }
        }

        binding.fabNewReport.setOnClickListener {
            findNavController().navigate(R.id.action_reportListFragment_to_createReportFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}