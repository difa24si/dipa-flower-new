package com.example.dipa_flower.Home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dipa_flower.MainActivity
import com.example.dipa_flower.R
import com.example.dipa_flower.WebViewActivity
import com.example.dipa_flower.databinding.FragmentHomeBinding
import com.example.dipa_flower.tugasp3.LoginActivity
import com.example.dipa_flower.tugasp4.HalamanActivity
import com.example.dipa_flower.tugasp4.ScreenActivity

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnWebView.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnKalku.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        // 🟢 Tombol ke HALAMAN
        binding.btnHalaman.setOnClickListener {
            val intent = Intent(requireContext(), HalamanActivity::class.java)
            startActivity(intent)
        }

// 🟣 Tombol ke SCREEN
        binding.btnScreen.setOnClickListener {
            val intent = Intent(requireContext(), ScreenActivity::class.java)
            startActivity(intent)
        }

    }
}