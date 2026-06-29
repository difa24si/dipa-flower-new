package com.example.dipa_flower

import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.dipa_flower.Home.HomeFragment
import com.example.dipa_flower.Profile.ProfileFragment
import com.example.dipa_flower.checklist.ChecklistFragment
import com.example.dipa_flower.contact.EmergencyContactFragment
import com.example.dipa_flower.databinding.ActivityBaseBinding
import com.example.dipa_flower.info.InfoFragment
import com.example.dipa_flower.receiver.ReminderReceiver

class BaseActivity : AppCompatActivity() {
    lateinit var binding: ActivityBaseBinding
    var infoTabToOpen = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        // Request notification permissions for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 999)
            }
        }

        // Schedule daily and weekly alarms
        ReminderReceiver.scheduleAlarms(this)

        replaceFragment(HomeFragment())

        binding.bottomNavView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.info -> {
                    replaceFragment(InfoFragment())
                    true
                }
                R.id.checklist -> {
                    replaceFragment(ChecklistFragment())
                    true
                }
                R.id.kontak -> {
                    replaceFragment(EmergencyContactFragment())
                    true
                }
                R.id.profile -> {
                    replaceFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    fun navigateToTab(itemId: Int, tabIndex: Int = 0) {
        infoTabToOpen = tabIndex
        binding.bottomNavView.selectedItemId = itemId
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
            .commit()
    }
}
