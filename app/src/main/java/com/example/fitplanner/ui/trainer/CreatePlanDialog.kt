package com.example.fitplanner.ui.trainer

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AdapterView
import com.example.fitplanner.data.db.AppDatabase
import com.example.fitplanner.data.db.entities.TrainingPlanEntity
import com.example.fitplanner.data.db.entities.UserEntity
import com.example.fitplanner.databinding.DialogCreatePlanBinding
import kotlinx.coroutines.runBlocking
import java.util.*

class CreatePlanDialog(
    context: Context,
    private val trainerId: Long,
    private val onCreate: (plan: TrainingPlanEntity) -> Unit
) : Dialog(context) {

    private lateinit var binding: DialogCreatePlanBinding
    private val db by lazy { AppDatabase.getInstance(context) }

    private var selectedAthlete: UserEntity? = null
    private var selectedDeadline: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogCreatePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(true)

        // Загружаем атлетов тренера
        val athletes = runBlocking { db.userDao().getAthletesByTrainer(trainerId) }
        val names = athletes.map { it.name }
        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerAthlete.adapter = adapter

        binding.spinnerAthlete.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedAthlete = athletes[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedAthlete = null
            }
        }

        // Выбор даты
        binding.btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDeadline = calendar.timeInMillis
                    binding.tvSelectedDate.text = "До: ${dayOfMonth}.${month+1}.$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Создание плана
        binding.btnCreate.setOnClickListener {
            val desc = binding.etDescription.text.toString()
            val athlete = selectedAthlete
            if (athlete != null && desc.isNotBlank()) {
                val plan = TrainingPlanEntity(
                    id = 0L,
                    athleteId = athlete.id,
                    trainerId = trainerId,
                    title = "План тренировки",
                    description = desc,
                    trainingDate = selectedDeadline,
                    isCompleted = false
                )
                onCreate(plan)
                dismiss()
            } else {
                binding.etDescription.error = "Заполните все поля"
            }
        }
    }
}
