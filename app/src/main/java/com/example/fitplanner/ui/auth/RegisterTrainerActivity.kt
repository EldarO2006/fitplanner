package com.example.fitplanner.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.data.db.entities.UserType
import com.example.fitplanner.databinding.ActivityRegisterTrainerBinding
import com.example.fitplanner.ui.trainer.TrainerMainActivity
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class RegisterTrainerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterTrainerBinding
    private val db by lazy { AppDatabase.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterTrainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener { register() }
    }

    private fun register() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val experience = binding.etExperience.text.toString().toIntOrNull()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || experience == null) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 8) {
            binding.etPassword.error = "Пароль должен быть минимум 8 символов"
            return
        }

        lifecycleScope.launch {
            // Проверяем уникальность email
            val existingUser = db.userDao().getByEmail(email)
            if (existingUser != null) {
                binding.etEmail.error = "Пользователь с таким email уже существует"
                return@launch
            }

            val user = UserEntity(
                name = name,
                email = email,
                passwordHash = password,
                userType = UserType.TRAINER,
                experienceYears = experience,
                registrationDate = System.currentTimeMillis()
            )

            val id = db.userDao().insert(user)

            SessionManager(this@RegisterTrainerActivity).saveUser(
                userId = id,
                name = name,
                email = email,
                userType = UserType.TRAINER
            )

            Toast.makeText(this@RegisterTrainerActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@RegisterTrainerActivity, TrainerMainActivity::class.java))
            finish()
        }
    }
}
