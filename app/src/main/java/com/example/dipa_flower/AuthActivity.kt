package com.example.dipa_flower

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AuthActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_auth)

        // SharedPreferences
        val sharedPref = getSharedPreferences("user_pref", MODE_PRIVATE)

        // Jika sudah login
        val isLogin = sharedPref.getBoolean("isLogin", false)

        if (isLogin) {
            startActivity(Intent(this, BaseActivity::class.java))
            finish()
        }

        // Hubungkan komponen
        etUsername = findViewById(R.id.username)
        etPassword = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnLogin)

        // Tombol Login
        btnLogin.setOnClickListener {

            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            // Login berhasil
            if (username == password && username.isNotEmpty()) {

                // Simpan session login
                val editor = sharedPref.edit()
                editor.putBoolean("isLogin", true)
                editor.putString("username", username)
                editor.apply()

                // Pindah ke MainActivity
                startActivity(Intent(this, BaseActivity::class.java))
                finish()

            } else {

                // Login gagal
                AlertDialog.Builder(this)
                    .setTitle("Peringatan")
                    .setMessage("Silahkan coba lagi")
                    .setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
}