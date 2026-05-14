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
        // Splash screen del sistema (Android 12+). En versiones anteriores
        // muestra un fallback simple con el ícono y el color de marca.
        installSplashScreen()
        // Aplicar el tema final antes de inflar la UI (evita el flash blanco).
        setTheme(R.style.Theme_NodoCivico)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNav: BottomNavigationView = binding.bottomNavigation
        bottomNav.setupWithNavController(navController)

        // El splash y el detalle no deben mostrar bottom navigation.
        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.splashFragment,
                R.id.reportDetailFragment,
                R.id.createReportFragment -> android.view.View.GONE
                else -> android.view.View.VISIBLE
            }
        }

        // Top-level destinations: en estas pantallas no aparece la flecha de back.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.reportListFragment,
                R.id.syncStatusFragment,
                R.id.settingsFragment
            )
        )
        // El layout no usa Toolbar en el Entregable 1: la toolbar se añadirá
        // en el Entregable 2 cuando ya haya un flujo CRUD completo.
        @Suppress("UNUSED_VARIABLE")
        val unused = appBarConfiguration
    }
}
