package com.nodocivico.app.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.FragmentCreateReportBinding

/**
 * Formulario de creación de reporte.
 *
 * Reproduce el panel "Nuevo reporte" del prototipo HTML. Para el Entregable 1
 * la validación es básica y los datos no se persisten: sólo se valida y se
 * muestra un Toast. En el Entregable 2 esta misma pantalla insertará el
 * Report en Room a través del ReportRepository.
 */
class CreateReportFragment : Fragment() {

    private var _binding: FragmentCreateReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categories = SampleData.categories.map { it.name }
        binding.spinnerCategory.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, categories
        )

        val priorities = Priority.values().map { it.label }
        binding.spinnerPriority.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities
        )

        binding.btnSave.setOnClickListener { handleSave(offline = false) }
        binding.btnSaveOffline.setOnClickListener { handleSave(offline = true) }
        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
    }

    private fun handleSave(offline: Boolean) {
        val title = binding.etTitle.text?.toString()?.trim().orEmpty()
        val description = binding.etDescription.text?.toString()?.trim().orEmpty()
        val location = binding.etLocation.text?.toString()?.trim().orEmpty()

        // Validación mínima del Entregable 1. En el Entregable 2 se ampliará
        // con TextInputLayout.error y limpieza automática al escribir.
        if (title.length < 5) {
            Toast.makeText(requireContext(), "El título debe tener al menos 5 caracteres", Toast.LENGTH_SHORT).show()
            return
        }
        if (description.length < 10) {
            Toast.makeText(requireContext(), "Describe la incidencia con al menos 10 caracteres", Toast.LENGTH_SHORT).show()
            return
        }
        if (location.isEmpty()) {
            Toast.makeText(requireContext(), "Indica una ubicación referencial", Toast.LENGTH_SHORT).show()
            return
        }

        val message = if (offline)
            "Reporte guardado localmente. Se sincronizará cuando vuelva la conexión."
        else
            "Reporte guardado correctamente."
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
