package com.nodocivico.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nodocivico.app.databinding.ActivityMainBinding
import com.nodocivico.app.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        setTheme(R.style.Theme_NodoCivico)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar conectividad con la API al iniciar
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.health()
                if (response.isSuccessful) {
                    Log.d("API", "API activa: ${response.body()}")
                } else {
                    Log.w("API", "API respondio con codigo: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API", "No se pudo conectar a la API: ${e.message}")
            }
        }

        // Sincronizar al iniciar la app
        val repository = (application as NodoCivicoApp).reportRepository
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = repository.sync()
                Log.d("API", "Sync inicial: pushed=${result.pushed}, pulled=${result.pulled}")
            } catch (e: Exception) {
                Log.e("API", "Error en sync inicial: ${e.message}")
            }
        }

        // ---------------- Navigation ----------------

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment

        navController = navHostFragment.navController

        val bottomNav: BottomNavigationView = binding.bottomNavigation

        bottomNav.setupWithNavController(navController)

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
