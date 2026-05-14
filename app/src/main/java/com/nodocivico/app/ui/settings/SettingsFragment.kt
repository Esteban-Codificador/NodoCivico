package com.nodocivico.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nodocivico.app.databinding.FragmentSettingsBinding

/**
 * Preferencias de la app.
 *
 * Equivale al panel "Preferencias" del prototipo HTML. Por ahora sólo
 * inicializa los selectores (tema, notificaciones y filtro por estado).
 * En el Entregable 3 se conectarán a SharedPreferences o DataStore para
 * persistir las decisiones del usuario.
 */
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.spinnerTheme.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            listOf("Sistema", "Claro", "Oscuro")
        )
        binding.spinnerNotifications.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            listOf("Activas", "Silenciosas", "Desactivadas")
        )
        binding.spinnerStatusFilter.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item,
            listOf("Todos", "Abiertos", "En proceso", "Cerrados")
        )

        binding.btnSave.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Las preferencias se guardarán en el Entregable 3.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
