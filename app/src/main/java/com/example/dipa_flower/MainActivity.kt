package com.example.dipa_flower

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        // Inset
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )

            insets
        }

        // SharedPreferences
        val sharedPref = getSharedPreferences(
            "user_pref",
            MODE_PRIVATE
        )

        // Inisialisasi Button
        val btnWebView = findViewById<Button>(R.id.btnWebView)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // BUTTON WEBVIEW
        btnWebView.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    WebViewActivity::class.java
                )
            )
        }

        // BUTTON LOGOUT
        btnLogout.setOnClickListener {

            AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Yakin ingin logout?")
                .setPositiveButton("Ya") { dialog, _ ->

                    // Hapus SharedPreferences
                    val editor = sharedPref.edit()

                    editor.clear()
                    editor.apply()

                    // Kembali ke AuthActivity
                    startActivity(
                        Intent(
                            this,
                            AuthActivity::class.java
                        )
                    )

                    finish()

                    dialog.dismiss()
                }

                .setNegativeButton("Tidak", null)
                .show()
        }
    }
}