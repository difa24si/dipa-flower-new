package com.example.dipa_flower.Home.tugasp3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dipa_flower.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button2.setOnClickListener {

            val email = binding.editTextTextEmailAddress2.text.toString()
            val password = binding.editTextTextPassword.text.toString()

            // bebas login (asal isi)
            if (email.isNotEmpty() && password.isNotEmpty()) {

                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("USERNAME", email)

                startActivity(intent)
                finish() // biar tidak bisa balik ke login pakai tombol back

            } else {
                Toast.makeText(this, "Isi dulu email & password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}