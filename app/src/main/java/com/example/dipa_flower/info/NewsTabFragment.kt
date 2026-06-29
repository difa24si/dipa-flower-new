package com.example.dipa_flower.info

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dipa_flower.Home.WebViewActivity
import com.example.dipa_flower.adapter.NewsAdapter
import com.example.dipa_flower.data.api.ApiClient
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.FavoriteNewsEntity
import com.example.dipa_flower.data.model.News
import com.example.dipa_flower.databinding.FragmentNewsTabBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsTabFragment : Fragment() {
    private var _binding: FragmentNewsTabBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter
    private val favoriteIds = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsTabBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadFavoritesAndNews()
    }

    private fun setupRecyclerView() {
        binding.rvNews.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(
            newsList = emptyList(),
            favoriteIds = favoriteIds,
            onItemClick = { news ->
                val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
                    putExtra("url", news.link)
                }
                startActivity(intent)
            },
            onFavoriteClick = { news, makeFavorite ->
                toggleFavorite(news, makeFavorite)
            }
        )
        binding.rvNews.adapter = newsAdapter
    }

    private fun loadFavoritesAndNews() {
        binding.progressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            
            // 1. Get favorites
            val favs = withContext(Dispatchers.IO) {
                db.favoriteNewsDao().getAllFavoriteNews()
            }
            favoriteIds.clear()
            favoriteIds.addAll(favs.map { it.id })

            // 2. Fetch News API
            try {
                val news = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getNewsList()
                }
                newsAdapter.updateData(news)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat berita: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun toggleFavorite(news: News, makeFavorite: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
            if (makeFavorite) {
                db.favoriteNewsDao().insertFavorite(
                    FavoriteNewsEntity(
                        id = news.id,
                        title = news.title,
                        image = news.image,
                        location = news.location,
                        date = news.date,
                        description = news.description,
                        link = news.link
                    )
                )
                favoriteIds.add(news.id)
            } else {
                db.favoriteNewsDao().deleteFavorite(
                    FavoriteNewsEntity(
                        id = news.id,
                        title = news.title,
                        image = news.image,
                        location = news.location,
                        date = news.date,
                        description = news.description,
                        link = news.link
                    )
                )
                favoriteIds.remove(news.id)
            }

            withContext(Dispatchers.Main) {
                newsAdapter.notifyDataSetChanged()
                val msg = if (makeFavorite) "Disimpan ke Favorit" else "Dihapus dari Favorit"
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
