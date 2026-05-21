package com.nodocivico.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nodocivico.app.databinding.ActivityMainBinding

/**
 * Activity única que aloja el NavHostFragment.
 *
 * Toda la navegación entre pantallas se hace con Navigation Component, tal
 * como exige el enunciado ("Uso de fragments como base de navegación").
 *
 * La BottomNavigationView aparece sólo después del Splash; mientras se está
 * en SplashFragment se oculta para presentar una pantalla de bienvenida limpia.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        setTheme(R.style.Theme_NodoCivico)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav: BottomNavigationView = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)

        // Ocultar bottom nav en pantallas que no son top-level
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.splashFragment,
                R.id.reportDetailFragment,
                R.id.createReportFragment,
                R.id.editReportFragment,
                R.id.calendarRemindersFragment -> android.view.View.GONE
                else -> android.view.View.VISIBLE
            }
        }
    }
}
