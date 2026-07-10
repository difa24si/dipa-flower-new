package com.example.dipa_flower.Home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dipa_flower.BaseActivity
import com.example.dipa_flower.R
import com.example.dipa_flower.adapter.NewsAdapter
import com.example.dipa_flower.adapter.ReportAdapter
import com.example.dipa_flower.data.api.ApiClient
import com.example.dipa_flower.data.db.AppDatabase
import com.example.dipa_flower.data.db.FavoriteNewsEntity
import com.example.dipa_flower.data.model.News
import com.example.dipa_flower.databinding.FragmentHomeBinding
import com.example.dipa_flower.receiver.ReminderReceiver
import com.example.dipa_flower.Home.pertemuan_13.ThirteenthActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var reportsAdapter: ReportAdapter
    private val favoriteIds = mutableSetOf<Int>()
    private var allNews = listOf<News>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val username = sharedPref.getString("username", "Relawan Desa") ?: "Relawan Desa"
        binding.tvUserName.text = username

        setupQuickActions()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // Reload data on resume (important for updating local reports list after user adds/edits a report in DisasterReportActivity!)
        loadFavoritesNewsAndReports()
    }

    private fun setupQuickActions() {
        binding.btnMitigasi.setOnClickListener {
            (activity as? BaseActivity)?.navigateToTab(R.id.info, 1)
        }

        binding.btnEvakuasi.setOnClickListener {
            (activity as? BaseActivity)?.navigateToTab(R.id.info, 2)
        }

        binding.btnChecklist.setOnClickListener {
            (activity as? BaseActivity)?.navigateToTab(R.id.checklist)
        }

        binding.btnKontak.setOnClickListener {
            (activity as? BaseActivity)?.navigateToTab(R.id.kontak)
        }

        binding.tvSeeAllNews.setOnClickListener {
            (activity as? BaseActivity)?.navigateToTab(R.id.info, 0)
        }

        binding.statusCard.setOnClickListener {
            val intent = Intent(requireContext(), com.example.dipa_flower.report.DisasterReportActivity::class.java)
            startActivity(intent)
        }

        binding.tvSeeAllReports.setOnClickListener {
            val intent = Intent(requireContext(), com.example.dipa_flower.report.DisasterReportActivity::class.java)
            startActivity(intent)
        }

        binding.btnReminder.setOnClickListener {
            val intent = Intent(requireContext(), com.example.dipa_flower.reminder.ReminderManagerActivity::class.java)
                startActivity(intent)
        }

        binding.btnPertemuan13.setOnClickListener {
            val intent = Intent(requireContext(), ThirteenthActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        // News RV
        binding.rvLatestNews.layoutManager = LinearLayoutManager(requireContext())
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
        binding.rvLatestNews.adapter = newsAdapter

        // Local Reports RV
        binding.rvHomeReports.layoutManager = LinearLayoutManager(requireContext())
        reportsAdapter = ReportAdapter(
            reports = emptyList(),
            onEditClick = {
                openReportsActivity()
            },
            onDeleteClick = {
                openReportsActivity()
            }
        )
        binding.rvHomeReports.adapter = reportsAdapter
    }

    private fun openReportsActivity() {
        val intent = Intent(requireContext(), com.example.dipa_flower.report.DisasterReportActivity::class.java)
        startActivity(intent)
    }

    private fun loadFavoritesNewsAndReports() {
        binding.progressHomeNews.visibility = View.VISIBLE
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())
            
            // 1. Load favorites IDs from Room
            val favs = withContext(Dispatchers.IO) {
                db.favoriteNewsDao().getAllFavoriteNews()
            }
            favoriteIds.clear()
            favoriteIds.addAll(favs.map { it.id })

            // 2. Load local reports from Room
            val localReports = withContext(Dispatchers.IO) {
                db.disasterReportDao().getAllReports()
            }

            if (localReports.isEmpty()) {
                binding.tvNoHomeReports.visibility = View.VISIBLE
                binding.rvHomeReports.visibility = View.GONE
            } else {
                binding.tvNoHomeReports.visibility = View.GONE
                binding.rvHomeReports.visibility = View.VISIBLE
                reportsAdapter.updateData(localReports.take(3))
            }

            // 3. Fetch News from REST API (Retrofit)
            try {
                val fetchedNews = withContext(Dispatchers.IO) {
                    ApiClient.apiService.getNewsList()
                }
                allNews = fetchedNews
                
                val latestTwo = fetchedNews.take(2)
                newsAdapter.updateData(latestTwo)
                
                checkNewNewsNotification(fetchedNews)
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Gagal memuat berita: ${e.message}", Toast.LENGTH_SHORT).show()
            } finally {
                binding.progressHomeNews.visibility = View.GONE
            }
        }
    }

    private fun checkNewNewsNotification(newsList: List<News>) {
        if (newsList.isEmpty()) return
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        val lastSeenId = sharedPref.getInt("last_seen_news_id", 0)
        val latestIdInList = newsList.maxOf { it.id }

        if (latestIdInList > lastSeenId) {
            ReminderReceiver.showNotification(
                requireContext(),
                103,
                "Informasi Bencana Terbaru",
                "Ada informasi bencana terbaru di wilayah Indonesia."
            )
            sharedPref.edit().putInt("last_seen_news_id", latestIdInList).apply()
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