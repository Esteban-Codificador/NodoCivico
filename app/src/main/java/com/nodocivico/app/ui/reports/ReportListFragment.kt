package com.nodocivico.app.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nodocivico.app.R
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.FragmentReportListBinding

/**
 * Lista de reportes recientes.
 *
 * Equivale al panel "Reportes recientes" del prototipo HTML. Cada item es
 * una tarjeta que muestra título, categoría, antigüedad y estado.
 *
 * En el Entregable 1 los datos vienen de [SampleData]. En el Entregable 2
 * se reemplazará la fuente por un Flow<List<Report>> emitido por Room a
 * través de un ReportRepository.
 */
class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

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
            // Por ahora se navega al detalle sin pasar el id; el Entregable 2
            // pasará el id mediante Safe Args para cargar el reporte real.
            findNavController().navigate(R.id.action_reportListFragment_to_reportDetailFragment)
        }
        binding.recyclerReports.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReports.adapter = adapter
        adapter.submit(SampleData.sampleReports)

        binding.fabNewReport.setOnClickListener {
            findNavController().navigate(R.id.action_reportListFragment_to_createReportFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
