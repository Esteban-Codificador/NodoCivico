package com.nodocivico.app.ui.reminders

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nodocivico.app.databinding.FragmentCalendarRemindersBinding
import com.nodocivico.app.util.DateFormats
import java.util.Calendar

class CalendarRemindersFragment : Fragment() {

    private var _binding: FragmentCalendarRemindersBinding? = null
    private val binding get() = _binding!!
    private var selectedDateMillis: Long? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarRemindersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    cal.set(year, month, day)
                    selectedDateMillis = cal.timeInMillis
                    binding.tvSelectedDate.text =
                        DateFormats.shortDate(cal.timeInMillis)
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        binding.btnSaveReminder.setOnClickListener {
            val message = binding.etReminderMessage.text?.toString()?.trim().orEmpty()
            if (message.isEmpty()) {
                binding.etReminderMessage.error = "Escribe un mensaje para el recordatorio"
                return@setOnClickListener
            }
            if (selectedDateMillis == null) {
                Snackbar.make(binding.root, "Selecciona una fecha", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // En Entregable 3 se conectará con AlarmManager y ReminderReceiver
            Snackbar.make(
                binding.root,
                "Recordatorio guardado para ${DateFormats.shortDate(selectedDateMillis!!)}",
                Snackbar.LENGTH_LONG
            ).show()
            findNavController().popBackStack()
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