package com.example.dipa_flower.Home

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.dipa_flower.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Setup WebView
        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl("https://difa05.alwaysdata.net/")
        }

        // 2. AKTIFKAN TOMBOL BACK DI XML (Penting!)
        binding.btnBack.setOnClickListener {
            // Ini akan memicu dispatcher yang kita buat di bawah
            onBackPressedDispatcher.onBackPressed()
        }

        // 3. LOGIKA BACK (WEB vs ACTIVITY)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.webView.canGoBack()) {
                    // Jika web bisa mundur, maka mundur di web
                    binding.webView.goBack()
                } else {
                    // Jika web sudah di halaman paling awal, tutup activity
                    isEnabled = false // Matikan callback agar tidak looping
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        }
        // Daftarkan callback ke sistem
        onBackPressedDispatcher.addCallback(this, callback)
    }
}