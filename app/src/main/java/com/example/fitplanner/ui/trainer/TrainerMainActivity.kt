package com.example.fitplanner.ui.trainer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitplanner.R
import com.example.fitplanner.databinding.ActivityTrainerMainBinding

class TrainerMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTrainerMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrainerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, TrainerHomeFragment())
            .commit()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_home -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TrainerHomeFragment())
                        .commit()
                    true
                }
                R.id.menu_plans -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TrainerPlansFragment())
                        .commit()
                    true
                }
                R.id.menu_athletes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, TrainerAthletesFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }
}
