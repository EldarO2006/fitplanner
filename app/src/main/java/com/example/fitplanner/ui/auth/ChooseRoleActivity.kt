package com.example.fitplanner.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitplanner.databinding.ActivityChooseRoleBinding

class ChooseRoleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseRoleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChooseRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAthlete.setOnClickListener {
            startActivity(Intent(this, RegisterAthleteActivity::class.java))
        }

        binding.btnTrainer.setOnClickListener {
            startActivity(Intent(this, RegisterTrainerActivity::class.java))
        }
    }
}
