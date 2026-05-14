package com.example.dipa_flower.Home.tugasp3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dipa_flower.MainActivity
import com.example.dipa_flower.databinding.ActivityDashboardBinding
import com.example.dipa_flower.Home.tugasp4.HalamanActivity
import com.example.dipa_flower.Home.tugasp4.ScreenActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Ambil username dari intent
        val username = intent.getStringExtra("USERNAME") ?: "User"

        // Tampilkan ke TextView
        binding.textView7.text = "Anda Telah Masuk Dashboard\n$username"

        // 🔵 Tombol KALKULATOR → ke MainActivity
        binding.button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // 🔴 Tombol KEMBALI KE LOGIN → ke LoginActivity
        binding.btnMain.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)

            // Hapus semua activity sebelumnya
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }

        // 🟢 Tombol ke HALAMAN
        binding.btnHalaman.setOnClickListener {
            val intent = Intent(this, HalamanActivity::class.java)
            startActivity(intent)
        }

// 🟣 Tombol ke SCREEN
        binding.btnScreen.setOnClickListener {
            val intent = Intent(this, ScreenActivity::class.java)
            startActivity(intent)
        }
    }
}