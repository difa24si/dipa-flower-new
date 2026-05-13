package com.example.dipa_flower

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)

        lifecycleScope.launch {

            delay(2000)

            val sharedPref = getSharedPreferences(
                "user_pref",
                MODE_PRIVATE
            )

            val isLogin = sharedPref.getBoolean(
                "isLogin",
                false
            )

            // Jika sudah login
            if (isLogin) {

                startActivity(
                    Intent(
                        this@SplashScreenActivity,
                        MainActivity::class.java
                    )
                )

            } else {

                // Jika belum login
                startActivity(
                    Intent(
                        this@SplashScreenActivity,
                        AuthActivity::class.java
                    )
                )
            }

            finish()
        }
    }
}