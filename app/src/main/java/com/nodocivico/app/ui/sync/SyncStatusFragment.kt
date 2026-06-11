package com.nodocivico.app.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nodocivico.app.NodoCivicoApp
import com.nodocivico.app.databinding.FragmentSyncStatusBinding
import kotlinx.coroutines.launch

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

        val repository = (requireActivity().application as NodoCivicoApp).reportRepository

        // Observar contadores reales de Room
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    repository.countSynced.collect { count ->
                        binding.tvSent.text = "$count reportes sincronizados con el servidor."
                    }
                }
                launch {
                    repository.countPending.collect { count ->
                        binding.tvPending.text = "$count cambios quedan en cola local."
                    }
                }
            }
        }

        binding.btnSyncNow.setOnClickListener {
            binding.btnSyncNow.isEnabled = false
            binding.btnSyncNow.text = "Sincronizando..."

            viewLifecycleOwner.lifecycleScope.launch {
                try {
                    val result = repository.sync()
                    Toast.makeText(
                        requireContext(),
                        "Subidos: ${result.pushed} | Descargados: ${result.pulled} | Errores: ${result.errors}",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Error de sincronizacion: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                } finally {
                    binding.btnSyncNow.isEnabled = true
                    binding.btnSyncNow.text = "Sincronizar ahora"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
