package com.example.dipa_flower.checklist

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.R
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.ChecklistEntity
import com.example.dipa_flower.databinding.FragmentChecklistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChecklistFragment : Fragment() {
    private var _binding: FragmentChecklistBinding? = null
    private val binding get() = _binding!!

    private lateinit var checklistAdapter: ChecklistAdapter
    private var checklistItems = mutableListOf<ChecklistEntity>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadChecklistData()

        binding.btnAddItem.setOnClickListener {
            addNewItem()
        }
    }

    private fun setupRecyclerView() {
        binding.rvChecklist.layoutManager = LinearLayoutManager(requireContext())
        checklistAdapter = ChecklistAdapter()
        binding.rvChecklist.adapter = checklistAdapter
    }

    private fun loadChecklistData() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val items = withContext(Dispatchers.IO) {
                db.checklistDao().getAllChecklist()
            }
            checklistItems.clear()
            checklistItems.addAll(items)
            checklistAdapter.notifyDataSetChanged()
        }
    }

    private fun addNewItem() {
        val itemName = binding.etNewItem.text.toString().trim()
        if (itemName.isEmpty()) {
            Toast.makeText(requireContext(), "Nama perlengkapan tidak boleh kosong", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            val newEntity = ChecklistEntity(item = itemName, completed = false)
            val generatedId = db.checklistDao().insertChecklist(newEntity)
            
            withContext(Dispatchers.Main) {
                checklistItems.add(newEntity.copy(id = generatedId.toInt()))
                checklistAdapter.notifyItemInserted(checklistItems.size - 1)
                binding.etNewItem.text.clear()
                Toast.makeText(requireContext(), "Berhasil ditambahkan", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateItemStatus(item: ChecklistEntity, isChecked: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            val updated = item.copy(completed = isChecked)
            db.checklistDao().updateChecklist(updated)
            
            // Sync status locally
            val index = checklistItems.indexOfFirst { it.id == item.id }
            if (index != -1) {
                checklistItems[index] = updated
                withContext(Dispatchers.Main) {
                    checklistAdapter.notifyItemChanged(index)
                }
            }
        }
    }

    private fun deleteItem(item: ChecklistEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            db.checklistDao().deleteChecklist(item)

            withContext(Dispatchers.Main) {
                val index = checklistItems.indexOfFirst { it.id == item.id }
                if (index != -1) {
                    checklistItems.removeAt(index)
                    checklistAdapter.notifyItemRemoved(index)
                }
                Toast.makeText(requireContext(), "Berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class ChecklistAdapter : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_checklist, parent, false)
            return ChecklistViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
            val item = checklistItems[position]
            holder.bind(item)
        }

        override fun getItemCount(): Int = checklistItems.size

        inner class ChecklistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val cbCompleted: CheckBox = view.findViewById(R.id.cbCompleted)
            val tvItemName: TextView = view.findViewById(R.id.tvItemName)
            val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)

            fun bind(item: ChecklistEntity) {
                tvItemName.text = item.item
                
                // Prevent trigger recursive checking listener while recycling views
                cbCompleted.setOnCheckedChangeListener(null)
                cbCompleted.isChecked = item.completed

                if (item.completed) {
                    tvItemName.paintFlags = tvItemName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    tvItemName.setTextColor(Color.GRAY)
                } else {
                    tvItemName.paintFlags = tvItemName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    tvItemName.setTextColor(Color.parseColor("#333333"))
                }

                cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                    updateItemStatus(item, isChecked)
                }

                btnDelete.setOnClickListener {
                    deleteItem(item)
                }
            }
        }
    }
}
