package com.example.fitplanner.ui.trainer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.appcompat.widget.SearchView
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.databinding.FragmentTrainerAthletesBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class TrainerAthletesFragment : Fragment() {

    private var _binding: FragmentTrainerAthletesBinding? = null
    private val binding get() = _binding!!

    private val db by lazy { AppDatabase.getInstance(requireContext()) }
    private val session by lazy { SessionManager(requireContext()) }

    private lateinit var adapter: TrainerAthletesAdapter
    private var athletesList: List<com.example.fitplanner.data.db.entities.UserEntity> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainerAthletesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TrainerAthletesAdapter(listOf()) { athlete ->
            lifecycleScope.launch {
                val count = db.trainingPlanDao().countByAthlete(athlete.id)
                Toast.makeText(requireContext(),
                    "${athlete.name} выполнил $count тренировок",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.recyclerAthletes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAthletes.adapter = adapter

        loadAthletes()

        // Поиск
        binding.searchAthletes.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                val filtered = athletesList.filter {
                    it.name.contains(newText ?: "", ignoreCase = true)
                }
                adapter.updateList(filtered)
                return true
            }
        })
    }

    private fun loadAthletes() {
        lifecycleScope.launch {
            athletesList = db.userDao().getAthletesByTrainer(session.userId)
            adapter.updateList(athletesList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
