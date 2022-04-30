package com.example.myfilms.presentation.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myfilms.R
import com.example.myfilms.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var toolbarLayout: AppBarLayout
    private lateinit var toolbar: MaterialToolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sideBar: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        bottomNavInit()
        sideBarInit()
        setVisibility()
    }

    private fun setVisibility() {
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.movies_fragment -> {
                    bottomNavigation.visibility = View.VISIBLE
                    toolbarLayout.visibility = View.VISIBLE
                }
                R.id.favouritesFragment -> {
                    bottomNavigation.visibility = View.VISIBLE
                    toolbarLayout.visibility = View.VISIBLE
                }
                R.id.details_fragment -> {
                    bottomNavigation.visibility = View.GONE
                    toolbarLayout.visibility = View.GONE
                }
                R.id.login_fragment -> {
                    bottomNavigation.visibility = View.GONE
                    toolbarLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun init() {

        navController = findNavController(R.id.main_container)
       /* bottomNavigation = binding.contentMain.bottomNavigation
        toolbarLayout = binding.contentMain.toolbarLayout
        toolbar = binding.contentMain.topToolbar*/

        bottomNavigation = findViewById(R.id.bottom_navigation)
        toolbarLayout = findViewById(R.id.toolbarLayout)
        toolbar = findViewById(R.id.topToolbar)

        drawerLayout = binding.drawerMainActivity
        sideBar = binding.sideNavigation
    }

    private fun sideBarInit() {

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.movies_nav,
                R.id.favorites_nav,
                R.id.settings,
                R.id.about,
                R.id.exit,
                R.id.share,
                R.id.rate_us
            ), drawerLayout
        )
        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        sideBar.setupWithNavController(navController)

    }


    private fun bottomNavInit() {
        bottomNavigation.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED
        bottomNavigation.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    companion object{
        var  isFirstDownloaded = false
    }

}