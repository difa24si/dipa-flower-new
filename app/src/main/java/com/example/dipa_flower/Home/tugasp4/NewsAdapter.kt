package com.example.dipa_flower.Home.tugasp4

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.databinding.ItemNewsBinding

class NewsAdapter(
    private val newsList: List<News>
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(
        val binding: ItemNewsBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NewsViewHolder {

        val binding = ItemNewsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return NewsViewHolder(binding)
    }

    override fun getItemCount(): Int = newsList.size

    override fun onBindViewHolder(
        holder: NewsViewHolder,
        position: Int
    ) {
        holder.binding.tvTitle.text = newsList[position].title
        holder.binding.tvDesc.text = newsList[position].description
    }
}