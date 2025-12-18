package com.example.fitplanner.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fitplanner.R
import com.example.fitplanner.databinding.ActivityAdminMainBinding
import com.example.fitplanner.ui.auth.LoginActivity
import com.example.fitplanner.utils.SessionManager

class AdminMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminMainBinding
    private val session by lazy { SessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // стартовый экран
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AdminAssignFragment())
            .commit()

        // кнопка выхода
        binding.btnLogout.setOnClickListener {
            session.clear()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
