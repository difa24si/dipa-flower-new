package com.example.dipa_flower

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.dipa_flower.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val items = listOf(
            OnboardingItem(
                "Selamat Datang",
                "Aplikasi informasi dan kesiapsiagaan bencana untuk masyarakat.",
                R.drawable.illustration_welcome
            ),
            OnboardingItem(
                "Pantau Informasi Resmi",
                "Dapatkan berita kebencanaan terbaru dari sumber terpercaya.",
                R.drawable.illustration_news
            ),
            OnboardingItem(
                "Siap Menghadapi Bencana",
                "Kelola informasi, pelajari mitigasi, dan aktifkan pengingat kesiapsiagaan.",
                R.drawable.illustration_readiness
            )
        )

        onboardingAdapter = OnboardingAdapter(items)
        binding.viewPager.adapter = onboardingAdapter

        setupIndicators(items.size)
        setCurrentIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
                if (position == items.size - 1) {
                    binding.btnSkip.visibility = View.GONE
                    binding.btnNext.visibility = View.GONE
                    binding.btnStart.visibility = View.VISIBLE
                } else {
                    binding.btnSkip.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                    binding.btnStart.visibility = View.GONE
                }
            }
        })

        binding.btnNext.setOnClickListener {
            val nextIndex = binding.viewPager.currentItem + 1
            if (nextIndex < items.size) {
                binding.viewPager.currentItem = nextIndex
            }
        }

        binding.btnSkip.setOnClickListener {
            completeOnboarding()
        }

        binding.btnStart.setOnClickListener {
            completeOnboarding()
        }
    }

    private fun setupIndicators(size: Int) {
        val indicators = arrayOfNulls<ImageView>(size)
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(8, 0, 8, 0)
        }
        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext).apply {
                setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.badge_bg // we can use a smaller circle or shape
                    )
                )
                layoutParams.width = 24
                layoutParams.height = 24
                this.layoutParams = layoutParams
            }
            binding.layoutIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.layoutIndicators.childCount
        for (i in 0 until childCount) {
            val imageView = binding.layoutIndicators.getChildAt(i) as ImageView
            if (i == index) {
                imageView.alpha = 1.0f
                imageView.scaleX = 1.2f
                imageView.scaleY = 1.2f
            } else {
                imageView.alpha = 0.4f
                imageView.scaleX = 1.0f
                imageView.scaleY = 1.0f
            }
        }
    }

    private fun completeOnboarding() {
        val sharedPref = getSharedPreferences("user_pref", Context.MODE_PRIVATE)
        sharedPref.edit().putBoolean("isFirstRun", false).apply()

        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    data class OnboardingItem(
        val title: String,
        val description: String,
        val imageRes: Int
    )

    inner class OnboardingAdapter(private val items: List<OnboardingItem>) :
        RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_onboarding, parent, false)
            return OnboardingViewHolder(view)
        }

        override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
            holder.bind(items[position])
        }

        override fun getItemCount(): Int = items.size

        inner class OnboardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            private val imgIllustration: ImageView = view.findViewById(R.id.imgIllustration)
            private val tvTitle: TextView = view.findViewById(R.id.tvTitle)
            private val tvDescription: TextView = view.findViewById(R.id.tvDescription)

            fun bind(item: OnboardingItem) {
                imgIllustration.setImageResource(item.imageRes)
                tvTitle.text = item.title
                tvDescription.text = item.description
            }
        }
    }
}
