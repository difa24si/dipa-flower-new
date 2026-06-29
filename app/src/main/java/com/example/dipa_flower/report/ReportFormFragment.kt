package com.example.dipa_flower.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.dipa_flower.R
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.DisasterReportEntity
import com.example.dipa_flower.databinding.FragmentReportFormBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ReportFormFragment : Fragment() {

    private var _binding: FragmentReportFormBinding? = null
    private val binding get() = _binding!!

    private var reportId: Int = 0
    private val disasterTypes = listOf(
        "Gempa Bumi",
        "Banjir Bandang",
        "Tanah Longsor",
        "Gunung Meletus",
        "Cuaca Ekstrem"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            reportId = it.getInt(ARG_REPORT_ID, 0)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSpinner()
        setupDefaultDate()

        if (reportId > 0) {
            (activity as? DisasterReportActivity)?.setHeaderTitle("Edit Laporan")
            binding.tvFormTitle.text = "Ubah Laporan Kejadian"
            binding.btnSave.text = "Perbarui Laporan"
            loadReportDetails()
        } else {
            (activity as? DisasterReportActivity)?.setHeaderTitle("Tambah Laporan")
            binding.tvFormTitle.text = "Buat Laporan Baru"
            binding.btnSave.text = "Simpan Laporan"
        }

        binding.btnSave.setOnClickListener {
            saveReport()
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            disasterTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerDisasterType.adapter = adapter
    }

    private fun setupDefaultDate() {
        // Set current date as default format "dd MMMM yyyy"
        val sdf = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        binding.etDate.setText(sdf.format(Date()))
    }

    private fun loadReportDetails() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val report = withContext(Dispatchers.IO) {
                db.disasterReportDao().getReportById(reportId)
            }

            report?.let {
                binding.etLocation.setText(it.location)
                binding.etDate.setText(it.date)
                binding.etReporter.setText(it.reporterName)
                binding.etDescription.setText(it.description)

                val spinnerPosition = disasterTypes.indexOf(it.disasterType)
                if (spinnerPosition >= 0) {
                    binding.spinnerDisasterType.setSelection(spinnerPosition)
                }
            }
        }
    }

    private fun saveReport() {
        val disasterType = binding.spinnerDisasterType.selectedItem.toString()
        val location = binding.etLocation.text.toString().trim()
        val date = binding.etDate.text.toString().trim()
        val reporter = binding.etReporter.text.toString().trim()
        val description = binding.etDescription.text.toString().trim()

        if (location.isEmpty()) {
            binding.etLocation.error = "Lokasi harus diisi"
            return
        }
        if (date.isEmpty()) {
            binding.etDate.error = "Tanggal harus diisi"
            return
        }
        if (reporter.isEmpty()) {
            binding.etReporter.error = "Nama pelapor harus diisi"
            return
        }
        if (description.isEmpty()) {
            binding.etDescription.error = "Deskripsi harus diisi"
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            val entity = DisasterReportEntity(
                id = reportId,
                disasterType = disasterType,
                location = location,
                date = date,
                reporterName = reporter,
                description = description
            )

            if (reportId > 0) {
                db.disasterReportDao().updateReport(entity)
            } else {
                db.disasterReportDao().insertReport(entity)
            }

            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    if (reportId > 0) "Laporan berhasil diperbarui" else "Laporan berhasil disimpan",
                    Toast.LENGTH_SHORT
                ).show()
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_REPORT_ID = "report_id"

        fun newInstance(reportId: Int): ReportFormFragment {
            val fragment = ReportFormFragment()
            val args = Bundle()
            args.putInt(ARG_REPORT_ID, reportId)
            fragment.arguments = args
            return fragment
        }
    }
}
