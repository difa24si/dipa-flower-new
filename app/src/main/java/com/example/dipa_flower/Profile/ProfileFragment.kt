package com.example.dipa_flower.Profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dipa_flower.AuthActivity
import com.example.dipa_flower.Home.WebViewActivity
import com.example.dipa_flower.adapter.NewsAdapter
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.FavoriteNewsEntity
import com.example.dipa_flower.data.model.News
import com.example.dipa_flower.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter
    private val favoriteNewsList = mutableListOf<News>()
    private val favoriteIds = mutableSetOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadFavoriteNews()
        setupLogout()
    }

    private fun setupRecyclerView() {
        binding.rvFavNews.layoutManager = LinearLayoutManager(requireContext())
        newsAdapter = NewsAdapter(
            newsList = favoriteNewsList,
            favoriteIds = favoriteIds,
            onItemClick = { news ->
                val intent = Intent(requireContext(), WebViewActivity::class.java).apply {
                    putExtra("url", news.link)
                }
                startActivity(intent)
            },
            onFavoriteClick = { news, _ ->
                // Since this is favorite page, clicking favorite star always means UNFAVORITE (delete)
                unfavoriteNews(news)
            }
        )
        binding.rvFavNews.adapter = newsAdapter
    }

    private fun loadFavoriteNews() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            val favEntities = withContext(Dispatchers.IO) {
                db.favoriteNewsDao().getAllFavoriteNews()
            }

            favoriteNewsList.clear()
            favoriteIds.clear()

            if (favEntities.isEmpty()) {
                binding.tvNoFavs.visibility = View.VISIBLE
                binding.rvFavNews.visibility = View.GONE
            } else {
                binding.tvNoFavs.visibility = View.GONE
                binding.rvFavNews.visibility = View.VISIBLE

                favEntities.forEach { entity ->
                    favoriteNewsList.add(
                        News(
                            id = entity.id,
                            title = entity.title,
                            image = entity.image,
                            location = entity.location,
                            date = entity.date,
                            description = entity.description,
                            link = entity.link
                        )
                    )
                    favoriteIds.add(entity.id)
                }
            }
            newsAdapter.notifyDataSetChanged()
        }
    }

    private fun unfavoriteNews(news: News) {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(requireContext())
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

            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Dihapus dari Favorit", Toast.LENGTH_SHORT).show()
                loadFavoriteNews() // Reload data
            }
        }
    }

    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin keluar dari aplikasi?")
                .setPositiveButton("Ya") { dialog, _ ->
                    val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
                    sharedPref.edit {
                        clear()
                    }

                    dialog.dismiss()

                    val intent = Intent(requireContext(), AuthActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}