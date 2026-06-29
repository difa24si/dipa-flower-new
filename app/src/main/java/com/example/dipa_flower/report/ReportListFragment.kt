package com.example.dipa_flower.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dipa_flower.adapter.ReportAdapter
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.DisasterReportEntity
import com.example.dipa_flower.databinding.FragmentReportListBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ReportListFragment : Fragment() {

    private var _binding: FragmentReportListBinding? = null
    private val binding get() = _binding!!

    private lateinit var reportAdapter: ReportAdapter
    private val reportList = mutableListOf<DisasterReportEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? DisasterReportActivity)?.setHeaderTitle("Daftar Kejadian Bencana")

        setupRecyclerView()
        loadReports()

        binding.fabAdd.setOnClickListener {
            val formFragment = ReportFormFragment.newInstance(0)
            (activity as? DisasterReportActivity)?.replaceFragment(formFragment, true)
        }
    }

    private fun setupRecyclerView() {
        binding.rvReports.layoutManager = LinearLayoutManager(requireContext())
        reportAdapter = ReportAdapter(
            reports = reportList,
            onEditClick = { report ->
                val formFragment = ReportFormFragment.newInstance(report.id)
                (activity as? DisasterReportActivity)?.replaceFragment(formFragment, true)
            },
            onDeleteClick = { report ->
                showDeleteConfirmationDialog(report)
            }
        )
        binding.rvReports.adapter = reportAdapter
    }

    private fun loadReports() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val list = withContext(Dispatchers.IO) {
                db.disasterReportDao().getAllReports()
            }
            
            reportList.clear()
            reportList.addAll(list)

            if (list.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvReports.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvReports.visibility = View.VISIBLE
            }
            reportAdapter.updateData(reportList)
        }
    }

    private fun showDeleteConfirmationDialog(report: DisasterReportEntity) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Hapus Laporan?")
            .setMessage("Apakah Anda yakin ingin menghapus laporan kejadian bencana di ${report.location}?")
            .setPositiveButton("Hapus") { dialog, _ ->
                deleteReport(report)
                dialog.dismiss()
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteReport(report: DisasterReportEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            db.disasterReportDao().deleteReport(report)
            
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Laporan berhasil dihapus", Toast.LENGTH_SHORT).show()
                loadReports()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
