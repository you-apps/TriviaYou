package com.bnyro.trivia.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnyro.trivia.API_URL
import com.bnyro.trivia.BuildConfig
import com.bnyro.trivia.GITHUB_URL
import com.bnyro.trivia.LICENSE_URL
import com.bnyro.trivia.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)

        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.version.text = BuildConfig.VERSION_NAME

        binding.github.setOnClickListener {
            openBrowserIntent(GITHUB_URL)
        }

        binding.api.setOnClickListener {
            openBrowserIntent(API_URL)
        }

        binding.license.setOnClickListener {
            openBrowserIntent(LICENSE_URL)
        }

        setContentView(binding.root)
    }

    private fun openBrowserIntent(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(url)
        startActivity(browserIntent)
    }
}
