package com.example.fitplanner.ui.trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.databinding.FragmentTrainerHomeBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class TrainerHomeFragment : Fragment() {

    private var _binding: FragmentTrainerHomeBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private val session by lazy { SessionManager(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadTrainerInfo()
        loadAthleteCount()
        setupLogout()
    }

    private fun loadTrainerInfo() {
        lifecycleScope.launch {
            val trainer: UserEntity? = db.userDao().getById(session.userId)
            trainer?.let {
                binding.tvTrainerName.text = it.name
                binding.tvTrainerEmail.text = it.email
                binding.tvTrainerExperience.text = "Стаж: ${it.experienceYears} лет"
            }
        }
    }

    private fun loadAthleteCount() {
        lifecycleScope.launch {
            val count = db.userDao().countAthletesByTrainer(session.userId)
            binding.tvAthleteCount.text = "Количество атлетов: $count"
        }
    }
    private fun setupLogout() {
        binding.btnLogout.setOnClickListener {
            session.clear() // очищаем все данные сессии
            // Переход на LoginActivity
            val intent = android.content.Intent(requireContext(), com.example.fitplanner.ui.auth.LoginActivity::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
