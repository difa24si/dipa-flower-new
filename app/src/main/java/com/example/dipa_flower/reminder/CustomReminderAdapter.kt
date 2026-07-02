package com.example.dipa_flower.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.data.db.CustomReminderEntity
import com.example.dipa_flower.databinding.ItemCustomReminderBinding
import java.util.Locale

class CustomReminderAdapter(
    private var reminders: List<CustomReminderEntity>,
    private val onToggle: (CustomReminderEntity, Boolean) -> Unit,
    private val onDelete: (CustomReminderEntity) -> Unit
) : RecyclerView.Adapter<CustomReminderAdapter.ReminderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val binding = ItemCustomReminderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ReminderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(reminders[position])
    }

    override fun getItemCount(): Int = reminders.size

    fun updateData(newList: List<CustomReminderEntity>) {
        reminders = newList
        notifyDataSetChanged()
    }

    inner class ReminderViewHolder(private val binding: ItemCustomReminderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CustomReminderEntity) {
            val timeStr = String.format(Locale.getDefault(), "%02d:%02d", item.hour, item.minute)
            binding.tvReminderTime.text = timeStr
            binding.tvReminderTitle.text = item.title
            binding.tvReminderMessage.text = item.message

            // Prevent listener from triggering during bind
            binding.switchEnabled.setOnCheckedChangeListener(null)
            binding.switchEnabled.isChecked = item.isEnabled

            binding.switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                onToggle(item, isChecked)
            }

            binding.btnDeleteReminder.setOnClickListener {
                onDelete(item)
            }
        }
    }
}
