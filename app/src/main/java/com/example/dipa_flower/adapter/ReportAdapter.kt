package com.example.dipa_flower.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.data.db.DisasterReportEntity
import com.example.dipa_flower.databinding.ItemReportBinding

class ReportAdapter(
    private var reports: List<DisasterReportEntity>,
    private val onEditClick: (DisasterReportEntity) -> Unit,
    private val onDeleteClick: (DisasterReportEntity) -> Unit
) : RecyclerView.Adapter<ReportAdapter.ReportViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val binding = ItemReportBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReportViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        holder.bind(reports[position])
    }

    override fun getItemCount(): Int = reports.size

    fun updateData(newList: List<DisasterReportEntity>) {
        reports = newList
        notifyDataSetChanged()
    }

    inner class ReportViewHolder(private val binding: ItemReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DisasterReportEntity) {
            binding.tvDisasterType.text = item.disasterType
            binding.tvDate.text = item.date
            binding.tvLocation.text = "Lokasi: ${item.location}"
            binding.tvReporter.text = "Pelapor: ${item.reporterName}"
            binding.tvDescription.text = item.description

            // Dynamic color based on disaster type for a premium look
            val colorStr = when (item.disasterType) {
                "Gempa Bumi" -> "#FF8F00" // Orange
                "Banjir Bandang" -> "#1976D2" // Blue
                "Tanah Longsor" -> "#795548" // Brown
                "Gunung Meletus" -> "#D32F2F" // Red
                "Cuaca Ekstrem" -> "#455A64" // Gray-blue
                else -> "#2E5A27" // Green (Default)
            }
            val color = Color.parseColor(colorStr)
            binding.indicator.setBackgroundColor(color)
            binding.tvDisasterType.setTextColor(color)

            binding.btnEdit.setOnClickListener { onEditClick(item) }
            binding.btnDelete.setOnClickListener { onDeleteClick(item) }
        }
    }
}
