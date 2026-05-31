package com.example.dipa_flower.Home.tugasp4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dipa_flower.R

class HalamanActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_halaman)

        val rvNews = findViewById<RecyclerView>(R.id.rvNews)

        val beritaList = listOf(
            News(
                "Gotong Royong Desa",
                "Warga desa melaksanakan kerja bakti membersihkan jalan dan saluran air."
            ),
            News(
                "Bantuan Desa",
                "Pemerintah desa menyalurkan bantuan kepada warga yang membutuhkan."
            ),
            News(
                "Posyandu Bulanan",
                "Kegiatan pemeriksaan kesehatan ibu dan anak berjalan lancar."
            ),
            News(
                "Pelatihan UMKM",
                "Pelatihan pembuatan produk dan pemasaran digital bagi UMKM desa."
            ),
            News(
                "Musyawarah Desa",
                "Pembahasan program pembangunan dan anggaran desa tahun ini."
            )
        )

        rvNews.layoutManager = LinearLayoutManager(this)
        rvNews.adapter = NewsAdapter(beritaList)
    }
}