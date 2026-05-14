package com.example.dipa_flower.Home.tugasp5

import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.dipa_flower.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // WebView
        binding.webView.webViewClient = WebViewClient()

        binding.webView.settings.javaScriptEnabled = true

        binding.webView.loadUrl("https://www.merdeka.com")
    }

    override fun onBackPressed() {

        if (binding.webView.canGoBack()) {

            binding.webView.goBack()

        } else {

            super.onBackPressed()
        }
    }
}