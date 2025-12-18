package com.example.fitplanner.ui.trainer

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.databinding.FragmentTrainerStatsBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class TrainerStatsFragment : Fragment() {

    private var _binding: FragmentTrainerStatsBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trainerId = SessionManager(requireContext()).userId

        lifecycleScope.launch {
            val plans = db.trainingPlanDao().getByTrainer(trainerId)
            binding.tvTotalPlans.text = "Всего планов: ${plans.size}"
            binding.tvCompleted.text =
                "Выполнено: ${plans.count { it.isCompleted }}"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
