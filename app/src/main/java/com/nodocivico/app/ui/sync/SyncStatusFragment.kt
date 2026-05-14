package com.nodocivico.app.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.FragmentSyncStatusBinding

/**
 * Estado de sincronización.
 *
 * Equivale al panel "Sincronización" del prototipo HTML. Muestra cuántos
 * reportes están enviados y cuántos siguen pendientes. En el Entregable 3
 * esta pantalla se conectará al ConnectivityReceiver y al cliente de API
 * para reflejar el estado real y permitir reintentos.
 */
class SyncStatusFragment : Fragment() {

    private var _binding: FragmentSyncStatusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSyncStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val reports = SampleData.sampleReports
        val sent = reports.count { !it.pendingSync }
        val pending = reports.count { it.pendingSync }

        binding.tvSent.text = "$sent reportes sincronizados con el servidor."
        binding.tvPending.text = "$pending cambios quedan en cola local."

        binding.btnSyncNow.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "La sincronización real se implementa en el Entregable 3.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
