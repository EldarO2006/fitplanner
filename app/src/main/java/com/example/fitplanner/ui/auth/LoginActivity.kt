package com.example.fitplanner.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.UserType
import com.example.fitplanner.databinding.ActivityLoginBinding
import com.example.fitplanner.ui.admin.AdminMainActivity
import com.example.fitplanner.ui.athlete.AthleteMainActivity
import com.example.fitplanner.ui.trainer.TrainerMainActivity
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val db by lazy { AppDatabase.getInstance(this) }
    private val session by lazy { SessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 АВТОВХОД
        if (session.userId != -1L) {
            openMain(session.userType)
            return
        }

        // 🔹 ВХОД
        binding.btnLogin.setOnClickListener {
            login()
        }

        // 🔹 РЕГИСТРАЦИЯ СПОРТСМЕНА
        binding.btnRegisterAthlete.setOnClickListener {
            startActivity(Intent(this, RegisterAthleteActivity::class.java))
        }

        // 🔹 РЕГИСТРАЦИЯ ТРЕНЕРА
        binding.btnRegisterTrainer.setOnClickListener {
            startActivity(Intent(this, RegisterTrainerActivity::class.java))
        }


    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(this, "Введите email и пароль", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔴 ВХОД АДМИНА
        if (email == "admin" && password == "admin") {
            session.saveUser(
                userId = -999L,
                name = "Admin",
                email = "admin",
                userType = UserType.ADMIN
            )
            openMain(UserType.ADMIN)
            return
        }

        // 🔹 Обычный вход
        lifecycleScope.launch {
            val user = db.userDao().getByEmail(email)

            if (user == null || user.passwordHash != password) {
                runOnUiThread {
                    Toast.makeText(
                        this@LoginActivity,
                        "Неверный email или пароль",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return@launch
            }

            session.saveUser(
                userId = user.id,
                name = user.name,
                email = user.email,
                userType = user.userType,
                trainerId = user.trainerId ?: -1
            )

            runOnUiThread {
                openMain(user.userType)
            }
        }
    }

    private fun openMain(type: UserType) {
        val intent = when (type) {
            UserType.ADMIN -> Intent(this, AdminMainActivity::class.java)
            UserType.TRAINER -> Intent(this, TrainerMainActivity::class.java)
            UserType.ATHLETE -> Intent(this, AthleteMainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
