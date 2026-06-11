package com.nodocivico.app.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.nodocivico.app.R
import com.nodocivico.app.data.model.ReportStatus
import com.nodocivico.app.databinding.FragmentReportDetailBinding
import com.nodocivico.app.ui.viewmodel.ReportDetailViewModel
import com.nodocivico.app.util.DateFormats
import kotlinx.coroutines.launch

class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportDetailViewModel by viewModels()
    private val args: ReportDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.load(args.reportId)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.report.collect { report ->
                    if (report == null) return@collect

                    binding.tvIcon.text = report.title.firstOrNull()?.uppercase() ?: "·"
                    binding.tvTitle.text = report.title
                    binding.tvMetaLocation.text =
                        "Ubicación: ${report.location} · Fecha: ${DateFormats.shortDate(report.createdAtMillis)}"
                    binding.tvMetaObservation.text =
                        "Prioridad: ${report.priority.label} · Sync pendiente: ${report.pendingSync}"

                    binding.tvStatus.text = report.status.label
                    val (bgRes, fgRes) = when (report.status) {
                        ReportStatus.OPEN        -> R.drawable.bg_status_open      to R.color.status_open_fg
                        ReportStatus.IN_PROGRESS -> R.drawable.bg_status_progress  to R.color.status_progress_fg
                        ReportStatus.RESOLVED    -> R.drawable.bg_status_closed    to R.color.status_closed_fg
                        ReportStatus.CLOSED      -> R.drawable.bg_status_closed    to R.color.status_closed_fg
                    }
                    binding.tvStatus.background =
                        ContextCompat.getDrawable(requireContext(), bgRes)
                    binding.tvStatus.setTextColor(
                        ContextCompat.getColor(requireContext(), fgRes)
                    )
                }
            }
        }

        binding.btnUpdateStatus.setOnClickListener {
            viewModel.cycleStatus()
        }

        binding.btnEdit.setOnClickListener {
            val action = ReportDetailFragmentDirections
                .actionReportDetailFragmentToEditReportFragment(args.reportId)
            findNavController().navigate(action)
        }

        binding.btnReminder.setOnClickListener {
            findNavController().navigate(
                R.id.action_reportDetailFragment_to_calendarRemindersFragment
            )
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}