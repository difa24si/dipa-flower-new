package com.example.dipa_flower.contact

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dipa_flower.R
import com.example.dipa_flower.databinding.FragmentEmergencyContactBinding

class EmergencyContactFragment : Fragment() {
    private var _binding: FragmentEmergencyContactBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmergencyContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contacts = listOf(
            EmergencyContact("BPBD (Penanggulangan Bencana)", "117", R.drawable.ic_warning),
            EmergencyContact("PMI (Palang Merah Indonesia)", "021-7992325", R.drawable.ic_mitigation),
            EmergencyContact("Pemadam Kebakaran (Damkar)", "113", R.drawable.ic_warning),
            EmergencyContact("Ambulans Darurat", "118", R.drawable.ic_phone),
            EmergencyContact("Polisi (Polsek Desa)", "110", R.drawable.ic_phone),
            EmergencyContact("Rumah Sakit Daerah", "119", R.drawable.ic_phone),
            EmergencyContact("Posko Darurat Desa", "08123456789", R.drawable.ic_home)
        )

        binding.lvContacts.adapter = ContactListAdapter(requireContext(), contacts)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun callNumber(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        startActivity(intent)
    }

    data class EmergencyContact(val name: String, val phone: String, val iconRes: Int)

    private inner class ContactListAdapter(
        private val context: Context,
        private val list: List<EmergencyContact>
    ) : BaseAdapter() {

        override fun getCount(): Int = list.size
        override fun getItem(position: Int): Any = list[position]
        override fun getItemId(position: Int): Long = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View
            val holder: ViewHolder

            if (convertView == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false)
                holder = ViewHolder()
                holder.imgIcon = view.findViewById(R.id.imgIcon)
                holder.tvInstansi = view.findViewById(R.id.tvInstansi)
                holder.tvPhone = view.findViewById(R.id.tvPhone)
                holder.btnCall = view.findViewById(R.id.btnCall)
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }

            val item = list[position]
            holder.tvInstansi?.text = item.name
            holder.tvPhone?.text = item.phone
            holder.imgIcon?.setImageResource(item.iconRes)

            val clickListener = View.OnClickListener {
                callNumber(item.phone)
            }

            holder.btnCall?.setOnClickListener(clickListener)
            view.setOnClickListener(clickListener)

            return view
        }
    }

    private class ViewHolder {
        var imgIcon: ImageView? = null
        var tvInstansi: TextView? = null
        var tvPhone: TextView? = null
        var btnCall: ImageButton? = null
    }
}
