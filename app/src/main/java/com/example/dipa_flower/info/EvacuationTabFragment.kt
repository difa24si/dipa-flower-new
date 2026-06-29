package com.example.dipa_flower.info

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.R
import com.example.dipa_flower.databinding.FragmentEvacuationTabBinding

class EvacuationTabFragment : Fragment() {
    private var _binding: FragmentEvacuationTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEvacuationTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spots = listOf(
            EvacuationSpot("Posko Utama Balai Desa", "Aula Kantor Balai Desa, RT 02/RW 01", 250, "AKTIF"),
            EvacuationSpot("Posko Tenda Darurat Lapangan", "Lapangan Bola Desa Merdeka, Jl. Pemuda", 500, "AKTIF"),
            EvacuationSpot("Gedung Serbaguna GOR", "GOR Olahraga Kecamatan Indah, Jl. Raya Utama", 300, "PENUH"),
            EvacuationSpot("SDN 01 Merdeka (Gedung B)", "Gedung Sekolah SDN 01 Merdeka, RT 05/RW 02", 150, "AKTIF"),
            EvacuationSpot("Posko Kesehatan & PMI", "Puskesmas Pembantu Desa Merdeka, RT 01", 80, "AKTIF")
        )

        binding.rvEvacuation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEvacuation.adapter = EvacuationAdapter(spots)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class EvacuationSpot(val name: String, val address: String, val capacity: Int, val status: String)

    private inner class EvacuationAdapter(private val items: List<EvacuationSpot>) :
        RecyclerView.Adapter<EvacuationAdapter.EvacuationViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvacuationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_evacuation, parent, false)
            return EvacuationViewHolder(view)
        }

        override fun onBindViewHolder(holder: EvacuationViewHolder, position: Int) {
            val item = items[position]
            holder.tvPoskoName.text = item.name
            holder.tvAddress.text = item.address
            holder.tvCapacity.text = "Kapasitas: ${item.capacity} Jiwa"
            holder.tvStatus.text = item.status
            
            if (item.status == "PENUH") {
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D32F2F")) // Red
            } else {
                holder.tvStatus.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#2E5A27")) // Green
            }
        }

        override fun getItemCount(): Int = items.size

        inner class EvacuationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvPoskoName: TextView = view.findViewById(R.id.tvPoskoName)
            val tvAddress: TextView = view.findViewById(R.id.tvAddress)
            val tvCapacity: TextView = view.findViewById(R.id.tvCapacity)
            val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        }
    }
}
