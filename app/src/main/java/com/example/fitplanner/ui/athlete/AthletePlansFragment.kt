package com.example.fitplanner.ui.athlete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplanner.R
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.TrainingPlanEntity
import com.example.fitplanner.databinding.FragmentAthletePlansBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class AthletePlansFragment : Fragment() {

    private var _binding: FragmentAthletePlansBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private val session by lazy { SessionManager(requireContext()) }

    private lateinit var adapter: AthletePlansAdapter
    private var allPlans: List<TrainingPlanEntity> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAthletePlansBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Настраиваем RecyclerView
        adapter = AthletePlansAdapter { plan ->
            findNavController().navigate(
                R.id.action_to_planDetails,
                Bundle().apply { putLong("planId", plan.id) }
            )
        }
        binding.recyclerViewPlans.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@AthletePlansFragment.adapter
        }

        // Загружаем данные и фильтры
        setupFilters()
        loadPlans()
    }

    private fun setupFilters() {
        // Все
        binding.chipAll.setOnClickListener { applyFilter(Filter.ALL) }
        // В обработке
        binding.chipActive.setOnClickListener { applyFilter(Filter.ACTIVE) }
        // Выполненные
        binding.chipCompleted.setOnClickListener { applyFilter(Filter.COMPLETED) }
    }

    private fun applyFilter(filter: Filter) {
        val filtered = when (filter) {
            Filter.ALL -> allPlans
            Filter.ACTIVE -> allPlans.filter { !it.isCompleted }
            Filter.COMPLETED -> allPlans.filter { it.isCompleted }
        }
        adapter.submitList(filtered)
    }

    private fun loadPlans() {
        lifecycleScope.launch {
            allPlans = db.trainingPlanDao().getForAthlete(
                athleteId = session.userId,
                trainerId = session.trainerId
            )
            // Сразу показываем все
            applyFilter(Filter.ALL)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private enum class Filter {
        ALL, ACTIVE, COMPLETED
    }
}
