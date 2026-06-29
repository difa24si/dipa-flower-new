package com.example.dipa_flower.report

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.dipa_flower.R
import com.example.dipa_flower.databinding.ActivityDisasterReportBinding

class DisasterReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDisasterReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDisasterReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutHeader) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, systemBars.top, 0, 0)
            insets
        }

        binding.btnBack.setOnClickListener {
            handleBackNavigation()
        }

        if (savedInstanceState == null) {
            replaceFragment(ReportListFragment(), false)
        }
    }

    fun setHeaderTitle(title: String) {
        binding.tvHeaderTitle.text = title
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment)
        
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    private fun handleBackNavigation() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        handleBackNavigation()
        return true
    }
}
