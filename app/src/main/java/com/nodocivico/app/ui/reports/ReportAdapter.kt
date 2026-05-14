package com.nodocivico.app.ui.reports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nodocivico.app.R
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.model.ReportStatus
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.ItemReportBinding
import com.nodocivico.app.util.DateFormats

/**
 * Adapter para la lista de reportes.
 *
 * No usa DiffUtil todavía: el conjunto es pequeño y estático para el
 * Entregable 1. En el Entregable 2 se migrará a ListAdapter + DiffUtil
 * cuando los datos sean reactivos a partir de Room.
 */
class ReportAdapter(
    private val onClick: (Report) -> Unit
) : RecyclerView.Adapter<ReportAdapter.VH>() {

    private val items: MutableList<Report> = mutableListOf()

    fun submit(reports: List<Report>) {
        items.clear()
        items.addAll(reports)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class VH(private val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(report: Report) {
            binding.tvReportIcon.text = report.title.firstOrNull()?.uppercase() ?: "·"
            binding.tvReportTitle.text = report.title
            val categoryName = SampleData.categoryName(report.categoryId)
            val priority = "Prioridad ${report.priority.label.lowercase()}"
            val relative = DateFormats.humanRelative(report.createdAtMillis)
            binding.tvReportMeta.text = "$categoryName · $priority · $relative"

            binding.tvStatus.text = report.status.label
            val (bgRes, fgRes) = when (report.status) {
                ReportStatus.ABIERTO    -> R.drawable.bg_status_open    to R.color.status_open_fg
                ReportStatus.EN_PROCESO -> R.drawable.bg_status_progress to R.color.status_progress_fg
                ReportStatus.CERRADO    -> R.drawable.bg_status_closed   to R.color.status_closed_fg
            }
            binding.tvStatus.background = ContextCompat.getDrawable(binding.root.context, bgRes)
            binding.tvStatus.setTextColor(
                ContextCompat.getColor(binding.root.context, fgRes)
            )

            binding.root.setOnClickListener { onClick(report) }
        }
    }
}
