package com.example.fitplanner.ui.athlete

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.fitplanner.R
import com.example.fitplanner.databinding.ActivityAthleteMainBinding

class AthleteMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAthleteMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAthleteMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_athlete) as NavHostFragment

        val navController = navHostFragment.navController

        // 🔥 ВАЖНО: BottomNav работает через NavController
        binding.bottomNavigation.setupWithNavController(navController)
    }
}
