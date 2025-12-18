package com.example.fitplanner.ui.athlete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.databinding.FragmentAthletePlanDetailsBinding
import kotlinx.coroutines.launch

class AthletePlanDetailsFragment : Fragment() {

    private var _binding: FragmentAthletePlanDetailsBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private var planId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔴 Безопасно получаем аргумент
        planId = requireArguments().getLong(ARG_PLAN_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAthletePlanDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadPlan()
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnComplete.setOnClickListener {
            completePlan()
        }
    }

    private fun loadPlan() {
        lifecycleScope.launch {
            val plan = db.trainingPlanDao().getById(planId)

            if (plan == null) {
                Toast.makeText(requireContext(), "План не найден", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
                return@launch
            }

            binding.tvTitle.text = plan.title
            binding.tvDescription.text = plan.description

            // 👇 если уже выполнен — блокируем кнопку
            if (plan.isCompleted) {
                binding.btnComplete.isEnabled = false
                binding.btnComplete.text = "План выполнен"
            }
        }
    }

    private fun completePlan() {
        lifecycleScope.launch {
            db.trainingPlanDao().markCompleted(planId)

            Toast.makeText(
                requireContext(),
                "Тренировка выполнена 💪",
                Toast.LENGTH_SHORT
            ).show()

            // 🔙 Возвращаемся назад к списку
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ARG_PLAN_ID = "planId"
    }
}
