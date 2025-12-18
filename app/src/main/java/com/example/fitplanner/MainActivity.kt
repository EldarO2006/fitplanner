package com.example.fitplanner

import com.example.fitplanner.ui.trainer.TrainerStatsFragment
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.fitplanner.databinding.ActivityMainBinding
import com.example.fitplanner.ui.trainer.TrainerPlansFragment
import com.example.fitplanner.ui.trainer.TrainerHomeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openFragment(TrainerHomeFragment())

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> openFragment(TrainerHomeFragment())
                R.id.menu_plans -> openFragment(TrainerPlansFragment())
                R.id.menu_stats -> openFragment(TrainerStatsFragment())
            }
            true
        }
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
