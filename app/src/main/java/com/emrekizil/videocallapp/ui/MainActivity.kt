package com.emrekizil.videocallapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.emrekizil.videocallapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        lifecycleScope.launch {
            val isUserRegistered = viewModel.isUserRegistered()
            with(navController){
                graph = navInflater.inflate(R.navigation.nav_graph).apply {
                    setStartDestination(getStartDestinationId(isUserRegistered))
                }
            }
        }

    }

    private fun getStartDestinationId(userRegistered: Boolean): Int {
        return if (userRegistered) {
            R.id.home_navigation
        } else {
            R.id.login_navigation
        }
    }
}