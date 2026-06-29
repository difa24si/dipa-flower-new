package com.example.dipa_flower.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.dipa_flower.BaseActivity
import com.example.dipa_flower.databinding.FragmentInfoBinding
import com.google.android.material.tabs.TabLayoutMediator

class InfoFragment : Fragment() {
    private var _binding: FragmentInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabTitles = listOf("Berita Bencana", "Panduan Mitigasi", "Lokasi Evakuasi")
        
        binding.viewPager.adapter = InfoPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

        // Check if redirected from Home quick actions
        val tabToOpen = (activity as? BaseActivity)?.infoTabToOpen ?: 0
        if (tabToOpen in 0..2) {
            binding.viewPager.post {
                binding.viewPager.setCurrentItem(tabToOpen, false)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Reset tab index on resume so subsequent clicks on BottomNavigationView open default tab 0
        (activity as? BaseActivity)?.infoTabToOpen = 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private class InfoPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> NewsTabFragment()
                1 -> MitigationTabFragment()
                2 -> EvacuationTabFragment()
                else -> NewsTabFragment()
            }
        }
    }
}
