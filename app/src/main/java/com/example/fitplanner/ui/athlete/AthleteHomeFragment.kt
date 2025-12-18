package com.example.fitplanner.ui.athlete
import android.content.Intent
import com.example.fitplanner.ui.auth.LoginActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.databinding.FragmentAthleteHomeBinding
import com.example.fitplanner.utils.SessionManager
import kotlinx.coroutines.launch

class AthleteHomeFragment : Fragment() {

    private var _binding: FragmentAthleteHomeBinding? = null
    private val binding get() = _binding!!
    private fun logout() {
        SessionManager(requireContext()).clear()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private lateinit var session: SessionManager
    private val db by lazy { AppDatabase.getInstance(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAthleteHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = SessionManager(requireContext())

        loadUser()

        binding.btnUpdateWeight.setOnClickListener {
            updateWeight()
        }
        binding.btnLogout.setOnClickListener {
            logout()
        }

    }

    private fun loadUser() {
        lifecycleScope.launch {
            val user = db.userDao().getById(session.userId)
                ?: return@launch

            binding.tvName.text = user.name
            binding.tvEmail.text = user.email
            binding.tvHeight.text = "${user.height ?: "-"} см"
            binding.tvWeight.text = "${user.weight ?: "-"} кг"
        }
    }

    private fun updateWeight() {
        val newWeightText = binding.etNewWeight.text.toString()

        if (newWeightText.isBlank()) {
            Toast.makeText(requireContext(), "Введите вес", Toast.LENGTH_SHORT).show()
            return
        }

        val newWeight = newWeightText.toFloatOrNull()
        if (newWeight == null) {
            Toast.makeText(requireContext(), "Некорректный вес", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            db.userDao().updateWeight(
                userId = session.userId,
                weight = newWeight
            )

            binding.tvWeight.text = "$newWeight кг"
            binding.etNewWeight.text?.clear()

            Toast.makeText(requireContext(), "Вес обновлён", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
