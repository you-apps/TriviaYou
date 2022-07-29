package com.bnyro.trivia.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.ActivitySettingsBinding
import com.bnyro.trivia.fragments.SettingsFragment
import com.bnyro.trivia.util.ThemeHelper

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.setThemeMode(this)
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)

        binding.backImageButton.setOnClickListener {
            onBackPressed()
        }

        setContentView(binding.root)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }
}
