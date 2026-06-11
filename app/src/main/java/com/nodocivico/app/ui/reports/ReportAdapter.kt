package com.nodocivico.app.ui.reports

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nodocivico.app.R
import com.nodocivico.app.data.model.Report
import com.nodocivico.app.data.model.ReportStatus
import com.nodocivico.app.data.model.SampleData
import com.nodocivico.app.databinding.ItemReportBinding
import com.nodocivico.app.util.DateFormats

class ReportAdapter(
    private val onClick: (Report) -> Unit
) : ListAdapter<Report, ReportAdapter.VH>(DiffCallback) {

    companion object DiffCallback : DiffUtil.ItemCallback<Report>() {
        override fun areItemsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Report, newItem: Report): Boolean =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

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
                ReportStatus.OPEN        -> R.drawable.bg_status_open      to R.color.status_open_fg
                ReportStatus.IN_PROGRESS -> R.drawable.bg_status_progress  to R.color.status_progress_fg
                ReportStatus.RESOLVED    -> R.drawable.bg_status_closed    to R.color.status_closed_fg
                ReportStatus.CLOSED      -> R.drawable.bg_status_closed    to R.color.status_closed_fg
            }
            binding.tvStatus.background =
                ContextCompat.getDrawable(binding.root.context, bgRes)
            binding.tvStatus.setTextColor(
                ContextCompat.getColor(binding.root.context, fgRes)
            )

            binding.root.setOnClickListener { onClick(report) }
        }
    }
}