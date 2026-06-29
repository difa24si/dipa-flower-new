package com.example.dipa_flower.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.R
import com.example.dipa_flower.databinding.FragmentMitigationTabBinding

class MitigationTabFragment : Fragment() {
    private var _binding: FragmentMitigationTabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMitigationTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val guides = listOf(
            MitigationGuide("Sebelum Gempa", "Siapkan tas siaga bencana, tentukan rute evakuasi aman, dan amankan perabotan rumah tangga yang mudah jatuh.", R.drawable.ic_mitigation),
            MitigationGuide("Saat Gempa", "Lindungi kepala, berlindung di bawah meja kokoh, jauhi jendela kaca, dan cari tempat terbuka di luar ruangan.", R.drawable.ic_warning),
            MitigationGuide("Setelah Gempa", "Periksa adanya cedera, waspadai gempa susulan, gunakan tangga darurat, dan berkumpullah di titik evakuasi.", R.drawable.ic_mitigation),
            MitigationGuide("Evakuasi Banjir", "Matikan aliran listrik dan gas di rumah, amankan dokumen penting, segera mengungsi ke posko evakuasi terdekat.", R.drawable.ic_evacuation),
            MitigationGuide("Pertolongan Pertama", "Siapkan kotak P3K, tangani luka luar secara steril, posisikan korban dengan aman, dan hubungi ambulans.", R.drawable.ic_phone)
        )

        binding.rvMitigation.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMitigation.adapter = MitigationAdapter(guides)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    data class MitigationGuide(val title: String, val description: String, val iconRes: Int)

    private inner class MitigationAdapter(private val items: List<MitigationGuide>) :
        RecyclerView.Adapter<MitigationAdapter.MitigationViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MitigationViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_mitigation, parent, false)
            return MitigationViewHolder(view)
        }

        override fun onBindViewHolder(holder: MitigationViewHolder, position: Int) {
            val item = items[position]
            holder.tvTitle.text = item.title
            holder.tvDescription.text = item.description
            holder.imgIcon.setImageResource(item.iconRes)
        }

        override fun getItemCount(): Int = items.size

        inner class MitigationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvTitle: TextView = view.findViewById(R.id.tvTitle)
            val tvDescription: TextView = view.findViewById(R.id.tvDescription)
            val imgIcon: ImageView = view.findViewById(R.id.imgIcon)
        }
    }
}
