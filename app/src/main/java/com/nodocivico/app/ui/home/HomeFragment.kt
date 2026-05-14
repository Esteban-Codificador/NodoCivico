package com.nodocivico.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nodocivico.app.R
import com.nodocivico.app.data.model.ReportStatus
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.FragmentHomeBinding

/**
 * Pantalla principal (Resumen general).
 *
 * Reproduce el panel "Resumen general" del prototipo HTML:
 *  - tres tarjetas de estadísticas (totales, pendientes, sincronizados)
 *  - dos tarjetas informativas (estado offline, accesos rápidos)
 *  - botones de navegación rápida hacia las otras secciones
 *
 * Para el Entregable 1 los números provienen de [SampleData]. En el
 * Entregable 2 se calcularán a partir de Room mediante consultas count(*).
 */
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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

        val reports = SampleData.sampleReports
        val total = reports.size
        val pending = reports.count { it.pendingSync || it.status != ReportStatus.CERRADO }
        val synced = reports.count { !it.pendingSync }

        binding.tvStatTotal.text = total.toString()
        binding.tvStatPending.text = pending.toString()
        binding.tvStatSynced.text = synced.toString()

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
