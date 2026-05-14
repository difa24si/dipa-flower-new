package com.example.dipa_flower.Home.tugasp4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.dipa_flower.R
import android.widget.Button

class ScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen)

        val btnStart = findViewById<Button>(R.id.btnGetStarted)

        btnStart.setOnClickListener {
            // pindah ke halaman berikutnya (misalnya HalamanActivity)
            startActivity(Intent(this, HalamanActivity::class.java))
        }
    }
}