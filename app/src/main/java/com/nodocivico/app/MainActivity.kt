package com.nodocivico.app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nodocivico.app.databinding.ActivityMainBinding
import com.nodocivico.app.network.Post
import com.nodocivico.app.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        installSplashScreen()
        setTheme(R.style.Theme_NodoCivico)

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ---------------- API REST ----------------

        RetrofitClient.api.getPosts().enqueue(object : Callback<List<Post>> {

            override fun onResponse(
                call: Call<List<Post>>,
                response: Response<List<Post>>
            ) {

                if (response.isSuccessful) {

                    Log.d(
                        "API_TEST",
                        "Posts obtenidos: ${response.body()?.size}"
                    )
                }
            }

            override fun onFailure(
                call: Call<List<Post>>,
                t: Throwable
            ) {

                Log.e(
                    "API_TEST",
                    "Error API: ${t.message}"
                )
            }
        })

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