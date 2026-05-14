package com.example.dipa_flower.Home

import android.content.Context // Tambahkan ini
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import com.example.dipa_flower.AuthActivity
import com.example.dipa_flower.MainActivity
import com.example.dipa_flower.databinding.FragmentHomeBinding
import com.example.dipa_flower.Home.tugasp4.HalamanActivity
import com.example.dipa_flower.Home.tugasp4.ScreenActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inisialisasi SharedPreferences
        // Ganti "user_pref" dengan nama file SharedPreferences yang Anda gunakan saat Login
        val sharedPref = requireContext().getSharedPreferences("user_pref", Context.MODE_PRIVATE)

        binding.btnWebView.setOnClickListener {
            val intent = Intent(requireContext(), WebViewActivity::class.java)
            startActivity(intent)
        }

        binding.btnKalku.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnHalaman.setOnClickListener {
            val intent = Intent(requireContext(), HalamanActivity::class.java)
            startActivity(intent)
        }

        binding.btnScreen.setOnClickListener {
            val intent = Intent(requireContext(), ScreenActivity::class.java)
            startActivity(intent)
        }

        // 2. Logika Logout
        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Konfirmasi")
                .setMessage("Apakah Anda yakin ingin keluar?")
                .setPositiveButton("Ya") { dialog, _ ->
                    // Hapus data login
                    sharedPref.edit {
                        clear()
                    }

                    dialog.dismiss()

                    // Pindah ke AuthActivity (Halaman Login)
                    val intent = Intent(requireContext(), AuthActivity::class.java)
                    // Clear task agar user tidak bisa tekan tombol 'Back' kembali ke Home
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}