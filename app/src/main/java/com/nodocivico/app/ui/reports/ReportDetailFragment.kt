package com.nodocivico.app.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.FragmentReportDetailBinding
import com.nodocivico.app.util.DateFormats

/**
 * Detalle del reporte.
 *
 * Equivale al panel "Detalle del reporte" del prototipo HTML. Para el
 * Entregable 1 se muestra siempre el primer reporte de muestra; en el
 * Entregable 2 recibirá el id por Safe Args y consultará el reporte
 * concreto en Room.
 */
class ReportDetailFragment : Fragment() {

    private var _binding: FragmentReportDetailBinding? = null
    private val binding get() = _binding!!

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

        val report = SampleData.sampleReports.first()
        binding.tvIcon.text = report.title.firstOrNull()?.uppercase() ?: "·"
        binding.tvTitle.text = report.title
        binding.tvMetaLocation.text = "Ubicación: ${report.location} · Fecha: ${DateFormats.shortDate(report.createdAtMillis)}"
        binding.tvMetaObservation.text = "Observación: caso en seguimiento con recordatorio activo."
        binding.tvStatus.text = report.status.label

        binding.btnUpdateStatus.setOnClickListener {
            Toast.makeText(requireContext(), "Pendiente para Entregable 2 (CRUD)", Toast.LENGTH_SHORT).show()
        }
        binding.btnEdit.setOnClickListener {
            Toast.makeText(requireContext(), "Pendiente para Entregable 2 (CRUD)", Toast.LENGTH_SHORT).show()
        }
        binding.btnReminder.setOnClickListener {
            Toast.makeText(requireContext(), "Pendiente para Entregable 3 (recordatorios)", Toast.LENGTH_SHORT).show()
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
