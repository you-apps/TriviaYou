package com.bnyro.trivia.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bnyro.trivia.R
import com.bnyro.trivia.databinding.ActivityMainBinding
import com.bnyro.trivia.models.SearchViewModel
import com.bnyro.trivia.util.BundleArguments
import com.bnyro.trivia.util.PreferenceHelper
import com.bnyro.trivia.util.ThemeHelper
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var searchView: SearchView

    private var startFragmentId = R.id.homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        ThemeHelper.setThemeMode(this)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.fragment)
        binding.bottomNavigationView.setupWithNavController(navController)

        binding.bottomNavigationView.labelVisibilityMode =
            when (
                PreferenceHelper.getString(
                    getString(R.string.label_visibility_key),
                    getString(R.string.label_visibility_default)
                )
            ) {
                "selected" -> NavigationBarView.LABEL_VISIBILITY_SELECTED
                "labeled" -> NavigationBarView.LABEL_VISIBILITY_LABELED
                "unlabeled" -> NavigationBarView.LABEL_VISIBILITY_UNLABELED
                else -> NavigationBarView.LABEL_VISIBILITY_AUTO
            }

        binding.bottomNavigationView.setOnItemSelectedListener {
            // set menu item on click listeners
            when (it.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.categoriesFragment -> {
                    navController.navigate(R.id.categoriesFragment)
                }
                R.id.libraryFragment -> {
                    navController.navigate(R.id.libraryFragment)
                }
            }
            false
        }

        // save start tab fragment id
        startFragmentId =
            when (PreferenceHelper.getString(getString(R.string.default_tab_key), "home")) {
                "home" -> R.id.homeFragment
                "categories" -> R.id.categoriesFragment
                "library" -> R.id.libraryFragment
                else -> R.id.homeFragment
            }

        // set default tab as start fragment
        navController.graph.setStartDestination(startFragmentId)

        // navigate to the default fragment
        navController.navigate(startFragmentId)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView

        val searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // do nothing when submitted
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (navController.currentDestination?.id != R.id.searchFragment) {
                    val bundle = bundleOf(BundleArguments.query to newText)
                    navController.navigate(R.id.searchFragment, bundle)
                } else {
                    searchViewModel.setQuery(newText)
                }
                return true
            }
        })

        searchItem.setOnActionExpandListener(
            object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                    if (navController.currentDestination?.id == R.id.searchFragment) onBackPressed()
                    return true
                }
            }
        )

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                true
            }
            R.id.action_about -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
                true
            }
            R.id.action_api_stats -> {
                navController.navigate(R.id.apiStatsFragment)
                true
            }
            R.id.action_user_stats -> {
                navController.navigate(R.id.userStatsFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == startFragmentId) {
            super.onBackPressed()
        } else if (navController.backQueue.isNotEmpty()) {
            navController.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
}
