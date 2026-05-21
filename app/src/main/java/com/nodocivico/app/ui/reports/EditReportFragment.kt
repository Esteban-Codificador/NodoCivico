package com.nodocivico.app.ui.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nodocivico.app.data.model.Priority
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.FragmentEditReportBinding
import com.nodocivico.app.ui.viewmodel.EditReportViewModel
import com.nodocivico.app.ui.viewmodel.SaveResult
import kotlinx.coroutines.launch

class EditReportFragment : Fragment() {

    private var _binding: FragmentEditReportBinding? = null
    private val binding get() = _binding!!
    private val viewModel: EditReportViewModel by viewModels()
    private val args: EditReportFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spinners
        val categories = SampleData.categories.map { it.name }
        binding.spinnerCategory.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, categories
        )
        val priorities = Priority.values().map { it.label }
        binding.spinnerPriority.adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_dropdown_item, priorities
        )

        viewModel.load(args.reportId)

        // Llenar campos con datos actuales del reporte
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.report.collect { report ->
                    report ?: return@collect
                    binding.etTitle.setText(report.title)
                    binding.etDescription.setText(report.description)
                    binding.etLocation.setText(report.location)

                    val catIndex = SampleData.categories.indexOfFirst { it.id == report.categoryId }
                    if (catIndex >= 0) binding.spinnerCategory.setSelection(catIndex)

                    val priIndex = Priority.values().indexOfFirst { it == report.priority }
                    if (priIndex >= 0) binding.spinnerPriority.setSelection(priIndex)
                }
            }
        }

        // Observar resultado del guardado
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.saveResult.collect { result ->
                    when (result) {
                        is SaveResult.Success -> {
                            Snackbar.make(
                                binding.root,
                                "Reporte actualizado correctamente.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            findNavController().popBackStack()
                        }
                        is SaveResult.Error -> {
                            Snackbar.make(
                                binding.root,
                                result.message,
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }

        binding.btnUpdate.setOnClickListener { handleUpdate() }
        binding.btnCancel.setOnClickListener { findNavController().popBackStack() }
    }

    private fun handleUpdate() {
        val title = binding.etTitle.text?.toString()?.trim().orEmpty()
        val description = binding.etDescription.text?.toString()?.trim().orEmpty()
        val location = binding.etLocation.text?.toString()?.trim().orEmpty()

        var hasError = false

        if (title.length < 5) {
            binding.etTitle.error = "El título debe tener al menos 5 caracteres"
            hasError = true
        } else {
            binding.etTitle.error = null
        }

        if (description.length < 10) {
            binding.etDescription.error = "Describe la incidencia con al menos 10 caracteres"
            hasError = true
        } else {
            binding.etDescription.error = null
        }

        if (location.isEmpty()) {
            binding.etLocation.error = "Indica una ubicación referencial"
            hasError = true
        } else {
            binding.etLocation.error = null
        }

        if (hasError) return

        val categoryId = SampleData.categories[binding.spinnerCategory.selectedItemPosition].id
        val priority = Priority.values()[binding.spinnerPriority.selectedItemPosition]

        viewModel.update(
            title = title,
            description = description,
            categoryId = categoryId,
            priority = priority,
            location = location
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}