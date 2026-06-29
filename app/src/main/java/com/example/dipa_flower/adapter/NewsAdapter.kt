package com.example.dipa_flower.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dipa_flower.R
import com.example.dipa_flower.data.model.News
import com.example.dipa_flower.databinding.ItemNewsBinding

class NewsAdapter(
    private var newsList: List<News>,
    private val favoriteIds: Set<Int>,
    private val onItemClick: (News) -> Unit,
    private val onFavoriteClick: (News, Boolean) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(newsList[position])
    }

    override fun getItemCount(): Int = newsList.size

    fun updateData(newList: List<News>) {
        newsList = newList
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(private val binding: ItemNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: News) {
            binding.tvTitle.text = item.title
            binding.tvLocation.text = item.location
            binding.tvDate.text = item.date
            binding.tvDesc.text = item.description

            // Load Image with Glide
            Glide.with(binding.root.context)
                .load(item.image)
                .placeholder(R.drawable.profile_placeholder)
                .error(R.drawable.profile_placeholder)
                .into(binding.imgNews)

            // Setup favorite star icon state
            val isFav = favoriteIds.contains(item.id)
            if (isFav) {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_filled)
            } else {
                binding.btnFavorite.setImageResource(R.drawable.ic_star_outline)
            }

            // Listeners
            binding.root.setOnClickListener { onItemClick(item) }
            
            binding.btnFavorite.setOnClickListener {
                val nowFav = !favoriteIds.contains(item.id)
                onFavoriteClick(item, nowFav)
            }
        }
    }
}
