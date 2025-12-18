package com.example.fitplanner.ui.trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.TrainingPlanEntity
import com.example.fitplanner.databinding.FragmentCreateTrainingPlanBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class CreateTrainingPlanFragment : Fragment() {

    private var _binding: FragmentCreateTrainingPlanBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private var athleteId: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        athleteId = requireArguments().getLong(ARG_ATHLETE_ID)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateTrainingPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val trainerId = SessionManager(requireContext()).userId

        binding.btnSave.setOnClickListener {
            lifecycleScope.launch {
                db.trainingPlanDao().insert(
                    TrainingPlanEntity(
                        athleteId = athleteId,
                        trainerId = trainerId,
                        title = binding.etTitle.text.toString(),
                        description = binding.etDescription.text.toString(),
                        trainingDate = System.currentTimeMillis()
                    )
                )
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ATHLETE_ID = "athlete_id"

        fun newInstance(id: Long) =
            CreateTrainingPlanFragment().apply {
                arguments = Bundle().apply {
                    putLong(ARG_ATHLETE_ID, id)
                }
            }
    }
}
