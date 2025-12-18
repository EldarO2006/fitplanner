package com.example.fitplanner.ui.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.databinding.FragmentAdminAssignBinding
import kotlinx.coroutines.launch

class AdminAssignFragment : Fragment() {

    private var _binding: FragmentAdminAssignBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }

    private var selectedAthlete: UserEntity? = null
    private lateinit var adapter: AthleteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminAssignBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        adapter = AthleteAdapter { athlete ->
            selectedAthlete = athlete
            Toast.makeText(
                requireContext(),
                "Выбран атлет: ${athlete.name}",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.recyclerAthletes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAthletes.adapter = adapter

        loadAthletes()
        setupTrainersSpinner()
    }

    private fun loadAthletes() {
        lifecycleScope.launch {
            val athletes = db.userDao().getAthletesWithoutTrainer()
            adapter.submitList(athletes)
        }
    }

    private fun setupTrainersSpinner() {
        lifecycleScope.launch {
            val trainers = db.userDao().getAllTrainers()

            val spinnerAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                trainers.map { it.name }
            )
            spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
            )

            binding.spinnerTrainers.adapter = spinnerAdapter

            binding.spinnerTrainers.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {

                    override fun onItemSelected(
                        parent: AdapterView<*>,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        val athlete = selectedAthlete ?: return
                        val trainer = trainers[position]

                        lifecycleScope.launch {
                            db.userDao().assignTrainer(
                                athleteId = athlete.id,
                                trainerId = trainer.id
                            )

                            Toast.makeText(
                                requireContext(),
                                "Тренер ${trainer.name} назначен ${athlete.name}",
                                Toast.LENGTH_LONG
                            ).show()

                            selectedAthlete = null
                            loadAthletes()
                        }
                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {}
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
