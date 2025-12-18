package com.example.fitplanner.ui.trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.databinding.FragmentTrainerPlansBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class TrainerPlansFragment : Fragment() {

    private var _binding: FragmentTrainerPlansBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private val session by lazy { SessionManager(requireContext()) }

    private lateinit var adapter: TrainerPlansAdapter
    private var athletes: List<UserEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerPlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настройка RecyclerView
        adapter = TrainerPlansAdapter()
        binding.recyclerTrainerPlans.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerTrainerPlans.adapter = adapter

        // Загрузка атлетов и планов
        loadAthletes()
        loadAllPlans()

        // Обработка клика по кнопке добавления тренировки
        binding.btnAddPlan.setOnClickListener {
            val dialog = CreatePlanDialog(requireContext(), session.userId) { newPlan ->
                // Сохраняем план в базе
                lifecycleScope.launch {
                    db.trainingPlanDao().insert(newPlan)
                    // Обновляем список после добавления
                    loadAllPlans()
                }
            }
            dialog.show()
        }

        // Чтобы список спиннера выпадал при нажатии
        binding.spinnerAthletes.setOnClickListener {
            binding.spinnerAthletes.showDropDown()
        }
    }

    private fun loadAthletes() {
        lifecycleScope.launch {
            athletes = db.userDao().getAthletesByTrainer(session.userId)

            val names = mutableListOf("Все атлеты")
            names.addAll(athletes.map { it.name })

            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                names
            )
            binding.spinnerAthletes.setAdapter(spinnerAdapter)

            binding.spinnerAthletes.setOnItemClickListener { _, _, position, _ ->
                if (position == 0) {
                    loadAllPlans()
                } else {
                    val athlete = athletes[position - 1]
                    loadPlansForAthlete(athlete.id)
                }
            }
        }
    }

    private fun loadAllPlans() {
        lifecycleScope.launch {
            val plans = db.trainingPlanDao().getAllForTrainer(session.userId)
            adapter.submitList(plans)
        }
    }

    private fun loadPlansForAthlete(athleteId: Long) {
        lifecycleScope.launch {
            val plans = db.trainingPlanDao().getForTrainerAndAthlete(session.userId, athleteId)
            adapter.submitList(plans)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
