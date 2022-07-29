package com.bnyro.trivia.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.bnyro.trivia.AUTHOR_URL
import com.bnyro.trivia.BuildConfig
import com.bnyro.trivia.GITHUB_URL
import com.bnyro.trivia.LICENSE_URL
import com.bnyro.trivia.databinding.ActivityAboutBinding
import com.bnyro.trivia.util.ThemeHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.net.URL

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.setThemeMode(this)
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)

        binding.back.setOnClickListener {
            onBackPressed()
        }
        binding.version.text = BuildConfig.VERSION_NAME

        binding.github.setOnClickListener {
            openBrowserIntent(GITHUB_URL)
        }

        binding.author.setOnClickListener {
            openBrowserIntent(AUTHOR_URL)
        }

        binding.license.setOnClickListener {
            showLicenseDialog()
        }

        setContentView(binding.root)
    }

    private fun openBrowserIntent(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.data = Uri.parse(url)
        startActivity(browserIntent)
    }

    private fun showLicenseDialog() {
        var licenseText = ""
        val thread = Thread {
            licenseText = URL(LICENSE_URL).readText()
        }
        thread.start()
        thread.join()
        val licenseHTML = Html.fromHtml(
            licenseText
                .substringAfter(
                    "<!-- The license text is in English and appears broken in RTL as\n" +
                        "     Arabic, Farsi, etc.  Explicitly set the direction to override the\n" +
                        "     one defined in the translation. -->"
                )
                .substringBefore("END OF TERMS AND CONDITIONS")
        )
        MaterialAlertDialogBuilder(this)
            .setMessage(licenseHTML)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .show()
    }
}
